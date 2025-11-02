package com.fiap.orderService._webApi.data.persistence.repositorys;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.fiap.orderService._webApi.data.persistence.entities.OrderDocument;
import com.fiap.orderService.core.application.dto.OrderWithStatusAndWaitMinutesProjection;

@Repository
public interface OrderRepository extends MongoRepository<OrderDocument, UUID> {

    Optional<OrderDocument> findByPaymentId(String paymentId);

    List<OrderDocument> findAllByCustomerIdOrderByDateDesc(UUID customerId);

    List<OrderDocument> findAllByDateBetween(LocalDateTime startDt, LocalDateTime endDt);

    @Aggregation(pipeline = {
        """
        { "$match": {
            "currentStatus": { "$in": ?0 },
            "$expr": {
                "$and": [
                    { "$eq": [
                        { "$dateToString": { "format": "%Y-%m-%d", "date": "$lastUpdateDate" } },
                        { "$dateToString": { "format": "%Y-%m-%d", "date": "$$NOW" } }
                    ] },
                    { "$or": [
                        { "$ne": ["$currentStatus", "FINALIZADO"] },
                        { "$lte": [
                            { "$dateDiff": { 
                                "startDate": "$lastUpdateDate",
                                "endDate": "$$NOW",
                                "unit": "minute"
                            } },
                            ?1
                        ] }
                    ] }
                ]
            }
        } }
        """,
        """
        { "$addFields": {
            "waitTimeMinutes": { "$dateDiff": {
                "startDate": "$date",
                "endDate": "$$NOW",
                "unit": "minute"
            } }
        } }
        """,
        """
        { "$addFields": {
            "statusOrder": { "$switch": {
                "branches": [
                    { "case": { "$eq": ["$currentStatus", "PRONTO"] }, "then": 1 },
                    { "case": { "$eq": ["$currentStatus", "EM_PREPARACAO"] }, "then": 2 },
                    { "case": { "$eq": ["$currentStatus", "RECEBIDO"] }, "then": 3 } 
                ],
                "default": 4
            } }
        } }
        """,
        """
        { "$sort": { "statusOrder": 1, "lastUpdateDate": 1 } }
        """,
        """
        { "$project": {
            "_id": 0,
            "orderId": "$_id",
            "status": "$currentStatus",
            "statusDt": "$lastUpdateDate",
            "customerId": "$customerId",
            "customerEmail": "$customerEmail",
            "orderDt": "$date",
            "waitTimeMinutes": "$waitTimeMinutes"
        } }
        """
    })
    List<OrderWithStatusAndWaitMinutesProjection> findTodayOrders(
        List<String> statusList,
        int finalizedMinutes
    );
}