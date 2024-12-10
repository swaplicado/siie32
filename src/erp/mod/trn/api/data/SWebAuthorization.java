/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.api.data;

import java.util.ArrayList;

/**
 *
 * @author Edwin Carmona
 */
public class SWebAuthorization {

    private int idAuthStatus;
    private String authStatusName;
    private String lastActionAt;
    private ArrayList<SWebAuthStep> lSteps;
    private ArrayList<Integer> lUsersInTurn;

    public SWebAuthorization() {
        this.idAuthStatus = 0;
        this.authStatusName = "";
        this.lSteps = new ArrayList<>();
        this.lUsersInTurn = new ArrayList<>();
    }

    public int getIdAuthStatus() {
        return idAuthStatus;
    }

    public void setIdAuthStatus(int idAuthStatus) {
        this.idAuthStatus = idAuthStatus;
    }

    public String getAuthStatusName() {
        return authStatusName;
    }

    public void setAuthStatusName(String authStatusName) {
        this.authStatusName = authStatusName;
    }

    public String getLastActionAt() {
        return lastActionAt;
    }

    public void setLastActionAt(String lastActionAt) {
        this.lastActionAt = lastActionAt;
    }

    public ArrayList<SWebAuthStep> getlSteps() {
        return lSteps;
    }

    public void setlSteps(ArrayList<SWebAuthStep> lSteps) {
        this.lSteps = lSteps;
    }

    public ArrayList<Integer> getlUsersInTurn() {
        return lUsersInTurn;
    }

    public void setlUsersInTurn(ArrayList<Integer> lUsersInTurn) {
        this.lUsersInTurn = lUsersInTurn;
    }
}
