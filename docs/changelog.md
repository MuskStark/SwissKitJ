# Changelog

All notable changes to SwissKit will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0-Alpha] - 2026-03-10

### Added
- Email address book management feature
  - `EmailAddressBookEntity` - Entity for storing email contacts
  - `EmailAddressBookMapper` - MyBatis mapper for address book operations
  - `EmailAddressBookView` - Dialog for viewing and managing contacts
  - `AddAddressView` - Dialog for adding new email addresses with tag selection
  - `QueryAllEmailInfoWorker` - Background worker for loading contacts
  - `QueryAllEmailInfoCallBack` - Callback interface for query results
- Email tag management feature
  - `EmailTagEntity` - Entity for email tags
  - `EmailTagMapper` - MyBatis mapper for tag operations
  - `EmailTagsView` - Dialog for managing tags with async loading
- Plugin loading system
  - `PluginLoader` - Loads JAR plugins from `.swisskit/plugins/` directory
  - `PluginDiagnostic` - Plugin diagnostics utility
  - Plugin installation UI in Settings page
- Application info utility (`AppInfo.java`)
  - Version reading from MANIFEST.MF or app.properties
  - Application name and version constants
- String validation utility (`StringUtil.java`)
  - Email format validation
- Database tables
  - `email_address_book` - Store email contacts with nicknames and tags
  - `email_tag` - Store tags for categorizing contacts
- Log4j configuration with file logging
  - Log files stored in `.swisskit/log/` directory

### Changed
- Replaced all Chinese comments with English
- Added Logger to all classes with database/IO operations
- Added Javadoc comments to Entity classes
- Added Javadoc comments to Mapper interfaces
- Updated SLF4J version from 2.20.0 to 2.0.16
- Added Lombok annotation processor configuration in pom.xml
- Settings page now includes Email and Plugin tabs

### Fixed
- Fixed needUpdateId reset timing in email tag update (moved to EDT)
- Fixed JAR execution issues with slf4j-api dependency

## [0.9.0] - 2026-03-06

### Added
- ConfigEditorView for editing complex split configurations
- ConfigEditorView.jfd JFormDesigner layout file
- Update method in ComplexSplitConfigMapper
- Update SQL in ComplexSplitConfigMapper.xml
- SettingKitPage for application settings
- SettingKitPage.jfd JFormDesigner layout file

### Changed
- ConfigView now accepts taskId parameter
- UpdateButton renamed in ConfigEditorView
- Removed TODO comments from ExcelKitPage and EmailKitPage

## [0.8.0] - 2026-03-04

### Added
- Excel complex split mode with custom configuration
- ConfigView window for viewing split configurations
- SetComplexSplitConfigWorker for setting split configurations
- ClearComplexSplitConfigWorker for deleting split configurations
- ShowConfigViewWorker for viewing configurations
- ConfigView.jfd JFormDesigner layout file
- Replace SQLite with H2 database
- H2 database version 2.4.240
- ExcelUtil.java utility class
- FileNameUtil.java utility class

### Changed
- Updated H2 database version from 2.2.224 to 2.4.240
- Updated version to 1.0-Alpha
- Renamed button2 to viewConfigBt in ExcelKitPage

### Fixed
- Fixed H2 schema initialization in DatabaseInit

## [0.7.0] - 2026-03-02

### Added
- Excel file split by sheet functionality
- Real-time progress tracking with percentage display
- Warning dialogs for user feedback
- NoModelDataListener for streaming Excel data reading
- Gradient progress bar component with smooth animation
- Fixed-width combo box component
- JAR file scanning support for production builds
- Lombok dependency for code simplification
- FastJSON2 dependency for JSON processing
- Maven Shade plugin for executable JAR creation
- CI/CD workflows for automated testing and building
- Multi-platform support (Windows, Linux, macOS)
- GitHub Actions for release automation

### Changed
- Updated Java version from 17 to 11
- Updated HeaderListener to return `Map<Integer, String>` instead of `List<String>`
- Improved logging throughout the application
- Replaced all Chinese text with English translations
- Updated project documentation

### Fixed
- Progress bar not showing text issue
- Button state management during background tasks
- Exception handling in worker threads

## [0.1.0] - 2026-02-27

### Added
- Initial project setup
- Plugin-based architecture with KitPage interface
- Automatic page discovery mechanism
- SideMenuBar component with dynamic menu generation
- Welcome page
- Excel tool page with file analysis
- ExcelAnalysisWorker for background processing
- HeaderListener for Excel header extraction
- FlatLaf theme integration
- GradientProgressBar component
- FixedWidthComboBox component
- UIUtils utility class
- Maven build configuration
- Log4j logging configuration
- Apache POI dependency for Excel processing
- Apache FESOD dependency for streaming Excel reading
- GitHub Actions workflows (CI, Release, Qodana)

### Features
- Excel file analysis with header extraction
- Multi-sheet support
- Progress tracking during operations
- Modern UI with FlatLaf theme
- Responsive design

---

[Unreleased]: https://github.com/MuskStark/SwissKitJ/compare/v1.0-Alpha...HEAD
[1.0-Alpha]: https://github.com/MuskStark/SwissKitJ/compare/v0.9.0...v1.0-Alpha
[0.9.0]: https://github.com/MuskStark/SwissKitJ/compare/v0.8.0...v0.9.0
[0.8.0]: https://github.com/MuskStark/SwissKitJ/compare/v0.7.0...v0.8.0
[0.7.0]: https://github.com/MuskStark/SwissKitJ/compare/v0.1.0...v0.7.0
[0.1.0]: https://github.com/MuskStark/SwissKitJ/releases/tag/v0.1.0
