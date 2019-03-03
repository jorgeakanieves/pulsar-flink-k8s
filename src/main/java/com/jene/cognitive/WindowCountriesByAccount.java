package com.jene.cognitive;

import com.jene.cognitive.model.AccountResult;
import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.sink.EsSink;
import com.jene.cognitive.utils.FoldTransactionsToResult;
import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

/**
 * @author Jorge Nieves (jene)
 */
public class WindowCountriesByAccount {
    private static String serviceUrl;
    private int parallelism;
    private static String elastichost;
    private static String index;
    private static String type;


    public WindowCountriesByAccount(String serviceUrl, int parallelism, String elastichost, String index, String type) {
        this.serviceUrl = serviceUrl;
        this.parallelism = parallelism;
        this.elastichost = elastichost;
        this.index = index;
        this.type = type;
    }

    public void stream(DataStream<Transaction> input){
        DataStream<AccountResult> accountStream = input.keyBy("accountId")
                //.timeWindow(Time.seconds(5))
                .window(TumblingEventTimeWindows.of(Time.seconds(10), Time.seconds(2)))
                .fold(new AccountResult(), new FoldTransactionsToResult())
                .filter(new FilterFunction<AccountResult>() {
                    @Override
                    public boolean filter(AccountResult value) throws Exception {
                        if(value.getCountryCounts().size()>1) return true;
                        return false;
                    }
                });

        accountStream.print().setParallelism(1);

        EsSink esSinkCountries = new EsSink(elastichost, index, type);
        accountStream.addSink(esSinkCountries.getBuilder());
    }
}
