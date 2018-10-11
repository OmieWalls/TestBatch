package com.hd.batch.to;

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.Entity;
import com.google.cloud.bigquery.FieldValue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.List;
import java.util.Random;

import static com.hd.batch.constants.QueryConstants.DATASTORE_EVENT_KIND;
import static com.hd.batch.constants.QueryConstants.DATASTORE_NAMESPACE;

/**
 * Convenience class for RFID readers
 */
public class Event {

    private String tagId;
    private String receiverId;
    private String storeNumber;
    private DateTime eventTime;
    private String location;
    private Boolean exitReader;
    private String upc;
    private String productName;
    private Double currRetailAmt;
    private int checkedCounter;
    private Boolean matched;

    public Event(String tagId, String receiverId, String storeNumber, DateTime eventTime, String location,
                 Boolean exitReader, String upc, String productName, Double currRetailAmt, int checkedCounter, Boolean matched) {
        this.tagId = tagId;
        this.receiverId = receiverId;
        this.storeNumber = storeNumber;
        this.eventTime = eventTime;
        this.location = location;
        this.exitReader = exitReader;
        this.upc = upc;
        this.productName = productName;
        this.currRetailAmt = currRetailAmt;
        this.checkedCounter = checkedCounter;
        this.matched = matched;
    }

    /**
     * Maps event from BigQuery TableResult. Each row is a list containing each FieldValue for an entity.
     *
     * @param tableResultRow
     */
    public Event(List<FieldValue> tableResultRow) {

        if (tableResultRow != null) {
            if (!tableResultRow.isEmpty()) {

                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss z");

                this.tagId = tableResultRow.get(1).getValue() != null ?
                        tableResultRow.get(1).getValue().toString() : null;

                this.receiverId = tableResultRow.get(2).getValue() != null ?
                        tableResultRow.get(2).getValue().toString() : null;

                this.storeNumber = tableResultRow.get(12).getValue() != null ?
                        tableResultRow.get(12).getValue().toString() : null;

                this.eventTime = tableResultRow.get(4).getValue() != null ?
                        formatter.parseDateTime(tableResultRow.get(4).getValue().toString()) : null;

                this.location = tableResultRow.get(7).getValue() != null ?
                        tableResultRow.get(7).getValue().toString() : null;

                this.exitReader = tableResultRow.get(8).getValue() != null ?
                        Boolean.parseBoolean(tableResultRow.get(8).getValue().toString()) : null;

                this.upc = tableResultRow.get(3).getValue() != null ?
                        tableResultRow.get(3).getValue().toString() : null;

                this.productName = tableResultRow.get(11).getValue() != null ?
                        tableResultRow.get(11).getValue().toString() : null;

                this.currRetailAmt = tableResultRow.get(6).getValue() != null ?
                        Double.parseDouble(tableResultRow.get(6).getValue().toString()) : null;

                this.checkedCounter = tableResultRow.get(14).getValue() != null ?
                        Integer.parseInt(tableResultRow.get(14).getValue().toString()) : null;

                this.matched = tableResultRow.get(13).getValue() != null ?
                        Boolean.parseBoolean(tableResultRow.get(13).getValue().toString()) : null;
            }
        }
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
                "tagId='" + tagId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", storeNumber='" + storeNumber + '\'' +
                ", eventTime=" + eventTime +
                ", location='" + location + '\'' +
                ", exitReader=" + exitReader +
                ", upc='" + upc + '\'' +
                ", productName='" + productName + '\'' +
                ", currRetailAmt=" + currRetailAmt +
                ", checkedCounter=" + checkedCounter +
                ", matched=" + matched + '\'' +
                '}';
    }

    /**
     * Generates a unique identifier for the event entity.
     *
     * Low priority - For integrity purposes, this will need check in DAO to assert UUID is not already used in the
     * highly unlikely event this duplicates a UUID.
     *
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
                saltLength++;

            }
            return salt.toString();
    }

    /**
     * Generates an entity for GCP Datastore or BigQuery event Kind
     *
     * @return Entity - Datastore/BigQuery NoSQL Database Entry/Row -
     *
     * Kind/Entity relationship
     * (comparable to MongoDB/Firestore Collection/Document relationship)
     */
    public Entity toEntity() {

//        NamespaceManager.set(DATASTORE_NAMESPACE);
        Entity eventEntity = new Entity(DATASTORE_EVENT_KIND, generateUUID());

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

        return eventEntity;
    }

}