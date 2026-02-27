# SwissKit

![SwissKit](https://img.shields.io/badge/SwissKit-Desktop%20Toolbox-blue) ![Java](https://img.shields.io/badge/Java-17-orange) ![License](https://img.shields.io/badge/License-MIT-green)

**SwissKit** is a *modular desktop toolbox* built with Java Swing. It provides a clean, extensible platform for various utility tools including Excel file processing, email sending, and more. The application uses a plugin-based architecture with automatic page discovery, making it easy to add new functionality.

If you want a lightweight, fast, and customizable desktop utility suite, this is it.

Website · Documentation · Getting Started · Contributing

---

## Quick Start

**Requirements:**
- **JDK 17 or higher**
- **Maven 3.6 or higher**

### Installation

```bash
# Clone the repository
git clone https://github.com/MuskStark/SwissKitJ.git
cd SwissKit

# Build the project
mvn clean package

# Run the application
mvn exec:java -Dexec.mainClass="fan.summer.Main"
```

Or run the JAR directly:

```bash
java -jar target/SwissKit-*.jar
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

### Current Tools

#### 📊 Excel Tool
- **File Analysis** - Read Excel file structure and extract headers
- **File Splitting** - Split Excel files by sheets or columns (in development)
- **Progress Tracking** - Real-time progress updates during processing
- **Multi-Sheet Support** - Handle multiple sheets in a single file

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
├── Main.java                    # Application entry point
├── kitpage/                     # Tool page modules
│   ├── KitPage.java            # Page interface definition
│   ├── WelcomePage.java        # Welcome page
│   ├── email/                  # Email tool
│   │   ├── EmailKitPage.java
│   │   └── EmailKitPage.form
│   └── excel/                  # Excel tool
│       ├── ExcelKitPage.java
│       ├── ExcelKitPage.form
│       ├── listener/           # Event listeners
│       │   └── HeaderListener.java
│       └── worker/             # Background workers
│           ├── ExcelAnalysisWorker.java
│           └── ExcelAnalysisCallback.java
└── utils/                       # Utility classes
    ├── SideMenuBar.java        # Side menu component
    └── UIUtils.java            # UI utilities
```

### Plugin System

SwissKit uses an automatic discovery mechanism for tool pages:

1. **Interface**: Implement the `KitPage` interface
2. **Methods**: Provide `getPanel()` and `getTitle()` methods
3. **Auto-discovery**: Pages are automatically discovered at runtime
4. **No Registration**: No manual registration required

Example:

```java
public class MyToolPage implements KitPage {
    private JPanel panel;

    public MyToolPage() {
        initComponents();
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
}
```

### Tech Stack

- **Language**: Java 17
- **Build Tool**: Maven
- **UI Framework**: Swing
- **UI Theme**: FlatLaf 3.5 (FlatIntelliJLaf)
- **Excel Processing**:
  - Apache FESOD 2.0.1-incubating (streaming read)
  - Apache POI (metadata extraction)
- **UI Components**: SwingX 1.6.5
- **Logging**: Log4j 2.25.3 + SLF4J 2.20.0

---

## Configuration

### Application Window

Default window settings:
- **Minimum Size**: 800x500 pixels
- **Default Size**: 900x600 pixels
- **Theme**: FlatIntelliJLaf

### Excel Tool Configuration

The Excel tool supports two splitting modes:
- **Split by Sheet**: Separate each sheet into individual files
- **Split by Column**: Split data based on column values

### Adding a Custom Icon

Place an icon file in `src/main/resources/`:
- `icon.png` (preferred)
- `icon.jpg`
- `app.png`

---

## Development

### Project Structure

- **Main.java** - Application entry point and main window initialization
- **KitPage.java** - Interface for tool pages
- **SideMenuBar.java** - Dynamic side menu component
- **UIUtils.java** - Common UI utilities and constants

### Adding a New Tool

1. Create a new package under `fan.summer.kitpage`
2. Create a class implementing `KitPage`
3. Implement required methods
4. Add UI components and functionality
5. Build and run - the page will be automatically discovered

### Code Style

- Use camelCase for method and variable names
- Use PascalCase for class names
- Add Javadoc comments for all public methods
- Follow existing code conventions

### Testing

```bash
# Run tests
mvn test

# Run with coverage
mvn test jacoco:report
```

### Building

```bash
# Clean build
mvn clean package

# Skip tests
mvn clean package -DskipTests
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
    @Override
    protected Result doInBackground() throws Exception {
        // Background thread execution
        Result result = doWork();
        publish(progress);
        return result;
    }

    @Override
    protected void process(List<Integer> chunks) {
        // EDT thread: Update UI
        progressBar.setValue(chunks.get(chunks.size() - 1));
    }

    @Override
    protected void done() {
        // EDT thread: Task completion
        try {
            callback.onSuccess(get());
        } catch (Exception e) {
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
- **Solution**: Ensure JDK 17+ is installed and JAVA_HOME is set

**Issue**: Excel files not reading
- **Solution**: Check file permissions and format compatibility

**Issue**: UI not rendering correctly
- **Solution**: Update FlatLaf dependencies and rebuild

### Debug Mode

Enable verbose logging:

```bash
java -Dlog4j.configurationFile=path/to/log4j2.xml -jar target/SwissKit-*.jar
```

---

## Roadmap

- [ ] Complete Excel file splitting functionality
- [ ] Implement email sending with SMTP support
- [ ] Add PDF processing tool
- [ ] Add image processing tool
- [ ] Create log4j2.xml configuration
- [ ] Add application configuration system
- [ ] Support theme switching
- [ ] Add unit tests
- [ ] Create distribution scripts

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

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## Authors

- **MuskStark**


---

## Acknowledgments

- FlatLaf theme framework
- Apache POI and Apache FESOD for Excel processing
- The open-source Java community

---

**Built with ❤️ using Java and Swing**