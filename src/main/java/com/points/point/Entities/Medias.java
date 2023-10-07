package com.points.point.Entities;

import java.util.Arrays;

public class Medias {

    private String mediaId;
    private String mediaPointCall;
    private String filename;
    private String fileType;
    private long fileSize;
    private byte[] file;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    public String getMediaPointCall() {
        return mediaPointCall;
    }

    public void setMediaPointCall(String mediaPointCall) {
        this.mediaPointCall = mediaPointCall;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "{" +
                "mediaId='" + mediaId + '\'' +
                ", mediaPointCall='" + mediaPointCall + '\'' +
                ", filename='" + filename + '\'' +
                ", fileType='" + fileType + '\'' +
                ", fileSize='" + fileSize +
                '}';
    }
}