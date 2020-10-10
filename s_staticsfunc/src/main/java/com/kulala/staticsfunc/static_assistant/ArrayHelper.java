package com.kulala.staticsfunc.static_assistant;

import java.util.List;
public class ArrayHelper {
    public static String arr2Str(Object[] arr){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < arr.length; i++){
            sb. append(arr[i].toString()+",");
        }
        return sb.toString();
    }

    /**
     * @return -1 相同的数据 -2 size同值不同 0-999新增的点 1000-1999 减少的点
     */
    public static int checkListStrDiff(List<String> newList,List<String> resList,String uncheckStr){
        if(newList.size() == resList.size()){//相同的
            if(newList.size()==0)return -1;
            for(int i = 0;i<newList.size();i++){
                if(!newList.get(i).equals(resList.get(i)))return -2;
            }
            return -1;
        }else if(newList.size() > resList.size()){//增加的
            for(int i = 0;i<newList.size();i++){
                if(i>=resList.size())return i;
                if(!newList.get(i).equals(resList.get(i)) && !newList.get(i).contains(uncheckStr))return i;
            }
        }else if(newList.size() < resList.size()){//减少的
            for(int i = 0;i<resList.size();i++){
                if(i>=newList.size())return 1000+i;
                if(!newList.get(i).equals(resList.get(i)) && !resList.get(i).contains(uncheckStr))return 1000+i;
            }
        }
        return 3000;//不确定的值
    }
}
