# Implementation Plan: JavaFX Binary Tree & BST Visualizer

A JavaFX desktop application to construct, render, and visually interact with **Binary Trees** (Level-Order) and **Binary Search Trees (BST)**.

## Project Goal
"Write a JavaFx program to draw a binary tree."

The app provides two modes of tree generation:
1. **Level-Order Binary Tree**: Constructs the tree from left-to-right top-to-bottom based on input sequence.
2. **Binary Search Tree (BST)**: Orders inserted elements according to BST rules ($left < root < right$).

---

## Proposed Architectural Components

### 1. Build & Configuration Layer

#### `pom.xml`
- Configured JavaFX 21 dependencies (`javafx-controls`, `javafx-fxml`, `javafx-graphics`).
- Configured `javafx-maven-plugin` (v0.0.8) pointing to `com.csc360.App`.

---

### 2. Domain & Rendering Models

#### `Line.java` (`com.csc360.model`)
- Encapsulates line endpoints (`Point2D`), stroke width, color, and highlight state for tree edges.

#### `Circle.java` (`com.csc360.model`)
- Encapsulates node coordinates (`Point2D`), search key text, radius, fill/border colors, and highlight state for tree nodes.

---

### 3. Tree Layout Engine

#### `TreeLayoutCalculator.java` (`com.csc360.layout`)
- Computes $(x, y)$ positions for each `TreeNode<T>` in the tree.
- Dynamically calculates horizontal spacing per depth level so tree branches do not overlap.
- Generates `Circle` and `Line` rendering objects.

---

### 4. JavaFX User Interface (UI) Layer

#### `TreeCanvas.java` (`com.csc360.view`)
- JavaFX Canvas view component.
- Handles window resizing events and redraws `Circle` nodes and `Line` edges using `GraphicsContext`.

#### `ControlPanel.java` (`com.csc360.view`)
- User inputs (comma/space-separated numbers).
- Mode selection (Level-Order Binary Tree vs BST).
- Action buttons (**Build Tree**, **Insert**, **Delete**, **Search**, **Clear**).
- Status bar displaying tree depth, node count, and traversal orders (In-order, Pre-order, Post-order).

#### `App.java` (`com.csc360`)
- JavaFX `Application` entry point launching `Stage` and setting up the main `BorderPane` scene.
