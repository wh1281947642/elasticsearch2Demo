package cn.itcast;


import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * <code>Test1</code>
 * </p>
 * 新增和修改数据
 * @author huiwang45@iflytek.com
 * @description
 * @date 2020/05/26 16:45
 */
public class Test1 {

    public static void main(String[] args) throws IOException {

        //1连接rest接口
        HttpHost httpHost = new HttpHost("192.168.118.128",9200,"http");
        RestClientBuilder restClientBuilder = RestClient.builder(httpHost);
        RestHighLevelClient restHighLevelClient = new RestHighLevelClient(restClientBuilder);

        //2.封装请求对象
        BulkRequest bulkRequest=new BulkRequest();
        IndexRequest indexRequest = new IndexRequest("sku","doc","10");
        Map skuMap =new HashMap();
        skuMap.put("name","华为p30pro新增新增");
        skuMap.put("brandName","华为");
        skuMap.put("categoryName","手机");
        skuMap.put("price",1010221);
        //skuMap.put("createTime","2014-11-05");
        skuMap.put("saleNum",101021);
        skuMap.put("commentNum",10102321);
        Map spec=new HashMap();
        spec.put("网络制式","移动4G");
        spec.put("屏幕尺寸","5");
        skuMap.put("spec",spec);
        indexRequest.source(skuMap);
        bulkRequest.add(indexRequest);

        //3.获取执行结果
        //IndexResponse response = restHighLevelClient.index(indexRequest);
        BulkResponse response = restHighLevelClient.bulk(bulkRequest);
        int status = response.status().getStatus();
        System.out.println(status);
        restHighLevelClient.close();

    }
}
