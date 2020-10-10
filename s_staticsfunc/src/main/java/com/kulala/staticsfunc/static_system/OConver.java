package com.kulala.staticsfunc.static_system;


import java.util.ArrayList;
import java.util.List;

public class OConver {

	public static String StrToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 12288) {
				c[i] = (char) 32;
				continue;
			}
			if (c[i] > 65280 && c[i] < 65375)
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}
	public static List<String> ListGetLastNoRep(List<String> list, int lastCount) {
		if (list.size() <= lastCount)
			return list;
		List<String> last = new ArrayList<String>();
		for (int i = list.size() - 1; i < lastCount; i--) {
			boolean have = false;
			for (int j = 0; j < last.size(); j++) {
				if (last.get(j).equals(list.get(i)))
					have = true;
			}
			if (have == false) {
				last.add(list.get(i));
			}
		}
		return last;
	}
	/**取字串表**/
	public static String[] ListGetStringArr(List<String> strArr){
		if(strArr==null)return null;
		String str = "";
		for (int i = 0; i < strArr.size(); i++) {
			str += strArr.get(i);
			if(i<strArr.size()-1)str += "wer7ert5rty3tyu";
		}
		if(str.equals(""))return null;
		String[] arr;
		try{
			arr = str.split("wer7ert5rty3tyu");
		} catch (Exception e) {
			arr = new String[]{str};
		}
		return arr;
	}
	//List<String> list=Arrays.asList(array);
	//String[] strings = new String[list.size()];
	//list.toArray(strings);
	/**取字串表**/
	public static Integer[] StringArr2IntegerArr(String[] strArr){
		if(strArr==null)return null;
		Integer[] result = new Integer[strArr.length];
		String str = "";
		for (int i = 0; i < strArr.length; i++) {
			result[i] = Integer.valueOf(strArr[i]);
		}
		return result;
	}
	//==============================================
	// //////////////////////////////////////////////////////
	/** String[] 检测相似 **/
	public static String[] StringArrCheckLike(String[] arr, String likeValue) {
		String str = "";
		char[] chr = likeValue.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			String strObj = arr[i];
			char[] objChr = strObj.toCharArray();
			boolean isLike = true;
			for (int j = 0; j < chr.length; j++) {
				if (chr[j] != objChr[j]) {
					isLike = false;
				}
			}
			if (isLike == true) {
				str += arr[i];
				if (i < arr.length - 1)
					str += "*#*#753#*#*";
			}
		}
		return str.split("*#*#753#*#*");
	}
	// //////////////////////////////////////////////////////
	//=======================
}
