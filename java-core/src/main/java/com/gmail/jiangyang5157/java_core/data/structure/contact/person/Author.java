package com.gmail.jiangyang5157.java_core.data.structure.contact.person;

import com.gmail.jiangyang5157.java_core.data.structure.contact.Contactable;
import com.gmail.jiangyang5157.java_core.data.structure.contact.EmailAddress;
import com.gmail.jiangyang5157.java_core.data.structure.contact.Type;

import java.util.HashMap;

/**
 * User: Yang
 * Date: 2014/11/16
 * Time: 23:01
 */
public class Author extends Person implements Contactable {

    private HashMap<Type, EmailAddress> emailAddresses = new HashMap<>();

    private Role role;

    public Author(String firstName, String lastName) {
        super(firstName, null, lastName);
    }

    public Author(String firstName, String middleName, String lastName) {
        super(firstName, middleName, lastName);
    }

    @Override
    public EmailAddress putEmailAddress(Type type, EmailAddress address) {
        return emailAddresses.put(type, address);
    }

    @Override
    public EmailAddress removeEmailAddress(Type type) {
        return emailAddresses.remove(type);
    }

    @Override
    public HashMap<Type, EmailAddress> getEmailAddresses() {
        return emailAddresses;
    }
}
