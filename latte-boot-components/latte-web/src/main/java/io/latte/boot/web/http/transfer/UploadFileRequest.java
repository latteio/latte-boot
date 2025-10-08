package io.latte.boot.web.http.transfer;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

/**
 * 文件上传模型
 *
 * @author : wugz
 * @since : 2022/4/8
 */
public class UploadFileRequest implements Serializable {
  private MultipartFile uploadFile;
  private String uploadCategoryPath;
  private boolean uploadChangeFilename;

  /**
   * 构造函数
   */
  public UploadFileRequest() {
  }

  public MultipartFile getUploadFile() {
    return uploadFile;
  }

  public void setUploadFile(MultipartFile uploadFile) {
    this.uploadFile = uploadFile;
  }

  public String getUploadCategoryPath() {
    return uploadCategoryPath;
  }

  public void setUploadCategoryPath(String uploadCategoryPath) {
    this.uploadCategoryPath = uploadCategoryPath;
  }

  public boolean isUploadChangeFilename() {
    return uploadChangeFilename;
  }

  public void setUploadChangeFilename(boolean uploadChangeFilename) {
    this.uploadChangeFilename = uploadChangeFilename;
  }

}
