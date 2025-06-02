/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.client;

import erp.SParamsApp;
import java.util.HashMap;
import redis.clients.jedis.Jedis;

/**
 *
 * @author Sergio Flores
 */
public interface SClientInterface {
    
    public boolean isGui();

    public sa.lib.gui.SGuiSession getSession();
    public erp.server.SSessionXXX getSessionXXX();
    public redis.clients.jedis.Jedis getJedis();
    public void setJedis(Jedis j);
    public SParamsApp getParamsApp();

    public javax.swing.JFrame getFrame();
    public javax.swing.JTabbedPane getTabbedPane();

    /**
     * @param iconType Constants defined in erp.lib.SLibConstants
     * @return 
     */
    public javax.swing.ImageIcon getImageIcon(int iconType);

    /**
     * @param moduleType Constant defined in erp.data.SDataConstants.
     * @return 
     */
    public erp.lib.gui.SGuiModule getGuiModule(int moduleType);
    public erp.lib.gui.SGuiDatePicker getGuiDatePickerXXX();
    public erp.lib.gui.SGuiDatePicker getGuiDatePeriodPickerXXX();
    public erp.lib.gui.SGuiDateRangePicker getGuiDateRangePickerXXX();
    public erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType);
    public cfd.DCfdSignature getCfdSignature(float cfdVersion);
//    public cfd.DCfdSignature getCfdSignature(java.util.Date cfdDate);
    public javax.swing.JFileChooser getFileChooser();
    public int pickOption(int optionType, erp.lib.form.SFormFieldInterface field, java.lang.Object filterKey);
    public int pickOption(int optionType, erp.form.SFormFieldAccount field, java.lang.Object filterKey);
    public String pickOption(int optionType, java.lang.Object filterKey);
    public double pickExchangeRate(int currency, java.util.Date date);

    public int showMsgBoxConfirm(java.lang.String msg);
    public void showMsgBoxWarning(java.lang.String msg);
    public void showMsgBoxInformation(java.lang.String msg);

    /*
     * XXX Methods added to accomplish compatibility to new Client Interface:
     */
    public HashMap<String, Object> createReportParams();
}
