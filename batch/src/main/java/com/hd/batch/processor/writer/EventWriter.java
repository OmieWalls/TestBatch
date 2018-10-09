package com.hd.batch.processor.writer;

import com.hd.batch.util.LoggerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EventWriter {

    @Autowired
    Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

}
