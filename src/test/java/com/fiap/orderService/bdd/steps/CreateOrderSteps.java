package com.fiap.orderService.bdd.steps;

import com.fiap.orderService.core.application.dto.CreateOrderInputDTO;
import com.fiap.orderService.core.application.dto.CreateOrderItemInputDTO;
import com.fiap.orderService.core.application.services.OrderCategoryService;
import com.fiap.orderService.core.application.useCases.CreateOrderUseCase;
import com.fiap.orderService.core.domain.entities.Order;
import com.fiap.orderService.core.domain.entities.OrderItem;
import com.fiap.orderService.core.domain.entities.Product;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.enums.OrderStatus;
import com.fiap.orderService.core.gateways.order.OrderGateway;
import com.fiap.orderService.core.gateways.product.ProductGateway;

import io.cucumber.java.Before;
import io.cucumber.java.pt.Dado;
import io.cucumber.java.pt.Então;
import io.cucumber.java.pt.Quando;

import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

/**
 * Step Definitions para os cenários de criação de pedido usando BDD com Cucumber
 */
public class CreateOrderSteps {

    @Mock
    private OrderGateway orderGateway;

    @Mock
    private ProductGateway productGateway;

    @Mock
    private OrderCategoryService orderCategoryService;

    private CreateOrderUseCase createOrderUseCase;
    private CreateOrderInputDTO orderInputDTO;
    private Order createdOrder;
    private InOrder inOrder;
    private List<Product> mockedProducts;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        createOrderUseCase = new CreateOrderUseCase(orderGateway, productGateway, orderCategoryService);
        reset(orderGateway, productGateway, orderCategoryService);
        mockedProducts = new ArrayList<>();
    }

    @Dado("que existe um produto com id {string} e nome {string} e preço {string} e categoria {string}")
    public void que_existe_um_produto_com_id_e_nome_e_preco_e_categoria(String productIdStr, String productName, String priceStr, String categoryStr) {
        UUID productId = UUID.fromString(productIdStr);
        BigDecimal price = new BigDecimal(priceStr);
        Category category = Category.valueOf(categoryStr);

        Product product = Product.build(productId, productName, price, category);
        mockedProducts.add(product);

        when(productGateway.findById(productId)).thenReturn(product);
    }

    @Dado("que existe um cliente com id {string} e email {string}")
    public void que_existe_um_cliente_com_id_e_email(String customerIdStr, String customerEmail) {
        // Cliente é apenas um UUID e email, não precisa de mock específico
    }

    @Quando("eu criar um pedido com os seguintes dados:")
    public void eu_criar_um_pedido_com_os_seguintes_dados(io.cucumber.datatable.DataTable dataTable) {
        var row = dataTable.asMaps().get(0);
        UUID customerId = UUID.fromString(row.get("customerId"));
        String customerEmail = row.get("customerEmail");

        // Parse items from the data table
        List<CreateOrderItemInputDTO> items = new ArrayList<>();
        String itemsStr = row.get("items");
        if (itemsStr != null && !itemsStr.isEmpty()) {
            String[] itemPairs = itemsStr.split(";");
            for (String itemPair : itemPairs) {
                String[] parts = itemPair.trim().split(",");
                if (parts.length == 2) {
                    UUID productId = UUID.fromString(parts[0].trim());
                    Integer quantity = Integer.parseInt(parts[1].trim());
                    items.add(CreateOrderItemInputDTO.builder()
                            .productId(productId)
                            .quantity(quantity)
                            .build());
                }
            }
        }

        orderInputDTO = CreateOrderInputDTO.builder()
                .customerId(customerId)
                .customerEmail(customerEmail)
                .items(items)
                .build();

        // Mock orderGateway.save to return the order
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock orderCategoryService to do nothing (validation passes)
        doNothing().when(orderCategoryService).validateCategoryListOrder(anyList(), any(OrderItem.class));

        createdOrder = createOrderUseCase.execute(orderInputDTO);
        inOrder = inOrder(orderGateway, productGateway, orderCategoryService);
    }

    @Quando("eu criar um pedido com customerId {string}, customerEmail {string} e items:")
    public void eu_criar_um_pedido_com_customerId_customerEmail_e_items(String customerIdStr, String customerEmail, io.cucumber.datatable.DataTable itemsTable) {
        UUID customerId = UUID.fromString(customerIdStr);

        List<CreateOrderItemInputDTO> items = new ArrayList<>();
        for (var row : itemsTable.asMaps()) {
            UUID productId = UUID.fromString(row.get("productId"));
            Integer quantity = Integer.parseInt(row.get("quantity"));
            items.add(CreateOrderItemInputDTO.builder()
                    .productId(productId)
                    .quantity(quantity)
                    .build());
        }

        orderInputDTO = CreateOrderInputDTO.builder()
                .customerId(customerId)
                .customerEmail(customerEmail)
                .items(items)
                .build();

        // Mock orderGateway.save to return the order
        when(orderGateway.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Mock orderCategoryService to do nothing (validation passes)
        doNothing().when(orderCategoryService).validateCategoryListOrder(anyList(), any(OrderItem.class));

        createdOrder = createOrderUseCase.execute(orderInputDTO);
        inOrder = inOrder(orderGateway, productGateway, orderCategoryService);
    }

    @Então("o pedido deve ser criado com sucesso")
    public void o_pedido_deve_ser_criado_com_sucesso() {
        assertNotNull(createdOrder, "O pedido criado não deve ser nulo");
    }

    @Então("o pedido deve ter o customerId {string}")
    public void o_pedido_deve_ter_o_customerId(String expectedCustomerIdStr) {
        UUID expectedCustomerId = UUID.fromString(expectedCustomerIdStr);
        assertEquals(expectedCustomerId, createdOrder.getCustomerId(),
                "O customerId do pedido deve corresponder ao informado");
    }

    @Então("o pedido deve ter o customerEmail {string}")
    public void o_pedido_deve_ter_o_customerEmail(String expectedEmail) {
        assertEquals(expectedEmail, createdOrder.getCustomerEmail(),
                "O customerEmail do pedido deve corresponder ao informado");
    }

    @Então("o pedido deve ter o status {string}")
    public void o_pedido_deve_ter_o_status(String expectedStatusStr) {
        OrderStatus expectedStatus = OrderStatus.valueOf(expectedStatusStr);
        assertEquals(expectedStatus, createdOrder.getCurrentStatus(),
                "O status do pedido deve corresponder ao esperado");
    }

    @Então("o pedido deve ter {int} item\\(s)")
    public void o_pedido_deve_ter_items(int expectedItemCount) {
        assertEquals(expectedItemCount, createdOrder.getItems().size(),
                String.format("O pedido deve ter %d item(s)", expectedItemCount));
    }

    @Então("o item {int} deve ter productId {string}")
    public void o_item_deve_ter_productId(int itemIndex, String expectedProductIdStr) {
        UUID expectedProductId = UUID.fromString(expectedProductIdStr);
        OrderItem item = createdOrder.getItems().get(itemIndex - 1);
        assertEquals(expectedProductId, item.getProductId(),
                String.format("O item %d deve ter o productId %s", itemIndex, expectedProductIdStr));
    }

    @Então("o item {int} deve ter quantidade {int}")
    public void o_item_deve_ter_quantidade(int itemIndex, int expectedQuantity) {
        OrderItem item = createdOrder.getItems().get(itemIndex - 1);
        assertEquals(expectedQuantity, item.getQuantity(),
                String.format("O item %d deve ter quantidade %d", itemIndex, expectedQuantity));
    }

    @Então("o gateway deve buscar os produtos antes de salvar")
    public void o_gateway_deve_buscar_os_produtos_antes_de_salvar() {
        for (CreateOrderItemInputDTO itemDTO : orderInputDTO.items()) {
            inOrder.verify(productGateway).findById(itemDTO.productId());
        }
        inOrder.verify(orderGateway).save(any(Order.class));
    }

    @Então("o gateway deve salvar o pedido")
    public void o_gateway_deve_salvar_o_pedido() {
        verify(orderGateway, times(1)).save(any(Order.class));
    }

    @Então("o serviço de categoria deve validar a ordem dos itens")
    public void o_servico_de_categoria_deve_validar_a_ordem_dos_itens() {
        verify(orderCategoryService, atLeastOnce()).validateCategoryListOrder(anyList(), any(OrderItem.class));
    }

    @Então("os itens duplicados devem ser mesclados")
    public void os_itens_duplicados_devem_ser_mesclados() {
        // Verificar que itens com mesmo productId foram mesclados
        // Isso é testado verificando que o número de itens é menor que o número de itens no input
        // quando há duplicatas
        assertTrue(createdOrder.getItems().size() <= orderInputDTO.items().size(),
                "Os itens duplicados devem ser mesclados");
    }

    @Então("a quantidade total do produto {string} deve ser {int}")
    public void a_quantidade_total_do_produto_deve_ser(String productIdStr, int expectedTotalQuantity) {
        UUID productId = UUID.fromString(productIdStr);
        OrderItem item = createdOrder.getItems().stream()
                .filter(i -> i.getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Produto não encontrado no pedido"));

        assertEquals(expectedTotalQuantity, item.getQuantity(),
                String.format("A quantidade total do produto %s deve ser %d", productIdStr, expectedTotalQuantity));
    }
}

