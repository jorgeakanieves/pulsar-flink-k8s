package com.jene.cognitive.sink;

import com.google.gson.Gson;
import org.apache.flink.api.common.functions.RuntimeContext;
import org.apache.flink.streaming.connectors.elasticsearch.ElasticsearchSinkFunction;
import org.apache.flink.streaming.connectors.elasticsearch.RequestIndexer;
import org.apache.flink.streaming.connectors.elasticsearch6.ElasticsearchSink;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Requests;
import org.elasticsearch.common.xcontent.XContentType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @uthor Jorge Nieves
 */

public class EsSink implements Serializable{
    private String host;
    private String index;
    private String type;
    private ElasticsearchSink sink;

    public EsSink(String host, String index, String type) {
        this.host = host;
        this.index = index;
        this.type = type;
        create();
    }
    public ElasticsearchSink getBuilder(){
        return sink;
    }
    private void create(){
        ElasticsearchSink.Builder<Object> builder = builder();
        builder.setBulkFlushMaxActions(1);
        sink =  builder.build();
    }

    public ElasticsearchSink.Builder<Object> builder() {

        List<HttpHost> httpHosts = new ArrayList<>();
        httpHosts.add(new HttpHost(host, 9200, "http"));

        ElasticsearchSink.Builder<Object> esSinkBuilder = new ElasticsearchSink.Builder<>(
                httpHosts,
                new ElasticsearchSinkFunction<Object>() {
                    public IndexRequest createIndexRequest(Object element) {
                        Gson gson = new Gson();
                        String json = gson.toJson(element);
                        return Requests.indexRequest()
                                .index(index)
                                .type(type)
                                .source(json, XContentType.JSON);
                    }

                    @Override
                    public void process(Object element, RuntimeContext ctx, RequestIndexer indexer) {
                        indexer.add(createIndexRequest(element));
                    }
                }
        );
        return esSinkBuilder;
    }
}
