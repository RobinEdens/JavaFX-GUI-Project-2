package Application;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Objects;

/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  Album class used throughout to populate the Application.Inventory.
 */
public class Album {
    private static int totalAlbums;

    private SimpleStringProperty bandName;
    private SimpleStringProperty albumName;
    private ArrayList<Song> trackList;
    private ArrayList<BandMember> members;
    private SimpleStringProperty publisher;
    private LocalDate datePublished;
    private String id;
    Album () {

    }
    Album(String bandName, String albumName, String publisher,
          ArrayList<Song> trackList, ArrayList<BandMember> members,
          LocalDate datePublished) {
        this.setBandName(bandName);
        this.setAlbumName(albumName);
        this.setPublisher(publisher);
        this.setTrackList(trackList);
        this.setMembers(members);
        this.setDatePublished(datePublished);
        Album.totalAlbums = Album.getTotalAlbums() + 1;
        this.id = Integer.toString(Album.getTotalAlbums());
    }

    // Sort of weird way to get Inventory to work the way a Friend class does in C++ for accessing private methods
    public void setID (Inventory.ChangeIndex change, String id) {
        Objects.requireNonNull(change);
        setID(id);
    }

    private void setID(String id) {
        this.id = id;
    }
    public static int getTotalAlbums() {
        return Album.totalAlbums;
    }

    protected static void deleteAlbum () {
        Album.totalAlbums--;
    }

    public String getBandName() { return this.bandName.get();}

    public void setBandName(String bandName) { this.bandName = new SimpleStringProperty(bandName);}

    public String getAlbumName() {
        return this.albumName.get();
    }

    public void setAlbumName(String albumName) { this.albumName = new SimpleStringProperty(albumName); }

    public String getPublisher() { return this.publisher.get(); }

    public void setPublisher(String publisher) { this.publisher = new SimpleStringProperty(publisher); }

    public ArrayList<Song> getTrackList() {
        return this.trackList;
    }

    public void setTrackList(ArrayList<Song> trackList) { this.trackList = trackList; }

    public ArrayList<BandMember> getMembers() { return members; }

    public void setMembers(ArrayList<BandMember> members) { this.members = members; }

    public LocalDate getDatePublished() { return this.datePublished; }

    public void setDatePublished(LocalDate datePublished) { this.datePublished = datePublished;}

    public int numOfSongs () {
        return this.getTrackList().size();
    }

    public double length() {
        double length = 0;
        for (Song song: this.getTrackList()) {
            length += song.getLength();
        }
        return length;
    }

    public boolean isSelfTitled() {
        return this.getBandName().equals(this.getAlbumName());
    }

    @Override
    public String toString() {
        return this.getBandName() + " - " + this.getAlbumName() + " | Released " + this.getDatePublished()
                + " | Length: " + this.length() + " | Publisher: " + this.getPublisher() ;
    }

    public String getId() {
        return id;
    }
}
