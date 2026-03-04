package fan.summer.database;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 * Database initialization utility class.
 * Handles SQLite database creation, table setup, and MyBatis SqlSessionFactory initialization.
 *
 * @author summer
 * @version 1.00
 * @date 2026/3/1
 */
public class DatabaseInit {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInit.class);

    private static SqlSessionFactory sqlSessionFactory;

    /**
     * Initializes the database: creates directories, tables, and MyBatis session factory.
     *
     * @throws RuntimeException if initialization fails
     */
    public static void init() {
        try {
            Path dbDir = Paths.get(".swisskit");
            if (!Files.exists(dbDir)) {
                Files.createDirectories(dbDir);
                logger.info("Created database directory: {}", dbDir.toAbsolutePath());
            }

            createTables();

            initMyBatis();

            logger.info("Database initialization completed");
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    /**
     * Creates database tables if they don't exist.
     * Creates swiss_kit_setting_email and complex_split_config tables.
     */
    private static void createTables() {
        String dbPath = ".swisskit/swisskit.db?sqlite.purejava=true";
        String url = "jdbc:sqlite:" + dbPath;

        String createEmailTable =
                "CREATE TABLE IF NOT EXISTS swiss_kit_setting_email (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "email TEXT NOT NULL," +
                        "password TEXT NOT NULL," +
                        "smtp_address TEXT NOT NULL," +
                        "smtp_port INTEGER NOT NULL," +
                        "need_tls INTEGER NOT NULL DEFAULT 0," +
                        "need_ssl INTEGER NOT NULL DEFAULT 0" +
                        ")";
        String createExcelTable =
                "CREATE TABLE IF NOT EXISTS complex_split_config (" +
                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "task_id TEXT NOT NULL," +
                        "field_name TEXT NOT NULL," +
                        "sheet_name TEXT NOT NULL," +
                        "header_index INTEGER NOT NULL," +
                        "column_index INTEGER NOT NULL" +
                        ")";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {

            stmt.execute(createEmailTable);
            stmt.execute(createExcelTable);
            logger.info("Database tables verified/created successfully");

        } catch (Exception e) {
            logger.error("Failed to create database tables", e);
            throw new RuntimeException("Failed to create database tables", e);
        }
    }

    /**
     * Initializes MyBatis SqlSessionFactory from configuration file.
     *
     * @throws RuntimeException if MyBatis initialization fails
     */
    private static void initMyBatis() {
        try (InputStream configStream = DatabaseInit.class.getClassLoader()
                .getResourceAsStream("mybatis-config.xml")) {

            if (configStream == null) {
                throw new RuntimeException("Cannot find mybatis-config.xml");
            }

            sqlSessionFactory = new SqlSessionFactoryBuilder()
                    .build(configStream);

            logger.info("MyBatis SqlSessionFactory initialized");

        } catch (Exception e) {
            logger.error("Failed to initialize MyBatis", e);
            throw new RuntimeException("Failed to initialize MyBatis", e);
        }
    }

    /**
     * Gets a new SqlSession from the MyBatis SqlSessionFactory.
     *
     * @return new SqlSession instance
     * @throws IllegalStateException if database not initialized
     */
    public static SqlSession getSqlSession() {
        if (sqlSessionFactory == null) {
            throw new IllegalStateException("Database not initialized. Call init() first.");
        }
        return sqlSessionFactory.openSession();
    }

    /**
     * Gets the MyBatis SqlSessionFactory instance.
     *
     * @return SqlSessionFactory instance
     * @throws IllegalStateException if database not initialized
     */
    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            throw new IllegalStateException("Database not initialized. Call init() first.");
        }
        return sqlSessionFactory;
    }
}
