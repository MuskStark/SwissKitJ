# API Reference

This section provides detailed API documentation for SwissKit's core interfaces and classes.

## Table of Contents

- [KitPage Interface](#kitpage-interface)
- [@SwissKitPage Annotation](#swisskitpage-annotation)
- [SwissKitPageScanner](#swisskitpagescanner)
- [Database Layer](#database-layer)
- [UI Components](#ui-components)
- [Worker Classes](#worker-classes)
- [Utility Classes](#utility-classes)
- [Listeners](#listeners)
- [Entity Classes](#entity-classes)

## KitPage Interface

The `KitPage` interface is the foundation for all tool pages in SwissKit.

### Interface Definition

```java
package fan.summer.api;

import javax.swing.*;

public interface KitPage {
    /**
     * Returns the main panel of this page.
     *
     * @return the JPanel containing all UI elements
     */
    JPanel getPanel();

    /**
     * Returns the display name for the menu.
     * Defaults to @SwissKitPage annotation value.
     *
     * @return the menu display name
     */
    default String getMenuName() {
        return "";
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
     * Defaults to @SwissKitPage annotation value.
     *
     * @return the tooltip text, or null if no tooltip
     */
    default String getMenuTooltip() {
        return "";
    }
}
```

### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getPanel()` | `JPanel` | Returns the main panel containing all UI elements |
| `getMenuName()` | `String` | Returns the menu display name (default: from annotation) |
| `getMenuIcon()` | `Icon` | Returns the menu icon (default: `null`) |
| `getMenuTooltip()` | `String` | Returns the menu tooltip (default: from annotation) |

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

The `@SwissKitPage` annotation configures tool page menu properties.

### Annotation Definition

```java
package fan.summer.annoattion;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SwissKitPage {
    String menuName() default "";
    String menuTooltip() default "";
    boolean visible() default true;
    int order() default 0;
}
```

### Properties

| Property | Type | Default | Description |
|----------|------|---------|-------------|
| `menuName` | String | "" | Display name in sidebar |
| `menuTooltip` | String | "" | Tooltip on hover |
| `visible` | boolean | true | Whether to show in menu |
| `order` | int | 0 | Display order (lower = first) |

---

## SwissKitPageScanner

The `SwissKitPageScaner` class automatically discovers and loads all `KitPage` implementations.

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

---

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

### SideMenuBar

Dynamic side menu component.

#### Constructor

```java
public SideMenuBar(List<KitPage> pages, JPanel contentPanel)
```

#### Methods

| Method | Return Type | Description |
|--------|-------------|-------------|
| `addPage(KitPage page)` | `void` | Adds a page to the menu |
| `removePage(int index)` | `void` | Removes a page from the menu |
| `selectPage(int index)` | `void` | Sets the selected page |
| `rebuildMenu()` | `void` | Rebuilds menu from pages list |
| `setPages(List<KitPage> newPages)` | `void` | Sets new page list |

---

## Worker Classes

### QueryAllEmailInfoWorker

Background worker for loading email contacts.

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

---

## Utility Classes

### AppInfo

Application version and name constants.

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

```java
public class EmailUtil {
    /**
     * Sends a plain text email.
     *
     * @param to      recipient email address
     * @param subject email subject
     * @param body    email body content
     */
    public static void sendText(String to, String subject, String body);

    /**
     * Tests SMTP connection using current configuration.
     *
     * @return true if connection successful
     */
    public static boolean testConnection();

    /**
     * Sends an email with full configuration.
     *
     * @param message EmailMessage builder
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

```java
public class StringUtil {
    /**
     * Validates email address format.
     *
     * @param email the email address to validate
     * @return true if valid email format
     */
    public static boolean checkEmail(String email);
}
```

### UIUtils

Common UI component creation methods.

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

```java
public interface QueryAllEmailInfoCallBack {
    void onSuccess(List<EmailAddressBookEntity> emailAddressBookEntities);
    void onFailure(Exception e);
}
```

### ExcelAnalysisCallback

Callback interface for Excel analysis results.

```java
public interface ExcelAnalysisCallback {
    void onSuccess(Map<String, Map<Integer, String>> result);
    void onFailure(Exception e);
}
```

---

**Need more help?** Check out the [Development Guide](development.md) for implementation examples!
