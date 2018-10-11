package com.hd.batch.dao

import com.google.appengine.api.datastore.DatastoreService
import com.google.appengine.api.datastore.DatastoreServiceFactory
import com.google.appengine.api.datastore.Entity
import com.google.appengine.api.datastore.Query
import com.google.cloud.bigquery.TableResult

import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig
import com.google.appengine.tools.development.testing.LocalServiceTestHelper

import com.hd.batch.to.Event
import org.joda.time.format.DateTimeFormat
import spock.lang.Specification

class BigQueryDAOSpec extends Specification {

    final LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig())
    final EventBigQueryDAO bigQueryDAO = new EventBigQueryDAO()

    def setup() {
        helper.setUp()
    }

    def cleanup() {
        helper.tearDown()
    }

    def "successful insert into an empty Datastore Kind"() {
        given:
        boolean empty = expectNoEntities()
        boolean notEmpty = expectInserts()

        expect:
        empty
        notEmpty
    }

    def "successful identical insert to prove there is no leaking across states"() {
        given:
        boolean empty = expectNoEntities()
        boolean notEmpty = expectInserts()

        expect:
        empty
        notEmpty
    }

    boolean expectNoEntities() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService()

        return 0 == ds.prepare(new Query("kind")).countEntities(withLimit(10))
    }

    boolean expectInserts() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService()

        ds.put(new Entity("kind"))
        ds.put(new Entity("kind"))

        return 2 == ds.prepare(new Query("kind")).countEntities(withLimit(10))
    }

    def "events retrieved after calling getEvents()"() {
        given:
        TableResult results

        when:
        results = bigQueryDAO.getEvents()

        then:
        results != null && results.iterateAll().size() > 0
    }

    def "entity updated after calling updateEvent()"() {
        given:

        Event event = new Event() //todo: insert event

        event.setExitReader(true)
        event.setUpc('3082303038')
        event.setProductName("Black & Decker Screwdriver")
        boolean result
        when:
        result = bigQueryDAO.updateEvent(event.toEntity())

        then:
        result
    }
}


