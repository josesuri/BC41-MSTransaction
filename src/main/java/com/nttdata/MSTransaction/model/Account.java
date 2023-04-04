package com.nttdata.MSTransaction.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("account")
public class Account {
    @Id
    private String id;
    private String idCustomer;
    private Double balance;
    private Double maintenance;
    private Integer numberTransactions;
    private Integer maxTransactions;
    private String accountType;
    private Double commission;
}
