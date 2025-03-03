/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.musr.data;

import org.json.simple.JSONObject;

/**
 *
 * @author Adrián Avilés
 */
public class SDataUserExport {
    protected int userId;
    protected String username;
    protected String email;
    protected String password;
    protected String lastName;
    protected String names;
    protected String fullName;
    protected JSONObject lApps;
    
    public SDataUserExport() {
        
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setlApps(JSONObject lApps) {
        this.lApps = lApps;
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getLastName() {
        return lastName;
    }

    public String getNames() {
        return names;
    }

    public String getFullName() {
        return fullName;
    }

    public JSONObject getlApps() {
        return lApps;
    }
}
