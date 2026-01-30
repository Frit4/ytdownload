package com.downloadvideo.aop;

import com.downloadvideo.service.AdminPanel;
import com.downloadvideo.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

@Aspect
@Service
@Slf4j
public class StopProject {
    @Autowired
    private AdminPanel adminPanel;
    @Autowired
    private UserService userService;
    @Around("execution(* com.downloadvideo.service.handler.main.MainHandler.handler(org.telegram.telegrambots.meta.api.objects.Update))")
    public Object stopProject(ProceedingJoinPoint point){
        if(adminPanel.isStop()) {
            Update update = (Update) point.getArgs()[0];
            Long id = null;
            if(update.hasMessage()){
                id=update.getMessage().getFrom().getId();
            }
            if(id==null) return null;
            if(!userService.isAdmin(id)){
                return null;
            }
            try {
                return point.proceed();
            } catch (Throwable e) {
                log.error("Ошибка при запуске сервера админом(из Tg)!",e);
            }
        }
        try {
            return point.proceed();
        } catch (Throwable e) {
            log.error("Ошибка при выполнение проверки на стоп!",e);
        }
        return null;
    }
}
