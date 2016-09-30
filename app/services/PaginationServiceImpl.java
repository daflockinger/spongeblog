package services;

import static dto.RestError.NOT_FOUND;

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;

import com.google.common.collect.ImmutableList;

import dao.ExtendedDAO;
import dao.PaginationDAO;
import dto.PaginationDTO;
import dto.PaginationQueryDTO;
import model.BaseModel;

public abstract class PaginationServiceImpl<M extends BaseModel, T extends PaginationDAO<M, ObjectId>>
		extends BaseServiceImpl<M, T> implements PaginationService<M>{
	public List<M> findAllInPage(PaginationDTO settings) {
		List<M> models = new ArrayList<>();
		models = dao().findAllInPage(new PaginationQueryDTO<M>(settings, getModelClass()));

		if (models == null) {
			return ImmutableList.of(errorModel(NOT_FOUND));
		}
		return models;
	}
	
	public boolean hasPreviousPage(PaginationDTO settings){
		return dao().hasPreviousPage(new PaginationQueryDTO<M>(settings, getModelClass()));
	}
}
