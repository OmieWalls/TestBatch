package com.hd.batch.constants;

public class QueryConstants {
    public static final String BIG_QUERY_RFID_DATABASE = "rfid-data-display";
    public static final String BIG_QUERY_RFID_EVENT_KIND = BIG_QUERY_RFID_DATABASE + ".rfid_table.event_copy";
    public static final String BIG_QUERY_RFID_TAG_KIND = BIG_QUERY_RFID_DATABASE + ".rfid_table.tag";
    public static final String BIG_QUERY_RFID_READER_KIND = BIG_QUERY_RFID_DATABASE + ".rfid_table.reader";
    public static final String BIG_QUERY_RFID_ERROR_KIND = BIG_QUERY_RFID_DATABASE + ".rfid_table.error";
    public static final String BIG_QUERY_RFID_SALES_KIND = BIG_QUERY_RFID_DATABASE + ".rfid_table.sales";
}
