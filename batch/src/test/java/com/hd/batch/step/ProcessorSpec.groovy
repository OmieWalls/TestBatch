package com.hd.batch.step

import com.hd.batch.step.processor.EventProcessor
import com.hd.batch.to.Event
import com.hd.batch.to.Sale
import com.hd.batch.util.Util
import spock.lang.Specification

class ProcessorSpec extends Specification {

    EventProcessor processor = new EventProcessor()

    def "Should validate events"() {
        given:
        def events = populateEventList()
        def sales = populateSaleList()
        List<Event> validatedEvents
        List<Event> expectedValidationResults = populateExpectedValidationResults()
        when:
        validatedEvents = processor.validateEvents(events, sales)

        expect:
        validatedEvents == expectedValidationResults
    }

    def "Should get sales time window from list of events"() {
        given:
        def timeWindows
        Map<String, List<Event>> eventsPerStoreMap = populateEventsPerStoreMap()
        List<Map<String, String>> expectedTimeWindowResults = populateExpectedTimeWindowResults()

        when:
        eventsPerStoreMap.each {
            timeWindows.add(processor.getSalesTimeWindowFromEventList(it.value))
        }
        timeWindows.removeAll(expectedTimeWindowResults)

        expect:
        0 == timeWindows.size()
    }

    List<Event> populateEventList() {
        def events = new ArrayList<>()
        def jsonArray = ["{\"uuid\": \"5g009j-5225-2e71-63ut-gj9r49o77\", \"tagId\": \"PILOT634056\", \"readerId\": \"83:94:65:3C:42:3F\", \"storeNumber\": \"8719\", \"eventTime\": \"2018-10-09 04:04:14\", \"location\": \"TOOL_RENTAL\", \"exitReader\": false, \"upc\": \"431834505046\", \"productName\": \"sapien arcu sed augue aliquam\", \"currRetailAmt\":  \"event_status\": \"THEFT\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"9x919o-9477-2v35-88vq-tt0b11j61\", \"tagId\": \"PILOT949737\", \"readerId\": \"3F:98:E8:70:E0:70\", \"storeNumber\": \"1539\", \"eventTime\": \"2018-10-09 12:37:44\", \"location\": \"MAIN_EXIT\", \"exitReader\": true, \"upc\": \"505725127267\", \"productName\": \"mus etiam vel augue vestibulum rutrum rutrum\", \"currRetailAmt\": 598.98, \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"4n473h-1603-9v48-52uy-hz5y70s34\", \"tagId\": \"PILOT335176\", \"readerId\": \"83:BA:70:32:38:43\", \"storeNumber\": \"0345\", \"eventTime\": \"2018-10-09 04:47:35\", \"location\": \"PRO_EXIT\", \"exitReader\": true, \"upc\": \"270927949545\", \"productName\": \"elementum eu interdum eu tincidunt in\", \"currRetailAmt\": 175.06, \"event_status\": \"NEW\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"1q115s-0587-5f64-84oo-mg6t67m90\", \"tagId\": \"PILOT855837\", \"readerId\": \"33:2A:22:EF:7C:7E\", \"storeNumber\": \"0345\", \"eventTime\": \"2018-10-09 13:43:09\", \"location\": \"MAIN_EXIT\", \"exitReader\": true, \"upc\": \"740916224742\", \"productName\": \"nulla quisque arcu libero\", \"currRetailAmt\": 609.66, \"event_status\": \"NEW\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"7h871z-4664-4w30-99zo-eu7a93o30\", \"tagId\": \"PILOT715489\", \"readerId\": \"F7:8A:26:EF:87:34\", \"storeNumber\": \"9037\", \"eventTime\": \"2018-10-09 10:23:19\", \"location\": \"BATHROOMS\", \"exitReader\": false, \"upc\": \"764446097066\", \"productName\": \"non interdum in ante vestibulum ante ipsum primis\", \"currRetailAmt\":  \"event_status\": \"THEFT\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"0i011f-7091-2y04-41su-db0f00a35\", \"tagId\": \"PILOT832758\", \"readerId\": \"9D:13:1D:8C:89:39\", \"storeNumber\": \"7493\", \"eventTime\": \"2018-10-09 14:17:24\", \"location\": \"RECEIVING_EXIT\", \"exitReader\": true, \"upc\": \"703212114657\", \"productName\": \"nunc donec quis orci eget orci vehicula\", \"currRetailAmt\": 939.49, \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"5o122f-5573-3f43-30nz-tk1t75g76\", \"tagId\": \"PILOT262174\", \"readerId\": \"2B:CC:9B:D3:76:4E\", \"storeNumber\": \"2082\", \"eventTime\": \"2018-10-09 01:22:01\", \"location\": \"MAIN_EXIT\", \"exitReader\": true, \"upc\": \"365432901738\", \"productName\": \"euismod scelerisque quam turpis\", \"currRetailAmt\": 421.74, \"event_status\": \"THEFT\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"9u143w-0639-8k06-62gd-ug6t76m37\", \"tagId\": \"PILOT510709\", \"readerId\": \"AD:E6:4F:7A:D2:53\", \"storeNumber\": \"3614\", \"eventTime\": \"2018-10-09 21:26:57\", \"location\": \"BATHROOMS\", \"exitReader\": false, \"upc\": \"755596675834\", \"productName\": \"sapien iaculis congue vivamus metus arcu adipiscing molestie\", \"currRetailAmt\":  \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"5l791p-2139-2k11-96gl-ql5v44x41\", \"tagId\": \"PILOT909924\", \"readerId\": \"07:14:90:AC:F5:24\", \"storeNumber\": \"3614\", \"eventTime\": \"2018-10-09 14:27:52\", \"location\": \"TOOL_AISLE_FRONT\", \"exitReader\": false, \"upc\": \"764446097066\", \"productName\": \"non interdum in ante vestibulum ante ipsum primis\", \"currRetailAmt\":  \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"2t890e-1130-7x33-12ps-il2s27t52\", \"tagId\": \"PILOT627842\", \"readerId\": \"AA:D0:D4:E6:50:9E\", \"storeNumber\": \"3614\", \"eventTime\": \"2018-10-09 06:45:07\", \"location\": \"MAIN_ENTRANCE\", \"exitReader\": false, \"upc\": \"680222437392\", \"productName\": \"orci luctus et ultrices posuere cubilia curae\", \"currRetailAmt\":  \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"2h248k-5304-8p34-12fw-vt3e96e10\", \"tagId\": \"PILOT563266\", \"readerId\": \"96:43:DF:49:F2:D2\", \"storeNumber\": \"7493\", \"eventTime\": \"2018-10-09 11:55:36\", \"location\": \"TOOL_AISLE_FRONT\", \"exitReader\": false, \"upc\": \"767088864410\", \"productName\": \"in sagittis dui vel nisl duis ac\", \"currRetailAmt\":  \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"3b196t-7913-7i23-66xr-nd5c24c49\", \"tagId\": \"PILOT283235\", \"readerId\": \"44:83:F5:12:E6:CD\", \"storeNumber\": \"1101\", \"eventTime\": \"2018-10-09 08:19:00\", \"location\": \"RECEIVING_EXIT\", \"exitReader\": true, \"upc\": \"755019398242\", \"productName\": \"cubilia curae duis faucibus accumsan\", \"currRetailAmt\": 694.41, \"event_status\": \"THEFT\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"8f861z-2820-5j90-03cg-cl9a96p03\", \"tagId\": \"PILOT726971\", \"readerId\": \"E8:9F:EB:B9:F5:AD\", \"storeNumber\": \"3491\", \"eventTime\": \"2018-10-09 23:49:29\", \"location\": \"GARDEN_EXIT\", \"exitReader\": true, \"upc\": \"767088864410\", \"productName\": \"in sagittis dui vel nisl duis ac\", \"currRetailAmt\": \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"3h033q-7309-4n00-54kp-cd8o01v36\", \"tagId\": \"PILOT217932\", \"readerId\": \"13:4C:A9:36:79:D8\", \"storeNumber\": \"0345\", \"eventTime\": \"2018-10-09 19:52:11\", \"location\": \"TOOL_AISLE BACK\", \"exitReader\": true, \"upc\": \"874587863300\", \"productName\": \"morbi non quam nec dui\", \"currRetailAmt\": 970.87, \"event_status\": \"THEFT\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"7e961q-3654-8d78-75wh-zg0u27d45\", \"tagId\": \"PILOT916477\", \"readerId\": \"CD:37:4D:44:4E:18\", \"storeNumber\": \"9037\", \"eventTime\": \"2018-10-09 13:01:21\", \"location\": \"POWER_TOOLS_BACK\", \"exitReader\": false, \"upc\": \"529788314098\", \"productName\": \"nascetur ridiculus mus etiam vel\", \"currRetailAmt\":  \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"9b619x-3639-3f41-78va-gg1q37d02\", \"tagId\": \"PILOT783163\", \"readerId\": \"CD:37:4D:44:4E:18\", \"storeNumber\": \"3491\", \"eventTime\": \"2018-10-09 23:27:50\", \"location\": \"MAIN_ENTRANCE\", \"exitReader\": false, \"upc\": \"476495077808\", \"productName\": \"cras non velit nec nisi\", \"currRetailAmt\":  \"event_status\": \"THEFT\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"5d915g-4946-8x78-42us-rr5i56h88\", \"tagId\": \"PILOT224713\", \"readerId\": \"96:43:DF:49:F2:D2\", \"storeNumber\": \"3491\", \"eventTime\": \"2018-10-09 08:06:49\", \"location\": \"PRO_EXIT\", \"exitReader\": true, \"upc\": \"874587863300\", \"productName\": \"morbi non quam nec dui\", \"currRetailAmt\": 757.69, \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"6z417b-5540-5p50-58kt-qi7z08v02\", \"tagId\": \"PILOT830481\", \"readerId\": \"D4:C8:4C:5E:39:28\", \"storeNumber\": \"6413\", \"eventTime\": \"2018-10-09 21:46:31\", \"location\": \"FONT_EXIT\", \"exitReader\": true, \"upc\": \"194636048598\", \"productName\": \"quam turpis adipiscing lorem vitae mattis nibh\", \"currRetailAmt\": 998.28, \"event_status\": \"THEFT\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"0a180y-3475-9a54-69pf-zz1w22g64\", \"tagId\": \"PILOT010332\", \"readerId\": \"E9:9F:F9:90:E3:4A\", \"storeNumber\": \"6413\", \"eventTime\": \"2018-10-09 21:21:29\", \"location\": \"TOOL_RENTAL\", \"exitReader\": false, \"upc\": \"625594256381\", \"productName\": \"donec vitae nisi nam ultrices libero non\", \"currRetailAmt\": \"event_status\": \"NEW\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"1p565q-6816-2h72-57ws-mn5g64s19\", \"tagId\": \"PILOT200049\", \"readerId\": \"00:CB:9B:A9:BB:9E\", \"storeNumber\": \"9037\", \"eventTime\": \"2018-10-09 23:58:10\", \"location\": \"RECEIVING_EXIT\", \"exitReader\": true, \"upc\": \"548027072493\", \"productName\": \"elit sodales scelerisque mauris sit amet eros suspendisse\", \"currRetailAmt\": 898.12, \"event_status\": \"DISMISSED\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"3x385l-2250-6u29-05ke-rm8t59x34\", \"tagId\": \"PILOT981060\", \"readerId\": \"A9:51:7A:94:F7:B3\", \"storeNumber\": \"3491\", \"eventTime\": \"2018-10-09 14:08:07\", \"location\": \"TOOL_RENTAL\", \"exitReader\": false, \"upc\": \"736243300623\", \"productName\": \"metus sapien ut nunc vestibulum ante\", \"currRetailAmt\":  \"event_status\": \"NEW\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"0w322m-3392-0s34-47jb-rh3l16x35\", \"tagId\": \"PILOT118663\", \"readerId\": \"6B:0E:03:3F:02:F8\", \"storeNumber\": \"9971\", \"eventTime\": \"2018-10-09 06:27:55\", \"location\": \"FONT_EXIT\", \"exitReader\": true, \"upc\": \"220342951484\", \"productName\": \"quisque erat eros viverra\", \"currRetailAmt\": \"event_status\": \"NEW\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"3o676m-3190-4j90-82tp-yf5p83y16\", \"tagId\": \"PILOT992906\", \"readerId\": \"B0:99:19:5A:8E:93\", \"storeNumber\": \"1101\", \"eventTime\": \"2018-10-09 14:42:24\", \"location\": \"PRO_EXIT\", \"exitReader\": true, \"upc\": \"916433492241\", \"productName\": \"magna ac consequat metus sapien ut nunc\", \"currRetailAmt\": 230.85, \"event_status\": \"NEW\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"3w155e-7870-8r56-61aa-sh0l51b69\", \"tagId\": \"PILOT924659\", \"readerId\": \"41:F3:2A:B2:99:69\", \"storeNumber\": \"0345\", \"eventTime\": \"2018-10-09 13:36:15\", \"location\": \"MAIN_EXIT\", \"exitReader\": true, \"upc\": \"160400532709\", \"productName\": \"libero ut massa volutpat convallis morbi odio odio\", \"currRetailAmt\": 304.65, \"event_status\": \"NEW\", \"checkedCounter\": 0, \"matched\": false }",
                         "{\"uuid\": \"4w390n-1927-3k52-29zx-uw3y75w26\", \"tagId\": \"PILOT659200\", \"readerId\": \"1D:1D:80:89:22:EF\", \"storeNumber\": \"7516\", \"eventTime\": \"2018-10-09 08:30:40\", \"location\": \"TOOL_AISLE BACK\", \"exitReader\": true, \"upc\": \"228488493320\", \"productName\": \"accumsan odio curabitur convallis duis\", \"currRetailAmt\": 605.59, \"event_status\": \"THEFT\", \"checkedCounter\": 0, \"matched\": false }"]
        jsonArray.each {
            events.add(Util.serialize(it, Event.getClass()))
        }
        return events
    }

