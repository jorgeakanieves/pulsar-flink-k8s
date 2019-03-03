package com.jene.cognitive.utils;


import com.jene.cognitive.model.Transaction;
import org.apache.flink.streaming.api.functions.timestamps.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.timestamps.BoundedOutOfOrdernessTimestampExtractor;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.time.Instant;

/**
 * @uthor Jorge Nieves
 */

public class TransactionTimeExtractor {
    public static class Bounded extends BoundedOutOfOrdernessTimestampExtractor<Transaction> {

        public Bounded(Time time) {
            super(time);
        }

        @Override
        public long extractTimestamp(Transaction element) {
            return Long.parseLong(element.getTimeMilis().toString());
        }
    }

    public static class Ascending extends AscendingTimestampExtractor<Transaction> {

        @Override
        public long extractAscendingTimestamp(Transaction element) {
            return Long.parseLong(element.getTimeMilis().toString());
        }
    }
}