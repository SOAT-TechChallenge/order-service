package com.fiap.orderService._webApi.data.persistence.external;

import com.fiap.orderService.core.interfaces.ProductDataSource;
import com.fiap.orderService.core.application.dto.ProductDTO;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.exceptions.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.Objects;

@Component
public class ProductDataSourceImpl implements ProductDataSource {

    private final String productBaseUrl;
    private final RestTemplate restTemplate;

    public ProductDataSourceImpl(
            RestTemplate restTemplate,
            @Value("${external.services.product.url}") String productBaseUrl) {
        this.restTemplate = restTemplate;
        this.productBaseUrl = productBaseUrl;
    }

    @Override
    public List<Category> listAvailableCategorys() {
        String url = productBaseUrl + "/products/list-available-categories";

        try {
            //TODO DESCOMENTAR 
            // Category[] categoriesArray = restTemplate.getForObject(url, Category[].class);
            // return categoriesArray != null ? Arrays.asList(categoriesArray) : Collections.emptyList();

            List<Category> categoriesArray = Arrays.asList(
                    Category.LANCHE,
                    Category.ACOMPANHAMENTO,
                    Category.BEBIDA,
                    Category.SOBREMESA
            );

            return categoriesArray;

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalStateException("Erro ao buscar categorias: " + e.getMessage());
        }
    }

    @Override
    public ProductDTO findById(UUID id) {
        String url = productBaseUrl + "/products/find-by-id/{id}";

        try {
            //TODO DESCOMENTAR
            // ProductDTO product = restTemplate.getForObject(url, ProductDTO.class, id);
            // return Objects.requireNonNull(product, "Produto não encontrado.");

            return new ProductDTO(
                    id,
                    "Hamburguer Mock",
                    new BigDecimal("25.00"),
                    Category.LANCHE
            );

        } catch (HttpClientErrorException.NotFound e) {
            throw new EntityNotFoundException("Produto com ID " + id + " não encontrado.");

        } catch (Exception e) {
            throw new RuntimeException("Falha na comunicação com o Serviço de Produto", e);
        }
    }
}
