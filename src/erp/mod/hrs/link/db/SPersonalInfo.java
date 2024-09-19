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
public class SPersonalInfo {
    private int numEmployee;
    private String firstName;
    private String lastName;
    private String rfc;
    private int idBpb;
    private int idAdd;
    private int idCon;
    private String telNumber01;
    private String telNumber02;
    private String telExt02;
    private String email01;
    private String email02;
    private String street;
    private String streetNumExt;
    private String streetNumInt;
    private String neighborhood;
    private String locality;
    private String county;
    private String zipCode;
    private String zipCodeFiscal;
    private String reference;
    private int fidSta;
    private int sexCl;
    private int sexTp;
    private int bloodCl;
    private int bloodTp;
    private int maritalCl;
    private int maritalTp;
    private int educationCl;
    private int educationTp;
    private String mate;
    private String dtBirMate;
    private int sexMateTp;
    private int sexMateCl;
    private String son1;
    private String dtBirSon1;
    private int sexSonTp1;
    private int sexSonCl1;
    private String son2;
    private String dtBirSon2;
    private int sexSonTp2;
    private int sexSonCl2;
    private String son3;
    private String dtBirSon3;
    private int sexSonTp3;
    private int sexSonCl3;
    private String son4;
    private String dtBirSon4;
    private int sexSonTp4;
    private int sexSonCl4;
    private String son5;
    private String dtBirSon5;
    private int sexSonTp5;
    private int sexSonCl5;
    private int emergCl;
    private int emergTp;
    private String emergsCon;
    private String emergsTel;
    private String benefs;

    public int getNumEmployee() {
        return numEmployee;
    }

    public String getTelNumber01() {
        return telNumber01;
    }

    public String getTelNumber02() {
        return telNumber02;
    }

    public String getTelExt02() {
        return telExt02;
    }

    public String getEmail01() {
        return email01;
    }

    public String getEmail02() {
        return email02;
    }

    public String getStreet() {
        return street;
    }

    public String getStreetNumExt() {
        return streetNumExt;
    }

