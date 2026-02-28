# SwissKit Documentation

![SwissKit](https://img.shields.io/badge/SwissKit-Desktop%20Toolbox-blue) ![Java](https://img.shields.io/badge/Java-11-orange) ![License](https://img.shields.io/badge/License-MIT-green)

**SwissKit** is a modular desktop toolbox built with Java Swing, providing a clean, extensible platform for various utility tools.

## 🚀 Quick Links

- [Getting Started](getting-started.md) - Installation and setup guide
- [Features](features.md) - Explore available tools and capabilities
- [Architecture](architecture.md) - Understand the plugin system and design
- [Development](development.md) - Contribute and extend the project

## 📦 What is SwissKit?

SwissKit is a **modular desktop toolbox** that allows you to:
- Process Excel files efficiently
- Send emails
- Extend functionality with custom plugins
- Enjoy a modern, responsive UI

### Key Features

- 📦 **Modular Architecture** - Plugin-based design with automatic page discovery
- 🎨 **Modern UI** - Built with Swing and FlatLaf theme framework
- ⚡ **High Performance** - Uses Apache FESOD for efficient Excel processing
- 🔄 **Async Processing** - Background tasks with SwingWorker
- 🛠️ **Easy Extension** - Add new tools by implementing `KitPage` interface

## 🌟 Getting Started

### Installation

```bash
# Clone the repository
git clone https://github.com/MuskStark/SwissKitJ.git
cd SwissKit

# Build the project
mvn clean package

# Run the application
java -jar target/SwissKit-1.0-SNAPSHOT.jar
```

### Requirements

- **JDK 11 or higher**
- **Maven 3.6 or higher** (for building from source)

## 📖 Documentation

Explore the documentation sections:

- **[Getting Started](getting-started.md)** - Learn how to install and run SwissKit
- **[Features](features.md)** - Discover all available tools and features
- **[Architecture](architecture.md)** - Understand the plugin system and internal design
- **[Development](development.md)** - Learn how to contribute and extend SwissKit
- **[API Reference](api.md)** - Detailed API documentation
- **[Contributing](contributing.md)** - Guidelines for contributing to the project
- **[Changelog](changelog.md)** - Version history and changes

## 🤝 Contributing

We welcome contributions! Please see the [Contributing Guide](contributing.md) for details.

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- **Summer** - Core development
- **PhoebeJ** - Excel functionality

## 🙏 Acknowledgments

- FlatLaf theme framework
- Apache POI and Apache FESOD
- The open-source Java community

---

**Built with ❤️ using Java and Swing**