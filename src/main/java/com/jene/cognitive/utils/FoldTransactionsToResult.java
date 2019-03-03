package com.jene.cognitive.utils;

import com.jene.cognitive.model.AccountResult;
import com.jene.cognitive.model.Transaction;
import org.apache.flink.api.common.functions.FoldFunction;

import java.time.Instant;

/**
 * @uthor Jorge Nieves
 */

public class FoldTransactionsToResult implements FoldFunction<Transaction, AccountResult> {

    @Override
    public AccountResult fold(AccountResult accumulator, Transaction value) throws Exception {
        accumulator.setCount(accumulator.getCount() + 1);
        accumulator.setAccountId(value.getAccountId());
        if(!accumulator.getCities().contains(value.getPeerCity())) {
            accumulator.getCities().add(value.getPeerCity());
        }
        if(!accumulator.getCountries().contains(value.getPeerCountry())) {
            accumulator.getCountries().add(value.getPeerCountry());
        }
        if(accumulator.getMinTimestamp() == 0) {
            accumulator.setMinTimestamp(Long.parseLong(value.getTimeMilis().toString()));
            accumulator.setMaxTimestamp(Long.parseLong(value.getTimeMilis().toString()));
        } else {
            Instant d1 = Instant.ofEpochMilli(accumulator.getMinTimestamp());
            Instant d2 = Instant.ofEpochMilli(Long.parseLong(value.getTimeMilis().toString()));
            if(d1.isBefore(d2)) {
                accumulator.setMinTimestamp(d1.toEpochMilli());
                accumulator.setMaxTimestamp(d2.toEpochMilli());
            } else {
                accumulator.setMinTimestamp(d2.toEpochMilli());
                accumulator.setMaxTimestamp(d1.toEpochMilli());
            }
        }
        if (accumulator.getCityCounts().containsKey(value.getPeerCity())){
            accumulator.getCityCounts().put(value.getPeerCity(), accumulator.getCityCounts().get(value.getPeerCity())+1);
        } else {
            accumulator.getCityCounts().put(value.getPeerCity(), 1);
        }
        if (accumulator.getCityAmounts().containsKey(value.getPeerCity())){
            accumulator.getCityAmounts().put(value.getPeerCity(), accumulator.getCityAmounts().get(value.getPeerCity())+value.getAmount());
        } else {
            accumulator.getCityAmounts().put(value.getPeerCity(), value.getAmount());
        }
        if (accumulator.getCountryCounts().containsKey(value.getPeerCountry())){
            accumulator.getCountryCounts().put(value.getPeerCountry(), accumulator.getCountryCounts().get(value.getPeerCountry())+1);
        } else {
            accumulator.getCountryCounts().put(value.getPeerCountry(), 1);
        }
        accumulator.setResult(accumulator.getResult());
        return accumulator;
    }
}