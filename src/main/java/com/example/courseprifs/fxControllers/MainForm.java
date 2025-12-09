package com.example.courseprifs.fxControllers;

import com.example.courseprifs.HelloApplication;
import com.example.courseprifs.hibernateControl.CustomHibernate;
import com.example.courseprifs.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class MainForm implements Initializable {
    @FXML
    public Tab userTab;
    @FXML
    public Tab managementTab;
    @FXML
    public Tab foodTab;
    @FXML
    public ListView<User> userListField;
    @FXML //Laikinas, galutineje versijoje jo nebus
    public Tab altTab;
    @FXML
    public TabPane tabsPane;
    //<editor-fold desc="User Tab Elements">
    @FXML
    public TableView<UserTableParameters> userTable;
    @FXML
    public TableColumn<UserTableParameters, Integer> idCol;
    @FXML
    public TableColumn<UserTableParameters, String> userTypeCol;
    @FXML
    public TableColumn<UserTableParameters, String> loginCol;
    @FXML
    public TableColumn<UserTableParameters, String> passCol;
    @FXML
    public TableColumn<UserTableParameters, String> nameCol;
    @FXML
    public TableColumn<UserTableParameters, String> surnameCol;
    @FXML
    public TableColumn<UserTableParameters, String> addrCol;
    @FXML
    public TableColumn<UserTableParameters, Void> dummyCol;
    @FXML
    public TableColumn<UserTableParameters, String> phoneCol;
    @FXML
    public ListView<Restaurant> restaurantListView;


    private ObservableList<UserTableParameters> data = FXCollections.observableArrayList();

    //</editor-fold>
    //<editor-fold desc="Order Tab Elements">
    public ListView<FoodOrder> ordersList;
    public TextField titleField;
    public ComboBox<BasicUser> clientList;
    public TextField priceField;
    public ComboBox<Restaurant> restaurantField;
    public ListView<BasicUser> basicUserList;
    public ComboBox<OrderStatus> orderStatusField;
    public ComboBox<OrderStatus> filterStatus;
    public ComboBox<BasicUser> filterClients;
    public DatePicker filterFrom;
    public DatePicker filterTo;
    public ListView<Cuisine> foodList;
    //</editor-fold>
    //<editor-fold desc="Cuisine Tab Elements">
    public TextField titleCuisineField;
    public TextArea ingredientsField;
    public ListView<Restaurant> restaurantList;
    public TextField cuisinePriceField;
    public CheckBox isDeadly;
    public CheckBox isVegan;
    public ListView<Cuisine> cuisineList;
    //</editor-fold>

    //<editor-fold desc="Admin chat Elements">
    public Tab chatTab;
    public ListView<Chat> allChats;
    public ListView<Message> chatMessages;
    //</editor-fold>


    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;
    private User currentUser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //<editor-fold desc="UserTable">
        userTable.setEditable(true);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        userTypeCol.setCellValueFactory(new PropertyValueFactory<>("userType"));
        loginCol.setCellValueFactory(new PropertyValueFactory<>("login"));
        //pass
        passCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        passCol.setCellFactory(TextFieldTableCell.forTableColumn());
        passCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPassword(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setPassword(event.getNewValue());
            customHibernate.update(user);
        });
        //name
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setName(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setName(event.getNewValue());
            customHibernate.update(user);
        });
        //surname
        surnameCol.setCellValueFactory(new PropertyValueFactory<>("surname"));
        surnameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setSurname(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setSurname(event.getNewValue());
            customHibernate.update(user);
        });
        //phone
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setOnEditCommit(event -> {
            event.getTableView().getItems().get(event.getTablePosition().getRow()).setPhoneNum(event.getNewValue());
            User user = customHibernate.getEntityById(User.class, event.getTableView().getItems().get(event.getTablePosition().getRow()).getId());
            user.setPhoneNumber(event.getNewValue());
            customHibernate.update(user);
        });

        addrCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        //</editor-fold>

        



        //pabaigti likusius stulpelius
