package com.gmail.jiangyang5157.java_math;

import java.io.Serializable;

/**
 * An Vector represents the position and direction
 * <p>
 * angle [0, 180] = 180 / PI * radian;
 * radian [0, PI] = PI / 180 * angle;
 *
 * @author Yang 8/19/2015.
 */
public class Vector implements Serializable {

    private static final long serialVersionUID = 2305812345233693950L;

    public static final double EPSILON = 1e-6;

    protected int D = 2;

    protected double[] data = null;

    public Vector(int D) {
        if (D < 2) {
            throw new IllegalArgumentException("Unexpected Dimensions");
        }

        this.D = D;
        this.data = new double[D];
    }

    public Vector(double... data) {
        D = data.length;
        if (D < 2) {
            throw new IllegalArgumentException("Unexpected Dimensions");
        }

        this.data = data;
    }

    public Vector(Vector that) {
        this.D = that.D;
        this.data = that.data;
    }

    public Vector plus(Vector that) {
        if (this.D != that.D) {
            throw new RuntimeException("Dimensions don't agree");
        }

        Vector ret = new Vector(D);
        for (int i = 0; i < D; i++) {
            ret.data[i] = this.data[i] + that.data[i];
        }
        return ret;
    }

    public Vector minus(Vector that) {
        if (this.D != that.D) {
            throw new RuntimeException("Dimensions don't agree");
        }

        Vector ret = new Vector(D);
        for (int i = 0; i < D; i++) {
            ret.data[i] = this.data[i] - that.data[i];
        }
        return ret;
    }

    public double dot(Vector that) {
        if (this.D != that.D) {
            throw new RuntimeException("Dimensions don't agree");
        }

        double ret = 0.0;
        for (int i = 0; i < D; i++) {
            ret = ret + (this.data[i] * that.data[i]);
        }
        return ret;
    }

    public Vector negate() {
        Vector ret = new Vector(D);
        for (int i = 0; i < D; i++) {
            ret.data[i] = -this.data[i];
        }
        return ret;
    }

    public Vector times(double factor) {
        Vector ret = new Vector(D);
        for (int i = 0; i < D; i++) {
            ret.data[i] = factor * data[i];
        }
        return ret;
    }

    public double length() {
        return Math.sqrt(this.dot(this));
    }

    public Vector direction() {
        double length = this.length();
        if (length == 0.0) {
            throw new ArithmeticException("Cannot normalize a zero length vector.");
        }
        return new Vector(this.times(1.0 / length));
    }

    public boolean epsilonEquals(Vector that) {
        if (this.D != that.D) {
            throw new RuntimeException("Dimensions don't agree");
        }

        for (int i = 0; i < D; i++) {
            double diff = this.data[i] - that.data[i];
            if (Double.isNaN(diff)) {
                return false;
            }
            if ((diff < 0 ? -diff : diff) > Vector.EPSILON) {
                return false;
            }
        }

        return true;
    }

    public int getDimension() {
        return D;
    }

    public double[] getData() {
        return this.data;
    }

    public double getData(int index) {
        return this.data[index];
    }

    /**
     * Algorithm from Effective Java by Joshua Bloch [Jon Aquino]
     */
    @Override
    public int hashCode() {
        int ret = 17;
        for (int i = 0; i < D; i++) {
            ret = 37 * ret + hashCode(data[i]);
        }
        return ret;
    }

    /**
     * Algorithm from Effective Java by Joshua Bloch [Jon Aquino]
     */
    public static int hashCode(double d) {
        long longBits = Double.doubleToLongBits(d);
        return (int) (longBits ^ (longBits >>> 32));
    }

    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("<");
        for (int i = 0; i < D - 1; i++) {
            ret.append(data[i]);
            ret.append(", ");
        }
        ret.append(data[D - 1]);
        ret.append(">");
        return ret.toString();
    }
}