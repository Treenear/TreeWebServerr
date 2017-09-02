package com.treenear.treewebserver.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by richardus on 8/24/17.
 */

public class ColDocument implements Parcelable {
    private int Id;
    private String Description;
    private String Remarks;
    private String EntryDate;
    private int EntryName;
    private int Status;

    public ColDocument(){};


    protected ColDocument(Parcel in) {
        Id = in.readInt();
        Description = in.readString();
        Remarks = in.readString();
        EntryDate = in.readString();
        EntryName = in.readInt();
        Status = in.readInt();
    }

    public static final Creator<ColDocument> CREATOR = new Creator<ColDocument>() {
        @Override
        public ColDocument createFromParcel(Parcel in) {
            return new ColDocument(in);
        }

        @Override
        public ColDocument[] newArray(int size) {
            return new ColDocument[size];
        }
    };

    public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }

        public String getDescription() {
            return Description;
        }

        public void setDescription(String description) {
            Description = description;
        }

        public String getRemarks() {
            return Remarks;
        }

        public void setRemarks(String remarks) {
            Remarks = remarks;
        }

        public String getEntryDate() {
            return EntryDate;
        }

        public void setEntryDate(String entryDate) {
            EntryDate = entryDate;
        }

        public int getEntryName() {
            return EntryName;
        }

        public void setEntryName(int entryName) {
            EntryName = entryName;
        }

        public int getStatus() {
            return Status;
        }

        public void setStatus(int status) {
            Status = status;
        }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(Id);
        dest.writeString(Description);
        dest.writeString(Remarks);
        dest.writeString(EntryDate);
        dest.writeInt(EntryName);
        dest.writeInt(Status);
    }
}
