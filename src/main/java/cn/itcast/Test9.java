package cn.itcast;


import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;

import java.io.IOException;
import java.util.Map;

/**
 * <p>
 * <code>Test9</code>
 * </p>
 * 高亮
 * @author huiwang45@iflytek.com
 * @description
 * @date 2020/05/26 16:45
 */
public class Test9 {

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

        //设置高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("name").preTags("<font style='color:red'>").postTags("</font>");
        searchSourceBuilder.highlighter(highlightBuilder);
        searchRequest.source(searchSourceBuilder);

        //3.获取执行结果
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest);
        SearchHits searchHits = searchResponse.getHits();
        long totalHits = searchHits.getTotalHits();
        System.out.println("记录数:"+totalHits);
        SearchHit[] hits = searchHits.getHits();
        System.out.println("高亮结果");
        for (SearchHit hit : hits) {
            //String source = hit.getSourceAsString();
            //System.out.println(source);
            Map<String, HighlightField> highlightFields = hit.getHighlightFields();
            HighlightField name = highlightFields.get("name");
            Text[] fragments = name.fragments();
            String s = fragments[0].toString();
            System.out.println(s);
        }
        restHighLevelClient.close();

    }
}
