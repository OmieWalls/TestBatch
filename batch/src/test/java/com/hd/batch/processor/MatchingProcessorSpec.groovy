package com.hd.batch.processor

import spock.lang.Specification

@Category(ProcessorSpec.class)
class MatchingProcessorSpec extends Specification {

    def 'Retrieve sales from a 30-minute window starting 10 minutes before the event'() {

        given: 'Stuff'
        def stuff = []

        given: 'And more stuff'
        def thing = 'Hello World';

        when: 'Stuff happens'
        stuff.add(thing);


        then: 'More stuff should happen'
        stuff hasSize(1)

        and: 'We get the stuff'
        stuff contains('Hello World!')

    }
}

