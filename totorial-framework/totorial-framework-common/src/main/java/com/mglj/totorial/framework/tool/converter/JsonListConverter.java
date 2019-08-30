package com.mglj.totorial.framework.tool.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mglj.totorial.framework.tool.logging.LogBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Json转换工具
 * 
 * @author zsp
 *
 * @param <T>
 */
public class JsonListConverter<T extends List<E>, E> implements StringConverter<T> {

	private static final Logger logger = LoggerFactory.getLogger(JsonListConverter.class);
	
	private final ObjectMapper mapper = new ObjectMapper();

	private Class<E> elementType;

	public JsonListConverter(Class<E> elementType) {
		Objects.requireNonNull(elementType, "The elementType is null.");
		this.elementType = elementType;
	}
	
	@Override
	public String serialize(T value) {
		if(value != null) {
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
			try {
				JsonNode jsonNode = mapper.readTree(value);
				if(jsonNode.isArray()) {
					List<E> list = new ArrayList<>();
					if(Objects.equals(String.class, clazz)) {
						for (JsonNode element : jsonNode) {
							list.add((E)element.toString());
						}
					} else {
						for (JsonNode element : jsonNode) {
							list.add(mapper.readValue(element.toString(), elementType));
						}
					}
					return (T)list;
				} else {
					throw new IllegalArgumentException("value is not an array, or incorrect length of classes");
				}
			} catch (IOException e) {
				logger.error(LogBuilder.create().from("反序列化错误", null, clazz, value), e);
			}
		}
		return null;
	}

}
