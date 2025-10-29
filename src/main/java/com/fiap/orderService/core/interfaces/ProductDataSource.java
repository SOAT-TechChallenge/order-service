package com.fiap.orderService.core.interfaces;

import java.util.List;
import java.util.UUID;

import com.fiap.orderService.core.application.dto.ProductDTO;
import com.fiap.orderService.core.domain.enums.Category;

public interface ProductDataSource {

    List<Category> listAvailableCategorys();

    ProductDTO findById(UUID id);
}
