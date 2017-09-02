package com.gmail.jiangyang5157.java_core.data.structure.contact;

/**
 * User: Yang
 * Date: 2014/11/23
 * Time: 12:42
 */
public enum Type {
	HOME("Home"),
	WORK("Work");

	private final String type;
	private Type(String type) {
		this.type = type;
	}
}