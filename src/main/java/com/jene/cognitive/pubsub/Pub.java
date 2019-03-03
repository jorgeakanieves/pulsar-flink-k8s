package com.jene.cognitive.pubsub;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.IntStream;

/**
 * Publisher
 /**
 * @uthor Jorge Nieves
 * 
 * <p>Example usage:
 *   --service-url pulsar://pulsar_cluster:6650 --input-topic input-topic --content "{\"operationDate\":\"2014-12-31T16:23:49+0000\",\"timeMilis\":1420043099000}"
 */
public class Pub {
  private static final Logger LOG = LoggerFactory.getLogger(Pub.class);
  public static void main(String[] args) throws IOException {
    final ParameterTool parameterTool = ParameterTool.fromArgs(args);
    if (parameterTool.getNumberOfParameters() < 2) {
      LOG.info("Missing parameters!");
      LOG.info("Usage: pulsar --service-url <pulsar-service-url> --input-topic <topic> --content <content>");
      return;
    }
    String serviceUrl = parameterTool.getRequired("service-url");
    String inputTopic = parameterTool.getRequired("input-topic");
    String content = parameterTool.get("content");

    LOG.info("Parameters:");
    LOG.info("\tServiceUrl:\t" + serviceUrl);
    LOG.info("\tInputTopic:\t" + inputTopic);
    LOG.info("\tContent:\t" + content);

    LOG.info("Created producer for the topic {}" + inputTopic);
    produceString(content, serviceUrl, inputTopic);
  }

  public static void produceBytes(String value, String serviceUrl, String inputTopic) throws IOException {
    PulsarClient client = PulsarClient.builder()
            .serviceUrl(serviceUrl)
            .enableTls(true)
            .build();
    Producer<byte[]> producer = client.newProducer()
            .topic(inputTopic)
            .compressionType(CompressionType.LZ4)
            .create();
    Message<byte[]> msg = MessageBuilder.create()
            .setContent(value.getBytes())
            .build();
    try {
      MessageId msgId = producer.send(msg);
      LOG.info("Published message '{}' with the ID {}" + value +  msgId);

    } catch (PulsarClientException e) {
      LOG.error(e.getMessage());
    }
    producer.close();
//    client.close();
//    client.shutdown();
  }

  public static void produceString(String value, String serviceUrl, String inputTopic) throws IOException {
    PulsarClient client = PulsarClient.builder()
            .serviceUrl(serviceUrl)
            .build();
    Producer<String> stringProducer = client.newProducer(Schema.STRING)
            .topic(inputTopic)
            .create();
    stringProducer.send(value);
    LOG.info("Published message '{}'" + value);
    stringProducer.close();
    client.close();
    client.shutdown();
  }

}
