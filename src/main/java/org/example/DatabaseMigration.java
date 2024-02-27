package org.example;

import org.flywaydb.core.Flyway;

public class DatabaseMigration {

    public static void main(String[] args) {
        // Create Flyway instance
        Flyway flyway = Flyway.configure()
                .dataSource("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", "sa", "")
                .locations("classpath:db/migration")//ya'll can modify this shi if ya have diferent directory
                .load();

        flyway.migrate(); //lezzzzgooooo
    }
}
