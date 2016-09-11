package dao;

import org.mongodb.morphia.dao.BasicDAO;

public abstract class ExtendedDAO<T,K> extends BasicDAO<T, K>{
	
	protected ExtendedDAO() {
		super(DataStoreFactory.get());
		
		//TODO do i need/want that?!
		//ValidationExtension validation = new ValidationExtension(DataStoreFactory.getMorphia());
	}
}
