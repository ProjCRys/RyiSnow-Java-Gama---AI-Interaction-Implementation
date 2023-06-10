package main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

public final class SystemRequirements {
    public SystemRequirements(){
        cloneOrUpdateRepo();
        copyRunFiles();
        installRequirements();
    }
    //System
    public void cloneOrUpdateRepo() {
        String remoteUrl = "https://github.com/ading2210/poe-api.git";
        String projectPath = System.getProperty("user.dir");
        String localPath = projectPath + File.separator + "lib/poe";

        try {
            File localRepo = new File(localPath);
            Git git;
            if (localRepo.exists()) {
                git = Git.open(localRepo);
                PullCommand pull = git.pull();
                pull.setCredentialsProvider(new UsernamePasswordCredentialsProvider("username", "password"));
                boolean isUpToDate = pull.call().isSuccessful();
                if (isUpToDate) {
                    System.out.println("Repository is up to date. No changes found.");
                } else {
                    System.out.println("Repository updated successfully!");
                }
            } else {
                git = Git.cloneRepository()
                        .setURI(remoteUrl)
                        .setDirectory(localRepo)
                        .setCredentialsProvider(new UsernamePasswordCredentialsProvider("username", "password"))
                        .call();
            }
            System.out.println("Repository located at: " + localRepo.getAbsolutePath());
        } catch (GitAPIException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void copyRunFiles() {
        String projectPath = System.getProperty("user.dir");
        Path libPath = Path.of(projectPath, "lib", "runFiles");
        Path poePath = Path.of(projectPath, "lib", "poe","poe-api", "src");

        // Files to copy
        String[] filesToCopy = {"clearMessages.py", "run.py", "run_jailbreak.py", "requirements.txt"};

        try {
            if (Files.notExists(poePath)) {
                Files.createDirectories(poePath);
            }

            for (String file : filesToCopy) {
                Path sourceFile = libPath.resolve(file);
                Path destinationFile = poePath.resolve(file);
                if (Files.exists(sourceFile) && !Files.exists(destinationFile)) {
                    Files.copy(sourceFile, destinationFile, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Copied file: " + sourceFile.getFileName());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void installRequirements() {
        String projectPath = System.getProperty("user.dir");
        Path requirementsPath = Path.of(projectPath, "lib", "poe", "poe-api", "src");

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("pip", "install", "--upgrade", "--no-cache-dir", "-r", "requirements.txt");
            processBuilder.directory(requirementsPath.toFile());

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Requirements installed successfully!");
            } else {
                System.out.println("Failed to install requirements.");
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}