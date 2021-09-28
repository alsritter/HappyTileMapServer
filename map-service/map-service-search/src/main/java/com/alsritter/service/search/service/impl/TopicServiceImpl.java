package com.alsritter.service.search.service.impl;

import com.alsritter.common.api.ResultCode;
import com.alsritter.common.exception.BusinessException;
import com.alsritter.service.search.model.TopicDoc;
import com.alsritter.service.search.repository.TopicDocRepository;
import com.alsritter.service.search.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author alsritter
 * @version 1.0
 **/
@Service
@RequiredArgsConstructor
public class TopicServiceImpl implements TopicService {
    private final TopicDocRepository topicDocRepository;
    private final ElasticsearchRestTemplate template;

    @Override
    public void saveTopic(TopicDoc topic) {
        try {
            topicDocRepository.save(topic);
        } catch (IllegalArgumentException e) {
            throw new BusinessException(ResultCode.ES_INSERT_FAILED, e);
        }
    }

    @Override
    public Page<TopicDoc> searchTopicByFull(String keyword) {
        NativeSearchQueryBuilder builder = new NativeSearchQueryBuilder();
        // 构建一个 Bool 查询
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.termQuery("tag", keyword))
                .should(QueryBuilders.matchQuery("title", keyword))
                .should(QueryBuilders.matchQuery("content", keyword));
        builder.withQuery(boolQueryBuilder);
        SearchHits<TopicDoc> search = template.search(builder.build(), TopicDoc.class);
        return convertTopicDoc(search);
    }


    private Page<TopicDoc> convertTopicDoc(SearchHits<TopicDoc> response) {
        if (response == null) return null;
        List<TopicDoc> result = response.stream().map(SearchHit::getContent).collect(Collectors.toList());
        return new AggregatedPageImpl<>(result);
    }
}
