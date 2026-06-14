package io.latte.boot.autoconfigure.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * DatasourceTransactionService
 *
 * @author : wugz
 * @since : 2026/6/12
 */
public class DatasourceTransactionService {
  private final Logger logger = LoggerFactory.getLogger(DatasourceTransactionService.class);
  private final PlatformTransactionManager platformTransactionManager;

  public DatasourceTransactionService(PlatformTransactionManager platformTransactionManager) {
    this.platformTransactionManager = platformTransactionManager;
  }

  public void requires(Runnable runnable) {
    TransactionStatus transactionStatus = null;
    try {
      DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
      defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
      transactionStatus = platformTransactionManager.getTransaction(defaultTransactionDefinition);

      // 执行用户逻辑
      runnable.run();

      platformTransactionManager.commit(transactionStatus);
    } catch (Exception exception) {
      logger.error("Biz execution error: {}", null != exception.getCause()
          ? exception.getCause().getMessage()
          : exception.getMessage());
      if (null != transactionStatus) {
        platformTransactionManager.rollback(transactionStatus);
      }
    }
  }

  public void requiresNew(Runnable runnable) {
    TransactionStatus transactionStatus = null;
    try {
      DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
      defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
      transactionStatus = platformTransactionManager.getTransaction(defaultTransactionDefinition);

      // 执行用户逻辑
      runnable.run();

      platformTransactionManager.commit(transactionStatus);
    } catch (Exception exception) {
      logger.error("Biz execution error: {}", null != exception.getCause()
          ? exception.getCause().getMessage()
          : exception.getMessage());
      if (null != transactionStatus) {
        platformTransactionManager.rollback(transactionStatus);
      }
    }
  }
}
