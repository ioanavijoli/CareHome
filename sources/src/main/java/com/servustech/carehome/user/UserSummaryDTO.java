package com.servustech.carehome.user;

/**
 * Created by user on 11/21/2016.
 */
public class UserSummaryDTO {

    private String uuid;
    private String name;
    private byte[] avatar;

    public UserSummaryDTO(String uuid, String name, byte[] avatar) {
        this.uuid = uuid;
        this.name = name;
        this.avatar = avatar;
    }

    public UserSummaryDTO(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
}
