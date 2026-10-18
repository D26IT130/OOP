
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.Arrays;

public class LogAnalyzer {

    public static void main(String[] args) {

        String keyword = "ERROR";

        Path file1 = Paths.get("app1.txt");
        Path file2 = Paths.get("app2.txt");
        Path file3 = Paths.get("app3.txt");

        try {
            Files.write(file1, Arrays.asList(
                "INFO: Application started",
                "INFO: User logged in",
                "ERROR: Database connection failed",
                "INFO: Retrying connection",
                "ERROR: Database timeout"
            ));

            Files.write(file2, Arrays.asList(
                "INFO: Server started",
                "ERROR: Unable to load configuration",
                "INFO: Configuration loaded",
                "ERROR: Server connection failed"
            ));

            Files.write(file3, Arrays.asList(
                "INFO: User registered",
                "ERROR: Email server failed",
                "WARNING: Disk space is low",
                "ERROR: File not found"
            ));

            Path[] files = {file1, file2, file3};

            int totalLines = 0;
            int totalErrors = 0;

            for (Path file : files) {

                List<String> lines = Files.readAllLines(file);
                int fileErrors = 0;

                for (String line : lines) {
                    totalLines++;

                    if (line.contains(keyword)) {
                        fileErrors++;
                        totalErrors++;
                    }
                }

                BasicFileAttributes attributes =
                    Files.readAttributes(
                        file,
                        BasicFileAttributes.class
                    );

                System.out.println("File: " + file);
                System.out.println("Errors: " + fileErrors);
                System.out.println("Size: " + attributes.size() + " bytes");
                System.out.println();
            }

            System.out.println("===== LOG ANALYSIS =====");
            System.out.println("Total lines: " + totalLines);
            System.out.println("Total errors: " + totalErrors);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
