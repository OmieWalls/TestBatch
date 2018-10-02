package com.hd.batch.dao;

import com.hd.batch.util.BigQueryUtilities;
import com.hd.batch.util.LoggerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BigQueryDAO {

    @Autowired
    BigQueryUtilities bigQueryUtilities;

    protected static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    public BigQueryDAO (BigQueryUtilities bigQueryUtilities) {

        this.bigQueryUtilities = bigQueryUtilities;
    }



}
