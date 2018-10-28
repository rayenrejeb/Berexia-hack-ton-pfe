package com.zhuinden.sparkexperiment;

import java.util.*;

public class Node {

    private int index;
    private String id;
    private String name;
    private String req;
    private List<String> in;
    private List<String> out;

    public Node() { }

    public Node(String id, String name, String req, List<String> in, List<String> out) {
        this.id = id;
        this.name = name;
        this.req = req;
        this.in = in;
        this.out = out;
    }

    public Node(HashMap<String,String> propertiesMap) {
        this.id = propertiesMap.getOrDefault("id", null);
        this.name = propertiesMap.getOrDefault("name", null);
        this.req = propertiesMap.getOrDefault("req", null);
        this.in = new ArrayList<String>();
        this.in.add(Collections.singletonList(propertiesMap.getOrDefault("inNode", "")).toString());
        this.out = new ArrayList<String>();
        this.out.add(Collections.singletonList(propertiesMap.getOrDefault("outNode", "")).toString());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReq() {
        return req;
    }

    public void setReq(String req) {
        this.req = req;
    }

    public List<String> getIn() {
        return in;
    }

    public void setIn(List<String> in) {
        this.in = in;
    }

    public List<String> getOut() {
        return out;
    }

    public void setOut(List<String> out) {
        this.out = out;
    }

    public int getIndex() { return this.index; }

    public void setIndex(int index){ this.index = index; }
}
