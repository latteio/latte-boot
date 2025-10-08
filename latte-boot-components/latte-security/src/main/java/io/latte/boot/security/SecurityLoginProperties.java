package io.latte.boot.security;

/**
 * SecurityLoginProperties
 *
 * @author : wugz
 * @since : 2021/11/5
 */
public class SecurityLoginProperties {
  private boolean useEncrypt;
  private String encryptPrivateKey;
  private boolean useCode;
  private long codeExpire;

  public SecurityLoginProperties() {
    this.useEncrypt = true;
    this.useCode = false;
    this.codeExpire = 0;
  }

  public boolean isUseEncrypt() {
    return useEncrypt;
  }

  public void setUseEncrypt(boolean useEncrypt) {
    this.useEncrypt = useEncrypt;
  }

  public String getEncryptPrivateKey() {
    return encryptPrivateKey;
  }

  public void setEncryptPrivateKey(String encryptPrivateKey) {
    this.encryptPrivateKey = encryptPrivateKey;
  }

  public boolean isUseCode() {
    return useCode;
  }

  public void setUseCode(boolean useCode) {
    this.useCode = useCode;
  }

  public long getCodeExpire() {
    return codeExpire;
  }

  public void setCodeExpire(long codeExpire) {
    this.codeExpire = codeExpire;
  }
}
