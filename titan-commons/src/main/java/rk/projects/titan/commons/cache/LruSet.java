package rk.projects.titan.commons.cache;

import reactor.core.publisher.Mono;

public interface LruSet<K> {

  Mono<K> get(K k);

  Mono<K> set(K k);

  Mono<Long> rem(K k);
}
