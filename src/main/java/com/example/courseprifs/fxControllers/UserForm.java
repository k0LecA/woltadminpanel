package com.example.courseprifs.fxControllers;

import com.example.courseprifs.hibernateControl.GenericHibernate;
import com.example.courseprifs.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

public class UserForm implements Initializable {

    @FXML
    public RadioButton userRadio;
    @FXML
    public RadioButton restaurantRadio;
    @FXML
    public RadioButton clientRadio;
    @FXML
    public RadioButton driverRadio;
    @FXML
    public ToggleGroup Select;
    @FXML
    public TextField usernameField;
    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField nameField;
    @FXML
    public TextField surnameField;
    @FXML
    public TextField phoneField;
    @FXML
    public Button updateButton;
    @FXML
    public Button saveButton;
    @FXML
    public ComboBox vehicleTypeComboBox;
    @FXML
    public TextField streetField;
    @FXML
    public TextField cityField;
    @FXML
    public TextField postalField;
    @FXML
    public TextField licenseField;
    @FXML
    public DatePicker bdatePicker;
    @FXML
    public Button backUserButton;

    private EntityManagerFactory entityManagerFactory;
    private GenericHibernate genericHibernate;
    private User userForUpdate;
    private boolean isForUpdate;

    public void setData(EntityManagerFactory entityManagerFactory, User user, boolean isForUpdate) {
        this.entityManagerFactory = entityManagerFactory;
        this.genericHibernate = new GenericHibernate(entityManagerFactory);
        this.userForUpdate = user;
        this.isForUpdate = isForUpdate;
        fillUserDataForUpdate();
    }

//    private void fillUserDataForUpdate() {
//        if(userForUpdate != null && isForUpdate){
//            if(userForUpdate instanceof User){
//                usernameField.setText(userForUpdate.getLogin());
//                passwordField.setText(userForUpdate.getPassword());
//                nameField.setText(userForUpdate.getName());
//                surnameField.setText(userForUpdate.getSurname());
//                phoneField.setText(userForUpdate.getPhoneNumber());
//                //String user_type=userForUpdate.
//                //likusius reiktu pabaigti
//            }
//        }else{
//            updateButton.setVisible(false);
//        }
//    }
private void fillUserDataForUpdate() {
    if (userForUpdate != null && isForUpdate) {
        // Common user fields
        usernameField.setText(userForUpdate.getLogin());
        passwordField.setText(userForUpdate.getPassword());
        nameField.setText(userForUpdate.getName());
        surnameField.setText(userForUpdate.getSurname());
        phoneField.setText(userForUpdate.getPhoneNumber());

        // Detect user type
        if (userForUpdate instanceof Driver driver) {
            driverRadio.setSelected(true);
            disableFields(); // adjust which fields are active

            // Fill driver-specific data
            licenseField.setText(driver.getLicence());
            bdatePicker.setValue(driver.getBDate());
            vehicleTypeComboBox.setValue(driver.getVehicleType());

            if (driver.getAddresses() != null && !driver.getAddresses().isEmpty()) {
                Address addr = driver.getAddresses().get(0);
                streetField.setText(addr.getStreet());
                cityField.setText(addr.getCity());
                postalField.setText(addr.getPostalCode());
            }

        } else if (userForUpdate instanceof Restaurant restaurant) {
            restaurantRadio.setSelected(true);
            disableFields();

            if (restaurant.getAddresses() != null && !restaurant.getAddresses().isEmpty()) {
                Address addr = restaurant.getAddresses().get(0);
                streetField.setText(addr.getStreet());
                cityField.setText(addr.getCity());
                postalField.setText(addr.getPostalCode());
            }

        } else if (userForUpdate instanceof BasicUser client) {
            clientRadio.setSelected(true);
            disableFields();

            if (client.getAddresses() != null && !client.getAddresses().isEmpty()) {
                Address addr = client.getAddresses().get(0);
                streetField.setText(addr.getStreet());
                cityField.setText(addr.getCity());
                postalField.setText(addr.getPostalCode());
            }

        } else {
            // Plain system user (base User)
            userRadio.setSelected(true);
            disableFields();
        }
    } else {
        updateButton.setVisible(false);
    }
}


