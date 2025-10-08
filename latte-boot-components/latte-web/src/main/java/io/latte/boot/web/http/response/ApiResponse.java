package io.latte.boot.web.http.response;

import io.latte.boot.web.http.message.MessageCodec;
import io.latte.boot.web.http.message.MessageUtils;

import java.util.UUID;

/**
 * ApiResponse
 *
 * @param <T>
 * @author : wugz
 * @since : 2018/6/10
 */
public class ApiResponse<T> implements IResponse<T> {
  /**
   * 响应成功或失败编码(1成功, 0失败)
   */
  private int code;

  /**
   * HttpStatus: 响应状态码
   */
  private int status;

  /**
   * 响应消息
   */
  private String msg;

  /**
   * 业务数据
   */
  private T data;

  /**
   * requestId(分布式追踪id, 可选), eventId(事件id, 可选)
   */
  private String requestId;
  private String eventId;

  /**
   * 构造函数
   */
  public ApiResponse() {
  }

  /**
   * 构造函数
   *
   * @param code      响应成功或失败编码(1成功, 0失败)
   * @param status    响应状态码
   * @param msg       响应消息
   * @param data      响应数据
   * @param requestId 请求id
   * @param eventId   事件id
   */
  public ApiResponse(
      int code,
      int status,
      String msg,
      T data,
      String requestId,
      String eventId) {
    this.code = code;
    this.status = status;
    this.msg = msg;
    this.data = data;
    this.requestId = requestId;
    this.eventId = eventId;
  }

  /**
   * 返回成功model
   *
   * @return
   */
  public static <T> ApiResponse<T> success() {
    return new ApiResponse<>(
        1,
        MessageUtils.getDefaultSuccessStatus(),
        MessageUtils.getDefaultSuccessMsg(),
        (T) null,
        getRequestIdInternal(),
        null);
  }

  /**
   * 返回成功model
   *
   * @param data 成功数据
   * @param <T>
   * @return
   */
  public static <T> ApiResponse<T> success(T data) {
    return new ApiResponse<>(
        1,
        MessageUtils.getDefaultSuccessStatus(),
        MessageUtils.getDefaultSuccessMsg(),
        data,
        getRequestIdInternal(),
        null);
  }

  /**
   * 返回成功model
   *
   * @param msg  成功消息
   * @param data 成功数据
   * @param <T>
   * @return
   */
  public static <T> ApiResponse<T> success(String msg, T data) {
    return new ApiResponse<>(
        1,
        MessageUtils.getDefaultSuccessStatus(),
        msg,
        data,
        getRequestIdInternal(),
        null);
  }

  /**
   * 返回成功model
   *
   * @param status 成功状态码
   * @param msg    成功消息
   * @param data   成功数据
   * @param <T>
   * @return
   */
  public static <T> ApiResponse<T> success(int status, String msg, T data) {
    return new ApiResponse<>(
        1,
        status,
        msg,
        data,
        getRequestIdInternal(),
        null);
  }

  /**
   * 返回失败model
   *
   * @return
   */
  public static <T> ApiResponse<T> failure() {
    return new ApiResponse<>(
        0,
        MessageUtils.getDefaultFailureStatus(),
        MessageUtils.getDefaultFailureMsg(),
        (T) null,
        getRequestIdInternal(),
        null);
  }

  /**
   * 返回失败model
   *
   * @param msg 失败消息
   * @return
   */
  public static <T> ApiResponse<T> failure(String msg) {
    return new ApiResponse<>(
        0,
        MessageUtils.getDefaultFailureStatus(),
        msg,
        (T) null,
        getRequestIdInternal(),
        null);
  }

  /**
   * 返回失败model
   *
   * @param status 失败状态码
   * @param msg    失败消息
   * @return
   */
  public static <T> ApiResponse<T> failure(int status, String msg) {
    return new ApiResponse<>(
        0,
        status,
        msg,
        (T) null,
        getRequestIdInternal(),
        null);
  }

  /**
   * 返回失败model
   *
   * @param status 失败状态码
   * @param msg    失败消息
   * @param data   失败数据
   * @return
   */
  public static <T> ApiResponse<T> failure(int status, String msg, T data) {
    return new ApiResponse<>(
        0,
        status,
        msg,
        data,
        getRequestIdInternal(),
        null);
  }

  /**
   * 返回失败model
   *
   * @param messageCodec 消息编码
   * @return
   */
  public static <T> ApiResponse<T> failure(MessageCodec messageCodec) {
    return new ApiResponse<>(
        0,
        messageCodec.status(),
        MessageUtils.getMessage(messageCodec),
        (T) null,
        getRequestIdInternal(),
        null);
  }

  private static String getRequestIdInternal() {
    return UUID.randomUUID().toString();
  }

  /* --------------------静态方法-------------------- */

  public ApiResponse<T> requestId(String requestId) {
    this.requestId = requestId;
    return this;
  }

  public ApiResponse<T> eventId(String eventId) {
    this.eventId = eventId;
    return this;
  }

  /**
   * 抛出失败异常
   */
  public void throwFailure(String msg) {
    this.status = MessageUtils.getDefaultFailureStatus();
    this.msg = msg;
    throw new RuntimeException(this.toString());
  }

  public int getCode() {
    return code;
  }

  public int getStatus() {
    return status;
  }

  public String getMsg() {
    return msg;
  }

  public T getData() {
    return data;
  }

  public String getRequestId() {
    return requestId;
  }

  public String getEventId() {
    return eventId;
  }

  public boolean isSuccess() {
    return code == 1;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(this.getClass().getName());
    stringBuilder.append(" {\n");
    stringBuilder.append("     code: ").append(code).append(",\n");
    stringBuilder.append("   status: ").append(status).append(",\n");
    stringBuilder.append("      msg: ").append(msg).append(",\n");
    stringBuilder.append("     data: ").append(data).append(",\n");
    stringBuilder.append("requestId: ").append(requestId).append(",\n");
    stringBuilder.append("  eventId: ").append(eventId).append("\n");
    stringBuilder.append("}");
    return stringBuilder.toString();
  }
}
