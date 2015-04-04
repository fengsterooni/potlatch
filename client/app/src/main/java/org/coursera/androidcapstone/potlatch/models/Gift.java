package org.coursera.androidcapstone.potlatch.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.common.base.Objects;

public class Gift implements Parcelable {
    private long id;
    private long parentId;

    private String title;
    private String dataUrl;
    private String text;
    private long touches;
    private User user;
    private String contentType;
    private boolean obscene;
    private boolean touched;

    public Gift() {
    }

    public Gift(String title, long parentId, String contentType, String text, User user, long touches, boolean touched, boolean obscene) {
        super();
        this.title = title;
        this.parentId = parentId;
        this.contentType = contentType;
        if (text != null)
            this.text = text;
        this.user = user;
        this.touches = touches;
        this.touched = touched;
        this.obscene = obscene;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parent_id) {
        this.parentId = parent_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDataUrl() {
        return dataUrl;
    }

    public void setDataUrl(String url) {
        this.dataUrl = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getTouches() {
        return touches;
    }

    public void setTouches(long touches) {
        this.touches = touches;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public boolean isObscene() {
        return obscene;
    }

    public void setObscene(boolean obscene) {
        this.obscene = obscene;
    }

    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(title, dataUrl);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Gift) {
            Gift other = (Gift) obj;
            return
                    Objects.equal(title, other.title)
                    && Objects.equal(dataUrl, other.dataUrl);

        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeLong(this.parentId);
        dest.writeString(this.title);
        dest.writeString(this.dataUrl);
        dest.writeString(this.text);
        dest.writeLong(this.touches);
        dest.writeParcelable(this.user, 0);
        dest.writeString(this.contentType);
        dest.writeByte(obscene ? (byte) 1 : (byte) 0);
        dest.writeByte(touched ? (byte) 1 : (byte) 0);
    }

    private Gift(Parcel in) {
        this.id = in.readLong();
        this.parentId = in.readLong();
        this.title = in.readString();
        this.dataUrl = in.readString();
        this.text = in.readString();
        this.touches = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.contentType = in.readString();
        this.obscene = in.readByte() != 0;
        this.touched = in.readByte() != 0;
    }

    public static final Creator<Gift> CREATOR = new Creator<Gift>() {
        public Gift createFromParcel(Parcel source) {
            return new Gift(source);
        }

        public Gift[] newArray(int size) {
            return new Gift[size];
        }
    };
}
