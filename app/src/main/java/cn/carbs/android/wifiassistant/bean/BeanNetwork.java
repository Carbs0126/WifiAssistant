package cn.carbs.android.wifiassistant.bean;

/**
 * Created by Carbs.Wang on 2016/7/13.
 */
public class BeanNetwork {

    public String ssid;
    public String scan_ssid;
    public String psk;
    public String key_mgmt;
    public String sim_slot;
    public String imsi;
    public String priority;
    public String auth_alg;
    public String wep_key0;

    @Override
    public String toString() {
        return "[BeanNetwork--> ssid:" + ssid
                + " psk:"+ psk
                + " scan_ssid:" + scan_ssid
                + " key_mgmt:" + key_mgmt
                + " sim_slot:" + sim_slot
                + " imsi:" + imsi
                + " priority:" + priority
                + " auth_alg:" + auth_alg
                + " wep_key0:" + wep_key0 + " ]";
    }

}
