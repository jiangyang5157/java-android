package com.gmail.jiangyang5157.java_core.data.structure.contact.person;

/**
 * User: Yang
 * Date: 2014/11/16
 * Time: 23:03
 */
public enum Role {
	DEVELOPER("Developer"),
	UX("User Experience");

	private final String role;
	private Role(String role) {
		this.role = role;
	}
}
