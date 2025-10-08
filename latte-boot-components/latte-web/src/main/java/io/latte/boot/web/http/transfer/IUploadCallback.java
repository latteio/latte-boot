package io.latte.boot.web.http.transfer;

import io.latte.boot.web.http.response.ApiResponse;

import java.util.List;

/**
 * 上传成功完成回调
 *
 * @author : wugz
 * @since : 2022/3/30
 */
public interface IUploadCallback<T> {
  /**
   * 回调处理
   *
   * @param uploadFiles 所有上传文件
   * @return
   * @throws RuntimeException
   */
  ApiResponse<T> handle(List<UploadFile> uploadFiles) throws RuntimeException;

}
