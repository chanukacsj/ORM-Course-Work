package org.example.dto.tm;

public class StudentTM {
    private String id;
    private String name;
    private String address;
    private String contact;
    private String dob;
    private String gen;
    private String regDate;

    public StudentTM(String id, String name, String address, String contact, String dob, String gen, String regDate) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.contact = contact;
        this.dob = dob;
        this.gen = gen;
        this.regDate = regDate;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getGen() {
        return gen;
    }

    public void setGen(String gen) {
        this.gen = gen;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }


    @Override
    public String toString() {
        return "StudentTM{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contact='" + contact + '\'' +
                ", dob='" + dob + '\'' +
                ", gen='" + gen + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }
}
