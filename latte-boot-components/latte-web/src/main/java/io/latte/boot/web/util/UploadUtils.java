package io.latte.boot.web.util;

import io.latte.boot.support.validate.Validate;
import io.latte.boot.support.web.IdUtils;
import io.latte.boot.support.web.MapUtils;
import io.latte.boot.support.web.PathUtils;
import io.latte.boot.web.http.response.ApiResponse;
import io.latte.boot.web.http.transfer.*;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.web.servlet.MultipartProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * UploadUtils
 *
 * @author : wugz
 * @since : 2022/2/8
 */
public class UploadUtils {
  /**
   * 文件参数
   */
  private static final String UPLOAD_RESOURCE_URL = "url";
  private static final String UPLOAD_RESOURCE_URL_PREFIX = "/upload";
  /**
   * 文件上传参数, 额外业务参数
   */
  // 上传参数: 参数前缀
  private static final String UPLOAD_PARAM_PREFIX = "/upload";
  // 上传参数: 文件分类路径名
  private static final String UPLOAD_PARAM_CATEGORY_PATH = "uploadCategoryPath";
  // 上传参数: 文件名
  private static final String UPLOAD_PARAM_FILENAME = "uploadFilename";
  // 上传参数: 上传图像BASE64数据
  private static final String UPLOAD_PARAM_IMAGE = "uploadImage";
  // 上传参数: 上传文件 / 图像文件是否改名
  private static final String UPLOAD_PARAM_CHANGE_FILENAME = "uploadChangeFilename";
  /**
   * 文件下载参数
   */
  // 下载参数: 参数前缀
  private static final String DOWNLOAD_PARAM_PREFIX = "download";
  // 下载参数: 下载资源的url
  private static final String DOWNLOAD_PARAM_URL = "downloadUrl";
  // 下载参数: 下载文件的文件名
  private static final String DOWNLOAD_PARAM_FILENAME = "downloadFilename";
  private static Logger logger = LoggerFactory.getLogger(UploadUtils.class);
  private static volatile UploadUtils INSTANCE;
  /* 初始化状态 */
  private boolean initialized = false;
  private MultipartResolver multipartResolver;
  private MultipartProperties multipartProperties;

  /**
   * 私有构造函数
   */
  private UploadUtils() {
  }

  /**
   * 初始化
   *
   * @param multipartResolver
   * @param multipartProperties
   */
  public static UploadUtils init(MultipartResolver multipartResolver,
                                 MultipartProperties multipartProperties) {
    if (null == INSTANCE) {
      synchronized (UploadUtils.class) {
        if (null == INSTANCE) {
          INSTANCE = new UploadUtils();
          INSTANCE.multipartResolver = multipartResolver;
          INSTANCE.multipartProperties = multipartProperties;
          INSTANCE.initialized = true;
        }
      }
    }

    return INSTANCE;
  }

  /**
   * 检测是否初始化
   */
  private static void checkUploadInitialized() {
    Validate.requireTrue(null != INSTANCE && INSTANCE.initialized,
        "Upload & Download is not enabled or not initialized");
  }

  /**
   * 文件上传
   *
   * @param request
   * @return
   */
  public static ApiResponse<List<UploadFile>> upload(HttpServletRequest request) {
    String categoryPath = request.getParameter(UPLOAD_PARAM_CATEGORY_PATH);
    String changeFilename = request.getParameter(UPLOAD_PARAM_CHANGE_FILENAME);
    return upload(request,
        StringUtils.hasText(categoryPath) ? categoryPath : "",
        (!StringUtils.hasText(changeFilename) || "true".equals(changeFilename)),
        new UploadEmptyCallback());
  }

  /**
   * 文件上传
   *
   * @param request
   * @param categoryPath 文件分类路径名(可为空)
   * @param changeName   是否改变上传的文件名
   * @return
   */
  public static ApiResponse<List<UploadFile>> upload(HttpServletRequest request, String categoryPath, boolean changeName) {
    return upload(request, categoryPath, changeName, new UploadEmptyCallback());
  }

