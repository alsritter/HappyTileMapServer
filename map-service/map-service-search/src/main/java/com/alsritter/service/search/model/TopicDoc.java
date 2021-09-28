package com.alsritter.service.search.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.Instant;

/**
 * 例如这里定义一个论坛帖子的 Doc
 *
 * @author alsritter
 * @version 1.0
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "topics")
public class TopicDoc {

    /**
     * topicId
     */
    @Id
    @NonNull
    private Long topicId;
    /**
     * 标签
     */
    @Field(type = FieldType.Keyword)
    private String tag;
    /**
     * 文章标题  要分词  FieldType.Text
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String title;
    /**
     * 内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word")
    private String content;
    /**
     * Instant 也是一种时间类型
     */
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
    private Instant createTime;
}
