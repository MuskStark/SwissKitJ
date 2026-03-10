# Architecture

SwissKit is built with a modular, plugin-based architecture that allows for easy extension and maintenance. This section covers the core architectural patterns and design decisions.

## Table of Contents

- [Overview](#overview)
- [Project Structure](#project-structure)
- [Plugin System](#plugin-system)
- [Database Layer](#database-layer)
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
│   │   └── SwissKitPage.java            # Page annotation
│   ├── api/                             # API interfaces
│   │   └── KitPage.java                 # Plugin interface
│   ├── plugin/                          # Plugin system
│   │   ├── PluginLoader.java            # Plugin JAR loader
│   │   └── PluginDiagnostic.java        # Plugin diagnostics
│   ├── scaner/                          # SPI-based scanner
│   │   └── SwissKitPageScaner.java      # Auto-discovery scanner
│   ├── database/                        # Database layer
│   │   ├── DatabaseInit.java            # Database initialization
│   │   ├── SwissKitDBTable.java         # Table marker interface
│   │   ├── entity/                      # Entity classes
│   │   │   ├── excel/
│   │   │   │   └── ComplexSplitConfigEntity.java
│   │   │   └── setting/email/
│   │   │       ├── SwissKitSettingEmailEntity.java
│   │   │       ├── EmailAddressBookEntity.java
│   │   │       └── EmailTagEntity.java
│   │   └── mapper/                      # MyBatis mappers
│   │       ├── excel/
│   │       │   └── ComplexSplitConfigMapper.java
│   │       └── setting/email/
│   │           ├── SwissKitSettingEmailMapper.java
│   │           ├── EmailAddressBookMapper.java
│   │           └── EmailTagMapper.java
│   ├── kitpage/                         # Tool page modules
│   │   ├── welcome/                     # Welcome page
│   │   │   ├── WelcomePage.java
│   │   │   └── WelcomePage.jfd
│   │   ├── email/                       # Email tool
│   │   │   ├── EmailKitPage.java
│   │   │   └── EmailKitPage.jfd
│   │   ├── excel/                       # Excel tool
│   │   │   ├── ExcelKitPage.java
│   │   │   ├── ExcelKitPage.jfd
│   │   │   ├── second/                  # Config views
│   │   │   │   ├── ConfigView.java
│   │   │   │   ├── ConfigView.jfd
│   │   │   │   ├── ConfigEditorView.java
│   │   │   │   └── ConfigEditorView.jfd
│   │   │   ├── listener/                # Event listeners
│   │   │   │   ├── HeaderListener.java
│   │   │   │   └── NoModelDataListener.java
│   │   │   └── worker/                  # Background workers
│   │   │       ├── ExcelAnalysisWorker.java
│   │   │       ├── ExcelAnalysisCallback.java
│   │   │       ├── ExcelSplitWorker.java
│   │   │       ├── SetComplexSplitConfigWorker.java
│   │   │       ├── ClearComplexSplitConfigWorker.java
│   │   │       └── ShowConfigViewWorker.java
│   │   └── setting/                     # Settings page
│   │       ├── SettingKitPage.java
│   │       ├── SettingKitPage.jfd
│   │       ├── second/                  # Sub-views
│   │       │   ├── AddAddressView.java
│   │       │   ├── AddAddressView.jfd
│   │       │   ├── EmailAddressBookView.java
│   │       │   ├── EmailAddressBookView.jfd
│   │       │   ├── EmailTagsView.java
│   │       │   └── EmailTagsView.jfd
│   │       └── worker/second/           # Query workers
│   │           ├── QueryAllEmailInfoCallBack.java
│   │           └── QueryAllEmailInfoWorker.java
│   ├── ui/                              # UI components
│   │   ├── StartLoadingPage.java        # Splash screen
│   │   ├── home/
│   │   │   └── HomePage.java            # Main window
│   │   ├── sidebar/
│   │   │   └── SideMenuBar.java         # Side menu
│   │   └── components/
│   │       ├── GradientProgressBar.java
│   │       └── FixedWidthComboBox.java
│   └── utils/
│       ├── AppInfo.java                 # Application version info
│       ├── UIUtils.java                 # UI utilities
│       ├── ExcelUtil.java               # Excel utilities
│       ├── FileNameUtil.java            # File name utilities
│       └── StringUtil.java              # String validation utilities
└── src/main/resources/
    ├── app.properties                   # Application properties
    ├── init.sql                         # Database initialization SQL
    ├── log4j2.xml                       # Log4j configuration
    ├── mybatis-config.xml               # MyBatis configuration
    ├── mapper/                          # MyBatis mapper XMLs
    └── META-INF/services/               # SPI service files
        └── fan.summer.api.KitPage
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

### SwissKitPageScanner

The `SwissKitPageScaner` class handles automatic discovery of all tool pages using SPI:

```java
public class SwissKitPageScaner {
    /**
     * Scans for KitPage implementations using SPI ServiceLoader.
     *
     * @return list of visible KitPage instances sorted by order
     */
    public static List<KitPage> scan() {
        // 1. Load services from META-INF/services/
        // 2. Filter by @SwissKitPage visible = true
        // 3. Sort by order() value
        // 4. Instantiate and return
    }
}
```

### Auto-Discovery Mechanism

SwissKit automatically discovers and loads all tools at runtime using SPI:

**Discovery Method**: Java SPI (Service Provider Interface)

**SPI Service File**: `src/main/resources/META-INF/services/fan.summer.api.KitPage`

**Current Registered Pages**:
```
fan.summer.kitpage.welcome.WelcomePage
fan.summer.kitpage.excel.ExcelKitPage
fan.summer.kitpage.email.EmailKitPage
fan.summer.kitpage.setting.SettingKitPage
```

### External Plugin Loading

SwissKit supports loading external JAR plugins:

**Plugin Directory**: `.swisskit/plugins/`

**Loading Process**:
1. `PluginLoader` scans plugin directory at startup
2. Loads JAR files using URLClassLoader
3. Scans for `KitPage` implementations
4. Registers discovered pages

## Database Layer

SwissKit uses H2 as embedded database with MyBatis for data access.

### Database Location

- **Path**: `.swisskit/swisskit.db` (relative to application runtime directory)
- **Type**: H2 (embedded, file-based, no external server required)

### Database Initialization

The `DatabaseInit` class handles:
- Creating `.swisskit` directory if not exists
- Creating database tables via `init.sql`
- Initializing MyBatis SqlSessionFactory

```java
// Initialize database
DatabaseInit.init();

// Get SqlSession for CRUD operations
try (SqlSession session = DatabaseInit.getSqlSession()) {
    MyMapper mapper = session.getMapper(MyMapper.class);
    // perform operations
    session.commit();
}
```

### Database Tables

| Table Name | Purpose |
|------------|---------|
| `swiss_kit_setting_email` | Email SMTP configuration |
| `complex_split_config` | Excel complex split configuration |
| `email_address_book` | Email contacts with nicknames and tags |
| `email_tag` | Tags for categorizing contacts |

### MyBatis Configuration

Database configuration is in `src/main/resources/mybatis-config.xml`:

```xml
<environment id="swisskit">
    <transactionManager type="JDBC"/>
    <dataSource type="UNPOOLED">
        <property name="driver" value="org.h2.Driver"/>
        <property name="url" value="${db.url}"/>
    </dataSource>
</environment>
```

### Entity Pattern

All entities use Lombok for boilerplate reduction:

```java
@Data
public class EmailAddressBookEntity {
    private Integer id;
    private String emailAddress;
    private String nickname;
    private String tags;  // JSON array of tag names
}
```

## UI Components

SwissKit includes custom UI components for a modern, consistent appearance.

### SideMenuBar

Dynamic side menu with the following architecture:

```java
public class SideMenuBar extends JPanel {
    private static final int MENU_WIDTH = 160;
    private List<KitPage> pages;
    private int selectedIndex = 0;

    // Color scheme
    private static final Color SELECTED_BG = new Color(0x2D, 0x2D, 0x2D);
    private static final Color SELECTED_TEXT = new Color(0xBB, 0x86, 0xFC);
    private static final Color HOVER_BG = new Color(0xE8, 0xE8, 0xE8);
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
        // 60 FPS animation
        animationTimer = new Timer(16, e -> {
            // Easing effect
            animatedValue += (targetValue - animatedValue) * 0.08f;
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

## Background Processing

SwissKit uses SwingWorker for all time-consuming operations to avoid blocking the UI.

### SwingWorker Pattern

```java
public class QueryAllEmailInfoWorker extends SwingWorker<List<EmailAddressBookEntity>, Void> {
    private QueryAllEmailInfoCallBack callback;

    @Override
    protected List<EmailAddressBookEntity> doInBackground() throws Exception {
        try (SqlSession session = DatabaseInit.getSqlSession()) {
            EmailAddressBookMapper mapper = session.getMapper(EmailAddressBookMapper.class);
            return mapper.selectEmailAddressBook();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    @Override
    protected void done() {
        try {
            callback.onSuccess(get());
        } catch (Exception e) {
            callback.onFailure(e);
        }
    }
}
```

**Key Benefits**:
- Non-blocking UI
- Real-time progress updates
- Proper error handling
- Thread-safe UI updates via `SwingUtilities.invokeLater()`

## Excel Processing

SwissKit uses Apache FESOD for efficient Excel processing.

### Processing Flow

```java
// 1. Get sheet names
try (ExcelReader excelReader = FesodSheet.read(file).build()) {
    for (String sheetName : sheetNames) {
        HeaderListener headerListener = new HeaderListener();
        ReadSheet readSheet = FesodSheet.readSheet(sheetName)
                .headRowNumber(1)
                .registerReadListener(headerListener)
                .build();
        excelReader.read(readSheet);
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
        this.headers = headMap;
    }
}
```

**NoModelDataListener** - Streams all data with logging:
```java
@Slf4j
public class NoModelDataListener extends AnalysisEventListener<Map<Integer, Object>> {
    private List<Map<Integer, Object>> cachedDataList;

    @Override
    public void invoke(Map<Integer, Object> data, AnalysisContext context) {
        log.info("Parsed one data row: {}", JSON.toJSONString(data));
        cachedDataList.add(data);
    }
}
```

## Technology Stack

### Core Technologies

- **Language**: Java 11
- **Build Tool**: Maven 3.6+
- **UI Framework**: Swing
- **UI Theme**: FlatLaf 3.5
- **Database**: H2 2.4.240 + MyBatis 3.5.19

### Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| flatlaf | 3.5 | Modern UI theme |
| fesod-sheet | 2.0.1-incubating | Excel streaming |
| log4j-core | 2.25.3 | Logging |
| log4j-slf4j2-impl | 2.25.3 | SLF4J binding |
| slf4j-api | 2.0.16 | Logging API |
| lombok | 1.18.42 | Code simplification |
| fastjson2 | 2.0.59 | JSON processing |
| h2 | 2.4.240 | H2 Database |
| mybatis | 3.5.19 | MyBatis ORM |
| miglayout-swing | 11.3 | Layout manager |

## Design Patterns

SwissKit employs several design patterns:

### Plugin Pattern
```java
public interface KitPage { }
public class ExcelPage implements KitPage { }
```

### Observer Pattern
```java
public interface QueryAllEmailInfoCallBack {
    void onSuccess(List<EmailAddressBookEntity> result);
    void onFailure(Exception e);
}
```

### Factory Pattern
```java
public class UIUtils {
    public static JPanel createSectionPanel(String title, JComponent content) { }
    public static JLabel createPageTitle(String title) { }
}
```

---

**Ready to contribute?** Check out the [Development Guide](development.md) to start building!