  /**
   * 文件上传
   *
   * @param request
   * @param categoryPath   文件分类路径(可为空)
   * @param changeName     是否改变上传的文件名
   * @param uploadCallback 回调函数
   * @return
   */
  public static <T> ApiResponse<T> upload(HttpServletRequest request,
                                          String categoryPath,
                                          boolean changeName,
                                          final IUploadCallback<T> uploadCallback) {
    Validate.requireNonNull(uploadCallback);
    checkUploadInitialized();

    final List<UploadFile> results = new ArrayList<>();

    /* 检查Form中是否有enctype="multipart/form-data" */
    if (null != INSTANCE.multipartResolver && INSTANCE.multipartResolver.isMultipart(request)) {
      MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;
      Iterator<String> it = multiRequest.getFileNames();
      while (it.hasNext()) {
        /* 一次遍历所有文件 */
        UploadFile uploadFile = null;
        List<MultipartFile> files = multiRequest.getFiles(it.next());
        final JSONObject extraParams = getExtraParams(MapUtils.getParameterMap(request));
        for (MultipartFile file : files) {
          if (file != null && StringUtils.hasText(file.getOriginalFilename())) {
            uploadFile = new UploadFile(
                UPLOAD_RESOURCE_URL_PREFIX + File.separator + categoryPath,
                INSTANCE.multipartProperties.getLocation() + File.separator + categoryPath,
                file.getOriginalFilename(),
                changeName,
                extraParams);
            if (uploadFile.check() && doUpload(file, uploadFile)) {
              results.add(uploadFile);
            }
          }
        }
      }
    }

    /* 后续处理并返回 */
    return uploadCallback.handle(results);
  }

  /**
   * 文件上传
   *
   * @param uploadFileRequest
   * @param <R>
   * @return
   */
  public static <R extends UploadFileRequest> ApiResponse<List<UploadFile>> upload(final R uploadFileRequest) {
    return upload(uploadFileRequest, new UploadEmptyCallback());
  }

  /**
   * 文件上传
   *
   * @param uploadFileRequest
   * @param uploadCallback
   * @param <R>
   * @param <T>
   * @return
   */
  public static <R extends UploadFileRequest, T> ApiResponse<T> upload(final R uploadFileRequest, final IUploadCallback<T> uploadCallback) {
    Validate.requireNonNull(uploadFileRequest);
    Validate.requireNonNull(uploadCallback);
    checkUploadInitialized();

    final List<UploadFile> results = new ArrayList<>();
    MultipartFile file = uploadFileRequest.getUploadFile();
    if (null != file) {
      UploadFile uploadFile = null;
      if (StringUtils.hasText(file.getOriginalFilename())) {
        uploadFile = new UploadFile(UPLOAD_RESOURCE_URL_PREFIX + File.separator + uploadFileRequest.getUploadCategoryPath(),
            INSTANCE.multipartProperties.getLocation() + File.separator + uploadFileRequest.getUploadCategoryPath(),
            file.getOriginalFilename(),
            uploadFileRequest.isUploadChangeFilename(),
            getExtraParams(uploadFileRequest));
        if (uploadFile.check() && doUpload(file, uploadFile)) {
          results.add(uploadFile);
        }
      }
    }

    /* 后续处理并返回 */
    return uploadCallback.handle(results);
  }

