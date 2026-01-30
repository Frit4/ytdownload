package com.userbot.userbot.listener.events;

import com.userbot.userbot.service.VideoDownloaded;
import it.tdlight.jni.TdApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class UpdateMessageSendEvent {
    @Autowired
    private VideoDownloaded videoDownloaded;
    @EventListener
    public void listen(TdApi.UpdateMessageSendSucceeded update){
        CompletableFuture<TdApi.File> completableFuture = videoDownloaded.getCompletableFuture(update.oldMessageId);
        if(completableFuture!=null){
            completableFuture.complete(null);
            videoDownloaded.delete(update.oldMessageId);
        }
    }
    @EventListener
    public void listen(TdApi.UpdateMessageSendFailed update){
        CompletableFuture<TdApi.File> completableFuture = videoDownloaded.getCompletableFuture(update.oldMessageId);
        if(completableFuture!=null){
            completableFuture.completeExceptionally(new RuntimeException(update.error.message));
            videoDownloaded.delete(update.oldMessageId);
        }
    }
}
