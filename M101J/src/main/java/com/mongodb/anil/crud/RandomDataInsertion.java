package com.mongodb.anil.crud;

import java.net.UnknownHostException;
import java.util.Random;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

/**
 * The Class RandomDataInsertion.
 */
public class RandomDataInsertion {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws UnknownHostException
	 *             the unknown host exception
	 */
	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient(new ServerAddress("localhost",
				27017));
		DB database = client.getDB("test");
		// Will create a new collection if none exists
		DBCollection collection = database.getCollection("fubar");

		Random generatorA = new Random();
		Random generatorB = new Random();
		Random generatorC = new Random();

		System.out
				.println("The number of records in collection BEFORE insert is: "
						+ collection.count());

		for (int i = 1; i <= 20000; i++) {
			collection.insert(new BasicDBObject("a", generatorA.nextInt(10000))
					.append("b", generatorB.nextInt(30000)).append("c",
							generatorC.nextInt(5000)));
		}

		System.out
				.println("The number of records in collection AFTER insert is: "
						+ collection.count());
	}
}
