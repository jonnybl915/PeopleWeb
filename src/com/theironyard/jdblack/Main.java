package com.theironyard.jdblack;
import spark.ModelAndView;
import spark.Spark;
import spark.template.mustache.MustacheTemplateEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    static final int STEP = 20;

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
                    ArrayList tempList = new ArrayList<> (personList.subList(offset, offset+STEP));

                    HashMap map = new HashMap();
                    map.put("personList", tempList);
                    map.put("offsetUp", offset + STEP);
                    map.put("offsetDown", offset - STEP);
                    map.put("showPrev", offset > 0);
                    map.put("showNext", offset + STEP < personList.size());
                    return new ModelAndView(map, "home.html");
                },
                new MustacheTemplateEngine()
        );
        Spark.get(
                "/person",
                (request, response) -> {
                    int id = Integer.valueOf(request.queryParams("id"));
                    Person p = personList.get(id - 1);
                    return new ModelAndView(p, "person.html");
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
