package com.hd.batch.dao;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.hd.batch.to.Event;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import static com.hd.batch.constants.QueryConstants.DATASTORE_NAMESPACE;

@Component
public class DatastoreDAO {

    private static final Logger LOGGER = Logger.getLogger(Event.class.getName());

    /**
     * write entity entity to datastore
     *
     * @param entity entity to be written
     */
    public void writeEntity(Entity entity) {
        LOGGER.info(String.format("Writing entity to datastore.  Entity:   %s ", String.valueOf(entity)));
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        NamespaceManager.set(DATASTORE_NAMESPACE); //TODO: Take note of namespace injection

        datastore.put(entity);
    }
}
