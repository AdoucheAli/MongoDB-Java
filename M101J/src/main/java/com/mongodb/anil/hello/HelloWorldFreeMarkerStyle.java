package com.mongodb.anil.hello;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class HelloWorldFreeMarkerStyle {

	public static void main(String[] args) throws IOException, TemplateException {
		Configuration config = new Configuration();
		config.setClassForTemplateLoading(HelloWorldFreeMarkerStyle.class, "/templates");

		Template template = config.getTemplate("hello.ftl");
		StringWriter writer = new StringWriter();
		Map<String , Object> keyValueMap = new HashMap<String, Object>();
		keyValueMap.put("name", "Anil");
		
		template.process(keyValueMap, writer);
		System.out.println("The filled template:" + writer.toString());
	}

}
