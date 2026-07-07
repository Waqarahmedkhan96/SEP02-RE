package utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseSeeder {
    private static final String SEED_SCRIPT = "/db/seed_test_data.sql";

    public static void seedTestData() throws SQLException, IOException {
        String sql = readSeedScript();

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            for (String command : sql.split(";")) {
                String trimmedCommand = command.trim();
                if (!trimmedCommand.isEmpty()) {
                    statement.execute(trimmedCommand);
                }
            }
        }
    }

    private static String readSeedScript() throws IOException {
        try (InputStream stream = DatabaseSeeder.class.getResourceAsStream(SEED_SCRIPT)) {
            if (stream == null) {
                throw new IOException("Seed script not found: " + SEED_SCRIPT);
            }
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
