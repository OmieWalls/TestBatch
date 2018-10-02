package com.hd.batch.services.impl;

import com.google.cloud.bigquery.*;
import com.google.appengine.api.datastore.Entity;
import com.hd.batch.dao.BigQueryDAO;
import com.hd.batch.dao.DatastoreDAO;
import com.hd.batch.services.EventServiceInterface;
import com.hd.batch.to.Event;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.*;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventServiceInterface {


}
