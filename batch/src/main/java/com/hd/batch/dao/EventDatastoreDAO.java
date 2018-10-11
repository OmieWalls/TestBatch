package com.hd.batch.dao;

import com.google.appengine.api.datastore.Entity;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class EventDatastoreDAO extends DatastoreDAO {

    /**
     * write entity event to datastore
     *
     * @param entities event entity to be written
     */
    @Override
    public boolean writeEntity(List<Entity> entities) {

        LOGGER.info(String.format("Writing event to datastore.  Tagid:   %s ",
                entities.stream().map(entity -> (String) entity.getProperty("tag_id")).toString()));
        try {
            return super.writeEntity(entities);

        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            return false;
        }

    }
}
