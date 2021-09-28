package com.alsritter.service.search.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.alsritter.service.search.model.TopicDoc;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.concurrent.Future;

/**
 * 注意这里是接口
 * ElasticsearchRepository<要映射的类型，主键类型>
 * 这个接口里面有基础的 crud，所以需要继承这个接口
 *
 * 注意，这个实现类不需要自己实现，Spring Data 会根据方法名称自动实现功能
 * 具体参考：https://www.jianshu.com/p/930c803a4ebd
 *
 * @author alsritter
 * @version 1.0
 **/
public interface TopicDocRepository extends ElasticsearchRepository<TopicDoc, Long> {

    /**
     * 根据标题返回消息列表，这里的 Pageable 工具类除了可以支持分页，也支持了排序
     */
    Page<TopicDoc> findByTitle(String title, Pageable pageable);

    /**
     * 通过 Future 异步获取数据，Top 就是我们取数据集的第一条
     */
    @Async
    Future<TopicDoc> findTopByTag(String tag, Sort sort);

    /**
     * 复合查询条件，必须同时满足
     * {“bool” : {“must” : [ {“field” : {“title” : “?”}}, {“field” : {“tag” : “?”}} ]}}
     */
    Page<TopicDoc> findByTitleAndTag(String title, String tag, Pageable pageable);

    /**
     * 复合查询条件，满足其中之一
     * {“bool” : {“should” : [ {“field” : {“title” : “?”}}, {“field” : {“tag” : “?”}} ]}}
     */
    Page<TopicDoc> findByTitleOrTag(String title, String tag, Pageable pageable);
}
