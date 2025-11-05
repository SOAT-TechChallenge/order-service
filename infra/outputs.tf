output "application_url" {
  description = "URL de acesso ao Application Load Balancer (ALB)."
  value       = "http://${aws_lb.order_alb.dns_name}"
}