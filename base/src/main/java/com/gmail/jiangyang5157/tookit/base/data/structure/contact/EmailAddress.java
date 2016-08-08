package com.gmail.jiangyang5157.tookit.base.data.structure.contact;

/**
 * User: Yang
 * Date: 2014/11/16
 * Time: 22:47
 */
public class EmailAddress {

    public static final String SYMBOL_AT = "@";

    private String prefix;
    private String suffix;

    public EmailAddress(String prefix, String suffix) {
        setPrefix(prefix);
        setSuffix(suffix);
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(prefix)
                .append(SYMBOL_AT)
                .append(suffix);
        return buffer.toString();
    }
}
