package io.latte.boot.support.resource;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * ResourceReader
 *
 * @author : wugz
 * @since : 2016/7/1
 */
public final class ResourceReader {
  private static Logger logger = LoggerFactory.getLogger(ResourceReader.class);

  /**
   * 构造函数
   */
  private ResourceReader() {
  }

  /**
   * 读取XML文件并返回Document
   *
   * @param url
   * @return
   */
  public static Document readXML(URL url) {
    try {
      if (null != url) {
        SAXReader reader = new SAXReader();
        return reader.read(url);
      }
    } catch (DocumentException e) {
      logger.error(e.getMessage());
    }
    return null;
  }

  /**
   * 读取XML文件并返回Document
   *
   * @param filePath 文件类路径(或者绝对路径)
   * @return 配置文件的Document
   */
  public static Document readXML(String filePath) {
    try {
      Document docu = null;
      /* xml资源对象(URL) */
      URL url = getResource(filePath);
      docu = readXML(url);
      if (null != docu) {
        return docu;
      }
      /* xml文件对象(File) */
      docu = readXML(new File(filePath));
      if (null != docu) {
        return docu;
      }
      /* xml文本(字符串) */
      docu = DocumentHelper.parseText(filePath);
      if (null != docu) {
        return docu;
      }
      throw new DocumentException("Destination resource not found: " + filePath);
    } catch (DocumentException e) {
      logger.error(e.getMessage());
    }
    return null;
  }

  /**
   * 读取XML文件并返回Document
   *
   * @param file 文件对象
   * @return 配置文件的Document
   */
  public static Document readXML(File file) {
    try {
      if (null != file && file.exists()) {
        SAXReader reader = new SAXReader();
        return reader.read(file);
      }
    } catch (DocumentException e) {
      logger.error(e.getMessage());
    }
    return null;
  }

  /**
   * 获得资源的输入流
   *
   * @param path 资源相对路径
   * @return
   */
  public static InputStream getResourceAsStream(String path) {
    if (null != path && !path.isEmpty()) {
      return ResourceReader.class.getClassLoader().getResourceAsStream(path);
    }
    return null;
  }

  /**
   * 获得资源的URL
   *
   * @param path 资源相对路径
   * @return
   */
  public static URL getResource(String path) {
    if (null != path && !path.isEmpty()) {
      return ResourceReader.class.getClassLoader().getResource(path);
    }
    return null;
  }
}
