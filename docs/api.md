# API Reference

This section provides detailed API documentation for SwissKit's core interfaces and classes.

## Table of Contents

- [KitPage Interface](#kitpage-interface)
- [UI Components](#ui-components)
- [Worker Classes](#worker-classes)
- [Utility Classes](#utility-classes)
- [Listeners](#listeners)

## KitPage Interface

The `KitPage` interface is the foundation for all tool pages in SwissKit.

### Interface Definition

```java
package fan.summer.kitpage;

import javax.swing.*;

public interface KitPage {
    /**
     * Returns the main panel of this page.
     *
     * @return the JPanel containing all UI elements
     */
    JPanel getPanel();

    /**
     * Returns the title of this page.
     *
     * @return the title string
     */
    String getTitle();

    /**
     * Returns the display name for the menu.
     * Defaults to getTitle() if not overridden.
     *
     * @return the menu display name
     */
    default String getMenuName() {
        return getTitle();
    }

    /**
     * Returns the icon for the menu item.
     *
     * @return the Icon, or null if no icon
     */
    default Icon getMenuIcon() {
        return null;
    }

    /**
     * Returns the tooltip text for the menu item.
     *
     * @return the tooltip text, or null if no tooltip
     */
    default String getMenuTooltip() {
        return null;
    }
}
```

### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPanel()` | `JPanel` | Returns the main panel containing all UI elements |
| `getTitle()` | `String` | Returns the page title |
| `getMenuName()` | `String` | Returns the menu display name (default: `getTitle()`) |
| `getMenuIcon()` | `Icon` | Returns the menu icon (default: `null`) |
| `getMenuTooltip()` | `String` | Returns the menu tooltip (default: `null`) |

### Example Implementation

```java
public class ExamplePage implements KitPage {
    private JPanel panel;

    public ExamplePage() {
        panel = new JPanel();
        panel.add(new JLabel("Example Page"));
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public String getTitle() {
        return "Example";
    }

    @Override
    public String getMenuName() {
        return "📋 Example";
    }

    @Override
    public String getMenuTooltip() {
        return "Open Example Tool";
    }
}
```

## UI Components

### GradientProgressBar

A progress bar with gradient colors and smooth animation.

#### Constructor

```java
public GradientProgressBar()
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `setValue(int value)` | `void` | Sets the current value (triggers animation) |
| `setStringPainted(boolean painted)` | `void` | Enables/disables text display |
| `setString(String text)` | `void` | Sets the text to display |

#### Example Usage

```java
GradientProgressBar progressBar = new GradientProgressBar();
progressBar.setMinimum(0);
progressBar.setMaximum(100);
progressBar.setStringPainted(true);
progressBar.setValue(50);
progressBar.setString("Processing... 50%");
```

### FixedWidthComboBox

A combo box with fixed width.

#### Constructor

```java
public FixedWidthComboBox(int width)
```

#### Parameters

- `width` - The fixed width in pixels

#### Example Usage

```java
FixedWidthComboBox comboBox = new FixedWidthComboBox(200);
comboBox.addItem("Option 1");
comboBox.addItem("Option 2");
```

## Worker Classes

### ExcelAnalysisWorker

Background worker for analyzing Excel files.

#### Constructor

```java
public ExcelAnalysisWorker(
    Path filePath,
    JProgressBar progressBar,
    JButton startBtn,
    ExcelAnalysisCallback callback
)
```

#### Parameters

- `filePath` - Path to the Excel file to analyze
- `progressBar` - Progress bar for updates
- `startBtn` - Button to disable during processing
- `callback` - Callback for results

#### Return Type

`Map<String, Map<Integer, String>>` - Map of sheet names to headers

#### Example Usage

```java
ExcelAnalysisWorker worker = new ExcelAnalysisWorker(
    filePath,
    progressBar,
    startButton,
    new ExcelAnalysisCallback() {
        @Override
        public void onSuccess(Map<String, Map<Integer, String>> result) {
            System.out.println("Analysis completed");
        }

        @Override
        public void onFailure(Exception e) {
            System.err.println("Analysis failed: " + e.getMessage());
        }
    }
);
worker.execute();
```

### ExcelSplitWorker

Background worker for splitting Excel files.

#### Constructor

```java
public ExcelSplitWorker(
    Path outputPath,
    Path orgFilePath,
    JProgressBar progressBar,
    JButton button,
    Component parentComponent
)
```

#### Parameters

- `outputPath` - Directory to save split files
- `orgFilePath` - Original Excel file path
- `progressBar` - Progress bar for updates
- `button` - Button to disable during processing
- `parentComponent` - Parent component for dialogs

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `setSplitSheetModel(Set<String> sheetNames)` | `ExcelSplitWorker` | Configure to split by sheet names |
| `setExcelFileAnalysisResultMap(Map<String, Map<Integer, String>> map)` | `ExcelSplitWorker` | Set analysis results |

#### Example Usage

```java
ExcelSplitWorker worker = new ExcelSplitWorker(
    outputPath,
    filePath,
    progressBar,
    splitButton,
    panel
);
worker.setSplitSheetModel(sheets.keySet())
       .setExcelFileAnalysisResultMap(analysisResult)
       .execute();
```

## Utility Classes

### UIUtils

Provides common UI component creation methods.

#### Static Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `createTitleLabel(String text)` | `JLabel` | Creates a title label |
| `createProgressBar()` | `JProgressBar` | Creates a progress bar |
| `showFileChooser(Component parent)` | `File` | Shows file chooser dialog |

#### Example Usage

```java
JLabel title = UIUtils.createTitleLabel("My Tool");
JProgressBar progress = UIUtils.createProgressBar();
```

### SideMenuBar

Dynamic side menu component.

#### Constructor

```java
public SideMenuBar(List<KitPage> pages)
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `addPage(KitPage page)` | `void` | Adds a page to the menu |
| `removePage(KitPage page)` | `void` | Removes a page from the menu |
| `setSelectedIndex(int index)` | `void` | Sets the selected page |

## Listeners

### HeaderListener

Listener for extracting Excel headers.

#### Constructor

```java
public HeaderListener()
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getHeaders()` | `Map<Integer, String>` | Returns the extracted headers |
| `clear()` | `void` | Clears the headers |

#### Example Usage

```java
HeaderListener listener = new HeaderListener();
ReadSheet readSheet = FesodSheet.readSheet(sheetName)
        .registerReadListener(listener)
        .build();
excelReader.read(readSheet);
Map<Integer, String> headers = listener.getHeaders();
```

### NoModelDataListener

Listener for streaming Excel data.

#### Constructor

```java
public NoModelDataListener()
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getCachedDataList()` | `List<Map<Integer, Object>>` | Returns cached data |
| `clear()` | `void` | Clears cached data |

#### Example Usage

```java
NoModelDataListener listener = new NoModelDataListener();
ReadSheet readSheet = FesodSheet.readSheet(sheetName)
        .registerReadListener(listener)
        .build();
excelReader.read(readSheet);
List<Map<Integer, Object>> data = listener.getCachedDataList();
```

## Callback Interfaces

### ExcelAnalysisCallback

Callback interface for Excel analysis results.

#### Interface Definition

```java
public interface ExcelAnalysisCallback {
    void onSuccess(Map<String, Map<Integer, String>> result);
    void onFailure(Exception e);
}
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `onSuccess(Map<String, Map<Integer, String>> result)` | `void` | Called on successful analysis |
| `onFailure(Exception e)` | `void` | Called on analysis failure |

---

**Need more help?** Check out the [Development Guide](development.md) for implementation examples!