  /**
   * 单个文件上传
   *
   * @param multipartFile
   * @param uploadFile
   * @return
   */
  private static boolean doUpload(MultipartFile multipartFile, UploadFile uploadFile) {
    boolean result = false;
    try {
      final File file = new File(uploadFile.getPath());
      multipartFile.transferTo(file);
      uploadFile.setSize(file.length());
      uploadFile.setChecksumCRC32(FileUtils.checksumCRC32(file));

      /* 设置文件权限 */
      result = file.setReadable(true) && file.setWritable(true);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
    return result;
  }

  /**
   * Base64图片上传
   *
   * @param params 参数
   * @param <R>
   * @return
   */
  public static <R extends UploadImageFileRequest> ApiResponse<List<UploadFile>> uploadBase64(R params) {
    return uploadBase64(params, new UploadEmptyCallback());
  }

  /**
   * Base64图片上传
   *
   * @param params 参数
   * @return
   */
  public static ApiResponse<List<UploadFile>> uploadBase64(Map<String, Object> params) {
    return uploadBase64(params, new UploadEmptyCallback());
  }

  /**
   * Base64图片上传
   *
   * @param params         参数
   * @param uploadCallback 回调函数
   * @param <R>
   * @param <T>
   * @return
   */
  public static <R extends UploadImageFileRequest, T> ApiResponse<T> uploadBase64(R params, IUploadCallback<T> uploadCallback) {
    Validate.requireNonNull(params);
    Validate.requireNonNull(uploadCallback);

    return uploadBase64(MapUtils.from(params), uploadCallback);
  }

  /**
   * Base64图片上传
   *
   * @param params         参数
   * @param uploadCallback 回调函数
   * @return
   */
  public static <T> ApiResponse<T> uploadBase64(Map<String, Object> params, IUploadCallback<T> uploadCallback) {
    Validate.requireNonNull(params);
    Validate.requireNonNull(params.get(UPLOAD_PARAM_IMAGE));
    Validate.requireNonNull(uploadCallback);
    checkUploadInitialized();

    final List<UploadFile> results = new ArrayList<>();
    String imageBase64 = params.get(UPLOAD_PARAM_IMAGE).toString();
    UploadImageFile imageFile = decodeImageFile(imageBase64);
    String categoryPath = Objects.isNull(params.get(UPLOAD_PARAM_CATEGORY_PATH)) ? "" : params.get(UPLOAD_PARAM_CATEGORY_PATH).toString();
    String uploadFilename = (!Objects.isNull(params.get(UPLOAD_PARAM_FILENAME)) ? params.get(UPLOAD_PARAM_FILENAME).toString() : IdUtils.getId()) + "." + imageFile.getType();
    boolean changeFilename = Objects.isNull(params.get(UPLOAD_PARAM_CHANGE_FILENAME)) || "true".equals(params.get(UPLOAD_PARAM_CHANGE_FILENAME).toString());
    JSONObject extraParams = getExtraParams(params);

    UploadFile uploadFile = new UploadFile(UPLOAD_RESOURCE_URL_PREFIX + File.separator + categoryPath,
        INSTANCE.multipartProperties.getLocation() + File.separator + categoryPath,
        uploadFilename,
        changeFilename,
        extraParams);
    if (uploadFile.check() && doUploadBase64(imageFile.getData(), uploadFile)) {
      results.add(uploadFile);
    }

    /* 后续处理并返回 */
    return uploadCallback.handle(results);
  }

  /**
   * 单个文件上传
   *
   * @param data
   * @param uploadFile
   * @return
   */
  private static boolean doUploadBase64(byte[] data, UploadFile uploadFile) {
    boolean result = false;
    try {
      /* 数据写文件, 并计算校验和: buffer=10K */
      final File file = new File(uploadFile.getPath());
      OutputStream out = new FileOutputStream(file);
      IOUtils.copy(new ByteArrayInputStream(data), out);
      out.close();
      uploadFile.setSize(data.length);
      uploadFile.setChecksumCRC32(FileUtils.checksumCRC32(file));

      /* 设置文件权限 */
      result = file.setReadable(true) && file.setWritable(true);
    } catch (IOException e) {
      logger.error(e.getMessage());
    }
    return result;
  }

  /**
   * 文件下载
   *
   * @param request
   */
  public static ResponseEntity<byte[]> download(HttpServletRequest request) throws IOException {
    String url = request.getParameter(DOWNLOAD_PARAM_URL);
    String downloadFilename = request.getParameter(DOWNLOAD_PARAM_FILENAME);
    String path = toPath(url);
    File downloadFile = new File(path);
    return download(downloadFile, downloadFilename);
  }

  /**
   * 文件下载
   *
   * @param downloadFile
   * @param downloadFilename
   * @return
   * @throws IOException
   */
  public static ResponseEntity<byte[]> download(File downloadFile, String downloadFilename) throws IOException {
    File file = Validate.requireNonNull(downloadFile, "downloadFile is null");
    checkUploadInitialized();

    if (file.exists()) {
      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
      headers.setContentDispositionFormData("attachment", StringUtils.hasText(downloadFilename) ? downloadFilename : file.getName());
      return new ResponseEntity<>(FileUtils.readFileToByteArray(file), headers, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  /**
   * 获取文件md5
   *
   * @param request
   * @return
   * @throws IOException
   */
  public static ResponseEntity<String> calculateMD5(HttpServletRequest request) throws IOException {
    checkUploadInitialized();

    String url = request.getParameter(UPLOAD_RESOURCE_URL);
    String path = toPath(url);
    File file = new File(path);
    if (file.exists()) {
      FileInputStream fis = new FileInputStream(file);
      String md5 = DigestUtils.md5DigestAsHex(fis);
      fis.close();
      return new ResponseEntity<>(md5, HttpStatus.OK);
    }
    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  /**
   * 获取资源的绝对路径
   *
   * @param url
   * @return
   */
  public static String toPath(String url) {
    checkUploadInitialized();
    return PathUtils.getRelativeRealPath(INSTANCE.multipartProperties.getLocation(), url, UPLOAD_RESOURCE_URL_PREFIX);
  }

  /**
   * 获取资源的相对url
   *
   * @param path
   * @return
   */
  public static String toUrl(String path) {
    checkUploadInitialized();
    return PathUtils.getRelativeRealPath(UPLOAD_RESOURCE_URL_PREFIX, path, INSTANCE.multipartProperties.getLocation());
  }

  /**
   * 解码Base64格式图片数据
   *
   * @param imageBase64
   * @return
   */
  public static UploadImageFile decodeImageFile(String imageBase64) {
    checkUploadInitialized();
    String type = imageBase64.substring(imageBase64.indexOf("data:image/") + "data:image/".length(), imageBase64.indexOf(";base64,"));
    byte[] data = Base64.getDecoder().decode(imageBase64.substring(imageBase64.indexOf(";base64,") + ";base64,".length()));
    for (int i = 0; i < data.length; i++) {
      if (data[i] < 0) {
        /* 调整异常数据 */
        data[i] += 256;
      }
    }
    return new UploadImageFile(type, data);
  }

  /**
   * 返回业务参数(预定义前缀参数外的所有参数)
   *
   * @return
   */
  private static JSONObject getExtraParams(final Map<String, Object> parameterMap) {
    final Map<String, Object> extraMap = new HashMap<>();
    if (null != parameterMap && !parameterMap.isEmpty()) {
      for (Map.Entry<String, Object> entry : parameterMap.entrySet()) {
        if (!entry.getKey().startsWith(UPLOAD_PARAM_PREFIX) && !entry.getKey().startsWith(DOWNLOAD_PARAM_PREFIX)) {
          extraMap.put(entry.getKey(), entry.getValue());
        }
      }
    }
    return new JSONObject(extraMap);
  }

  /**
   * 返回业务参数(预定义前缀参数外的所有参数)
   *
   * @param object
   * @return
   */
  private static JSONObject getExtraParams(final Object object) {
    Class<?> clazz = object.getClass();
    // If clazz is a System class then set includeSuperClass to false.
    boolean includeSuperClass = clazz.getClassLoader() != null;
    Method[] methods = includeSuperClass ? clazz.getMethods() : clazz.getDeclaredMethods();

    Map<String, Object> map = new HashMap<>();
    for (final Method method : methods) {
      final int modifiers = method.getModifiers();
      if (Modifier.isPublic(modifiers)
          && !Modifier.isStatic(modifiers)
          && method.getParameterTypes().length == 0
          && !method.isBridge()
          && method.getReturnType() != Void.TYPE
          && isValidMethodName(method.getName())) {
        final String key = getKeyNameFromMethod(method);
        if (key != null && !key.isEmpty()) {
          try {
            final Object result = method.invoke(object);
            if (result != null) {
              map.put(key, result);
              if (result instanceof Closeable) {
                try {
                  ((Closeable) result).close();
                } catch (IOException ignore) {
                }
              }
            }
          } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ignore) {
          }
        }
      }
    }
    return new JSONObject(map);
  }

  private static boolean isValidMethodName(String name) {
    return !"getClass".equals(name) && !"getDeclaringClass".equals(name);
  }

  private static String getKeyNameFromMethod(Method method) {
    String key = null;
    final String name = method.getName();
    if (name.startsWith("get") && name.length() > 3) {
      key = name.substring(3);
    } else if (name.startsWith("is") && name.length() > 2) {
      key = name.substring(2);
    }
    if (null == key || Character.isLowerCase(key.charAt(0))) {
      return null;
    }
    if (key.length() == 1) {
      key = key.toLowerCase(Locale.ROOT);
    } else if (!Character.isUpperCase(key.charAt(1))) {
      key = key.substring(0, 1).toLowerCase(Locale.ROOT) + key.substring(1);
    }
    if (key.startsWith(UPLOAD_PARAM_PREFIX) || key.startsWith(DOWNLOAD_PARAM_PREFIX)) {
      return null;
    }
    return key;
  }
}
