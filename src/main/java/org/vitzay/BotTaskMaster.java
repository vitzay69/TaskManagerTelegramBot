package org.vitzay;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class BotTaskMaster extends TelegramLongPollingBot {
    @Override
    public String getBotUsername() {
        return "TaskMaster";
    }

    @Override
    public String getBotToken() {
        return "7171813369:AAE7NKP0Ibm63Tdn_gI7kBt0LHZmDBnBYb4";
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            var message = update.getMessage();
            var text = message.getText();
            var chatId = message.getChatId();
            var user = message.getFrom();

            System.out.println(user + "write: " + text + " " + chatId);

            if(message.isCommand()) {
                if (text.equals("/start")) {
                    SendMessage(chatId, """
                            Hello I`m your personal Task Master.
                            I can save your notes and remind you.
                            If you wanna save some notes just say me - /note.
                            If you wanna check your notes just say me - /notes.
                            If you wanna I remind you about some note say - /remind.
                            """);
                }
            }
        }
    }

    public void SendMessage(Long id, String text) {
        SendMessage sendMessage = SendMessage.builder()
                .chatId(id.toString())
                .text(text).build();

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }
}
