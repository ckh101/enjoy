package com.ckh.enjoy.utils;

import okhttp3.logging.HttpLoggingInterceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpLogger implements HttpLoggingInterceptor.Logger {
    private static Logger logger = LoggerFactory.getLogger(HttpLogger.class);
    @Override
    public void log(String message) {
        logger.info(message);
    }

}
