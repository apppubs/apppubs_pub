package com.hingecloud.apppubs.pub.exception;

import com.hingecloud.apppubs.pub.model.vo.CreateTaskVO;
import com.hingecloud.apppubs.pub.tools.CodeConst;
import com.hingecloud.apppubs.pub.tools.JsonResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局的异常处理类
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler
    public JsonResult handleException(Exception ex) {
        if (ex instanceof ArgumentCheckException) {
            logger.info("{}", ExceptionUtils.getMessage(ex));
            return JsonResult.illegalArgument(ex.getMessage());
        } else if (ex instanceof HttpRequestMethodNotSupportedException) {
            logger.info("{}", ExceptionUtils.getMessage(ex));
            return JsonResult.failure(HttpStatus.METHOD_NOT_ALLOWED.toString(),
                    String.format("不支持的请求方式[%s]", ((HttpRequestMethodNotSupportedException) ex).getMethod()));
        } else if (ex instanceof HttpMediaTypeNotSupportedException) {
            logger.info("{}", ExceptionUtils.getMessage(ex));
            return JsonResult.failure(HttpStatus.UNSUPPORTED_MEDIA_TYPE.toString(),
                    String.format("不支持的ContentType[%s]", ((HttpMediaTypeNotSupportedException) ex).getContentType()));
        } else if (ex instanceof CreateTaskException) {
            return JsonResult.failure(CodeConst.TASK_CREATE_ERROR, ex.getMessage());
        } else {
            logger.error("{}", ExceptionUtils.getMessage(ex), ex);
            return JsonResult.generic(ex.getMessage());
        }
    }

}