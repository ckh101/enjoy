package com.hbnet.fastsh.web.exception;

/**
 * @ClassName: 业务中断异常
 * @Auther: zoulr@qq.com
 * @Date: 2019/3/22 17:45
 */
public class ServiceException extends RuntimeException {
    public ServiceException() {}

    public ServiceException(String message) {
        super(message);
    }
}
