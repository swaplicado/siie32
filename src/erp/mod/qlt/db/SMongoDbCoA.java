/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.qlt.db;

import java.util.Date;
import org.bson.Document;
import org.bson.types.ObjectId;

/**
 *
 * @author Edwin Carmona
 */
public class SMongoDbCoA {
    
    private org.bson.types.ObjectId _id;
    private Date coADate;
    private String lotNum;
    private Date loadDate;
    private String tankOrigin;
    private String productName;
    private String productType; // Convencional, Orgánico, NONO-GMO
    private String clientName;
    private String identification;
    private String loadedFrom;
    private String containerId;
    private double netWeight;
    private String netWeightUnit;
    private String vgm; // Verified Gross Mass
    private String vgmUnit; // kg, lb, etc.
    private String seals;
    private String topSeals;
    private String bottomSeals;
    private String eori;
    private String notes;
    private String personSignatureName;
    private String personSignaturePosition;

    public Document toDocument() {
        Document doc = new Document();
        doc.append("_id", _id);
        doc.append("coADate", coADate);
        doc.append("lotNum", lotNum);
        doc.append("loadDate", loadDate);
        doc.append("tankOrigin", tankOrigin);
        doc.append("productName", productName);
        doc.append("productType", productType);
        doc.append("clientName", clientName);
        doc.append("identification", identification);
        doc.append("loadedFrom", loadedFrom);
        doc.append("containerId", containerId);
        doc.append("netWeight", netWeight);
        doc.append("netWeightUnit", netWeightUnit);
        doc.append("vgm", vgm); // VGM is not used in this class
        doc.append("vgmUnit", vgmUnit); // VGM unit is not used in this class
        doc.append("seals", seals);
        doc.append("topSeals", topSeals);
        doc.append("bottomSeals", bottomSeals);
        doc.append("eori", eori);
        doc.append("notes", notes);
        doc.append("personSignatureName", personSignatureName);
        doc.append("personSignaturePosition", personSignaturePosition);
        
        return doc;
    }

    /** 
     * Convierte un documento de MongoDB a un objeto SMongoDbCoA.
     * 
     * @param doc El documento de MongoDB a convertir.
     * @return Un objeto SMongoDbCoA con los datos del documento.
     * Si el documento es nulo, se devuelve null.
     */
    public static SMongoDbCoA fromDocument(Document doc) {
        SMongoDbCoA coa = new SMongoDbCoA();
        
        if (doc == null) {
            return null;
        }
        
        coa.setId(doc.getObjectId("_id"));
        coa.setCoADate(doc.getDate("coADate"));
        coa.setLotNum(doc.getString("lotNum"));
        coa.setLoadDate(doc.getDate("loadDate"));
        coa.setTankOrigin(doc.getString("tankOrigin"));
        coa.setProductName(doc.getString("productName"));
        coa.setProductType(doc.getString("productType"));
        coa.setClientName(doc.getString("clientName"));
        coa.setIdentification(doc.getString("identification"));
        coa.setLoadedFrom(doc.getString("loadedFrom"));
        coa.setContainerId(doc.getString("containerId"));
        coa.setNetWeight(doc.getDouble("netWeight"));
        coa.setNetWeightUnit(doc.getString("netWeightUnit"));
        coa.setVgm(doc.getString("vgm")); // VGM is not used in this class
        coa.setVgmUnit(doc.getString("vgmUnit"));
        coa.setSeals(doc.getString("seals"));
        coa.setTopSeals(doc.getString("topSeals"));
        coa.setBottomSeals(doc.getString("bottomSeals"));
        coa.setEori(doc.getString("eori"));
        coa.setNotes(doc.getString("notes"));
        coa.setPersonSignatureName(doc.getString("personSignatureName"));
        coa.setPersonSignaturePosition(doc.getString("personSignaturePosition"));

        return coa;
    }

    public ObjectId getId() {
        return _id;
    }

    public void setId(ObjectId _id) {
        this._id = _id;
    }

    public Date getCoADate() {
        return coADate;
    }

    public void setCoADate(Date coADate) {
        this.coADate = coADate;
    }

    public String getLotNum() {
        return lotNum;
    }

    public void setLotNum(String lotNum) {
        this.lotNum = lotNum == null ? "" : lotNum;
    }

    public Date getLoadDate() {
        return loadDate;
    }

    public void setLoadDate(Date loadDate) {
        this.loadDate = loadDate;
    }

    public String getTankOrigin() {
        return tankOrigin;
    }

    public void setTankOrigin(String tankOrigin) {
        this.tankOrigin = tankOrigin == null ? "" : tankOrigin;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName == null ? "" : productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType == null ? "" : productType;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName == null ? "" : clientName;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification == null ? "" : identification;
    }

    public String getLoadedFrom() {
        return loadedFrom;
    }

    public void setLoadedFrom(String loadedFrom) {
        this.loadedFrom = loadedFrom == null ? "" : loadedFrom;
    }

    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId == null ? "" : containerId;
    }

    public double getNetWeight() {
        return netWeight;
    }

    public void setNetWeight(double netWeight) {
        this.netWeight = netWeight;
    }

    public String getNetWeightUnit() {
        return netWeightUnit;
    }

    public void setNetWeightUnit(String netWeightUnit) {
        this.netWeightUnit = netWeightUnit == null ? "" : netWeightUnit;
    }

    public String getVgm() {
        return vgm;
    }

    public void setVgm(String vgm) {
        this.vgm = vgm == null ? "" : vgm;
    }

    public String getVgmUnit() {
        return vgmUnit;
    }

    public void setVgmUnit(String vgmUnit) {
        this.vgmUnit = vgmUnit == null ? "" : vgmUnit;
    }

    public String getSeals() {
        return seals;
    }

    public void setSeals(String seals) {
        this.seals = seals;
    }

    public String getTopSeals() {
        return topSeals;
    }

    public void setTopSeals(String topSeals) {
        this.topSeals = topSeals;
    }

    public String getBottomSeals() {
        return bottomSeals;
    }

    public void setBottomSeals(String bottomSeals) {
        this.bottomSeals = bottomSeals;
    }

    public String getEori() {
        return eori;
    }

    public void setEori(String eori) {
        this.eori = eori == null ? "" : eori;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes == null ? "" : notes;
    }

    public String getPersonSignatureName() {
        return personSignatureName;
    }

    public void setPersonSignatureName(String personSignatureName) {
        this.personSignatureName = personSignatureName == null ? "" : personSignatureName;
    }

    public String getPersonSignaturePosition() {
        return personSignaturePosition;
    }

    public void setPersonSignaturePosition(String personSignaturePosition) {
        this.personSignaturePosition = personSignaturePosition == null ? "" : personSignaturePosition;
    }
    
    
}
