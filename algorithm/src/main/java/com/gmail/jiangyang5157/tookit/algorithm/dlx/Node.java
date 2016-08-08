package com.gmail.jiangyang5157.tookit.algorithm.dlx;

/**
 * @author Yang
 */
public class Node {

	// The associated row
	private int row = 0;
	// The associated (column) root node
	private Node root;

	private Node up;
	private Node down;
	private Node left;
	private Node right;

	protected Node(int row, Node root) {
		this.row = row;
		this.root = root;
	}

	protected Node(int row, Node root, Node up, Node down, Node left, Node right) {
		this.row = row;
		this.root = root;

		this.up = up;
		this.down = down;
		this.left = left;
		this.right = right;
	}

	public int getRow() {
		return row;
	}
	public Node getRoot() {
		return root;
	}

	protected void setUp(Node up) {
		this.up = up;
	}
	public Node getUp() {
		return up;
	}
	protected void setDown(Node down) {
		this.down = down;
	}
	public Node getDown() {
		return down;
	}
	protected void setLeft(Node left) {
		this.left = left;
	}
	public Node getLeft() {
		return left;
	}
	protected void setRight(Node right) {
		this.right = right;
	}
	public Node getRight() {
		return right;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Node<row: ").append(row).append(", root: ").append(root.toString()).append(">");
		return buffer.toString();
	}
}
