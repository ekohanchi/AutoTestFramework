package com.project.services.db;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.project.core.AppLib;

public class MongoDB {
	protected String environment = "";
	
	public MongoDB(String env) {
		environment = env;
	}
	
	private MongoClient connect() {
		MongoClient mongoClient = null;
		try {
			if (environment.equals(AppLib.ENV_PROD)) {
				mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			} else if (environment.equals(AppLib.ENV_QA)) {
				mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost:27017"));
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		return mongoClient;
	}

	private DB getDB(String dbName) {
		DB database = connect().getDB(dbName);
		return database;
	}
	
	private DBCollection getCollection() {
		DBCollection collection = getDB("dbName").getCollection("collectionName");
		return collection;
	}
	
	private DBCursor getDBCursor(String field, int value) {
		DBObject query = new BasicDBObject(field, value);
		DBCursor cursor = getCollection().find(query);
		return cursor;
	}
	
	public List<DBObject> findAllRecordsInClickData(String field, int value) {
		DBCursor cursor = getDBCursor(field, value);
		List<DBObject> dbObjectList = new ArrayList<DBObject>();
		
		if (cursor.size() == 0) {
			return null;
		}
		
		while (cursor.hasNext()) {
			dbObjectList.add(cursor.next());
		}

		return dbObjectList;
	}
}
