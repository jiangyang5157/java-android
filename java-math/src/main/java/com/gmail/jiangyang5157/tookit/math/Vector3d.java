package com.gmail.jiangyang5157.tookit.math;

import java.io.Serializable;

/**
 * @author Yang 8/18/2015.
 */
public class Vector3d extends Vector implements Serializable {

    public Vector3d() {
        super(3);
    }

    public Vector3d(double... data) {
        super(data);
        if (D != 3) {
            throw new IllegalArgumentException("Unexpected Dimensions");
        }
    }

    public Vector3d(Vector that) {
        this(that.getData());
    }

    public Vector3d cross(Vector3d that) {
        return new Vector3d(
                this.data[1] * that.data[2] - this.data[2] * that.data[1],
                this.data[2] * that.data[0] - this.data[0] * that.data[2],
                this.data[0] * that.data[1] - this.data[1] * that.data[0]);
    }

    public double alpha() {
        return Math.atan2(this.data[1], this.data[0]);
    }

    public double delta() {
        double length = this.length();
        if (length == 0) {
            throw new ArithmeticException("Cannot get delta from a zero length vector.");
        }

        return Math.asin(this.data[2] / length);
    }

    public double radian(Vector3d that) {
        double thisLength = this.length();
        double thatLength = that.length();

        if (thisLength == 0 || thatLength == 0) {
            throw new ArithmeticException("Unexpected zero length vector.");
        }

        double radian = this.dot(that) * (1.0 / (thisLength * thatLength));
        radian = radian < -1.0 ? -1.0 : radian > 1.0 ? 1.0 : radian;
        return Math.acos(radian);
    }

    public void rotateXaxis(double radian){
        final double sin = Math.sin(radian);
        final double cos = Math.cos(radian);
        final double y = this.data[1];
        final double z = this.data[2];
        this.data[1] = y * cos + z * sin;
        this.data[2] = y * -sin + z * cos;
    }

    public void rotateYaxis(double radian){
        final double sin = Math.sin(radian);
        final double cos = Math.cos(radian);
        final double x = this.data[0];
        final double z = this.data[2];
        this.data[0] = x * cos - z * sin;
        this.data[2] = x * sin + z * cos;
    }

    public void rotateZaxis(double radian){
        final double sin = Math.sin(radian);
        final double cos = Math.cos(radian);
        final double x = this.data[0];
        final double y = this.data[1];
        this.data[0] = x * cos + y * sin;
        this.data[1] = x * -sin + y * cos;
    }
}