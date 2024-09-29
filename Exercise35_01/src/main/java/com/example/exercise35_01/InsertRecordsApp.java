/*Program Name: InsertRecordsApp.java
 * Authors: Austin P
 * Date last Updated: 9/29/2024
 * Purpose: This program uses both javaFX and mySQL to make a program that
 * connects this GUI to a SQL database, but unlike exercise 34_01, this program uses another
 * java class once you click the connect to database button, this GUI is under DBConnectionDialog.java
 * and is meant to just set up the connection details to a database, then returns to this GUI which allows
 * the user to either batch or not batch insert a bunch of random data points into a database table.
 */


package com.example.exercise35_01;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;

// Used to Log errors that may occur
import java.util.logging.Logger;
import java.util.logging.Level;

public class InsertRecordsApp extends Application {

    private Connection connection;
    // Logger instance for logging errors
    private static final Logger logger = Logger.getLogger(InsertRecordsApp.class.getName());
    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));


        // The UI Components for GUI, only buttons for the first gui
        Button DBConnectButton = new Button("Connect to Database");
        Button InsertDBnoBatch = new Button("Insert without Batch");
        Button InsertDBWithBatch = new Button("Insert with Batch");

        // Connect button, when pressed, opens the DB connection dialog GUI window
        // So you can connect to a database
        DBConnectButton.setOnAction(e -> connectToDatabase());

        // Inserts records without using batch updates
        InsertDBnoBatch.setOnAction(e -> insertWithoutBatch());

        // Inserts records using batch updates
        InsertDBWithBatch.setOnAction(e -> insertWithBatch());

        root.getChildren().addAll(DBConnectButton, InsertDBnoBatch, InsertDBWithBatch);

        Scene scene = new Scene(root, 300, 200);
        primaryStage.setTitle("Database Batch Update Performance");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method that connects to a database
    private void connectToDatabase() {
        DBConnectionDialog dialog = new DBConnectionDialog();
        dialog.showConnectionDialogAndWait().ifPresent(conn -> this.connection = conn);
    }

    // Method that inserts records without using any batch updates
    private void insertWithoutBatch() {
        if (connection == null) {
            System.out.println("No database connection.");
            return;
        }

        try (PreparedStatement Statements = connection.prepareStatement("INSERT INTO randomrecords (number1, number2, number3) VALUES (?, ?, ?)")) {
            Instant start = Instant.now();

            for (int i = 0; i < 1000; i++) {
                Statements.setDouble(1, Math.random());
                Statements.setDouble(2, Math.random());
                Statements.setDouble(3, Math.random());
                Statements.executeUpdate();
            }

            Instant end = Instant.now();
            System.out.println("Time taken without batch: " + Duration.between(start, end).toMillis() + " ms");
            // Logs any errors that may occur during the runtime
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    // Method that inserts a bunch of records using batch updates
    private void insertWithBatch() {
        if (connection == null) {
            System.out.println("No database connection.");
            return;
        }

        try (PreparedStatement Statements = connection.prepareStatement("INSERT INTO randomrecords (number1, number2, number3) VALUES (?, ?, ?)")) {
            Instant start = Instant.now();

            for (int i = 0; i < 1000; i++) {
                Statements.setDouble(1, Math.random());
                Statements.setDouble(2, Math.random());
                Statements.setDouble(3, Math.random());
                Statements.addBatch();

                if (i % 100 == 0) {
                    Statements.executeBatch();
                }
            }
            // Final batch execution statement
            Statements.executeBatch();

            Instant end = Instant.now();
            System.out.println("Time taken with batch: " + Duration.between(start, end).toMillis() + " ms");
            // Logs any errors that may occur during the runtime
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}