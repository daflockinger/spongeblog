package helpers;

import org.apache.http.HttpStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import dto.ErrorDTO;
import dto.OperationResult;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;

public class JsonHelper {
	protected ObjectMapper mapper;
	
	public JsonHelper(){
		mapper = new ObjectMapper();
	}
	
	public <U> U extractModel(RequestBody body, Class<U> modelClass) {
		U model = null;

		if (isBodyValidJson(body)) {
			try {
				model = mapper.treeToValue(body.asJson(), modelClass);
			} catch (JsonProcessingException jsonException) {
				Logger.error("Error processing json: ", jsonException);
			}
		}
		return model;
	}

	private boolean isBodyValidJson(RequestBody body) {
		return body != null && body.asJson() != null;
	}
	
	public Result getInvalidJsonMessage(RequestBody body) {
		return Controller.status(HttpStatus.SC_BAD_REQUEST, Json.toJson(new ErrorDTO("Invalid/Malformed Json", body.asText())));
	}

	public Result getStatus(OperationResult<?> result) {
		Result status = Controller.status(result.getStatus());

		if (result.getEntity() != null) {
			status = Controller.status(result.getStatus(), Json.toJson(result.getEntity()));
		}
		return status;
	}
}
