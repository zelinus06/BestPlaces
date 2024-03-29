package com.bestplaces;

import com.bestplaces.Service.MyTelegramBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class BestPlacesApplication {

	public static void main(String[] args) {
		SpringApplication.run(BestPlacesApplication.class, args);
		try {
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(new MyTelegramBot());
		} catch (TelegramApiRequestException e) {
			e.printStackTrace();
		} catch (TelegramApiException e) {
			throw new RuntimeException(e);
		}
	}

}
