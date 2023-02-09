package com.example.springredis;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("Meetings") // 여기는 레디스 클라이언트이고 레디스 서버를 따로 띄워야 한다. -> 우리는 도커로 띄운다.
public class Meeting {
    @Id
    private String id;
    private String title;
    private Date startAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }
}
