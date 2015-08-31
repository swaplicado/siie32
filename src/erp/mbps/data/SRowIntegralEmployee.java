/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mbps.data;

/**
 *
 * @author Juan Barajas
 */
public class SRowIntegralEmployee extends erp.lib.table.STableRow {

    protected int mnPkEmployeeId;
    protected String msEmployeeName;
    protected String msEmployeeNumber;
    protected String msEmployeeCategory;
    protected String msEmployeeType;
    protected String msDepartament;
    protected String msSalaryType;
    protected String msStreet;
    protected String msStreetNumberExt;
    protected String msStreetNumberInt;
    protected String msNeighborhood;
    protected String msZipCode;
    protected String msPoBox;
    protected String msLocality;
    protected String msCounty;
    protected String msState;
    protected String msCountry;

    public SRowIntegralEmployee() {
        mnPkEmployeeId = 0;
        msEmployeeName = "";
        msEmployeeNumber = "";
        msEmployeeCategory = "";
        msEmployeeType = "";
        msDepartament = "";
        msSalaryType = "";
        msStreet = "";
        msStreetNumberExt = "";
        msStreetNumberInt = "";
        msNeighborhood = "";
        msZipCode = "";
        msPoBox = "";
        msLocality = "";
        msCounty = "";
        msState = "";
        msCountry = "";
    }

    @Override
    public void prepareTableRow() {
        mvValues.clear();
        mvValues.add(msEmployeeName);
        mvValues.add(msEmployeeNumber);
        mvValues.add(msSalaryType);
        mvValues.add(msEmployeeCategory);
        mvValues.add(msEmployeeType);
        mvValues.add(msDepartament);
        mvValues.add(msStreet);
        mvValues.add(msStreetNumberExt);
        mvValues.add(msStreetNumberInt);
        mvValues.add(msNeighborhood);
        mvValues.add(msZipCode);
        mvValues.add(msPoBox);
        mvValues.add(msLocality);
        mvValues.add(msCounty);
        mvValues.add(msState);
        mvValues.add(msCountry);
    }

    public void setPkEmployeeId(int n) { mnPkEmployeeId = n; }
    public void setEmployeeName(String s) { msEmployeeName = s; }
    public void setEmployeeNumber(String s) { msEmployeeNumber = s; }
    public void setEmployeeCategory(String s) { msEmployeeCategory = s; }
    public void setEmployeeType(String s) { msEmployeeType = s; }
    public void setDepartament(String s) { msDepartament = s; }
    public void setSalaryType(String s) { msSalaryType = s; }
    public void setStreet(String s) { msStreet = s; }
    public void setStreetNumberExt(String s) { msStreetNumberExt = s; }
    public void setStreetNumberInt(String s) { msStreetNumberInt = s; }
    public void setNeighborhood(String s) { msNeighborhood = s; }
    public void setZipCode(String s) { msZipCode = s; }
    public void setPoBox(String s) { msPoBox = s; }
    public void setLocality(String s) { msLocality = s; }
    public void setCounty(String s) { msCounty = s; }
    public void setState(String s) { msState = s; }
    public void setCountry(String s) { msCountry = s; }

    public int getPkEmployeeId() { return mnPkEmployeeId; }
    public String getEmployeeName() { return msEmployeeName ; }
    public String getEmployeeNumber() { return msEmployeeNumber ; }
    public String getEmployeeCategory() { return msEmployeeCategory; }
    public String getEmployeeType() { return msEmployeeType; }
    public String getDepartament() { return msDepartament; }
    public String getSalaryType() { return msSalaryType; }
    public String getStreet() { return msStreet; }
    public String getStreetNumberExt() { return msStreetNumberExt; }
    public String getStreetNumberInt() { return msStreetNumberInt; }
    public String getNeighborhood() { return msNeighborhood; }
    public String getZipCode() { return msZipCode; }
    public String getPoBox() { return msPoBox; }
    public String getLocality() { return msLocality; }
    public String getCounty() { return msCounty; }
    public String getState() { return msState; }
    public String getCountry() { return msCountry; }
}
