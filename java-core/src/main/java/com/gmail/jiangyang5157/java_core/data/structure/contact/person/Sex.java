package com.gmail.jiangyang5157.java_core.data.structure.contact.person;

/**
 * User: Yang
 * Date: 2014/11/16
 * Time: 21:53
 */
public enum Sex {
	MALE("Male"),
	FEMALE("Female");

	private final String sex;
	private Sex(String sex) {
		this.sex = sex;
	}
}
