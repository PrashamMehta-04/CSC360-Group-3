package com.csc360.builder;

import com.csc360.model.BinaryTree;
import com.csc360.model.TreeNode;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TreeBuilderTest {

    @Test
    public void testBuildLevelOrder() {
        List<Integer> inputs = Arrays.asList(10, 5, 15, 2, 7);
        BinaryTree<Integer> tree = TreeBuilder.buildLevelOrder(inputs);

        assertNotNull(tree.getRoot());
        assertEquals(10, tree.getRoot().getData());

        TreeNode<Integer> left = tree.getRoot().getLeft();
        TreeNode<Integer> right = tree.getRoot().getRight();

        assertNotNull(left);
        assertEquals(5, left.getData());
        assertNotNull(right);
        assertEquals(15, right.getData());

        assertNotNull(left.getLeft());
        assertEquals(2, left.getLeft().getData());
        assertNotNull(left.getRight());
        assertEquals(7, left.getRight().getData());
    }
}