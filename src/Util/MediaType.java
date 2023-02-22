package Util;

public enum MediaType {
    TEXT(".txt"),
    IMAGE(".jpg"),
    AUDIO(".mp3"),
    VIDEO(".mp4");


    private String fileExtension;

    MediaType(String fileExtension) {

        this.fileExtension = fileExtension;
    }
}
