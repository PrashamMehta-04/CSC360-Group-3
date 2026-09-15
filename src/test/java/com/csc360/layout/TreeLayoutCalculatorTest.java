package com.csc360.layout;

import com.csc360.builder.TreeBuilder;
import com.csc360.model.BinaryTree;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TreeLayoutCalculatorTest {

    @Test
    public void testEmptyTreeLayout() {
        BinaryTree<Integer> emptyTree = new BinaryTree<>();
        PositionedNode<Integer> layout = TreeLayoutCalculator.calculateLayout(emptyTree, 800.0);
        assertNull(layout);
    }

    @Test
    public void testLayoutCalculation() {
        // Build level order tree: root 10, left 5, right 15
        List<Integer> inputs = List.of(10, 5, 15);
        BinaryTree<Integer> tree = TreeBuilder.buildLevelOrder(inputs);

        double canvasWidth = 800.0;
        PositionedNode<Integer> rootPos = TreeLayoutCalculator.calculateLayout(tree, canvasWidth);

        assertNotNull(rootPos);
        assertEquals(10, rootPos.getData());
        assertEquals(400.0, rootPos.getX(), 0.001); // Center of canvas
        assertEquals(TreeLayoutCalculator.DEFAULT_TOP_MARGIN, rootPos.getY(), 0.001);

        PositionedNode<Integer> leftPos = rootPos.getLeft();
        PositionedNode<Integer> rightPos = rootPos.getRight();

        assertNotNull(leftPos);
        assertEquals(5, leftPos.getData());
        assertEquals(400.0 - 200.0, leftPos.getX(), 0.001);
        assertEquals(TreeLayoutCalculator.DEFAULT_TOP_MARGIN + TreeLayoutCalculator.DEFAULT_VERTICAL_GAP, leftPos.getY(), 0.001);

        assertNotNull(rightPos);
        assertEquals(15, rightPos.getData());
        assertEquals(400.0 + 200.0, rightPos.getX(), 0.001);
        assertEquals(TreeLayoutCalculator.DEFAULT_TOP_MARGIN + TreeLayoutCalculator.DEFAULT_VERTICAL_GAP, rightPos.getY(), 0.001);
    }
}
