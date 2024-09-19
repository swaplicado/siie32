/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.link.db;

import java.util.ArrayList;

/**
 *
 * @author Cesar Orozco
 */
public class SPersonalInfoResponse {
    private ArrayList<SClassHrsCat> classHrsCat;
    private ArrayList<STpHrsCat> tpHrsCat;
    private ArrayList<SLocuSta> locuSta;
    private ArrayList<STpTel> tpTel;
    private SPersonalInfo personalInfo;
    private int id_bp;
    private int id_bpb;
    private int id_add;
    private int id_con;

    public ArrayList<SClassHrsCat> getClassHrsCat() {
        return classHrsCat;
    }

    public ArrayList<STpHrsCat> getTpHrsCat() {
        return tpHrsCat;
    }

    public ArrayList<SLocuSta> getLocuSta() {
        return locuSta;
    }

    public ArrayList<STpTel> getTpTel() {
        return tpTel;
    }

    public SPersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public int getId_bp() {
        return id_bp;
    }

    public int getId_bpb() {
        return id_bpb;
    }

    public int getId_add() {
        return id_add;
    }

    public int getId_con() {
        return id_con;
    }
    
    public void setClassHrsCat(ArrayList<SClassHrsCat> classHrsCat) {
        this.classHrsCat = classHrsCat;
    }

    public void setTpHrsCat(ArrayList<STpHrsCat> tpHrsCat) {
        this.tpHrsCat = tpHrsCat;
    }

    public void setLocuSta(ArrayList<SLocuSta> locuSta) {
        this.locuSta = locuSta;
    }

    public void setTpTel(ArrayList<STpTel> tpTel) {
        this.tpTel = tpTel;
    }

    public void setPersonalInfo(SPersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public void setId_bp(int id_bp) {
        this.id_bp = id_bp;
    }

    public void setId_bpb(int id_bpb) {
        this.id_bpb = id_bpb;
    }

    public void setId_add(int id_add) {
        this.id_add = id_add;
    }

    public void setId_con(int id_con) {
        this.id_con = id_con;
    }
    
}