    public void disableFields() {
        if (userRadio.isSelected()) {
            userRadio.setSelected(true);
            cityField.setDisable(true);
            streetField.setDisable(true);
            postalField.setDisable(true);
            licenseField.setDisable(true);
            vehicleTypeComboBox.setDisable(true);
            bdatePicker.setDisable(true);
        } else if (restaurantRadio.isSelected()) {
            restaurantRadio.setSelected(true);
            cityField.setDisable(false);
            streetField.setDisable(false);
            postalField.setDisable(false);
            licenseField.setDisable(true);
            vehicleTypeComboBox.setDisable(true);
            bdatePicker.setDisable(true);
        } else if (clientRadio.isSelected()) {
            clientRadio.setSelected(true);
            cityField.setDisable(false);
            streetField.setDisable(false);
            postalField.setDisable(false);
            licenseField.setDisable(true);
            vehicleTypeComboBox.setDisable(true);
            bdatePicker.setDisable(true);
        } else if (driverRadio.isSelected()) {
            driverRadio.setSelected(true);
            cityField.setDisable(false);
            streetField.setDisable(false);
            postalField.setDisable(false);
            licenseField.setDisable(false);
            vehicleTypeComboBox.setDisable(false);
            bdatePicker.setDisable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        disableFields();
        //comboTest.getItems().addAll(VehicleType.values());
        vehicleTypeComboBox.getItems().addAll(VehicleType.values());
    }

    public void createNewUser() {
        try {
            if (userRadio.isSelected()) {
                // --- BASIC USER (no address info) ---
                User user = new User(
                        usernameField.getText(),
                        passwordField.getText(),
                        nameField.getText(),
                        surnameField.getText(),
                        phoneField.getText()
                );
                genericHibernate.create(user);
            }
            else if (restaurantRadio.isSelected()) {
                // --- RESTAURANT ---
                Restaurant restaurant = new Restaurant(
                        usernameField.getText(),
                        passwordField.getText(),
                        nameField.getText(),
                        surnameField.getText(),
                        phoneField.getText(),
                        "" // old address field removed, just pass empty or placeholder
                );

                // create structured address
                if (!streetField.getText().isEmpty() && !cityField.getText().isEmpty()) {
                    Address address = new Address(
                            streetField.getText(),
                            cityField.getText(),
                            postalField.getText(),
                            "Restaurant location"
                    );
                    address.setUser(restaurant);
                    restaurant.setAddresses(List.of(address));
                }

                genericHibernate.create(restaurant);
            }
            else if (clientRadio.isSelected()) {
                // --- CLIENT (BasicUser) ---
                BasicUser client = new BasicUser(
                        usernameField.getText(),
                        passwordField.getText(),
                        nameField.getText(),
                        surnameField.getText(),
                        phoneField.getText(),
                        "" // placeholder
                );

                if (!streetField.getText().isEmpty() && !cityField.getText().isEmpty()) {
                    Address address = new Address(
                            streetField.getText(),
                            cityField.getText(),
                            postalField.getText(),
                            "Client address"
                    );
                    address.setUser(client);
                    client.setAddresses(List.of(address));
                }

                genericHibernate.create(client);
            }
            else if (driverRadio.isSelected()) {
                // --- DRIVER ---
                Driver driver = new Driver(
                        usernameField.getText(),
                        passwordField.getText(),
                        nameField.getText(),
                        surnameField.getText(),
                        phoneField.getText(),
                        "", // no addressField anymore
                        licenseField.getText(),
                        bdatePicker.getValue(),
                        (VehicleType) vehicleTypeComboBox.getValue()
                );

                if (!streetField.getText().isEmpty() && !cityField.getText().isEmpty()) {
                    Address address = new Address(
                            streetField.getText(),
                            cityField.getText(),
                            postalField.getText(),
                            "Driver address"
                    );
                    address.setUser(driver);
                    driver.setAddresses(List.of(address));
                }

                genericHibernate.create(driver);
            }

            FxUtils.generateAlert(Alert.AlertType.INFORMATION, "Success", "User created successfully", "The new user has been saved.");

        } catch (Exception e) {
            e.printStackTrace();
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Failed to create user", e.getMessage());
        }
    }



    public void updateUser(ActionEvent actionEvent) {
        try {
            userForUpdate.setLogin(usernameField.getText());
            userForUpdate.setPassword(passwordField.getText());
            userForUpdate.setName(nameField.getText());
            userForUpdate.setSurname(surnameField.getText());
            userForUpdate.setPhoneNumber(phoneField.getText());
            userForUpdate.setDateUpdated(LocalDateTime.now());


            if (userForUpdate instanceof Driver driver) {
                driver.setLicence(licenseField.getText());
                driver.setVehicleType((VehicleType) vehicleTypeComboBox.getValue());
                driver.setBDate(bdatePicker.getValue());

                // update or create address
                if (driver.getAddresses() == null || driver.getAddresses().isEmpty()) {
                    Address addr = new Address(streetField.getText(), cityField.getText(), postalField.getText(), "Driver address");
                    driver.addAddress(addr);
                } else {
                    Address addr = driver.getAddresses().get(0);
                    addr.setStreet(streetField.getText());
                    addr.setCity(cityField.getText());
                    addr.setPostalCode(postalField.getText());
                }
            }

            else if (userForUpdate instanceof Restaurant restaurant) {
                if (restaurant.getAddresses() == null || restaurant.getAddresses().isEmpty()) {
                    Address addr = new Address(streetField.getText(), cityField.getText(), postalField.getText(), "Restaurant address");
                    restaurant.addAddress(addr);
                } else {
                    Address addr = restaurant.getAddresses().get(0);
                    addr.setStreet(streetField.getText());
                    addr.setCity(cityField.getText());
                    addr.setPostalCode(postalField.getText());
                }
            }

            else if (userForUpdate instanceof BasicUser client) {
                if (client.getAddresses() == null || client.getAddresses().isEmpty()) {
                    Address addr = new Address(streetField.getText(), cityField.getText(), postalField.getText(), "Client address");
                    client.addAddress(addr);
                } else {
                    Address addr = client.getAddresses().get(0);
                    addr.setStreet(streetField.getText());
                    addr.setCity(cityField.getText());
                    addr.setPostalCode(postalField.getText());
                }
            }

            genericHibernate.update(userForUpdate);

            FxUtils.generateAlert(Alert.AlertType.INFORMATION,
                    "Success",
                    "User updated",
                    "User data updated successfully!");

        } catch (Exception e) {
            e.printStackTrace();
            FxUtils.generateAlert(Alert.AlertType.ERROR,
                    "Error",
                    "Failed to update user",
                    e.getMessage());
        }
    }


    public void returnToForm(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/courseprifs/login-form.fxml"));
            Parent parent = fxmlLoader.load();

            // Get the current stage from the button
            Stage stage = (Stage) backUserButton.getScene().getWindow();

            // Set new scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error", "Navigation failed", e.getMessage());
        }
    }

}
