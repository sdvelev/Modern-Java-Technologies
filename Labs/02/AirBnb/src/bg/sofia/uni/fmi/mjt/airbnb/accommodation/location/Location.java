package bg.sofia.uni.fmi.mjt.airbnb.accommodation.location;

public class Location {

    private final double x;
    private final double y;

    public Location(double x, double y){
        this.x=x;
        this.y=y;
    }

    public double getX(){
        return this.x;
    }

    public double getY(){
        return this.y;
    }

}
