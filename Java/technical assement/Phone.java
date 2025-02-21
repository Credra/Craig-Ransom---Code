package wim;

public class Phone {
    double easting;
    double northing;
    double longitude;
    double latitude;
    //ID and index could be combined
    char ID;
    //stores the index in terms of the input data
    //this is used to index the distance array
    int index;
    int frequency;
    
    // Constructor
    public Phone(char ID, double easting, double northing, double longitude, double latitude, int index)
    {
    	this.easting = easting;
        this.northing = northing;
        this.longitude = longitude;
        this.latitude = latitude;
        this.ID = ID;
        this.frequency = -1;
        this.index = index;
    }
}
