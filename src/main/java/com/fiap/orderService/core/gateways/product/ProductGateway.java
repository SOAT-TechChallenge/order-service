package com.fiap.orderService.core.gateways.product;

import java.util.List;
import java.util.UUID;

import com.fiap.orderService.core.domain.entities.Product;
import com.fiap.orderService.core.domain.enums.Category;

public interface ProductGateway {

    List<Category> listAvailableCategorys();

    Product findById(UUID id);
}
