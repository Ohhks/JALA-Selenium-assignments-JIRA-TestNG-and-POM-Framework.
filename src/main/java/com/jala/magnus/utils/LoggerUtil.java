package com.jala.magnus.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Framework Step 13: Centralized Log4j2 logger utility.
 */
public class LoggerUtil {
    public static Logger getLogger(Class<?> clazz) {
        return LogManager.getLogger(clazz);
    }
}
