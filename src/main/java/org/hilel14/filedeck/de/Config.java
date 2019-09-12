package org.hilel14.filedeck.de;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    private static final Logger LOGGER = Logger.getLogger(Config.class.getName());

    private final Path rootFolder;
    private final Path evoFolder;
    private final Path devFolder;
    private final Path qaFolder;
    private final Path mastersFolder;
    private BasicDataSource dataSource;
    private String appVersion = "unknown";
    private final String graphicsFiles;
    private final String litmusConfiguration;

    public Config(Properties properties) throws IOException {
        // set paths
        rootFolder = Paths.get(properties.getProperty("paths.root"));
        devFolder = rootFolder.resolve("Dev");
        qaFolder = rootFolder.resolve("QA");
        mastersFolder = rootFolder.resolve("Masters");
        evoFolder = Paths.get(properties.getProperty("paths.evo"));
        // SET graphics pattern
        graphicsFiles = properties.getProperty("graphics.files");
        // create data source
        createDataSource(properties);
        // get app version
        getMavenProperties();
        // get the litmus configuration        
        litmusConfiguration = properties.getProperty("litmus.configuration");
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

    /**
     * @return the rootFolder
     */
    public Path getRootFolder() {
        return rootFolder;
    }

    /**
     * @return the evoFolder
     */
    public Path getEvoFolder() {
        return evoFolder;
    }

    /**
     * @return the devFolder
     */
    public Path getDevFolder() {
        return devFolder;
    }

    /**
     * @return the qaFolder
     */
    public Path getQaFolder() {
        return qaFolder;
    }

    /**
     * @return the mastersFolder
     */
    public Path getMastersFolder() {
        return mastersFolder;
    }

    /**
     * @return the dataSource
     */
    public BasicDataSource getDataSource() {
        return dataSource;
    }

    /**
     * @return the appVersion
     */
    public String getAppVersion() {
        return appVersion;
    }

    /**
     * @return the graphicsFiles
     */
    public String getGraphicsFiles() {
        return graphicsFiles;
    }

    /**
     * @return the litmusConfiguration
     */
    public String getLitmusConfiguration() {
        return litmusConfiguration;
    }

}
