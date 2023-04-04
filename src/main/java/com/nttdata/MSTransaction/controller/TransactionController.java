package com.nttdata.MSTransaction.controller;
import com.nttdata.MSTransaction.model.Movements;
import com.nttdata.MSTransaction.service.TransactionService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/deposit/{id}")
    public Mono<Movements> deposit(@PathVariable("id") String idProduct,
                                   @RequestParam Double amount) {

        return transactionService.depositIntoAccount(idProduct, amount);

    }

    @PostMapping("/cashout/{id}")
    public Mono<Movements> cashout(@PathVariable("id") String idProduct,
                                  @RequestParam Double amount) {

        return transactionService.cashoutFromAccount(idProduct, amount);

    }

    @PostMapping("/transfer/{idFrom}/{idTo}")
    public Mono<Movements> transferTo(@PathVariable("idFrom") String idProductFrom,
                                    @PathVariable("idTo") String idProductTo,
                                    @RequestParam Double amount) {

        return transactionService.transferToAccount(idProductFrom, idProductTo, amount);

    }

}