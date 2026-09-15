package com.csc360.layout;

import com.csc360.model.TreeNode;

/**
 * Represents a tree node with calculated 2D canvas coordinates (X, Y).
 *
 * @param <T> Data type contained within the node
 */
public class PositionedNode<T> {
    private final TreeNode<T> treeNode;
    private final double x;
    private final double y;
    private PositionedNode<T> left;
    private PositionedNode<T> right;

    public PositionedNode(TreeNode<T> treeNode, double x, double y) {
        this.treeNode = treeNode;
        this.x = x;
        this.y = y;
        this.left = null;
        this.right = null;
    }

    public TreeNode<T> getTreeNode() {
        return treeNode;
    }

    public T getData() {
        return treeNode != null ? treeNode.getData() : null;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public PositionedNode<T> getLeft() {
        return left;
    }

    public void setLeft(PositionedNode<T> left) {
        this.left = left;
    }

    public PositionedNode<T> getRight() {
        return right;
    }

    public void setRight(PositionedNode<T> right) {
        this.right = right;
    }

    public boolean isLeaf() {
        return left == null && right == null;
    }
}
