package com.jene.cognitive.utils.source;

import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.serde.JsonSerdeTransaction;
import com.jene.cognitive.utils.TransactionTimeExtractor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.pulsar.PulsarSourceBuilder;

/**
 * @author Jorge Nieves (jene)
 */
public class InputPulsarStream {
    private StreamExecutionEnvironment env;
    private static String serviceUrl;
    private static String topic;
    private static String subscription;

    public InputPulsarStream(StreamExecutionEnvironment env, String serviceUrl, String inputTopic, String subscription){
        this.env = env;
        this.serviceUrl = serviceUrl;
        this.topic = inputTopic;
        this.subscription = subscription;
    }

    public DataStream<Transaction> getStream (){
        PulsarSourceBuilder<Transaction> builder = PulsarSourceBuilder.builder(new JsonSerdeTransaction(TypeInformation.of(Transaction.class)))
                .serviceUrl(serviceUrl)
                .topic(topic)
                .subscriptionName(subscription);
        SourceFunction<Transaction> src = builder.build();

        //DataStream<Transaction> input = env.addSource(new TransactionsUnlimitedSource(500));
        /*
        DataStream<Transaction> input = env.addSource(new TransactionsUnlimitedSource(500))
                .assignTimestampsAndWatermarks(new TransactionTimeExtractor.Bounded(Time.seconds(5)));
        */
        return env.addSource(src)
                .assignTimestampsAndWatermarks(new TransactionTimeExtractor.Bounded(Time.seconds(5)));
        //.assignTimestampsAndWatermarks(new TransactionTimeExtractor.Ascending());

    }
}
