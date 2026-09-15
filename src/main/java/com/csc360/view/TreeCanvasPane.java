package com.csc360.view;

import com.csc360.layout.PositionedNode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * JavaFX Pane responsible for rendering PositionedNode tree layouts.
 */
public class TreeCanvasPane extends Pane {

    private static final double NODE_RADIUS = 22.0;
    private static final Color NODE_COLOR = Color.web("#2B5B84");
    private static final Color BORDER_COLOR = Color.web("#1E3A5F");
    private static final Color LINE_COLOR = Color.web("#4A6B82");
    private static final Color TEXT_COLOR = Color.WHITE;

    public TreeCanvasPane() {
        setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-width: 1;");
    }

    /**
     * Renders a positioned tree layout hierarchy on the canvas pane.
     *
     * @param <T> Data type of node values
     * @param rootLayout root node with calculated layout coordinates
     */
    public <T> void renderTree(PositionedNode<T> rootLayout) {
        getChildren().clear();
        if (rootLayout == null) {
            return;
        }

        // Draw connecting lines first (so lines render behind node circles)
        drawLines(rootLayout);

        // Draw node circles and text labels
        drawNodes(rootLayout);
    }

    private <T> void drawLines(PositionedNode<T> node) {
        if (node == null) return;

        if (node.getLeft() != null) {
            Line leftLine = new Line(node.getX(), node.getY(), node.getLeft().getX(), node.getLeft().getY());
            leftLine.setStroke(LINE_COLOR);
            leftLine.setStrokeWidth(2.5);
            getChildren().add(leftLine);
            drawLines(node.getLeft());
        }

        if (node.getRight() != null) {
            Line rightLine = new Line(node.getX(), node.getY(), node.getRight().getX(), node.getRight().getY());
            rightLine.setStroke(LINE_COLOR);
            rightLine.setStrokeWidth(2.5);
            getChildren().add(rightLine);
            drawLines(node.getRight());
        }
    }

    private <T> void drawNodes(PositionedNode<T> node) {
        if (node == null) return;

        // Draw circle for current node
        Circle circle = new Circle(node.getX(), node.getY(), NODE_RADIUS);
        circle.setFill(NODE_COLOR);
        circle.setStroke(BORDER_COLOR);
        circle.setStrokeWidth(2.0);

        // Draw text label centered inside node
        String valText = node.getData() != null ? node.getData().toString() : "";
        Text text = new Text(valText);
        text.setFont(Font.font("Segoe UI", FontWeight.BOLD, 14));
        text.setFill(TEXT_COLOR);

        // Center text on (X, Y)
        text.setX(node.getX() - text.getLayoutBounds().getWidth() / 2.0);
        text.setY(node.getY() + text.getLayoutBounds().getHeight() / 4.0);

        getChildren().addAll(circle, text);

        drawNodes(node.getLeft());
        drawNodes(node.getRight());
    }
}
