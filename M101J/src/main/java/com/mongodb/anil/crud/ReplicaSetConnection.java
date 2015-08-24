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
import com.mongodb.ReadPreference;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.anil.BaseOperationsHelper;

/**
 * @author H122337
 * 
 */
public class ReplicaSetConnection {

	/**
	 * @param args
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) throws UnknownHostException {
		// Note that we are not connecting to the primary node but as long as we
		// have 1 valid node in the list, MongoDB will discover the replica set
		// nodes and connect accordingly
		MongoClient client = new MongoClient(Arrays.asList(new ServerAddress(
				"localhost", 27018), new ServerAddress("localhost", 27019)));

		// Connect to database
		DB database = client.getDB("mongoclass");
		// Will create a new collection if none exists
		DBCollection collection = database.getCollection("employee");

		// clean up the collection before each test
		collection.drop();

		ReplicaSetConnection example = new ReplicaSetConnection();
		BaseOperationsHelper.startOperationLayout("insertEmployee");
		example.readEmployee(collection);

		BaseOperationsHelper.startOperationLayout("readEmployee");
		example.insertEmployee(collection);
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

			// We are essentially asking the driver to send a getLastError()
			// message so that the server acknowledges that it has saved the
			// insert to the write-ahead journal log
			collection.insert(object, WriteConcern.JOURNALED);

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

		// We set the read preference to read from any node that is within 15ms
		// of the node that has the lowest ping interval

		// Note that the read and write preferences can be set individually at
		// the client,db, collection and the individual operation level. My
		// guess is the the fine grained control overrides the control set in
		// the parent.
		DBCursor cursor = collection.find().setReadPreference(
				ReadPreference.nearest());
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

}
