package cn.carbs.android.wifiassistant.io;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

import cn.carbs.android.wifiassistant.bean.BeanNetwork;
import cn.carbs.android.wifiassistant.constant.Constant;

/**
 * Created by Rick.Wang on 2016/7/14.
 */
public class ReadNetworks {
    private static final String TAG = "ReadNetworks";

    private static final HashMap<String, Field> HASH_MAP_FIELDS = new HashMap<>();

    static {
        Field[] fields = BeanNetwork.class.getDeclaredFields();
        for(Field field : fields){
            HASH_MAP_FIELDS.put(field.getName(), field);
        }
    }

    public static int processSuAndCopyFile(String copyTofilePath){
        try {
            Runtime runtime = Runtime.getRuntime();
            Process process = runtime.exec("su");
            OutputStream outputStream = process.getOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
            dataOutputStream.writeBytes("cat " + Constant.WifiConfig.WPA_FILE_ABS_NAME
                    + ">" + copyTofilePath + "\n");
            dataOutputStream.writeBytes("chmod 777 " + copyTofilePath + "\n");
            dataOutputStream.writeBytes("exit\n");
            process.waitFor();
            if(process.exitValue() == 0xFF){
                return Constant.SuFeedbackCode.FAIL_SU;
            }
            File file = new File(copyTofilePath);
            if(!file.exists()){
                return Constant.SuFeedbackCode.FAIL_COPY_FILE;
            }
        }catch (Exception e){
            return Constant.SuFeedbackCode.FAIL_UNKNOWN_EXCEPTION;
        }
        return Constant.SuFeedbackCode.SUCCESS;
    }

    public static ArrayList<BeanNetwork> readNetworksHistory(String filePath){
        ArrayList<BeanNetwork> networks = new ArrayList<>();
        try {
            FileReader fr = new FileReader(filePath);//"c:\\wifi.txt"
            BufferedReader bf = new BufferedReader(fr);
            String currLine;
            while ((currLine = bf.readLine()) != null){
                if(currLine != null && currLine.trim().toLowerCase().startsWith("network")){
                    BeanNetwork bean = new BeanNetwork();
                    while ((currLine = bf.readLine()) != null){
                        if(currLine.trim().endsWith("}")){
                            networks.add(bean);
                            break;
                        }
                        String[] ss = currLine.split("=");
                        if(ss != null && ss.length == 2){
                            Field f = HASH_MAP_FIELDS.get(ss[0].trim());
                            if(f != null){
                                f.set(bean, getValue(ss[1]));
                            }else{
                                Log.d(TAG,"fieldHashMap.get(ss[0]) == null ss[0] : " + ss[0]);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return networks;
    }

    private static String getValue(String rawValue){
        if(rawValue == null){
            return rawValue;
        }
        String trimedRawValue = rawValue.trim();
        if(trimedRawValue.startsWith("\"") && trimedRawValue.endsWith("\"") && trimedRawValue.length() > 1){
            return trimedRawValue.substring(1,trimedRawValue.length()-1);
        }
        return trimedRawValue;
    }

}
