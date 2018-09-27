package com.hd.batch.processor;

import com.hd.batch.to.Upc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.batch.item.ItemProcessor;

public class UpcProcessor implements ItemProcessor<Upc, Upc> {

    private static final Logger log = LoggerFactory.getLogger(UpcProcessor.class);

    @Override
    public Upc process(final Upc upc) {
        final String nameId = upc.getNameId();
        final String upcNumber = upc.getUpc();
        final String productDescription = upc.getProductDescription().toUpperCase();

        final Upc transformedUpc = new Upc(nameId, upcNumber, productDescription);

        log.info("Converting (" + upc + ") into (" + transformedUpc + ")");

        return transformedUpc;
    }

}