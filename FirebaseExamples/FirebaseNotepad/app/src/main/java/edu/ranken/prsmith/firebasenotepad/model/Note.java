package edu.ranken.prsmith.firebasenotepad.model;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentId;

public class Note {
    @DocumentId public String id;
    public String title;
    public String body;
    public String ownerUid;
    public Timestamp createdAt;
    public Timestamp updatedAt;
}
