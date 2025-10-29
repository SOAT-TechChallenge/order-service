package com.fiap.orderService._webApi.data.persistence.repositorys;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.fiap.orderService._webApi.data.persistence.entities.OrderDocument;

@Repository
public interface OrderRepository extends MongoRepository<OrderDocument, UUID> {

    Optional<OrderDocument> findByPaymentId(String paymentId);

    List<OrderDocument> findAllByCustomerIdOrderByDateDesc(UUID customerId);

    List<OrderDocument> findAllByDateBetween(LocalDateTime startDt, LocalDateTime endDt);

    @Query(value = "{ 'currentStatus': { $in: ?0 }, 'lastUpdateDate': { $gte: ?1, $lte: ?2 } }",
            sort = "{ 'currentStatus': 1, 'lastUpdateDate': 1 }")
    List<OrderDocument> findTodayOrdersByStatusAndDate(
            List<String> statusList,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
}
