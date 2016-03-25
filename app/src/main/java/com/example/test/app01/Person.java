package com.example.test.app01;

/**
 * Created by lushangqi on 2016/3/10.
 */

public class Person {
    private String id;
    private String name;
    private String role;
    private String email;
    private String passwd;
    private String create_date;
    private String update_date;
    private String image;
    public Person(String id, String name, String role) {
        this.id = id;
        this.name = name;
        this.role = role;
    }

    public Person(String id, String name, String email, String passwd, String create_date,
    String update_date, String image, String role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.passwd = passwd;
        this.create_date = create_date;
        this.update_date = update_date;
        this.image = image;
        this.role = role;
    }

    public String getId() {return id;}
    public String getName() {return name;}
    public String getEmail() {return email;}
    public String getPasswd() {return passwd;}
    public String getCreate_date() {return create_date;}
    public String getUpdate_date() {return update_date;}
    public String getImage() {return image;}
    public String getRole() {return role;}


    public void setId(String id) {this.id = id;}
    public void setaName(String name) {this.name = name;}
    public void setEmail(String email) {this.name = email;}
    public void setPasswd(String passwd) {this.name = passwd;}
    public void setCreate_date(String create_date) {this.name = create_date;}
    public void setUpdate_date(String update_date) {this.name = update_date;}
    public void setImage(String image) {this.name = image;}
    public void setRole(String role) {this.role = role;}


}
