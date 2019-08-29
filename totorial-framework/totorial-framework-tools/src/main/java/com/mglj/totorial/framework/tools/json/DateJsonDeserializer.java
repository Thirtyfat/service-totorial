package com.mglj.totorial.framework.tools.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.mglj.totorial.framework.tools.util.CalendarUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

@JsonComponent
public class DateJsonDeserializer extends JsonDeserializer<Date> {
	
	private static final Logger logger = LoggerFactory.getLogger(DateJsonDeserializer.class);
	
	@Override
	public Date deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String value = jp.getText();
		try {
			return CalendarUtils.parse(value);
		} catch (ParseException e) {
			logger.error("解析时间错误", e);
			return null;
		}
	}
	
}
