package com.fiap.orderService._webApi.data.persistence.external;

import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fiap.orderService.core.application.dto.AttendantDTO;
import com.fiap.orderService.core.domain.exceptions.EntityNotFoundException;
import com.fiap.orderService.core.interfaces.AttendantDataSource;

@Component
public class AttendantDataSourceImpl implements AttendantDataSource {

    private final String registrationUrl;
    private final RestTemplate restTemplate;

    public AttendantDataSourceImpl(
            RestTemplate restTemplate,
            @Value("${external.services.registration.url}") String registrationUrl) {
        this.restTemplate = restTemplate;
        this.registrationUrl = registrationUrl;
    }

    @Override
    public AttendantDTO findById(UUID id) {
        String url = registrationUrl + "/products/find-by-id/{id}";

        try {
            AttendantDTO attendant = restTemplate.getForObject(url, AttendantDTO.class, id);
            return Objects.requireNonNull(attendant, "Atendente não encontrado.");

            // return new AttendantDTO(
            //         id,
            //         "Atendente de Teste",
            //         "teste@teste.com",
            //         "46765845801"
            // );

        } catch (HttpClientErrorException.NotFound e) {
            throw new EntityNotFoundException("Atendnete com ID " + id + " não encontrado.");

        } catch (Exception e) {
            throw new RuntimeException("Falha na comunicação com o Serviço de Registro", e);
        }
    }
}
