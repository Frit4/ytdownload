package com.userbot.userbot.model;

import lombok.Getter;

import java.nio.file.Paths;

public class DownloadPath {
    public static final String PATH = String.valueOf(Paths.get(System.getProperty("user.dir"),"download"));
}
