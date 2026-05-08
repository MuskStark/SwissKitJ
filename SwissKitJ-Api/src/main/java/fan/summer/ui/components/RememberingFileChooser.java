package fan.summer.ui.components;

import fan.summer.i18n.I18nManager;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Locale;
import java.util.Properties;

/**
 * A file chooser wrapper that remembers the last selected directory across sessions.
 * <p>
 * Provides selection modes for files, directories, or both. Directory memory persists
 * to a properties file in the application settings directory.
 * <p>
 * Usage example:
 * <pre>{@code
 * RememberingFileChooser chooser = new RememberingFileChooser(
 *     "last_dir_excel_input",
 *     RememberingFileChooser.SelectionMode.FILE_SELECTION
 * );
 * chooser.setI18nPrefix("excel.input");
 * chooser.setFileFilter(new FileNameExtensionFilter("Excel Files", "xlsx", "xls"));
 * File file = chooser.showOpenDialog(parentFrame);
 * if (file != null) {
 *     // process selected file
 * }
 * }</pre>
 *
 * @author summer
 * @since 2.2.0
 */
public class RememberingFileChooser extends JPanel {

    /**
     * Selection mode for the file chooser.
     */
    public enum SelectionMode {
        /** Select only files. */
        FILE_SELECTION(JFileChooser.FILES_ONLY),
        /** Select only directories. */
        DIRECTORY_SELECTION(JFileChooser.DIRECTORIES_ONLY),
        /** Select both files and directories. */
        FILES_AND_DIRECTORIES_SELECTION(JFileChooser.FILES_AND_DIRECTORIES);

        /** The underlying JFileChooser mode value. */
        private final int modeValue;

        SelectionMode(int modeValue) {
            this.modeValue = modeValue;
        }

        int getModeValue() {
            return modeValue;
        }
    }

    private static final String SETTINGS_DIR = ".swisskit";
    private static final String PROPERTIES_FILE = "last-directories.properties";

    /** Unique key for persisting the last directory. */
    private final String persistenceKey;
    /** The selection mode for this file chooser. */
    private final SelectionMode selectionMode;
    /** The underlying JFileChooser instance. */
    private final JFileChooser fileChooser;

    /** i18n prefix for localized dialog titles. */
    private String i18nPrefix;
    /** Custom file filter for file selection mode. */
    private FileFilter customFileFilter;

    /**
     * Creates a new RememberingFileChooser with the specified key and selection mode.
     *
     * @param persistenceKey  unique key for persisting the last directory
     * @param selectionMode  the file/directory selection mode
     */
    public RememberingFileChooser(String persistenceKey, SelectionMode selectionMode) {
        this.persistenceKey = persistenceKey;
        this.selectionMode = selectionMode;
        this.fileChooser = new JFileChooser();
        this.fileChooser.setFileSelectionMode(selectionMode.getModeValue());
        this.fileChooser.setMultiSelectionEnabled(false);

        setLayout(new BorderLayout());
        add(fileChooser, BorderLayout.CENTER);

        restoreLastDirectory();
    }

    /**
     * Creates a new RememberingFileChooser with FILE_SELECTION mode.
     *
     * @param persistenceKey unique key for persisting the last directory
     */
    public RememberingFileChooser(String persistenceKey) {
        this(persistenceKey, SelectionMode.FILE_SELECTION);
    }

    /**
     * Restores the last used directory from persistent storage.
     */
    private void restoreLastDirectory() {
        String lastDir = getLastDirectory(persistenceKey);
        if (lastDir != null && new File(lastDir).exists()) {
            fileChooser.setCurrentDirectory(new File(lastDir));
        }
    }

    /**
     * Gets the last selected directory for a given key.
     *
     * @param key the persistence key
     * @return the last directory path, or null if not found
     */
    public static String getLastDirectory(String key) {
        File propsFile = getPropertiesFile();
        if (!propsFile.exists()) {
            return null;
        }

        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream(propsFile)) {
            props.load(fis);
            return props.getProperty(key);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Sets the last selected directory for a given key.
     *
     * @param key  the persistence key
     * @param path the directory path to save
     */
    public static void setLastDirectory(String key, String path) {
        File propsFile = getPropertiesFile();

        Properties props = new Properties();
        if (propsFile.exists()) {
            try (FileInputStream fis = new FileInputStream(propsFile)) {
                props.load(fis);
            } catch (IOException e) {
                // Ignore - start fresh
            }
        }

        props.setProperty(key, path);

        File parentDir = propsFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(propsFile)) {
            props.store(fos, "Last selected directories");
        } catch (IOException e) {
            // Ignore - directory memory won't persist
        }
    }

    /**
     * Gets the properties file for storing directory preferences.
     */
    private static File getPropertiesFile() {
        String userDir = System.getProperty("user.dir");
        return new File(userDir, SETTINGS_DIR + "/" + PROPERTIES_FILE);
    }

    /**
     * Sets the i18n prefix for dialog title and button text.
     * Looks up: {prefix}.dialog.title and {prefix}.button.text
     *
     * @param i18nPrefix the i18n key prefix
     */
    public void setI18nPrefix(String i18nPrefix) {
        this.i18nPrefix = i18nPrefix;
    }

    /**
     * Sets a custom file filter for file selection mode.
     *
     * @param filter the file filter to use
     */
    public void setFileFilter(FileFilter filter) {
        this.customFileFilter = filter;
        fileChooser.setFileFilter(filter);
    }

    /**
     * Sets the dialog title using i18n if prefix is set, otherwise uses default.
     *
     * @param title the dialog title
     */
    public void setDialogTitle(String title) {
        fileChooser.setDialogTitle(title);
    }

    private String getLocalizedTitle() {
        if (i18nPrefix != null) {
            String key = i18nPrefix + ".dialog.title";
            String localized = I18nManager.get(key);
            if (!localized.equals(key)) {
                return localized;
            }
        }
        return selectionMode == SelectionMode.DIRECTORY_SELECTION
                ? I18nManager.get("file.chooser.directory.title")
                : I18nManager.get("file.chooser.file.title");
    }

    /**
     * Shows an open dialog and returns the selected file or directory.
     *
     * @param parent the parent component for the dialog
     * @return the selected File, or null if cancelled
     */
    public File showOpenDialog(Component parent) {
        fileChooser.setDialogTitle(getLocalizedTitle());
        int result = fileChooser.showOpenDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = fileChooser.getSelectedFile();
            saveCurrentDirectory(selected);
            return selected;
        }
        return null;
    }

    /**
     * Shows a save dialog and returns the selected file.
     *
     * @param parent the parent component for the dialog
     * @return the selected File, or null if cancelled
     */
    public File showSaveDialog(Component parent) {
        fileChooser.setDialogTitle(getLocalizedTitle());
        int result = fileChooser.showSaveDialog(parent);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = fileChooser.getSelectedFile();
            saveCurrentDirectory(selected);
            return selected;
        }
        return null;
    }

    /**
     * Saves the current directory based on the selected file.
     */
    private void saveCurrentDirectory(File selected) {
        if (selected != null) {
            File dir = selected.isDirectory() ? selected : selected.getParentFile();
            if (dir != null && dir.exists()) {
                setLastDirectory(persistenceKey, dir.getAbsolutePath());
            }
        }
    }

    /**
     * Gets the underlying JFileChooser for advanced configuration.
     *
     * @return the JFileChooser instance
     */
    public JFileChooser getFileChooser() {
        return fileChooser;
    }
}