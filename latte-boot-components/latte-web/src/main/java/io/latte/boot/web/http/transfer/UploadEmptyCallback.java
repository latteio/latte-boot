package io.latte.boot.web.http.transfer;

import io.latte.boot.web.http.response.ApiResponse;

import java.util.List;

/**
 * UploadEmptyCallback
 *
 * @author : wugz
 * @since : 2022/3/30
 */
public class UploadEmptyCallback implements IUploadCallback<List<UploadFile>> {
  /**
   * 回调处理
   *
   * @param uploadFiles 所有上传文件
   * @return
   * @throws RuntimeException
   */
  public ApiResponse<List<UploadFile>> handle(List<UploadFile> uploadFiles) throws RuntimeException {
    return ApiResponse.success(uploadFiles);
  }

}
