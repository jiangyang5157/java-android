package com.gmail.jiangyang5157.tookit.algorithm.dlx;

import java.util.ArrayList;

/**
 * see more efficient golang implementation: https://github.com/jiangyang5157/go-dlx
 * @author Yang
 */
@Deprecated
public class Matrix {
    private final static String TAG = "[Matrix]";

    private HeadNode head = null;
    private ArrayList<Node> cache = null;

    private ArrayList<int[]> results = null;
    private int maxResultsSize = 0;

    /**
     * @param head The head node of the toroidal doubly-linked list
     */
    protected Matrix(HeadNode head) {
        this.head = head;

        cache = new ArrayList<Node>();
        results = new ArrayList<int[]>();
    }

    /**
     * Performs a cover operation on a given column node
     *
     * @param column A given column node
     */
    public void cover(Node column) {
        column.getRight().setLeft(column.getLeft());
        column.getLeft().setRight(column.getRight());

        for (Node rowNode = column.getDown(); rowNode != column; rowNode = rowNode
                .getDown()) {
            for (Node rightNode = rowNode.getRight(); rightNode != rowNode; rightNode = rightNode
                    .getRight()) {
                rightNode.getDown().setUp(rightNode.getUp());
                rightNode.getUp().setDown(rightNode.getDown());
            }
        }
    }

    /**
     * Performs an uncover operation on a given column node
     *
     * @param column A given column node
     */
    public void uncover(Node column) {
        for (Node rowNode = column.getUp(); rowNode != column; rowNode = rowNode
                .getUp()) {
            for (Node leftNode = rowNode.getLeft(); leftNode != rowNode; leftNode = leftNode
                    .getLeft()) {
                leftNode.getDown().setUp(leftNode);
                leftNode.getUp().setDown(leftNode);
            }
        }

        column.getRight().setLeft(column);
        column.getLeft().setRight(column);
    }

    /**
     * Performs a search operation on the given toroidal doubly-linked list
     *
     * @param k The current iteration of the search operation
     */
    private void search(final int k) {
//        System.out.println(TAG + " search(" + k + ")");
        if (k == 0) {
            cache.clear();
        }

        Node root = head.getRight();

        if (root == head) {
//            System.out.println(TAG + " Find a solution in search(" + k + ")");
            final int size = cache.size();
            int[] rows = new int[size];
            for (int i = 0; i < size; i++) {
                Node node = cache.get(i);
                rows[i] = node.getRow();
            }

            results.add(rows);

            return;
        }

        cover(root);
        for (Node rowNode = root.getDown(); (results.size() < maxResultsSize)
                && (rowNode != root); rowNode = rowNode.getDown()) {
            cache.add(rowNode);
            for (Node rightNode = rowNode.getRight(); rightNode != rowNode; rightNode = rightNode
                    .getRight()) {
                cover(rightNode.getRoot());
            }

            search(k + 1);

            for (Node rightNode = rowNode.getRight(); rightNode != rowNode; rightNode = rightNode
                    .getRight()) {
                uncover(rightNode.getRoot());
            }
            cache.remove(rowNode);
        }
        uncover(root);
    }

    public ArrayList<int[]> solve(final int maxResultsSize) {
        this.maxResultsSize = maxResultsSize;

        results.clear();
        search(0);
        return results;
    }
}
