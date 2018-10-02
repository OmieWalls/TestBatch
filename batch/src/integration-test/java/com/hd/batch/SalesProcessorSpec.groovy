package com.hd.batch


import spock.lang.Specification

class SalesProcessorSpec extends Specification {

    def 'Retrieve sales from a 30-minute window starting 10 minutes before the event'() {

        given: 'We create a new list of sales data'
        def sales = []

        given: 'We create a event data object'
        def event = new Event();

        when: 'An event is being validated'


        then: 'The list should provide sales data with transaction timestamps that match the window'
        list hasSize(1)

        and: 'The list should contain the correct sales data'
        list contains('Hello World!')


//    TODO: Sales Window
//    TODO: getSalesFromTimeWindow()
//      TODO: testTransactionToBeReturnedInResult
//      TODO: testExpectTransactionNotToBeReturnedInResult
//      TODO: testExpectNoTransactionsReturned
//      TODO: testExpectTransactionReturnFrom10MinuteWindowBeforeEvent
//      TODO: testExpectTransactionReturnFrom20MinuteWindowAfterEvent
//      TODO: testHandleNullOrEmptyTransactionListInput
//      TODO: testHandleBadUpcCode
//      TODO: testHandleBadEventTimestamp
//      TODO: testIsExitEvent
//      TODO: testIsNotExitEvent
    }
}

