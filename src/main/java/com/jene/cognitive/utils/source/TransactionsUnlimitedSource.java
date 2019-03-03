package com.jene.cognitive.utils.source;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jene.cognitive.model.Transaction;
import com.jene.cognitive.pubsub.Pub;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @uthor Jorge Nieves
 */

public class TransactionsUnlimitedSource  extends RichParallelSourceFunction<Transaction> {
    private static final long serialVersionUID = 3589767994783688247L;
    private static final Logger LOG = LoggerFactory.getLogger(Pub.class);
    private boolean running = true;

    private final long pause;
    private static List<Transaction> samples;
    private Integer current;

    public TransactionsUnlimitedSource(long pause) {
        this.pause = pause;
        try {
            this.samples = this.getFileTransactions();
        }catch(FileNotFoundException ex){
            LOG.error(ex.getMessage());
        }
        this.current = 0;
    }

    @Override
    public void open(Configuration configuration) {
    }

    public void run(SourceContext<Transaction> sourceContext) throws Exception {
        while (running) {
            Random r = new Random();
            Transaction t = samples.get(r.nextInt(samples.size()+ 1));
            this.current++;
            t.setTimeMilis(new BigInteger(String.valueOf(Instant.now().toEpochMilli())));
            sourceContext.collect(t);
            Thread.sleep(pause);
        }
    }

    public static List<Transaction> getFileTransactions() throws FileNotFoundException {
        List<Transaction> sample = new ArrayList<Transaction>();
        File file = new File(Thread.currentThread().getContextClassLoader().getResource("transactions")
                .getFile());
        Scanner sc = new Scanner(file);
        sc.close();

        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").create();
            Transaction transaction = (Transaction) gson.fromJson(line, Transaction.class);
            sample.add(transaction);
        }
        scanner.close();
        return sample;
    }

    public void cancel() {
        running = false;
    }

}