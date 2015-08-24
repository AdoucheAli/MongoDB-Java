/**
 * 
 */
package com.mongodb.anil.hello;

import java.net.UnknownHostException;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * @author H122337
 *
 */
public class HelloWorldMongoDB {
 public static void main(String[] args) throws UnknownHostException{
	 MongoClient client = new MongoClient(new ServerAddress("localhost", 27017));
	 
	 DB database = client.getDB("mongoclass");
	 DBCollection collection = database.getCollection("course");
	 
	 DBObject document = collection.findOne();
	 System.out.println(document);
 }
}
