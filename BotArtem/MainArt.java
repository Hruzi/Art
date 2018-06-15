package BotArtem;

import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class MainArt {
    public static void main(String args[]){

        ArtemInterface interf = new ArtemInterface();
        interf.setVisible(true);

        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();

        try {
            botsApi.registerBot(new ArtemBot());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
