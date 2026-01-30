package com.downloadvideo.service.handler;

import com.downloadvideo.config.bot.MainBot;
import com.downloadvideo.model.event.MessageEvent;
import com.downloadvideo.service.SendTelegram;
import com.downloadvideo.service.handler.main.MessageHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Service
@Slf4j
public class CommandHandler implements MessageHandlers {
    @Autowired
    private SendTelegram sendTelegram;

    private final List<String> commands = List.of("/start");
    @Override
    public boolean isValid(String message) {
        for(String command: commands){
            if(message.equalsIgnoreCase(command)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void handle(MessageEvent event) {
        Update update = event.getUpdate();
        String command = update.getMessage().getText();
        if(command.equalsIgnoreCase("/start")){
            SendMessage message = new SendMessage(update.getMessage().getChatId().toString(),
                    "Отправьте ссылку на видео и мы скачаем для вас видео любого качества с любой озвучкой.");
            sendTelegram.sendMessage(message);
        }
    }
}
