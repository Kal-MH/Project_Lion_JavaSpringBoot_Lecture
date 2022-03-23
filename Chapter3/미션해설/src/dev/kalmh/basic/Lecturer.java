package dev.kalmh.basic;

public class Lecturer extends AbstractPerson{
    public Lecturer(String name, int age) {
        super(name, age);
    }

    @Override
    public void speak() {
        System.out.println(String.format("My name is %s, and I am the lecturer of this class", getName()));
    }
}
