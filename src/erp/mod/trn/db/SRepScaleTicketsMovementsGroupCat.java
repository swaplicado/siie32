/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.trn.db;

import java.util.ArrayList;

/**
 *
 * @author Isabel Servín
 */
public class SRepScaleTicketsMovementsGroupCat {
    
    String nom;
    ArrayList<Object> inc;
    ArrayList<Object> exc;
    
    public String getNom() {
        return nom;
    }
    
    public void setNom(String nom) {
        this.nom = nom;
    }
    
    public ArrayList<Object> getInc() {
        return inc;
    }
    
    public void setInc(ArrayList<Object> inc) {
        this.inc = inc;
    }
    
    public ArrayList<Object> getExc() {
        return exc;
    }
    
    public void setExc(ArrayList<Object> exc) {
        this.exc = exc;
    }
}
