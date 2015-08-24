package com.mongodb.anil.hello;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HelloWorldSparkFreemarker {

	public static void main(String[] args) throws IOException,
			TemplateException {
		final Configuration config = new Configuration();
		config.setClassForTemplateLoading(HelloWorldFreeMarkerStyle.class,
				"/templates");

		Spark.get(new Route("/") {

			@Override
			public Object handle(Request arg0, Response arg1) {
				Template template;
				StringWriter writer = null;
				try {
					template = config.getTemplate("hello.ftl");
					writer = new StringWriter();
					Map<String, Object> keyValueMap = new HashMap<String, Object>();
					keyValueMap.put("name", "Anil");
					template.process(keyValueMap, writer);

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return writer.toString();
			}
		});
	}
}
