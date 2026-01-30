package com.downloadvideo.service;

import com.downloadvideo.config.bot.MainBot;
import com.downloadvideo.model.DownloadProcess;
import com.downloadvideo.model.postgresql.VideoDataEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.network.Send;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.InputFile;

@Slf4j
@Service
public class SendVideoToUser {
    @Autowired
    private MainBot mainBot;
    @Autowired
    private UserService userService;
    @Autowired
    private VideoDataService videoDataService;
    @Autowired
    private SendTelegram sendTelegram;

    public void sendVideo(VideoDataEntity videoData,String fileId){
        DeleteMessage deleteMessage = new DeleteMessage(videoData.getChat_id().toString(),videoData.getMessage_id().intValue());
        SendVideo sendVideo = new SendVideo();
        sendVideo.setChatId(videoData.getChat_id());
        sendVideo.setVideo(new InputFile(fileId));
        sendVideo.setCaption("Видео готово!");
        try {
            sendTelegram.sendVideo(sendVideo);
            sendTelegram.deleteMessage(deleteMessage);
            log.info("Успешная отправка видео пользователю! id задачи:{}",videoData.getId());
            videoDataService.completionProcess(videoData.getId(),DownloadProcess.FINISH);
            userService.addCountDownloads(videoData.getUser_id());

        } catch (Exception e) {
            videoDataService.completionProcess(videoData.getId(),DownloadProcess.FAIL);
            videoDataService.completionProcess(videoData.getId(),DownloadProcess.FAIL);
            log.error("Произошла ошибка при отправке видео пользователю!",e);
        }
    }

}
