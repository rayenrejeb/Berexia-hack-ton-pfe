package com.zhuinden.sparkexperiment;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
/**
 * This Class is used to handle the graph from the client side and calculate the order of actions treatment.
 * @author Rayen Rejeb and Safwen Trabelsi
 * @version 1.0.0
 */


// This class represents a directed graph using adjacency
// list representation
class Pair {
    public int pair1;
    public int pair2;
}

@Service
public class HandleGraph {


    @Autowired
    SparkSession sparkSession;

    @Autowired
    private HandleQueries handleQueries;

    @Value("files.path")
    private String Path;

    private Map<Integer, Node> nodeByIndex = new HashMap<Integer, Node>();
    private Map<Integer, Integer> links = new HashMap<Integer, Integer>();

    int newIndex = 0;

    public Dataset<Row> executeGraph(Map<String, Node> all){
        all.forEach((id, node) -> {
                node.setIndex(newIndex);
                nodeByIndex.put(newIndex,node);
                newIndex++;
        });
        nodeByIndex.forEach((id, node) -> {
            if(node.getOut() != null) {
                for(String text : node.getOut()){
                    if(text != ""){
                        links.put(id,all.get(text).getIndex());
                    }
                }
            }
        });
        Graph g = new Graph(all.size());
            links.forEach((u,v) -> {
                g.addEdge(u, v);
            });

        List<Integer> result = g.topologicalSort();
        for(Integer index : result){
            System.out.println(index + " , " + nodeByIndex.get(index).getName());
        }

        //traiter noeud par noeud
        for(Integer index : result){
            String type = getIdOfNodeFromIndex(index).getName();
            if(type.contains("select")){
                Node precedentNode = all.get(getIdOfNodeFromIndex(index).getIn().get(0));
                handleQueries.executeSelect(sparkSession,precedentNode, getIdOfNodeFromIndex(index));
            }else if(type.contains("csv") || type.contains("xlsx")){
                handleQueries.loadCsv(sparkSession,getIdOfNodeFromIndex(index));
            }else if(type.contains("combine")){
                System.out.println("start combine");
                handleQueries.executeCombine(sparkSession,getIdOfNodeFromIndex(index), all);
            }
        }
        System.out.println("resultat");
        Dataset<Row> liste =  sparkSession.sql("Select * from result");
        System.out.println("size : !" + liste);
        List<String> finalResult = new ArrayList<String>();
        liste.foreach(data -> {
            //System.out.println(data);
        });

            System.out.println("eeeee : " + finalResult.size());


        return liste;

    }

    public Node getIdOfNodeFromIndex(int i){
        return nodeByIndex.get(i);
    }
}
