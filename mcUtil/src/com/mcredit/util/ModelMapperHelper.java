package com.mcredit.util;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;

public class ModelMapperHelper {

	private static ModelMapper MAPPER = null;

	private static ModelMapper getMapper() {
		if (MAPPER == null) {
			MAPPER = new ModelMapper();
			MAPPER.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
		}
		return MAPPER;
	}

	public static <S, T> T map(S source, Class<T> targetClass) {
		return getMapper().map(source, targetClass);
	}

	public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
		List<T> list = new ArrayList<>();
		for (S s : source) {
			list.add(getMapper().map(s, targetClass));
		}
		return list;
	}

	public static <S, T> void mapTo(S source, T dist) {
		getMapper().map(source, dist);
	}
}
