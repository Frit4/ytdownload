package com.downloadvideo.service.handler;

import com.downloadvideo.config.bot.MainBot;
import com.downloadvideo.model.event.MessageEvent;
import com.downloadvideo.model.postgresql.UserEntity;
import com.downloadvideo.service.AdminPanel;
import com.downloadvideo.service.SendTelegram;
import com.downloadvideo.service.handler.main.MessageHandlers;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.List;

@Service
@Slf4j
public class AdminCommandHandler implements MessageHandlers {
    @Autowired
    private AdminPanel adminPanel;
    @Autowired
    private SendTelegram sendTelegram;
    private final List<String> commands = List.of("/block","/stop","/start","/getUser","/getCountUsers");
    @Override
    public boolean isValid(String message) {
        for(String command: commands){
            if(message.startsWith(command)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void handle(MessageEvent event) throws NumberFormatException{
        Update update = event.getUpdate();
        String command = update.getMessage().getText();
         if(command.equalsIgnoreCase("/block")){
            String[] str = command.split(" ");
            if(str.length!=4){
                return;
            } else{
                if(str[1].equalsIgnoreCase("id")){
                    if(Boolean.parseBoolean(str[3])){
                        adminPanel.blockUser(update.getMessage().getFrom().getId());
                    } else{
                        adminPanel.unblockUser(update.getMessage().getFrom().getId());
                    }
                } else if(str[1].equalsIgnoreCase("username")){
                    if(Boolean.parseBoolean(str[3])){
                        adminPanel.blockUser(update.getMessage().getFrom().getUserName());
                    } else{
                        adminPanel.unblockUser(update.getMessage().getFrom().getUserName());
                    }
                }
            }
        } else if(command.startsWith("/start")){
             String[] str = command.split(" ");
             if(str.length==2){
                 if(str[1].equalsIgnoreCase("server")){
                     adminPanel.setStop(false);
                 } else if(str[1].equalsIgnoreCase("sendError")){
                     adminPanel.setSendError(true);
                 }
             }
         } else if(command.startsWith("/stop")){
             String[] str = command.split(" ");
             if(str.length==2){
                 if(str[1].equalsIgnoreCase("server")){
                     adminPanel.setStop(true);
                 } else if(str[1].equalsIgnoreCase("sendError")){
                     adminPanel.setSendError(false);
                 }
             }
         } else if(command.startsWith("/getCountUsers")){
             Long count = adminPanel.getUserCount();
             if(count==null){
                 return;
             }
             sendTelegram.sendMessage(new SendMessage(String.valueOf(update.getMessage().getChatId()),String.valueOf(count)));

         }
         else if(command.startsWith("/getUser")){
             String[] str = command.split(" ");
             if(str.length!=3){
                 return;
             }
             UserEntity userEntity = null;
             if(str[1].equalsIgnoreCase("id")){
                 userEntity=adminPanel.getUser(Long.parseLong(str[2]));
             } else if(str[1].equalsIgnoreCase("username")){
                 userEntity=adminPanel.getUser(str[2]);
             }
             if(userEntity==null) return;
             sendTelegram.sendMessage(new SendMessage(String.valueOf(update.getMessage().getChatId()),userEntity.toString()));

         }

    }
}
