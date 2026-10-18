# MiniBank

MiniBank is a console-based Java banking application demonstrating checked
banking exceptions, a reflection-backed repository, synchronized account
operations, batch processing, serialization, and end-of-day reporting.

## Requirements

- JDK 17 or later
- Maven 3.8 or later

## Build and test

From the project root:

```powershell
mvn test
mvn package
```

The runnable JAR is created at `target/minibank-1.0.0.jar`.

## Run

```powershell
java -jar target/minibank-1.0.0.jar
```

The menu supports opening savings, current, and fixed-deposit accounts; deposits;
withdrawals; transfers; statements; account listing; transaction batches; and
an end-of-day report. Account data is loaded at startup and saved when the
application exits.

## Data files

- `data/accounts.dat` stores serialized accounts.
- `data/logs/transactions.log` is the append-only transaction log.
- `data/end-of-day-report.txt` stores the generated report.

Batch entries use the format `ACCOUNT_NUMBER DEPOSIT|WITHDRAW AMOUNT`.
Reports total successful deposits and withdrawals from the transaction logs.
