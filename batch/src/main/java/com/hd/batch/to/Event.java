package com.hd.batch.to;

import com.hd.batch.util.Util;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;

public class Event {
    private Util util;
    private String name_id;
    private String curr_ts;
    private String event_status;
    private String event_timestamp;
    private String location;
    private String exit_event;
    private String matched;
    private String product_image_url;
    private String product_name;
    private String product_price;
    private String reader_id;
    private String register;
    private String store;
    private String tag_id;
    private String upc;
    private String video_url;
    private String validation_status;

    public String getName_id() {
        return name_id;
    }

    public void setName_id(String name_id) {
        this.name_id = name_id;
    }

    public String getCurr_ts() {
        return curr_ts;
    }

    public void setCurr_ts(String curr_ts) {
        this.curr_ts = curr_ts;
    }

    public String getEvent_status() {
        return event_status;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    public String getEvent_timestamp() {
        return event_timestamp;
    }

    public void setEvent_timestamp(String event_timestamp) {
        this.event_timestamp = event_timestamp;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getExit_event() {
        return exit_event;
    }

    public void setExit_event(String exit_event) {
        this.exit_event = exit_event;
    }

    public String getMatched() {
        return matched;
    }

    public void setMatched(String matched) {
        this.matched = matched;
    }

    public String getProduct_image_url() {
        return product_image_url;
    }

    public void setProduct_image_url(String product_image_url) {
        this.product_image_url = product_image_url;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getReader_id() {
        return reader_id;
    }

    public void setReader_id(String reader_id) {
        this.reader_id = reader_id;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getTag_id() {
        return tag_id;
    }

    public void setTag_id(String tag_id) {
        this.tag_id = tag_id;
    }

    public String getUpc() {
        return upc;
    }

    public void setUpc(String upc) {
        this.upc = upc;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getValidation_status() {
        return validation_status;
    }

    public void setValidation_status(String validation_status) {
        this.validation_status = validation_status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(name_id, event.name_id) &&
                Objects.equals(curr_ts, event.curr_ts) &&
                Objects.equals(event_status, event.event_status) &&
                Objects.equals(event_timestamp, event.event_timestamp) &&
                Objects.equals(location, event.location) &&
                Objects.equals(exit_event, event.exit_event) &&
                Objects.equals(matched, event.matched) &&
                Objects.equals(product_image_url, event.product_image_url) &&
                Objects.equals(product_name, event.product_name) &&
                Objects.equals(product_price, event.product_price) &&
                Objects.equals(reader_id, event.reader_id) &&
                Objects.equals(register, event.register) &&
                Objects.equals(store, event.store) &&
                Objects.equals(tag_id, event.tag_id) &&
                Objects.equals(upc, event.upc) &&
                Objects.equals(video_url, event.video_url) &&
                Objects.equals(validation_status, event.validation_status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name_id, curr_ts, event_status, event_timestamp, location, exit_event, matched, product_image_url, product_name, product_price, reader_id, register, store, tag_id, upc, video_url, validation_status);
    }

    @Override
    public String toString() {
        return "Event{" +
                "name_id='" + name_id + '\'' +
                ", curr_ts='" + curr_ts + '\'' +
                ", event_status='" + event_status + '\'' +
                ", event_timestamp='" + event_timestamp + '\'' +
                ", location='" + location + '\'' +
                ", exit_event='" + exit_event + '\'' +
                ", matched='" + matched + '\'' +
                ", product_image_url='" + product_image_url + '\'' +
                ", product_name='" + product_name + '\'' +
                ", product_price='" + product_price + '\'' +
                ", reader_id='" + reader_id + '\'' +
                ", register='" + register + '\'' +
                ", store='" + store + '\'' +
                ", tag_id='" + tag_id + '\'' +
                ", upc='" + upc + '\'' +
                ", video_url='" + video_url + '\'' +
                ", validation_status='" + validation_status + '\'' +
                '}';
    }

    public Object serialize(String json) {
        return util.serialize(json, this.getClass());
    }

}
