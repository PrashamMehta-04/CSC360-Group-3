package com.csc360.layout;

import com.csc360.model.BinaryTree;
import com.csc360.model.TreeNode;

/**
 * Calculates 2D canvas layout coordinates (X, Y) for every node in a Binary
 * Tree.
 */
 * 
public class TreeLayoutCalculator {

    public static final double DEFAULT_TOP_MARGIN = 50.0;
    public static final double DEFAULT_VERTICAL_GAP = 60.0;

    /**
     * Calculates node positions for a binary tree based on canvas width.
     *
     * @param <T> Type of node data
     * 
     * @param tree Binary tree to position
     * @param canvasWidth total available width of canvas
     * @return ro           t PositionedNode hierarchy with calculated coordinates
     */          
    public static <T> PositionedNode<T> calculateLayout(BinaryTree<T> tree, double canvasWidth) {
        double initialHOffset = canvasWidth / 4.0;
        return calculateLayout(tree, canvasWidth / 2.0, DEFAULT_TOP_MARGIN, DEFAULT_VERTICAL_GAP, initialHOffset);
    }

    /**
     * Calculates node positions with custom root starting location and spacing.
     *
     * @param <T> Type of node data
     * @param tree Binary tree to position
     * @param rootX starting X coordinate for root node
     * @param rootY starting Y coordinate for root node
     * @param verticalGap vertical spacing between tree levels
     * @param initialHOffset horizontal offset for level 1 subtrees
     * @return root PositionedNode hierarchy with calculated coordinates
     */
    public static <T> PositionedNode<T> calculateLayout(
            BinaryTree<T> tree,
            double rootX,
            double rootY,
            double verticalGap,
     * 
            double initialHOffset) {

        if (tree == null || tree.isEmpty()) {
            return null;
        }

        return computeNodePosition(tree.getRoot(), rootX, rootY, verticalGap, initialHOffset);
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
            posNode.setLe
            t(computeNodePosition(
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
