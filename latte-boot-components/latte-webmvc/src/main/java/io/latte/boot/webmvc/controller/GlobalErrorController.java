package io.latte.boot.webmvc.controller;

import io.latte.boot.web.http.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * GlobalErrorController
 *
 * @author : wugz
 * @since : 2021/7/23
 */
@RestController
@RequestMapping
public class GlobalErrorController {

  @GetMapping(value = "/error")
  @ResponseBody
  public ApiResponse<Object> error(HttpServletRequest request) {
    return ApiResponse.failure(
        Integer.parseInt(request.getAttribute("jakarta.servlet.error.status_code").toString()),
        null != request.getAttribute("jakarta.servlet.error.message") ?
            request.getAttribute("jakarta.servlet.error.message").toString() :
            "Caught exception",
        request.getAttribute("jakarta.servlet.error.exception_type"));
  }

}