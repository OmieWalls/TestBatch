package com.hd.batch

import spock.lang.Specification

class MatchingProcessorSpec extends Specification {

    def 'Match exit event to POS data'() {

        given: 'Stuff'
        def stuff = []

        given: 'And more stuff'
        def thing = 'Hello World'

        when: 'Stuff happens'
        stuff.add(thing);


        then: 'More stuff should happen'
        stuff hasSize(1)

        and: 'We get the stuff'
        stuff contains('Hello World!')

    }

    //todo:
    /*
        testExpectSalesMatchToBeTrue
        testExpectSalesMatchToBeFalse
        testExpectSalesNotToReturnPreviousMatchedTransaction
        testNullOrEmptyTransactionList
        testNullOrEmptyPreviousMatchedTransactionList
        testNotNullTransactionList
        testNotNullPreviousMatchedTransactionList
        testHandleBadUpcCode
        testHandleBadEventTimestamp
        testIsExitEvent
        testIsNotExitEvent
    */
}

