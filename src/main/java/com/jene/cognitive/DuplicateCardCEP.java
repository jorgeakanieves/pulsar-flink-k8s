package com.jene.cognitive;

import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.model.cep.dupCard.DuplicateCardWarning;
import com.jene.cognitive.model.cep.dupCard.DuplicateCardWarningPattern;
import com.jene.cognitive.model.cep.fraud.FraudWarning;
import com.jene.cognitive.model.cep.fraud.FraudWarningPattern;
import com.jene.cognitive.sink.EsSink;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.connectors.pulsar.FlinkPulsarProducer;
import org.apache.pulsar.client.api.ProducerConfiguration;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author Jorge Nieves (jene)
 */
public class DuplicateCardCEP extends StreamCEP{
    private static String serviceUrl;
    private static String topic;
    private int parallelism;
    private static String elastichost;
    private static String index;
    private static String type;


    public DuplicateCardCEP(String serviceUrl, String outputTopic, int parallelism, String elastichost, String index, String type) {
        this.serviceUrl = serviceUrl;
        this.topic = outputTopic;
        this.parallelism = parallelism;
        this.elastichost = elastichost;
        this.index = index;
        this.type = type;
    }

    public void stream(DataStream<Transaction> input){
        DataStream<DuplicateCardWarning> dupCardWarning =  toWarning(input, new DuplicateCardWarningPattern());
        dupCardWarning.print().setParallelism(1);

        dupCardWarning.addSink(new FlinkPulsarProducer<>(
                serviceUrl,
                topic,
                DuplicateCardWarning -> DuplicateCardWarning.toString().getBytes(UTF_8),
                new ProducerConfiguration(),
                DuplicateCardWarning -> DuplicateCardWarning.toString()
        )).setParallelism(parallelism);

        EsSink esDupCardSing = new EsSink(elastichost, index, type);
        dupCardWarning.addSink(esDupCardSing.getBuilder());
    }
}