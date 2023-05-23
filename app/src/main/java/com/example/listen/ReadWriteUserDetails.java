package com.example.listen;

public class ReadWriteUserDetails {
    public String username, email, password;
    public ReadWriteUserDetails(){};
    public ReadWriteUserDetails(String textfullusername, String textemail, String textpassword){
        this.username = textfullusername;
        this.email = textemail;
        this.password = textpassword;
    }
}
