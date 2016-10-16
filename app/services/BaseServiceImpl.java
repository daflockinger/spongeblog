package services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.http.HttpStatus;
import org.bson.types.ObjectId;

import com.google.inject.Inject;

import dao.ExtendedDAO;
import dto.BaseDTO;
import dto.RestError;
import exceptions.GeneralServiceException;
import exceptions.ModelNotFoundException;
import ma.glasnost.orika.MapperFacade;
import model.BaseModel;
import model.Keyword;
import play.Logger;
import utils.BlogMapperFactory;

public abstract class BaseServiceImpl<N extends BaseModel, M extends BaseDTO, T extends ExtendedDAO<N, ObjectId>>
		implements BaseService<M,N> {

	protected abstract T dao();
	
	@Inject
	private BlogMapperFactory mapperFactory;
	
	
	public void setMapperFactory(BlogMapperFactory mapperFactory) {
		this.mapperFactory = mapperFactory;
	}

	protected MapperFacade mapper(){
		return mapperFactory.getFascade();
	}

	public M create(M dto) throws GeneralServiceException{
		if (dto == null || (dto != null && isNotUnique(dto))) {
			throw new ModelNotFoundException(getModelClass().getSimpleName());
		}
		dao().save(mapper().map(dto,getModelClass()));
		dto.setStatus(HttpStatus.SC_CREATED);

		return  dto;
	}

	protected abstract boolean isNotUnique(M dto);

	public M findById(ObjectId id) throws ModelNotFoundException {
		M dto = mapper().map(dao().get(id),getDTOClass());

		if (dto == null) {
			throw new ModelNotFoundException(getModelClass().getSimpleName());
		}
		return dto;
	}
	
	public List<M> findAll() throws GeneralServiceException{
		List<N> models = dao().find().asList();
		
		if(models != null){
			return models.stream().map(model -> mapper().map(model, getDTOClass())).collect(Collectors.toList());
		} else {
			return new ArrayList<>();
		}
	}

	public M errorModel(RestError message) {
		return errorModel(message,"");
	}
	
	public M errorModel(RestError message,String addonMessage) {
		try {
			M errorModel = getDTOClass().newInstance();
			errorModel.setErrorMessage(message.toString() + addonMessage);
			errorModel.setStatus(message.status());
			return errorModel;
		} catch (InstantiationException e) {
			Logger.error("Create dto Instance failed", e);
		} catch (IllegalAccessException e) {
			Logger.error("Create dto Instance failed", e);
		}
		return null;
	}

	public abstract Class<N> getModelClass();
	public abstract Class<M> getDTOClass();

	public M update(M dto) throws ModelNotFoundException{
		if (dto == null || isNotValid(dto)) {
			throw new ModelNotFoundException(getModelClass().getSimpleName());
		}
		dao().save(mapper().map(dto,getModelClass()));

		return dto;
	}

	private boolean isNotValid(M dto) {
		return dto.getId() == null;
	}

	protected boolean existsWithId(ObjectId id) throws ModelNotFoundException {
		return findById(id).getStatus() != HttpStatus.SC_NOT_FOUND;
	}

	public M delete(ObjectId id) throws ModelNotFoundException{
		if (!existsWithId(id)) {
			throw new ModelNotFoundException(getModelClass().getSimpleName());
		}
		M toDelete = findById(id);
		dao().deleteById(id);

		return toDelete;
	}
}
