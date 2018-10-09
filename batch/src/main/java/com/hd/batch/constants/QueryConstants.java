package com.hd.batch.constants;

public class QueryConstants {
    public static final String BIG_QUERY_PROJECT_ENV = "rfid-data-display"; // GCP Project ID
    public static final String BIG_QUERY_DATABASE = "rfid_table";

    public static final String EVENT = "event_copy";
    public static final String TAG = "tag";
    public static final String READER = "reader";
    public static final String ERROR = "error";
    public static final String SALES = "sales";
    public static final String DATASTORE_EVENT_KIND = "rfidevent";
    public static final String DATASTORE_NAMESPACE = "rfidBatch";

    public static final String BIG_QUERY_EVENT_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATABASE + "." + EVENT;
    public static final String BIG_QUERY_TAG_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATABASE + "." + TAG;
    public static final String BIG_QUERY_READER_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATABASE + "." + READER;
    public static final String BIG_QUERY_ERROR_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATABASE + "." + ERROR;
    public static final String BIG_QUERY_SALES_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATABASE + "." + SALES;
}
