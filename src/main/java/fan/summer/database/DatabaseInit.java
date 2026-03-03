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

public class DatabaseInit {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInit.class);

    private static SqlSessionFactory sqlSessionFactory;

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

    private static void createTables() {
        String dbPath = ".swisskit/swisskit.db";
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

    public static SqlSession getSqlSession() {
        if (sqlSessionFactory == null) {
            throw new IllegalStateException("Database not initialized. Call init() first.");
        }
        return sqlSessionFactory.openSession();
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        if (sqlSessionFactory == null) {
            throw new IllegalStateException("Database not initialized. Call init() first.");
        }
        return sqlSessionFactory;
    }
}
