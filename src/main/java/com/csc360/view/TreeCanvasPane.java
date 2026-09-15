package com.csc360.view;

import com.csc360.layout.PositionedNode;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * JavaFX Pane responsible for rendering PositionedNode tree layouts using
 * orthogonal (vertical-horizontal-vertical) line connectors, supporting dynamic
 * canvas sizing and interactive Zoom/Pan.
 */
public class TreeCanvasPane extends Pane {

    private static final double NODE_RADIUS = 22.0;
    private static final Color NODE_COLOR = Color.web("#2B5B84");
    private static final Color BORDER_COLOR = Color.web("#1E3A5F");
    private static final Color LINE_COLOR = Color.web("#4A6B82");
    private static final Color TEXT_COLOR = Color.WHITE;

    private static final double MIN_ZOOM = 0.2;
    private static final double MAX_ZOOM = 3.0;

    private final DoubleProperty zoomScale = new SimpleDoubleProperty(1.0);

    // Mouse drag tracking for panning
    private double lastMouseX;
    private double lastMouseY;

    public TreeCanvasPane() {
        setStyle("-fx-background-color: #F8FAFC; -fx-border-color: #E2E8F0; -fx-border-width: 1;");

        // Bind JavaFX scale transforms to zoomScale property
        scaleXProperty().bind(zoomScale);
        scaleYProperty().bind(zoomScale);

        // Setup mouse scroll wheel zoom
        setOnScroll(this::handleScroll);

        // Setup click-and-drag panning
        setOnMousePressed(event -> {
            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
        });

        setOnMouseDragged(event -> {
            double deltaX = event.getSceneX() - lastMouseX;
            double deltaY = event.getSceneY() - lastMouseY;

            setTranslateX(getTranslateX() + deltaX);
            setTranslateY(getTranslateY() + deltaY);

            lastMouseX = event.getSceneX();
            lastMouseY = event.getSceneY();
        });
    }

    private void handleScroll(ScrollEvent event) {
        event.consume();
        double delta = event.getDeltaY();
        double scaleFactor = (delta > 0) ? 1.1 : 0.9;
        setZoomScale(zoomScale.get() * scaleFactor);
    }

    public DoubleProperty zoomScaleProperty() {
        return zoomScale;
    }

    public double getZoomScale() {
        return zoomScale.get();
    }

    public void setZoomScale(double scale) {
        double clamped = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, scale));
        zoomScale.set(clamped);
    }

    public void zoomIn() {
        setZoomScale(zoomScale.get() * 1.2);
    }

    public void zoomOut() {
        setZoomScale(zoomScale.get() / 1.2);
    }

    public void resetZoom() {
        zoomScale.set(1.0);
        setTranslateX(0);
        setTranslateY(0);
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

        // Draw orthogonal connecting lines first (behind node circles)
        drawLines(rootLayout);

        // Draw node circles and text labels
        drawNodes(rootLayout);
    }

    private <T> void drawLines(PositionedNode<T> node) {
        if (node == null) return;

        PositionedNode<T> left = node.getLeft();
        PositionedNode<T> right = node.getRight();

        if (left != null || right != null) {
            double parentX = node.getX();
            double parentY = node.getY();

            double childY = (left != null) ? left.getY() : right.getY();
            double midY = parentY + (childY - parentY) / 2.0;

            // 1. Vertical line going straight down from parent to midY
            Line stemLine = createLine(parentX, parentY, parentX, midY);
            getChildren().add(stemLine);

            if (left != null && right != null) {
                // Horizontal bar connecting left child X to right child X at midY
                Line horizLine = createLine(left.getX(), midY, right.getX(), midY);
                getChildren().add(horizLine);

                // Vertical drop line to left child
                Line leftDrop = createLine(left.getX(), midY, left.getX(), left.getY());
                getChildren().add(leftDrop);

                // Vertical drop line to right child
                Line rightDrop = createLine(right.getX(), midY, right.getX(), right.getY());
                getChildren().add(rightDrop);

            } else if (left != null) {
                // Horizontal bar to left child X at midY
                Line horizLine = createLine(parentX, midY, left.getX(), midY);
                getChildren().add(horizLine);

                // Vertical drop line to left child
                Line leftDrop = createLine(left.getX(), midY, left.getX(), left.getY());
                getChildren().add(leftDrop);

            } else {
                // Horizontal bar to right child X at midY
                Line horizLine = createLine(parentX, midY, right.getX(), midY);
                getChildren().add(horizLine);

                // Vertical drop line to right child
                Line rightDrop = createLine(right.getX(), midY, right.getX(), right.getY());
                getChildren().add(rightDrop);
            }
        }

        if (left != null) drawLines(left);
        if (right != null) drawLines(right);
    }

    private Line createLine(double startX, double startY, double endX, double endY) {
        Line line = new Line(startX, startY, endX, endY);
        line.setStroke(LINE_COLOR);
        line.setStrokeWidth(2.5);
        return line;
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