    List<Sale> populateSaleList() {
        def sales = new ArrayList<>()
//        def jsonArray = ["{ \"storeNumber\": \"9971\", \"salesTsLocal\":\ "2017-05-20 10:18:4\7", \"upcCode\":\ "637148987215\", \"skuNumber\":\ "2478780429\",\"unitSales\": 4, \"currRetailAmount\": 758.90, \"posTransTypeCode\": 5, \"posTransId\": \"3ee1fc76-978d-4848-822f-1cc918d8109a\", \"registerNumber\": 21 }",
//                         "{ \"storeNumber\": \"7516\", \"salesTsLocal\": \"2018-05-13 14:15:49\", \"upcCode\": \"784567652238\",
//                             \"skuNumber\": \"4905447968\",
//                         \"unitSales\": 15,
//                             \"currRetailAmount\": 492.91,
//                             \"posTransTypeCode\": 3,
//                             \"posTransId\": \"5645721a-13c5-4feb-9f50-d288eac526e8\",
//                             \"registerNumber\": 5
//                         }", "{\"storeNumber\": \"1101\", \"salesTsLocal\": \"2017-11-27 23:13:23\", \"upcCode\": \"009562509456\",
//                             \"skuNumber\": \"0710624662\",
//        \"unitSales\": 14,
//                             \"currRetailAmount\": 479.88,
//                             \"posTransTypeCode\": 5,
//                             \"posTransId\": \"0f4d7452-6a9c-41aa-a61b-b0c4504d2aef\",
//                             \"registerNumber\": 17
//                         }", "{\"storeNumber\": \"7493\", \"salesTsLocal\": \"2018-02-24 11:12:04\", \"upcCode\": \"548027072493\",
//                             \"skuNumber\": \"1788933389\",
//        \"unitSales\": 21,
//                             \"currRetailAmount\": 607.30,
//                             \"posTransTypeCode\": 1,
//                             \"posTransId\": \"5bae6b40-b1ee-4de2-a281-a51f68a6040a\",
//                             \"registerNumber\": 25
//                         }", "{\"storeNumber\": \"1995\", \"salesTsLocal\": \"2018-02-05 09:22:39\", \"upcCode\": \"680222437392\",
//                             \"skuNumber\": \"1984310755\",
//        \"unitSales\": 20,
//                             \"currRetailAmount\": 268.34,
//                             \"posTransTypeCode\": 5,
//                             \"posTransId\": \"c237ce4d-2590-4aa5-8b7b-2a4a03333d6f\",
//                             \"registerNumber\": 11
//                         }", "{\"storeNumber\": \"3614\", \"salesTsLocal\": \"2017-09-10 04:17:30\", \"upcCode\": \"695364384049\",
//                             \"skuNumber\": \"8060781235\",
//        \"unitSales\": 18,
//                             \"currRetailAmount\": 396.62,
//                             \"posTransTypeCode\": 4,
//                             \"posTransId\": \"43f68d70-6a14-4e06-ad13-7aa2dc350c94\",
//                             \"registerNumber\": 17
//                         }", "{\"storeNumber\": \"9037\", \"salesTsLocal\": \"2018-02-27 11:13:51\", \"upcCode\": \"168995180380\",
//                             \"skuNumber\": \"2971954498\",
//        \"unitSales\": 23,
//                             \"currRetailAmount\": 967.17,
//                             \"posTransTypeCode\": 2,
//                             \"posTransId\": \"c5039722-038f-4047-88bb-60b738c3fd9c\",
//                             \"registerNumber\": 6
//                         }", "{\"storeNumber\": \"8719\", \"salesTsLocal\": \"2017-07-05 11:34:22\", \"upcCode\": \"100190926600\",
//                             \"skuNumber\": \"4232595473\",
//        \"unitSales\": 11,
//                             \"currRetailAmount\": 991.57,
//                             \"posTransTypeCode\": 4,
//                             \"posTransId\": \"e463a723-594b-4ee6-bb1e-0301af91cc24\",
//                             \"registerNumber\": 3
//                         }", "{\"storeNumber\": \"3614\", \"salesTsLocal\":\ "2018-05-25 09:20:2\5", \"upcCode\":\ "703212114657\",
//                             \"skuNumber\":\ "2854528832\",
//        \"unitSales\": 4,
//                             \"currRetailAmount\": 276.37,
//                             \"posTransTypeCode\": 1,
//                             \"posTransId\": \"568ed8c8-2685-4067-aa2a-44d787f19c77\",
//                             \"registerNumber\": 8
//                         }", "{\"storeNumber\": \"9385\", \"salesTsLocal\": \"2017-07-30 21:46:23\", \"upcCode\": \"130593883238\",
//                             \"skuNumber\": \"2136558234\",
//        \"unitSales\": 12,
//                             \"currRetailAmount\": 240.45,
//                             \"posTransTypeCode\": 1,
//                             \"posTransId\": \"4ac15b5e-0098-4668-bbe8-21a61368efec\",
//                             \"registerNumber\": 7
//                         }", "{\"storeNumber\": \"7516\", \"salesTsLocal\":\ "2017-09-25 11:01:3\2", \"upcCode\":\ "060318869202\",
//                             \"skuNumber\":\ "2731998024\",
//        \"unitSales\": 1,
//                             \"currRetailAmount\": 517.35,
//                             \"posTransTypeCode\": 1,
//                             \"posTransId\": \"5094daeb-5d81-4188-8c01-e18dbc71fb09\",
//                             \"registerNumber\": 2
//                         }", "{\"storeNumber\": \"2082\", \"salesTsLocal\": \"2018-07-03 07:00:39\", \"upcCode\": \"785289435210\",
//                             \"skuNumber\": \"6191059701\",
//        \"unitSales\": 15,
//                             \"currRetailAmount\": 657.55,
//                             \"posTransTypeCode\": 5,
//                             \"posTransId\": \"49872035-1234-4c4c-b268-c3f3514adf3d\",
//                             \"registerNumber\": 23
//                         }", "{\"storeNumber\":\" 345\", \"salesTsLocal\": \"2018-01-20 08:34:28\", \"upcCode\": \"874587863300\",
//                             \"skuNumber\": \"0562896724\",
//        \"unitSales\": 21,
//                             \"currRetailAmount\": 212.84,
//                             \"posTransTypeCode\": 1,
//                             \"posTransId\": \"dd63ef38-c97f-4652-92bb-598f9fca66d6\",
//                             \"registerNumber\": 18
//                         }", "{\"storeNumber\": \"1995\", \"salesTsLocal\":\ "2017-11-18 02:20:3\6", \"upcCode\":\ "365432901738\",
//                             \"skuNumber\":\ "6118879869\",
//        \"unitSales\": 7,
//                             \"currRetailAmount\": 324.24,
//                             \"posTransTypeCode\": 1,
//                             \"posTransId\": \"808869b0-76e2-4931-a34f-4e5f5efd28a2\",
//                             \"registerNumber\": 12
//                         }", "{\"storeNumber\": \"3491\", \"salesTsLocal\": \"2017-09-14 16:00:24\", \"upcCode\": \"441143550990\",
//                             \"skuNumber\": \"3944385047\",
//        \"unitSales\": 19,
//                             \"currRetailAmount\": 163.76,
//                             \"posTransTypeCode\": 3,
//                             \"posTransId\": \"52260ea8-874f-4802-a54c-b830cc421358\",
//                             \"registerNumber\": 13
//                         }", "{\"storeNumber\": \"9385\", \"salesTsLocal\": \"2018-09-02 15:52:37\", \"upcCode\": \"883949464128\",
//                             \"skuNumber\": \"6784085418\",
//        \"unitSales\": 19,
//                             \"currRetailAmount\": 760.22,
//                             \"posTransTypeCode\": 3,
//                             \"posTransId\": \"64976d02-ac0c-412e-bbc3-4b63932e51d9\",
//                             \"registerNumber\": 3
//                         }", "{\"storeNumber\": \"9037\", \"salesTsLocal\": \"2018-03-01 10:29:16\", \"upcCode\": \"009562509456\",
//                             \"skuNumber\": \"2983406148\",
//        \"unitSales\": 20,
//                             \"currRetailAmount\": 647.41,
//                             \"posTransTypeCode\": 3,
//                             \"posTransId\": \"1b4825d5-0b43-4cdf-9c22-94a5738e47cb\",
//                             \"registerNumber\": 17
//                         }", "{\"storeNumber\": \"9385\", \"salesTsLocal\":\ "2017-12-26 19:01:4\5", \"upcCode\":\ "736243300623\",
//                             \"skuNumber\":\ "5817624915\",
//        \"unitSales\": 5,
//                             \"currRetailAmount\": 482.51,
//                             \"posTransTypeCode\": 1,
//                             \"posTransId\": \"d735ab61-12be-48b6-ac45-7bbd5b340e7f\",
//                             \"registerNumber\": 7
//                         }", "{\"storeNumber\": \"1539\", \"salesTsLocal\": \"2018-07-31 21:17:58\", \"upcCode\": \"165518580558\",
//                             \"skuNumber\": \"0365127329\",
//        \"unitSales\": 19,
//                             \"currRetailAmount\": 984.83,
//                             \"posTransTypeCode\": 1,
//                             \"posTransId\": \"4c8bdaf5-bdd8-43c4-9b3c-4534b208eacc\",
//                             \"registerNumber\": 20
//                         }", "{\"storeNumber\": \"1101\", \"salesTsLocal\":\ "2018-01-31 14:12:2\8", \"upcCode\":\ "108757193989\",
//                             \"skuNumber\":\ "1866001566\",
//        \"unitSales\": 4,
//                             \"currRetailAmount\": 160.49,
//                             \"posTransTypeCode\": 4,
//                             \"posTransId\": \"ead3b7e4-3ba2-49bc-8985-ff080e91d901\",
//                             \"registerNumber\": 28
//                         }", "{\"storeNumber\": \"7516\", \"salesTsLocal\": \"2018-04-13 03:37:50\", \"upcCode\": \"364424432163\",
//                             \"skuNumber\": \"9664918601\",
//        \"unitSales\": 18,
//                             \"currRetailAmount\": 452.57,
//                             \"posTransTypeCode\": 3,
//                             \"posTransId\": \"c9361d34-d377-41d2-9b24-4fb32ad48640\",
//                             \"registerNumber\": 25
//                         }", "{\"storeNumber\": \"9385\", \"salesTsLocal\": \"2017-10-31 11:58:28\", \"upcCode\": \"156681072155\",
//                             \"skuNumber\": \"8667606278\",
//        \"unitSales\": 17,
//                             \"currRetailAmount\": 202.94,
//                             \"posTransTypeCode\": 2,
//                             \"posTransId\": \"2f5b7dc7-bfd9-4444-a85c-180427a1211c\",
//                             \"registerNumber\": 1
//                         }", "{\"storeNumber\": \"1539\", \"salesTsLocal\": \"2017-08-25 02:47:15\", \"upcCode\": \"748725589444\",
//                             \"skuNumber\": \"7515611464\",
//        \"unitSales\": 11,
//                             \"currRetailAmount\": 479.01,
//                             \"posTransTypeCode\": 5,
//                             \"posTransId\": \"0ad66cdc-6596-4805-8640-52a8065798d1\",
//                             \"registerNumber\": 3
//                         }", "{\"storeNumber\": \"9971\", \"salesTsLocal\": \"2018-05-13 07:10:27\", \"upcCode\": \"009562509456\",
//                             \"skuNumber\": \"0141812265\",
//        \"unitSales\": 25,
//                             \"currRetailAmount\": 255.38,
//                             \"posTransTypeCode\": 3,
//                             \"posTransId\": \"3b6bb3e8-6eef-407e-ae14-433497abcd2f\",
//                             \"registerNumber\": 2
//                         }", "{\"storeNumber\": \"1539\", \"salesTsLocal\": \"2017-06-15 16:09:24\", \"upcCode\": \"170143858433\",
//                             \"skuNumber\": \"4980048897\",
//        \"unitSales\": 25,
//                             \"currRetailAmount\": 362.87,
//                             \"posTransTypeCode\": 5,
//                             \"posTransId\": \"ffb1f6a3-dc4e-4a5c-a7f4-10d5ac575d36\",
//                 "  "          \"registerNumber\": 13
//                   \""    \""  }]
        return sales
    }

    List<Event> populateExpectedValidationResults() {
        def events = new ArrayList<>()
        return events
    }

    Map<String, List<Event>> populateEventsPerStoreMap() {
        def eventsPerStoreMap = new HashMap<>()
        return eventsPerStoreMap
    }

    List<Map<String, String>> populateExpectedTimeWindowResults() {
        def expectedTimeWindowResults = new ArrayList<>()
        return expectedTimeWindowResults
    }
}
