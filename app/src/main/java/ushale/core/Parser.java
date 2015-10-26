package ushale.core;

import java.util.LinkedList;
import java.util.List;

import ushale.secvk.MainActivity;

/**
 * Created by ushale on 04.03.15.
 */
public class Parser {
    public static String[] cutThis(String str, String first, String sec){

        MainActivity ma = Core.getInstance().getMainActivity();
        return null;
    }

    public List<String> getArgs(String response,String dil, String arg){
        String[] result = response.split(dil);
        List<String> ret = new LinkedList<String>();
        for(int i = 0; i<result.length; i++){
            if(result[i].equals(arg)){
                ret.add(result[i+1]);
            }
        }
        return ret;
    }
}
