/*Program Name: InsertRecordsApp.java
 * Authors: Austin P
 * Date last Updated: 9/29/2024
 * Purpose: Works in conjunction with InsertRecordsApp.java and this file is meant to set up
 * the connection between the GUI and a mySQL database.
*/
package com.example.exercise35_01;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
// Used to Log errors that may occur
import java.util.logging.Logger;
import java.util.logging.Level;

public class DBConnectionDialog extends Stage {
    // Database connection UI fields
    private final TextField inputHostName;
    private final  TextField inputDBName;
    private final TextField inputUserName;
    private final PasswordField inputPassword;
    private Connection connection;
    // Logger instance for logging errors
    private static final Logger logger = Logger.getLogger(DBConnectionDialog.class.getName());

    public DBConnectionDialog() {
        // The UI Components for GUI, title, the text boxes and buttons

        setTitle("Database Connection GUI");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        // Hostname input box
        Label hostLabel = new Label("Host:");
        inputHostName = new TextField("localhost");
        grid.add(hostLabel, 0, 0);
        grid.add(inputHostName, 1, 0);

        // Database name input box
        Label dbLabel = new Label("Database:");
        inputDBName = new TextField();
        grid.add(dbLabel, 0, 1);
        grid.add(inputDBName, 1, 1);

        // Username input box
        Label userLabel = new Label("Username:");
        inputUserName = new TextField();
        grid.add(userLabel, 0, 2);
        grid.add(inputUserName, 1, 2);

        // Password input box
        Label passwordLabel = new Label("Password:");
        inputPassword = new PasswordField();
        grid.add(passwordLabel, 0, 3);
        grid.add(inputPassword, 1, 3);

        // Connect button that calls the connectToDatabase method
        Button connectButton = new Button("Connect");
        connectButton.setOnAction(e -> connectToDatabase());
        grid.add(connectButton, 1, 4);

        // Set the dialog box properties
        Scene scene = new Scene(grid);
        setScene(scene);
        initModality(Modality.APPLICATION_MODAL);
    }
    // Method that uses the data inputted to try and connect to a database
    // If failure occurs, output and error
    private void connectToDatabase() {
        String host = inputHostName.getText();
        String dbName = inputDBName.getText();
        String user = inputUserName.getText();
        String password = inputPassword.getText();

        String url = "jdbc:mysql://" + host + "/" + dbName;

        try {
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the database successfully!");
            close();
            // Logs any errors that may occur during the runtime
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Database connection failed", e);
        }
    }

    // Method that tries to avoid name clashing with the Stage's showAndWait()
    public Optional<Connection> showConnectionDialogAndWait() {
        // Call the original showAndWait() from Stage
        super.showAndWait();
        return Optional.ofNullable(connection);
    }
}
