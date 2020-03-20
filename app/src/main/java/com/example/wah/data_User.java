package com.example.wah;
//Singleton class for User
public class data_User {
    private String email;
    private String userName;
    private String password;

    public String getAbout() {
        return About;
    }

    public void setAbout(String about) {
        About = about;
    }

    private String About;
    private static data_User user = null;
    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }


    private  data_User(String email,String username, String Password,String about)
    {
        this.email = email;
        this.userName = username;
        this.password = Password;
        this.About=about;
    }
    private data_User()
    {
        email=null;
        userName=null;
        password=null;
    }
    public static data_User getInstance(String email,String username, String Password,String about)
    {

        if(user == null)
        {
            return user=new data_User(email,username, Password,about);
        }
        return user;
    }
    public static data_User getInstance()
    {

        if(user == null)
        {
            return user=new data_User();
        }
        return user;
    }
}
