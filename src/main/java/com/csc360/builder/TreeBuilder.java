package com.csc360.builder;

import com.csc360.model.BinaryTree;
import com.csc360.model.TreeNode;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Factory and builder utility responsible for constructing BinaryTree instances
 * from input sequences.
 */
public class TreeBuilder {

    /**
     * Builds a BinaryTree in Level-Order (breadth-first) from a list of input values.
     *
     * @param <T> Type of value
     * @param values sequence of input values
     * @return constructed BinaryTree instance
     */
    public static <T> BinaryTree<T> buildLevelOrder(List<T> values) {
        BinaryTree<T> tree = new BinaryTree<>();
        if (values == null || values.isEmpty() || values.get(0) == null) {
            return tree;
        }

        TreeNode<T> root = new TreeNode<>(values.get(0));
        tree.setRoot(root);

        Queue<TreeNode<T>> queue = new LinkedList<>();
        queue.add(root);

        int i = 1;
        while (!queue.isEmpty() && i < values.size()) {
            TreeNode<T> current = queue.poll();

            // Left child
            if (i < values.size()) {
                T leftVal = values.get(i++);
                if (leftVal != null) {
                    TreeNode<T> leftNode = new TreeNode<>(leftVal);
                    current.setLeft(leftNode);
                    queue.add(leftNode);
                }
            }

            // Right child
            if (i < values.size()) {
                T rightVal = values.get(i++);
                if (rightVal != null) {
                    TreeNode<T> rightNode = new TreeNode<>(rightVal);
                    current.setRight(rightNode);
                    queue.add(rightNode);
                }
            }
        }

        return tree;
    }

    /**
     * Builds a Binary Search Tree (BST) from a list of comparable input values.
     *
     * @param <T> Type of value implementing Comparable interface
     * @param values sequence of input values
     * @return constructed BinaryTree instance configured as a BST
     */
    public static <T extends Comparable<T>> BinaryTree<T> buildBST(List<T> values) {
        BinaryTree<T> tree = new BinaryTree<>();
        if (values == null || values.isEmpty()) {
            return tree;
        }

        TreeNode<T> root = null;
        for (T val : values) {
            if (val != null) {
                root = insertBST(root, val);
            }
        }
        tree.setRoot(root);
        return tree;
    }

    private static <T extends Comparable<T>> TreeNode<T> insertBST(TreeNode<T> current, T data) {
        if (current == null) {
            return new TreeNode<>(data);
        }

        int cmp = data.compareTo(current.getData());
        if (cmp < 0) {
            current.setLeft(insertBST(current.getLeft(), data));
        } else if (cmp > 0) {
            current.setRight(insertBST(current.getRight(), data));
        }
        return current;
    }
}
