package com.example.courseprifs.fxControllers;

import com.example.courseprifs.hibernateControl.CustomHibernate;
import com.example.courseprifs.model.*;
import jakarta.persistence.EntityManagerFactory;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import com.example.courseprifs.hibernateControl.GenericHibernate;

public class ChatForm {

    @FXML
    public ListView<Message> messageList;
    @FXML
    public TextArea messageBody;

    private EntityManagerFactory entityManagerFactory;
    private CustomHibernate customHibernate;
    private GenericHibernate genericHibernate;

    private User currentUser;
    private FoodOrder currentFoodOrder;

    public void setData(EntityManagerFactory entityManagerFactory, User currentUser, FoodOrder currentFoodOrder) {
        this.entityManagerFactory = entityManagerFactory;
        this.currentUser = currentUser;
        this.currentFoodOrder = currentFoodOrder;

        this.customHibernate = new CustomHibernate(entityManagerFactory);
        this.genericHibernate = new GenericHibernate(entityManagerFactory);

        loadMessages();
    }


    private void loadMessages() {
        if (currentFoodOrder.getChat() == null) return;

        Chat chat = customHibernate.getChatWithMessages(currentFoodOrder.getChat().getId());

        messageList.getItems().setAll(chat.getMessages());

        if (!chat.getMessages().isEmpty()) {
            messageList.scrollTo(chat.getMessages().size() - 1);
        }
    }


    public void sendMessage() {
        try {
            String text = messageBody.getText().trim();
            if (text.isEmpty()) return;

            // If chat does not exist, create and save it properly
            if (currentFoodOrder.getChat() == null) {
                Chat newChat = new Chat("Chat for order: " + currentFoodOrder.getName(), currentFoodOrder);
                customHibernate.create(newChat);

                currentFoodOrder.setChat(newChat);
                customHibernate.update(currentFoodOrder);  // VERY IMPORTANT
            }

            // Reload chat SAFELY (prevents foreign key problems)
            Chat chat = customHibernate.getChatWithMessages(currentFoodOrder.getChat().getId());

            // Load sender
            User sender = customHibernate.getEntityById(User.class, 8);


            if (sender == null) {
                // Create if missing (but don't manually set id!)
                sender = new User("system", "none", "System", "Bot", "000000");
                customHibernate.create(sender);
            }

            // Create new message
            Message message = new Message();
            message.setChat(chat);
            message.setUser(sender);
            message.setMessage(text);
            message.setDateCreated(LocalDateTime.now());

            customHibernate.create(message);

            // Refresh food order to ensure chat is updated
            currentFoodOrder = customHibernate.getEntityById(FoodOrder.class, currentFoodOrder.getId());

            // Clear and reload
            messageBody.clear();
            loadMessages();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteSelectedMessage(ActionEvent actionEvent) {
        Message selected = messageList.getSelectionModel().getSelectedItem();

        if (selected == null) {
            FxUtils.generateAlert(Alert.AlertType.WARNING,
                    "No message selected",
                    null,
                    "Please select a message to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Message");
        confirm.setHeaderText("Delete this message?");
        confirm.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return;

        genericHibernate.delete(Message.class, selected.getId());

        loadMessages();
    }





}
