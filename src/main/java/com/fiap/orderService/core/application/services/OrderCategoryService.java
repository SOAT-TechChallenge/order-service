package com.fiap.orderService.core.application.services;



import java.util.Arrays;
import java.util.List;

import com.fiap.orderService.core.domain.entities.OrderItem;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.exceptions.WrongCategoryOrderException;
import com.fiap.orderService.core.gateways.product.ProductGateway;

public class OrderCategoryService {

    private final ProductGateway productGateway;

    public OrderCategoryService(ProductGateway productGateway) {
        this.productGateway = productGateway;
    }

    public void validateCategoryListOrder(List<OrderItem> currentItems, OrderItem newItem) {
        List<Category> availableCategories = productGateway.listAvailableCategorys();

        List<Category> orderedCategories = Arrays.stream(Category.values())
                .filter(availableCategories::contains)
                .toList();

        Category newCategory = newItem.getCategory();
        int newIndex = orderedCategories.indexOf(newCategory);

        if (newIndex == -1) {
            throw new WrongCategoryOrderException();
        }

        if (currentItems.isEmpty()) {
            return;
        }

        int maxUsedIndex = currentItems.stream()
                .map(OrderItem::getCategory)
                .mapToInt(orderedCategories::indexOf)
                .max()
                .orElse(-1);

        if (newIndex < maxUsedIndex) {
            throw new WrongCategoryOrderException();
        }
    }
}
