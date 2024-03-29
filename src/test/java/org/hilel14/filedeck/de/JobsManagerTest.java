package org.hilel14.filedeck.de;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author hilel14
 */
public class JobsManagerTest {

    Config config;

    public JobsManagerTest() throws IOException, SQLException {
        createConfig();
        databaseSetup();
        fileSystemSetup();
    }

    @BeforeAll
    public static void setUpClass() throws IOException {

    }

    @AfterAll
    public static void tearDownClass() {

    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    private void createConfig() throws IOException {
        Properties properties = Main.getProperties();
        properties.put("db.driver", "org.h2.Driver");
        properties.put("db.url", "jdbc:h2:mem:filedeck");
        Path temp = Files.createTempDirectory("filedeck.test.");
        properties.put("paths.root", temp.resolve("root").toString());
        properties.put("paths.evo", temp.resolve("evo").toString());
        this.config = new Config(properties);
    }

    private void databaseSetup() throws IOException, SQLException {
        try (InputStream inStream = JobsManagerTest.class.getClassLoader().getResourceAsStream("create-tables.h2.sql");
                Connection connection = config.getDataSource().getConnection();) {
            PreparedStatement statement = connection.prepareStatement(new String(inStream.readAllBytes()));
            statement.executeUpdate();
        }
    }

    private void fileSystemSetup() throws IOException {
        // create basic directory tree
        Files.createDirectories(config.getMastersFolder());
        Files.createDirectories(config.getDevFolder());
        Files.createDirectories(config.getQaFolder());
        Files.createDirectories(config.getEvoFolder());
        // read list of test files
        Path list = config.getMastersFolder().resolve("list");
        try (InputStream inStream = JobsManagerTest.class.getClassLoader().getResourceAsStream("masters.txt")) {
            FileOutputStream outStream = new FileOutputStream(list.toFile());
            outStream.write(inStream.readAllBytes());
        }
        List<String> files = Files.readAllLines(list);
        // create test files
        for (String file : files) {
            Path out = config.getMastersFolder().getParent().resolve(file);
            //System.out.println(out);
            Files.createDirectories(out.getParent());
            Files.createFile(out);
        }
    }

    /**
     * Test of createJob method, of class JobsManager.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testCreateJob() throws Exception {
        // completly new job
        Job job = new Job();
        job.setPaperCode("002001");
        job.setUserName("test");
        JobsManager instance = new JobsManager(config);
        instance.createJob(job);
        Path path = config.getDevFolder().resolve("002").resolve("002001");
        assertTrue(Files.exists(path));
        // new version of an existing job
        job = new Job();
        job.setPaperCode("001001");
        job.setBaseJob("001001");
        job.setBaseVersion("v_02");
        job.setUserName("test");
        instance = new JobsManager(config);
        instance.createJob(job);
        path = config.getDevFolder().resolve("001").resolve("001001").resolve("001001-v2.indd");
        assertTrue(Files.exists(path));
        path = config.getDevFolder().resolve("001").resolve("001001").resolve("zevel-1.doc");
        assertFalse(Files.exists(path));
        for (String folder : new String[]{"inbox", "outbox", "press"}) {
            path = config.getDevFolder().resolve("001").resolve("001001").resolve(folder);
            assertTrue(path.toFile().list().length == 0);
        }
        // new job based on another job with envelopes
        job = new Job();
        job.setPaperCode("002002");
        job.setBaseJob("001002");
        job.setBaseVersion("v_01");
        job.getEnvelopes().add("env11");
        job.setUserName("test");
        instance = new JobsManager(config);
        instance.createJob(job);
        path = config.getDevFolder().resolve("002").resolve("002002").resolve("envelopes").resolve("env11");
        assertTrue(Files.exists(path.resolve("002002-page1.indd")));
    }

    /**
     * Test of moveJobToQa and releaseVersion, methods, of class JobsManager.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testReleaseJob() throws Exception {
        // start with a new job
        Job job = new Job();
        job.setPaperCode("003001");
        job.setUserName("test");
        JobsManager instance = new JobsManager(config);
        instance.createJob(job);
        // move job to qa
        instance.moveJobToQa(job.getPaperCode());
        Path path = config.getDevFolder().resolve("003").resolve("003001");
        assertFalse(Files.exists(path));
        path = config.getQaFolder().resolve("003").resolve("003001");
        assertTrue(Files.exists(path));
        // release version
        instance.releaseVersion(job.getPaperCode());
        path = config.getMastersFolder().resolve("003").resolve("003001").resolve("v_01");
        assertTrue(Files.exists(path));
    }

}
