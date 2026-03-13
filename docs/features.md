# Features

SwissKit provides a variety of tools for everyday tasks. This section covers all available features and how to use them.

## Table of Contents

- [Excel Tool](#excel-tool)
- [Email Tool](#email-tool)
- [Settings](#settings)
- [Welcome Page](#welcome-page)
- [Plugin System](#plugin-system)
- [Custom UI Components](#custom-ui-components)

## Excel Tool

The Excel tool provides powerful file processing capabilities including analysis and splitting.

### File Analysis

Analyze Excel files to extract structure and header information.

**Features**:
- Read all sheet pages
- Extract headers from each sheet
- Real-time progress display
- Support for .xlsx and .xls formats

**How to Use**:

1. Click the "Select File" button
2. Choose an Excel file
3. Click "Analyze" button
4. View results in the progress area

**Example Output**:
```
Parsing completed! Total 3 sheets
- Sheet1: [ID, Name, Email, Phone]
- Sheet2: [Date, Amount, Status]
- Sheet3: [Product, Quantity, Price]
```

### File Splitting

Split Excel files by sheet into separate files.

**Features**:
- Split each sheet into individual Excel file
- Real-time progress updates with percentage
- Automatic header preservation
- Warning dialogs for validation

**How to Use**:

1. Select an Excel file
2. Choose output directory
3. Select "Split by Sheet" checkbox
4. Click "Split" button
5. Wait for completion

**Output Structure**:
```
output/
├── Sheet1.xlsx
├── Sheet2.xlsx
└── Sheet3.xlsx
```

### Complex Split Mode

Advanced splitting with custom configuration stored in database.

**Features**:
- Custom header row index
- Custom split column index
- Configuration persistence
- Edit saved configurations

**How to Use**:

1. Select an Excel file
2. Analyze the file first
3. Configure header and column indices
4. Save configuration for reuse
5. View/Edit configurations via "View Config" button

### Progress Tracking

All Excel operations include real-time progress tracking:

- Visual progress bar with gradient animation
- Percentage display
- Status messages
- Completion notifications

## Email Tool

The email tool allows you to compose and send emails, with support for single and mass email sending. SwissKit uses **Simple Java Mail** library for robust SMTP email handling.

**Features**:
- Email composition with subject and body
- Multiple recipient support (To, Cc)
- Mass email mode with tag-based recipients
- SMTP configuration integration (via Simple Java Mail)
- Attachment support by tag-based folder selection

### Single Email Sending

Send emails to individual recipients.

**How to Use**:

1. Enter email subject
2. Enter recipient email addresses (To, Cc)
3. Enter email body content
4. Click "Sent" button
5. Wait for sending completion

### Mass Email Sending

Send emails to multiple recipients based on tag configuration.

**Features**:
- Tag-based recipient selection
- Attachment files from tag-based folders
- Automatic recipient matching by tags
- Support for multiple tags (To/Cc)

**How to Use**:

1. Enable "MassSent" checkbox
2. Click "MassSendConfig" to configure
3. Select To tag (required)
4. Select Cc tag (optional)
5. Enable attachment if needed, select folder
6. Save configuration
7. Click "Sent" to send emails

**How It Works**:

1. The system generates a unique task ID for the mass sending session
2. Load all contacts from address book
3. Parse attachment files from selected folder (filename format: `filename_tag.ext`)
4. For each tag, find matching recipients and send emails
5. Show progress and results

### View Configuration

View the current mass sending configuration.

**How to Use**:

1. Enable "MassSent" checkbox
2. Click "ViewSentConfig" button
3. View configuration in dialog

## Settings

The settings page provides application configuration options with multiple tabs.

### Email Settings Tab

Configure SMTP server for email sending.

**Features**:
- Protocol selection (SMTP)
- Server URL and port configuration
- Username and password
- TLS/SSL support
- Test email sending

### Email Address Book Tab

Manage email contacts with nicknames and tags.

**Features**:
- Add new email addresses
- Edit contact nicknames
- Assign tags to contacts
- View all contacts in table format

**How to Use**:

1. Click "Open Address Book" button
2. View existing contacts in the table
3. Click "Add New Address" to add a contact
4. Enter email, nickname, and select tags
5. Click "Insert" to save

### Tag Management

Create and manage tags for categorizing contacts.

**Features**:
- Create new tags
- Edit existing tags
- Delete tags
- Tags stored in database

**How to Use**:

1. Open Address Book
2. Click "Modify Tags" button
3. Add new tag or double-click to edit
4. Tags are automatically available in Add Address dialog

### Plugin Tab

Install external JAR plugins to extend functionality.

**Features**:
- Select JAR file from file system
- Install plugins to `.swisskit/plugins/` directory
- Plugins loaded on next startup

**How to Use**:

1. Navigate to Plugin tab
2. Click "Choice Plugin" to select a JAR file
3. Click "Upload" to install
4. Restart application to load the new plugin

## Welcome Page

The welcome page provides an overview of the application.

**Features**:
- Application introduction
- Quick access to all tools
- Operation guidance
- Modern, clean design

## Plugin System

SwissKit's modular architecture allows easy extension of functionality using SPI (Service Provider Interface).

### Adding New Tools

To add a new tool:

1. Create a package under `fan.summer.kitpage`
2. Implement the `KitPage` interface
3. Add `@SwissKitPage` annotation for menu configuration
4. Register in SPI service file (`META-INF/services/fan.summer.api.KitPage`)
5. The tool will be automatically discovered and sorted by order

**Example**:

```java
import fan.summer.api.KitPage;
import fan.summer.annoattion.SwissKitPage;

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

    @Override
    public JPanel getPanel() {
        return panel;
    }
}
```

### Installing External Plugins

1. Build your plugin as a JAR file
2. Open Settings → Plugin tab
3. Select and upload the JAR file
4. Restart SwissKit

## Database Layer

SwissKit includes a built-in database layer using H2 and MyBatis for persistent storage.

### Features

- **Embedded Database** - No external server required
- **Automatic Initialization** - Database created on first run
- **MyBatis Integration** - Clean DAO layer with XML mappers
- **Multiple Tables** - Support for various data types

### Database Location

- **Path**: `.swisskit/swisskit.db`
- **Auto-created**: On first application run

### Current Tables

| Table | Purpose |
|-------|---------|
| `swiss_kit_setting_email` | Email SMTP configuration |
| `complex_split_config` | Excel complex split settings |
| `email_address_book` | Email contacts with nicknames and tags |
| `email_tag` | Tags for categorizing contacts |
| `email_mass_sent_config` | Mass email sending configuration |

## Custom UI Components

SwissKit includes custom UI components for enhanced user experience.

### GradientProgressBar

A progress bar with smooth animation and gradient effects.

**Features**:
- Blue to purple gradient
- Smooth easing animation
- Rounded corners
- Glossy highlight effect
- Optional text display
- 60 FPS animation

**Usage**:
```java
GradientProgressBar progressBar = new GradientProgressBar();
progressBar.setMinimum(0);
progressBar.setMaximum(100);
progressBar.setValue(50);
progressBar.setStringPainted(true);
progressBar.setString("Processing... 50%");
```

### FixedWidthComboBox

A combo box with fixed width for consistent UI layout.

**Features**:
- Fixed width configuration
- Consistent appearance
- Easy integration

**Usage**:
```java
FixedWidthComboBox comboBox = new FixedWidthComboBox(200);
comboBox.addItem("Option 1");
comboBox.addItem("Option 2");
```

### SideMenuBar

Dynamic side menu component with modern styling.

**Features**:
- 160px width
- Selected state highlighting
- Mouse hover effects
- Custom icons and tooltips
- Runtime page management
- Automatic page discovery

**Color Scheme**:
- Selected background: Dark gray (#2D2D2D)
- Selected text: Purple (#BB86FC)
- Hover background: Light gray (#E8E8E8)
- Default background: Lighter gray (#F3F3F3)

## Performance Features

SwissKit is optimized for performance:

- **Streaming Excel Processing** - Uses Apache FESOD for efficient memory usage
- **Async Operations** - Background tasks with SwingWorker
- **Progress Tracking** - Real-time updates without blocking UI
- **Lazy Loading** - Pages loaded on demand

## Accessibility

SwissKit aims to be accessible:

- Keyboard navigation support
- Clear visual feedback
- Consistent UI patterns
- Descriptive tooltips

## Future Features

Planned enhancements:

- [ ] PDF processing tool
- [ ] Image processing tool
- [ ] Theme switching
- [ ] Multi-language support

---

**Want to learn more?** Check out the [Architecture](architecture.md) section to understand how everything works.
