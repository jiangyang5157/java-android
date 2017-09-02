package com.gmail.jiangyang5157.java_dlx;

/**
 * @author Yang
 */
public class RootNode extends Node {

	// The associated column
	private int column = 0;

	//The nodes count in this column
	private int size = 0;

	protected RootNode(int column) {
		super(-1, null);
		this.column = column;
	}

	protected RootNode(int column, int size, Node up, Node down, Node left,
					   Node right) {
		super(-1, null, up, down, left, right);
		this.column = column;
		this.size = size;
	}

	protected void setColumn(int column) {
		this.column = column;
	}
	protected int getColumn() {
		return column;
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
		buffer.append("RootNode<column: ").append(column).append(", size: ").append(size).append(">");
		return buffer.toString();
	}
}
