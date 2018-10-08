package com.hd.batch.processor;

import com.google.appengine.api.datastore.Entity;
import com.google.cloud.bigquery.FieldValue;
import com.hd.batch.dao.EventDatastoreDAO;
import com.hd.batch.dao.EventBigQueryDAO;
import com.hd.batch.to.Event;
import com.hd.batch.util.LoggerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class EventProcessor implements ItemProcessor<Event, Entity> {

    @Autowired
    public EventBigQueryDAO eventBigQueryDAO;

    @Autowired
    public EventDatastoreDAO eventDatastoreDAO;

    @Autowired
    public SaleProcessor saleProcessor;


    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    @Override
    public Entity process(final Event event) {

        if (event != null) {

            try {

                if (event.getUpc() == null || event.getReceiverId() == null) {
                    throw new Exception(String.format("Error! no matching readers found for tag id: %s  store: %s ", event.getTagId(), event.getStoreNumber()));
                }

                // build event entity with results from matching against sales data
                return saleProcessor.process(event);

            } catch (Exception e) {

                // create the error row
                try {

                    LOGGER.error(e.getMessage());
                    LOGGER.error(String.format("Got exception processing event.  Exception: %s. Event: %s ", e.getMessage(), event));

                    eventBigQueryDAO.writeRFIDError(e.getMessage(), event);

                } catch (Exception bqEx) {
                    LOGGER.error(String.format("Got exception writing to BQ Error table.  Exception: %s. Event: %s ", bqEx.getMessage(), event));
                }
            }
        }
        return null;
    }
}
