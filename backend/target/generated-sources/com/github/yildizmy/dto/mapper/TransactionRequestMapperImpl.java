package com.github.yildizmy.dto.mapper;

import com.github.yildizmy.domain.entity.Transaction;
import com.github.yildizmy.dto.request.TransactionRequest;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-03T13:14:24+0000",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
)
@Component
public class TransactionRequestMapperImpl extends TransactionRequestMapper {

    @Override
    public Transaction toTransaction(TransactionRequest dto) {
        if ( dto == null ) {
            return null;
        }

        Transaction transaction = new Transaction();

        if ( dto.getCreatedAt() != null ) {
            transaction.setCreatedAt( dto.getCreatedAt() );
        }
        else {
            transaction.setCreatedAt( java.time.Instant.now() );
        }
        transaction.setAmount( dto.getAmount() );
        transaction.setDescription( dto.getDescription() );
        transaction.setId( dto.getId() );

        transaction.setStatus( com.github.yildizmy.domain.enums.Status.SUCCESS );
        transaction.setReferenceNumber( java.util.UUID.randomUUID() );

        setToEntityFields( transaction, dto );

        return transaction;
    }

    @Override
    public TransactionRequest toTransactionRequest(Transaction entity) {
        if ( entity == null ) {
            return null;
        }

        TransactionRequest transactionRequest = new TransactionRequest();

        transactionRequest.setAmount( entity.getAmount() );
        transactionRequest.setCreatedAt( entity.getCreatedAt() );
        transactionRequest.setDescription( entity.getDescription() );
        transactionRequest.setId( entity.getId() );
        transactionRequest.setReferenceNumber( entity.getReferenceNumber() );
        transactionRequest.setStatus( entity.getStatus() );

        return transactionRequest;
    }
}
