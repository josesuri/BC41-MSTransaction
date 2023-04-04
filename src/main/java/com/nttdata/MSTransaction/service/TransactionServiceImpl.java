package com.nttdata.MSTransaction.service;

import com.nttdata.MSTransaction.model.Account;
import com.nttdata.MSTransaction.model.Movements;
import com.nttdata.MSTransaction.proxy.TransactionProxy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private TransactionProxy TransactionProxy = new TransactionProxy();

    @Override
    public Mono<Movements> depositIntoAccount(String idAccount, Double amount) {

        return TransactionProxy.getAccount(idAccount)
                .flatMap(resp->checkMonthlyTransactions(resp, amount))
                .flatMap(resp->deposit(resp, amount))
                .flatMap(TransactionProxy::updateAccount)
                .flatMap(resp->saveMovements(idAccount, "deposit", amount, null));

    }

    @Override
    public Mono<Movements> cashoutFromAccount(String idAccount, Double amount) {
        return TransactionProxy.getAccount(idAccount)
                .flatMap(resp->checkBalance(resp, amount))
                .flatMap(resp->checkMonthlyTransactions(resp, amount))
                .flatMap(resp->cashout(resp, amount))
                .flatMap(TransactionProxy::updateAccount)
                .flatMap(resp->saveMovements(idAccount, "cashout", amount, null));

    }

    @Override
    public Mono<Movements> transferToAccount(String idAccountFrom, String idAccountTo, Double amount) {

        return TransactionProxy.getAccount(idAccountFrom)
                .flatMap(resp->checkBalance(resp, amount))
                .flatMap(resp->checkMonthlyTransactions(resp, amount))
                .flatMap(resp->makeTransaction(resp, idAccountTo, amount));
    }


    //TRANSACTIONS UTIL METHODS
    public Mono<Account> checkBalance(Account account, Double amount){
        return account.getBalance()>amount ? Mono.just(account)
                : Mono.error(()->new IllegalArgumentException("Not enough balance"));
    }

    public Mono<Account> deposit(Account account, Double amount){
        account.setBalance(account.getBalance()+amount);
        return Mono.just(account);
    }

    public Mono<Account> cashout(Account account, Double amount){
        account.setBalance(account.getBalance()-amount);
        return Mono.just(account);
    }

    public Mono<Account> checkMonthlyTransactions(Account account, Double amount){

        Integer movements = account.getMaxTransactions();

        if(movements>0) {
            account.setMaxTransactions(movements-1);
            return Mono.just(account);
        }else {
            if(account.getBalance()>amount+account.getCommission()) {
                account.setBalance(account.getBalance()-account.getCommission());
                saveMovements(account.getId(), "commission", account.getCommission(), null).subscribe();
                return Mono.just(account);
            }else {
                return Mono.error(() -> new IllegalArgumentException("Not enough balance"));
            }
        }
    }

    public Mono<Movements> makeTransaction(Account accountFrom, String idAccountTo, Double amount){
        return TransactionProxy.getAccount(idAccountTo)
                .flatMap(accountTo->deposit(accountTo, amount))
                .flatMap(TransactionProxy::updateAccount)
                .flatMap(accountTo->cashout(accountFrom, amount))
                .flatMap(TransactionProxy::updateAccount)
                .flatMap(resp->saveMovements(idAccountTo, "transfer from", amount, accountFrom.getId()))
                .flatMap(resp->saveMovements(accountFrom.getId(), "transfer to", amount, idAccountTo));
    }

    public Mono<Movements> saveMovements(String idProduct,
                                     String type,
                                     Double amount,
                                     String idThirdPartyProduct) {

        Movements movement = new Movements();
        movement.setIdProduct(idProduct);
        movement.setType(type);
        movement.setAmount(amount);
        movement.setIdThirdPartyProduct(idThirdPartyProduct);
        movement.setDate(new Date());

        return TransactionProxy.saveMovements(movement);

    }
    /*@Autowired
    TransactionRepository transactionRepository;

    @Override
    public Mono<Transaction> createTransaction(Transaction t) {
        return transactionRepository.save(t);
    }

    @Override
    public Mono<Transaction> updateTransaction(Transaction t) {
        return transactionRepository.save(t);
    }

    @Override
    public Mono<Transaction> findByTransactionId(String id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Flux<Transaction> findAll() {
        return transactionRepository.findAll();
    }

    @Override
    public Flux<Transaction> findAllByAccountId(String id) {
        return transactionRepository.findAll(Sort.by(String.valueOf(id)));
    }

    @Override
    public Mono<Void> deleteTransaction(String id) {
        return transactionRepository.deleteById(id);
    }
    */
}
