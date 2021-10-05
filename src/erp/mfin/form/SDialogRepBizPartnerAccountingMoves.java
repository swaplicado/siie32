/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SDialogRepBizPartnerAccountingMoves.java
 *
 * Created on 29/06/2010, 05:02:26 PM
 */

package erp.mfin.form;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.SLibTimeUtilities;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormField;
import erp.lib.form.SFormUtilities;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mod.bps.db.SBpsUtils;
import erp.mtrn.form.SDialogFilterFunctionalArea;
import erp.mtrn.data.STrnFunctionalAreaUtils;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeUtils;

/**
 *
 * @author Sergio Flores, Edwin Carmona, Sergio Flores, Claudio Peña
 */
public class SDialogRepBizPartnerAccountingMoves extends javax.swing.JDialog implements java.awt.event.ActionListener, java.awt.event.ItemListener, java.awt.event.FocusListener {

    public static final int DOC_DAYS_NO = 1;
    public static final int DOC_DAYS_YES = 2;

    boolean mbFirstTime;
    private erp.client.SClientInterface miClient;
    int mnBizPartnerId;
    int mnBizPartnerCategoryId;

    private erp.lib.form.SFormField moFieldYear;
    private erp.lib.form.SFormField moFieldBizPartner;
    private erp.lib.form.SFormField moFieldDateRef;
    private java.util.Vector<erp.lib.form.SFormField> mvFields;

    int mnOptionPickerId;
    int[] manSysMoveTypeKey;
    private java.lang.String msBizPartnerCatSng;
    private java.lang.String msBizPartnerCatPlr;
    
    private erp.mtrn.form.SDialogFilterFunctionalArea moDialogFilterFunctionalArea;
    private int mnFunctionalAreaId;
    private String msFunctionalAreasIds;

    /** Creates new form SDialogRepBizPartnerAccountingMoves
     * @param client
     * @param idBizPartnerCategory
     */
    public SDialogRepBizPartnerAccountingMoves(erp.client.SClientInterface client, int idBizPartnerCategory) {
        super(client.getFrame(), false);
        miClient = client;
        mnBizPartnerCategoryId = idBizPartnerCategory;
        
        initComponents();
        initComponentsCustom();
        
        initForm();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlYear = new javax.swing.JLabel();
        jtfYear = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jcbBizPartner = new javax.swing.JComboBox();
        jbPickBizPartner = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jckShowPayDays = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jlDateRef = new javax.swing.JLabel();
        jftDateRef = new javax.swing.JFormattedTextField();
        jbPickDateRef = new javax.swing.JButton();
        jPanel11 = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jlBizPartner1 = new javax.swing.JLabel();
        jtfFunctionalArea = new javax.swing.JTextField();
        jbFunctionalArea = new javax.swing.JButton();
        jPanel12 = new javax.swing.JPanel();
        jckShowFolioOrders = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jbPrint = new javax.swing.JButton();
        jbClose = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Parámetros del reporte:"));
        jPanel2.setLayout(new java.awt.BorderLayout());

        jPanel6.setLayout(new java.awt.GridLayout(8, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlYear.setText("Ejercicio: *");
        jlYear.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlYear);

        jtfYear.setText("2000");
        jtfYear.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel8.add(jtfYear);

        jPanel6.add(jPanel8);

        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Asociado negocios: *");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel4.add(jlBizPartner);

        jcbBizPartner.setPreferredSize(new java.awt.Dimension(350, 23));
        jPanel4.add(jcbBizPartner);

        jbPickBizPartner.setText("...");
        jbPickBizPartner.setFocusable(false);
        jbPickBizPartner.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel4.add(jbPickBizPartner);

        jPanel6.add(jPanel4);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel6.add(jPanel9);

        jPanel7.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckShowPayDays.setText("Mostrar días de pago y mora");
        jckShowPayDays.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel7.add(jckShowPayDays);

        jPanel6.add(jPanel7);

        jPanel3.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlDateRef.setText("Fecha referencia: *");
        jlDateRef.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel3.add(jlDateRef);

        jftDateRef.setText("dd/mm/yyyy");
        jftDateRef.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel3.add(jftDateRef);

        jbPickDateRef.setIcon(new javax.swing.ImageIcon(getClass().getResource("/erp/img/cal_cal.gif"))); // NOI18N
        jbPickDateRef.setToolTipText("Seleccionar fecha");
        jbPickDateRef.setFocusable(false);
        jbPickDateRef.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel3.add(jbPickDateRef);

        jPanel6.add(jPanel3);

        jPanel11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));
        jPanel6.add(jPanel11);

        jPanel10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner1.setText("Área funcional:");
        jlBizPartner1.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel10.add(jlBizPartner1);

