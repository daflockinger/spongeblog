package dao;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.google.inject.Singleton;
import com.mongodb.MongoClient;

public class DataStoreFactory {
	private static volatile Datastore dataStore = null;
	private static volatile Morphia morphia = null;
	
	private DataStoreFactory(){
	}
	
	public static synchronized Datastore get(){
		
		if(dataStore == null){
			morphia = new Morphia();
			dataStore = morphia.createDatastore(new MongoClient(), "spongeblog");
		}
		
		return dataStore;
	}
	
	public static Morphia getMorphia(){
		return morphia;
	}
}
