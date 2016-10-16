package helpers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;
import com.google.inject.Singleton;

import dto.BaseDTO;
import model.BaseModel;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;

@Singleton
public class JsonHelper {
	protected ObjectMapper mapper;

	public JsonHelper() {
		mapper = new MongoObjectMapper();
		mapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
		mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
		//mapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);//TODO try that, don't know if it works
	}

	public <U> U extractModel(RequestBody body, Class<U> modelClass) {
		U model = null;

		if (isBodyValidJson(body)) {
			try {
				if(body.asJson().elements().next().isContainerNode()){
					model = mapper.treeToValue(body.asJson().elements().next(),modelClass);
				} else {
					model = mapper.treeToValue(body.asJson(), modelClass);
				}
			} catch (JsonProcessingException jsonException) {
				Logger.error("Error processing json: ", jsonException);
			}
		}
		return model;
	}

	private boolean isBodyValidJson(RequestBody body) {
		return body != null && body.asJson() != null;
	}

	public <M> Result getInvalidJsonMessage(M model) {
		return Controller.status(HttpStatus.SC_BAD_REQUEST, Json.toJson(model))
				.withHeader("Access-Control-Allow-Origin", "*");
	}

	public <T extends BaseDTO> Result getResponse(T result) {
		Result status = Controller.status(result.getStatus());

		if (result != null) {
			status = Controller.status(result.getStatus(), Json.toJson(result));
		}
		return status;
	}
	
	public <T extends BaseDTO> Result getResponse(T result, Class<T> type) {
		Result status = Controller.status(result.getStatus());

		if (result != null) {
			status = Controller.status(result.getStatus(), Json.toJson(wrapObjectForJson(result,type)));
		}
		return status;
	}

	public <T extends BaseDTO> Result getResponses(List<T> results, Class<T> type) {
		Integer status = HttpStatus.SC_OK;
		Optional<T> result = results.stream().findAny();

		if (result.isPresent()) {
			status = result.get().getStatus();
		}
		return Controller.status(status, Json.toJson(wrapListForJson(results, type)));
	}

	public <T extends BaseDTO> Map<String, T> wrapObjectForJson(T result, Class<T> type) {
		return Collections.singletonMap(type.getSimpleName().replaceFirst("DTO", "").toLowerCase(), result);
	}
	
	public <T extends BaseDTO> Map<String, List<T>> wrapListForJson(List<T> results, Class<T> type) {
		return Collections.singletonMap(type.getSimpleName().replaceFirst("DTO", "").toLowerCase(), results);
	}

	private static class ObjectIdSerializer extends JsonSerializer<ObjectId> {

		@Override
		public void serialize(ObjectId value, JsonGenerator jgen, SerializerProvider provider)
				throws IOException, JsonProcessingException {
			jgen.writeString(value.toString());
		}
	}
	
	private static class MongoObjectMapper extends ObjectMapper {

		public MongoObjectMapper() {
			SimpleModule module = new SimpleModule("ObjectIdmodule");
			module.addSerializer(ObjectId.class, new ObjectIdSerializer());
			this.registerModule(module);
		}

	}
}
