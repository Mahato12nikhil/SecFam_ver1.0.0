package com.project.nikhil.secfamfinal1.Chat;

import androidx.annotation.Nullable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Chat {
    String id;
    String lastMessage;
    long timestamp;
    int unreadMessageCount;
    boolean typing = false;
    String name="",image="", status="";
    DatabaseReference reference;
    ValueEventListener listener;
    public Chat() {
    }

    public Chat(String id, String lastMessage, long timestamp, int unreadMessageCount, boolean typing) {
        this.id = id;
        this.lastMessage = lastMessage;
        this.timestamp = timestamp;
        this.typing = typing;
        this.unreadMessageCount = unreadMessageCount;

    }

    public boolean isTyping() {
        return typing;
    }

    public void setTyping(boolean typing) {
        this.typing = typing;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }


    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DatabaseReference getReference() {

        return reference;
    }

    public void setReference(DatabaseReference reference) {
        this.reference = reference;
    }

    public ValueEventListener getListener() {
        return listener;
    }

    public void setListener(ValueEventListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        assert obj != null;
        if (getClass() != obj.getClass()) {
            return false;
        }
        if (this == obj)return true;
        return (id.equals(((Chat)obj).id));
    }
}
