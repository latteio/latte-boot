package io.latte.boot.autoconfigure.datasource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 * DatabaseTransactionService
 *
 * @author : wugz
 * @since : 2026/6/12
 */
public class DatabaseTransactionService {
  private final Logger logger = LoggerFactory.getLogger(DatabaseTransactionService.class);
  private final PlatformTransactionManager platformTransactionManager;

  public DatabaseTransactionService(PlatformTransactionManager platformTransactionManager) {
    this.platformTransactionManager = platformTransactionManager;
  }

  public boolean requires(Runnable runnable) {
    TransactionStatus transactionStatus = null;
    try {
      DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
      defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
      transactionStatus = platformTransactionManager.getTransaction(defaultTransactionDefinition);

      // Execute user biz
      runnable.run();

      platformTransactionManager.commit(transactionStatus);
      return true;
    } catch (Exception exception) {
      logger.error("Biz execution error: {}", null != exception.getCause()
          ? exception.getCause().getMessage()
          : exception.getMessage());
      if (null != transactionStatus) {
        platformTransactionManager.rollback(transactionStatus);
      }
      return false;
    }
  }

  public boolean requiresNew(Runnable runnable) {
    TransactionStatus transactionStatus = null;
    try {
      DefaultTransactionDefinition defaultTransactionDefinition = new DefaultTransactionDefinition();
      defaultTransactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
      transactionStatus = platformTransactionManager.getTransaction(defaultTransactionDefinition);

      // Execute user biz
      runnable.run();

      platformTransactionManager.commit(transactionStatus);
      return true;
    } catch (Exception exception) {
      logger.error("Biz execution error: {}", null != exception.getCause()
          ? exception.getCause().getMessage()
          : exception.getMessage());
      if (null != transactionStatus) {
        platformTransactionManager.rollback(transactionStatus);
      }
      return false;
    }
  }
}
