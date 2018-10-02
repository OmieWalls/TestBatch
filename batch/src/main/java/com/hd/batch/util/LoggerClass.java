package com.hd.batch.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggerClass {

    private static Logger log = LoggerFactory.getLogger(LoggerClass.class);

        public static void main(String[] args) {

            log.trace("Trace Message: ");
            log.debug("Debug Message: ");
            log.info("Info Message: ");
            log.warn("Warn Message: ");
            log.error("Error Message: ");
    }
}
