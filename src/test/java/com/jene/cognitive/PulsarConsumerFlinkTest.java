package com.jene.cognitive;

import com.jene.cognitive.async.RestAsyncFunction;
import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.model.cep.fraud.FraudWarning;
import com.jene.cognitive.model.cep.fraud.FraudWarningPattern;
import com.jene.cognitive.sink.EsSink;
import com.jene.cognitive.utils.TransactionTimeExtractor;
import com.jene.cognitive.utils.source.TransactionsUnlimitedSource;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.async.AsyncFunction;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.jene.cognitive.StreamCEP.toWarning;


/**
 * @uthor Jorge Nieves
 */
public class PulsarConsumerFlinkTest {

    private static final String serviceUrl = "pulsar://171.124.11.11:6650";
    private StreamExecutionEnvironment env;
    private static final String elastichost = "127.0.0.1";

    @Before
    public void setUp(){
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().disableSysoutLogging();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.setParallelism(1);
        this.env = env;
    }

    static class Output{
        public static int count;
    }

    @Test
    public void testMockInput(){

        DataStream<Transaction> input = env.addSource(new SourceFunction<Transaction>() {
            @Override
            public void run(SourceContext<Transaction> ctx) throws Exception {
                ctx.collect(new Transaction(Long.parseLong("1900000000644"),  new Date(), new BigInteger(String.valueOf(Instant.now().toEpochMilli())), "",
                                -692.72, "6767-2620-9378-9803-463", "", "",
                                "LA LLACUNA", "ARA", "", "ES",  new Date(), "",
                                "", "ES6373310907560000001059","ES6373310907560000001059", "",
                                2468.76, "", "", "",
                                null, null);
                ctx.collect(new Transaction(Long.parseLong("1900000000645"),  new Date(), new BigInteger(String.valueOf(Instant.now().toEpochMilli())), "",
                        -692.72, "6767-2620-9378-9803-463", "", "",
                        "LA LLACUNA", "ARA", "", "ES",  new Date(), "",
                        "", "ES6373310907560000001059","ES6373310907560000001059", "",
                        2468.76, "", "", "",
                        null, null);
            }

            @Override
            public void cancel() {

            }
        }).assignTimestampsAndWatermarks(new TransactionTimeExtractor.Bounded(Time.seconds(5)));

        input.keyBy("transactionId")
                .window(TumblingEventTimeWindows.of(Time.of(3, TimeUnit.MILLISECONDS)))
                .reduce(new ReduceFunction<Transaction>() {
                    @Override
                    public Transaction reduce(Transaction value1, Transaction value2) throws Exception {
                        return value1;
                    }
                }).addSink(new SinkFunction<Transaction>() {
            @Override
            public void invoke(Transaction value, Context context) throws Exception {
                Output.count = Output.count + 1;
            }
        });

        try {
            env.execute("Pulsar Transactions Stream");
            Thread.sleep(2000);
        } catch (Exception  ex) {
            System.out.println(ex.toString());
        }

        assert (Output.count > 0);
    }

    @Test
    public void testCepHighValues(){
        DataStream<Transaction> input = env.addSource(new SourceFunction<Transaction>() {
            @Override
            public void run(SourceContext<Transaction> ctx) throws Exception {
                ctx.collect(new Transaction(Long.parseLong("1900000000644"),  new Date(), new BigInteger(String.valueOf(Instant.now().toEpochMilli())), "",
                        -692.72, "6767-2620-9378-9803-463", "", "",
                        "LA LLACUNA", "ARA", "", "ES",  new Date(), "",
                        "", "ES6373310907560000001059","ES6373310907560000001059", "",
                        2468.76, "", "", "",
                        null, null);
                ctx.collect(new Transaction(Long.parseLong("1900000000645"),  new Date(), new BigInteger(String.valueOf(Instant.now().toEpochMilli())), "",
                        -692.72, "6767-2620-9378-9803-463", "", "",
                        "LA LLACUNA", "ARA", "", "ES",  new Date(), "",
                        "", "ES6373310907560000001059","ES6373310907560000001059", "",
                        2468.76, "", "", "",
                        null, null);
            }

            @Override
            public void cancel() {

            }
        }).assignTimestampsAndWatermarks(new TransactionTimeExtractor.Bounded(Time.seconds(5)));


        final HighValueCEP highValueCep = new HighValueCEP(serviceUrl, "fraud-test", 1, elastichost, "treasury", "highvalue");
        highValueCep.stream(input).addSink(new SinkFunction<FraudWarning>() {
            @Override
            public void invoke(FraudWarning value, Context context) throws Exception {
                Output.count = Output.count + 1;
            }
        });


        try {
            env.execute("Pulsar Transactions Stream");
            Thread.sleep(2000);
        } catch (Exception  ex) {
            System.out.println(ex.toString());
        }

        assert (Output.count > 0);
    }
}
