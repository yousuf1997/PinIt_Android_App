package Mohamed.mad.markmycar;

public class LocationPoints {
    private Double lattitude, longitude;
    private String title, markedDate;

    public LocationPoints(Double lattitude, Double longitude, String title, String markedDate) {
        this.lattitude = lattitude;
        this.longitude = longitude;
        this.title = title;
        this.markedDate = markedDate;
    }

    public Double getLattitude() {
        return lattitude;
    }

    public void setLattitude(Double lattitude) {
        this.lattitude = lattitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMarkedDate() {
        return markedDate;
    }

    public void setMarkedDate(String markedDate) {
        this.markedDate = markedDate;
    }
}
