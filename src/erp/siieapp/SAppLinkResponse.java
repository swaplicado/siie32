/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.siieapp;

import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SAppLinkResponse {

    int code;
    String message;
    String link;
    String folio;
    ArrayList<Integer> nextUsers;

    public SAppLinkResponse() {
        code = 0;
        message = "";
        link = "";
        folio = "";
        nextUsers = new ArrayList<>();
    }

    public SAppLinkResponse(int code, String message, String link, ArrayList<Integer> nextUsers) {
        this.code = code;
        this.message = message;
        this.link = link;
        this.nextUsers = nextUsers;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public ArrayList<Integer> getNextUsers() {
        return nextUsers;
    }

    public void setNextUsers(ArrayList<Integer> nextUsers) {
        this.nextUsers = nextUsers;
    }
}
