package aop.stage0;

import aop.DataAccessException;
import aop.StubUserHistoryDao;
import aop.domain.User;
import aop.repository.UserDao;
import aop.repository.UserHistoryDao;
import aop.service.AppUserService;
import aop.service.UserService;
import java.lang.reflect.Proxy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.PlatformTransactionManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class Stage0Test {

    private static final Logger log = LoggerFactory.getLogger(Stage0Test.class);

    @Autowired
    private UserDao userDao;

    @Autowired
    private UserHistoryDao userHistoryDao;

    @Autowired
    private StubUserHistoryDao stubUserHistoryDao;

    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @BeforeEach
    void setUp() {
        final var user = new User("gugu", "password", "hkkang@woowahan.com");
        userDao.insert(user);
    }

    @Test
    void testChangePassword() {
        final var appUserService = new AppUserService(userDao, userHistoryDao);
        final TransactionHandler transactionHandler = new TransactionHandler(platformTransactionManager, appUserService);

        // 동적 프록시 생성
        // 클래스 로더 정보, 인터페이스, 핸들러 로직 -> 해당 인터페이를 기반으로 동적 프록시를 생성후, 결과를 반환한다.
        final UserService proxy = (UserService) Proxy.newProxyInstance(UserService.class.getClassLoader(),
                new Class[]{UserService.class}, transactionHandler);

        final var newPassword = "qqqqq";
        final var createBy = "gugu";
        proxy.changePassword(1L, newPassword, createBy);

        final var actual = proxy.findById(1L);

        assertThat(actual.getPassword()).isEqualTo(newPassword);
    }

    @Test
    void testTransactionRollback() {
        final var appUserService = new AppUserService(userDao, stubUserHistoryDao);
        final TransactionHandler transactionHandler = new TransactionHandler(platformTransactionManager, appUserService);

        final UserService proxy = (UserService) Proxy.newProxyInstance(UserService.class.getClassLoader(),
                new Class[]{UserService.class}, transactionHandler);

        final var newPassword = "newPassword";
        final var createBy = "gugu";
        assertThrows(DataAccessException.class,
                () -> proxy.changePassword(1L, newPassword, createBy));

        final var actual = proxy.findById(1L);

        assertThat(actual.getPassword()).isNotEqualTo(newPassword);
    }
}
