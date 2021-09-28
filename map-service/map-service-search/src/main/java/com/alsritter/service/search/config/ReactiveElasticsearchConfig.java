package com.alsritter.service.search.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.reactive.ReactiveElasticsearchClient;
import org.springframework.data.elasticsearch.client.reactive.ReactiveRestClients;
import org.springframework.data.elasticsearch.config.AbstractReactiveElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

/**
 * 方法配置了 ES Data 的响应式客户端，并通过注解启用 ReactiveElasticsearchRepositories
 *
 * @author alsritter
 * @version 1.0
 **/
@Configuration
@EnableReactiveElasticsearchRepositories
public class ReactiveElasticsearchConfig extends AbstractReactiveElasticsearchConfiguration {
    @Override
    public ReactiveElasticsearchClient reactiveElasticsearchClient() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo("localhost:19200")
                .build();
        return ReactiveRestClients.create(clientConfiguration);
    }
}