package com.mongodb.anil.crud;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.anil.BaseOperationsHelper;
import com.mongodb.gridfs.GridFS;
import com.mongodb.gridfs.GridFSDBFile;
import com.mongodb.gridfs.GridFSInputFile;

/**
 * The Class FridFSBlobExample.
 */
public class GridFSBlobExample {

	/**
	 * The main method.
	 * 
	 * @param args
	 *            the arguments
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		// Use any file greater than 16MB
		String fileName = "jprofiler_windows-x64_7_2_2.exe";

		MongoClient client = new MongoClient(new ServerAddress("localhost",
				27017));
		DB database = client.getDB("mongoclass");

		GridFSBlobExample example = new GridFSBlobExample();
		BaseOperationsHelper.startOperationLayout("createBlobUsingGridFS");
		example.createBlobUsingGridFS(database, fileName);

		BaseOperationsHelper.startOperationLayout("readBlobUsingGridFS");
		example.readBlobUsingGridFS(database, fileName);
		BaseOperationsHelper.closeOperationLayout();
	}

	/**
	 * Creates the blob using grid fs.
	 * 
	 * @param database
	 *            the database
	 */
	private void createBlobUsingGridFS(DB database, String fileName) {
		FileInputStream inputStream = null;

		// Check if file exists
		try {
			inputStream = new FileInputStream(fileName);
		} catch (FileNotFoundException fnfException) {
			System.out.println("Cannot find the file: " + fileName);
			System.out.println();
		}

		// You store the BLOBs as a separate 2 collections(metadata,chunks) in
		// the DB and these 2 collections are shared by all other user defined
		// connections
		GridFS blobs = new GridFS(database, "blobs");

		// Create an input file
		GridFSInputFile inputFile = blobs.createFile(inputStream, fileName);

		// Create metadata for the object
		DBObject metaObject = new BasicDBObject("desription",
				"Installer for JProfile");
		List<String> tags = new ArrayList<String>();
		tags.add("Java");
		tags.add("Profiling");

		metaObject.put("tags", tags);

		inputFile.setMetaData(metaObject);
		inputFile.save();

		System.out.println("Object Id of the newly saved file: "
				+ inputFile.get("_id"));

	}

	/**
	 * Read blob using grid fs.
	 * 
	 * @param database
	 *            the database
	 * @param fileName
	 *            the file name
	 * @throws FileNotFoundException
	 *             the file not found exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	private void readBlobUsingGridFS(DB database, String fileName)
			throws FileNotFoundException, IOException {
		// You store the BLOBs as a separate 2 collections(metadata,chunks) in
		// the DB and these 2 collections are shared by all other user defined
		// connections
		GridFS blobs = new GridFS(database, "blobs");

		GridFSDBFile gridFile = blobs.findOne(new BasicDBObject("filename",
				fileName));

		// We split the file name of the "." to add copy to the name
		String[] fileParts = fileName.split("\\.");
		FileOutputStream outputStream = new FileOutputStream(fileParts[0]
				+ "-copy." + fileParts[1]);

		gridFile.writeTo(outputStream);

		System.out.println("File Written to disk.");

	}
}
