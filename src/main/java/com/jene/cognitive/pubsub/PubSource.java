package com.jene.cognitive.pubsub;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.utils.source.TransactionsUnlimitedSource;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.pulsar.client.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

/**
 * Publisher
 /**
 * @uthor Jorge Nieves
 * 
 * <p>Example usage:
 *   --service-url pulsar://pulsar_cluster:6650 --input-topic input-topic --content "{\"operationDate\":\"2014-12-31T16:23:49+0000\",\"timeMilis\":1420043099000}"
 */
public class PubSource {
  private static final Logger LOG = LoggerFactory.getLogger(PubSource.class);
  public static void main(String[] args) throws IOException {
    final ParameterTool parameterTool = ParameterTool.fromArgs(args);
    if (parameterTool.getNumberOfParameters() < 2) {
      LOG.info("Missing parameters!");
      LOG.info("Usage: pulsar --service-url <pulsar-service-url> --input-topic <topic>");
      return;
    }
    String serviceUrl = parameterTool.getRequired("service-url");
    String inputTopic = parameterTool.getRequired("input-topic");


    LOG.info("Parameters:");
    LOG.info("\tServiceUrl:\t" + serviceUrl);
    LOG.info("\tInputTopic:\t" + inputTopic);

    PulsarClient client = PulsarClient.builder()
            .serviceUrl(serviceUrl)
            .build();
    Producer<String> stringProducer = client.newProducer(Schema.STRING)
            .topic(inputTopic)
            .create();

    LOG.info("Created producer for the topic {}" + inputTopic);

    File file = new File(Thread.currentThread().getContextClassLoader().getResource("transactions").getFile());
    Scanner sc = new Scanner(file);
    sc.close();

    Scanner scanner = new Scanner(file);
    while (scanner.hasNextLine()) {
      String line = scanner.nextLine();

      stringProducer.send(line);
      LOG.info("Published message '{}'" + line);

    }
    scanner.close();

    stringProducer.close();
    client.close();

  }

}
