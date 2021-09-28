package com.alsritter.service.search.common;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 例如这里定义一个论坛帖子的 Doc
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@Document(indexName = "topics")
public class TopicDoc {

    /**
     * topicId
     */
    @Id
    private Long topicId;
    /**
     * 标签
     */
    private String tag;
    /**
     * 文章标题  要分词  FieldType.Text
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String title;
    /**
     * 内容
     */
    @Field(type = FieldType.Text,analyzer = "ik_max_word",searchAnalyzer = "ik_max_word")
    private String content;
}
