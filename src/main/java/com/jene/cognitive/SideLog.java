package com.jene.cognitive;

import com.jene.cognitive.model.Transaction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.connectors.pulsar.FlinkPulsarProducer;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;
import org.apache.pulsar.client.api.ProducerConfiguration;

import java.io.Serializable;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Jorge Nieves (jene)
 */
public class SideLog implements Serializable{
    private static String serviceUrl;
    private static String topic;
    private int parallelism;

    public SideLog(String serviceUrl, String outputTopic, int parallelism){
        this.serviceUrl = serviceUrl;
        this.topic = outputTopic;
        this.parallelism = parallelism;
    }
    public void stream(DataStream<Transaction> input){
        final OutputTag<String> outputTag = new OutputTag<String>("log-accounts"){};
        SingleOutputStreamOperator<Transaction> trxStream =
                input.process(new ProcessFunction<Transaction, Transaction>() {
                    @Override
                    public void processElement(
                            Transaction value,
                            Context ctx,
                            Collector<Transaction> out) throws Exception {
                        out.collect(value);
                        ctx.output(outputTag, String.valueOf(value.getAccountId()) + '|' + value.getTransactionId());
                    }
                });

        DataStream<Tuple2<String, String>> sideLogStream = trxStream.getSideOutput(outputTag)
                .map(new MapFunction<String, Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, String> map(String s) throws Exception {
                        String[] acc = s.split("\\|");
                        return new Tuple2<String, String>(acc[0],acc[1]);
                    }
                }).keyBy(0).reduce(new ReduceFunction<Tuple2<String, String>>() {
                    @Override
                    public Tuple2<String, String> reduce(Tuple2<String, String> value1, Tuple2<String, String> value2) throws Exception {
                        return new Tuple2<String, String>(value1.f0, value1.f1 + '|' + value2.f1);
                    }
                });
        sideLogStream.print().setParallelism(1);

        sideLogStream.addSink(new FlinkPulsarProducer<>(
                serviceUrl,
                topic,
                log-> log.toString().getBytes(UTF_8),
                new ProducerConfiguration(),
                log -> log.toString()
        )).setParallelism(parallelism);
    }
}
