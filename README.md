# SwissKit

![SwissKit](https://img.shields.io/badge/SwissKit-Desktop%20Toolbox-blue) ![Java](https://img.shields.io/badge/Java-11-orange) ![License](https://img.shields.io/badge/License-MIT-green) ![Maven](https://img.shields.io/badge/Maven-3.6+-red)

**SwissKit** is a *modular desktop toolbox* built with Java Swing. It provides a clean, extensible platform for various utility tools including Excel file processing, email sending, and more. The application uses a plugin-based architecture with automatic page discovery, making it easy to add new functionality.

If you want a lightweight, fast, and customizable desktop utility suite, this is it.

---

## Quick Start

**Requirements:**
- **JDK 11 or higher**
- **Maven 3.6 or higher**

### Installation

```bash
# Clone the repository
git clone https://github.com/MuskStark/SwissKitJ.git
cd SwissKit

# Build the project
mvn clean package

# Run the application (executable JAR)
java -jar target/SwissKit-1.0-SNAPSHOT.jar
```

Or using Maven exec plugin:

```bash
mvn exec:java -Dexec.mainClass="fan.summer.Main"
```

### Development Mode

```bash
# Compile
mvn clean compile

# Run
mvn exec:java -Dexec.mainClass="fan.summer.Main"
```

---

## Features

- **📦 Modular Architecture** - Plugin-based design with automatic page discovery
- **🎨 Modern UI** - Built with Swing and FlatLaf theme framework
- **⚡ High Performance** - Uses Apache FESOD for efficient Excel processing
- **🔄 Async Processing** - Background tasks with SwingWorker for non-blocking UI
- **🌐 Multi-format Support** - Excel, email, and extensible for other formats
- **🛠️ Easy Extension** - Add new tools by implementing the `KitPage` interface
- **📊 Custom UI Components** - Gradient progress bar, fixed-width combo box

### Current Tools

#### 📊 Excel Tool
- **File Analysis** - Read Excel file structure and extract headers
- **File Splitting** - Split Excel files by sheets
- **Progress Tracking** - Real-time progress updates with percentage display
- **Multi-Sheet Support** - Handle multiple sheets in a single file
- **Streaming Data Processing** - Efficient memory usage with Apache FESOD
- **Warning Dialogs** - User-friendly error messages

#### 📧 Email Tool
- **Email Composition** - Compose emails with subject and body
- **Recipient Management** - Add multiple recipients
- **Sending Functionality** - Send emails (in development)

#### 🏠 Welcome Page
- Application overview and guidance
- Quick access to all tools

---

## Architecture

```
SwissKit/
├── Main.java                        # Application entry point
├── kitpage/                         # Tool page modules
│   ├── KitPage.java                # Page interface definition
│   ├── WelcomePage.java            # Welcome page
│   ├── email/                      # Email tool
│   │   ├── EmailKitPage.java
│   │   └── EmailKitPage.form
│   └── excel/                      # Excel tool
│       ├── ExcelKitPage.java
│       ├── ExcelKitPage.form
│       ├── listener/               # Event listeners
│       │   ├── HeaderListener.java     # Header extraction
│       │   └── NoModelDataListener.java # Data streaming
│       └── worker/                 # Background workers
│           ├── ExcelAnalysisWorker.java   # File analysis
│           ├── ExcelAnalysisCallback.java # Analysis callback
│           └── ExcelSplitWorker.java      # File splitting
├── ui/                             # Custom UI components
│   └── components/
│       ├── GradientProgressBar.java  # Animated progress bar
│       └── FixedWidthComboBox.java    # Fixed-width combo box
└── utils/                          # Utility classes
    ├── SideMenuBar.java           # Side menu component
    └── UIUtils.java               # UI utilities
```

### Plugin System

SwissKit uses an automatic discovery mechanism for tool pages:

1. **Interface**: Implement the `KitPage` interface
2. **Methods**: Provide `getPanel()` and `getTitle()` methods
3. **Auto-discovery**: Pages are automatically discovered at runtime from file system or JAR
4. **No Registration**: No manual registration required
5. **Sorting**: Pages are displayed alphabetically by class name

Example:

```java
package fan.summer.kitpage.mytool;

import fan.summer.kitpage.KitPage;
import javax.swing.*;

public class MyToolPage implements KitPage {
    private JPanel panel;

    public MyToolPage() {
        initComponents();
    }

    private void initComponents() {
        panel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("My Tool");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);
    }

    @Override
    public JPanel getPanel() {
        return panel;
    }

    @Override
    public String getTitle() {
        return "My Tool";
    }

    @Override
    public String getMenuName() {
        return "🔧 My Tool";
    }

    @Override
    public String getMenuTooltip() {
        return "Open My Tool";
    }
}
```

### Tech Stack

- **Language**: Java 11
- **Build Tool**: Maven
- **UI Framework**: Swing
- **UI Theme**: FlatLaf 3.5 (FlatIntelliJLaf)
- **Excel Processing**:
  - Apache FESOD 2.0.1-incubating (streaming read)
  - Apache POI (metadata extraction)
- **UI Components**: SwingX 1.6.5
- **Logging**: Log4j 2.25.3 + SLF4J 2.20.0
- **JSON Processing**: FastJSON2 2.0.59
- **Code Simplification**: Lombok 1.18.42

---

## Configuration

### Application Window

Default window settings:
- **Minimum Size**: 800x500 pixels
- **Default Size**: 900x600 pixels
- **Theme**: FlatIntelliJLaf

### Excel Tool Configuration

The Excel tool supports splitting modes:
- **Split by Sheet** (✅ Implemented): Separate each sheet into individual files
- **Split by Column** (🚧 In Development): Split data based on column values

### Adding a Custom Icon

Place an icon file in `src/main/resources/`:
- `icon.png` (preferred)
- `icon.jpg`
- `app.png`

If not found, a log message will be displayed.

---

## Development

### Project Structure

- **Main.java** - Application entry point and main window initialization
- **KitPage.java** - Interface for tool pages
- **SideMenuBar.java** - Dynamic side menu component
- **UIUtils.java** - Common UI utilities and constants

### Adding a New Tool

1. Create a new package under `fan.summer.kitpage` (e.g., `pdf/`, `image/`)
2. Create a class implementing `KitPage`
3. Implement required methods (`getPanel()`, `getTitle()`)
4. Optionally override `getMenuName()`, `getMenuIcon()`, `getMenuTooltip()`
5. Add UI components and functionality
6. Build and run - the page will be automatically discovered

### Code Style

- Use camelCase for method and variable names
- Use PascalCase for class names
- Add Javadoc comments for all public methods
- Follow existing code conventions
- All UI text and comments should be in English
- Use `SansSerif` font for UI components

### Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn test jacoco:report
```

### Building

```bash
# Clean build with executable JAR
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Run executable JAR
java -jar target/SwissKit-1.0-SNAPSHOT.jar
```

---

## Background Task Pattern

SwissKit uses SwingWorker for background processing:

```java
// 1. Define callback interface
public interface TaskCallback {
    void onSuccess(Result result);
    void onFailure(Exception e);
}

// 2. Create SwingWorker
public class MyWorker extends SwingWorker<Result, Integer> {
    private final JProgressBar progressBar;
    private final JButton button;
    private final Component parentComponent;
    private final TaskCallback callback;

    public MyWorker(JProgressBar progressBar, JButton button,
                    Component parentComponent, TaskCallback callback) {
        this.progressBar = progressBar;
        this.button = button;
        this.parentComponent = parentComponent;
        this.callback = callback;
    }

    @Override
    protected Result doInBackground() throws Exception {
        // Initialize UI in EDT thread
        SwingUtilities.invokeLater(() -> {
            progressBar.setValue(0);
            progressBar.setStringPainted(true);
            button.setEnabled(false);
        });

        // Background thread execution
        Result result = doWork();

        // Publish progress
        publish(progress);

        return result;
    }

    @Override
    protected void process(List<Integer> chunks) {
        // EDT thread: Update UI
        if (!chunks.isEmpty()) {
            int latestProgress = chunks.get(chunks.size() - 1);
            progressBar.setValue(latestProgress);
            progressBar.setString("Processing... " + latestProgress + "%");
        }
    }

    @Override
    protected void done() {
        // EDT thread: Task completion
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
}
```

---

## Dependencies

### Core Dependencies

| Dependency | Version | Purpose |
|------------|---------|---------|
| flatlaf | 3.5 | FlatLaf theme core |
| flatlaf-extras | 3.5 | FlatLaf extras |
| flatlaf-intellij-themes | 3.5 | IntelliJ theme |
| swingx-all | 1.6.5 | Swing extensions |
| fesod-sheet | 2.0.1-incubating | Excel streaming |
| log4j-core | 2.25.3 | Log4j implementation |
| log4j-slf4j-impl | 2.20.0 | SLF4J binding |
| lombok | 1.18.42 | Code simplification |
| fastjson2 | 2.0.59 | JSON processing |

### Build Plugins

| Plugin | Version | Purpose |
|--------|---------|---------|
| maven-shade-plugin | 3.5.3 | Package all dependencies into executable JAR |
| maven-compiler-plugin | 3.13.0 | Compilation configuration |

### Adding Dependencies

Edit `pom.xml`:

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>library-name</artifactId>
    <version>1.0.0</version>
</dependency>
```

---

## UI Components

### SideMenuBar

Dynamic menu component with:
- 220px width
- Selected state highlighting
- Mouse hover effects
- Custom icons and tooltips
- Runtime page management
- Color scheme: Dark gray selected, purple text, light gray hover

### GradientProgressBar

Custom progress bar with:
- Blue to purple gradient effect
- Smooth animation with easing
- Rounded corners
- Glossy highlight effect
- Optional text display
- 60 FPS animation timer

### FixedWidthComboBox

Fixed-width combo box component for consistent UI layout.

### UIUtils

Common UI utilities:
- Color constants
- File/folder picker panels
- Page title creation
- Progress bars
- Layout helpers

---

## Troubleshooting

### Common Issues

**Issue**: Application won't start
- **Solution**: Ensure JDK 11+ is installed and JAVA_HOME is set

**Issue**: Excel files not reading
- **Solution**: Check file permissions and format compatibility (.xlsx, .xls)

**Issue**: UI not rendering correctly
- **Solution**: Update FlatLaf dependencies and rebuild

**Issue**: Progress bar not showing text
- **Solution**: Ensure `setStringPainted(true)` is called in EDT thread

**Issue**: Button not re-enabled after task completion
- **Solution**: Check `done()` method and ensure proper exception handling

### Debug Mode

Enable verbose logging (requires log4j2.xml):

```bash
java -Dlog4j.configurationFile=path/to/log4j2.xml -jar target/SwissKit-1.0-SNAPSHOT.jar
```

---

## Roadmap

- [x] Excel file analysis functionality
- [x] Excel file split by sheet functionality
- [x] Progress bar with percentage display
- [x] Gradient progress bar component
- [x] Warning dialogs for user feedback
- [ ] Excel file split by column functionality
- [ ] Implement email sending with SMTP support
- [ ] Add PDF processing tool
- [ ] Add image processing tool
- [ ] Create log4j2.xml configuration
- [ ] Add application configuration system
- [ ] Support theme switching
- [ ] Add unit tests
- [ ] Create distribution scripts
- [ ] Add multi-language support

---

## Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m ':sparkles: Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Commit Message Format

Follow the conventional commits format with emojis:

- `:sparkles:` - New feature
- `:art:` - Improve structure/format
- `:memo:` - Documentation
- `:bug:` - Bug fix
- `:arrow_up:` - Upgrade dependency
- `:recycle:` - Refactor code
- `:white_check_mark:` - Add/update tests
- `:wrench:` - Configuration files
- `:arrow_down:` - Downgrade dependency

### Code Review Guidelines

- Ensure all code is in English (comments, UI text, etc.)
- Follow existing code style and conventions
- Add appropriate logging statements
- Handle exceptions gracefully
- Test your changes thoroughly

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## Authors

- **Summer** - Core development
- **PhoebeJ** - Excel functionality

---

## Acknowledgments

- FlatLaf theme framework for modern UI
- Apache POI and Apache FESOD for Excel processing
- The open-source Java community
- IntelliJ IDEA for excellent IDE support

---

## Documentation

For detailed technical documentation, see [AGENTS.md](AGENTS.md).

---

**Built with ❤️ using Java and Swing**

---

## Changelog

### Version 1.0-SNAPSHOT

#### Recent Changes
- ✨ Add Excel split functionality with progress tracking
- ✨ Add NoModelDataListener for streaming Excel data reading
- ✨ Update HeaderListener to return Map<Integer, String>
- ✨ Add progress bar updates during split operations
- 🌐 Replace all Chinese text with English translations
- 📦 Add Lombok and FastJSON2 dependencies
- 🔧 Add Maven Shade plugin for building executable JAR
- 📦 Add JAR file scanning support in Main class
- ⬇️ Update Java version from 17 to 11
- ⚠️ Add warning dialog when split mode is not selected
- 📝 Improve logging throughout the application

#### Previous Features
- ✨ Excel file analysis with header extraction
- ✨ Gradient progress bar component with smooth animation
- ✨ Fixed-width combo box component
- ✨ Modular plugin architecture with auto-discovery
- ✨ Side menu bar with dynamic page management
- ✨ FlatLaf theme integration
- 📧 Email tool UI (in development)