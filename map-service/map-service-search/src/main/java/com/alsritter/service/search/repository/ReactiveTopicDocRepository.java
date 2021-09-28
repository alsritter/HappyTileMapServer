package com.alsritter.service.search.repository;

import com.alsritter.service.search.model.TopicDoc;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

/**
 * 反应式编程
 * 通过 Reactive 进行 TopicDoc 操作
 * 返回类型为 Flux<T> 或 Mono<T>
 * ReactiveSortingRepository 继承了 ReactiveCrudRepository，所以我们直接继承 ReactiveSortingRepository
 * 在写法上除了返回类型不同，其它与 TopicDocRepository 类似
 *
 * @author alsritter
 * @version 1.0
 **/
public interface ReactiveTopicDocRepository extends ReactiveSortingRepository<TopicDoc, Long> {
    Flux<TopicDoc> findByTag(String tag, Pageable pageable);
}
