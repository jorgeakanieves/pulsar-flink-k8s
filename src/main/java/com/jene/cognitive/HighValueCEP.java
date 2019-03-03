package com.jene.cognitive;

import com.jene.cognitive.model.Transaction;
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
public class HighValueCEP extends StreamCEP{
    private static String serviceUrl;
    private static String topic;
    private int parallelism;
    private static String elastichost;
    private static String index;
    private static String type;


    public HighValueCEP(String serviceUrl, String outputTopic, int parallelism, String elastichost, String index, String type) {
        this.serviceUrl = serviceUrl;
        this.topic = outputTopic;
        this.parallelism = parallelism;
        this.elastichost = elastichost;
        this.index = index;
        this.type = type;
    }

    public DataStream<FraudWarning> stream(DataStream<Transaction> input){
        DataStream<FraudWarning> highValWarnings =  toWarning(input, new FraudWarningPattern());
        return highValWarnings;
    }

    public void sink(DataStream<FraudWarning> highValWarnings){
        highValWarnings.print().setParallelism(parallelism);

        highValWarnings.addSink(new FlinkPulsarProducer<>(
                serviceUrl,
                topic,
                FraudWarning -> FraudWarning.toString().getBytes(UTF_8),
                new ProducerConfiguration(),
                FraudWarning -> FraudWarning.toString()
        )).setParallelism(parallelism);

        EsSink esFraudSink = new EsSink(elastichost, index, type);
        highValWarnings.addSink(esFraudSink.getBuilder());
    }
}