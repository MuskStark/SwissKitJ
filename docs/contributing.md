# Contributing

Thank you for your interest in contributing to SwissKit! This document provides guidelines and instructions for contributing to the project.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [How to Contribute](#how-to-contribute)
- [Development Workflow](#development-workflow)
- [Pull Request Process](#pull-request-process)
- [Coding Standards](#coding-standards)
- [Reporting Issues](#reporting-issues)

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Welcome new contributors
- Focus on what is best for the community

## Getting Started

### Prerequisites

- JDK 11 or higher
- Maven 3.6 or higher
- Git
- IntelliJ IDEA (recommended)

### Setup

```bash
# Fork the repository
# Clone your fork
git clone https://github.com/YOUR_USERNAME/SwissKitJ.git
cd SwissKit

# Add upstream remote
git remote add upstream https://github.com/MuskStark/SwissKitJ.git

# Build the project
mvn clean compile

# Run the application
mvn exec:java -Dexec.mainClass="fan.summer.Main"
```

## How to Contribute

There are many ways to contribute:

### Reporting Bugs

1. Check existing [Issues](https://github.com/MuskStark/SwissKitJ/issues)
2. Create a new issue with:
   - Clear title
   - Detailed description
   - Steps to reproduce
   - Expected behavior
   - Actual behavior
   - Environment details (OS, Java version)

### Suggesting Features

1. Check existing feature requests
2. Create a new issue with:
   - Clear description of the feature
   - Use cases and benefits
   - Possible implementation approach

### Submitting Code

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## Development Workflow

### 1. Create a Branch

```bash
git checkout -b feature/your-feature-name
```

Branch naming conventions:
- `feature/` - New features
- `bugfix/` - Bug fixes
- `docs/` - Documentation updates
- `refactor/` - Code refactoring

### 2. Make Changes

- Write clean, readable code
- Add comments where necessary
- Update documentation
- Follow coding standards

### 3. Test Your Changes

```bash
# Compile
mvn clean compile

# Run tests
mvn test

# Build
mvn clean package

# Run application
mvn exec:java -Dexec.mainClass="fan.summer.Main"
```

### 4. Commit Your Changes

```bash
git add .
git commit -m "type: description"
```

Commit message format:
- `:sparkles:` - New feature
- `:art:` - Improve structure/format
- `:memo:` - Documentation
- `:bug:` - Bug fix
- `:arrow_up:` - Upgrade dependency
- `:recycle:` - Refactor code

Examples:
```bash
git commit -m ":sparkles: Add PDF processing tool"
git commit -m ":bug: Fix progress bar not updating"
git commit -m ":memo: Update API documentation"
```

### 5. Push to Your Fork

```bash
git push origin feature/your-feature-name
```

### 6. Create Pull Request

1. Go to the original repository on GitHub
2. Click "New Pull Request"
3. Select your branch
4. Fill in the PR template
5. Submit

## Pull Request Process

### PR Template

```markdown
## Description
Brief description of changes

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
Describe how you tested your changes

## Checklist
- [ ] Code follows project style
- [ ] Self-reviewed the code
- [ ] Commented complex code
- [ ] Updated documentation
- [ ] No new warnings
- [ ] Added tests
- [ ] All tests pass
```

### Review Process

1. Automated checks run (CI)
2. Maintainer reviews your PR
3. Address any feedback
4. PR is merged

## Coding Standards

### Language

- All code comments in English
- All UI text in English
- Variable and method names in English

### Code Style

- Follow existing code conventions
- Use meaningful names
- Add Javadoc for public APIs
- Keep methods focused and small

### UI Standards

- Use `SansSerif` font
- Use color constants from `UIUtils`
- Ensure consistent layout

### Thread Safety

- Update UI components in EDT thread only
- Use `SwingUtilities.invokeLater()` for UI updates

```java
// Correct
SwingUtilities.invokeLater(() -> progressBar.setValue(50));

// Incorrect
progressBar.setValue(50); // From background thread
```

### Error Handling

- Handle exceptions gracefully
- Provide user-friendly error messages
- Log errors for debugging

```java
try {
    // Operation
} catch (Exception e) {
    logger.error("Operation failed", e);
    JOptionPane.showMessageDialog(parent,
            "Operation failed: " + e.getMessage(),
            "Error", JOptionPane.ERROR_MESSAGE);
}
```

## Reporting Issues

### Bug Report Template

```markdown
## Description
Clear description of the bug

## Steps to Reproduce
1. Step one
2. Step two
3. Step three

## Expected Behavior
What should happen

## Actual Behavior
What actually happens

## Environment
- OS: [e.g., Windows 10, macOS 12]
- Java Version: [e.g., 11.0.15]
- SwissKit Version: [e.g., 1.0-SNAPSHOT]

## Screenshots
If applicable, add screenshots

## Additional Context
Any other relevant information
```

### Feature Request Template

```markdown
## Feature Description
Clear description of the feature

## Use Cases
Describe the use cases

## Proposed Solution
How you envision this feature working

## Alternatives Considered
Other approaches you considered

## Additional Context
Any other relevant information
```

## Getting Help

If you need help:

1. Check existing documentation
2. Search existing issues
3. Ask in discussions or issues

## Recognition

Contributors will be acknowledged in the project's AUTHORS file.

---

**Thank you for contributing to SwissKit!** 🎉