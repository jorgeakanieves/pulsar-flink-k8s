package com.jene.cognitive;

import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.utils.source.InputPulsarStream;
import org.apache.flink.api.common.restartstrategy.RestartStrategies;
import org.apache.flink.api.java.utils.ParameterTool;
import org.apache.flink.streaming.api.CheckpointingMode;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a streaming transaction processing on pulsar topics.
 *
 * <p>Example usage:
 *   --service-url pulsar://pulsar_cluster:6650 --input-topic tx_in --subscription tx_sub --output-topic tx_out --elasticsearch elastic_cluster
 *   --high-value-topic high_value --dup-card-topic dup_card --tx-index transactions --tx-type transaction --fraud-index treasury
 *   --fraud-type highvalue --dup-card-index fraud --dup-card-type dup_cards --countries-index accounts --countries-type countries
 *   --log-topic log_tx --parallelism 1
 *
 *   @uthor Jorge Nieves
 */
public class PulsarConsumerFlink {

    private static final Logger LOG = LoggerFactory.getLogger(PulsarConsumerFlink.class);

    public static void main(String[] args) throws Exception {
        final ParameterTool parameterTool = ParameterTool.fromArgs(args);

        if (parameterTool.getNumberOfParameters() < 2) {
            LOG.info("Missing parameters!");
            LOG.info("Usage: --service-url <pulsar-service-url> --input-topic <topic> --subscription <sub> " +
                    "--output-topic <topic> --elasticsearch <hostname> --high-value-topic <topic> --dup-card-topic <topic> " +
                    "--tx-index <index> --tx-type <type> --fraud-index <index> --fraud-type <type> --dup-card-index <index> --dup-card-type " +
                    "<type> --countries-index <index> --countries-type <type> --log-topic <topic> parallelism <parallelism>");
            return;
        }

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.getConfig().disableSysoutLogging();
        env.getConfig().setRestartStrategy(RestartStrategies.fixedDelayRestart(4, 10000));
        env.enableCheckpointing(5000);
        env.getConfig().setGlobalJobParameters(parameterTool);
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        env.enableCheckpointing(5000, CheckpointingMode.EXACTLY_ONCE);

        String serviceUrl = parameterTool.getRequired("service-url");
        String inputTopic = parameterTool.getRequired("input-topic");
        String subscription = parameterTool.get("subscription", "tx_sub");
        String outputTopic = parameterTool.get("output-topic", "tx_out");
        String elasticHost = parameterTool.get("elasticsearch", "elastichost");
        String fraudTopic = parameterTool.get("high-value-topic", "highValueTopic");
        String dupliCardTopic = parameterTool.get("dup-card-topic", "dup_card");
        String txIndex = parameterTool.get("tx-index", "transactions");
        String txType = parameterTool.get("tx-type", "transaction");
        String fraudIndex = parameterTool.get("fraud-index", "treasury");
        String fraudType = parameterTool.get("fraud-type", "highvalue");
        String dupliCardIndex = parameterTool.get("dup-card-index", "fraud");
        String dupliCardType = parameterTool.get("dup-card-type", "dup-cards");
        String countriesIndex = parameterTool.get("countries-index", "accounts");
        String countriesType = parameterTool.get("countries-type", "countries");
        String logTopic = parameterTool.get("log-topic", "log-topic");

        int parallelism = parameterTool.getInt("parallelism", 1);

        LOG.info("Parameters:");
        LOG.info("\tServiceUrl:\t" + serviceUrl);
        LOG.info("\tInputTopic:\t" + inputTopic);
        LOG.info("\tSubscription:\t" + subscription);
        LOG.info("\tOutputTopic:\t" + outputTopic);
        LOG.info("\tElasticHost:\t" + elasticHost);
        LOG.info("\tfraudTopic:\t" + fraudTopic);
        LOG.info("\tdupliCardTopic:\t" + dupliCardTopic);
        LOG.info("\ttxIndex:\t" + txIndex);
        LOG.info("\ttxType:\t" + txType);
        LOG.info("\tfraudIndex:\t" + fraudIndex);
        LOG.info("\tfraudType:\t" + fraudType);
        LOG.info("\tdupliCardIndex:\t" + dupliCardIndex);
        LOG.info("\tdupliCardType:\t" + dupliCardType);
        LOG.info("\tcountriesIndex:\t" + countriesIndex);
        LOG.info("\tcountriesType:\t" + countriesType);
        LOG.info("\tlogTopic:\t" + logTopic);

        LOG.info("\tParallelism:\t" + parallelism);

        // input
        final InputPulsarStream inputStream = new InputPulsarStream(env, serviceUrl, inputTopic, subscription);
        DataStream<Transaction> input = inputStream.getStream();
        // side log
        final SideLog sideLog = new SideLog(serviceUrl, logTopic, parallelism);
        sideLog.stream(input);
        // async rest geolocation
        final AsyncGeo asyncGeo = new AsyncGeo(serviceUrl, logTopic, parallelism, elasticHost, txIndex, txType);
        DataStream<Transaction> asyncStream = asyncGeo.stream(input);
        // high value
        final HighValueCEP highValueCep = new HighValueCEP(serviceUrl, fraudTopic, parallelism, elasticHost, fraudIndex, fraudType);
        highValueCep.sink(highValueCep.stream(asyncStream));
        // duplicated card
        final DuplicateCardCEP dupCardCep = new DuplicateCardCEP(serviceUrl, dupliCardTopic, parallelism, elasticHost, dupliCardIndex, dupliCardType);
        dupCardCep.stream(asyncStream);
        // windowing accumulator countries by account
        final WindowCountriesByAccount windowCountrAcc = new WindowCountriesByAccount(serviceUrl, parallelism, elasticHost, countriesIndex, countriesType);
        windowCountrAcc.stream(asyncStream);

        env.execute("Pulsar Transactions Stream");
    }




}

