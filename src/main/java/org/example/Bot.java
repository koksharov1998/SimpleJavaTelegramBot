package org.example;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.CopyMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.json.*;

import java.io.*;

public class Bot extends TelegramLongPollingBot {

    private final String botToken;

    public Bot() {
        StringBuilder stringBuilder = new StringBuilder();
        try (var bufferedReader = new BufferedReader(new FileReader("src\\main\\resources\\botToken.json"))) {
            String inputLine;
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONObject obj = new JSONObject(stringBuilder.toString());
        botToken = obj.getString("botToken");
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();
        var user = msg.getFrom();
        var id = user.getId();

        System.out.println(user.getFirstName() + " wrote " + msg.getText());
        sendText(id, msg.getText());
    }


    @Override
    public String getBotUsername() {
        return "MyNewTest1123Bot";
    }

    public void sendText(Long who, String what) {
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        try {
            execute(sm);                        //Actually sending the message
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);      //Any error will be printed here
        }
    }

    public void copyMessage(Long who, Integer msgId) {
        CopyMessage cm = CopyMessage.builder()
                .fromChatId(who.toString())  //We copy from the user
                .chatId(who.toString())      //And send it back to him
                .messageId(msgId)            //Specifying what message
                .build();
        try {
            execute(cm);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
