package com.gmail.jiangyang5157.java_core.data.structure.contact;

import java.util.HashMap;

/**
 * User: Yang
 * Date: 2014/11/16
 * Time: 22:59
 */
public interface Contactable {
	HashMap<Type, EmailAddress> getEmailAddresses();

	EmailAddress putEmailAddress(Type type, EmailAddress address);

	EmailAddress removeEmailAddress(Type type);
}
