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


    static ArrayList<Person> personList = new ArrayList<>();
    public static void main(String[] args) throws FileNotFoundException {
        readPeopleFile();
        Spark.init();
        Spark.get(
                "/",
                (request, response) -> {
                    HashMap map = new HashMap();

                    map.put("personList", personList);
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
