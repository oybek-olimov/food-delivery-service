package org.example.deliveryservice.generic;

import lombok.Getter;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
public class AppErrorDTO {

    private String errorMessage;

    private String errorPath;

    private int errorCode;;

    private LocalDateTime timestamp;

    public AppErrorDTO(String errorMessage, String errorPath, int errorCode) {
        this.errorMessage = errorMessage;
        this.errorPath = errorPath;
        this.errorCode = errorCode;
        this.timestamp = LocalDateTime.now(Clock.system(ZoneId.of("Asia/Tashkent")));
    }
}
