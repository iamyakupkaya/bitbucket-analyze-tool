package com.orion.bitbucket.Bitbucket.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Helper {

    @SuppressWarnings("unchecked")
	public static <T> Map<T, Long> count(List<T> inputList) {
		return (Map<T, Long>) inputList.stream().collect(Collectors.groupingBy(new Function<Object, Object>() {
			public Object apply(Object k) {
				return k;
			}
		}, Collectors.counting()));
	}
    
}
