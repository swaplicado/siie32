/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.loc.form;

import sa.lib.grid.SGridRow;

/**
 *
 * @author Sergio Flores
 */
public class SRowNeighborhood implements SGridRow {
    
    public int ZipCodeId;
    public int StateId;
    public int NeighborhoodId;
    public String NeighborhoodCode;
    public String NeighborhoodName;
    public String LocalityCode;
    public String LocalityName;
    public String CountyCode;
    public String CountyName;
    public String StateCode;
    public String StateName;
    
    public SRowNeighborhood(final int zipCodeId, final int stateId, final int neighborhoodId,
            final String neighborhoodCode, final String neighborhoodName,
            final String localityCode, final String localityName,
            final String countyCode, final String countyName,
            final String stateCode, final String stateName) {
        ZipCodeId = zipCodeId;
        StateId = stateId;
        NeighborhoodId = neighborhoodId;
        NeighborhoodCode = neighborhoodCode;
        NeighborhoodName = neighborhoodName;
        LocalityCode = localityCode;
        LocalityName = localityName;
        CountyCode = countyCode;
        CountyName = countyName;
        StateCode = stateCode;
        StateName = stateName;
    }

    @Override
    public int[] getRowPrimaryKey() {
        return new int[] { ZipCodeId, StateId, NeighborhoodId };
    }

    @Override
    public String getRowCode() {
        return SPickerNeighborhood.ZipCodeFormat.format(ZipCodeId);
    }

    @Override
    public String getRowName() {
        return getRowCode();
    }

    @Override
    public boolean isRowSystem() {
        return false;
    }

    @Override
    public boolean isRowDeletable() {
        return false;
    }

    @Override
    public boolean isRowEdited() {
        return false;
    }

    @Override
    public void setRowEdited(boolean edited) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getRowValueAt(int col) {
        Object value = null;
        
        switch (col) {
            case 0:
                value = getRowCode();
                break;
            case 1:
                value = NeighborhoodCode;
                break;
            case 2:
                value = NeighborhoodName;
                break;
            case 3:
                value = LocalityCode;
                break;
            case 4:
                value = LocalityName;
                break;
            case 5:
                value = CountyCode;
                break;
            case 6:
                value = CountyName;
                break;
            case 7:
                value = StateCode;
                break;
            case 8:
                value = StateName;
                break;
            default:
                // nothing
        }
        
        return value;
    }

    @Override
    public void setRowValueAt(Object value, int col) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
