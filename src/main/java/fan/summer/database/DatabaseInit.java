package fan.summer.database;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

public class DatabaseInit {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseInit.class);

    private static final String DB_URL;

    static {
        String dbPath = Path.of(System.getProperty("user.dir"))
                .resolve(".swisskit")
                .resolve("swisskit")
                .toAbsolutePath()
                .toString()
                .replace("\\", "/");
        DB_URL = "jdbc:h2:file:" + dbPath
                + ";AUTO_SERVER=TRUE"
                + ";INIT=CREATE SCHEMA IF NOT EXISTS PUBLIC\\;SET SCHEMA PUBLIC";
    }

    private static SqlSessionFactory sqlSessionFactory;

    public static void init() {
        try {
            // 确保数据库目录存在
            Path dbDir = Path.of(System.getProperty("user.dir")).resolve(".swisskit");
            if (!Files.exists(dbDir)) {
                Files.createDirectories(dbDir);
                logger.info("Created database directory: {}", dbDir.toAbsolutePath());
            }

            createTables();
            initMyBatis();

            logger.info("Database initialization completed, url={}", DB_URL);
        } catch (Exception e) {
            logger.error("Failed to initialize database", e);
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    private static void createTables() {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("H2 driver not found", e);
        }

        // Get the path to init.sql
        String initSqlPath = DatabaseInit.class.getClassLoader()
                .getResource("init.sql").getPath();

        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {

            // Use H2 RUNSCRIPT command to execute SQL file
            stmt.execute("RUNSCRIPT FROM '" + initSqlPath + "'");
            logger.info("Database tables verified/created successfully from init.sql");

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

            // 通过 Properties 将动态 URL 注入 mybatis-config.xml 的 ${db.url} 占位符
            Properties props = new Properties();
            props.setProperty("db.url", DB_URL);

            sqlSessionFactory = new SqlSessionFactoryBuilder()
                    .build(configStream, props);

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