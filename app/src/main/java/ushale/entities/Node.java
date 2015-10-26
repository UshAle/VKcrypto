package ushale.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by ushale on 04.03.15.
 */
public class Node {
    private List<Node> subnodes;
    private Map<String,String> params;
    public Node(){
        subnodes = new LinkedList<Node>();
        params = new HashMap<String,String>();
    }

    public void addNode(Node n){
        subnodes.add(n);
    }
    public List<Node> getSubNodes(){
        return subnodes;
    }
    public void addParam(String name,String value){
        params.put(name,value);
    }
    public String getParam(String name){
        return params.get(name);
    }
}
