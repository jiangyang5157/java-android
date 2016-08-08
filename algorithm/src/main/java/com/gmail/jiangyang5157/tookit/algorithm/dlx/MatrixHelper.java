package com.gmail.jiangyang5157.tookit.algorithm.dlx;

import java.util.ArrayList;

/**
 * @author Yang
 */
public class MatrixHelper {

    private ArrayList<Node> rowHeaders;

    public MatrixHelper() {
        rowHeaders = new ArrayList<Node>();
    }

    /**
     * Builds a matrix based off the input integer representation
     *
     * @return A matrix
     */
    public Matrix build(final int[][] matrixCover) {
        rowHeaders.clear();

        int rowsCount = matrixCover.length;
        int columnsCount = rowsCount > 0 ? matrixCover[0].length : 0;

        // new head
        HeadNode head = new HeadNode(columnsCount);

        // - head - root 1 - root 2 - .... - root columnCount -
        Node root = head;
        for (int j = 0; j < columnsCount; j++, root = root.getRight()) {
            RootNode rightNode = new RootNode(j);
            rightNode.setLeft(root);
            root.setRight(rightNode);
        }
        root.setRight(head);
        head.setLeft(root);

        // |
        // root x
        // |
        // node 1
        // |
        // node 2
        // |
        // ....
        // node n
        // |
        root = head.getRight();
        Node rootLastNode = head.getRight();
        for (int j = 0; j < columnsCount; j++, root = rootLastNode = root
                .getRight()) {
            for (int i = 0; i < rowsCount; i++) {
                if (matrixCover[i][j] == 0) {
                    continue;
                }

                Node node = new Node(i, root);
                node.setUp(rootLastNode);
                rootLastNode.setDown(node);

                rootLastNode = rootLastNode.getDown();

                ((RootNode) root).setSize(((RootNode) root).getSize() + 1);
            }

            rootLastNode.setDown(root);
            root.setUp(rootLastNode);
        }

        root = head.getRight();
        Node[] rowNodesCache = new Node[columnsCount];
        for (int j = 0; j < columnsCount; j++, root = root.getRight()) {
            rowNodesCache[j] = root.getDown();
        }

        ArrayList<Node> rowNodes = new ArrayList<Node>();
        for (int i = 0; i < rowsCount; i++) {
            rowNodes.clear();

            for (int j = 0; j < columnsCount; j++) {
                if (matrixCover[i][j] != 0) {
                    rowNodes.add(rowNodesCache[j]);
                    rowNodesCache[j] = rowNodesCache[j].getDown();
                }
            }

            if (!rowNodes.isEmpty()) {
                rowHeaders.add(rowNodes.get(0));

                Node rowCursor = rowNodes.get(0);
                int size = rowNodes.size();
                for (int index = 0; index < size; index++) {
                    Node node = rowNodes.get(index);
                    if (node == rowNodes.get(0)) {
                        continue;
                    }
                    node.setLeft(rowCursor);
                    rowCursor.setRight(node);
                    rowCursor = node;
                }

                rowCursor.setRight(rowNodes.get(0));
                rowNodes.get(0).setLeft(rowCursor);
            } else {
                rowHeaders.add(null);
            }
        }

        return new Matrix(head);
    }

    /**
     * @param rowIndex The index of the row header node
     * @return The row header node by the given index
     */
    public Node getRowHeader(final int rowIndex) {
        return rowHeaders.get(rowIndex);
    }
}
