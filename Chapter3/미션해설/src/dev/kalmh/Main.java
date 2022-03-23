package dev.kalmh;

import dev.kalmh.basic.Lecturer;
import dev.kalmh.basic.Person;
import dev.kalmh.basic.Student;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class Main {

    public static void main(String[] args) {
	    //basic mission
        Person studentKim = new Student("kal", 28);
        Person studentLee = new Student("Lee", 30);
        Person studentPark = new Student("Park", 23);

        Person lecturer = new Lecturer("Kim", 35);

        List<Person> everyone = new Vector<>();
        everyone.add(studentKim);
        everyone.add(studentLee);
        everyone.add(studentPark);
        everyone.add(lecturer);

        for (Person person: everyone) {
            person.speak();
        }

        //challenge mission
        printItems(everyone);
    }

    public static <T> void printItems(Iterable<T> iterable) {
        Iterator<T> iterator = iterable.iterator();

        if (!iterator.hasNext()) {
            System.out.println("No elements.");
            return ;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("idx\t\titem\n");
        for(int i = 0; iterator.hasNext(); i++) {
            T item = iterator.next();
            sb.append(
                    String.format("%d\t\t%s\n", i, item.toString())
            );
        }
        System.out.println(sb.toString());
    }
}
