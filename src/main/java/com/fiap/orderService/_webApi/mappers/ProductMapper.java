package com.fiap.orderService._webApi.mappers;

import com.fiap.orderService.core.application.dto.ProductDTO;
import com.fiap.orderService.core.domain.entities.Product;

public class ProductMapper {

    public static Product toDomain(ProductDTO dto) {
        if (dto == null) {
            return null;
        }

        return Product.build(
                dto.id(),
                dto.name(),
                dto.price(),
                dto.category()
        );
    }
}
