package cn.carbs.android.wifiassistant.dummy;

import java.util.ArrayList;
import java.util.List;

import cn.carbs.android.wifiassistant.bean.BeanNetwork;

public class DummyData{

    public static class HistoryData{
        public static final List<BeanNetwork> NETWORKS = new ArrayList<>();
        static {
            for(int i = 0; i < 30; i++) {
                BeanNetwork bean = new BeanNetwork();
                bean.ssid = "ssid " + i;
                bean.psk = "psk " + i;
                NETWORKS.add(bean);
            }
        }
    }

}
