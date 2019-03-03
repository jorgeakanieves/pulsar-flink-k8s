package com.jene.cognitive.utils.source;

import com.jene.cognitive.model.Fees;
import com.jene.cognitive.model.GeoLoc;
import com.jene.cognitive.model.Transaction;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @uthor Jorge Nieves
 */

public class TransactionsLimitedSource extends PeriodicEmittingDataSourceFunction<Transaction> {

    @Override
    protected Iterable<Transaction> iterable() {

        Fees fees = new Fees();
        GeoLoc peerLocation = new GeoLoc();
        peerLocation.setLon(52.5179604);
        peerLocation.setLat(13.3775465);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        List<Transaction> sample =Arrays.asList(

                new Transaction(Long.parseLong("1900000000644"),  new Date(), new BigInteger("1420043099000"), "",
                        -692.72, "6767-2620-9378-9803-463", "", "",
                        "LA LLACUNA", "ARA", "", "ES",  new Date(), "",
                        "", "ES6373310907560000001059","ES6373310907560000001059", "",
                        2468.76, "", "", "",
                        fees, peerLocation)
        );


        return sample;
    }


    @Override
    protected Duration interval() {
        return Duration.ofSeconds(1);
    }
}