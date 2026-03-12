# SwissKit

![SwissKit](https://img.shields.io/badge/SwissKit-Desktop%20Toolbox-blue) ![Java](https://img.shields.io/badge/Java-11-orange) ![License](https://img.shields.io/badge/License-MIT-green) ![Maven](https://img.shields.io/badge/Maven-3.6+-red)

**SwissKit** is a *modular desktop toolbox* built with Java Swing. It provides a clean, extensible platform for various utility tools including Excel file processing, email sending, email address book management, and plugin support. The application uses a plugin-based architecture with automatic page discovery, making it easy to add new functionality.

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
cd SwissKitJ

# Build the project
mvn clean package

# Run the application (executable JAR)
java -jar target/SwissKit-1.0-Alpha.jar
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

- **📦 Modular Architecture** - Plugin-based design with SPI auto-discovery
- **🎨 Modern UI** - Built with Swing and FlatLaf theme framework
- **⚡ High Performance** - Uses Apache FESOD for efficient Excel processing
- **🔄 Async Processing** - Background tasks with SwingWorker for non-blocking UI
- **💾 Database Support** - H2 + MyBatis for persistent storage
- **📧 Email Management** - Address book with tags for contact organization
- **🔌 Plugin System** - Install external JAR plugins to extend functionality
- **🛠️ Easy Extension** - Add new tools by implementing the `KitPage` interface
- **📊 Custom UI Components** - Gradient progress bar, fixed-width combo box
- **🚀 Splash Screen** - Professional startup experience with loading indicator

### Current Tools

#### 📊 Excel Tool
- **File Analysis** - Read Excel file structure and extract headers
- **File Splitting** - Split Excel files by sheets or column values
- **Complex Split Mode** - Advanced splitting with custom configuration
- **Config View** - View and edit split configurations
- **Progress Tracking** - Real-time progress updates with percentage display
- **Multi-Sheet Support** - Handle multiple sheets in a single file
- **Streaming Data Processing** - Efficient memory usage with Apache FESOD

#### 📧 Email Tool
- **Email Composition** - Compose emails with subject and body
- **Recipient Management** - Add multiple recipients (To, Cc)
- **Mass Email Support** - Send emails to multiple contacts based on tags
- **Tag-based Recipients** - Load recipients from address book by tags
- **Attachment by Tag** - Attach files from tag-based folder selection
- **SMTP Integration** - Full SMTP support with TLS/SSL

#### ⚙️ Settings
- **Email Server Configuration** - SMTP settings with TLS/SSL support
- **Email Address Book** - Manage email contacts with nicknames
- **Tag Management** - Create and manage tags for categorizing contacts
- **Plugin Installation** - Install JAR plugins via file upload

#### 🏠 Welcome Page
- Application overview and guidance
- Quick access to all tools

---

## Architecture

```
SwissKit/
├── Main.java                        # Application entry point
├── annoattion/                      # Annotations
│   └── SwissKitPage.java           # Page annotation
├── api/                            # API interfaces
│   └── KitPage.java                # Plugin interface
├── plugin/                         # Plugin system
│   ├── PluginLoader.java           # Plugin JAR loader
│   └── PluginDiagnostic.java       # Plugin diagnostics
├── scaner/                         # SPI-based scanner
│   └── SwissKitPageScaner.java    # Auto-discovery scanner
├── database/                       # Database layer (H2 + MyBatis)
│   ├── DatabaseInit.java           # Database initialization
│   ├── SwissKitDBTable.java       # Table marker interface
│   ├── entity/                     # Entity classes
│   │   ├── excel/
│   │   │   └── ComplexSplitConfigEntity.java
│   │   └── setting/email/
│   │       ├── SwissKitSettingEmailEntity.java
│   │       ├── EmailAddressBookEntity.java
│   │       └── EmailTagEntity.java
│   └── mapper/                     # MyBatis mappers
│       ├── email/
│       │   └── EmailMassSentConfigMapper.java
│       ├── excel/
│       │   └── ComplexSplitConfigMapper.java
│       └── setting/email/
│           ├── SwissKitSettingEmailMapper.java
│           ├── EmailAddressBookMapper.java
│           └── EmailTagMapper.java
├── kitpage/                        # Tool page modules
│   ├── welcome/                    # Welcome page
│   ├── email/                      # Email tool
│   ├── excel/                      # Excel tool
│   │   ├── second/                 # Config views
│   │   ├── listener/              # Event listeners
│   │   └── worker/                # Background workers
│   └── setting/                    # Settings page
│       ├── second/                 # Address book, tags views
│       └── worker/second/         # Query workers
├── ui/                              # UI components
│   ├── StartLoadingPage.java        # Splash screen
│   ├── home/
│   │   └── HomePage.java           # Main window
│   ├── sidebar/
│   │   └── SideMenuBar.java        # Side menu
│   └── components/
│       ├── GradientProgressBar.java
│       └── FixedWidthComboBox.java
└── utils/
    ├── AppInfo.java                # Application version info
    ├── UIUtils.java                # UI utilities
    ├── ExcelUtil.java              # Excel utilities
    ├── FileNameUtil.java           # File name utilities
    └── StringUtil.java             # String validation utilities
```

### Plugin System

SwissKit uses an automatic discovery mechanism with annotations and SPI (Service Provider Interface) for tool pages:

1. **Interface**: Implement the `KitPage` interface
2. **Annotation**: Add `@SwissKitPage` annotation to configure menu properties
3. **SPI Registration**: Add class name to `META-INF/services/fan.summer.api.KitPage`
4. **Auto-discovery**: Pages are automatically discovered at runtime using `SwissKitPageScaner`
5. **Sorting**: Pages are sorted by `order()` value in annotation

Example:

```java
package fan.summer.kitpage.mytool;

import fan.summer.api.KitPage;
import fan.summer.annoattion.SwissKitPage;

import javax.swing.*;

@SwissKitPage(
        menuName = "🔧 My Tool",
        menuTooltip = "Open My Tool",
        visible = true,
        order = 10
)
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
}
```

### Tech Stack

| Category | Technology | Version |
|----------|------------|---------|
| **Language** | Java | 11 |
| **Build Tool** | Maven | 3.6+ |
| **UI Framework** | Swing + FlatLaf | 3.5 |
| **Excel Processing** | Apache FESOD | 2.0.1-incubating |
| **Database** | H2 + MyBatis | 2.4.240 / 3.5.19 |
| **Logging** | Log4j2 + SLF4J | 2.25.3 / 2.0.16 |
| **JSON** | FastJSON2 | 2.0.59 |
| **Code Simplification** | Lombok | 1.18.42 |

---

## Database

### Tables

| Table Name | Purpose |
|------------|---------|
| `swiss_kit_setting_email` | Email SMTP configuration |
| `complex_split_config` | Excel complex split configuration |
| `email_address_book` | Email contacts with nicknames and tags |
| `email_tag` | Tags for categorizing email contacts |
| `email_mass_sent_config` | Mass email sending configuration |

### Database Location

- **Path**: `.swisskit/swisskit.db` (relative to application runtime directory)
- **Type**: H2 (embedded, file-based, no external server required)

---

## Development

### Adding a New Tool

1. Create a new package under `fan.summer.kitpage` (e.g., `pdf/`, `image/`)
2. Create a class implementing `KitPage`
3. Add `@SwissKitPage` annotation for menu configuration
4. Register in SPI service file (`META-INF/services/fan.summer.api.KitPage`)
5. Build and run - the page will be automatically discovered

### Code Style

- Use camelCase for method and variable names
- Use PascalCase for class names
- Add Javadoc comments for all public methods
- All UI text and comments should be in English
- Use SLF4J Logger for logging
- Use `SansSerif` font for UI components

### Building

```bash
# Clean build with executable JAR
mvn clean package

# Skip tests
mvn clean package -DskipTests

# Run executable JAR
java -jar target/SwissKit-1.0-Alpha.jar
```

---

## Roadmap

- [x] Excel file analysis functionality
- [x] Excel file split by sheet functionality
- [x] Excel file split by column functionality
- [x] Excel complex split mode with configuration
- [x] Email address book management
- [x] Email tag management
- [x] Plugin installation support
- [x] Email sending with SMTP support
- [x] Mass email sending with tag-based recipients
- [ ] Add PDF processing tool
- [ ] Add image processing tool
- [ ] Support theme switching
- [ ] Add unit tests
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

- `✨` / `:sparkles:` - New feature
- `📝` / `:memo:` - Documentation
- `🐛` / `:bug:` - Bug fix
- `♻️` / `:recycle:` - Refactor code
- `⬆️` / `:arrow_up:` - Upgrade dependency

---

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

## Authors

- **Summer** - Core development
- **PhoebeJ** - Email & Excel functionality

---

## Documentation

For detailed technical documentation, see [AGENTS.md](AGENTS.md).

For user documentation, see the [docs](docs/) folder.

---

**Built with ❤️ using Java and Swing**
