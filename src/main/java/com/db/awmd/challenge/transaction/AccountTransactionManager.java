package com.db.awmd.challenge.transaction;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;

import java.lang.reflect.Proxy;
import java.util.Map;

/**
 * @author This class Handles Transactional Operations
 */
public class AccountTransactionManager {

    private final AccountsRepository accountsRepository;

    private TransacrionInvocationHandler<Account> handler;

    @Getter
    private boolean autoCommit = false;

    @Getter
    private AccountsRepository repoProxy;

    public AccountTransactionManager(AccountsRepository repository) {
        this.accountsRepository = repository;

        handler = new TransacrionInvocationHandler<>(accountsRepository);
        repoProxy = (AccountsRepository) Proxy.newProxyInstance(AccountsRepository.class.getClassLoader()
                        , new Class[] { AccountsRepository.class }, handler);

    }

    public void doInTransaction(TransactionCallback callback) {
        TransactionContext<Account, Account> context = new TransactionContext<>();
        ThreadLocal<TransactionContext<Account, Account>> localContext = handler.getLocalContext();
        localContext.set(context);
        try {
            callback.process();
            if (autoCommit) {
                commit();
            }
        } catch (Exception e) {
            rollBack();
            throw e;
        }

    }

    public void commit() {
        TransactionContext<Account, Account> localContext = handler.getLocalContext().get();
        Map<Account, Account> savePoints = localContext.getSavePoints();
        // swap save points value to repository
        savePoints.forEach((key, value) -> value.setBalance(key.getBalance()));
    }

    private void rollBack() {
        // Destroy Save points within same transactional context
        TransactionContext<Account, Account> localContext = handler.getLocalContext().get();
        localContext.getSavePoints().clear();
    }

}
