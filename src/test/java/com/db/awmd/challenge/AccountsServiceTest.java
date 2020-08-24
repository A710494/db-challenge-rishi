package com.db.awmd.challenge;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.service.AccountsService;
import com.db.awmd.challenge.service.NotificationService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AccountsServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(AccountsServiceTest.class);

    @Autowired
    private AccountsService accountsService;

    @Mock
    private NotificationService notificationService;

    @Test
    public void addAccount() {
        Account account = new Account("Id-123");
        account.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(account);

        assertThat(this.accountsService.getAccount("Id-123")).isEqualTo(account);
    }

    @Test
    public void addAccount_failsOnDuplicateId() {
        String uniqueId = "Id-" + System.currentTimeMillis();
        Account account = new Account(uniqueId);
        this.accountsService.createAccount(account);

        try {
            this.accountsService.createAccount(account);
            fail("Should have failed when adding duplicate account");
        } catch (DuplicateAccountIdException ex) {
            assertThat(ex.getMessage()).isEqualTo("Account id " + uniqueId + " already exists!");
        }

    }

    @Test
    public void amountTransfer_TransactionCommit() {
        Account accountFrom = new Account("Id-341");
        accountFrom.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(accountFrom);
        Account accountTo = new Account("Id-342");
        accountTo.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(accountTo);
        this.accountsService.amountTransfer("Id-341", "Id-342", new BigDecimal(1000));
        assertThat(this.accountsService.getAccount("Id-341").getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(this.accountsService.getAccount("Id-342").getBalance()).isEqualTo(new BigDecimal(2000));

    }

    @Test
    public void amountTransfer_TransactionRollBack() {
        Account accountFrom = new Account("Id-350");
        accountFrom.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(accountFrom);
        Account accountTo = new Account("Id-351");
        accountTo.setBalance(new BigDecimal(1000));
        this.accountsService.createAccount(accountTo);
        this.accountsService.amountTransfer("Id-350", "Id-351", new BigDecimal(1000));

        try {
            //make transfer when balance insufficient
            this.accountsService.amountTransfer("Id-350", "Id-351", new BigDecimal(500));
        } catch (Exception e) {
            assertThat(e.getMessage()).isEqualTo("Insufficient balance in account");
        }
        //Transaction will be rollBack and no account will be updated
        assertThat(this.accountsService.getAccount("Id-350").getBalance()).isEqualTo(BigDecimal.ZERO);
        assertThat(this.accountsService.getAccount("Id-351").getBalance()).isEqualTo(new BigDecimal(2000));

    }

    @Test
    public void concurrentAmountTransfer_TransactionCommit() {
        Account accountFrom = new Account("Id-370");
        accountFrom.setBalance(new BigDecimal(100000));
        this.accountsService.createAccount(accountFrom);
        Account accountTo = new Account("Id-371");
        accountTo.setBalance(new BigDecimal(100000));
        this.accountsService.createAccount(accountTo);

        List<Thread> threads = new ArrayList<>();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 200; i++) {
            Runnable r = new RunTest();
            Thread thread = new Thread(r);
            threads.add(thread);
            thread.start();
        }

        int count = 1;
        while (count != 0) {
            count = 0;
            for (Thread thread : threads) {
                if (thread.isAlive()) {
                    count++;
                }
            }
        }
        long endTime = System.currentTimeMillis();
        logger.info("Parallel test completed in: {} ms", (endTime - startTime));
    }

    class RunTest implements Runnable {
        @Override
        public void run() {
            accountsService.amountTransfer("Id-370", "Id-371", BigDecimal.ONE);
        }
    }
}
