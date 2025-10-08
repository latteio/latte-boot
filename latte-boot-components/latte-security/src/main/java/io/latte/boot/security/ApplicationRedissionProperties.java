package io.latte.boot.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

/**
 * ApplicationRedissionProperties
 *
 * @author : wugz
 * @since : 2022/3/30
 */
@ConfigurationProperties(prefix = "application.redis")
public class ApplicationRedissionProperties {
  /**
   * 是否启用: 默认禁止
   */
  private boolean enabled = false;

  /**
   * Database index used by the connection factory.
   */
  private int database = 0;

  /**
   * Connection URL. Overrides host, port, and password. User is ignored. Example:
   * redis://user:password@example.com:6379
   */
  private String url;

  /**
   * Redis server host.
   */
  private String host = "localhost";

  /**
   * Login username of the redis server.
   */
  private String username;

  /**
   * Login password of the redis server.
   */
  private String password;

  /**
   * Redis server port.
   */
  private int port = 6379;

  /**
   * Read timeout.
   */
  private Duration timeout;

  /**
   * Connection timeout.
   */
  private Duration connectTimeout;

  private Pool pool;

  public ApplicationRedissionProperties() {
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public int getDatabase() {
    return database;
  }

  public void setDatabase(int database) {
    this.database = database;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public Duration getTimeout() {
    return timeout;
  }

  public void setTimeout(Duration timeout) {
    this.timeout = timeout;
  }

  public Duration getConnectTimeout() {
    return connectTimeout;
  }

  public void setConnectTimeout(Duration connectTimeout) {
    this.connectTimeout = connectTimeout;
  }

  public Pool getPool() {
    return pool;
  }

  public void setPool(Pool pool) {
    this.pool = pool;
  }

  /**
   * Pool properties.
   */
  public static class Pool {
    /**
     * Whether to enable the pool. Enabled automatically if "commons-pool2" is
     * available. With Jedis, pooling is implicitly enabled in sentinel mode and this
     * setting only applies to single node setup.
     */
    private Boolean enabled;

    /**
     * Maximum number of "idle" connections in the pool. Use a negative value to
     * indicate an unlimited number of idle connections.
     */
    private int maxIdle = 8;

    /**
     * Target for the minimum number of idle connections to maintain in the pool. This
     * setting only has an effect if both it and time between eviction runs are
     * positive.
     */
    private int minIdle = 0;

    /**
     * Maximum number of connections that can be allocated by the pool at a given
     * time. Use a negative value for no limit.
     */
    private int maxActive = 8;

    /**
     * Maximum amount of time a connection allocation should block before throwing an
     * exception when the pool is exhausted. Use a negative value to block
     * indefinitely.
     */
    private Duration maxWait = Duration.ofMillis(-1);

    /**
     * Time between runs of the idle object evictor thread. When positive, the idle
     * object evictor thread starts, otherwise no idle object eviction is performed.
     */
    private Duration timeBetweenEvictionRuns;

    public Boolean getEnabled() {
      return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
      this.enabled = enabled;
    }

    public int getMaxIdle() {
      return this.maxIdle;
    }

    public void setMaxIdle(int maxIdle) {
      this.maxIdle = maxIdle;
    }

    public int getMinIdle() {
      return this.minIdle;
    }

    public void setMinIdle(int minIdle) {
      this.minIdle = minIdle;
    }

    public int getMaxActive() {
      return this.maxActive;
    }

    public void setMaxActive(int maxActive) {
      this.maxActive = maxActive;
    }

    public Duration getMaxWait() {
      return this.maxWait;
    }

    public void setMaxWait(Duration maxWait) {
      this.maxWait = maxWait;
    }

    public Duration getTimeBetweenEvictionRuns() {
      return this.timeBetweenEvictionRuns;
    }

    public void setTimeBetweenEvictionRuns(Duration timeBetweenEvictionRuns) {
      this.timeBetweenEvictionRuns = timeBetweenEvictionRuns;
    }
  }

}
