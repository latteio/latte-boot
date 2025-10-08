package io.latte.boot.support.web;

import jakarta.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * WebUtils
 *
 * @author : wugz
 * @since : 2021/9/2
 */
public final class WebUtils {

  private WebUtils() {
  }

  /**
   * 获取Ip
   *
   * @param request
   * @return
   */
  public static String getIp(HttpServletRequest request) {
    List<String> ips = new ArrayList<>();
    ips.add(request.getHeader("x-forwarded-for"));
    ips.add(request.getHeader("Proxy-Client-IP"));
    ips.add(request.getHeader("WL-Proxy-Client-IP"));
    Optional<String> ipOptional = ips.stream().filter(ip -> (null != ip && !ip.isEmpty() && !"Unknown".equalsIgnoreCase(ip))).findFirst();
    return ipOptional.orElseGet(request::getRemoteAddr);
  }

}
