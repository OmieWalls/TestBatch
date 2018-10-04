package com.hd.batch.constants;

import static com.hd.batch.constants.QueryConstants.*;

public interface SaleServiceQueryConstants {

   String SELECT_BQ_SALES = "SELECT STR_NBR, SLS_TS_LOCAL, UPC_CD, SKU_NBR,  UNT_SLS, CURR_RETL_AMT, POS_TRANS_TYP_CD,  "
                    + "POS_TRANS_ID, RGSTR_NBR FROM `" + BIG_QUERY_RFID_SALES_KIND + "` "
                    + "WHERE STR_NBR = '@storeNumber' AND SLS_TS_LOCAL "
                    + "BETWEEN  '@startTime' AND '@endTime' AND UPC_CD IN ('@upc')";
}