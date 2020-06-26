package rk.projects.titan.commons.cache;

import reactor.core.publisher.Mono;

public interface KeyValueStore<K, V> {

  Mono<V> get(K k);

  Mono<V> set(K k, V v);
}
