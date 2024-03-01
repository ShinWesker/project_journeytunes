package dhbw.mosbach.routes;

public enum Configuration {
    INSTANCE;

    public String getAccessToken() {
        return "5b3ce3597851110001cf62481a14657348be4e418d3388652c890b0d";
    }

    public String getApiUrlDirections(){
        return "https://api.openrouteservice.org/v2/directions/driving-car";
    }
    public String getApiUrlGeocode(){
        return "https://api.openrouteservice.org/geocode/search";
    }
}
