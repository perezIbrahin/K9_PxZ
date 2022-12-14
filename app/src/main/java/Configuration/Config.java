package Configuration;

public class Config {
    /*type of network communication*/
    public boolean isNetworkBle = false;
    public boolean isNetworkEth = true;

    /*Network-Ethernet*/
    private String server_address = "192.168.6.203";//remote ip address
    private Integer server_port =1200;//remote port
    private int socketTimeout=5000;//timeout

    public String getServer_address() {
        return server_address;
    }

    public Integer getServer_port() {
        return server_port;
    }

    public int getSocketTimeout() {
        return socketTimeout;
    }
}
