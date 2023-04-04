package com.nttdata.MSTransaction.model;

import lombok.Data;

import java.util.Date;
@Data
public class Movements {
    private String id;
    private String idProduct;
    private String type;
    private Double amount;
    private String idThirdPartyProduct;
    private Date date;
}