//        addrCol.setCellValueFactory(new PropertyValueFactory<>(""));

        //reloadTableData();
        //reloadRestaurantTable();
        //loadRestaurants();
    }

    public void setData(EntityManagerFactory entityManagerFactory, User user) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = user;
        this.customHibernate = new CustomHibernate(entityManagerFactory);
        setUserFormVisibility();
        reloadTableData();
        loadRestaurants();
    }

    private void setUserFormVisibility() {
        if (currentUser instanceof User) {
            //turbut nieko nedarom, gal kazka custom

        } else if (currentUser instanceof Restaurant) {
//            altTab.setDisable(true);
            tabsPane.getTabs().remove(altTab); //Man net nesugeneruos sito tabo
        }

    }

    //<editor-fold desc="User Tab functionality">
    public void reloadTableData() {
        if (userTab.isSelected()) {
            data.clear();
            List<User> users = customHibernate.getAllRecords(User.class);
            for (User u : users) {
                UserTableParameters userTableParameters = new UserTableParameters();
                userTableParameters.setId(u.getId());
                userTableParameters.setUserType(u.getClass().getSimpleName());
                userTableParameters.setLogin(u.getLogin());
                userTableParameters.setPassword(u.getPassword());
                userTableParameters.setName(u.getName());
                userTableParameters.setSurname(u.getSurname());
                userTableParameters.setPhoneNum(u.getPhoneNumber());
                //baigti bendrus laukus
                if (u instanceof BasicUser basicUser) {
                    String allAddresses = basicUser.getAddresses()
                            .stream()
                            .map(Address::toString)
                            .collect(Collectors.joining(" | "));
                    userTableParameters.setAddress(allAddresses);
                }
                if (u instanceof Restaurant restaurant) {
                    String allAddresses = restaurant.getAddresses()
                            .stream()
                            .map(Address::toString)
                            .collect(Collectors.joining(" | "));
                    userTableParameters.setAddress(allAddresses);
                }
                if (u instanceof Driver driver) {
                    String allAddresses = driver.getAddresses()
                            .stream()
                            .map(Address::toString)
                            .collect(Collectors.joining(" | "));
                    userTableParameters.setAddress(allAddresses);
                }
                data.add(userTableParameters);
            }
            userTable.getItems().clear();
            userTable.getItems().addAll(data);
        } else if (managementTab.isSelected()) {
            clearAllOrderFields();
            List<FoodOrder> foodOrders = getFoodOrders();
            ordersList.getItems().addAll(foodOrders);
            //double check kodel rodo per daug vartotoju
            clientList.getItems().addAll(customHibernate.getAllRecords(BasicUser.class));
            //jei dirbsit su ListView:
            basicUserList.getItems().addAll(customHibernate.getAllRecords(BasicUser.class));
            restaurantField.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
            orderStatusField.getItems().addAll(OrderStatus.values());
        } else if (altTab.isSelected()) {
            List<User> userList = customHibernate.getAllRecords(User.class);
            userListField.getItems().addAll(userList);
        } else if (foodTab.isSelected()) {
            clearAllCuisineFields();
            restaurantList.getItems().addAll(customHibernate.getAllRecords(Restaurant.class));
        } else if (chatTab.isSelected()) {
            loadAllChats();

        }
        //pabaigt
    }

    public void loadRestaurants() {
        List<Restaurant> restaurants = customHibernate.getAllRecords(Restaurant.class);
        ObservableList<Restaurant> restaurantData = FXCollections.observableArrayList(restaurants);
        restaurantListView.setItems(restaurantData);
    }


    private void clearAllOrderFields() {
        //turbut reik salygos sakiniu
        ordersList.getItems().clear();
        basicUserList.getItems().clear();
        clientList.getItems().clear();
        restaurantField.getItems().clear();
        titleField.clear();
        priceField.clear();
    }

    private void clearAllCuisineFields() {
        foodList.getItems().clear();
        cuisinePriceField.clear();
        titleCuisineField.clear();
        ingredientsField.clear();
        isDeadly.setSelected(false);
        isVegan.setSelected(false);
        restaurantList.getItems().clear();
    }
    //</editor-fold>

    //<editor-fold desc="Alternative Tab Functions">

    public void addUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, null, false);


        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void loadUser(ActionEvent actionEvent) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("user-form.fxml"));
        Parent parent = fxmlLoader.load();

        User selectedUser = userListField.getSelectionModel().getSelectedItem();

        UserForm userForm = fxmlLoader.getController();
        userForm.setData(entityManagerFactory, selectedUser, true);


        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.showAndWait();
    }

    public void deleteUser() {
        User selectedUser = userListField.getSelectionModel().getSelectedItem();
        customHibernate.delete(User.class, selectedUser.getId());
    }

    //</editor-fold>

    //<editor-fold desc="Order Tab functionality">
    private List<FoodOrder> getFoodOrders() {
        if (currentUser instanceof Restaurant) {
            return customHibernate.getRestaurantOrders((Restaurant) currentUser);
        } else {
            return customHibernate.getAllRecords(FoodOrder.class);
        }
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

//    public void createOrder() {
//        if (isNumeric(priceField.getText())) {
////            FoodOrder foodOrder = new FoodOrder(titleField.getText(), Double.parseDouble(priceField.getText()), clientList.getValue(), restaurantField.getValue());
//            //FoodOrder foodOrder = new FoodOrder(titleField.getText(), Double.parseDouble(priceField.getText()), clientList.getValue(), foodList.getSelectionModel().getSelectedItems(), restaurantField.getValue());
//            //customHibernate.create(foodOrder);
//
//            //Alternatyvus būdas:
////            FoodOrder foodOrder2 = new FoodOrder(titleField.getText(), Double.parseDouble(priceField.getText()), basicUserList.getSelectionModel().getSelectedItem(), restaurantField.getValue());
////            customHibernate.create(foodOrder2);
//            //fillOrderLists();
//        }
//    }
    public void createOrder() {
        try {
            // Validate numeric input
            if (!isNumeric(priceField.getText())) {
                FxUtils.generateAlert(Alert.AlertType.WARNING,
                        "Invalid price",
                        "Price must be numeric",
                        "Please enter a valid price.");
                return;
            }

            // Validate selections
            BasicUser buyer = clientList.getSelectionModel().getSelectedItem();
            Restaurant restaurant = restaurantField.getSelectionModel().getSelectedItem();
            ObservableList<Cuisine> selectedItems = foodList.getSelectionModel().getSelectedItems();

            if (buyer == null || restaurant == null || selectedItems.isEmpty()) {
                FxUtils.generateAlert(Alert.AlertType.WARNING,
                        "Missing data",
                        "Incomplete order details",
                        "Select a buyer, restaurant, and at least one cuisine item.");
                return;
            }

            // Calculate total item price
            double totalPrice = selectedItems.stream()
                    .mapToDouble(Cuisine::getPrice)
                    .sum();

            // Optional delivery fee — can be fixed or based on distance later
            double deliveryFee = 5.0;

            // Choose delivery address (for now first buyer address)
            Address deliveryAddress = null;
            if (buyer.getAddresses() != null && !buyer.getAddresses().isEmpty()) {
                deliveryAddress = buyer.getAddresses().get(0);
            }

            // --- Create FoodOrder entity ---
            FoodOrder order = new FoodOrder(
                    titleField.getText(),
                    deliveryFee,
                    buyer,
                    restaurant,
                    deliveryAddress,
                    OrderStatus.PENDING
            );
            order.setPrice(totalPrice + deliveryFee);

            // --- Create OrderItems ---
            List<OrderItem> orderItems = new ArrayList<>();
            for (Cuisine cuisine : selectedItems) {
                OrderItem item = new OrderItem(order, cuisine, 1, cuisine.getPrice(), "");
                orderItems.add(item);
            }
            order.setItemList(orderItems);

            // --- Create a chat for this order ---
            Chat chat = new Chat("Chat for order: " + order.getName(), order);
            order.setChat(chat);

            // --- Persist order ---
            customHibernate.create(order);

            // Refresh the order list in UI
            fillOrderLists();

            FxUtils.generateAlert(Alert.AlertType.INFORMATION,
                    "Success",
                    "Order created",
                    "The order was successfully created and saved.");

        } catch (Exception e) {
            e.printStackTrace();
            FxUtils.generateAlert(Alert.AlertType.ERROR,
                    "Error",
                    "Order creation failed",
                    e.getMessage());
        }
    }


    public void updateOrder() {
        FoodOrder foodOrder = ordersList.getSelectionModel().getSelectedItem();
        foodOrder.setRestaurant(restaurantField.getSelectionModel().getSelectedItem());
        foodOrder.setName(titleField.getText());
        foodOrder.setPrice(Double.valueOf(priceField.getText()));
        foodOrder.setOrderStatus(orderStatusField.getValue());
        foodOrder.setBuyer(clientList.getSelectionModel().getSelectedItem());

        customHibernate.update(foodOrder);
        //SItas naudojamas bus daug kur
        fillOrderLists();
    }

    public void deleteOrder() {
        FoodOrder selectedOrder = ordersList.getSelectionModel().getSelectedItem();
        customHibernate.delete(FoodOrder.class, selectedOrder.getId());
        fillOrderLists();
    }

    private void fillOrderLists() {
        ordersList.getItems().clear();
        ordersList.getItems().addAll(customHibernate.getAllRecords(FoodOrder.class));
    }

    public void loadOrderInfo() {
        //not optimal, code duplication
        FoodOrder selectedOrder = ordersList.getSelectionModel().getSelectedItem();
        clientList.getItems().stream()
                .filter(c -> c.getId() == selectedOrder.getBuyer().getId())
                .findFirst()
                .ifPresent(u -> clientList.getSelectionModel().select(u));

        basicUserList.getItems().stream()
                .filter(c -> c.getId() == selectedOrder.getBuyer().getId())
                .findFirst()
                .ifPresent(u -> basicUserList.getSelectionModel().select(u));
        titleField.setText(selectedOrder.getName());
        priceField.setText(selectedOrder.getPrice().toString());
        restaurantField.getItems().stream()
                .filter(r -> r.getId() == selectedOrder.getRestaurant().getId())
                .findFirst()
                .ifPresent(u -> restaurantField.getSelectionModel().select(u));
        //orderStatusField.getItems().stream() PATIEMS PABANDYT
        //greiciausiai reiktu field enable/disable
        disableFoodOrderFields();

    }

    private void disableFoodOrderFields() {
        if (orderStatusField.getSelectionModel().getSelectedItem() == OrderStatus.COMPLETED) {
            clientList.setDisable(true);
            priceField.setDisable(true);
        }
    }

    public void filterOrders() {
    }

    public void loadRestaurantMenuForOrder() {
        foodList.getItems().clear();
        foodList.getItems().addAll(customHibernate.getRestaurantCuisine(restaurantField.getSelectionModel().getSelectedItem()));
    }
    //</editor-fold>

    //<editor-fold desc="Cuisine Tab Functionality">
    public void createNewMenuItem() {
        Cuisine cuisine = new Cuisine(titleCuisineField.getText(), ingredientsField.getText(), Double.parseDouble(cuisinePriceField.getText()), isDeadly.isSelected(), isVegan.isSelected(), restaurantList.getSelectionModel().getSelectedItem());
        customHibernate.create(cuisine);
    }

    public void updateMenuItem(ActionEvent actionEvent) {
    }

    public void loadRestaurantMenu() {
        cuisineList.getItems().addAll(customHibernate.getRestaurantCuisine(restaurantList.getSelectionModel().getSelectedItem()));
    }
    //</editor-fold>

    public void loadAllChats() {
        allChats.getItems().clear();
        allChats.getItems().addAll(customHibernate.getAllRecords(Chat.class));
    }

    //<editor-fold desc="Admin Chat Functionality">
    public void loadChatMessages() {
        Chat selected = allChats.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        List<Message> msgs = customHibernate.getChatMessages(selected);

        chatMessages.getItems().setAll(msgs);

        if (!msgs.isEmpty()) {
            chatMessages.scrollTo(msgs.size() - 1);
        }
    }


    public void deleteChat() {
    }

    public void deleteMessage() {
    }

    public void loadChatForm() {
        try {
            Chat selectedChat = allChats.getSelectionModel().getSelectedItem();
            if (selectedChat == null) {
                FxUtils.generateAlert(Alert.AlertType.WARNING,
                        "No chat selected", "Please select a chat first.", "");
                return;
            }

            // FIX: load chat with messages initialized
            selectedChat = customHibernate.getChatWithMessages(selectedChat.getId());

            // Load order fresh too
            FoodOrder order = customHibernate.getEntityById(FoodOrder.class, selectedChat.getFoodOrder().getId());

            FXMLLoader loader = new FXMLLoader(HelloApplication.class.getResource("chat-form.fxml"));
            Parent root = loader.load();

            ChatForm controller = loader.getController();
            controller.setData(entityManagerFactory, currentUser, order);

            Stage stage = new Stage();
            stage.setTitle("Chat - " + selectedChat.getName());
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            FxUtils.generateAlert(Alert.AlertType.ERROR, "Error loading chat",
                    "Could not open chat window", e.getMessage());
        }
    }





}
