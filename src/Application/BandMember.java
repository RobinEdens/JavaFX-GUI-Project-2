package Application;

public class BandMember {
    private String fName;
    private String lName;
    private String instruments;

    BandMember(String fname, String lname, String instruments) {
        this.setfName(fname);
        this.setlName(lname);
        this.setInstruments(instruments);
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getInstruments() {
        return instruments;
    }

    public void setInstruments(String instruments) {
        this.instruments = instruments;
    }

    @Override
    public String toString() { return "Name: " + this.getfName() + " " + this.getlName() + " - Instrument: " + this.getInstruments();}
}
