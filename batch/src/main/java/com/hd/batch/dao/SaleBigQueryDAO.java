package com.hd.batch.dao;


import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.TableResult;
import com.hd.batch.constants.SaleServiceQueryConstants;
import com.hd.batch.util.BigQueryUtilities;
import com.hd.batch.util.LoggerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;


@Component
public class SaleBigQueryDAO extends BigQueryDAO {

    @Autowired
    BigQueryUtilities bigQueryUtilities;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    public SaleBigQueryDAO(BigQueryUtilities bigQueryUtilities) {
        super(bigQueryUtilities);
    }

    public HashMap<String, Object> getMatchingSales(String storeNumber, String startTime,
                                                    String endTime, String upc) throws Exception {

        LOGGER.info(String.format("Query sales for store: %s start time: %s end time: %s upd: %s", storeNumber, startTime, endTime, upc));
        HashMap<String, Object> matchedData = new HashMap<String, Object>();

        String queryString = SaleServiceQueryConstants.SELECT_BQ_SALES.replace("@storeNumber", storeNumber)
                .replace("@startTime", startTime)
                .replace("@endTime", endTime)
                .replace("@upc", upc);

        TableResult result = bigQueryUtilities.runNamed(queryString);

        if (result.getTotalRows() > 0) {
            LOGGER.info("Found a sale for tag");

            for (List<FieldValue> rowDt : result.iterateAll()) {

                if (rowDt.get(0).getValue() != null) {
                    matchedData.put("register", rowDt.get(8).getValue().toString());

                }
            }
        }
        return matchedData;
    }

}
