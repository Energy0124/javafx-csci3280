/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Stephy
 */
public class PlayList {

    private final SimpleStringProperty title;
    private final SimpleStringProperty album;
    private final SimpleStringProperty singer;
    private final SimpleStringProperty length;

    private String formatLength(int intLength) {
        int hour, minute, second;
        hour = intLength / 3600;
        intLength -= hour * 3600;
        minute = intLength / 60;
        intLength -= minute * 60;
        second = intLength;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }

    public PlayList(String title, String album, String singer, int intLength) {
        this.title = new SimpleStringProperty(title);
        this.album = new SimpleStringProperty(album);
        this.singer = new SimpleStringProperty(singer);
        this.length = new SimpleStringProperty(this.formatLength(intLength));
    }

    public String getTitle() {
        return this.title.get();
    }

    public String getAlbum() {
        return this.album.get();
    }

    public String getSinger() {
        return this.singer.get();
    }

    public String getLength() {
        return this.length.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public void setSinger(String singer) {
        this.singer.set(singer);
    }

    public void setLength(int intLength) {
        this.length.set(this.formatLength(intLength));
    }
}
