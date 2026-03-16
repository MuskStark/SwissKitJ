# Changelog

All notable changes to SwissKit will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

## [1.0-Alpha4] - 2026-03-12

### Added
- Progress bar support for email sending
  - Added JProgressBar parameter to EmailSentWorker constructor
  - Changed SwingWorker type to support progress updates (Void → Integer)
  - Added process() and done() methods for progress bar updates

### Changed
- Display tag names instead of IDs in configuration view
  - Updated EmailKitPage to convert tag IDs to tag names for display
- Improved progress bar real-time updates
  - Moved publish() inside loop for real-time progress updates in EmailSentWorker

### Fixed
- Set combo box to unselected state after loading tags in MassSentConfigView

### Removed
- Removed unused ViewSentConfigView file

## [1.0-Alpha3] - 2026-03-12

### Changed
- Tag association mechanism refactored
  - Replaced tag name with tag ID (Long) for email tag handling
  - Added `TagComBoxItemDto` for combo box display with ID and name

## [1.0-Alpha2] - 2026-03-12

### Added
- Email sending functionality
  - `EmailUtil.java` - SMTP email sending utility with TLS/SSL support
  - Support for single email sending
  - Support for attachments
- Mass email sending feature
  - `EmailMassSentConfigEntity` - Entity for mass sending configuration
  - `EmailMassSentConfigMapper` - MyBatis mapper for mass config operations
  - `MassSentConfigView` - Dialog for configuring mass sending settings
  - `ViewSentConfigView` - Dialog for viewing configuration
  - `EmailSentWorker` - Background worker for mass email sending
  - Tag-based recipient selection
  - Attachment support by tag-based folder selection
  - Task ID generation for tracking mass sending sessions
- Database table `email_mass_sent_config` for storing mass sending configurations

### Changed
- Renamed ExcelSentWorker to EmailSentWorker for better naming consistency
- Fixed email tags storage (use JSON string instead of JSON type in H2)
- Added sent button action in EmailKitPage to trigger EmailSentWorker

### Fixed
- Fixed tag selection feature in AddAddressView (JSON serialization)
- Email tags now properly stored as JSON string in database

## [1.0-Alpha] - 2026-03-10

### Added
- Excel complex split mode with custom configuration
- ConfigView and ConfigEditorView for viewing/editing split configurations
- Multiple background workers for Excel operations
  - `ExcelAnalysisWorker` - Analyze Excel files
  - `ExcelSplitWorker` - Split Excel files
  - `SetComplexSplitConfigWorker` - Save configurations
  - `ClearComplexSplitConfigWorker` - Delete configurations
  - `ShowConfigViewWorker` - View configurations
- Replace SQLite with H2 database
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
- String validation utility (`StringUtil.java`)
  - Email format validation
- Database tables
  - `swiss_kit_setting_email` - Email SMTP configuration
  - `complex_split_config` - Excel complex split configuration
  - `email_address_book` - Email contacts with nicknames and tags
  - `email_tag` - Tags for categorizing email contacts
- Log4j configuration with file logging
  - Log files stored in `.swisskit/log/` directory
- Custom UI components
  - `GradientProgressBar` - Progress bar with gradient animation
  - `FixedWidthComboBox` - Fixed-width combo box

### Changed
- Updated Java version from 17 to 11
- Updated H2 database version to 2.4.240
- Replaced all Chinese comments with English
- Added Logger to all classes with database/IO operations
- Added Javadoc comments to Entity and Mapper classes
- Updated SLF4J version from 2.20.0 to 2.0.16
- Added Lombok annotation processor configuration in pom.xml
- Settings page now includes Email and Plugin tabs
- Updated HeaderListener to return `Map<Integer, String>` instead of `List<String>`

### Fixed
- Fixed needUpdateId reset timing in email tag update (moved to EDT)
- Fixed JAR execution issues with slf4j-api dependency
- Fixed H2 schema initialization in DatabaseInit
- Progress bar not showing text issue
- Button state management during background tasks

---

[Unreleased]: https://github.com/MuskStark/SwissKitJ/compare/v1.0-Alpha4...HEAD
[1.0-Alpha4]: https://github.com/MuskStark/SwissKitJ/compare/v1.0-Alpha3...v1.0-Alpha4
[1.0-Alpha3]: https://github.com/MuskStark/SwissKitJ/compare/v1.0-Alpha2...v1.0-Alpha3
[1.0-Alpha2]: https://github.com/MuskStark/SwissKitJ/compare/v1.0-Alpha...v1.0-Alpha2
[1.0-Alpha]: https://github.com/MuskStark/SwissKitJ/compare/v1.0-Alpha...v1.0-Alpha