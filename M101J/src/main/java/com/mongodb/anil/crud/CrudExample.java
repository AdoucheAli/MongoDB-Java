/**
 * 
 */
package com.mongodb.anil.crud;

import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.QueryBuilder;
import com.mongodb.ServerAddress;
import com.mongodb.anil.BaseOperationsHelper;

/**
 * @author H122337
 * 
 */
public class CrudExample {

	/**
	 * @param args
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException {
		MongoClient client = new MongoClient(new ServerAddress("localhost",
				27017));
		DB database = client.getDB("mongoclass");
		// Will create a new collection if none exists
		DBCollection collection = database.getCollection("employee");

		// clean up the collection before each test
		collection.drop();

		CrudExample example = new CrudExample();
		BaseOperationsHelper.startOperationLayout("readEmployee");
		example.insertEmployee(collection);

		BaseOperationsHelper.startOperationLayout("insertEmployee");
		example.readEmployee(collection);

		BaseOperationsHelper.startOperationLayout("queryEmployee");
		example.queryEmployee(collection);

		BaseOperationsHelper.startOperationLayout("updateEmployee");
		example.updateEmployee(collection);

		BaseOperationsHelper.startOperationLayout("removeEmployee");
		example.removeEmployee(collection);
		BaseOperationsHelper.closeOperationLayout();
	}

	/**
	 * Insert employee.
	 * 
	 * @param collection
	 *            the collection
	 */
	private void insertEmployee(DBCollection collection) {
		String[] names = new String[] { "Anil Allewar", "Rohit Ghatol",
				"Amit Jahagirdar" };
		String[] designations = new String[] { "Sr Solutions Architect",
				"Director of Engg", "Technical Lead" };
		String[] street1 = new String[] { "20 main street", "95 main street",
				"105 main street" };
		String[] street2 = new String[] { "43 southwest avenue",
				"70 southwest avenue", "217 southwest avenue" };

		DBObject object = null;

		// Loop through to insert 3 objects
		for (int i = 0; i < 3; i++) {

			object = new BasicDBObject();

			object.put("name", names[i]);
			object.put("designation", designations[i]);
			object.put("date_of_birth", new Date(578568767));

			DBObject addresshome = new BasicDBObject();
			addresshome.put("street1", street1[i]);
			addresshome.put("city", "Houston");
			addresshome.put("state", "TX");
			addresshome.put("country", "United States");
			addresshome.put("zip", 77042);

			object.put(
					"address",
					Arrays.asList(
							addresshome,
							new BasicDBObject().append("street1", street2[i])
									.append("city", "Houston")
									.append("state", "TX")
									.append("country", "United States")
									.append("zip", 77076)));

			collection.insert(object);

			System.out.println("Inserted object is: " + object);
		}
	}

	/**
	 * Read employee.
	 * 
	 * @param collection
	 *            the collection
	 */
	private void readEmployee(DBCollection collection) {
		System.out.println("\nReading one object using findOne");
		DBObject readObject = collection.findOne();
		System.out.println("Object: " + readObject);

		System.out.println("\nReading all data using cursor and find() method");
		DBCursor cursor = collection.find();
		try {
			while (cursor.hasNext()) {
				System.out.println("The next object is: " + cursor.next());
			}
		} finally {
			// Always close the cursor
			cursor.close();
		}

		System.out.println("\nThe number of elements in collection is: "
				+ collection.count());
	}

	/**
	 * Query employee.
	 * 
	 * @param collection
	 *            the collection
	 */
	private void queryEmployee(DBCollection collection) {
		// Exact match query
		DBObject query = new BasicDBObject();
		query.put("name", "Anil Allewar");

		System.out.println("\nExact Query match");
		DBCursor cursor = collection.find(query);

		try {
			while (cursor.hasNext()) {
				System.out.println("The object retrieved by query: "
						+ cursor.next());
			}
		} finally {
			cursor.close();
		}

		System.out.println("\nResults returned by QueryBuilder");
		QueryBuilder builder = QueryBuilder.start("name").lessThan("B")
				.and("address.zip").greaterThan(77042);

		// We want to skip the _id field and that goes as the 2nd argument to
		// find(). The sort, skip etc options are available on the cursor
		cursor = collection
				.find(builder.get(), new BasicDBObject("_id", false)).sort(
						new BasicDBObject("address.street1", 1));
		try {
			while (cursor.hasNext()) {
				System.out
						.println("The object retrieved by query and sorted on street1: "
								+ cursor.next());
			}
		} finally {
			cursor.close();
		}
	}

	/**
	 * Update employee.
	 * 
	 * @param collection
	 *            the collection
	 */
	private void updateEmployee(DBCollection collection) {
		System.out
				.println("\nUpdating by setting the date for Amit to current date");

		System.out.println("Current data with inserted date:"
				+ collection.findOne(new BasicDBObject("name",
						new BasicDBObject("$lt", "An"))));

		collection.update(new BasicDBObject("name", new BasicDBObject("$lt",
				"An")), new BasicDBObject("$set", new BasicDBObject(
				"date_of_birth", new Date())));

		System.out.println("Updated with current date:"
				+ collection.findOne(new BasicDBObject("name",
						new BasicDBObject("$lt", "An"))));

		System.out.println("\nChecking with upsert true");

		collection.update(new BasicDBObject("name", "Shantanu Dindokar"),
				new BasicDBObject("name", "Shantanu Dindokar").append(
						"designation", "Technical Lead 2"), true, false);

		System.out.println("Checking whether the object got inserted:"
				+ collection.findOne(new BasicDBObject("name",
						"Shantanu Dindokar")));
	}

	/**
	 * Removes the employee.
	 * 
	 * @param collection
	 *            the collection
	 */
	private void removeEmployee(DBCollection collection) {
		System.out.println("\nDeleting the entry that was upserted");

		DBObject objectToRemove = collection.findOne(new BasicDBObject("name",
				"Shantanu Dindokar"));

		collection.remove(objectToRemove);

		System.out.println("\nRemaining documents");
		DBCursor cursor = collection.find();

		try {
			while (cursor.hasNext()) {
				System.out.println("The object retrieved by query: "
						+ cursor.next());
			}
		} finally {
			cursor.close();
		}
	}
}
