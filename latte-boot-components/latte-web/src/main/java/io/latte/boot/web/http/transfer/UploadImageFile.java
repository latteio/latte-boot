package io.latte.boot.web.http.transfer;

import java.io.Serializable;

/**
 * UploadImageFile
 *
 * @author : wugz
 * @since : 2022/2/8
 */
public class UploadImageFile implements Serializable {
  private String type;
  private byte[] data;

  /**
   * 构造函数
   *
   * @param type
   * @param data
   */
  public UploadImageFile(String type, byte[] data) {
    this.type = type;
    this.data = data;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public byte[] getData() {
    return data;
  }

  public void setData(byte[] data) {
    this.data = data;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append(UploadImageFile.class.getSimpleName()).append("{");
    builder.append("type: ").append(type).append(", ");
    builder.append("data: ").append("***");
    builder.append("}");
    return builder.toString();
  }
}
