package edu.uci.ics.sidneyjt.service.idm.helper;

public class UserStorage
{
    private Integer user_id;
    private String email;
    private Integer status;
    private Integer plevel;
    private String salt;
    private String pword;

    public UserStorage(){}
    public UserStorage(Integer user_id, String email, Integer status, Integer plevel, String salt, String pword) {
        this.user_id = user_id;
        this.email = email;
        this.status = status;
        this.plevel = plevel;
        this.salt = salt;
        this.pword = pword;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public String getEmail() {
        return email;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getPlevel() {
        return plevel;
    }

    public String getSalt() {
        return salt;
    }

    public String getPword() {
        return pword;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setPlevel(Integer plevel) {
        this.plevel = plevel;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }
}
