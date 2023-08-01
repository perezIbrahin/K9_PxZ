package Util;

public class Configuration {



    //temperature
    public  Integer MAX_RANGE_TEMPERATURE=80;
    public  Integer MIN_RANGE_TEMPERATURE=1;

    //humidity
    public  Integer MAX_RANGE_HUMIDITY=80;
    public  Integer MIN_RANGE_HUMIDITY=30;

    //battery
    public  Integer MAX_RANGE_BATTERY=80;
    public  Integer MIN_RANGE_BATTERY=10;

    public  Integer MAX_RANGE_TIME_DELAY=60;
    public  Integer MIN_RANGE_TIME_DELAY=1;

    //location-who changed the data
    public String LOCATION_SETUP_TEMP="temp";
    public String LOCATION_SETUP_HUM="hum";
    public String LOCATION_SETUP_BAT="bat";
    public String LOCATION_SETUP_CONTACT_PRI="contact_primary";
    public String LOCATION_SETUP_CONTACT_ALT="contact_alternative";
    //units
    public  String UNIT_TEMP="ºF";
    public  String UNIT_HUM="%";
    public  String UNIT_BAT="%";
}
