package dev.kalmh.controller.demo;

public class SamplePayload {
    private String name;
    private int age;
    private String occupation;

    public SamplePayload(String name, int age, String occupation) {
        this.name = name;
        this.age = age;
        this.occupation = occupation;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SamplePayload{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", Occuptation='" + occupation + '\'' +
                '}';
    }
}
