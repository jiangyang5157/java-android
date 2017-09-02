package com.gmail.jiangyang5157.java_math;

import java.io.Serializable;

/**
 * @author Yang 8/18/2015.
 */
public class Vector2d extends Vector implements Serializable {

    public Vector2d() {
        super(2);
    }

    public Vector2d(double... data) {
        super(data);
        if (D != 2) {
            throw new IllegalArgumentException("Unexpected Dimensions");
        }
    }

    public Vector2d(Vector that) {
        this(that.getData());
    }

    public double cross(Vector2d that) {
        return this.data[0] * that.data[1] + this.data[1] * that.data[0];
    }

    public double alpha() {
        return Math.atan2(this.data[1], this.data[0]);
    }

    public Vector2d rotate(double radian) {
        double cosRadians = Math.cos(radian);
        double sinRadians = Math.sin(radian);
        return new Vector2d(this.data[0] * cosRadians - this.data[1] * sinRadians, this.data[0] * sinRadians + this.data[1] * cosRadians);
    }
}
