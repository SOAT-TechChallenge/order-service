package com.fiap.orderService;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    // Simula a conexão com o Mongo para o driver não falhar na inicialização
    "spring.data.mongodb.uri=mongodb://localhost:27017/order-test-db",
    
    // Simula a URL do serviço de registro usada nos DataSources (@Value)
    "external.services.registration.url=http://localhost:8080",
    
    // Simula a propriedade de e-mail usada no Controller (@Value)
    "app.mail.from=noreply@test.com"
})
class OrderServiceApplicationTest {

    @Test
    void contextLoads() {
        // Verifica se o Spring consegue subir o contexto completo sem erros de dependência
    }

}