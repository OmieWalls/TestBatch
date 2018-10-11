package com.hd.batch.dao;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.*;
import com.hd.batch.to.Event;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static com.hd.batch.constants.QueryConstants.DATASTORE_EVENT_KIND;
import static com.hd.batch.constants.QueryConstants.DATASTORE_NAMESPACE;

@Component
public class EventDatastoreDAO {

    @Autowired
    DatastoreDAO datastoreDAO;

    private static final java.util.logging.Logger LOGGER = java.util.logging.Logger.getLogger(Event.class.getName());

    /**
     * write entity event to datastore
     *
     * @param entities event entity to be written
     */
    public void writeEntity(List<Entity> entities) {
        LOGGER.info(String.format("Writing event to datastore.  Tagid:   %s ", entities.stream().map(entity -> (String) entity.getProperty("tag_id")).toString()));

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        NamespaceManager.set(DATASTORE_NAMESPACE);
        //todo: get entities where tagId = tag
        datastore.put(entities);

    }

    private Query prepareQuery(Entity entity) {
        Query.Filter propertyFilter =  new Query.FilterPredicate("name_id", Query.FilterOperator.EQUAL, entity.getProperty("name_id"));

        return new Query(DATASTORE_EVENT_KIND).setFilter(propertyFilter);
    }
}
