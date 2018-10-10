package com.hd.batch.constants;

public class QueryConstants {

    // Project & Database Names
    public static final String BIG_QUERY_PROJECT_ENV = System.getenv("BIG_QUERY_PROJECT_ENV") != null ? System.getenv("BIG_QUERY_PROJECT_ENV") : "dev-ambient-intelligence"; // GCP Project ID
    public static final String BIG_QUERY_DATASET = System.getenv("BIG_QUERY_DATASET") != null ? System.getenv("BIG_QUERY_DATASET") : "RFID";
    public static final String DATASTORE_NAMESPACE = System.getenv("DATASTORE_NAMESPACE") != null ? System.getenv("DATASTORE_NAMESPACE") : "RFID";

    // Short BigQuery & Datastore Kinds
    public static final String EVENT = System.getenv("EVENT") != null ? System.getenv("EVENT") : "event";
    public static final String TAG = System.getenv("TAG") != null ? System.getenv("TAG") : "tag";
    public static final String READER = System.getenv("READER") != null ? System.getenv("READER") : "reader";
    public static final String ERROR = System.getenv("ERROR") != null ? System.getenv("ERROR") : "error";
    public static final String SALES = System.getenv("SALES") != null ? System.getenv("SALES") : "salse";
    public static final String DATASTORE_EVENT_KIND = System.getenv("DATASTORE_EVENT_KIND") != null ? System.getenv("") : "event";

    // Full BigQuery & Datastore Kind Names
    public static final String BIG_QUERY_EVENT_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATASET + "." + EVENT;
    public static final String BIG_QUERY_TAG_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATASET + "." + TAG;
    public static final String BIG_QUERY_READER_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATASET + "." + READER;
    public static final String BIG_QUERY_ERROR_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATASET + "." + ERROR;
    public static final String BIG_QUERY_SALES_KIND = BIG_QUERY_PROJECT_ENV + "." + BIG_QUERY_DATASET + "." + SALES;
}
