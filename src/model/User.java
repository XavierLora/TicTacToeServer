package model;
import java.util.Objects;

public class User {
    private String username;
    private String password;
    private String displayName;
    private boolean online;

    public User(){
    }

    public User(String username, String password, String displayName, boolean online){
        this.username = username;
        this.password = password;
        this.displayName = displayName;
        this.online = online;
    }

    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
    public String getDisplayName(){
        return displayName;
    }
    public boolean isOnline(){
        return online;
    }

    public void setUsername(String username){
        this.username = username;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public void setOnline(boolean online){
        this.online = online;
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }else if(o==null || getClass() != o.getClass()){
            return false;
        }
        User user = (User) o;
        return username.equals(user.username);
    }

}