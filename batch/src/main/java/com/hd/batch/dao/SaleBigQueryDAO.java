package com.hd.batch.dao;

import com.google.cloud.bigquery.FieldValueList;
import com.google.cloud.bigquery.TableResult;
import com.hd.batch.constants.SaleServiceQueryConstants;
import com.hd.batch.to.Sale;
import com.hd.batch.util.LoggerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
public class SaleBigQueryDAO extends BigQueryDAO {

    public SaleBigQueryDAO( ) {
    }

    public List<Sale> getMatchingSales(String storeNumber, Map<String, String> timeWindow, List<String> upcList) throws Exception {

        List<Sale> sales = new ArrayList<>();

        LOGGER.info(String.format("Query sales for store: %s start time: %s end time: %s upd: %s",
                storeNumber, timeWindow.get("start"), timeWindow.get("end"), upcList.toString()));

        String upcListString = upcList.toString().replace("[","").replace("]","");

        String queryString = SaleServiceQueryConstants.SELECT_BQ_SALES.replace("@storeNumber", storeNumber)
                .replace("@startTime", timeWindow.get("start"))
                .replace("@endTime", timeWindow.get("end"))
                .replace("@upc", upcListString);

        TableResult result = runNamed(queryString);

        if (result.getTotalRows() > 0) {
            LOGGER.info("Found a sale for tag");

            result.iterateAll().forEach(row -> sales.add(new Sale(row)));
        }

        return sales;
    }

}
