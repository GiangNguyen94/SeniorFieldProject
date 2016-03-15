package com.esotericsoftware.jsonbeans;

import java.util.ArrayList;

public class writeToJson {

	public static void main(String[] args) throws WrongJsonFormatException {
		Person person = new Person();
		person.setName("Nate");
		person.setAge(31);
		ArrayList<PhoneNumber> numbers = new ArrayList<PhoneNumber>();
		numbers.add(new PhoneNumber("Home", "206-555-1234"));
		numbers.add(new PhoneNumber("Work", "425-555-4321"));
		person.setNumbers(numbers);
		/*
		Json json = new Json();
		json.setOutputType(OutputType.json);
		json.setElementType(Person.class, "numbers", PhoneNumber.class);
		json.addClassTag("phoneNumber", PhoneNumber.class);
		System.out.println(json.prettyPrint(person));
	    
	    */
		/*
	    Json json = new Json();
	    json.setOutputType(OutputType.minimal);
	    String text = json.toJson(person, Object.class);//Person.class
	    System.out.println(json.prettyPrint(text));
	    Object person2 = json.fromJson(Object.class, text);
	    */
	    ///*
	    Json json = new Json();
	    json.setOutputType(OutputType.json);
	    String text = json.toJson(person, Object.class);
	    System.out.println(text);
	    JsonValue root = new JsonReader().parse(text);
	    System.out.println(root.get("numbers").get(1));
		//*/
	}

}
