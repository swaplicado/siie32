/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp;

import cfd.DCfdSignature;
import erp.client.SClientInterface;
import erp.form.SFormFieldAccount;
import erp.gui.session.SSessionCustomApi;
import erp.lib.form.SFormFieldInterface;
import erp.lib.form.SFormOptionPickerInterface;
import erp.lib.gui.SGuiDatePicker;
import erp.lib.gui.SGuiDateRangePicker;
import erp.lib.gui.SGuiModule;
import erp.mcfg.data.SDataCompany;
import erp.mcfg.data.SDataParamsCompany;
import erp.musr.data.SDataUser;
import erp.server.SSessionXXX;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import redis.clients.jedis.Jedis;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiSession;

/**
 *
 * @author Sergio Flores, Isabel Servín, Sergio Flores
 */
public class SClientApi implements SClientInterface {
    
    protected final SGuiSession moSession;
    protected final int mnUserId;
    protected SSessionXXX moSessionXXX;
    
    public SClientApi(SGuiSession session, int userId) {
        moSession = session; 
        moSession.setSessionCustom(new SSessionCustomApi(session));
        mnUserId = userId;
        createSessionXXX();
    }
    
    private void createSessionXXX() {
        moSessionXXX = new SSessionXXX();
        
        moSessionXXX.setCompany(getCompany());
        moSessionXXX.setUser(getUser());
        moSessionXXX.setParamsCompany(getParamsCompany());
    }
    
    private SDataCompany getCompany() {
        SDataCompany company = new SDataCompany();
        
        try {
            String sql = "SELECT id_co FROM cfg_param_co";
            ResultSet resultSet = moSession.getStatement().executeQuery(sql);
            if (resultSet.next()) {
                company.read(new int[] { resultSet.getInt(1) }, moSession.getStatement());
            }
        }
        catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return company;
    }
    
    private SDataUser getUser() {
        SDataUser user = new SDataUser();
        
        try {
            user.read(new int[] { mnUserId }, moSession.getStatement());
        }
        catch(Exception e) {
            System.err.println(e.getMessage());
        }
        
        return user;
    }
    
    private SDataParamsCompany getParamsCompany() {
        SDataParamsCompany params = new SDataParamsCompany();
        params.read(new int[] { moSessionXXX.getCompany().getPkCompanyId() }, moSession.getStatement());
        return params;
    }
    
    @Override
    public boolean isGui() {
        return false;
    }

    @Override
    public SGuiSession getSession() {
        return moSession;
    }

    @Override
    public SSessionXXX getSessionXXX() {
        return moSessionXXX;
    }

    @Override
    public Jedis getJedis() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setJedis(Jedis j) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SParamsApp getParamsApp() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JFrame getFrame() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JTabbedPane getTabbedPane() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ImageIcon getImageIcon(int iconType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiModule getGuiModule(int moduleType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiDatePicker getGuiDatePickerXXX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiDatePicker getGuiDatePeriodPickerXXX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiDateRangePicker getGuiDateRangePickerXXX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SFormOptionPickerInterface getOptionPicker(int optionType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public DCfdSignature getCfdSignature(float cfdVersion) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getSwapServicesSetting(String setting) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JFileChooser getFileChooser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int pickOption(int optionType, SFormFieldInterface field, Object filterKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int pickOption(int optionType, SFormFieldAccount field, Object filterKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String pickOption(int optionType, Object filterKey) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double pickExchangeRate(int currency, Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int showMsgBoxConfirm(String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showMsgBoxWarning(String msg) {
        System.err.println(msg);
    }

    @Override
    public void showMsgBoxInformation(String msg) {
        System.out.println(msg);
    }

    @Override
    public HashMap<String, Object> createReportParams() { 
        HashMap<String, Object> map = new HashMap<>();

        map.put("sCompanyName", moSessionXXX.getCompany().getCompany());
        map.put("sUserName", moSessionXXX.getUser().getUser());
        map.put("sVendorLabel", SClient.VENDOR_COPYRIGHT);
        map.put("sVendorWebsite", SClient.VENDOR_WEBSITE);
        map.put("bShowDetailBackground", moSessionXXX.getParamsCompany().getIsReportsBackground());
        map.put("oDateFormat", SLibUtils.DateFormatDate);
        map.put("oDatetimeFormat", SLibUtils.DateFormatDatetime);
        map.put("oDatetimeFormatIso", SLibUtils.IsoFormatDatetime);
        map.put("oTimeFormat", SLibUtils.DateFormatTime);
        map.put("oValueFormat", SLibUtils.getDecimalFormatAmount());
        map.put("sImageDir", moSessionXXX.getParamsCompany().getImagesDirectory());
        map.put("sXmlBaseDir", moSessionXXX.getParamsCompany().getXmlBaseDirectory());

        return map;
    }
}
