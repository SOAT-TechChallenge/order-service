package com.fiap.orderService.core.gateways.product;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.fiap.orderService._webApi.mappers.ProductMapper;
import com.fiap.orderService.core.domain.entities.Product;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.interfaces.ProductDataSource;

public class ProductGatewayImpl implements ProductGateway {

    private final ProductDataSource dataSource;

    public ProductGatewayImpl(ProductDataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Category> listAvailableCategorys() {
        List<Category> availableCategories = dataSource.listAvailableCategorys();

        return Arrays.stream(Category.values())
                .filter(availableCategories::contains)
                .toList();
    }

    @Override
    public Product findById(UUID id) {
        return ProductMapper.toDomain(dataSource.findById(id));
    }

}
