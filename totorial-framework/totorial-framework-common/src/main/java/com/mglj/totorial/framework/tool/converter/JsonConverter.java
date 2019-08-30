package com.mglj.totorial.framework.tool.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mglj.totorial.framework.tool.logging.LogBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;

/**
 * Json转换工具
 * 
 * @author zsp
 *
 * @param <T>
 */
public class JsonConverter<T> implements StringConverter<T> {

	private static final Logger logger = LoggerFactory.getLogger(JsonConverter.class);
	
	private final ObjectMapper mapper = new ObjectMapper();
	
	@Override
	public String serialize(T value) {
		if(value != null) {
			if(value instanceof String) {
				return (String)value;
			}
			try {
				return mapper.writeValueAsString(value);
			} catch (JsonProcessingException e) {
				logger.error(LogBuilder.create().from("序列化错误", null, value), e);
			}
		}
		return null;
	}

	@Override
	public T deserialize(String value, Class<T> clazz) {
		if(value != null) {
			if(Objects.equals(String.class, clazz)) {
				return (T)value;
			}
			try {
				return mapper.readValue(value, clazz);
			} catch (IOException e) {
				logger.error(LogBuilder.create().from("反序列化错误", null, clazz, value), e);
			}
		}
		return null;
	}

}
