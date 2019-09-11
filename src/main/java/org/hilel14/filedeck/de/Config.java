package org.hilel14.filedeck.de;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;
import org.hilel14.filedeck.de.gui.SettingsDialog;

/**
 *
 * @author hilel14
 */
public class Config {

    public static final Logger LOGGER = Logger.getLogger(Config.class.getName());
    public static Config config;

    public Path appDataFolder = Paths.get(System.getProperty("user.home")).resolve(".beeri");
    public Path rootFolder;
    public Path evoFolder;
    public Path devFolder;
    public Path qaFolder;
    public Path mastersFolder;
    public BasicDataSource dataSource;
    public String appVersion = "unknown";
    public String litmusConfiguration;

    public static Config getInstance() {
        if (config == null) {
            try {
                config = new Config();
            } catch (IOException ex) {
                LOGGER.log(Level.SEVERE, null, ex);
            }
        }
        return config;
    }

    private Config() throws IOException {
        Properties props = getAppProperties();
        // set paths
        rootFolder = Paths.get(props.getProperty("paths.root"));
        devFolder = rootFolder.resolve("Dev");
        qaFolder = rootFolder.resolve("QA");
        mastersFolder = rootFolder.resolve("Masters");
        evoFolder = Paths.get(props.getProperty("paths.evo"));
        // create data source
        createDataSource(props);
        // get app version
        getMavenProperties();
        // get the litmus configuration        
        litmusConfiguration = props.getProperty("litmus.configuration");
    }

    private Properties getAppProperties() throws IOException {
        Properties props = new Properties();
        Path settingsFile = appDataFolder.resolve("filedeck-settings.xml");
        LOGGER.log(Level.INFO, "Loading application properties from {0}", settingsFile);
        if (!Files.exists(settingsFile)) {
            Files.createDirectories(settingsFile.getParent());
            InputStream in = Config.class.getClassLoader().getResourceAsStream("default-settings.xml");
            props.loadFromXML(in);
            try (OutputStream out = new FileOutputStream(settingsFile.toFile())) {
                props.storeToXML(out, "default settings for filedeck desktop application " + new Date());
            }
        }
        try (InputStream in = new FileInputStream(settingsFile.toFile())) {
            props.loadFromXML(in);
        }
        return props;
    }

    private void createDataSource(Properties props) {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName(props.getProperty("db.driver"));
        dataSource.setUrl(props.getProperty("db.url"));
        dataSource.setUsername(props.getProperty("db.user"));
        dataSource.setPassword(props.getProperty("db.password"));
        dataSource.setMinIdle(5);
        dataSource.setMaxIdle(10);
        dataSource.setMaxOpenPreparedStatements(100);
        dataSource.setValidationQuery("select 1");
        LOGGER.log(Level.INFO, "Database connection string: {0}", props.getProperty("db.url"));
    }

    /**
     * Get values from maven pom.properties. this will only work after the
     * package phase.
     *
     */
    private void getMavenProperties() throws IOException {
        Properties props = new Properties();
        String resourceName = "META-INF/maven/org.hilel14/filedeck-de/pom.properties";
        InputStream inStream = SettingsDialog.class.getClassLoader().getResourceAsStream(resourceName);
        if (inStream == null) {
            LOGGER.log(Level.WARNING, "Unable to load {0}", resourceName);
        } else {
            props.load(inStream);
            LOGGER.log(Level.INFO, "Group ID: {0}", props.getProperty("groupId"));
            LOGGER.log(Level.INFO, "Artifact ID: {0}", props.getProperty("artifactId"));
            LOGGER.log(Level.INFO, "Version: {0}", props.getProperty("version"));
            this.appVersion = props.getProperty("version");
        }
    }

}
