package com.theironyard.jdblack;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class Main {


    static ArrayList<Person> personList = new ArrayList<>();
    public static void main(String[] args) throws FileNotFoundException {
        readPeopleFile();
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    int offset = 0;
                    String offStr = request.queryParams("offset");
                    if (offStr != null){
                        offset = Integer.valueOf(offStr);
                    }
                    ArrayList tempList = new ArrayList<> (personList.subList(offset, offset+20));

                    HashMap map = new HashMap();
                    map.put("personList", tempList);
                    map.put("offsetUp", offset + 20);
                    map.put("offsetDown", offset - 20);
                    return new ModelAndView(map, "home.html");
                },
                new MustacheTemplateEngine()
        );
    }
    public static void readPeopleFile() throws FileNotFoundException {

        File f = new File("people.csv");
        Scanner fileScanner = new Scanner(f);
        fileScanner.nextLine();
        while (fileScanner.hasNext()) {
            String[] columns = fileScanner.nextLine().split(",");
            Person person = new Person(Integer.valueOf(columns[0]), columns[1], columns[2], columns[3], columns[4], columns[5]);
            personList.add(person);
        }
    }
}
