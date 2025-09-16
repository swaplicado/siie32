/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.cli.swap;

import cfd.DCfdSignature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import erp.SParamsApp;
import erp.client.SClientInterface;
import erp.data.SDataConstantsSys;
import erp.form.SFormFieldAccount;
import erp.lib.form.SFormFieldInterface;
import erp.lib.form.SFormOptionPickerInterface;
import erp.lib.gui.SGuiDatePicker;
import erp.lib.gui.SGuiDateRangePicker;
import erp.lib.gui.SGuiModule;
import erp.mcfg.data.SCfgUtils;
import erp.mod.cfg.swap.SSwapConsts;
import erp.mod.cfg.utils.SAuthJsonUtils;
import erp.server.SSessionXXX;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import redis.clients.jedis.Jedis;
import sa.lib.SLibUtils;
import sa.lib.db.SDbConsts;
import sa.lib.db.SDbDatabase;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiSession;
import sa.lib.gui.SGuiUserGui;
import sa.lib.gui.SGuiYearMonthPicker;
import sa.lib.gui.SGuiYearPicker;

/**
 *
 * @author Sergio Flores
 */
public class SSwapClient implements SClientInterface, SGuiClient {
    
    private boolean mbIsDev;
    private SGuiSession moSession;
    
    private boolean mbSwapServicesLinkUp;
    private int[] manSwapServicesCompanies;
    private int mnSwapServicesInstance;
    
    public SSwapClient(final String dbHost, final int dbPort, final String dbName, final boolean isDev) throws Exception {
        mbIsDev = isDev;
        
        SDbDatabase database = new SDbDatabase(SDbConsts.DBMS_MYSQL);

        database.connect(dbHost, "" + dbPort, dbName, "root", "msroot");

        moSession = new SGuiSession(this);
        
        moSession.setUser(new SSwapUser());

        moSession.setSystemDate(new Date());
        moSession.setCurrentDate(moSession.getSystemDate());
        moSession.setDatabase(database);
        
        JsonNode config = new ObjectMapper().readTree(SCfgUtils.getParamValue(moSession.getStatement(), SDataConstantsSys.CFG_PARAM_SWAP_SERVICES_CONFIG));

        mbSwapServicesLinkUp = SLibUtils.parseInt(SAuthJsonUtils.getValueOfElementAsText(config, "", SSwapConsts.CFG_NVP_LINK_UP)) == 1;

        if (mbSwapServicesLinkUp) {
            String companies = SAuthJsonUtils.getValueOfElementAsText(config, "", SSwapConsts.CFG_NVP_COMPANIES);
            manSwapServicesCompanies = SLibUtils.textExplodeAsIntArray(companies, ",");
            mnSwapServicesInstance = SLibUtils.parseInt(SAuthJsonUtils.getValueOfElementAsText(config, "", SSwapConsts.CFG_NVP_INSTANCE));
        }
    }

    @Override
    public boolean isGui() {
        return false;
    }

    @Override
    public boolean isDev() {
        return mbIsDev;
    }

    @Override
    public SGuiSession getSession() {
        return moSession;
    }

    @Override
    public SSessionXXX getSessionXXX() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        Object value = null;
        
        switch (setting) {
            case SSwapConsts.CFG_NVP_LINK_UP:
                value = mbSwapServicesLinkUp;
                break;
            case SSwapConsts.CFG_NVP_COMPANIES:
                value = manSwapServicesCompanies;
                break;
            case SSwapConsts.CFG_NVP_INSTANCE:
                value = mnSwapServicesInstance;
                break;
            default:
                // nothing
        }
        
        return value;
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
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showMsgBoxInformation(String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public HashMap<String, Object> createReportParams() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SDbDatabase getSysDatabase() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Statement getSysStatement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public sa.lib.gui.SGuiDatePicker getDatePicker() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public sa.lib.gui.SGuiDateRangePicker getDateRangePicker() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiYearPicker getYearPicker() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiYearMonthPicker getYearMonthPicker() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiUserGui readUserGui(int[] key) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public SGuiUserGui saveUserGui(int[] key, String gui) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTableCompany() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getTableUser() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAppName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAppRelease() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAppCopyright() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getAppProvider() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void computeSessionSettings() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void preserveSessionSettings() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void showMsgBoxError(String msg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object getLockManager() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
