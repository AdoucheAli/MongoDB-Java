/**
 * 
 */
package com.mongodb.anil.hello;

import java.io.IOException;
import java.io.StringWriter;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * @author H122337
 * 
 */
public class HelloWorldMongoDBSparkFreeMarker {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws IOException,
			TemplateException {
		// Setup the Freemarker configuration
		final Configuration config = new Configuration();
		config.setClassForTemplateLoading(HelloWorldFreeMarkerStyle.class,
				"/templates");

		// Setup MongoDB stuff
		MongoClient client = new MongoClient(new ServerAddress("localhost",
				27017));
		final DB database = client.getDB("mongoclass");
		final DBCollection collection = database.getCollection("course");

		Spark.get(new Route("/") {

			@Override
			public Object handle(Request arg0, Response arg1) {
				Template template;
				StringWriter writer = null;
				try {
					template = config.getTemplate("hello.ftl");
					writer = new StringWriter();
					// DBObject extends HashMap
					DBObject document = collection.findOne();
					template.process(document, writer);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return writer.toString();
			}
		});
	}

}
