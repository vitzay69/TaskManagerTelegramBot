package org.vitzay.service;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.vitzay.dao.NotesDAO;
import org.vitzay.entity.Note;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BotTaskMaster extends TelegramLongPollingBot {

    private final Map<Long, String> userStates = new HashMap<>();
    private NotesDAO dao = new NotesDAO();

    @Override
    public String getBotUsername() {
        return "TaskMaster";
    }

    @Override
    public String getBotToken() {
        return System.getenv("BOT_TOKEN");
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {

            var message = update.getMessage();
            var text = message.getText();
            Long chatId = message.getChatId();
            var user = message.getFrom();

            System.out.println(user + "write: " + text + " " + chatId);

            if (message.isCommand()) {
                if (text.equals("/toma")) {
                    sendMessage(chatId, """
                            Hello I`m your personal Task Master.
                            I can save your notes.
                            If you wanna save some notes just say me - /note.
                            If you wanna check your notes just say me - /listNotes.
                            If you wanna edit some note just say - /editName or /editDescription.
                            If you wanna delete some notes just say me - /delete.
                            """);
                } else if (text.startsWith("/note")) {
                    if (text.contains("|")) {
                        int index = text.indexOf("|");
                        String description = text.substring(index + 1);
                        Note note = new Note(text.substring(6, index), description, Timestamp.valueOf(LocalDateTime.now()), chatId);
                        try {
                            dao.create(note);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        sendMessage(chatId, "Your note has been saved.");
                    } else {
                        Note note = new Note(text.substring(6), Timestamp.valueOf(LocalDateTime.now()), chatId);
                        try {
                            dao.create(note);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        sendMessage(chatId, "Your note has been saved.");
                    }

                } else if (text.startsWith("/listNotes")) {
                    List<Note> noteList = dao.getNotes(chatId);
                    if (!noteList.isEmpty()) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for (Note note : noteList) {
                            stringBuilder.append(note.toString()).append("\n");
                        }
                        sendMessage(chatId, stringBuilder.toString());
                    } else {
                        sendMessage(chatId, "No notes found.");
                    }

                } else if (text.startsWith("/delete")) {
                    int id = Integer.parseInt(text.substring(8));
                    try {
                        dao.delete(id, chatId);
                        sendMessage(chatId, "Note deleted successfully.");
                    } catch (SQLException e) {
                        sendMessage(chatId, "Note with the specified ID does not exist.");
                        e.printStackTrace();
                    }
                } else if (text.startsWith("/editName")) {
                    int index = text.indexOf("|");
                    String newName = text.substring(index + 1);
                    int id = Integer.parseInt(text.substring(10, index - 1));
                    try {
                        dao.editName(id, newName, chatId);
                        sendMessage(chatId, "Note edited successfully.");
                    } catch (SQLException e) {
                        sendMessage(chatId, "Note with the specified ID does not exist.");
                        e.printStackTrace();
                    }
                } else if (text.startsWith("/editDescription")) {
                    int index = text.indexOf("|");
                    String newDescription = text.substring(index + 1);
                    int id = Integer.parseInt(text.substring(17, index - 1));
                    try {
                        dao.editDescription(id, newDescription, chatId);
                        sendMessage(chatId, "Note edited successfully.");
                    } catch (SQLException e) {
                        sendMessage(chatId, "Note with the specified ID does not exist.");
                        e.printStackTrace();
                    }
                }
            }
        }

    }


    public void sendMessage(Long id, String text) {
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
