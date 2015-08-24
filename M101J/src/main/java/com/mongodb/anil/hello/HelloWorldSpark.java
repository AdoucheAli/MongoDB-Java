package com.mongodb.anil.hello;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

public class HelloWorldSpark {
	public static void main(String[] args){
		Spark.get(new Route("/") {
			
			@Override
			public Object handle(Request arg0, Response arg1) {
				return "Hello World From Spark";
			}
		});
	}
}
