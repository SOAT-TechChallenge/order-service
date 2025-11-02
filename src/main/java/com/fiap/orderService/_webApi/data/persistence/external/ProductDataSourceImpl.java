package com.fiap.orderService._webApi.data.persistence.external;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fiap.orderService.core.application.dto.ProductDTO;
import com.fiap.orderService.core.domain.enums.Category;
import com.fiap.orderService.core.domain.exceptions.EntityNotFoundException;
import com.fiap.orderService.core.interfaces.ProductDataSource;

@Component
public class ProductDataSourceImpl implements ProductDataSource {

    private final String registrationUrl;
    private final RestTemplate restTemplate;

    public ProductDataSourceImpl(
            RestTemplate restTemplate,
            @Value("${external.services.registration.url}") String registrationUrl) {
        this.restTemplate = restTemplate;
        this.registrationUrl = registrationUrl;
    }

    @Override
    public List<Category> listAvailableCategorys() {
        String url = registrationUrl + "/api/product/list-available-categories";

        try {
            Category[] categoriesArray = restTemplate.getForObject(url, Category[].class);
            return categoriesArray != null ? Arrays.asList(categoriesArray) : Collections.emptyList();

        } catch (HttpClientErrorException | HttpServerErrorException e) {
            throw new IllegalStateException("Erro ao buscar categorias: " + e.getMessage());
        }
    }

    @Override
    public ProductDTO findById(UUID id) {
        String url = registrationUrl + "/api/product/find-by-id/{id}";

        try {
            ProductDTO product = restTemplate.getForObject(url, ProductDTO.class, id);
            return Objects.requireNonNull(product, "Produto não encontrado.");

        } catch (HttpClientErrorException.NotFound e) {
            throw new EntityNotFoundException("Produto com ID " + id + " não encontrado.");

        } catch (Exception e) {
            throw new RuntimeException("Falha na comunicação com o Serviço de Registro", e);
        }
    }
}
