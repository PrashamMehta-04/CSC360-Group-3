package com.csc360;

import com.csc360.builder.TreeBuilder;
import com.csc360.layout.PositionedNode;
import com.csc360.layout.TreeLayoutCalculator;
import com.csc360.model.BinaryTree;
import com.csc360.view.TreeCanvasPane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactive Binary Tree Visualizer JavaFX Application.
 * Allows users to input values to construct and visualize binary trees in real-time.
 */
public class App extends Application {

    private TreeCanvasPane canvasPane;
    private TextField inputField;
    private ComboBox<String> treeTypeComboBox;
    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interactive Binary Tree Visualizer - CSC360");

        BorderPane rootPane = new BorderPane();
        rootPane.setPadding(new Insets(10));

        // Canvas for rendering the tree
        canvasPane = new TreeCanvasPane();
        canvasPane.setPrefSize(900, 550);
        rootPane.setCenter(canvasPane);

        // Control Panel at top
        HBox controlBox = new HBox(12);
        controlBox.setAlignment(Pos.CENTER_LEFT);
        controlBox.setPadding(new Insets(10, 10, 15, 10));
        controlBox.setStyle("-fx-background-color: #EDF2F7; -fx-background-radius: 6;");

        Label inputLabel = new Label("Input Sequence:");
        inputLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2D3748;");

        inputField = new TextField("10, 5, 15, 2, 7, 12, 20");
        inputField.setPromptText("Enter numbers separated by commas or spaces");
        inputField.setPrefWidth(280);

        Label modeLabel = new Label("Tree Mode:");
        modeLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2D3748;");

        treeTypeComboBox = new ComboBox<>();
        treeTypeComboBox.getItems().addAll("Left-to-Right (Level Order)", "Binary Search Tree (BST)");
        treeTypeComboBox.setValue("Left-to-Right (Level Order)");

        Button buildBtn = new Button("Build Tree");
        buildBtn.setStyle("-fx-background-color: #3182CE; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        buildBtn.setOnAction(e -> handleBuildTree());

        Button clearBtn = new Button("Clear");
        clearBtn.setStyle("-fx-background-color: #E2E8F0; -fx-text-fill: #4A5568; -fx-font-weight: bold; -fx-cursor: hand;");
        clearBtn.setOnAction(e -> handleClear());

        controlBox.getChildren().addAll(inputLabel, inputField, modeLabel, treeTypeComboBox, buildBtn, clearBtn);
        rootPane.setTop(controlBox);

        // Status bar at bottom
        statusLabel = new Label("Ready. Enter numbers and click 'Build Tree'.");
        statusLabel.setPadding(new Insets(8, 5, 0, 5));
        statusLabel.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px;");
        rootPane.setBottom(statusLabel);

        // Build default tree on startup
        handleBuildTree();

        Scene scene = new Scene(rootPane, 950, 650);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Re-render layout if window resizes
        canvasPane.widthProperty().addListener((obs, oldVal, newVal) -> handleBuildTree());
    }

    private void handleBuildTree() {
        String rawInput = inputField.getText();
        if (rawInput == null || rawInput.trim().isEmpty()) {
            canvasPane.renderTree(null);
            statusLabel.setText("Status: Tree cleared (empty input).");
            return;
        }

        List<Integer> values = parseInputValues(rawInput);
        if (values.isEmpty()) {
            statusLabel.setText("Status: No valid numbers found in input.");
            return;
        }

        BinaryTree<Integer> tree;
        String mode = treeTypeComboBox.getValue();
        if ("Binary Search Tree (BST)".equals(mode)) {
            tree = TreeBuilder.buildBST(values);
        } else {
            tree = TreeBuilder.buildLevelOrder(values);
        }

        double width = canvasPane.getWidth() > 0 ? canvasPane.getWidth() : 900.0;
        PositionedNode<Integer> layout = TreeLayoutCalculator.calculateLayout(tree, width);
        canvasPane.renderTree(layout);

        statusLabel.setText(String.format("Status: Built %s tree with %d nodes.", mode, values.size()));
    }

    private void handleClear() {
        inputField.setText("");
        canvasPane.renderTree(null);
        statusLabel.setText("Status: Cleared.");
    }

    private List<Integer> parseInputValues(String raw) {
        List<Integer> list = new ArrayList<>();
        String[] tokens = raw.split("[,\\s]+");
        for (String token : tokens) {
            if (!token.trim().isEmpty()) {
                try {
                    list.add(Integer.parseInt(token.trim()));
                } catch (NumberFormatException ignored) {
                    // Ignore non-integer tokens
                }
            }
        }
        return list;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
