package org.hilel14.filedeck.de;

import java.awt.Desktop;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hilel14
 */
public class JobsManager {

    public static final Logger LOGGER = Logger.getLogger(JobsManager.class.getName());
    Config config;
    DataTool dataTool;

    public JobsManager(Config config) {
        this.config = config;
        this.dataTool = new DataTool(config.getDataSource());
    }

    public void createJob(Job job) throws IOException, SQLException {
        // add job to database
        dataTool.createJob(job.getPaperCode(), job.getUserName());
        // create empty directory layout for main job and envelopes
        Path jobFolder = findJobFolder(job.getPaperCode(), "dev");
        createDirectoryLayout(jobFolder);
        for (String envelope : job.getEnvelopes()) {
            Path envelopeFolder = jobFolder.resolve("envelopes").resolve(envelope);
            createDirectoryLayout(envelopeFolder);
        }
        // copy files from base job to main job and envelopes
        if (job.getBaseJob() != null) {
            Path baseJobFolder = findJobFolder(job.getBaseJob(), "masters").resolve(job.getBaseVersion());
            copyFromBase(baseJobFolder, job.getBaseJob(), jobFolder, job.getPaperCode());
            for (String envelope : job.getEnvelopes()) {
                Path baseEnvelopeFolder = baseJobFolder.resolve("envelopes").resolve(envelope);
                if (Files.exists(baseEnvelopeFolder)) {
                    Path envelopeFolder = jobFolder.resolve("envelopes").resolve(envelope);
                    copyFromBase(baseEnvelopeFolder, job.getBaseJob(), envelopeFolder, job.getPaperCode());
                }
            }
        }
    }

    private void copyFromBase(Path sourceFolder, String sourceName, Path targetFolder, String targetName) throws IOException {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceFolder, config.getGraphicsFiles())) {
            for (Path source : stream) {
                String fileName = source.getFileName().toString().replace(sourceName, targetName);
                Files.copy(source, targetFolder.resolve(fileName));
            }
        }
    }

    private void createDirectoryLayout(Path root) throws IOException {
        Files.createDirectories(root);
        Files.createDirectory(root.resolve("inbox"));
        Files.createDirectory(root.resolve("outbox"));
        Files.createDirectory(root.resolve("press"));
    }

    public void moveJobToQa(String paperCode) throws IOException, SQLException {
        Path source = findJobFolder(paperCode, "dev");
        Path target = findJobFolder(paperCode, "qa");
        Files.createDirectories(target.getParent());
        Files.move(source, target);
        dataTool.updateJobStatus(paperCode, "qa");
    }

    public void moveJobToDev(String paperCode) throws IOException, SQLException {
        Path source = findJobFolder(paperCode, "qa");
        Path target = findJobFolder(paperCode, "dev");
        Files.createDirectories(target.getParent());
        Files.move(source, target);
        dataTool.updateJobStatus(paperCode, "dev");
    }

    public void deleteJob(String paperCode, String jobStatus) throws IOException, SQLException {
        // delete job record from database
        dataTool.deleteJob(paperCode);
        // delete job folder
        Path target = findJobFolder(paperCode, jobStatus);
        deleteFolder(target);
    }

    public void copyToEvo(String paperCode) throws IOException {
        Path sourceFolder = findJobFolder(paperCode, "qa").resolve("press");
        Path targetFolder = config.getEvoFolder().resolve(paperCode);
        Files.createDirectories(targetFolder);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(sourceFolder)) {
            for (Path source : stream) {
                Files.copy(source, targetFolder.resolve(source.getFileName()));
            }
        }
    }

    public void releaseVersion(String paperCode) throws IOException, SQLException {
        Path sourceFolder = findJobFolder(paperCode, "qa");
        // delete old pdf folder from outbox
        Path old = sourceFolder.resolve("outobx").resolve("old");
        if (Files.exists(old)) {
            deleteFolder(old);
        }
        // delete old pdf folder from outbox in envelopes
        Path envFolder = sourceFolder.resolve("envelopes");
        if (Files.exists(envFolder)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(envFolder)) {
                for (Path source : stream) {
                    if (Files.isDirectory(source) && source.getFileName().toString().contains("env")) {
                        old = source.resolve("outobx").resolve("old");
                        if (Files.exists(old)) {
                            deleteFolder(old);
                        }
                    }
                }
            }
        }
        // move job folder from QA to Masters
        Path targetFolder = findJobFolder(paperCode, "masters");
        String nextVersion = findNextVersion(paperCode);
        Files.createDirectories(targetFolder);
        Files.move(sourceFolder, targetFolder.resolve(nextVersion));
        // delete record from jobs table
        dataTool.deleteJob(paperCode);
    }

    public String[] getAllStatusCodes() {
        String[] statusCodes = new String[]{"any", "dev", "qa"};
        return statusCodes;
    }

    public Path findJobFolder(String paperCode, String jobStatus) {
        Path base;
        switch (jobStatus) {
            case "dev":
                base = config.getDevFolder();
                break;
            case "qa":
                base = config.getQaFolder();
                break;
            case "masters":
                base = config.getMastersFolder();
                break;
            default:
                LOGGER.log(Level.WARNING, "{0} is not a valid job status code", jobStatus);
                throw new java.lang.IllegalArgumentException("Invalid status code: " + jobStatus);
        }
        return base.resolve(paperCode.substring(0, 3)).resolve(paperCode);
    }

    public List<String> getVersions(String paperCode) throws IOException {
        Path jobFolder = findJobFolder(paperCode, "masters");
        List<String> versions = new ArrayList<>();
        if (Files.exists(jobFolder)) {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(jobFolder)) {
                for (Path source : stream) {
                    if (Files.isDirectory(source)) {
                        if (source.getFileName().toString().startsWith("v_")) {
                            versions.add(source.getFileName().toString());
                        }
                    }
                }
            }
        }
        Collections.sort(versions);
        return versions;
    }

    private String findNextVersion(String paperCode) throws IOException {
        List<String> versions = getVersions(paperCode);
        if (versions.isEmpty()) {
            return "v_01";
        }
        String lastVersion = versions.get(versions.size() - 1);
        int nextVersion = Integer.parseInt(lastVersion.substring(2)) + 1;
        return "v_" + addLeadinZeros(nextVersion, 2);
    }

    private String addLeadinZeros(int n, int max) {
        String s = String.valueOf(n);
        while (s.length() < max) {
            s = "0" + s;
        }
        return s;
    }

    private void deleteFolder(Path path) throws IOException {
        if (Files.exists(path)) {
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }

    public void openJobFolder(String paperCode, String jobStatus) throws IOException {
        Path target = findJobFolder(paperCode, jobStatus);
        Desktop.getDesktop().open(target.toFile());
    }
}
