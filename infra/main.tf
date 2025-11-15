terraform {
  required_version = ">= 1.0"
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

# Data sources para VPC e subnets existentes
data "aws_vpc" "existing" {
  filter {
    name   = "tag:Name"
    values = ["techchallenge-vpc"]
  }
}

data "aws_subnets" "public" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.existing.id]
  }

  filter {
    name   = "tag:Name"
   values = ["techchallenge-vpc-public-us-east-1a", "techchallenge-vpc-public-us-east-1b"]
  }
}

data "aws_subnets" "private" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.existing.id]
  }

  filter {
    name   = "tag:Name"
    values = ["techchallenge-vpc-private-us-east-1a", "techchallenge-vpc-private-us-east-1b"]
  }
}

data "aws_iam_role" "existing_ecs_execution_role" {
  name = "LabRole"
}

# Security Group para o ALB
resource "aws_security_group" "alb_sg" {
  name        = "order-service-alb-sg"
  description = "Security group for ALB"
  vpc_id      = data.aws_vpc.existing.id

  ingress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "order-service-alb-sg"
  }
}

# Security Group para ECS
resource "aws_security_group" "ecs_sg" {
  name        = "order-service-ecs-sg"
  description = "Security group for ECS tasks"
  vpc_id      = data.aws_vpc.existing.id

  ingress {
    from_port       = 8080
    to_port         = 8080
    protocol        = "tcp"
    security_groups = [aws_security_group.alb_sg.id]
  }

  egress {
    from_port   = 80
    to_port     = 80
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"] 
  }

  egress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  tags = {
    Name = "order-service-ecs-sg"
  }
}

# ALB
resource "aws_lb" "order_alb" {
  name               = "order-service-alb"
  internal           = false
  load_balancer_type = "application"
  security_groups    = [aws_security_group.alb_sg.id]
  subnets            = data.aws_subnets.public.ids

  enable_deletion_protection = false

  tags = {
    Name = "order-service-alb"
  }
}

# Target Group
resource "aws_lb_target_group" "order_tg" {
  name        = "order-tg"
  port        = 8080
  protocol    = "HTTP"
  vpc_id      = data.aws_vpc.existing.id
  target_type = "ip"

  tags = {
    Name = "order-tg"
  }
}

# Listener do ALB
resource "aws_lb_listener" "order_listener" {
  load_balancer_arn = aws_lb.order_alb.arn
  port              = "80"
  protocol          = "HTTP"

  default_action {
    type             = "forward"
    target_group_arn = aws_lb_target_group.order_tg.arn
  }
}

# ECS Cluster
resource "aws_ecs_cluster" "order_cluster" {
  name = "order-cluster"

  setting {
    name  = "containerInsights"
    value = "disabled"
  }

  tags = {
    Name = "order-cluster"
  }
}

# ECS Task Definition
resource "aws_ecs_task_definition" "order_task" {
  family                   = "order-service"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = 512
  memory                   = 1024

  execution_role_arn = data.aws_iam_role.existing_ecs_execution_role.arn

  container_definitions = jsonencode([{
    name  = "order-service"
    image = "breno091073/order-service:latest"
    portMappings = [{
      containerPort = 8080
      hostPort      = 8080
      protocol      = "tcp"
    }]
    essential = true

    environment = [
      {
        name  = "SERVER_PORT"
        value = "8080"
      },
      {
        name  = "SPRING_DATA_MONGODB_URI"
        value = "mongodb://root:123456@localhost:27017/techchallenge_order_service?retryWrites=true&w=majority&authSource=admin"
      },
      {
        name  = "EXTERNAL_SERVICES_REGISTRATION_URL"
        value = "http://registration-service-alb-1122775453.us-east-1.elb.amazonaws.com"
      }
    ]
    logConfiguration = {
      logDriver = "awslogs",
      options = {
        "awslogs-group"         = aws_cloudwatch_log_group.order_logs.name,
        "awslogs-region"        = "us-east-1",
        "awslogs-stream-prefix" = "order-service"
      }
    }
    },
    # ----------------------------------------------
    # 2. Contêiner do MongoDB (O Banco de Dados)
    # ----------------------------------------------
    {
      name  = "mongodb"
      image = "mongo:latest"
      portMappings = [{
        containerPort = 27017
        hostPort      = 27017
        protocol      = "tcp"
      }]
      essential = true
      environment = [
        { name = "MONGO_INITDB_ROOT_USERNAME", value = "root" },
        { name = "MONGO_INITDB_ROOT_PASSWORD", value = "123456" },
        { name = "MONGO_INITDB_DATABASE", value = "techchallenge_order_service" }
      ]
      logConfiguration = {
        logDriver = "awslogs",
        options = {
          "awslogs-group"         = aws_cloudwatch_log_group.order_logs.name,
          "awslogs-region"        = "us-east-1",
          "awslogs-stream-prefix" = "mongodb"
        }
      }
    }
  ])


  tags = {
    Name = "order-service-task"
  }
}

# ECS Service
resource "aws_ecs_service" "order_service" {
  name            = "order-service"
  cluster         = aws_ecs_cluster.order_cluster.id
  task_definition = aws_ecs_task_definition.order_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"

  force_new_deployment = true

  network_configuration {
    security_groups  = [aws_security_group.ecs_sg.id]
    subnets          = data.aws_subnets.private.ids
    assign_public_ip = false
  }

  load_balancer {
    target_group_arn = aws_lb_target_group.order_tg.arn
    container_name   = "order-service"
    container_port   = 8080
  }

  health_check_grace_period_seconds = 180

  depends_on = [aws_lb_listener.order_listener]

  tags = {
    Name = "order-service"
  }
}

# CloudWatch Log Group
resource "aws_cloudwatch_log_group" "order_logs" {
  name              = "/ecs/order-service"
  retention_in_days = 7

  tags = {
    Name = "order-service-logs"
  }
}
