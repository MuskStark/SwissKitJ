# API Reference

This section provides detailed API documentation for SwissKit's core interfaces and classes.

## Table of Contents

- [Module Overview](#module-overview)
- [KitPage Interface](#kitpage-interface)
- [@SwissKitPage Annotation](#swisskitpage-annotation)
- [SwissKitPageScanner](#swisskitpagescanner)
- [Database Layer](#database-layer)
- [UI Components](#plugin.swisskit.hpl.ui-components)
- [Worker Classes](#plugin.swisskit.hpl.worker-classes)
- [Utility Classes](#utility-classes)
- [Listeners](#listeners)
- [Entity Classes](#entity-classes)

## Module Overview

SwissKit uses a multi-module architecture. Key components are organized as follows:

| Module            | Package                                        | Description                |
|-------------------|------------------------------------------------|----------------------------|
| `SwissKitJ-Api`   | `fan.summer.api`                               | Core interfaces (KitPage)  |
| `SwissKitJ-Api`   | `fan.summer.annoattion`                        | Annotations (SwissKitPage) |
| `SwissKitJ-Api`   | `fan.summer.plugin.swisskit.hpl.ui.components` | Shared UI components       |
| `SwissKit` (main) | `fan.summer.database`                          | Database layer             |
| `SwissKit` (main) | `fan.summer.kitpage`                           | Tool implementations       |
| `SwissKit` (main) | `fan.summer.utils`                             | Utility classes            |

---

## KitPage Interface

The `KitPage` interface is the foundation for all tool pages in SwissKit. It is located in the `SwissKitJ-Api` module.

### Location

```
SwissKitJ-Api/src/main/java/fan/summer/api/KitPage.java
```

### Interface Definition

```java
package fan.summer.api;

import fan.summer.annoattion.SwissKitPage;

import javax.swing.*;

/**
 * Interface for SwissKit plugin pages.
 * All plugin pages must implement this interface and be annotated with {@link SwissKitPage}.
 */
public interface KitPage {
    /**
     * Returns the main panel for this page.
     * This panel will be displayed in the content area when the page is selected.
     *
     * @return the JPanel containing the page content
     */
    JPanel getPanel();

    /**
     * Returns the menu display name.
     * Default implementation reads from {@link SwissKitPage#menuName()} annotation.
     *
     * @return menu display name
     */
    default String getMenuName() {
        SwissKitPage annotation = getClass().getAnnotation(SwissKitPage.class);
        if (annotation != null && !annotation.menuName().isEmpty()) {
            return annotation.menuName();
        }
        return getClass().getSimpleName();
    }

    /**
     * Returns the menu icon.
     * Override this method to provide a custom icon for the menu item.
     *
     * @return menu icon, or null for no icon
     */
    default Icon getMenuIcon() {
        return null;
    }

    /**
     * Returns the menu tooltip text shown on hover.
     * Default implementation reads from {@link SwissKitPage#menuTooltip()} annotation.
     *
     * @return tooltip text, or null for no tooltip
     */
    default String getMenuTooltip() {
        SwissKitPage annotation = getClass().getAnnotation(SwissKitPage.class);
        if (annotation != null) {
            return annotation.menuTooltip();
        }
        return null;
    }
}
```

### Methods

| Method             | Return Type | Description                                              |
|--------------------|-------------|----------------------------------------------------------|
| `getPanel()`       | `JPanel`    | Returns the main panel containing all UI elements        |
| `getMenuName()`    | `String`    | Returns the menu display name (default: from annotation) |
| `getMenuIcon()`    | `Icon`      | Returns the menu icon (default: `null`)                  |
| `getMenuTooltip()` | `String`    | Returns the menu tooltip (default: from annotation)      |

### Example Implementation

```java
import fan.summer.api.KitPage;
import fan.summer.annoattion.SwissKitPage;

@SwissKitPage(
        menuName = "📋 Example",
        menuTooltip = "Open Example Tool",
        visible = true,
        order = 10
)
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
}
```

---

## @SwissKitPage Annotation

The `@SwissKitPage` annotation configures tool page menu properties. It is located in the `SwissKitJ-Api` module.

### Location

```
SwissKitJ-Api/src/main/java/fan/summer/annoattion/SwissKitPage.java
```

### Annotation Definition

```java
package fan.summer.annoattion;

import java.lang.annotation.*;

/**
 * Annotation to mark a class as a KitPage implementation.
 * Used for automatic page registration and menu configuration.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SwissKitPage {
    /**
     * Menu display name shown in the sidebar.
     */
    String menuName() default "";

    /**
     * Menu tooltip text shown on hover.
     */
    String menuTooltip() default "";

    /**
     * Whether this page is visible in the menu.
     */
    boolean visible() default true;

    /**
     * Menu item order for sorting.
     * Lower values appear first in the menu.
     */
    int order() default 0;
}
```

### Properties

| Property      | Type    | Default | Description                   |
|---------------|---------|---------|-------------------------------|
| `menuName`    | String  | ""      | Display name in sidebar       |
| `menuTooltip` | String  | ""      | Tooltip on hover              |
| `visible`     | boolean | true    | Whether to show in menu       |
| `order`       | int     | 0       | Display order (lower = first) |

---

## SwissKitPageScanner

The `SwissKitPageScaner` class automatically discovers and loads all `KitPage` implementations.

### Location

```
src/main/java/fan/summer/scaner/SwissKitPageScaner.java
```

```java
package fan.summer.scaner;

public class SwissKitPageScaner {
    /**
     * Scans for KitPage implementations using SPI ServiceLoader.
     *
     * @return list of visible KitPage instances sorted by order
     */
    public static List<KitPage> scan() {
        // Implementation
    }
}
```

### Key Features

- **SPI-based**: Uses Java ServiceLoader mechanism
- **Visibility Filter**: Only includes pages with `visible = true`
- **Order Sorting**: Sorts by `order()` value in annotation
- **Auto-Instantiation**: Creates page instances automatically

---

## Database Layer

### DatabaseInit

Handles database initialization and MyBatis configuration.

```java
package fan.summer.database;

public class DatabaseInit {
    private static SqlSessionFactory sqlSessionFactory;

    /**
     * Initializes database and MyBatis.
     * Creates tables from init.sql if not exist.
     */
    public static void init() { }

    /**
     * Returns a new SqlSession for database operations.
     */
    public static SqlSession getSqlSession() { }

    /**
     * Returns the SqlSessionFactory.
     */
    public static SqlSessionFactory getSqlSessionFactory() { }
}
```

### Entity Classes

#### EmailAddressBookEntity

```java
@Data
public class EmailAddressBookEntity {
    private Integer id;
    private String emailAddress;
    private String nickname;
    private String tags;  // JSON array
}
```

#### EmailTagEntity

```java
@Data
public class EmailTagEntity {
    private Long id;
    private String tag;
}
```

#### EmailSentLogEntity

```java
@Data
public class EmailSentLogEntity {
    private Long id;
    private String to;
    private String cc;
    private String bcc;
    private String subject;
    private String content;
    private String attachment;
    private Date sendTime;
    private boolean isSuccess;
}
```

#### ComplexSplitConfigEntity

```java
@Data
public class ComplexSplitConfigEntity {
    private Long id;
    private String taskId;
    private String fieldName;
    private String sheetName;
    private Integer headerIndex;
    private Integer columnIndex;
}
```

### Mapper Interfaces

#### EmailAddressBookMapper

```java
public interface EmailAddressBookMapper {
    void insert(EmailAddressBookEntity entity);
    List<EmailAddressBookEntity> selectEmailAddressBook();
}
```

#### EmailTagMapper

```java
public interface EmailTagMapper {
    void insert(EmailTagEntity entity);
    void update(EmailTagEntity entity);
    List<EmailTagEntity> selectAll();
}
```

#### EmailSentLogMapper

```java
public interface EmailSentLogMapper {
    void insert(EmailSentLogEntity entity);
    List<EmailSentLogEntity> selectAll();
}
```

---

## UI Components

UI components are located in the `SwissKitJ-Api` module for shared use across the main application and plugins.

### GradientProgressBar

A progress bar with gradient colors and smooth animation.

**Location**: `SwissKitJ-Api/src/main/java/fan/summer/plugin.swisskit.hpl.ui/components/GradientProgressBar.java`

#### Constructor

```java
public GradientProgressBar()
```

#### Methods

| Method                              | Return Type | Description                                 |
|-------------------------------------|-------------|---------------------------------------------|
| `setValue(int value)`               | `void`      | Sets the current value (triggers animation) |
| `setStringPainted(boolean painted)` | `void`      | Enables/disables text display               |
| `setString(String text)`            | `void`      | Sets the text to display                    |

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

**Location**: `SwissKitJ-Api/src/main/java/fan/summer/plugin.swisskit.hpl.ui/components/FixedWidthComboBox.java`

#### Constructor

```java
public FixedWidthComboBox(int width)
```

### SideMenuBar

Dynamic side menu component.

**Location**: `src/main/java/fan/summer/plugin.swisskit.hpl.ui/sidebar/SideMenuBar.java`

#### Constructor

```java
public SideMenuBar(List<KitPage> pages, JPanel contentPanel)
```

#### Methods

| Method                             | Return Type | Description                   |
|------------------------------------|-------------|-------------------------------|
| `addPage(KitPage page)`            | `void`      | Adds a page to the menu       |
| `removePage(int index)`            | `void`      | Removes a page from the menu  |
| `selectPage(int index)`            | `void`      | Sets the selected page        |
| `rebuildMenu()`                    | `void`      | Rebuilds menu from pages list |
| `setPages(List<KitPage> newPages)` | `void`      | Sets new page list            |

### TableUtil

Utility class for consistent JTable initialization.

**Location**: `src/main/java/fan/summer/utils/plugin.swisskit.hpl.ui/TableUtil.java`

```java
public abstract class TableUtil {
    /**
     * Initializes a JTable with specified columns and data.
     *
     * @param table the JTable to initialize
     * @param columns column names
     * @param rowData list of row data arrays
     * @param isCellEditableIndex columns after this index are editable
     * @return the initialized JTable
     */
    public static JTable initTable(JTable table, String[] columns, 
                                    List<Object[]> rowData, int isCellEditableIndex) {
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column > isCellEditableIndex;
            }
        };
        for (Object[] row : rowData) {
            model.addRow(row);
        }
        table.setModel(model);
        return table;
    }
}
```

---

## Worker Classes

### QueryAllEmailInfoWorker

Background plugin.swisskit.hpl.worker for loading email contacts.

#### Constructor

```java
public QueryAllEmailInfoWorker(QueryAllEmailInfoCallBack callBack)
```

#### Example Usage

```java
new QueryAllEmailInfoWorker(new QueryAllEmailInfoCallBack() {
    @Override
    public void onSuccess(List<EmailAddressBookEntity> result) {
        // Handle success
    }

    @Override
    public void onFailure(Exception e) {
        // Handle error
    }
}).execute();
```

### ExcelAnalysisWorker

Background plugin.swisskit.hpl.worker for analyzing Excel files.

#### Constructor

```java
public ExcelAnalysisWorker(
    Path filePath,
    JProgressBar progressBar,
    JButton startBtn,
    ExcelAnalysisCallback callback
)
```

### ExcelSplitWorker

Background plugin.swisskit.hpl.worker for splitting Excel files.

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

### EmailSentWorker

Background plugin.swisskit.hpl.worker for sending emails.

#### Constructor

```java
public EmailSentWorker(
    String subject,
    String body,
    String taskId,
    boolean isMassSent,
    JProgressBar progressBar
)
```

---

## Utility Classes

### AppInfo

Application version and name constants.

**Location**: `src/main/java/fan/summer/utils/AppInfo.java`

```java
public abstract class AppInfo {
    public static final String VERSION = resolveVersion();
    public static final String NAME = loadProperty("app.name", "SwissKit");

    public static String getVersion();
    public static String getName();
    public static String getFullName(); // NAME + "-" + VERSION
}
```

### EmailUtil

Email sending utility using Simple Java Mail library. Automatically loads SMTP configuration from database.

**Location**: `src/main/java/fan/summer/utils/EmailUtil.java`

```java
public class EmailUtil {
    /**
     * Sends a plain text email.
     */
    public static void sendText(String to, String subject, String body);

    /**
     * Tests SMTP connection using current configuration.
     */
    public static boolean testConnection();

    /**
     * Sends an email with full configuration.
     */
    public static void sendEmail(EmailMessage message);
}
```

**Example Usage**:

```java
// Plain text email
EmailUtil.sendText("to@example.com", "Subject", "Hello!");

// HTML email with attachments
EmailUtil.sendEmail(
    EmailMessage.builder()
        .to("a@example.com", "b@example.com")
        .cc("c@example.com")
        .subject("Monthly Report")
        .htmlBody("<h1>Report</h1>")
        .attachments(new File("report.pdf"))
        .build()
);

// Test connection
if (EmailUtil.testConnection()) {
    System.out.println("SMTP connection successful!");
}
```

### StringUtil

String validation utilities.

**Location**: `src/main/java/fan/summer/utils/StringUtil.java`

```java
public class StringUtil {
    /**
     * Validates email address format.
     */
    public static boolean checkEmail(String email);
}
```

### UIUtils

Common UI component creation methods.

**Location**: `src/main/java/fan/summer/utils/UIUtils.java`

```java
public class UIUtils {
    // Color constants
    public static final Color PRIMARY_COLOR = new Color(0xBB, 0x86, 0xFC);
    public static final Color TEXT_COLOR = new Color(0x60, 0x60, 0x60);
    public static final Color LIGHT_GRAY = new Color(0xF3, 0xF3, 0xF3);

    // Factory methods
    public static JPanel createSectionPanel(String title, JComponent content);
    public static JLabel createPageTitle(String title);
    public static JProgressBar createProgressBar();
    public static JPanel createCenterButtonPanel(JButton... buttons);
}
```

---

## Listeners

### HeaderListener

Listener for extracting Excel headers.

**Location**: `src/main/java/fan/summer/kitpage/excel/listener/HeaderListener.java`

```java
public class HeaderListener extends AnalysisEventListener<Map<Integer, String>> {
    private Map<Integer, String> headers = new HashMap<>();

    @Override
    public void invokeHeadMap(Map<Integer, String> headMap, AnalysisContext context);

    @Override
    public void invoke(Map<Integer, String> data, AnalysisContext context);

    public Map<Integer, String> getHeaders();
    public void clear();
}
```

### NoModelDataListener

Listener for streaming Excel data.

**Location**: `src/main/java/fan/summer/kitpage/excel/listener/NoModelDataListener.java`

```java
@Slf4j
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, Object>> {
    private List<Map<Integer, Object>> cachedDataList;

    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context);

    @Override
    public void doAfterAllAnalysed(AnalysisContext context);

    public List<Map<Integer, Object>> getCachedDataList();
    public void clear();
}
```

---

## Callback Interfaces

### QueryAllEmailInfoCallBack

Callback interface for email info queries.

**Location**:
`src/main/java/fan/summer/kitpage/setting/plugin.swisskit.hpl.worker/second/QueryAllEmailInfoCallBack.java`

```java
public interface QueryAllEmailInfoCallBack {
    void onSuccess(List<EmailAddressBookEntity> emailAddressBookEntities);
    void onFailure(Exception e);
}
```

### ExcelAnalysisCallback

Callback interface for Excel analysis results.

**Location**: `src/main/java/fan/summer/kitpage/excel/plugin.swisskit.hpl.worker/ExcelAnalysisCallback.java`

```java
public interface ExcelAnalysisCallback {
    void onSuccess(Map<String, Map<Integer, String>> result);
    void onFailure(Exception e);
}
```

---

**Need more help?** Check out the [Development Guide](development.md) for implementation examples!