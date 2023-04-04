package com.nttdata.MSTransaction.proxy;

import com.nttdata.MSTransaction.model.Account;
import com.nttdata.MSTransaction.model.Movements;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class TransactionProxy {
    private final WebClient.Builder webClientBuilder = WebClient.builder();

    public Mono<Account> getAccount(String idProduct){
        return webClientBuilder.build()
                .get()
                .uri("http://localhost:9020/account/" + idProduct)
                .retrieve()
                .bodyToMono(Account.class);
    }

    public Mono<Account> updateAccount(Account account){
        return webClientBuilder.build()
                .put()
                .uri("http://localhost:9020/account/")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(account))
                .retrieve()
                .bodyToMono(Account.class);
    }

    public Mono<Movements> saveMovements(Movements movement) {
        return webClientBuilder.build()
                .post()
                .uri("http://localhost:9020/movements")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(movement))
                .retrieve()
                .bodyToMono(Movements.class);
    }
}
