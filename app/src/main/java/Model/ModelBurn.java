package Model;

public class ModelBurn {
    private String modName;
    private String modSerial;

    public String getModName() {
        return modName;
    }

    public void setModName(String modName) {
        this.modName = modName;
    }

    public String getModSerial() {
        return modSerial;
    }

    public void setModSerial(String modSerial) {
        this.modSerial = modSerial;
    }

    public String getModDesc() {
        return modDesc;
    }

    public void setModDesc(String modDesc) {
        this.modDesc = modDesc;
    }

    public String getModFreq() {
        return modFreq;
    }

    public void setModFreq(String modFreq) {
        this.modFreq = modFreq;
    }

    public String getModTime() {
        return modTime;
    }

    public void setModTime(String modTime) {
        this.modTime = modTime;
    }

    public String getModInt() {
        return modInt;
    }

    public void setModInt(String modInt) {
        this.modInt = modInt;
    }


    public String getModStart() {
        return modStart;
    }

    public void setModStart(String modStart) {
        this.modStart = modStart;
    }

    public String getModEnd() {
        return modEnd;
    }

    public void setModEnd(String modEnd) {
        this.modEnd = modEnd;
    }

    private String modDesc;
    //parameters
    private String modFreq;
    private String modTime;
    private String modInt;
    //module
    private String modTransdA;

    public String getModTransdA() {
        return modTransdA;
    }

    public void setModTransdA(String modTransdA) {
        this.modTransdA = modTransdA;
    }

    public String getModTransdB() {
        return modTransdB;
    }

    public void setModTransdB(String modTransdB) {
        this.modTransdB = modTransdB;
    }

    private String modTransdB;
    //status
    private String modStart;
    private String modEnd;
    private String modStatus;

    public String getModStatus() {
        return modStatus;
    }

    public void setModStatus(String modStatus) {
        this.modStatus = modStatus;
    }
}
