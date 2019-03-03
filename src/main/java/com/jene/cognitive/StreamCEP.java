package com.jene.cognitive;

import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.model.cep.IWarning;
import com.jene.cognitive.model.cep.IWarningPattern;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.api.java.typeutils.GenericTypeInfo;
import org.apache.flink.cep.CEP;
import org.apache.flink.cep.PatternSelectFunction;
import org.apache.flink.cep.PatternStream;
import org.apache.flink.streaming.api.datastream.DataStream;

import java.util.List;
import java.util.Map;

/**
 * @author Jorge Nieves (jene)
 */
public class StreamCEP {

    public static <TWarningType extends IWarning> DataStream<TWarningType> toWarning(DataStream<Transaction> localTransactionsDataStream, IWarningPattern<Transaction, TWarningType> warningPattern) {
        PatternStream<Transaction> tempPatternStream = CEP.pattern(
                localTransactionsDataStream.keyBy(new KeySelector<Transaction, String>() {
                    @Override
                    public String getKey(Transaction localTx) throws Exception {
                        return localTx.getAccountId();
                    }
                }),
                warningPattern.getEventPattern());

        DataStream<TWarningType> warnings = tempPatternStream.select(new PatternSelectFunction<Transaction, TWarningType>() {
            @Override
            public TWarningType select(Map<String, List<Transaction>> map) throws Exception {
                return warningPattern.create(map);
            }
        }, new GenericTypeInfo<TWarningType>(warningPattern.getWarningTargetType()));

        return warnings;
    }
}
