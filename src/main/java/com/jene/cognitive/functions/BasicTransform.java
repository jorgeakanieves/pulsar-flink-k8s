package com.jene.cognitive.functions;

import com.google.gson.Gson;
import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.utils.LocationIqGeolocation;
import org.apache.pulsar.functions.api.Function;
import org.apache.pulsar.functions.api.Context;

/**
 * @uthor Jorge Nieves
 */

public class BasicTransform implements Function<String, String> {
    @Override
    public String process(String input, Context context) {
        Gson gson = new Gson();
        Transaction t = (Transaction) gson.fromJson(input, Transaction.class);
        try {
            t.setPeerLocation(LocationIqGeolocation.getGeo(t.getPeerAddress()));
            context.getLogger().info("processed >> " + t.getTransactionId() + " " + t.getPeerLocation().toString());
        } catch (Exception ex) {
            context.getLogger().error(ex.getMessage());
        }
        return gson.toJson(t);
    }
}