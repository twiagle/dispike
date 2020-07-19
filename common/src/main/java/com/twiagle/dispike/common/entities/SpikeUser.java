package com.twiagle.dispike.common.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SpikeUser {
    private Long id;
    private String nickname;
    private String password;
    private String salt;
    private String head;
    private LocalDateTime registerDate;
    private LocalDateTime lastLoginDate;
    private Integer loginCount;
}
