package com.nttdata.MSTransaction.service;

import com.nttdata.MSTransaction.model.Account;
import com.nttdata.MSTransaction.service.util.RestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.TEXT_EVENT_STREAM;

@Service
public class AccountServiceImpl {
    @Value("${backend.eureka.account.get-by-id}")
    private String urlGetAccountById;

    @Value("${backend.eureka.account.update}")
    private String urlUpdateAccount;

    @Autowired
    @Qualifier("wcLoadBalanced")
    private WebClient.Builder webClientBuilder;

    public Mono<Account> getAccountById(String id) {
        return webClientBuilder
                .clientConnector(RestUtils.getDefaultClientConnector())
                .build()
                .get()
                .uri(urlGetAccountById,id)
                .retrieve()
                .bodyToMono(Account.class)
                ;
    }

    public void updateAccount(Account a) {
        webClientBuilder
                .clientConnector(RestUtils.getDefaultClientConnector())
                .build()
                .put()
                .uri(urlUpdateAccount)
                .body(Mono.just(a),Account.class)
                .retrieve()
                .bodyToMono(Account.class)
                .subscribe();
    }
}
