# Architecture

SwissKit is built with a modular, plugin-based architecture that allows for easy extension and maintenance. This section covers the core architectural patterns and design decisions.

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Plugin System](#plugin-system)
- [UI Components](#ui-components)
- [Background Processing](#background-processing)
- [Excel Processing](#excel-processing)
- [Technology Stack](#technology-stack)

## Overview

SwissKit follows these architectural principles:

- **Modularity** - Each tool is a self-contained module
- **Extensibility** - Easy to add new tools via plugins
- **Performance** - Efficient processing with streaming and async operations
- **Maintainability** - Clean code structure and separation of concerns

## Project Structure

```
SwissKit/
├── src/main/java/fan/summer/
│   ├── Main.java                        # Application entry point
│   ├── annoattion/                      # Annotations
│   │   └── SwissKitPage.java           # Page annotation
│   ├── kitpage/                         # Tool page modules
│   │   ├── KitPage.java                # Page interface
│   │   ├── KitPageScanner.java         # Auto-discovery scanner
│   │   ├── WelcomePage.java            # Welcome page
│   │   ├── email/                      # Email tool
│   │   │   ├── EmailKitPage.java
│   │   │   └── EmailKitPage.form
│   │   └── excel/                      # Excel tool
│   │       ├── ExcelKitPage.java
│   │       ├── ExcelKitPage.form
│   │       ├── listener/               # Event listeners
│   │       │   ├── HeaderListener.java
│   │       │   └── NoModelDataListener.java
│   │       └── worker/                 # Background workers
│   │           ├── ExcelAnalysisWorker.java
│   │           ├── ExcelAnalysisCallback.java
│   │           └── ExcelSplitWorker.java
│   ├── ui/                             # Custom UI components
│   │   └── components/
│   │       ├── GradientProgressBar.java
│   │       └── FixedWidthComboBox.java
│   └── utils/                          # Utility classes
│       ├── SideMenuBar.java
│       └── UIUtils.java
└── docs/                               # Documentation
```

## Plugin System

The plugin system is the core of SwissKit's extensibility.

### @SwissKitPage Annotation

All tools use the `@SwissKitPage` annotation for configuration:

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SwissKitPage {
    String menuName() default "";
    String menuTooltip() default "";
    boolean visible() default true;
    int order() default 0;
}
```

### KitPage Interface

All tools must implement the `KitPage` interface:

```java
public interface KitPage {
    JPanel getPanel();              // Get page panel
    default String getMenuName() {  // Get menu display name (from annotation)
        return "";
    }
    default Icon getMenuIcon() {    // Get menu icon (optional)
        return null;
    }
    default String getMenuTooltip() { // Get menu tooltip (from annotation)
        return "";
    }
}
```

### KitPageScanner

The `KitPageScanner` class handles automatic discovery of all tool pages:

```java
public class KitPageScanner {
    /**
     * Scans for KitPage implementations in the specified package and subpackages.
     *
     * @param packageName the base package to scan
     * @return list of visible KitPage instances sorted by order
     */
    public static List<KitPage> scan(String packageName) {
        // 1. Find all classes with @SwissKitPage annotation
        // 2. Filter by visible = true
        // 3. Sort by order() value
        // 4. Instantiate and return
    }
}
```

### Auto-Discovery Mechanism

SwissKit automatically discovers and loads all tools at runtime:

**Discovery Methods**:

1. **File System Scanning** - Scans compiled `.class` files recursively
2. **JAR File Scanning** - Reads JAR entries for packaged applications

**Loading Process**:

```java
// HomePage.java
private void initPages() {
    // Scan all packages under fan.summer.kitpage
    // Includes subpackages: email/, excel/, etc.
    pages = KitPageScanner.scan("fan.summer.kitpage");
}
```

**Key Features**:

- No manual registration required
- Recursive subpackage scanning
- Automatic sorting by `order` value
- Visibility filtering
- Supports both development and production environments
- Fallback mechanism if scanning fails

## UI Components

SwissKit includes custom UI components for a modern, consistent appearance.

### SideMenuBar

Dynamic side menu with the following architecture:

```java
public class SideMenuBar extends JPanel {
    private static final int WIDTH = 220;
    private List<KitPage> pages;
    private int selectedIndex = 0;

    // Color scheme
    private static final int SELECTED_BG = 0x2D2D2D;
    private static final int SELECTED_TEXT = 0xBB86FC;
    private static final int HOVER_BG = 0xE8E8E8;
    private static final int DEFAULT_BG = 0xF3F3F3;
}
```

**Features**:
- Dynamic menu item generation
- Selected state highlighting
- Mouse hover effects
- Custom icons and tooltips
- Runtime page management

### GradientProgressBar

Custom progress bar with animation:

```java
public class GradientProgressBar extends JProgressBar {
    private Timer animationTimer;
    private float animatedValue = 0f;
    private float targetValue = 0f;

    public GradientProgressBar() {
        setOpaque(false);
        setStringPainted(false);

        // 60 FPS animation
        animationTimer = new Timer(16, e -> {
            if (Math.abs(animatedValue - targetValue) < 0.5f) {
                animatedValue = targetValue;
            } else {
                // Easing effect
                animatedValue += (targetValue - animatedValue) * 0.08f;
            }
            repaint();
        });
        animationTimer.start();
    }
}
```

**Features**:
- Blue to purple gradient
- Smooth easing animation (60 FPS)
- Rounded corners
- Glossy highlight effect
- Optional text display

## Background Processing

SwissKit uses SwingWorker for all time-consuming operations to avoid blocking the UI.

### SwingWorker Pattern

```java
public abstract class BaseWorker<T, V> extends SwingWorker<T, V> {
    protected final JProgressBar progressBar;
    protected final JButton button;
    protected final Component parentComponent;
    protected final Callback<T> callback;

    @Override
    protected T doInBackground() throws Exception {
        // Initialize UI in EDT
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            button.setEnabled(false);
        });

        // Background execution
        T result = executeTask();

        return result;
    }

    @Override
    protected void process(List<V> chunks) {
        // Update UI in EDT
        if (!chunks.isEmpty()) {
            V latest = chunks.get(chunks.size() - 1);
            progressBar.setValue((Integer) latest);
            progressBar.setString("Processing... " + latest + "%");
        }
    }

    @Override
    protected void done() {
        // Task completion in EDT
        try {
            get(); // Check for exceptions
            progressBar.setValue(100);
            progressBar.setString("Completed!");
            button.setEnabled(true);
            callback.onSuccess(get());
        } catch (Exception e) {
            progressBar.setString("Failed: " + e.getMessage());
            JOptionPane.showMessageDialog(parentComponent,
                    e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            button.setEnabled(true);
            callback.onFailure(e);
        }
    }

    protected abstract T executeTask() throws Exception;
}
```

**Key Benefits**:

- Non-blocking UI
- Real-time progress updates
- Proper error handling
- Thread-safe UI updates

## Excel Processing

SwissKit uses a hybrid approach for Excel processing:

### Technology Selection

| Library | Purpose | Advantages |
|---------|---------|------------|
| Apache POI | Metadata extraction | Mature, comprehensive Excel support |
| Apache FESOD | Streaming data read | High performance, low memory usage |

### Processing Flow

```java
// 1. Get sheet names using Apache POI
try (Workbook workbook = WorkbookFactory.create(file)) {
    for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
        sheetNames.add(workbook.getSheetName(i));
    }
}

// 2. Read data using Apache FESOD
try (ExcelReader excelReader = FesodSheet.read(file).build()) {
    for (String sheetName : sheetNames) {
        HeaderListener headerListener = new HeaderListener();
        ReadSheet readSheet = FesodSheet.readSheet(sheetName)
                .headRowNumber(1)
                .registerReadListener(headerListener)
                .build();
        excelReader.read(readSheet);
        result.put(sheetName, headerListener.getHeaders());
    }
}
```

### Listeners

**HeaderListener** - Extracts headers from first row:

```java
public class HeaderListener extends AnalysisEventListener<Map<Integer, String>> {
    private Map<Integer, String> headers = new HashMap<>();

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context) {
        if (!headRead) {
            this.headers = headMap;
            headRead = true;
        }
    }

    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
        // Stop after first row
        throw new ExcelAnalysisStopException("Stop after header");
    }
}
```

**NoModelDataListener** - Streams all data:

```java
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, Object>> {
    private List<Map<Integer, Object>> cachedDataList;

    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
        cachedDataList.add(data);
    }

    public void clear() {
        cachedDataList = new ArrayList<>();
    }
}
```

## Technology Stack

### Core Technologies

- **Language**: Java 11
- **Build Tool**: Maven 3.6+
- **UI Framework**: Swing
- **UI Theme**: FlatLaf 3.5

### Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| flatlaf | 3.5 | Modern UI theme |
| fesod-sheet | 2.0.1-incubating | Excel streaming |
| log4j-core | 2.25.3 | Logging |
| lombok | 1.18.42 | Code simplification |
| fastjson2 | 2.0.59 | JSON processing |

### Build Plugins

- **maven-shade-plugin** - Creates executable JAR
- **maven-compiler-plugin** - Compilation configuration

## Design Patterns

SwissKit employs several design patterns:

### Plugin Pattern

```java
// Interface
public interface KitPage {
    JPanel getPanel();
    String getTitle();
}

// Implementation
public class ExcelPage implements KitPage { }
```

### Observer Pattern

```java
// Callback interface
public interface Callback<T> {
    void onSuccess(T result);
    void onFailure(Exception e);
}

// Usage
worker.execute();
```

### Factory Pattern

```java
// Component factory
public class UIUtils {
    public static JButton createButton(String text) { }
    public static JLabel createTitle(String text) { }
}
```

---

**Ready to contribute?** Check out the [Development Guide](development.md) to start building!