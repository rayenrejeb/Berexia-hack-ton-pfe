package com.zhuinden.sparkexperiment.Controller;

import com.zhuinden.sparkexperiment.HandleGraph;
import com.zhuinden.sparkexperiment.HandleQueries;
import com.zhuinden.sparkexperiment.Node;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.web.bind.annotation.*;


import java.io.*;
import java.util.*;

/**
 * Created on 2018.10.28
 */
//@CrossOrigin(origins = {"${allowedUrl}"}, maxAge = 3600)
@RequestMapping("/api")
@RestController
public class ApiController {

@Value("${files.path}")
    private String Path;

    @Autowired
    private HandleGraph handleGraph;

    @Autowired
    private HandleQueries handleQueries;

    @PostMapping("/execute")
    public List<List<String>>  execute(@RequestBody String data) throws ParseException {

        /**
         * This is to simulate the front app and it respond to the server with the desired data
         */
/*
        JSONParser parser = new JSONParser();
        JSONArray json = (JSONArray) parser.parse(data);

        Map<String, Node> allNodess = new HashMap<String, Node>();

        for(int i = 0; i < json.size(); i++){
            HashMap<String,String> listNodes = (HashMap<String,String>)json.get(i);
            Node node = new Node(listNodes);
            allNodess.put(node.getId(),node);
        }
*/
        //This Map will handle every Node mapped with it's ID
        Map<String, Node> allNodes = new HashMap<String, Node>();

        //This Nodes are used to Simulate the input from the Front-end Application
        //It's just a matter of cast that we couldn't figure it out in the last second
        Node a = new Node(
                "ra54s2zsd",
                "select",
                "select Life_ID from ? where Age_at_Commencement > 20",
                Arrays.asList("a54dz1ce9dc"),
                Arrays.asList("a54dz1ceddff9dc"));
        Node b = new Node(
                "a54dz1ce9dc",
                "Korea Policy File 100k.csv",
                "",
                null,
                Arrays.asList("ra54s2zsd"));
        Node c = new Node(
                "ra54s2zffsd",
                "select",
                "select * from ? where Age_at_Commencement > 20",
                Arrays.asList("a54dz1ceff9dc"),
                Arrays.asList("a54dz1ceddff9dc"));
        Node d = new Node(
                "a54dz1ceff9dc",
                "Korea Policy File 100k1.csv",
                "",
                null,
                Arrays.asList("ra54s2zffsd"));
        Node e = new Node(
                "a54dz1ceddff9dc",
                "combine",
                "Life_ID,Life_ID",
                Arrays.asList("ra54s2zffsd","ra54s2zsd"),
                null);



        allNodes.put(a.getId(), a);
        allNodes.put(b.getId(), b);
        allNodes.put(c.getId(), c);
        allNodes.put(d.getId(), d);
        allNodes.put(e.getId(), e);

        List<List<String>> finalResult = new ArrayList<List<String>>();
         handleGraph.executeGraph(allNodes).collectAsList().forEach(dsata ->{
             List<String> res = new ArrayList<String>();

             for(int i = 0; i < dsata.size(); i++){
                 res = new ArrayList<String>();
                 res.add(dsata.toString());
             }
             finalResult.add(res);

         });
        return finalResult;

    }

    @GetMapping("/getAllFiles")
    public Map<String,String[]> getAllFiles(){
        System.out.println(Path);
        File folder = new File(Path);
        File[] listOfFiles = folder.listFiles();

        Map<String,String[]> result = new HashMap<String, String[]>();

        BufferedReader bufferedReader = null;

        for (int i = 0; i < listOfFiles.length; i++) {
            if ((listOfFiles[i].isFile()) && listOfFiles[i].getName().contains("csv")) {

                try {
                    bufferedReader = new BufferedReader(new FileReader(this.Path + listOfFiles[i].getName()));
                    String line = bufferedReader.readLine();
                    String[] split = line.split(";");
                    result.put(listOfFiles[i].getName(),split);

                }catch (IOException e){
                    e.printStackTrace();
                }finally {
                    if(bufferedReader != null)
                        try{
                            bufferedReader.close();
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                }
            }else if ((listOfFiles[i].isFile()) && listOfFiles[i].getName().contains("xlsx")){
                try{
                    InputStream ExcelFileToRead = new FileInputStream(this.Path + listOfFiles[i].getName());
                    XSSFWorkbook wb = new XSSFWorkbook(ExcelFileToRead);
                    XSSFWorkbook test = new XSSFWorkbook();
                    XSSFSheet sheet = wb.getSheetAt(0);
                    XSSFRow row;
                    XSSFCell cell;
                    Iterator rows = sheet.rowIterator();
                    row=(XSSFRow) rows.next();
                    Iterator cells = row.cellIterator();
                    List<String> split = new ArrayList<String>();
                    while (cells.hasNext()){
                        cell=(XSSFCell) cells.next();
                        if (cell.getCellType() == XSSFCell.CELL_TYPE_STRING)
                        {
                            split.add(cell.getStringCellValue()+" ");
                        }
                        else if(cell.getCellType() == XSSFCell.CELL_TYPE_NUMERIC)
                        {
                            split.add(cell.getNumericCellValue()+" ");
                        }
                    }
                    String[] columns = new String[split.size()];
                    columns = split.toArray(columns);
                    result.put(listOfFiles[i].getName(),columns);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return result;
    }
}

