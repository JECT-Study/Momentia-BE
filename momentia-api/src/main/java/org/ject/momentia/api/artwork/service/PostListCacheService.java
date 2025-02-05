package org.ject.momentia.api.artwork.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class PostListCacheService {

	private final Map<String, Page<Long>> cache = new ConcurrentHashMap<>();

	public void put(String key, Page<Long> value) {
		cache.put(key, value);
	}

	public Page<Long> get(String key) {
		return cache.get(key);
	}
}
