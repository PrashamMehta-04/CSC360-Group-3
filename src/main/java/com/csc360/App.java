package com.csc360;

import com.csc360.builder.TreeBuilder;
import com.csc360.layout.PositionedNode;
import com.csc360.layout.TreeLayoutCalculator;
import com.csc360.model.BinaryTree;
import com.csc360.view.TreeCanvasPane;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Interactive Binary Tree Visualizer JavaFX Application.
 * Supports auto-expanding canvas with scrollbars, mouse scroll zoom, drag-to-pan,
 * and zero node overlapping layout algorithms.
 */
public class App extends Application {

    private TreeCanvasPane canvasPane;
    private ScrollPane scrollPane;
    private TextField inputField;
    private ComboBox<String> treeTypeComboBox;
    private Label statusLabel;
    private Label zoomLabel;
    private Slider spacingSlider;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Interactive Binary Tree Visualizer - CSC360");

        BorderPane rootPane = new BorderPane();
        rootPane.setPadding(new Insets(10));

        // Create canvas pane and wrap in a Group so transforms scale cleanly inside ScrollPane
        canvasPane = new TreeCanvasPane();
        Group canvasGroup = new Group(canvasPane);

        scrollPane = new ScrollPane(canvasGroup);
        scrollPane.setFitToWidth(false);
        scrollPane.setFitToHeight(false);
        scrollPane.setPannable(true); // Enables drag panning inside ScrollPane
        scrollPane.setStyle("-fx-background-color: #F8FAFC; -fx-background: #F8FAFC;");
        rootPane.setCenter(scrollPane);

        // Control Panel at top
        HBox controlBox = new HBox(10);
        controlBox.setAlignment(Pos.CENTER_LEFT);
        controlBox.setPadding(new Insets(10, 10, 12, 10));
        controlBox.setStyle("-fx-background-color: #EDF2F7; -fx-background-radius: 6;");

        Label inputLabel = new Label("Input Sequence:");
        inputLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2D3748;");

        inputField = new TextField("10, 5, 15, 2, 7, 12, 20");
        inputField.setPromptText("Enter numbers separated by commas or spaces");
        inputField.setPrefWidth(260);

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

        // Zoom Controls
        Label zoomTitle = new Label("Zoom:");
        zoomTitle.setStyle("-fx-font-weight: bold; -fx-text-fill: #2D3748; -fx-padding: 0 0 0 10;");

        Button zoomInBtn = new Button("+");
        zoomInBtn.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
        zoomInBtn.setOnAction(e -> {
            canvasPane.zoomIn();
            updateZoomLabel();
        });

        Button zoomOutBtn = new Button("-");
        zoomOutBtn.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
        zoomOutBtn.setOnAction(e -> {
            canvasPane.zoomOut();
            updateZoomLabel();
        });

        Button resetZoomBtn = new Button("Reset");
        resetZoomBtn.setStyle("-fx-font-weight: bold; -fx-cursor: hand;");
        resetZoomBtn.setOnAction(e -> {
            canvasPane.resetZoom();
            updateZoomLabel();
        });

        zoomLabel = new Label("100%");
        zoomLabel.setStyle("-fx-text-fill: #4A5568; -fx-font-size: 12px;");

        // Listen for scroll wheel zoom changes
        canvasPane.zoomScaleProperty().addListener((obs, oldVal, newVal) -> updateZoomLabel());

        // Spacing Controls
        Label spacingLabel = new Label("Spacing:");
        spacingLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #2D3748; -fx-padding: 0 0 0 10;");

        spacingSlider = new Slider(40, 150, TreeLayoutCalculator.DEFAULT_VERTICAL_GAP);
        spacingSlider.setShowTickMarks(true);
        spacingSlider.setShowTickLabels(false);
        spacingSlider.setPrefWidth(100);
        spacingSlider.valueProperty().addListener((obs, oldVal, newVal) -> handleBuildTree());

        controlBox.getChildren().addAll(
                inputLabel, inputField,
                modeLabel, treeTypeComboBox,
                buildBtn, clearBtn,
                spacingLabel, spacingSlider,
                zoomTitle, zoomInBtn, zoomOutBtn, resetZoomBtn, zoomLabel
        );
        rootPane.setTop(controlBox);

        // Status bar at bottom
        statusLabel = new Label("Ready. Enter numbers and click 'Build Tree'. Use mouse wheel or Drag to Pan & Zoom.");
        statusLabel.setPadding(new Insets(8, 5, 0, 5));
        statusLabel.setStyle("-fx-text-fill: #718096; -fx-font-size: 12px;");
        rootPane.setBottom(statusLabel);

        // Build default tree on startup
        handleBuildTree();

        Scene scene = new Scene(rootPane, 1050, 700);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateZoomLabel() {
        int percent = (int) Math.round(canvasPane.getZoomScale() * 100);
        zoomLabel.setText(percent + "%");
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

        double viewportWidth = scrollPane.getViewportBounds().getWidth() > 0 ? scrollPane.getViewportBounds().getWidth() : 1000.0;
        double viewportHeight = scrollPane.getViewportBounds().getHeight() > 0 ? scrollPane.getViewportBounds().getHeight() : 600.0;
        
        double verticalGap = spacingSlider != null ? spacingSlider.getValue() : TreeLayoutCalculator.DEFAULT_VERTICAL_GAP;

        double reqWidth = TreeLayoutCalculator.calculateRequiredWidth(tree, viewportWidth);
        double reqHeight = TreeLayoutCalculator.calculateRequiredHeight(tree, viewportHeight, verticalGap);

        canvasPane.setPrefSize(reqWidth, reqHeight);

        PositionedNode<Integer> layout = TreeLayoutCalculator.calculateLayout(tree, reqWidth, verticalGap);
        canvasPane.renderTree(layout);

        statusLabel.setText(String.format("Status: Built %s tree with %d nodes. Canvas Size: %.0fx%.0f. Scroll/Drag/Zoom to navigate.", mode, values.size(), reqWidth, reqHeight));
    }

    private void handleClear() {
        inputField.setText("");
        canvasPane.renderTree(null);
        canvasPane.resetZoom();
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
