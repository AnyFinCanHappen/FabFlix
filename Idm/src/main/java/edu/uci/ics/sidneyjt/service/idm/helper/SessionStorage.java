package edu.uci.ics.sidneyjt.service.idm.helper;


import edu.uci.ics.sidneyjt.service.idm.security.Token;

import javax.xml.ws.Service;
import java.sql.Timestamp;

public class SessionStorage
{
    private String email;
    private Token sessionID;
    private Timestamp timeCreated;
    private Timestamp exprTime;
    private Timestamp lastUsed;
    private Integer status;

    public SessionStorage(){}
    public SessionStorage(String email, Token sessionID, Timestamp timeCreated, Timestamp exprTime, Timestamp lastUsed, Integer status) {
        this.email = email;
        this.sessionID = sessionID;
        this.timeCreated = timeCreated;
        this.exprTime = exprTime;
        this.lastUsed = lastUsed;
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }


    public String getEmail() {
        return email;
    }

    public Token getSessionID() {
        return sessionID;
    }

    public Timestamp getTimeCreated() {
        return timeCreated;
    }

    public Timestamp getExprTime() {
        return exprTime;
    }

    public Timestamp getLastUsed() {
        return lastUsed;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSessionID(Token sessionID) {
        this.sessionID = sessionID;
    }

    public void setTimeCreated(Timestamp timeCreated) {
        this.timeCreated = timeCreated;
    }

    public void setExprTime(Timestamp exprTime) {
        this.exprTime = exprTime;
    }

    public void setLastUsed(Timestamp lastUsed) {
        this.lastUsed = lastUsed;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
