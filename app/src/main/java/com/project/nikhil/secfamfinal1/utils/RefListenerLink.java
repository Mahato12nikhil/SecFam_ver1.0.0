package com.project.nikhil.secfamfinal1.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class RefListenerLink {
    DatabaseReference reference;
    ValueEventListener listener;

    public RefListenerLink(DatabaseReference reference, ValueEventListener listener) {
        this.reference = reference;
        this.listener = listener;
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
}
