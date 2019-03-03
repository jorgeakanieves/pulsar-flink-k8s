package com.jene.cognitive;

import com.jene.cognitive.async.RestAsyncFunction;
import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.sink.EsSink;
import org.apache.flink.streaming.api.datastream.AsyncDataStream;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.functions.async.AsyncFunction;
import org.apache.flink.streaming.connectors.pulsar.FlinkPulsarProducer;
import org.apache.pulsar.client.api.ProducerConfiguration;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Jorge Nieves (jene)
 */
public class AsyncGeo implements Serializable {
    private static String serviceUrl;
    private static String topic;
    private int parallelism;
    private static String elastichost;
    private static String index;
    private static String type;


    public AsyncGeo(String serviceUrl, String outputTopic, int parallelism, String elastichost, String index, String type){
        this.serviceUrl = serviceUrl;
        this.topic = outputTopic;
        this.parallelism = parallelism;
        this.elastichost = elastichost;
        this.index = index;
        this.type = type;
    }

    public DataStream<Transaction> stream(DataStream<Transaction> input){
        AsyncFunction<Transaction, Transaction> function =
                new RestAsyncFunction(20000);

        DataStream<Transaction> asyncStream = AsyncDataStream.unorderedWait(
                input,
                function,
                10000L,
                TimeUnit.MILLISECONDS,
                20).setParallelism(parallelism);

        asyncStream.print().setParallelism(parallelism);

        asyncStream.addSink(new FlinkPulsarProducer<>(
                serviceUrl,
                topic,
                Transaction -> Transaction.toString().getBytes(UTF_8),
                new ProducerConfiguration(),
                Transaction -> Transaction.toString()
        )).setParallelism(parallelism);

        EsSink esSinkTransactions = new EsSink(elastichost, index, type);
        asyncStream.addSink(esSinkTransactions.getBuilder());
        return asyncStream;
    }
}
