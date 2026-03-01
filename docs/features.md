# Features

SwissKit provides a variety of tools for everyday tasks. This section covers all available features and how to use them.

## Table of Contents

- [Excel Tool](#excel-tool)
- [Email Tool](#email-tool)
- [Welcome Page](#welcome-page)
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

### Progress Tracking

All Excel operations include real-time progress tracking:

- Visual progress bar with gradient animation
- Percentage display
- Status messages
- Completion notifications

## Email Tool

The email tool allows you to compose and send emails (in development).

**Features** (Planned):
- Email composition with subject and body
- Multiple recipient support
- SMTP configuration
- Attachment support
- Email templates

**Status**: 🚧 In Development

## Welcome Page

The welcome page provides an overview of the application.

**Features**:
- Application introduction
- Quick access to all tools
- Operation guidance
- Modern, clean design

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
- 220px width
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

## Plugin System

SwissKit's modular architecture allows easy extension of functionality.

### Adding New Tools

To add a new tool:

1. Create a package under `fan.summer.kitpage`
2. Implement the `KitPage` interface
3. Add `@SwissKitPage` annotation for menu configuration
4. The tool will be automatically discovered and sorted by order

**Example**:
```java
import fan.summer.kitpage.KitPage;
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

See [Development Guide](development.md) for more details.

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
- [ ] Plugin marketplace
- [ ] Cloud integration

---

**Want to learn more?** Check out the [Architecture](architecture.md) section to understand how everything works.