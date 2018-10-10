package com.hd.batch.dao

import com.google.cloud.bigquery.TableResult
import com.hd.batch.to.Event
import org.joda.time.format.DateTimeFormat
import org.springframework.beans.factory.annotation.Autowired
import spock.lang.Specification

class BigQueryDAOSpec extends Specification {

    @Autowired
    EventBigQueryDAO bigQueryDAO

    def "events retrieved after calling getEvents()"() {
        given:

        TableResult results = null

        when:
        results = bigQueryDAO.getEvents()

        then:
        results != null && results.iterateAll().size() > 0
    }

    def "entity updated after calling updateEvent()"() {
        given:

        Event event = new Event("PILOT966401", null, "345", DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss").parseDateTime("2018-10-09 11:26:04"),
                null, null, null, null, null, 0, false);

        event.setExitReader(true)
        event.setUpc('3082303038')
        event.setProductName("Black & Decker Screwdriver")
        boolean result;
        when:
        result = bigQueryDAO.updateEvent(event.toEntity())

        then:
        result
    }
}


