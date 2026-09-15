package com.csc360.layout;

import com.csc360.model.BinaryTree;
import com.csc360.model.TreeNode;

/**
 * Calculates 2D canvas layout coordinates (X, Y) for every node in a Binary Tree,
 * guaranteeing zero node overlap by dynamically scaling canvas width based on tree depth.
 */
public class TreeLayoutCalculator {

    public static final double DEFAULT_TOP_MARGIN = 60.0;
    public static final double DEFAULT_VERTICAL_GAP = 75.0;
    public static final double MIN_LEAF_SPACING = 55.0;

    /**
     * Calculates node positions for a binary tree, dynamically expanding canvas width
     * if tree depth requires it to prevent node overlap.
     *
     * @param <T> Type of node data
     * @param tree Binary tree to position
     * @param viewportWidth available viewport width
     * @return root PositionedNode hierarchy with calculated coordinates
     */
    public static <T> PositionedNode<T> calculateLayout(BinaryTree<T> tree, double viewportWidth) {
        return calculateLayout(tree, viewportWidth, DEFAULT_VERTICAL_GAP);
    }

    /**
     * Calculates node positions for a binary tree with a custom vertical gap.
     */
    public static <T> PositionedNode<T> calculateLayout(BinaryTree<T> tree, double viewportWidth, double verticalGap) {
        if (tree == null || tree.isEmpty()) {
            return null;
        }

        int depth = getTreeDepth(tree.getRoot());
        int maxLeavesAtBottom = 1 << Math.max(0, depth - 1);
        double minRequiredWidth = maxLeavesAtBottom * MIN_LEAF_SPACING;

        double totalWidth = Math.max(viewportWidth, minRequiredWidth);
        double rootX = totalWidth / 2.0;
        double initialHOffset = totalWidth / 4.0;

        return computeNodePosition(tree.getRoot(), rootX, DEFAULT_TOP_MARGIN, verticalGap, initialHOffset);
    }

    /**
     * Calculates node positions with custom root starting location and spacing.
     */
    public static <T> PositionedNode<T> calculateLayout(
            BinaryTree<T> tree,
            double rootX,
            double rootY,
            double verticalGap,
            double initialHOffset) {

        if (tree == null || tree.isEmpty()) {
            return null;
        }

        return computeNodePosition(tree.getRoot(), rootX, rootY, verticalGap, initialHOffset);
    }

    /**
     * Calculates the total required canvas width needed to draw the tree without overlap.
     */
    public static <T> double calculateRequiredWidth(BinaryTree<T> tree, double viewportWidth) {
        if (tree == null || tree.isEmpty()) {
            return viewportWidth;
        }
        int depth = getTreeDepth(tree.getRoot());
        int maxLeavesAtBottom = 1 << Math.max(0, depth - 1);
        return Math.max(viewportWidth, maxLeavesAtBottom * MIN_LEAF_SPACING);
    }

    /**
     * Calculates the total required canvas height needed to draw the tree.
     */
    public static <T> double calculateRequiredHeight(BinaryTree<T> tree, double viewportHeight) {
        return calculateRequiredHeight(tree, viewportHeight, DEFAULT_VERTICAL_GAP);
    }

    public static <T> double calculateRequiredHeight(BinaryTree<T> tree, double viewportHeight, double verticalGap) {
        if (tree == null || tree.isEmpty()) {
            return viewportHeight;
        }
        int depth = getTreeDepth(tree.getRoot());
        double minHeight = (depth + 1) * verticalGap + DEFAULT_TOP_MARGIN;
        return Math.max(viewportHeight, minHeight);
    }

    private static <T> int getTreeDepth(TreeNode<T> node) {
        if (node == null) return 0;
        return 1 + Math.max(getTreeDepth(node.getLeft()), getTreeDepth(node.getRight()));
    }

    private static <T> PositionedNode<T> computeNodePosition(
            TreeNode<T> node,
            double x,
            double y,
            double verticalGap,
            double hOffset) {

        if (node == null) {
            return null;
        }

        PositionedNode<T> posNode = new PositionedNode<>(node, x, y);

        if (node.getLeft() != null) {
            posNode.setLeft(computeNodePosition(
                    node.getLeft(),
                    x - hOffset,
                    y + verticalGap,
                    verticalGap,
                    hOffset / 2.0
            ));
        }

        if (node.getRight() != null) {
            posNode.setRight(computeNodePosition(
                    node.getRight(),
                    x + hOffset,
                    y + verticalGap,
                    verticalGap,
                    hOffset / 2.0
            ));
        }

        return posNode;
    }
}
