package com.nttdata.MSTransaction.service;

import com.nttdata.MSTransaction.model.Movements;
import reactor.core.publisher.Mono;

public interface TransactionService {
    public Mono<Movements> depositIntoAccount(String idAccount, Double amount);
    public Mono<Movements> cashoutFromAccount(String idAccount, Double amount);
    public Mono<Movements> transferToAccount(String idAccountFrom, String idAccountTo, Double amount);
}
