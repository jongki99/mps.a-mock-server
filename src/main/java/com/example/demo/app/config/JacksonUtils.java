package com.example.demo.app.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.boot.json.JacksonJsonParser;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JacksonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();
    static {
        mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false); // jpa 관련 converting 시에 에러 무시.
		mapper.registerModule(new JavaTimeModule()); // java LocalDateTime, commerce 참조.
    }

    private static final JacksonJsonParser parser = new JacksonJsonParser(mapper);

    public static String toJson(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    public static String toJsonNoEx(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (IOException ex) {
            log.error("json error. obj={}", obj, ex);
            return null;
        }
    }

    public static Map<String, Object> toMap(String json) {
        return parser.parseMap(json);
    }

    public static <T> T toObject(String json, Class<T> clazz) throws IOException {
        return mapper.readValue(json, clazz);
    }

    public static <T> T toObjectNoEx(String json, Class<T> clazz) {
        try {
            return mapper.readValue(json, clazz);
        } catch (IOException ex) {
            log.error("json error. str={}", json, ex);
            return null;
        }
    }

    public static <T> T toObject(String json, TypeReference<T> typeReference) throws IOException {
        return mapper.readValue(json, typeReference);
    }

    public static <T> T toObjectNoEx(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException ex) {
            log.error("json error. str={}", json, ex);
            return null;
        }
    }

    public static <T> T toObject(JsonNode json, Class<T> clazz) throws IOException {
        return mapper.readValue(json.traverse(), clazz);
    }

    public static JsonNode toJsonNode(Object obj) {
        return mapper.convertValue(obj, JsonNode.class);
    }

    public static Map<String, String> toMap(Object obj) {
        return mapper.convertValue(obj, new TypeReference<Map<String, String>>() {});
    }

    public static <T> T toObject(File file, Class<T> clazz) throws IOException {
        return mapper.readValue(file, clazz);
    }

    public static <T> T toObject(InputStream is, Class<T> clazz) throws IOException {
        return mapper.readValue(is, clazz);
    }

    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

	public static <T> T toClass(Object obj, Class<T> valueType){
		try {
			if(obj instanceof String){
				return mapper.readValue((String)obj, valueType);
			} else {
				return mapper.readValue(mapper.writeValueAsString(obj), valueType);			
			}
		} catch (JsonProcessingException e) {
			log.debug("toClass error", e);
			throw new RuntimeException("json parsing error="+e.getMessage());
			// throw new ApiErrorException(CouponError.JSON_PROCESSING_ERROR);
		}
	}
}
