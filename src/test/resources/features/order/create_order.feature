# language: pt
Funcionalidade: Criar Pedido
  Como um cliente
  Eu quero criar um pedido
  Para que eu possa fazer uma compra

  Cenário: Criar pedido com sucesso com um item
    Dado que existe um produto com id "550e8400-e29b-41d4-a716-446655440000" e nome "Hambúrguer" e preço "25.50" e categoria "LANCHE"
    E que existe um cliente com id "660e8400-e29b-41d4-a716-446655440000" e email "cliente@test.com"
    Quando eu criar um pedido com customerId "660e8400-e29b-41d4-a716-446655440000", customerEmail "cliente@test.com" e items:
      | productId                                | quantity |
      | 550e8400-e29b-41d4-a716-446655440000    | 2        |
    Então o pedido deve ser criado com sucesso
    E o pedido deve ter o customerId "660e8400-e29b-41d4-a716-446655440000"
    E o pedido deve ter o customerEmail "cliente@test.com"
    E o pedido deve ter o status "PAGAMENTO_PENDENTE"
    E o pedido deve ter 1 item(s)
    E o item 1 deve ter productId "550e8400-e29b-41d4-a716-446655440000"
    E o item 1 deve ter quantidade 2
    E o gateway deve buscar os produtos antes de salvar
    E o gateway deve salvar o pedido
    E o serviço de categoria deve validar a ordem dos itens

  Cenário: Criar pedido com múltiplos itens
    Dado que existe um produto com id "550e8400-e29b-41d4-a716-446655440000" e nome "Hambúrguer" e preço "25.50" e categoria "LANCHE"
    E que existe um produto com id "550e8400-e29b-41d4-a716-446655440001" e nome "Batata Frita" e preço "12.00" e categoria "ACOMPANHAMENTO"
    E que existe um produto com id "550e8400-e29b-41d4-a716-446655440002" e nome "Refrigerante" e preço "8.50" e categoria "BEBIDA"
    E que existe um cliente com id "660e8400-e29b-41d4-a716-446655440000" e email "cliente@test.com"
    Quando eu criar um pedido com customerId "660e8400-e29b-41d4-a716-446655440000", customerEmail "cliente@test.com" e items:
      | productId                                | quantity |
      | 550e8400-e29b-41d4-a716-446655440000    | 1        |
      | 550e8400-e29b-41d4-a716-446655440001    | 2        |
      | 550e8400-e29b-41d4-a716-446655440002    | 1        |
    Então o pedido deve ser criado com sucesso
    E o pedido deve ter o status "PAGAMENTO_PENDENTE"
    E o pedido deve ter 3 item(s)
    E o gateway deve buscar os produtos antes de salvar
    E o gateway deve salvar o pedido

  Cenário: Mesclar itens duplicados no pedido
    Dado que existe um produto com id "550e8400-e29b-41d4-a716-446655440000" e nome "Hambúrguer" e preço "25.50" e categoria "LANCHE"
    E que existe um cliente com id "660e8400-e29b-41d4-a716-446655440000" e email "cliente@test.com"
    Quando eu criar um pedido com customerId "660e8400-e29b-41d4-a716-446655440000", customerEmail "cliente@test.com" e items:
      | productId                                | quantity |
      | 550e8400-e29b-41d4-a716-446655440000    | 2        |
      | 550e8400-e29b-41d4-a716-446655440000    | 3        |
    Então o pedido deve ser criado com sucesso
    E o pedido deve ter o status "PAGAMENTO_PENDENTE"
    E o pedido deve ter 1 item(s)
    E os itens duplicados devem ser mesclados
    E a quantidade total do produto "550e8400-e29b-41d4-a716-446655440000" deve ser 5
    E o gateway deve salvar o pedido

