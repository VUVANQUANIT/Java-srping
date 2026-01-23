package com.example.JavaSpring1.Config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.stat.Statistics;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import java.util.Map;

/**
 * Configuration để log thời gian thực thi query của Hibernate
 * Sẽ hiển thị thống kê về query execution time định kỳ
 * Sử dụng ApplicationListener để tránh circular dependency
 */
@Slf4j
@Component
public class QueryExecutionTimeLogger implements HibernatePropertiesCustomizer, ApplicationListener<ContextRefreshedEvent> {
    private volatile boolean initialized = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (initialized) {
            return;
        }
        // Chỉ lấy EntityManagerFactory sau khi context đã sẵn sàng
        try {
            var entityManagerFactory = event.getApplicationContext().getBean(jakarta.persistence.EntityManagerFactory.class);
            initialized = true;
            initializeStatisticsLogger(entityManagerFactory);
        } catch (Exception e) {
            log.warn("Could not obtain EntityManagerFactory for statistics: {}", e.getMessage());
        }
    }

    private void initializeStatisticsLogger(jakarta.persistence.EntityManagerFactory entityManagerFactory) {
        try {
            SessionFactoryImplementor sessionFactory = entityManagerFactory.unwrap(SessionFactoryImplementor.class);
            if (sessionFactory != null && sessionFactory.getStatistics().isStatisticsEnabled()) {
                // Log statistics mỗi 60 giây
                Thread statisticsThread = new Thread(() -> {
                    while (!Thread.currentThread().isInterrupted()) {
                        try {
                            Thread.sleep(60000); // 60 giây
                            Statistics stats = sessionFactory.getStatistics();
                            if (stats.getQueryExecutionCount() > 0) {
                                long avgTime = stats.getQueryExecutionCount() > 0 
                                    ? stats.getQueryExecutionMaxTime() / stats.getQueryExecutionCount() 
                                    : 0;
                                
                                log.info("╔══════════════════════════════════════════════════════════╗");
                                log.info("║          Hibernate Query Statistics (Last 60s)          ║");
                                log.info("╠══════════════════════════════════════════════════════════╣");
                                log.info("║ Query Execution Count:        {:>30} ║", stats.getQueryExecutionCount());
                                log.info("║ Slowest Query Time:           {:>27} ms ║", stats.getQueryExecutionMaxTime());
                                log.info("║ Average Query Time:            {:>27} ms ║", avgTime);
                                log.info("║ Entity Load Count:             {:>30} ║", stats.getEntityLoadCount());
                                log.info("║ Collection Load Count:        {:>30} ║", stats.getCollectionLoadCount());
                                log.info("║ Second Level Cache Hits:       {:>30} ║", stats.getSecondLevelCacheHitCount());
                                log.info("║ Second Level Cache Misses:     {:>30} ║", stats.getSecondLevelCacheMissCount());
                                log.info("╚══════════════════════════════════════════════════════════╝");
                            }
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        } catch (Exception e) {
                            log.error("Error logging Hibernate statistics", e);
                        }
                    }
                }, "Hibernate-Statistics-Logger");
                statisticsThread.setDaemon(true);
                statisticsThread.start();
                log.info(" Hibernate Query Statistics Logger started");
            }
        } catch (Exception e) {
            log.warn("Could not initialize Hibernate statistics logger: {}", e.getMessage());
        }
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        // Enable statistics - đã được set trong application.properties
        // Nhưng đảm bảo nó được enable
        if (!hibernateProperties.containsKey("hibernate.generate_statistics")) {
            hibernateProperties.put("hibernate.generate_statistics", true);
        }
    }
}
