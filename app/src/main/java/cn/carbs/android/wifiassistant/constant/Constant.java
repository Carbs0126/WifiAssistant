package cn.carbs.android.wifiassistant.constant;

/**
 * Created by Rick.Wang on 2016/7/13.
 */
public class Constant {

    public static class WifiAssistant{
        public static final String PACKAGE_NAME = "cn.carbs.android.wifiassistant";
    }

    public static class WifiConfig{
        public static final String WPA_FILE_ABS_NAME = "/data/misc/wifi/wpa_supplicant.conf";
    }

    public static class FileConfig{
        public static final String HISTORY_STORAGE_FILE_ABS_NAME = "/data/data/" + WifiAssistant.PACKAGE_NAME + "/wifi_config";
    }

    public static class History{
        public static final String DATA = "DATA";
    }

    public static class SuFeedbackCode{
        public static final int SUCCESS = 0;
        public static final int FAIL_SU = 1;
        public static final int FAIL_COPY_FILE = 2;
        public static final int FAIL_UNKNOWN_EXCEPTION = 3;
    }

}
