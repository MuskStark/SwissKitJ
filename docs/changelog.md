# Changelog

All notable changes to SwissKit will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

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

[Unreleased]: https://github.com/MuskStark/SwissKitJ/compare/v0.1.0...HEAD
[0.1.0]: https://github.com/MuskStark/SwissKitJ/releases/tag/v0.1.0