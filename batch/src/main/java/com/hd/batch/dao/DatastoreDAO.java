package com.hd.batch.dao;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;

import java.util.List;

import com.hd.batch.util.LoggerClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import static com.hd.batch.constants.QueryConstants.DATASTORE_NAMESPACE;

@Component
public class DatastoreDAO {

    public Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    /**
     * write entity to datastore
     *
     * @param entities - List of Entities to be written
     */
    public boolean writeEntity(List<Entity> entities) {
        try {
            DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
            NamespaceManager.set(DATASTORE_NAMESPACE); //TODO: Take note of namespace injection

            datastore.put(entities);
            return true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }
    }
}
