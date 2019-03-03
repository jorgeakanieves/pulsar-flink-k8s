package com.jene.cognitive.async;

/**
 * @uthor Jorge Nieves
 */

import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.utils.LocationIqGeolocation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;
import org.apache.flink.util.ExecutorUtils;

import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RestAsyncFunction extends RichAsyncFunction<Transaction, Transaction> {
    private static final long serialVersionUID = 2098635244857937717L;

    private transient ExecutorService executorService;

    private final long shutdownWaitTS;

    public RestAsyncFunction(long shutdownWaitTS) {
        this.shutdownWaitTS = shutdownWaitTS;
    }

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        executorService = Executors.newFixedThreadPool(3);
    }

    @Override
    public void close() throws Exception {
        super.close();
        ExecutorUtils.gracefulShutdown(shutdownWaitTS, TimeUnit.MILLISECONDS, executorService);
    }

    @Override
    public void asyncInvoke(final Transaction t, final ResultFuture<Transaction> resultFuture) {
        executorService.submit(() -> {
            try {
                t.setPeerLocation(LocationIqGeolocation.getGeo(t.getPeerAddress()));
                resultFuture.complete(Collections.singleton(t));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        });
    }
}