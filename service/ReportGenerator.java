package service;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

public final class ReportGenerator {

    private ReportGenerator() {
    }

    public static String generate(Path logsFolder, Path reportFile)
            throws IOException {

        long totalDeposits = 0;
        long totalWithdrawals = 0;

        StringBuilder report =
                new StringBuilder("End-of-day transaction report\n");

        try (DirectoryStream<Path> files =
                     Files.newDirectoryStream(logsFolder)) {

            for (Path file : files) {

                if (!Files.isRegularFile(file)) {
                    continue;
                }

                BasicFileAttributes attributes =
                        Files.readAttributes(file, BasicFileAttributes.class);

                report.append("\nFile: ").append(file.getFileName())
                        .append("\nSize: ").append(attributes.size())
                        .append(" bytes")
                        .append("\nLast modified: ")
                        .append(attributes.lastModifiedTime())
                        .append('\n');

                try (BufferedReader reader = Files.newBufferedReader(
                        file, StandardCharsets.UTF_8)) {

                    String line;
                    int lineNumber = 0;

                    while ((line = reader.readLine()) != null) {
                        lineNumber++;

                        String[] fields = line.split(",", -1);

                        if (fields.length != 2) {
                            throw new IOException(
                                    "Invalid transaction log entry in "
                                            + file + " at line " + lineNumber
                                            + ": " + line);
                        }

                        long amount;

                        try {
                            amount = Long.parseLong(fields[1]);
                        } catch (NumberFormatException e) {
                            throw new IOException(
                                    "Invalid transaction amount in "
                                            + file + " at line " + lineNumber
                                            + ": " + line, e);
                        }

                        if (amount <= 0) {
                            throw new IOException(
                                    "Transaction amount must be positive in "
                                            + file + " at line " + lineNumber
                                            + ".");
                        }

                        switch (fields[0]) {
                            case "DEPOSIT" ->
                                    totalDeposits = Math.addExact(
                                            totalDeposits, amount);

                            case "WITHDRAW" ->
                                    totalWithdrawals = Math.addExact(
                                            totalWithdrawals, amount);

                            default -> throw new IOException(
                                    "Unknown transaction type in "
                                            + file + " at line " + lineNumber
                                            + ": " + fields[0]);
                        }
                    }
                }
            }
        }

        report.append("\nTotal deposits: ").append(totalDeposits)
                .append("\nTotal withdrawals: ").append(totalWithdrawals)
                .append("\nNet transactions: ")
                .append(totalDeposits - totalWithdrawals)
                .append('\n');

        String summary = report.toString();

        Path parent = reportFile.toAbsolutePath().getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.writeString(
                reportFile, summary, StandardCharsets.UTF_8);

        return summary;
    }
}
