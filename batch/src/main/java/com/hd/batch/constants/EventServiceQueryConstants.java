package com.hd.batch.constants;

public interface EventServiceQueryConstants {

    String BIG_QUERY_RFID_DATABASE = "rfid-data-display";
    String BIG_QUERY_RFID_EVENT_KIND = BIG_QUERY_RFID_DATABASE + ".rfid_table.event_copy";
    String BIG_QUERY_RFID_TAG_KIND = BIG_QUERY_RFID_DATABASE + ".rfid_table.tag";
    String BIG_QUERY_RFID_READER_KIND = BIG_QUERY_RFID_DATABASE + ".rfid_table.reader";
    String BIG_QUERY_RFID_ERROR_KIND = BIG_QUERY_RFID_DATABASE + ".rfid_table.error";

    String UPDATE_BQ_CONVERT_HEX_TO_ASCII = "UPDATE `"+ BIG_QUERY_RFID_EVENT_KIND + "` event_copy " +
            "SET ascii_tag_id = " +
            "  (SELECT " +
            "    (CASE " +
            "      WHEN tag.tag_id IS NOT NULL THEN event.tag_id " +
            "      ELSE CAST(FROM_HEX(event.tag_id) AS STRING) " +
            "    END) AS ascii_tag_id " +
            "    FROM `"+ BIG_QUERY_RFID_EVENT_KIND + "` event " +
            "    LEFT JOIN `" + BIG_QUERY_RFID_TAG_KIND + "` tag ON tag.tag_id = event.tag_id " +
            "    WHERE event_copy.name_id = event.name_id " +
            "   ) " +
            "WHERE event_copy.ascii_tag_id IS NULL;";

    String SELECT_BQ_RFID_EVENTS_BY_CHECK_COUNT_AND_EXIT_FLAG = "SELECT event.video_url, event.tag_id, reader.reader_id, tag.upc, event.event_timestamp, event.curr_ts, " +
            "tag.current_retail_amount, reader.location, reader.exit, event.event_status, event.product_image_url, " +
            "tag.product_description, event.store, event.matched, event.check_count, event.signal " +
            "FROM `"+ BIG_QUERY_RFID_EVENT_KIND + "` event " +
            "LEFT JOIN `" + BIG_QUERY_RFID_TAG_KIND + "` tag ON tag.tag_id = event.ascii_tag_id " +
            "LEFT JOIN `" + BIG_QUERY_RFID_READER_KIND + "` reader ON reader.reader_id = event.reader_id " +
            "WHERE check_count < 2 AND matched = false AND exit_event = true";


    //write event to BQ for analytics purposes
    String UPDATE_BQ_RFID_EVENT_BY_TAG_ID =
            "UPDATE `"+ BIG_QUERY_RFID_EVENT_KIND + "` "
                    + "SET MATCHED =@matched, check_count = @check_count "
                    + "WHERE tag_id = '@tag_id'";

    //write error to BigQuery for analytics
    String INSERT_BQ_RFID_ERROR = "INSERT INTO `" + BIG_QUERY_RFID_ERROR_KIND + "` "
            + "(CURR_TS, ERROR, EVENT_DATA) "
            + "VALUES ('@currTime', '@error', '@event')";

}
