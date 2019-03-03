package com.jene.cognitive.model.cep.fraud;

import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.model.cep.IWarningPattern;
import org.apache.flink.cep.pattern.Pattern;
import org.apache.flink.cep.pattern.conditions.IterativeCondition;
import org.apache.flink.cep.pattern.conditions.SimpleCondition;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.List;
import java.util.Map;

/**
 * @uthor Jorge Nieves
 */

public class FraudWarningPattern  implements IWarningPattern<Transaction, FraudWarning> {

    public FraudWarningPattern() {}

    @Override
    public FraudWarning create(Map<String, List<Transaction>> pattern) {
        Transaction first = pattern.get("first").get(0);
        Transaction second = pattern.get("second").get(0);

        return new FraudWarning(first, second);
    }

    @Override
    public Pattern<Transaction, ?> getEventPattern() {
        return Pattern
                .<Transaction>begin("first").where(

                        new SimpleCondition<Transaction>() {
                            @Override
                            public boolean filter(Transaction event) throws Exception {
                                return event.getAmount() <= -500;
                            }
                        })
                .next("second").where(new IterativeCondition<Transaction>() {
                    @Override
                    public boolean filter(Transaction event, IterativeCondition.Context<Transaction> ctx) throws Exception {
                        if (event.getAmount() >= -500) {
                            return false;
                        }
                        for (Transaction firstEvent : ctx.getEventsForPattern("first")) {
                            if (event.getAccountId().equals(firstEvent.getAccountId())) {
                                return true;
                            }
                        }
                        return false;
                    }
                })
                .within(Time.days(7));
    }

    @Override
    public Class<FraudWarning> getWarningTargetType() {
        return FraudWarning.class;
    }
}