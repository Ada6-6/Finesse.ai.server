package com.powerpuff.billServer.model;

public class Message {
    private String content;
    private String role;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    // 添加 getter 方法
    public String getRole() {
        return role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
