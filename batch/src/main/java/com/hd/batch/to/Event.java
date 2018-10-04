package com.hd.batch.to;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.google.appengine.api.datastore.Entity;
import com.hd.batch.util.LoggerClass;
import com.hd.batch.util.Util;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;

/**
 * Convenience class for RFID readers
 */
public class Event {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggerClass.class);

    private Util util;
    private String tagId;
    private String receiverId;
    private String storeNumber;
    private DateTime eventTime;
    private Integer signal;
    private String location;
    private Boolean exitReader;
    private String upc;
    private String productName;
    private Double currRetailAmt;
    private int checkedCounter;
    private Boolean matched;
    private String register;

    public Event(String tagId, String receiverId, String storeNumber, DateTime eventTime, Integer signal, String location,
                 Boolean exitReader, String upc, String productName, Double currRetailAmt, int checkedCounter, Boolean matched,
                 String register) {
        this.tagId = tagId;
        this.receiverId = receiverId;
        this.storeNumber = storeNumber;
        this.eventTime = eventTime;
        this.signal = signal;
        this.location = location;
        this.exitReader = exitReader;
        this.upc = upc;
        this.productName = productName;
        this.currRetailAmt = currRetailAmt;
        this.checkedCounter = checkedCounter;
        this.matched = matched;
        this.register = register;
    }

    public Integer getSignal() {
        return signal;
    }

    public void setSignal(Integer signal) {
        this.signal = signal;
    }

    public String getTagId() {
        return tagId;
    }

    public void setTagId(String tagId) {
        this.tagId = tagId;
    }

    public DateTime getEventTime() {
        return eventTime;
    }

    public void setEventTime(DateTime eventTime) {
        this.eventTime = eventTime;
    }

    public String getStoreNumber() {
        return storeNumber;
    }

    public void setStoreNumber(String storeNumber) {
        this.storeNumber = storeNumber;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Boolean getExitReader() {
        return exitReader;
    }

    public void setExitReader(Boolean exitReader) {
        this.exitReader = exitReader;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Double getCurrRetailAmt() {
        return currRetailAmt;
    }

    public void setCurrRetailAmt(Double currRetailAmt) {
        this.currRetailAmt = currRetailAmt;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public int getCheckedCounter() {
        return checkedCounter;
    }

    public void setCheckedCounter(int checkedCounter) {
        this.checkedCounter = checkedCounter;
    }

    public Boolean getMatched() {
        return matched;
    }

    public void setMatched(Boolean matched) {
        this.matched = matched;
    }

    @Override
    public String toString() {
        return "Event{" +
                "util=" + util +
                ", tagId='" + tagId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", storeNumber='" + storeNumber + '\'' +
                ", eventTime=" + eventTime +
                ", signal=" + signal +
                ", location='" + location + '\'' +
                ", exitReader=" + exitReader +
                ", upc='" + upc + '\'' +
                ", productName='" + productName + '\'' +
                ", currRetailAmt=" + currRetailAmt +
                ", checkedCounter=" + checkedCounter +
                ", matched=" + matched +
                ", register='" + register + '\'' +
                '}';
    }

    public Object serialize(String json) {
        return util.serialize(json, this.getClass());
    }

    /**
     * Returns JSON formatted Event object.
     *
     * @return String - JSON
     */
    public String json() {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        try {
            return ow.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }

    /**
     * Generates a unique identifier for the event entity.
     * Format: #@###@-####-#@##-##@@-@@#@##@##
     *  # = Number
     *  @ = Lowercase alpha
     *  - = Dash
     *
     * @return String - UUID character sequence
     */
    private String generateUUID() {
        String saltAlphaChars = "abcdefghijklmnopqrstuvwxyz";
        String saltNumericChars = "1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        int saltLength = salt.length();
        int index;
            while (saltLength < 32) { // length of the random string.

                if (saltLength == 6  || saltLength == 11 ||
                    saltLength == 16 || saltLength == 21) {

                    salt.append("-"); // append a dash at key points in the string
                } else if (saltLength == 1  || saltLength == 5  ||
                           saltLength == 13 || saltLength == 19 ||
                           saltLength == 20 || saltLength == 22 ||
                           saltLength == 23 || saltLength == 25 ||
                           saltLength == 28) {
                    index = (int) (rnd.nextFloat() * saltAlphaChars.length());
                    salt.append(saltAlphaChars.charAt(index)); // append a letter at certain indexes
                } else { // append a number in all other indexes
                    index = (int) (rnd.nextFloat() * saltNumericChars.length());
                    salt.append(saltNumericChars.charAt(index));
                }

            }
            return salt.toString();
    }

    /**
     * Generates an entity for GCP Datastore or BigQuery event Kind
     *
     * @return Entity - Datastore/BigQuery NoSQL Database Entry/Row - Kind/Entity relationship
     * (similar to Collection/Document)
     */
    public Entity entity() {
        Entity eventEntity = new Entity("event", generateUUID());

        eventEntity.setProperty("curr_ts", new Date());
        eventEntity.setProperty("reader_id", this.getReceiverId());
        eventEntity.setProperty("event_status", "new");
        eventEntity.setProperty("event_timestamp", new Date(this.getEventTime().getMillis()));
        eventEntity.setProperty("exit_event", this.getExitReader());
        eventEntity.setProperty("location", this.getLocation());
        eventEntity.setProperty("product_image_url", "");
        eventEntity.setProperty("product_name", this.getProductName());
        eventEntity.setProperty("product_price", this.getCurrRetailAmt());
        eventEntity.setProperty("upc", this.getUpc());
        eventEntity.setProperty("store", this.getStoreNumber());
        eventEntity.setProperty("tag_id", this.getTagId());
        eventEntity.setProperty("video_url", "");
        eventEntity.setProperty("matched", this.getMatched());
        eventEntity.setProperty("checkedCounter", this.getCheckedCounter());

        if (this.getRegister() != null && !this.getRegister().isEmpty()) {
            eventEntity.setProperty("register", this.getRegister());
        } else {
            eventEntity.setProperty("register", "");
        }

        return eventEntity;
    }

}