package com.gmail.jiangyang5157.tookit.algorithm.dlx;

/**
 * @author Yang
 */
public class HeadNode extends Node {

    // The roots count in this matrix
    private int size = 0;

    protected HeadNode(int size) {
        super(-1, null);
        this.size = size;
    }

    protected HeadNode(int size, Node left, Node right) {
        super(-1, null, null, null, left, right);
        this.size = size;
    }

    protected void setSize(int size) {
        this.size = size;
    }

    protected int getSize() {
        return size;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("HeadNode<size: ").append(size).append(">");
        return buffer.toString();
    }
}
