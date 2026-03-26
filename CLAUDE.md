# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build Commands

```bash
# Install API module to local Maven repo (required before building main project)
mvn install -f SwissKitJ-Api/pom.xml -DskipTests

# Compile
mvn clean compile

# Run (development)
mvn exec:java -Dexec.mainClass="fan.summer.Main"

# Package executable JAR
mvn clean package

# Package without tests
mvn clean package -DskipTests

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=ClassName
```

## Architecture

SwissKit is a modular desktop toolbox built with Java Swing. It uses a multi-module Maven structure:

- **SwissKitJ-Api** - Shared API module (interfaces, annotations, UI components). Must be installed to local Maven repo before building the main module.
- **SwissKit** (main) - Core application with Excel, Email, and Settings tools.
- **SwissKitJ-Plugin-Qcc** - Example plugin project.

### Plugin System

SwissKit auto-discovers tools using Java SPI (Service Provider Interface). To add a new tool:

1. Create a class implementing `KitPage` interface (from `fan.summer.api`)
2. Annotate with `@SwissKitPage` (from `fan.summer.annoattion`)
3. Register in `META-INF/services/fan.summer.api.KitPage`
4. Tool appears automatically in sidebar, sorted by `order()` value

### Key Components

- `Main.java` - Application entry point
- `SwissKitPageScaner` - Discovers all `KitPage` implementations via SPI
- `PluginLoader` - Loads external JAR plugins from `.swisskit/plugins/`
- `DatabaseInit` - Initializes H2 database at `.swisskit/swisskit.db`
- `SideMenuBar` - Dynamic sidebar menu showing all discovered pages

### Database

- H2 embedded database with MyBatis ORM
- Database stored at `.swisskit/swisskit.db`
- MyBatis mappers in `src/main/resources/mapper/` with corresponding Java mapper interfaces

### UI Conventions

- Use `SansSerif` font for UI components
- Use color constants from `UIUtils`
- All long-running operations must use `SwingWorker` to avoid blocking EDT
- UI updates from background threads must use `SwingUtilities.invokeLater()`

### Technology Stack

- Java 11
- Maven 3.6+
- Swing + FlatLaf 3.5 (UI theme)
- Apache FESOD 2.0.1-incubating (Excel processing)
- H2 2.4.240 + MyBatis 3.5.19 (database)
- Simple Java Mail 8.12.6 (email)
- Lombok 1.18.42 (code generation)

## Code Standards

- All code comments and UI text in English
- camelCase for methods/variables, PascalCase for class names
- Javadoc for public methods
- Thread safety: always update UI on EDT using `SwingUtilities.invokeLater()`
