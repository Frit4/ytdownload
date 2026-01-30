package com.downloadvideo.model.event;

import com.downloadvideo.config.bot.MainBot;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.Update;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CallbackEvent {
    private Update update;
}
