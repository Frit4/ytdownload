package com.downloadvideo.aop;

import com.downloadvideo.config.bot.MainBot;
import com.downloadvideo.model.event.CallbackEvent;
import com.downloadvideo.model.event.MessageEvent;
import com.downloadvideo.service.SendTelegram;
import com.downloadvideo.service.VideoDataService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Aspect
@Service
@Slf4j
public class CheckExistsTask {
    @Autowired
    private VideoDataService videoDataService;
    @Autowired
    private SendTelegram sendTelegram;
    @Around("execution(* com.downloadvideo.service.handler.YoutubeMessageHandler.handle(com.downloadvideo.model.event.MessageEvent)) || "+
    "execution(* com.downloadvideo.service.handler.DownloadHandler.handle(com.downloadvideo.model.event.CallbackEvent)) || "+
    "execution(* com.downloadvideo.service.handler.QualityHandler.handle(com.downloadvideo.model.event.CallbackEvent))")
    public Object check(ProceedingJoinPoint point){
        java.lang.Object object=point.getArgs()[0];
        Long id = null;
        String chatId = null;
        if(object instanceof MessageEvent messageEvent){
            id = messageEvent.getUpdate().getMessage().getFrom().getId();

            chatId=String.valueOf(messageEvent.getUpdate().getMessage().getChatId());
        } else if(object instanceof CallbackEvent callbackEvent){
            id = callbackEvent.getUpdate().getCallbackQuery().getFrom().getId();
            chatId=String.valueOf(callbackEvent.getUpdate().getCallbackQuery().getMessage().getChatId());
        }
        if(id==null){
            return null;
        }
        if(videoDataService.checkExistsTask(id)){
            try {
                return point.proceed();
            } catch (Throwable e) {
                log.error("Ошибка при проверке имеющийся активной загрузи!",e);
            }
        } else {
            sendTelegram.sendMessage(new SendMessage(String.valueOf(chatId),
                    "Вы уже скачиваете видео!\n Дождитесь конца скачивания или в случае долгой загрузки 2 минуты посл начала загрузки."));

        }
        return null;
    }
}
