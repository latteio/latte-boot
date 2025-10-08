package io.latte.boot.web.http.transfer;

import java.io.Serializable;

/**
 * BASE64图像文件上传模型
 *
 * @author : wugz
 * @since : 2022/4/8
 */
public class UploadImageFileRequest implements Serializable {
  private String uploadFilename;
  private String uploadImage;
  private String uploadCategoryPath;
  private boolean uploadChangeFilename;

  /**
   * 构造函数
   */
  public UploadImageFileRequest() {
  }

  public String getUploadFilename() {
    return uploadFilename;
  }

  public void setUploadFilename(String uploadFilename) {
    this.uploadFilename = uploadFilename;
  }

  public String getUploadImage() {
    return uploadImage;
  }

  public void setUploadImage(String uploadImage) {
    this.uploadImage = uploadImage;
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
