package com.hd.batch.dao;

import com.google.appengine.api.datastore.Entity;
import com.google.cloud.bigquery.TableResult;
import com.hd.batch.constants.EventServiceQueryConstants;
import com.hd.batch.to.Event;
import com.hd.batch.util.BigQueryUtilities;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.stereotype.Component;


@Component
public class EventBigQueryDAO extends BigQueryDAO {


    public EventBigQueryDAO(BigQueryUtilities bigQueryUtilities) {
        super(bigQueryUtilities);
    }

    /**
     * returns list of event entities from BigQuery
     *
     * @return TableResult result - List of event entities
     */
    public TableResult getEvents(){

        TableResult result = null;

        try {
            result = bigQueryUtilities.runNamed(
                    EventServiceQueryConstants.SELECT_BQ_RFID_EVENTS_BY_CHECK_COUNT_AND_EXIT_FLAG);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return result;
    }

    /**
     * updates event entity in BigQuery
     *
     * @param event - Event object
     *
     * @throws Exception
     */
    public void updateEvent(Entity event) throws Exception {
        LOGGER.info(String.format("Writing event to bigquery.  Tagid:   %s ", (String) event.getProperty("tag_id")));

        String queryString = EventServiceQueryConstants.UPDATE_BQ_RFID_EVENT_BY_TAG_ID
                .replace("@matched", Boolean.toString((Boolean) event.getProperty("matched")))
                .replace("@check_count", Integer.toString((Integer)event.getProperty("checkedCounter")))
                .replace("@tag_id", (String) event.getProperty("tag_id"));

        // Instantiates a client
        bigQueryUtilities.runNamed(queryString);
    }

    /**
     * Writes error event to BigQuery
     *
     * @param error - error which occurred
     * @param event - event entity to be written
     *
     * @throws Exception on error
     */
    public void writeError(String error, Event event) throws Exception {

        String queryString = EventServiceQueryConstants.INSERT_BQ_RFID_ERROR.replace("@currTime",
                ISODateTimeFormat.dateTime().print(new DateTime(DateTimeZone.UTC)))
                .replace("@error", error)
                .replace("@event", event.toString());

        // Instantiates a client
        bigQueryUtilities.runNamed(queryString);
    }

    /**
     * Updates an entity by converting the RFID tagIds from Hex to ASCII and retrieves result of entry updating.
     *
     * At the present time, this is necessary as tags presented during the POC phase are both of type HEX and ASCII
     * due to some being pre-encoded by Zebra. Todo: Update after clarification has been given as to whether we will
     * continue encoding tags or discontinue encoding.
     *
     * @return BigQuery TableResult result - List of event entities
     */
    public TableResult updateBigQueryEntityWithHexToASCII(){

        TableResult result = null;

        try {
            result = bigQueryUtilities.runNamed(EventServiceQueryConstants.UPDATE_BQ_CONVERT_HEX_TO_ASCII);
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }

        return result;
    }
}
