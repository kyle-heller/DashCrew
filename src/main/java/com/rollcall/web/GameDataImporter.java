package com.rollcall.web;

import com.rollcall.web.services.external.BggApiServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;


@Component
public class GameDataImporter implements CommandLineRunner {

    @Autowired
    private BggApiServiceImpl gameImportServiceImpl;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) {
        String csvFilePath = "src/main/resources/static/importcsvs/boardgame_top_100_owned.csv";

        // Example for PostgreSQL
        boolean tableExists = jdbcTemplate.queryForObject(
                "SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = 'games');",
                Boolean.class);

        if (Boolean.TRUE.equals(tableExists)) {
            gameImportServiceImpl.importGamesFromCsv(csvFilePath);
            System.out.println("Import completed!");
        } else {
            System.out.println("The 'games' table does not exist. Skipping import.");
        }
    }

}