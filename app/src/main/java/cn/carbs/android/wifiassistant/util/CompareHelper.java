package cn.carbs.android.wifiassistant.util;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Rick.Wang on 2016/7/15.
 */
public class CompareHelper {

    public static final Collator COLLATOR = Collator.getInstance();
    public static final Comparator<String> COMPARATOR_STRING_ASC;
    public static final Comparator<String> COMPARATOR_STRING_DESC;

    static
    {
        COMPARATOR_STRING_ASC = new Comparator<String>(){
            public final int compare(String a, String b){
                return COLLATOR.compare(a, b);
            }
        };

        COMPARATOR_STRING_DESC = new Comparator<String>(){
            public final int compare(String a, String b){
                return COLLATOR.compare(b, a);
            }
        };
    }

    private CompareHelper(){}

    /**
     * 获取从all队列剔除ignores后的队列
     * @param listAlls
     * @param listIgnores
     * @return
     */
    public static <E extends Comparable> ArrayList<E> getSurvives(ArrayList<E> listAlls, ArrayList<E> listIgnores){

        ArrayList<E> listAllSurvives = new ArrayList();

        int sizeAll = listAlls.size();
        int sizeIgnore = listIgnores.size();
        int indexAll = 0;
        int indexIgnore = 0;

        for(; indexIgnore < sizeIgnore; ){
            for(; indexAll < sizeAll;){

                int compareRet = listIgnores.get(indexIgnore).compareTo(listAlls.get(indexAll));

                if(compareRet < 0){
                    indexIgnore ++;
                }else if(compareRet == 0){
//					indexIgnore ++;
                    indexAll++;
                }else if(compareRet > 0){
                    listAllSurvives.add(listAlls.get(indexAll));
                    indexAll++;
                }

                if(indexIgnore >= sizeIgnore){
                    for(int i = indexAll; i < sizeAll; i++){
                        listAllSurvives.add(listAlls.get(i));
                    }
                    break;
                }
            }
            if(indexAll >= sizeAll){
                break;
            }
        }
        return listAllSurvives;
    }

    public static boolean checkStringEqual(String a, String b){
        if(a == null && b == null){
            return true;
        }else if(a == null && b != null){
            return false;
        }else{
            return a.equals(b);
        }
    }
}
