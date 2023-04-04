package com.nttdata.MSTransaction.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document("transaction")
public class Transaction {
    @Id
    private String id;
    private String idAccountOrigin;
    private String idAccountDestiny;
    private Double amount;
    //@DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date date;
    private TransactionType transactionType;
}
