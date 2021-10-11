package Application;
import java.util.ArrayList;

/*
    - @Author: Robin Edens
    - @Date: October 7, 2020
    - @Description:  Application.Inventory class for project. Works as the primary inventory and the inventory object is kept alive as long as possible
    - by endless passes between the Application windows, as I was unable to find a way to implement a static inventory that is accessible
    - by all subclasses of Controller, but it would create a new Controller object every time the window changed.
 */
public class Inventory {

    // Actual ArrayList that functions as an inventory
    private ArrayList<Album> albumInventory;
    public static final class ChangeIndex {private ChangeIndex() {} };
    private static final ChangeIndex changeIndex = new ChangeIndex();

    public Inventory() {
        albumInventory = new ArrayList<>();
    }
    public Inventory(ArrayList<Album> albumInventory) {
        this.albumInventory = albumInventory;
    }

    public ArrayList<Album> getAlbumInventory () {
        return this.albumInventory;
    }
    public void add(Album album) {
        this.albumInventory.add(album);
    }

    // Deletes from inventory at passed index and subtracts one from total albums
    public void delete(int index){
        this.albumInventory.remove(index);
        for (int iter = index; iter < this.getAlbumInventory().size(); iter++) {
            this.albumInventory.get(iter).setID(changeIndex, Integer.toString(iter+1));
        }
        Album.deleteAlbum();
    }

    // Replaces Album at index with modified one
    public void change (int songIndex, Album newAlbum) {
        this.getAlbumInventory().set(songIndex, newAlbum);
    }


    // There for the sake of having a print command, but primarily is made obsolete by toString() which is used more
    public void print() {
        for (Album album: this.albumInventory) {
            System.out.println(album);
        }
    }

    public boolean isEmpty() {
        return albumInventory.isEmpty();
    }

    // Never had a chance to implement due to time constraints
    public void sortByBand() {

    }
    // Never had a chance to implement due to time constraints
    public void sortByAlbum() {

    }

    @Override
    public String toString(){
        StringBuilder string = new StringBuilder();
        for (Album album: this.albumInventory) {
            string.append(album + "\n");
        }
        return string.toString();
    }
}
