package com.jene.cognitive.pubsub;


import java.io.IOException;

import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.Message;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.SubscriptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Subscriber
 *
 * @uthor Jorge Nieves
 *
 * <p>Example usage:
 *   --service-url pulsar://pulsar_cluster:6650 --input-topic input-topic --subscription subscription --content "{\"operationDate\":\"2014-12-31T16:23:49+0000\",\"timeMilis\":1420043099000}"
 */

public class Sub {
  private static final Logger LOG = LoggerFactory.getLogger(Pub.class);
  public static void main(String[] args) throws IOException {
    final ParameterTool parameterTool = ParameterTool.fromArgs(args);
    if (parameterTool.getNumberOfParameters() < 2) {
      LOG.info("Missing parameters!");
      LOG.info("Usage: pulsar --service-url <pulsar-service-url> --input-topic <topic> --subscription <subscription> --content <content>");
      return;
    }
    String serviceUrl = parameterTool.getRequired("service-url");
    String inputTopic = parameterTool.getRequired("input-topic");
    String subscription = parameterTool.getRequired("subscription");
    String content = parameterTool.get("content");
    LOG.info("Parameters:");
    LOG.info("\tServiceUrl:\t" + serviceUrl);
    LOG.info("\tInputTopic:\t" + inputTopic);
    LOG.info("\tSubscription:\t" + subscription);
    LOG.info("\tContent:\t" + content);
    PulsarClient client = PulsarClient.builder()
            .serviceUrl(serviceUrl)
            .build();
    Consumer<byte[]> consumer = client.newConsumer()
            .topic(inputTopic)
            .subscriptionType(SubscriptionType.Shared)
            .subscriptionName(subscription)
            .subscribe();
    LOG.info("Created consumer for the topic {}", inputTopic);

    do {
      Message<byte[]> msg = consumer.receive();
      String c = new String(msg.getData());
      LOG.info("Received message '{}' with ID {}", c, msg.getMessageId());
      consumer.acknowledge(msg);
    } while (true);
  }
}
