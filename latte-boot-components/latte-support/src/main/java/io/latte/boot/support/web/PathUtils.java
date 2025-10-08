package io.latte.boot.support.web;

import com.google.common.base.Strings;

/**
 * PathUtils
 *
 * @author : wugz
 * @since : 2016/7/1
 */
public final class PathUtils {
  public static final String SEPARATOR = "/";
  public static final String LAST_PATH = "../";

  /**
   * 构造函数
   */
  private PathUtils() {
  }

  /**
   * 获得相对路径
   *
   * @param parent
   * @param child
   * @return
   */
  public static String getRelativePath(String parent, String child) {
    parent = getPrettyPath(parent);
    child = getPrettyPath(child);
    return getOffsetPath(getPrettyPath(getPathAsChild(parent, child)));
  }

  /**
   * 获得相对真实地址
   *
   * @param prefixPath  地址前缀
   * @param appendPath  追加地址
   * @param replacePath 替换开始地址
   * @return
   */
  public static String getRelativeRealPath(String prefixPath, String appendPath, String replacePath) {
    StringBuilder path = new StringBuilder();
    if (Strings.isNullOrEmpty(prefixPath)) {
      prefixPath = "";
    } else {
      prefixPath = getPrettyPath(prefixPath);
    }

    if (Strings.isNullOrEmpty(appendPath)) {
      appendPath = "";
    } else {
      appendPath = getPrettyPath(appendPath);
    }

    if (!Strings.isNullOrEmpty(replacePath)) {
      replacePath = getPrettyPath(replacePath);
      path.append(prefixPath).append(SEPARATOR).append(appendPath.replace(replacePath, ""));
    } else {
      path.append(prefixPath).append(SEPARATOR).append(appendPath);
    }

    return getPrettyPath(path.toString());
  }

  /**
   * 返回美化的路径
   *
   * @param path
   * @return
   */
  public static String getPrettyPath(String path) {
    return getPrettyPath(path, true);
  }

  /**
   * Normalize a relative URI path that may have relative values ("/./",
   * "/../", and so on ). <strong>WARNING</strong> - This method is
   * useful only for normalizing application-generated paths.  It does not
   * try to perform security checks for malicious input.
   *
   * @param path             Relative path to be normalized
   * @param replaceBackSlash Should '\\' be replaced with '/'
   * @return The normalized path or <code>null</code> if the path cannot be
   * @from tomcat 9.0.41 RequestUtil.normalize()
   * normalized
   */
  private static String getPrettyPath(String path, boolean replaceBackSlash) {
    if (path == null) {
      return null;
    }

    if (path.isEmpty()) {
      return path;
    }

    // Create a place for the normalized path
    String normalized = path;

    if (replaceBackSlash && normalized.indexOf('\\') >= 0)
      normalized = normalized.replace('\\', '/');

    boolean addedTrailingSlash = false;
    if (normalized.endsWith("/.") || normalized.endsWith("/..")) {
      normalized = normalized + "/";
      addedTrailingSlash = true;
    }

    // Resolve occurrences of "://" and "//" in the normalized path
    boolean existProtocolSlash = false;
    while (true) {
      int index = normalized.indexOf("//");
      if (index < 0) {
        break;
      }
      if (normalized.charAt(index - 1) == ':') {
        normalized = normalized.replaceAll("://", ":/");
        existProtocolSlash = true;
      } else {
        normalized = normalized.substring(0, index) + normalized.substring(index + 1);
      }
    }
    if (existProtocolSlash) {
      normalized = normalized.replaceAll(":/", "://");
    }

    // Resolve occurrences of "/./" in the normalized path
    while (true) {
      int index = normalized.indexOf("/./");
      if (index < 0) {
        break;
      }
      normalized = normalized.substring(0, index) + normalized.substring(index + 2);
    }

    // Resolve occurrences of "/../" in the normalized path
    while (true) {
      int index = normalized.indexOf("/../");
      if (index < 0) {
        break;
      }
      if (index == 0) {
        return null;  // Trying to go outside our context
      }
      int index2 = normalized.lastIndexOf('/', index - 1);
      normalized = normalized.substring(0, index2) + normalized.substring(index + 3);
    }

    if (normalized.length() > 1 && addedTrailingSlash) {
      // Remove the trailing '/' we added to that input and output are
      // consistent w.r.t. to the presence of the trailing '/'.
      normalized = normalized.substring(0, normalized.length() - 1);
    }

    // Return the normalized path that we have completed
    return normalized;
  }

  /**
   * 获取偏移路径
   *
   * @param path
   * @return
   */
  private static String getOffsetPath(String path) {
    if (Strings.isNullOrEmpty(path)) {
      return "";
    }
    StringBuilder retPath = new StringBuilder();
    String[] realPaths = path.split("/");
    for (int i = 0; i < realPaths.length - 1; i++) {
      retPath.append(LAST_PATH);
    }
    return Strings.isNullOrEmpty(retPath.toString()) ? "" : retPath.toString();
  }

  /**
   * Former pathIsChild.
   *
   * @param path
   * @param pathChild
   * @return
   */
  private static String getPathAsChild(String path, String pathChild) {
    if (path == null || pathChild == null) {
      return null;
    }
    if (pathChild.compareTo(path) == 0) {
      return null;
    }
    if (path.length() == 0 && !pathChild.startsWith("/")) {
      return pathChild;
    }
    if (!path.endsWith("/")) {
      path = path + "/";
    }
    if (pathChild.startsWith(path)) {
      return pathChild.substring(path.length());
    }
    return null;
  }

  public static String getPrettyUrl(String url) {
    /* 处理协议部分和路径部分 */
    String[] parts = url.split("://", 2);
    if (parts.length == 2) {
      String protocol = parts[0];
      String path = parts[1];
      /* 替换路径中连续的斜杠为单个斜杠 */
      path = path.replaceAll("/+", "/");
      return protocol + "://" + path;
    }

    /* 如果没有协议部分, 直接处理斜杠 */
    return url.replaceAll("/+", "/");
  }
}
