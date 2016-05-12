package com.esotericsoftware.jsonbeans;

import java.util.ArrayList;

public class Person {
	   private String name;
	   private int age;
	   private ArrayList<PhoneNumber> numbers;
	public void setName(String string) {
		this.name = string;
	}
	public void setAge(int i) {
		this.age = i;
	}
	public void setNumbers(ArrayList<PhoneNumber> numbers2) {
		this.numbers = numbers2;
	}

}
