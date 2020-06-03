package cn.itcast;


import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;

import java.io.IOException;

/**
 * <p>
 * <code>Test8</code>
 * </p>
 * 排序
 * @author huiwang45@iflytek.com
 * @description
 * @date 2020/05/26 16:45
 */
public class Test8 {

    public static void main(String[] args) throws IOException {

        //1连接rest接口
        HttpHost httpHost = new HttpHost("192.168.118.128",9200,"http");
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);

        //2.封装请求对象
        SearchRequest searchRequest = new SearchRequest("sku");
        //设置查询类型
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //通过查询构建器工具构建查询条件
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("name", "手机");
        searchSourceBuilder.query(queryBuilder);

        //searchSourceBuilder.sort("price",SortOrder.DESC);
        searchSourceBuilder.sort("price",SortOrder.valueOf("DESC"));


        searchRequest.source(searchSourceBuilder);
        //3.获取执行结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits searchHits = searchResponse.getHits();
        long totalHits = searchHits.getTotalHits();
        System.out.println("记录数:"+totalHits);
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String source = hit.getSourceAsString();
            System.out.println(source);
        }
        restHighLevelClient.close();

    }
}