        jtfFunctionalArea.setEditable(false);
        jtfFunctionalArea.setPreferredSize(new java.awt.Dimension(300, 23));
        jPanel10.add(jtfFunctionalArea);

        jbFunctionalArea.setText("...");
        jbFunctionalArea.setToolTipText("Seleccionar asociado de negocios:");
        jbFunctionalArea.setFocusable(false);
        jbFunctionalArea.setPreferredSize(new java.awt.Dimension(23, 23));
        jPanel10.add(jbFunctionalArea);

        jPanel6.add(jPanel10);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jckShowFolioOrders.setText("Mostrar folios de pedidos");
        jckShowFolioOrders.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel12.add(jckShowFolioOrders);

        jPanel6.add(jPanel12);

        jPanel2.add(jPanel6, java.awt.BorderLayout.NORTH);

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        jbPrint.setText("Imprimir");
        jbPrint.setToolTipText("[Ctrl + Enter]");
        jbPrint.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbPrint);

        jbClose.setText("Cerrar");
        jbClose.setToolTipText("[Escape]");
        jbClose.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel1.add(jbClose);

        getContentPane().add(jPanel1, java.awt.BorderLayout.SOUTH);

        setSize(new java.awt.Dimension(576, 388));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        windowActivated();
    }//GEN-LAST:event_formWindowActivated

    private void initComponentsCustom() {
        mnBizPartnerId = 0;
        msBizPartnerCatSng = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategoryId, SUtilConsts.NUM_SNG);
        msBizPartnerCatPlr = SBpsUtils.getBizPartnerCategoryName(mnBizPartnerCategoryId, SUtilConsts.NUM_PLR);
        jlBizPartner.setText(msBizPartnerCatSng + ": *");
        jbPickBizPartner.setToolTipText(SUtilConsts.TXT_SELECT + " " + msBizPartnerCatSng.toLowerCase());
        setTitle("Movimientos contables de " + msBizPartnerCatPlr.toLowerCase() +" por documento");
        
        switch (mnBizPartnerCategoryId) {
            case SDataConstantsSys.BPSS_CT_BP_SUP:
                manSysMoveTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_SUP;
                SFormUtilities.populateComboBox(miClient, jcbBizPartner, mnOptionPickerId = SDataConstants.BPSX_BP_SUP);
                break;
            case SDataConstantsSys.BPSS_CT_BP_CUS:
                manSysMoveTypeKey = SDataConstantsSys.FINS_TP_SYS_MOV_BPS_CUS;
                SFormUtilities.populateComboBox(miClient, jcbBizPartner, mnOptionPickerId = SDataConstants.BPSX_BP_CUS);
                break;
            default:
                miClient.showMsgBoxWarning(SLibConstants.MSG_ERR_UTIL_UNKNOWN_OPTION);
        }
        
        moFieldYear = new SFormField(miClient, SLibConstants.DATA_TYPE_INTEGER, true, jtfYear, jlYear);
        moFieldYear.setIntegerMin(2000);
        moFieldYear.setIntegerMax(2100);
        moFieldYear.setMinInclusive(true);
        moFieldYear.setMaxInclusive(true);
        moFieldYear.setDecimalFormat(miClient.getSessionXXX().getFormatters().getYearFormat());
        moFieldBizPartner = new SFormField(miClient, SLibConstants.DATA_TYPE_KEY, true, jcbBizPartner, jlBizPartner);
        moFieldBizPartner.setPickerButton(jbPickBizPartner);
        moFieldDateRef = new SFormField(miClient, SLibConstants.DATA_TYPE_DATE, true, jftDateRef, jlDateRef);
        moFieldDateRef.setPickerButton(jbPickDateRef);

        mvFields = new Vector<SFormField>();
        mvFields.add(moFieldYear);
        mvFields.add(moFieldBizPartner);
        mvFields.add(moFieldDateRef);

        jbPickBizPartner.addActionListener(this);
        jbPickDateRef.addActionListener(this);
        jbPrint.addActionListener(this);
        jbClose.addActionListener(this);
        jbFunctionalArea.addActionListener(this);
        jckShowPayDays.addItemListener(this);
        jckShowFolioOrders.addItemListener(this);
        jtfYear.addFocusListener(this);
        jftDateRef.addFocusListener(this);

        SFormUtilities.createActionMap(rootPane, this, "actionPrint", "print", KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK);
        SFormUtilities.createActionMap(rootPane, this, "actionClose", "close", KeyEvent.VK_ESCAPE, 0);
        
        // áreas funcionales:
        jbFunctionalArea.setEnabled(miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas());
        mnFunctionalAreaId = SLibConstants.UNDEFINED;
        moDialogFilterFunctionalArea = new SDialogFilterFunctionalArea(miClient);
        renderFunctionalArea();
    }

    private void windowActivated() {
        if (mbFirstTime) {
            mbFirstTime = false;

            if (jtfYear.isEnabled()) {
                jtfYear.requestFocus();
            }
            else {
                jftDateRef.requestFocus();
            }
        }
    }

    private void print() {
        int year = moFieldYear.getInteger();
        Cursor cursor = getCursor();
        Map<String, Object> map = null;
        JasperPrint jasperPrint = null;
        JasperViewer jasperViewer = null;
        SDataBizPartner bizPartner = null;
        SDataBizPartnerCategory bizPartnerCategory = null;
        
        String areasFilter = "";
        if (miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas()) {
            if (msFunctionalAreasIds.isEmpty()) {
                areasFilter = "";
            }
            else {
                areasFilter = " AND d.fid_func IN ( " + msFunctionalAreasIds + " ) ";
            }
        }

        try {
            setCursor(new Cursor(Cursor.WAIT_CURSOR));

            bizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, moFieldBizPartner.getKeyAsIntArray(), SLibConstants.EXEC_MODE_VERBOSE);

            map = miClient.createReportParams();
            map.put("nSysMoveCatId", manSysMoveTypeKey[0]);
            map.put("nSysMoveTypeId", manSysMoveTypeKey[1]);
            map.put("sBizPartnerCat", msBizPartnerCatSng.toUpperCase());
            map.put("sBizPartnerCatPlural", msBizPartnerCatPlr.toUpperCase());
            map.put("nLocalCurrencyId", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getPkCurrencyId());
            map.put("sLocalCurrency", miClient.getSessionXXX().getParamsErp().getDbmsDataCurrency().getCurrency());
            map.put("nYear", year);
            map.put("tDate", jckShowPayDays.isSelected() ? moFieldDateRef.getDate() : SLibTimeUtilities.createDate(year, 12, 31));
            map.put("nBizPartnerId", bizPartner.getPkBizPartnerId());
            map.put("sBizPartner", bizPartner.getBizPartner());

            switch (mnBizPartnerCategoryId) {
                case SDataConstantsSys.BPSS_CT_BP_SUP:
                    bizPartnerCategory = bizPartner.getDbmsCategorySettingsSup();
                    break;
                case SDataConstantsSys.BPSS_CT_BP_CUS:
                    bizPartnerCategory = bizPartner.getDbmsCategorySettingsCus();
                    break;
                default:
            }

            map.put("dCreditLimit", bizPartnerCategory.getEffectiveCreditLimit());
            map.put("nDaysCredit", bizPartnerCategory.getEffectiveDaysOfCredit());
            map.put("nDaysGrace", bizPartnerCategory.getEffectiveDaysOfGrace());
            map.put("sCreditType", SDataReadDescriptions.getCatalogueDescription(miClient, SDataConstants.BPSS_TP_CRED, new int[] { bizPartnerCategory.getEffectiveCreditTypeId() }));
            map.put("sFuncText", jtfFunctionalArea.getText());
            map.put("sFilterFunctionalArea", areasFilter);
            
            if (jckShowFolioOrders.isSelected()) {
                jasperPrint = SDataUtilities.fillReport(miClient,SDataConstantsSys.REP_FIN_BPS_ACC_MOV_ORD , map);
            } else {
                jasperPrint = SDataUtilities.fillReport(miClient, jckShowPayDays.isSelected() ? SDataConstantsSys.REP_FIN_BPS_ACC_MOV_DAY : SDataConstantsSys.REP_FIN_BPS_ACC_MOV, map);
            }
            
            jasperViewer = new JasperViewer(jasperPrint, false);
            jasperViewer.setTitle(getTitle());
            jasperViewer.setVisible(true);
        }
        catch (Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            setCursor(cursor);
        }
    }
    
    private void itemStateChangedShowPayDays() {
        int[] date = null;
        
        if (jckShowPayDays.isSelected()) {
            jftDateRef.setEnabled(true);
            jbPickDateRef.setEnabled(true);
            jckShowFolioOrders.setEnabled(false);
            jckShowFolioOrders.setSelected(false);
            
            date = SLibTimeUtils.digestDate(miClient.getSession().getCurrentDate());
            moFieldDateRef.setFieldValue(SLibTimeUtils.createDate(moFieldYear.getInteger(), date[1], date[2]));
        }
        else {
            jftDateRef.setEnabled(false);
            jbPickDateRef.setEnabled(false);
            jckShowFolioOrders.setEnabled(true);
            
            moFieldDateRef.resetField();
        }
    }

    private void itemStateChangedShowFolioOrders() {        
        if (jckShowFolioOrders.isSelected()) {
            jckShowPayDays.setEnabled(false);
            jckShowPayDays.setSelected(false);
            jbPickDateRef.setEnabled(false);
        }
        else {
            jckShowPayDays.setEnabled(true);
        }
    }
    
    public void focusLostYear() {
        int[] date = null;
        
        if (jckShowPayDays.isSelected()) {
            date = SLibTimeUtils.digestDate(miClient.getSession().getCurrentDate());
            moFieldDateRef.setFieldValue(SLibTimeUtils.createDate(moFieldYear.getInteger(), date[1], date[2]));
        }
    }

    public void focusLostDate() {
        moFieldYear.setFieldValue(SLibTimeUtilities.digestYear(moFieldDateRef.getDate())[0]);
    }
    
    private void actionFunctionalArea() {
        moDialogFilterFunctionalArea.formRefreshCatalogues();
        moDialogFilterFunctionalArea.formReset();
        moDialogFilterFunctionalArea.setFunctionalAreaId(mnFunctionalAreaId);
        moDialogFilterFunctionalArea.setFormVisible(true);

        if (moDialogFilterFunctionalArea.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_OK) {
            mnFunctionalAreaId = moDialogFilterFunctionalArea.getFunctionalAreaId();
            renderFunctionalArea();
        }
    }
    
    private void renderFunctionalArea() {
        String texts[] = STrnFunctionalAreaUtils.getTextFilterOfFunctionalAreas(miClient, mnFunctionalAreaId);
        msFunctionalAreasIds = texts[0];
        
        jtfFunctionalArea.setText(texts[1]);
        jtfFunctionalArea.setCaretPosition(0);
    }

    private void actionPickDate() {
        miClient.getGuiDatePickerXXX().pickDate(moFieldDateRef.getDate(), moFieldDateRef);
    }

    private void actionPickBizPartner() {
        miClient.pickOption(mnOptionPickerId, moFieldBizPartner, null);
    }

    public void actionPrint() {
        boolean error = false;
        JComponent component = null;

        for (SFormField field : mvFields) {
            if (!field.validateField()) {
                error = true;
                component = field.getComponent();
            }
        }

        if (error) {
            if (component != null) {
                component.requestFocus();
            }
        }
        else {
            print();
        }
    }

    public void actionClose() {
        setVisible(false);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JButton jbClose;
    private javax.swing.JButton jbFunctionalArea;
    private javax.swing.JButton jbPickBizPartner;
    private javax.swing.JButton jbPickDateRef;
    private javax.swing.JButton jbPrint;
    private javax.swing.JComboBox jcbBizPartner;
    private javax.swing.JCheckBox jckShowFolioOrders;
    private javax.swing.JCheckBox jckShowPayDays;
    private javax.swing.JFormattedTextField jftDateRef;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBizPartner1;
    private javax.swing.JLabel jlDateRef;
    private javax.swing.JLabel jlYear;
    private javax.swing.JTextField jtfFunctionalArea;
    private javax.swing.JTextField jtfYear;
    // End of variables declaration//GEN-END:variables

    public void initForm() {
       mbFirstTime = true;
        
        moFieldYear.setFieldValue(miClient.getSessionXXX().getWorkingYear());
        jckShowPayDays.setSelected(false);
        jckShowFolioOrders.setSelected(false);
        itemStateChangedShowPayDays(); // resets reference date
        itemStateChangedShowFolioOrders(); // resets reference date

        if (mnBizPartnerId == SLibConsts.UNDEFINED) {
            moFieldBizPartner.resetField();
        }
        else {
            SFormUtilities.locateComboBoxItem(jcbBizPartner, new int[] { mnBizPartnerId });
        }
    }

    public void setBizPartnerId(int id) {
        mnBizPartnerId = id;
    }

    public int getBizPartnerId() {
        return mnBizPartnerId;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button == jbPickBizPartner) {
                actionPickBizPartner();
            }
            else if (button == jbPickDateRef) {
                actionPickDate();
            }
            else if (button == jbPrint) {
                actionPrint();
            }
            else if (button == jbClose) {
                actionClose();
            }
            else if (button == jbFunctionalArea) {
                actionFunctionalArea();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JCheckBox) {
            JCheckBox checkBox = (JCheckBox) e.getSource();
            
            if (checkBox == jckShowPayDays) {
                itemStateChangedShowPayDays();
            }
            if (checkBox == jckShowFolioOrders) {
                itemStateChangedShowFolioOrders();
            }
        }
    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {
        if (e.getSource() instanceof JFormattedTextField) {
            JFormattedTextField formattedTextField = (JFormattedTextField) e.getSource();

            if (formattedTextField == jftDateRef) {
                focusLostDate();
            }
        }
        else if (e.getSource() instanceof JTextField) {
            JTextField textField = (JTextField) e.getSource();

            if (textField == jtfYear) {
                focusLostYear();
            }
        }
    }
}
