package org.vitzay;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import org.vitzay.service.BotTaskMaster;

public class Main {
    public static void main(String[] args) {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            BotTaskMaster botTaskMaster = new BotTaskMaster();
            botsApi.registerBot(botTaskMaster);
            System.out.println("Bot is running...");
        } catch (TelegramApiException e) {
            System.err.println("Failed to register bot: " + e.getMessage());
            e.printStackTrace();
        }
    }
}