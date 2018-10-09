package com.hd.batch.dao;


import com.google.cloud.bigquery.FieldValue;
import com.google.cloud.bigquery.TableResult;
import com.hd.batch.constants.SaleServiceQueryConstants;
import com.hd.batch.to.Sale;
import com.hd.batch.util.BigQueryUtilities;
import com.hd.batch.util.LoggerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class SaleBigQueryDAO extends BigQueryDAO {

    @Autowired
    BigQueryUtilities bigQueryUtilities;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    public SaleBigQueryDAO(BigQueryUtilities bigQueryUtilities) {
        super(bigQueryUtilities);
    }

    public List<Sale> getMatchingSales(String storeNumber, Map<String, String> timeWindow, List<String> upcList) throws Exception {

        LOGGER.info(String.format("Query sales for store: %s start time: %s end time: %s upd: %s",
                storeNumber, timeWindow.get("start"), timeWindow.get("end"), upcList.toString()));

        String upcListString = upcList.toString().replace("[","").replace("]","");

        HashMap<String, Object> matchedData = new HashMap<>();

        String queryString = SaleServiceQueryConstants.SELECT_BQ_SALES.replace("@storeNumber", storeNumber)
                .replace("@startTime", timeWindow.get("start"))
                .replace("@endTime", timeWindow.get("end"))
                .replace("@upc", upcListString);

        TableResult result = bigQueryUtilities.runNamed(queryString);
        //todo return list of sales
        if (result.getTotalRows() > 0) {
            LOGGER.info("Found a sale for tag");

            for (List<FieldValue> property : result.iterateAll()) {
                matchedData.put("register", property.get(8).getValue() != null ?
                        String.valueOf(property.get(8).getValue()) : null);
            }
        }

        return matchedData;
    }

}
