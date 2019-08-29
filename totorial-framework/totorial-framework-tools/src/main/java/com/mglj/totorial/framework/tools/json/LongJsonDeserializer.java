package com.mglj.totorial.framework.tools.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

@JsonComponent
public class LongJsonDeserializer extends JsonDeserializer<Long> {

	private static final Logger logger = LoggerFactory.getLogger(LongJsonDeserializer.class);
	
	@Override
	public Long deserialize(JsonParser p, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		String value = p.getText();
		try {
			return value == null || "".equals(value.trim()) ? null : Long.parseLong(value);
		} catch (NumberFormatException e) {
			logger.error("解析长整形错误: " + value, e);
			return null;
		}
	}

}
