package Model;

public class ModelScan {
    private String deviceName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getDevUIID() {
        return devUIID;
    }

    public void setDevUIID(String devUIID) {
        this.devUIID = devUIID;
    }

    public String getDevRssi() {
        return devRssi;
    }

    public void setDevRssi(String devRssi) {
        this.devRssi = devRssi;
    }

    private String devUIID;
    private String devRssi;
}
