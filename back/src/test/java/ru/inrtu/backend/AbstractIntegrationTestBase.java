package ru.inrtu.backend;

import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Базовый класс для создания интеграционных тестов.
 *
 * @author Kamron Boturkhonov
 * @since 2023.03.16
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = Main.class)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public abstract class AbstractIntegrationTestBase {

    /**
     * Логгер.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractIntegrationTestBase.class);

    /**
     * Логин админа.
     */
    protected final String DEMO_ADMIN = "admin@a.ru";

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    @Qualifier("schoolchildService")
    protected UserDetailsService userDetailsService;

    /**
     * Менеджер транзакций.
     */
    @Autowired
    protected PlatformTransactionManager transactionManager;

    /**
     * Список операций, которые будут выполнены после окончания выполнения теста.
     * <p/>
     * После каждой обработки список очищается.
     */
    protected final List<Runnable> cleaners = new ArrayList<>();

    /**
     * Инициализация текущего пользователя, запускаемая перед каждым тестом.
     */
    @Before
    public void assignDefaultActiveUser() {
        assignActiveUser(DEMO_ADMIN);
    }

    /**
     * Устанавливает текущего пользователя.
     *
     * @param login
     *            логин текущего пользователя
     */
    protected void assignActiveUser(final String login) {
        final UserDetails user = userDetailsService.loadUserByUsername(login);
        SecurityContextHolder.getContext()
                .setAuthentication(
                        new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities()));
    }

    /**
     * Удаляет все созданные в тестах объекты.
     */
    @After
    public void runCleaners() {
        if (cleaners.isEmpty()) {
            return;
        }
        Collections.reverse(cleaners);

        cleaners.forEach(cleaner -> {
            boolean retry = true;
            int retryCount = 8;
            while (retry) {
                retry = false;
                retryCount -= 1;
                try {
                    doInTransaction(false, cleaner);
                } catch (final ObjectOptimisticLockingFailureException lockException) {
                    if (retryCount <= 0) {
                        throw lockException;
                    }
                    retry = true;
                } catch (final Exception exception) {
                    LOG.error("Something bad happens (cleaner: {})", cleaner, exception);
                }
            }
        });
        cleaners.clear();
    }

    /**
     * Запуск обработчика в транзакции.
     *
     * @param readOnly
     *            флаг, указывающий, что транзакция должна быть только для чтения
     * @param handler
     *            выполняемый обработчик
     */
    protected void doInTransaction(final boolean readOnly, final Runnable handler) {
        final TransactionTemplate transactionTemplate = new TransactionTemplate(transactionManager);
        if (readOnly) {
            transactionTemplate.setReadOnly(true);
        }
        transactionTemplate.execute(status -> {
            handler.run();
            return null;
        });
    }

}
