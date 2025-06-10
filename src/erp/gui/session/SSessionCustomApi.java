/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.gui.session;

import erp.cfd.SCfdXmlCatalogs;
import erp.mod.SModConsts;
import java.sql.ResultSet;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiSessionCustom;

/**
 *
 * @author Isabel Servín
 */
public final class SSessionCustomApi implements SGuiSessionCustom {
    
    private final SGuiSession moSession;
    private int[] manLocalCountryKey;
    private SCfdXmlCatalogs moCfdXmlCatalogs;
    
    public SSessionCustomApi(SGuiSession session) {
        moSession = session;
        updateSessionSettings();
    }

    public void updateSessionSettings() {
        try {
            String sql = "SELECT fid_cty FROM " + SModConsts.TablesMap.get(SModConsts.CFG_PARAM_ERP) + " ";
            ResultSet resultSet = moSession.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                manLocalCountryKey = new int[] { resultSet.getInt(1) };
            }
            moCfdXmlCatalogs = new SCfdXmlCatalogs(moSession);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public SCfdXmlCatalogs getCfdXmlCatalogs() { return moCfdXmlCatalogs; }
    
    @Override
    public int[] getLocalCountryKey() {
        return manLocalCountryKey;
    }

    @Override
    public String getLocalCountry() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLocalCountryCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLocalCountry(int[] key) {
        return SLibUtils.compareKeys(key, manLocalCountryKey);
    }

    @Override
    public int[] getLocalCurrencyKey() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLocalCurrency() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLocalCurrencyCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean isLocalCurrency(int[] key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLocalLanguage() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCountry(int[] key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCountryCode(int[] key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCurrency(int[] key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getCurrencyCode(int[] key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getLanguage(int[] key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getTerminal() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }   
}
