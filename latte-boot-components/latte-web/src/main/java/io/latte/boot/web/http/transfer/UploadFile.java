package io.latte.boot.web.http.transfer;

import io.latte.boot.support.web.IdUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.UUID;

/**
 * UploadFile
 *
 * @author : wugz
 * @since : 2022/2/8
 */
public class UploadFile implements Serializable {
  /* 额外数据(存额外业务参数) */
  private final JSONObject extraParams;
  private String id;
  /* 原始名称 */
  private String originalFilename;
  /* 新名称 */
  private String filename;
  /* 扩展名 */
  private String extension;
  /* 文件大小 */
  private long size;
  /* 文件校验和 */
  private long checksumCRC32;
  /* 文件存储的绝对路径 */
  private String path;
  /* 文件存储的相对路径 */
  private String url;

  /**
   * 构造函数
   *
   * @param baseUrl
   * @param basePath
   * @param originalFilename
   */
  public UploadFile(String baseUrl, String basePath, String originalFilename) {
    this(baseUrl, basePath, originalFilename, false, new JSONObject());
  }

  /**
   * 构造函数
   *
   * @param baseUrl
   * @param basePath
   * @param originalFilename
   * @param changeFilename
   */
  public UploadFile(String baseUrl, String basePath, String originalFilename, boolean changeFilename, JSONObject extraParams) {
    this.id = IdUtils.getId();
    this.originalFilename = originalFilename.substring(originalFilename.lastIndexOf(File.separator) + 1);
    this.extension = originalFilename.contains(".") ? originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase() : "";
    this.filename = changeFilename ? (UUID.randomUUID().toString() + this.extension) : this.originalFilename;
    this.path = (basePath + File.separator + this.filename).replace(File.separatorChar, '/').replaceAll("/+", "/");
    this.url = (baseUrl + File.separator + this.filename).replace(File.separatorChar, '/').replaceAll("/+", "/");
    this.extraParams = null != extraParams ? extraParams : new JSONObject();
  }

  /**
   * 文件上传
   *
   * @return
   */
  public boolean check() {
    boolean result = false;

    /* 检查父路径是否存在 */
    File file = new File(this.getPath());
    if (!file.getParentFile().exists()) {
      final boolean mkdirs = file.getParentFile().mkdirs();
    }

    /* 判断是否有文件直接创建文件 */
    if (!file.exists()) {
      try {
        final boolean newFile = file.createNewFile();
      } catch (IOException ignored) {
      }
    }

    /* 文件存在校验 */
    if (file.exists()) {
      result = true;
    }
    return result;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getOriginalFilename() {
    return originalFilename;
  }

  public void setOriginalFilename(String originalFilename) {
    this.originalFilename = originalFilename;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public long getChecksumCRC32() {
    return checksumCRC32;
  }

  public void setChecksumCRC32(long checksumCRC32) {
    this.checksumCRC32 = checksumCRC32;
  }

  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public JSONObject getExtraParams() {
    return extraParams;
  }

  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("{");
    builder.append("id: ").append(this.id).append(", ");
    builder.append("originalFilename: ").append(this.originalFilename).append(", ");
    builder.append("filename: ").append(this.filename).append(", ");
    builder.append("extension: ").append(this.extension).append(", ");
    builder.append("path: ").append(this.path).append(", ");
    builder.append("url: ").append(this.url).append(", ");
    builder.append("size: ").append(this.size).append(", ");
    builder.append("checksumCRC32: ").append(this.checksumCRC32).append(", ");
    builder.append("extraParams: ").append(this.extraParams);
    builder.append("}");
    return builder.toString();
  }

}
