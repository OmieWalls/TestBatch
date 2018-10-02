package com.hd.batch.dao;

import com.google.appengine.api.datastore.Entity;
import com.google.cloud.bigquery.*;
import com.hd.batch.constants.EventServiceQueryConstants;
import com.hd.batch.to.RFIDEvent;
import com.hd.batch.util.BigQueryUtilities;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Component
public class BigQueryDAO {

    @Autowired
    BigQueryUtilities bigQueryUtilities;

    private static final Logger LOG = Logger.getLogger(RFIDEvent.class.getName());

    public BigQueryDAO (BigQueryUtilities bigQueryUtilities) {
        this.bigQueryUtilities = bigQueryUtilities;
    }

    public TableResult getEventData(String lcp){
        TableResult result = null;
        try {
            result = bigQueryUtilities.runNamed(EventServiceQueryConstants.BQ_RFID_EVENTS_BY_LCP.get(lcp));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public HashMap<String, Object> lookupMatchingSales(String storeNumber, String startTime,
                                                       String endTime, String upc, String lcp) throws Exception {

        LOG.info(String.format("Query sales for store: %s start time: %s end time: %s upd: %s lcp: %s", storeNumber, startTime, endTime, upc, lcp));
        HashMap<String, Object> matchedData = new HashMap<String, Object>();

        String queryString = EventServiceQueryConstants.BQ_SALES_BY_LCP.get(lcp).replace("@storeNumber", storeNumber)
                .replace("@startTime", startTime)
                .replace("@endTime", endTime)
                .replace("@upc", upc);

        TableResult result = bigQueryUtilities.runNamed(queryString);

        if (result.getTotalRows() > 0) {
            LOG.info("Found a sale for tag");
            for (List<FieldValue> rowDt : result.iterateAll()) {
                if (rowDt.get(0).getValue() != null) {
                    matchedData.put("register", rowDt.get(8).getValue().toString());

                }
            }
        }
        return matchedData;
    }

    public void updateEventToBQ(Entity event, String lcp) throws Exception {
        LOG.info(String.format("Writing event to bigquery.  Tagid:   %s ", (String) event.getProperty("tag_id")));

        String queryString = EventServiceQueryConstants.BQ_UPDATE_EVENT_LCP.get(lcp)
                .replace("@matched", Boolean.toString((Boolean) event.getProperty("matched")))
                .replace("@check_count", Integer.toString((Integer)event.getProperty("checkedCounter")))
                .replace("@tag_id", (String) event.getProperty("tag_id"));

        // Instantiates a client
        bigQueryUtilities.runNamed(queryString);
    }

    /**
     * write error event to big query
     *
     * @param error error which occurred
     * @param event event entity to be written
     * @param lcp   life cycle phase
     * @throws Exception on error
     */
    public void writeErrorToBQ(String error, RFIDEvent event, String lcp) throws Exception {
        String queryString = EventServiceQueryConstants.BQ_ERROR_LCP.get(lcp).replace("@currTime",
                ISODateTimeFormat.dateTime().print(new DateTime(DateTimeZone.UTC)))
                .replace("@error", error)
                .replace("@event", event.toString());

        // Instantiates a client
        bigQueryUtilities.runNamed(queryString);
    }

    public TableResult convertHexToAscii(String lcp){
        TableResult result = null;
        try {
            result = bigQueryUtilities.runNamed(EventServiceQueryConstants.BQ_CONVERT_HEX_TO_ASCII_BY_LCP.get(lcp));
        }catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }

}
