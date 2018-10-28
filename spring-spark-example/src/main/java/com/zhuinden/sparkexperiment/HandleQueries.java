package com.zhuinden.sparkexperiment;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HandleQueries {

    @Autowired
    private ConvertXLXSToCSV convertXLXSToCSV;

    private String path = "/home/rayen/Documents/spring-spark-example/src/main/resources/";

    //id du noeud précédent
    Map<String, Dataset<Row>> listOfCsvInSpark = new HashMap<String, Dataset<Row>>();

    @Async
    public void loadCsv(SparkSession sparkSession, Node node) {
        String filePath = path + node.getName();
        System.out.println(filePath);
        String extension = filePath.substring(filePath.lastIndexOf("."));

        switch (extension) {
            case ".csv":
                try {
                    String nameOfTable = node.getId().replaceAll(" ","").replaceAll(".xslx","").replaceAll(".csv","").replaceAll("/","");
                    Dataset<Row> csv = sparkSession.read().format("csv").option("header", "true").option("dateFormat", "dd-MM-yyyy").option("delimiter", ";").option("quote", "")
                            .option("inferSchema", "true").load("/home/rayen/Documents/spring-spark-example/src/main/resources/Korea Policy File 100k.csv");
                    csv.createOrReplaceTempView(nameOfTable);

                    listOfCsvInSpark.put(filePath.substring((filePath.lastIndexOf("/") + 1)).replace(" ", "").replaceAll(".csv", "").replaceAll(".xlsx", ""), csv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            case ".xlsx":
                try {
                    convertXLXSToCSV.convertSelectedSheetInXLXSFileToCSV(filePath, 0);
                    String newPathOfCsvFile = filePath.substring(0, filePath.lastIndexOf(".")) + ".csv";
                    Dataset<Row> csv = sparkSession.read().format("csv").option("header", "true").option("delimiter", ";").option("quote", "")
                            .option("inferSchema", "true").load(newPathOfCsvFile);
                    csv.createOrReplaceTempView(filePath.substring((filePath.lastIndexOf("/") + 1)).replaceAll(" ", "_").replaceAll(".csv", "").replaceAll(".xlsx", ""));
                    listOfCsvInSpark.put(node.getId().replace(" ", "").replaceAll(".csv", "").replaceAll(".xlsx", ""), csv);
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }
    //This methode is used to execute select queries
    public void executeSelect(SparkSession sparkSession, Node node, Node selectNode) {
        String tableName = node.getId().replaceAll(" ","").replaceAll(".csv","").replaceAll(".xslx","").replaceAll("/","");
        String query = selectNode.getReq().replace("?", tableName);
                System.out.println(query);
        Dataset<Row> sel = sparkSession.sql(query);
        sel.createOrReplaceTempView(selectNode.getId().replaceAll(" ","").replaceAll(".csv","").replaceAll(".xslx","").replaceAll("/",""));
        sel.createOrReplaceTempView("result");

        listOfCsvInSpark.put(selectNode.getId().replaceAll(" ", "_").replaceAll(".csv", "").replaceAll(".xlsx", ""),sel);
        sel.show(3, false);
    }

    //This methode is used to Combine two streams
    public void executeCombine(SparkSession sparkSession, Node node,Map<String, Node> all) {
        List<String> listOfTablesToCombine = new ArrayList<String>();
        for(String nameOfTable : node.getIn())
            listOfTablesToCombine.add(all.get(nameOfTable).getId().replaceAll(" ","").replaceAll(".csv","").replaceAll(".xslx","").replaceAll("/",""));
        String columnsToCombine[] = node.getReq().split(",");
        System.out.println("Combine : " + listOfTablesToCombine.get(1));
        listOfCsvInSpark.get(listOfTablesToCombine.get(0)).join(listOfCsvInSpark.get(listOfTablesToCombine.get(1)), listOfCsvInSpark.get(listOfTablesToCombine.get(0)).col(columnsToCombine[0]).equalTo(listOfCsvInSpark.get(listOfTablesToCombine.get(1)).col(columnsToCombine[1])))
            .createOrReplaceTempView("result");
    }
}