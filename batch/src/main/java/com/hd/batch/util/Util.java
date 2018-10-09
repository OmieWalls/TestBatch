package com.hd.batch.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Util {
    private Logger log = LoggerFactory.getLogger(LoggerClass.class);

    public Object serialize(String json, Class classType) {
        try {
            return new ObjectMapper().readValue(json, classType);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
