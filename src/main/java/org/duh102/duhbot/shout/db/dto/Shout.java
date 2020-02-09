package org.duh102.duhbot.shout.db.dto;

import java.util.Objects;

public class Shout {
    private Long id;
    private String shout;
    private String user;
    private String channel;
    private Boolean isAction;
    private Long timestamp;
    public Shout(Long id, String shout, String user, String channel, Boolean isAction, Long timestamp) {
        this.id = id;
        this.shout = shout;
        this.user = user;
        this.channel = channel;
        this.isAction = isAction;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShout() {
        return shout;
    }

    public void setShout(String shout) {
        this.shout = shout;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public Boolean isAction() {
        return isAction;
    }

    public void setAction(Boolean action) {
        isAction = action;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Shout{" +
                "id=" + id +
                ", shout='" + shout + '\'' +
                ", user='" + user + '\'' +
                ", channel='" + channel + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shout shout1 = (Shout) o;
        return Objects.equals(id, shout1.id) &&
                shout.equals(shout1.shout) &&
                user.equals(shout1.user) &&
                channel.equals(shout1.channel) &&
                isAction.equals(shout1.isAction) &&
                timestamp.equals(shout1.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, shout, user, channel, isAction, timestamp);
    }
}
