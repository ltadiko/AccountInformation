package nl.rabo.accountinformation;

import nl.rabo.accountinformation.services.AccountService;
import nl.rabo.accountinformation.services.BalanceService;
import nl.rabo.accountinformation.services.TransactionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
        classes = {
                AccountInformationApplication.class
        },
        properties = {
                "spring.main.allow-bean-definition-overriding=true"
        })
@ExtendWith(SpringExtension.class)
class AccountInformationApplicationTests {

    @Autowired
    ApplicationContext applicationContext;

    @Test
    void contextLoads() {
        assertNotNull(applicationContext.getBean("accountService", AccountService.class));
        assertNotNull(applicationContext.getBean("balanceService", BalanceService.class));
        assertNotNull(applicationContext.getBean("transactionService", TransactionService.class));
        assertNotNull(applicationContext.getBean("balanceService", BalanceService.class));
    }

}