    public String getStreetNumInt() {
        return streetNumInt;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getLocality() {
        return locality;
    }

    public String getCounty() {
        return county;
    }

    public String getZipCode() {
        return zipCode;
    }

    public int getFidSta() {
        return fidSta;
    }

    public int getMaritalCl() {
        return maritalCl;
    }

    public int getMaritalTp() {
        return maritalTp;
    }

    public int getEducationCl() {
        return educationCl;
    }

    public int getEducationTp() {
        return educationTp;
    }

    public String getMate() {
        return mate;
    }

    public String getDtBirMate() {
        return dtBirMate;
    }

    public int getSexMateTp() {
        return sexMateTp;
    }

    public int getSexMateCl() {
        return sexMateCl;
    }

    public String getSon1() {
        return son1;
    }

    public String getDtBirSon1() {
        return dtBirSon1;
    }

    public int getSexSonTp1() {
        return sexSonTp1;
    }

    public int getSexSonCl1() {
        return sexSonCl1;
    }

    public String getSon2() {
        return son2;
    }

    public String getDtBirSon2() {
        return dtBirSon2;
    }

    public int getSexSonTp2() {
        return sexSonTp2;
    }

    public int getSexSonCl2() {
        return sexSonCl2;
    }

    public String getSon3() {
        return son3;
    }

    public String getDtBirSon3() {
        return dtBirSon3;
    }

    public int getSexSonTp3() {
        return sexSonTp3;
    }

    public int getSexSonCl3() {
        return sexSonCl3;
    }

    public String getSon4() {
        return son4;
    }

    public String getDtBirSon4() {
        return dtBirSon4;
    }

    public int getSexSonTp4() {
        return sexSonTp4;
    }

    public int getSexSonCl4() {
        return sexSonCl4;
    }

    public String getSon5() {
        return son5;
    }

    public String getDtBirSon5() {
        return dtBirSon5;
    }

    public int getSexSonTp5() {
        return sexSonTp5;
    }

    public int getSexSonCl5() {
        return sexSonCl5;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getRfc() {
        return rfc;
    }

    public int getSexCl() {
        return sexCl;
    }

    public int getSexTp() {
        return sexTp;
    }

    public int getBloodCl() {
        return bloodCl;
    }

    public int getBloodTp() {
        return bloodTp;
    }

    public int getEmergCl() {
        return emergCl;
    }

    public int getEmergTp() {
        return emergTp;
    }

    public String getEmergsCon() {
        return emergsCon;
    }

    public String getEmergsTel() {
        return emergsTel;
    }

    public String getBenefs() {
        return benefs;
    }

    public int getIdBpb() {
        return idBpb;
    }

    public int getIdAdd() {
        return idAdd;
    }

    public int getIdCon() {
        return idCon;
    }

    public String getReference() {
        return reference;
    }

    public String getZipCodeFiscal() {
        return zipCodeFiscal;
    }
    
    public void setNumEmployee(int numEmployee) {
        this.numEmployee = numEmployee;
    }

    public void setTelNumber01(String telNumber01) {
        this.telNumber01 = telNumber01;
    }

    public void setTelNumber02(String telNumber02) {
        this.telNumber02 = telNumber02;
    }

    public void setTelExt02(String telExt02) {
        this.telExt02 = telExt02;
    }

    public void setEmail01(String email01) {
        this.email01 = email01;
    }

    public void setEmail02(String email02) {
        this.email02 = email02;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNumExt(String streetNumExt) {
        this.streetNumExt = streetNumExt;
    }

    public void setStreetNumInt(String streetNumInt) {
        this.streetNumInt = streetNumInt;
    }

    public void setNeighborhood(String neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setFidSta(int fidSta) {
        this.fidSta = fidSta;
    }

    public void setMaritalCl(int maritalCl) {
        this.maritalCl = maritalCl;
    }

    public void setMaritalTp(int maritalTp) {
        this.maritalTp = maritalTp;
    }

    public void setEducationCl(int educationCl) {
        this.educationCl = educationCl;
    }

    public void setEducationTp(int educationTp) {
        this.educationTp = educationTp;
    }

    public void setMate(String mate) {
        this.mate = mate;
    }

    public void setDtBirMate(String dtBirMate) {
        this.dtBirMate = dtBirMate;
    }

    public void setSexMateTp(int sexMateTp) {
        this.sexMateTp = sexMateTp;
    }

    public void setSexMateCl(int sexMateCl) {
        this.sexMateCl = sexMateCl;
    }

    public void setSon1(String son1) {
        this.son1 = son1;
    }

    public void setDtBirSon1(String dtBirSon1) {
        this.dtBirSon1 = dtBirSon1;
    }

    public void setSexSonTp1(int sexSonTp1) {
        this.sexSonTp1 = sexSonTp1;
    }

    public void setSexSonCl1(int sexSonCl1) {
        this.sexSonCl1 = sexSonCl1;
    }

    public void setSon2(String son2) {
        this.son2 = son2;
    }

    public void setDtBirSon2(String dtBirSon2) {
        this.dtBirSon2 = dtBirSon2;
    }

    public void setSexSonTp2(int sexSonTp2) {
        this.sexSonTp2 = sexSonTp2;
    }

    public void setSexSonCl2(int sexSonCl2) {
        this.sexSonCl2 = sexSonCl2;
    }

    public void setSon3(String son3) {
        this.son3 = son3;
    }

    public void setDtBirSon3(String dtBirSon3) {
        this.dtBirSon3 = dtBirSon3;
    }

    public void setSexSonTp3(int sexSonTp3) {
        this.sexSonTp3 = sexSonTp3;
    }

    public void setSexSonCl3(int sexSonCl3) {
        this.sexSonCl3 = sexSonCl3;
    }

    public void setSon4(String son4) {
        this.son4 = son4;
    }

    public void setDtBirSon4(String dtBirSon4) {
        this.dtBirSon4 = dtBirSon4;
    }

    public void setSexSonTp4(int sexSonTp4) {
        this.sexSonTp4 = sexSonTp4;
    }

    public void setSexSonCl4(int sexSonCl4) {
        this.sexSonCl4 = sexSonCl4;
    }

    public void setSon5(String son5) {
        this.son5 = son5;
    }

    public void setDtBirSon5(String dtBirSon5) {
        this.dtBirSon5 = dtBirSon5;
    }

    public void setSexSonTp5(int sexSonTp5) {
        this.sexSonTp5 = sexSonTp5;
    }

    public void setSexSonCl5(int sexSonCl5) {
        this.sexSonCl5 = sexSonCl5;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public void setSexCl(int sexCl) {
        this.sexCl = sexCl;
    }

    public void setSexTp(int sexTp) {
        this.sexTp = sexTp;
    }

    public void setBloodCl(int bloodCl) {
        this.bloodCl = bloodCl;
    }

    public void setBloodTp(int bloodTp) {
        this.bloodTp = bloodTp;
    }

    public void setEmergCl(int emergCl) {
        this.emergCl = emergCl;
    }

    public void setEmergTp(int emergTp) {
        this.emergTp = emergTp;
    }

    public void setEmergsCon(String emergsCon) {
        this.emergsCon = emergsCon;
    }

    public void setEmergsTel(String emergsTel) {
        this.emergsTel = emergsTel;
    }

    public void setBenefs(String benefs) {
        this.benefs = benefs;
    }

    public void setIdBpb(int idBpb) {
        this.idBpb = idBpb;
    }

    public void setIdAdd(int idAdd) {
        this.idAdd = idAdd;
    }

    public void setIdCon(int idCon) {
        this.idCon = idCon;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public void setZipCodeFiscal(String zipCodeFiscal) {
        this.zipCodeFiscal = zipCodeFiscal;
    }
    
}
