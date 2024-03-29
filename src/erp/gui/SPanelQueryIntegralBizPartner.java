/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SPanelQueryIntegralBizPartner.java
 *
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.lib.table.STabFilterYear;
import erp.lib.table.STableConstants;
import erp.lib.table.STableSetting;
import erp.lib.table.STableTabInterface;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerCategory;
import erp.mfin.data.SFinUtilities;
import erp.mod.SModConsts;
import erp.mod.trn.view.qi.SViewCreditNotes;
import erp.mod.trn.view.qi.SViewCreditNotesToReturn;
import erp.mod.trn.view.qi.SViewInvoices;
import erp.mod.trn.view.qi.SViewInvoicesBalance;
import erp.mod.trn.view.qi.SViewInvoicesToSend;
import erp.mod.trn.view.qi.SViewInvoicesToSupply;
import erp.mod.trn.view.qi.SViewOrders;
import erp.mod.trn.view.qi.SViewOrdersToProcess;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.util.Calendar;
import java.util.Locale;
import java.util.Vector;
import javax.swing.JComboBox;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import sa.gui.util.SUtilConsts;
import sa.lib.SLibConsts;
import sa.lib.SLibTimeConsts;
import sa.lib.SLibTimeUtils;
import sa.lib.SLibUtils;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiItem;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Claudio Peña
 */
public class SPanelQueryIntegralBizPartner extends javax.swing.JPanel implements STableTabInterface, java.awt.event.ActionListener, java.awt.event.ItemListener, javax.swing.event.ChangeListener {
    
    private static final int TAB_INV_BAL = 0;
    private static final int TAB_INV_SUP = 1;
    private static final int TAB_CN_RET = 2;
    private static final int TAB_INV_SND = 3;
    private static final int TAB_INV = 4;
    private static final int TAB_CN = 5;
    private static final int TAB_ORD = 6;
    private static final int TAB_ORD_PRC = 7;
    
    private int mnViewType;
    private int mnBizPartnerCategory;
    private erp.client.SClientInterface miClient;
    
    private java.util.Vector<erp.lib.table.STableSetting> mvTableSettings;
    private erp.lib.table.STabFilterYear moFilterYear;

    private SDataBizPartner moBizPartner;
    private SDataBizPartnerCategory moBizPartnerCategory;
    
    private SViewInvoicesBalance moInvoicesBalance;
    private SViewInvoicesToSupply moInvoicesToSupply;
    private SViewCreditNotesToReturn moCreditNotesToReturn;
    private SViewInvoicesToSend moInvoicesToSend;
    private SViewInvoices moInvoices;
    private SViewCreditNotes moCreditNotes;
    private SViewOrders moOrders;
    private SViewOrdersToProcess moOrdersToProcess;
     
    private int mnYear;
    private int mnCurrentPeriodIndex;
    private java.util.Date mtDateStart;
    private java.util.Date mtDateEnd;
    private javax.swing.JToggleButton[] majtbPeriods;
     
    /** Creates new form SPanelQueryIntegralBizPartner
     * @param client 
     * @param viewType 
     * @param bizPartnerCategory
     */
    public SPanelQueryIntegralBizPartner(erp.client.SClientInterface client, final int viewType, final int bizPartnerCategory) {
        miClient = client;
        mnViewType = viewType;
        mnBizPartnerCategory = bizPartnerCategory;

        initComponents();
        initComponentsExtra();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jbgPeriods = new javax.swing.ButtonGroup();
        jpFilter = new javax.swing.JPanel();
        jlBizPartner = new javax.swing.JLabel();
        jcbBizPartner = new javax.swing.JComboBox();
        jtfYearPeriod = new javax.swing.JTextField();
        jtbPeriod01 = new javax.swing.JToggleButton();
        jtbPeriod02 = new javax.swing.JToggleButton();
        jtbPeriod03 = new javax.swing.JToggleButton();
        jtbPeriod04 = new javax.swing.JToggleButton();
        jtbPeriod05 = new javax.swing.JToggleButton();
        jtbPeriod06 = new javax.swing.JToggleButton();
        jtbPeriod07 = new javax.swing.JToggleButton();
        jtbPeriod08 = new javax.swing.JToggleButton();
        jtbPeriod09 = new javax.swing.JToggleButton();
        jtbPeriod10 = new javax.swing.JToggleButton();
        jtbPeriod11 = new javax.swing.JToggleButton();
        jtbPeriod12 = new javax.swing.JToggleButton();
        jtbFullYear = new javax.swing.JToggleButton();
        jpData = new javax.swing.JPanel();
        jpDataBizPartner = new javax.swing.JPanel();
        jpBizPartner = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jlBizPartnerName = new javax.swing.JLabel();
        jtBizPartnerName = new javax.swing.JTextField();
        jPanel23 = new javax.swing.JPanel();
        jlBizPartnerAddress = new javax.swing.JLabel();
        jtfBizPartnerAddress = new javax.swing.JTextField();
        jPanel24 = new javax.swing.JPanel();
        jlBizPartnerLocality = new javax.swing.JLabel();
        jtfBizPartnerLocality = new javax.swing.JTextField();
        jPanel27 = new javax.swing.JPanel();
        jlBizPartnerCp = new javax.swing.JLabel();
        jtfBizPartnerCp = new javax.swing.JTextField();
        jPanel26 = new javax.swing.JPanel();
        jlBizPartnerCountry = new javax.swing.JLabel();
        jtfBizPartnerCountry = new javax.swing.JTextField();
        jpCreditStatus = new javax.swing.JPanel();
        jPanel42 = new javax.swing.JPanel();
        jPanel48 = new javax.swing.JPanel();
        jlCreditStatusBalance = new javax.swing.JLabel();
        jtfCreditStatusBalance = new javax.swing.JTextField();
        jtfCreditStatusBalanceCur = new javax.swing.JTextField();
        jPanel43 = new javax.swing.JPanel();
        jlCreditStatusCreditLimit = new javax.swing.JLabel();
        jtfCreditStatusCreditLimit = new javax.swing.JTextField();
        jtfCreditStatusCreditLimitCur = new javax.swing.JTextField();
        jPanel44 = new javax.swing.JPanel();
        jlCreditStatusCreditDays = new javax.swing.JLabel();
        jtfCreditStatusCreditDays = new javax.swing.JTextField();
        jPanel45 = new javax.swing.JPanel();
        jlCreditStatusGraceDays = new javax.swing.JLabel();
        jtfCreditStatusGraceDays = new javax.swing.JTextField();
        jPanel46 = new javax.swing.JPanel();
        jlCreditStatusOperationsStart = new javax.swing.JLabel();
        jtfCreditStatusOperationsStart = new javax.swing.JFormattedTextField();
        jPanel47 = new javax.swing.JPanel();
        jlCreditStatusCreditLimitType = new javax.swing.JLabel();
        jtfCreditStatusCreditLimitType = new javax.swing.JTextField();
        jtpDataViews = new javax.swing.JTabbedPane();
        jpInvoicesBalance = new javax.swing.JPanel();
        jpInvoicesToSupply = new javax.swing.JPanel();
        jpCreditNotesToReturn = new javax.swing.JPanel();
        jpInvoicesToSend = new javax.swing.JPanel();
        jpInvoices = new javax.swing.JPanel();
        jpCreditNotes = new javax.swing.JPanel();
        jpOrders = new javax.swing.JPanel();
        jpOrdersToProcess = new javax.swing.JPanel();

        setLayout(new java.awt.BorderLayout(0, 5));

        jpFilter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartner.setText("Asociado negocios:");
        jlBizPartner.setPreferredSize(new java.awt.Dimension(100, 23));
        jpFilter.add(jlBizPartner);

        jcbBizPartner.setPreferredSize(new java.awt.Dimension(250, 23));
        jpFilter.add(jcbBizPartner);

        jtfYearPeriod.setEditable(false);
        jtfYearPeriod.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfYearPeriod.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfYearPeriod.setText("2001-01");
        jtfYearPeriod.setToolTipText("Período actual");
        jtfYearPeriod.setFocusable(false);
        jtfYearPeriod.setPreferredSize(new java.awt.Dimension(90, 23));
        jpFilter.add(jtfYearPeriod);

        jbgPeriods.add(jtbPeriod01);
        jtbPeriod01.setText("01");
        jtbPeriod01.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod01.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod01);

        jbgPeriods.add(jtbPeriod02);
        jtbPeriod02.setText("02");
        jtbPeriod02.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod02.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod02);

        jbgPeriods.add(jtbPeriod03);
        jtbPeriod03.setText("03");
        jtbPeriod03.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod03.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod03);

        jbgPeriods.add(jtbPeriod04);
        jtbPeriod04.setText("04");
        jtbPeriod04.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod04.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod04);

        jbgPeriods.add(jtbPeriod05);
        jtbPeriod05.setText("05");
        jtbPeriod05.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod05.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod05);

        jbgPeriods.add(jtbPeriod06);
        jtbPeriod06.setText("06");
        jtbPeriod06.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod06.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod06);

        jbgPeriods.add(jtbPeriod07);
        jtbPeriod07.setText("07");
        jtbPeriod07.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod07.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod07);

        jbgPeriods.add(jtbPeriod08);
        jtbPeriod08.setText("08");
        jtbPeriod08.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod08.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod08);

        jbgPeriods.add(jtbPeriod09);
        jtbPeriod09.setText("09");
        jtbPeriod09.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod09.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod09);

        jbgPeriods.add(jtbPeriod10);
        jtbPeriod10.setText("10");
        jtbPeriod10.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod10.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod10);

        jbgPeriods.add(jtbPeriod11);
        jtbPeriod11.setText("11");
        jtbPeriod11.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod11.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod11);

        jbgPeriods.add(jtbPeriod12);
        jtbPeriod12.setText("12");
        jtbPeriod12.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbPeriod12.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbPeriod12);

        jbgPeriods.add(jtbFullYear);
        jtbFullYear.setText("Año");
        jtbFullYear.setToolTipText("Ver todo el año");
        jtbFullYear.setMargin(new java.awt.Insets(2, 0, 2, 0));
        jtbFullYear.setPreferredSize(new java.awt.Dimension(40, 23));
        jpFilter.add(jtbFullYear);

        add(jpFilter, java.awt.BorderLayout.NORTH);

        jpData.setLayout(new java.awt.BorderLayout());

        jpDataBizPartner.setLayout(new java.awt.BorderLayout());

        jpBizPartner.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos generales:"));
        jpBizPartner.setLayout(new java.awt.GridLayout(5, 1, 0, 5));

        jPanel8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerName.setText("Nombre:");
        jlBizPartnerName.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel8.add(jlBizPartnerName);

        jtBizPartnerName.setEditable(false);
        jtBizPartnerName.setFocusable(false);
        jtBizPartnerName.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel8.add(jtBizPartnerName);

        jpBizPartner.add(jPanel8);

        jPanel23.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerAddress.setText("Dirección:");
        jlBizPartnerAddress.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel23.add(jlBizPartnerAddress);

        jtfBizPartnerAddress.setEditable(false);
        jtfBizPartnerAddress.setFocusable(false);
        jtfBizPartnerAddress.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel23.add(jtfBizPartnerAddress);

        jpBizPartner.add(jPanel23);

        jPanel24.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerLocality.setText("Localidad:");
        jlBizPartnerLocality.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel24.add(jlBizPartnerLocality);

        jtfBizPartnerLocality.setEditable(false);
        jtfBizPartnerLocality.setFocusable(false);
        jtfBizPartnerLocality.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel24.add(jtfBizPartnerLocality);

        jpBizPartner.add(jPanel24);

        jPanel27.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerCp.setText("CP:");
        jlBizPartnerCp.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel27.add(jlBizPartnerCp);

        jtfBizPartnerCp.setEditable(false);
        jtfBizPartnerCp.setFocusable(false);
        jtfBizPartnerCp.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel27.add(jtfBizPartnerCp);

        jpBizPartner.add(jPanel27);

        jPanel26.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlBizPartnerCountry.setText("País:");
        jlBizPartnerCountry.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel26.add(jlBizPartnerCountry);

        jtfBizPartnerCountry.setEditable(false);
        jtfBizPartnerCountry.setFocusable(false);
        jtfBizPartnerCountry.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel26.add(jtfBizPartnerCountry);

        jpBizPartner.add(jPanel26);

        jpDataBizPartner.add(jpBizPartner, java.awt.BorderLayout.NORTH);

        jpCreditStatus.setBorder(javax.swing.BorderFactory.createTitledBorder("Saldo del asociado de negocios:"));
        jpCreditStatus.setLayout(new java.awt.BorderLayout());

        jPanel42.setOpaque(false);
        jPanel42.setLayout(new java.awt.GridLayout(6, 1, 0, 5));

        jPanel48.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCreditStatusBalance.setText("Saldo final:");
        jlCreditStatusBalance.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel48.add(jlCreditStatusBalance);

        jtfCreditStatusBalance.setEditable(false);
        jtfCreditStatusBalance.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCreditStatusBalance.setText("0.00");
        jtfCreditStatusBalance.setFocusable(false);
        jtfCreditStatusBalance.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel48.add(jtfCreditStatusBalance);

        jtfCreditStatusBalanceCur.setEditable(false);
        jtfCreditStatusBalanceCur.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfCreditStatusBalanceCur.setText("MXN");
        jtfCreditStatusBalanceCur.setFocusable(false);
        jtfCreditStatusBalanceCur.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel48.add(jtfCreditStatusBalanceCur);

        jPanel42.add(jPanel48);

        jPanel43.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCreditStatusCreditLimit.setText("Límite crédito:");
        jlCreditStatusCreditLimit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel43.add(jlCreditStatusCreditLimit);

        jtfCreditStatusCreditLimit.setEditable(false);
        jtfCreditStatusCreditLimit.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCreditStatusCreditLimit.setText("0.00");
        jtfCreditStatusCreditLimit.setFocusable(false);
        jtfCreditStatusCreditLimit.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel43.add(jtfCreditStatusCreditLimit);

        jtfCreditStatusCreditLimitCur.setEditable(false);
        jtfCreditStatusCreditLimitCur.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jtfCreditStatusCreditLimitCur.setText("MXN");
        jtfCreditStatusCreditLimitCur.setFocusable(false);
        jtfCreditStatusCreditLimitCur.setPreferredSize(new java.awt.Dimension(35, 23));
        jPanel43.add(jtfCreditStatusCreditLimitCur);

        jPanel42.add(jPanel43);

        jPanel44.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCreditStatusCreditDays.setText("Días crédito:");
        jlCreditStatusCreditDays.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel44.add(jlCreditStatusCreditDays);

        jtfCreditStatusCreditDays.setEditable(false);
        jtfCreditStatusCreditDays.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCreditStatusCreditDays.setText("0");
        jtfCreditStatusCreditDays.setFocusable(false);
        jtfCreditStatusCreditDays.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel44.add(jtfCreditStatusCreditDays);

        jPanel42.add(jPanel44);

        jPanel45.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCreditStatusGraceDays.setText("Días gracia:");
        jlCreditStatusGraceDays.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel45.add(jlCreditStatusGraceDays);

        jtfCreditStatusGraceDays.setEditable(false);
        jtfCreditStatusGraceDays.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfCreditStatusGraceDays.setText("0");
        jtfCreditStatusGraceDays.setFocusable(false);
        jtfCreditStatusGraceDays.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel45.add(jtfCreditStatusGraceDays);

        jPanel42.add(jPanel45);

        jPanel46.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCreditStatusOperationsStart.setText("Inicio operación:");
        jlCreditStatusOperationsStart.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel46.add(jlCreditStatusOperationsStart);

        jtfCreditStatusOperationsStart.setEditable(false);
        jtfCreditStatusOperationsStart.setText("yyyy/mm/dd");
        jtfCreditStatusOperationsStart.setFocusable(false);
        jtfCreditStatusOperationsStart.setPreferredSize(new java.awt.Dimension(75, 23));
        jPanel46.add(jtfCreditStatusOperationsStart);

        jPanel42.add(jPanel46);

        jPanel47.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 0));

        jlCreditStatusCreditLimitType.setText("Tipo límite crédito:");
        jlCreditStatusCreditLimitType.setPreferredSize(new java.awt.Dimension(100, 23));
        jPanel47.add(jlCreditStatusCreditLimitType);

        jtfCreditStatusCreditLimitType.setEditable(false);
        jtfCreditStatusCreditLimitType.setText("X");
        jtfCreditStatusCreditLimitType.setToolTipText("");
        jtfCreditStatusCreditLimitType.setFocusable(false);
        jtfCreditStatusCreditLimitType.setPreferredSize(new java.awt.Dimension(200, 23));
        jPanel47.add(jtfCreditStatusCreditLimitType);

        jPanel42.add(jPanel47);

        jpCreditStatus.add(jPanel42, java.awt.BorderLayout.NORTH);

        jpDataBizPartner.add(jpCreditStatus, java.awt.BorderLayout.CENTER);

        jpData.add(jpDataBizPartner, java.awt.BorderLayout.WEST);
        jpDataBizPartner.getAccessibleContext().setAccessibleName("Datos del Cliente");

        jpInvoicesBalance.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpInvoicesBalance.setLayout(new java.awt.BorderLayout());
        jtpDataViews.addTab("Facturas con saldo y anticipos", jpInvoicesBalance);

        jpInvoicesToSupply.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpInvoicesToSupply.setLayout(new java.awt.BorderLayout());
        jtpDataViews.addTab("Facturas x surtir", jpInvoicesToSupply);

        jpCreditNotesToReturn.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpCreditNotesToReturn.setLayout(new java.awt.BorderLayout());
        jtpDataViews.addTab("Notas de crédito x devolver", jpCreditNotesToReturn);

        jpInvoicesToSend.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpInvoicesToSend.setLayout(new java.awt.BorderLayout());
        jtpDataViews.addTab("Facturas x enviar", jpInvoicesToSend);

        jpInvoices.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpInvoices.setLayout(new java.awt.BorderLayout());
        jtpDataViews.addTab("Facturas", jpInvoices);

        jpCreditNotes.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpCreditNotes.setLayout(new java.awt.BorderLayout());
        jtpDataViews.addTab("Notas de crédito", jpCreditNotes);

        jpOrders.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpOrders.setLayout(new java.awt.BorderLayout());
        jtpDataViews.addTab("Pedidos", jpOrders);

        jpOrdersToProcess.setBorder(javax.swing.BorderFactory.createTitledBorder(""));
        jpOrdersToProcess.setLayout(new java.awt.BorderLayout());
        jtpDataViews.addTab("Pedidos x procesar", jpOrdersToProcess);

        jpData.add(jtpDataViews, java.awt.BorderLayout.CENTER);
        jtpDataViews.getAccessibleContext().setAccessibleName("Facturas con saldo y anticipos");
        jtpDataViews.getAccessibleContext().setAccessibleDescription("");

        add(jpData, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel42;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JPanel jPanel45;
    private javax.swing.JPanel jPanel46;
    private javax.swing.JPanel jPanel47;
    private javax.swing.JPanel jPanel48;
    private javax.swing.JPanel jPanel8;
    private javax.swing.ButtonGroup jbgPeriods;
    private javax.swing.JComboBox jcbBizPartner;
    private javax.swing.JLabel jlBizPartner;
    private javax.swing.JLabel jlBizPartnerAddress;
    private javax.swing.JLabel jlBizPartnerCountry;
    private javax.swing.JLabel jlBizPartnerCp;
    private javax.swing.JLabel jlBizPartnerLocality;
    private javax.swing.JLabel jlBizPartnerName;
    private javax.swing.JLabel jlCreditStatusBalance;
    private javax.swing.JLabel jlCreditStatusCreditDays;
    private javax.swing.JLabel jlCreditStatusCreditLimit;
    private javax.swing.JLabel jlCreditStatusCreditLimitType;
    private javax.swing.JLabel jlCreditStatusGraceDays;
    private javax.swing.JLabel jlCreditStatusOperationsStart;
    private javax.swing.JPanel jpBizPartner;
    private javax.swing.JPanel jpCreditNotes;
    private javax.swing.JPanel jpCreditNotesToReturn;
    private javax.swing.JPanel jpCreditStatus;
    private javax.swing.JPanel jpData;
    private javax.swing.JPanel jpDataBizPartner;
    private javax.swing.JPanel jpFilter;
    private javax.swing.JPanel jpInvoices;
    private javax.swing.JPanel jpInvoicesBalance;
    private javax.swing.JPanel jpInvoicesToSend;
    private javax.swing.JPanel jpInvoicesToSupply;
    private javax.swing.JPanel jpOrders;
    private javax.swing.JPanel jpOrdersToProcess;
    private javax.swing.JTextField jtBizPartnerName;
    private javax.swing.JToggleButton jtbFullYear;
    private javax.swing.JToggleButton jtbPeriod01;
    private javax.swing.JToggleButton jtbPeriod02;
    private javax.swing.JToggleButton jtbPeriod03;
    private javax.swing.JToggleButton jtbPeriod04;
    private javax.swing.JToggleButton jtbPeriod05;
    private javax.swing.JToggleButton jtbPeriod06;
    private javax.swing.JToggleButton jtbPeriod07;
    private javax.swing.JToggleButton jtbPeriod08;
    private javax.swing.JToggleButton jtbPeriod09;
    private javax.swing.JToggleButton jtbPeriod10;
    private javax.swing.JToggleButton jtbPeriod11;
    private javax.swing.JToggleButton jtbPeriod12;
    private javax.swing.JTextField jtfBizPartnerAddress;
    private javax.swing.JTextField jtfBizPartnerCountry;
    private javax.swing.JTextField jtfBizPartnerCp;
    private javax.swing.JTextField jtfBizPartnerLocality;
    private javax.swing.JTextField jtfCreditStatusBalance;
    private javax.swing.JTextField jtfCreditStatusBalanceCur;
    private javax.swing.JTextField jtfCreditStatusCreditDays;
    private javax.swing.JTextField jtfCreditStatusCreditLimit;
    private javax.swing.JTextField jtfCreditStatusCreditLimitCur;
    private javax.swing.JTextField jtfCreditStatusCreditLimitType;
    private javax.swing.JTextField jtfCreditStatusGraceDays;
    private javax.swing.JFormattedTextField jtfCreditStatusOperationsStart;
    private javax.swing.JTextField jtfYearPeriod;
    private javax.swing.JTabbedPane jtpDataViews;
    // End of variables declaration//GEN-END:variables

    /*
     * Private methods
     */

    private void initComponentsExtra() {
        SGuiParams params = new SGuiParams(mnBizPartnerCategory);

        mvTableSettings = new Vector<STableSetting>();
        
        miClient.getSession().populateCatalogue(jcbBizPartner, SModConsts.BPSU_BP, mnBizPartnerCategory, null);
        jcbBizPartner.addItemListener(this);
        
        moInvoicesBalance = new SViewInvoicesBalance((SGuiClient) miClient, mnViewType, TAB_INV_BAL, "Facturas con saldo y anticipos", params);
        jpInvoicesBalance.add(moInvoicesBalance);
        
        moInvoicesToSupply = new SViewInvoicesToSupply((SGuiClient) miClient, mnViewType, TAB_INV_SUP , "Facturas x surtir", params);
        jpInvoicesToSupply.add(moInvoicesToSupply);
        
        moCreditNotesToReturn = new SViewCreditNotesToReturn((SGuiClient) miClient, mnViewType, TAB_CN_RET, "Notas de crédito x devolver ", params);
        jpCreditNotesToReturn.add(moCreditNotesToReturn);
        
        moInvoicesToSend = new SViewInvoicesToSend((SGuiClient) miClient, mnViewType, TAB_INV_SND , "Facturas x enviar", params);
        jpInvoicesToSend.add(moInvoicesToSend);
        
        moInvoices = new SViewInvoices((SGuiClient) miClient, mnViewType, TAB_INV , "Facturas", params);
        jpInvoices.add(moInvoices);
        
        moCreditNotes = new SViewCreditNotes((SGuiClient) miClient, mnViewType, TAB_CN, "Notas de crédito", params);
        jpCreditNotes.add(moCreditNotes);
        
        moOrders = new SViewOrders((SGuiClient) miClient, mnViewType, TAB_ORD , "Pedidos", params);
        jpOrders.add(moOrders);
        
        moOrdersToProcess = new SViewOrdersToProcess((SGuiClient) miClient, mnViewType, TAB_ORD_PRC , "Pedidos x procesar", params);
        jpOrdersToProcess.add(moOrdersToProcess);
        
        moFilterYear = new STabFilterYear(miClient, this);
        jpFilter.add(moFilterYear, 2);
        
        resetBizPartner();
        resetBizPartnerCategory();
     
        majtbPeriods = new JToggleButton[13];
        majtbPeriods[0] = jtbPeriod01;
        majtbPeriods[1] = jtbPeriod02;
        majtbPeriods[2] = jtbPeriod03;
        majtbPeriods[3] = jtbPeriod04;
        majtbPeriods[4] = jtbPeriod05;
        majtbPeriods[5] = jtbPeriod06;
        majtbPeriods[6] = jtbPeriod07;
        majtbPeriods[7] = jtbPeriod08;
        majtbPeriods[8] = jtbPeriod09;
        majtbPeriods[9] = jtbPeriod10;
        majtbPeriods[10] = jtbPeriod11;
        majtbPeriods[11] = jtbPeriod12;
        majtbPeriods[12] = jtbFullYear;

        mnYear = miClient.getSession().getCurrentYear();
        int month = SLibTimeUtils.digestMonth(miClient.getSession().getCurrentDate())[1];
        String[] months = SLibTimeUtils.createMonthsOfYear(Locale.getDefault(), Calendar.LONG);
        
        for (int index = 0; index < SLibTimeConsts.MONTHS; index++) {
            majtbPeriods[index].setToolTipText(SUtilConsts.TXT_SELECT + " " + months[index]);
            majtbPeriods[index].addActionListener(this);
            if (index + 1 == month) {
                majtbPeriods[index].setEnabled(true);
            }
        }
        
        jtbFullYear.setToolTipText(SUtilConsts.TXT_SELECT + " año");
        jtbFullYear.addActionListener(this);
        
        jtpDataViews.addChangeListener(this);
        
        jtfCreditStatusBalanceCur.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        jtfCreditStatusCreditLimitCur.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
        
        stateChangedDataViews();
    }
    
    private void initViewData() {
        int idBizPartner = moBizPartner == null ? SLibConsts.UNDEFINED : moBizPartner.getPkBizPartnerId();
        moOrders.initView(mtDateStart, mtDateEnd, mnYear, idBizPartner);
        moOrdersToProcess.initView(mtDateStart, mtDateEnd, mnYear, idBizPartner);
        moInvoices.initView(mtDateStart, mtDateEnd, mnYear, idBizPartner);
        moInvoicesToSend.initView(mtDateStart, mtDateEnd, mnYear, idBizPartner);
        moInvoicesToSupply.initView(mtDateStart, mtDateEnd, mnYear, idBizPartner);
        moCreditNotes.initView(mtDateStart, mtDateEnd, mnYear, idBizPartner);
        moInvoicesBalance.initView(mtDateStart, mtDateEnd, mnYear, idBizPartner);
        moCreditNotesToReturn.initView(mnYear, idBizPartner);
    }
    
    private void setYear(final int year) {
        int month = 0;
        mnYear = year;  
        
        if (jtbFullYear.isSelected()) {
            selectFullYear();
        }
        else {
            for (int period = 0; period < majtbPeriods.length; period++) {
                if (majtbPeriods[period].isSelected()) {
                    month = period + 1;
                    break;
                }
            }
            selectPeriod(month);
        }
    }
     
    private void selectFullYear() {
        mtDateStart = SLibTimeUtils.getBeginOfMonth(SLibTimeUtils.createDate(mnYear, 1));
        mtDateEnd = SLibTimeUtils.getEndOfYear(mtDateStart);
        jtfYearPeriod.setText(SLibUtils.DateFormatDate.format(mtDateEnd));
    }
    
    private void selectPeriod(int month) {
        mtDateStart = SLibTimeUtils.getBeginOfMonth(SLibTimeUtils.createDate(mnYear, month));
        mtDateEnd = SLibTimeUtils.getEndOfMonth(mtDateStart);
        jtfYearPeriod.setText(SLibUtils.DateFormatDate.format(mtDateEnd));
    }
    
    public void resetBizPartner() {
        jtBizPartnerName.setText("");
        jtfBizPartnerAddress.setText("");
        jtfBizPartnerLocality.setText("");
        jtfBizPartnerCp.setText("");
        jtfBizPartnerCountry.setText("");
        moBizPartner = null;
    }
    
    public void resetBizPartnerCategory() {
        jtfCreditStatusBalance.setText("");
        jtfCreditStatusCreditLimit.setText("");
        jtfCreditStatusCreditDays.setText("");
        jtfCreditStatusGraceDays.setText("");
        jtfCreditStatusOperationsStart.setText("");
        jtfCreditStatusCreditLimitType.setText("");
    }
    
    private void renderBizPartner() {
        try {
            int[] pk = (int[]) (((SGuiItem) jcbBizPartner.getSelectedItem()).getPrimaryKey());
            moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, pk, SLibConstants.EXEC_MODE_SILENT);
            
            if (moBizPartner != null) {
                moBizPartnerCategory = mnBizPartnerCategory == SDataConstantsSys.BPSS_CT_BP_CUS ? moBizPartner.getDbmsCategorySettingsCus() : moBizPartner.getDbmsCategorySettingsSup();

                jtBizPartnerName.setText(moBizPartner.getBizPartner());
                jtfBizPartnerAddress.setText(moBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getStreet()           
                        + " " + moBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getStreetNumberExt()
                        + " " + moBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getStreetNumberInt());
                jtfBizPartnerLocality.setText(moBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getLocality());
                jtfBizPartnerCp.setText(moBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getZipCode());
                jtfBizPartnerCountry.setText(moBizPartner.getDbmsBizPartnerBranchHq().getDbmsBizPartnerBranchAddressOfficial().getDbmsDataCountry().getCountry());

                if (moBizPartnerCategory != null) {
                    jtfCreditStatusBalance.setText(SLibUtils.DecimalFormatValue2D.format(SFinUtilities.getBizPartnerBalance(miClient, moBizPartner.getPkBizPartnerId(), mnBizPartnerCategory, null)));
                    jtfCreditStatusCreditLimit.setText(SLibUtils.DecimalFormatValue2D.format(moBizPartnerCategory.getCreditLimit()) + "");
                    jtfCreditStatusCreditLimitCur.setText(miClient.getSession().getSessionCustom().getLocalCurrencyCode());
                    jtfCreditStatusCreditDays.setText(SLibUtils.DecimalFormatValue0D.format(moBizPartnerCategory.getDaysOfCredit()) + "");
                    jtfCreditStatusGraceDays.setText(SLibUtils.DecimalFormatValue0D.format(moBizPartnerCategory.getDaysOfGrace()) + "");
                    jtfCreditStatusOperationsStart.setText(SLibUtils.DateFormatDate.format(moBizPartnerCategory.getDateStart()));
                    jtfCreditStatusCreditLimitType.setText(moBizPartnerCategory.getDbmsCreditType() == null ? "" : moBizPartnerCategory.getDbmsCreditType());
                }
                else {
                    resetBizPartnerCategory();
                }
                
                initViewData();
            }
            else {
                resetBizPartner();
                resetBizPartnerCategory();
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
    }
    
    private boolean doesTabRequirePeriods(int tab) {
        return SLibUtils.belongsTo(tab, new int[] { TAB_INV, TAB_CN, TAB_ORD });
    }
    
    private void preserveCurrentPeriodIndex() {
        mnCurrentPeriodIndex = -1;
        
        for (int index = 0; index < majtbPeriods.length; index++) {
            if (majtbPeriods[index].isSelected()) {
                mnCurrentPeriodIndex = index;
                break;
            }
        }
    }
    
    private void restoreCurrentPeriodIndex() {
        if (mnCurrentPeriodIndex != -1 && mnCurrentPeriodIndex < majtbPeriods.length) {
            majtbPeriods[mnCurrentPeriodIndex].setSelected(true);
        }
    }
    
    private void actionPerformedFullYear() {
        selectFullYear();
        initViewData();
    }
    
    private void actionPerformedPeriod() {
        for (int period = 0; period < majtbPeriods.length; period++) {
            if (majtbPeriods[period].isSelected()) {
                selectPeriod(period + 1);   // as month
                initViewData();
                break;
            }
        }
    }

    private void itemStateChangedBizPartner() {
        renderBizPartner();
    }
    
    private void stateChangedDataViews() {
        if (doesTabRequirePeriods(jtpDataViews.getSelectedIndex())) {
            if (majtbPeriods[0].isEnabled()) {
                preserveCurrentPeriodIndex();
            }
            else {
                // period toggle buttons will be enabled:
                
                for (JToggleButton majtbPeriod : majtbPeriods) {
                    majtbPeriod.setEnabled(true);
                }

                restoreCurrentPeriodIndex();
                actionPerformedPeriod();
            }
        }
        else {
            if (majtbPeriods[0].isEnabled()) {
                // period toggle buttons will be disabled:
                
                for (JToggleButton majtbPeriod : majtbPeriods) {
                    majtbPeriod.setEnabled(false);
                }
                
                preserveCurrentPeriodIndex();
                actionPerformedFullYear();
                jbgPeriods.clearSelection();
            }
        }
    }
    
    /*
     * Public methods:  
     */
    
    @Override
    public int getTabType() {
        return mnViewType;
    }
    
    @Override
    public int getTabTypeAux01() {
        return SLibConsts.UNDEFINED;
    }

    @Override
    public int getTabTypeAux02() {
        return SLibConsts.UNDEFINED;
    }
    
    @Override
    public void addSetting(STableSetting setting) {
        boolean add = true;
        erp.lib.table.STableSetting settingAux;

        for (int i = 0; i < mvTableSettings.size(); i++) {
            settingAux = (erp.lib.table.STableSetting) mvTableSettings.get(i);
            if (settingAux.getType() == setting.getType()) {
                add = false;
                mvTableSettings.set(i, setting);
                break;
            }
        }

        if (add) {
            mvTableSettings.add(setting);
        }
    }

    @Override
    public void updateSetting(STableSetting setting) {
        addSetting(setting);

        if (setting.getType() == STableConstants.SETTING_FILTER_YEAR) {
            try {
                setYear((Integer) setting.getSetting());
                initViewData();
            }
            catch (Exception ex) {
                SLibUtils.printException(this, ex);
            }
        }
    }

    @Override
    public void actionRefresh(int mode) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector<Integer> getSuscriptors() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vector<STableSetting> getTableSettings() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JToggleButton) {
            JToggleButton toggleButton = (JToggleButton) e.getSource();
            
            if (toggleButton == jtbFullYear) {
                actionPerformedFullYear();
            }
            else {
                actionPerformedPeriod();
            }
        }
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        if (e.getSource() instanceof JComboBox) {
            if (e.getStateChange() == ItemEvent.SELECTED) {
                JComboBox comboBox = (JComboBox) e.getSource();
                if (comboBox == jcbBizPartner) {
                    itemStateChangedBizPartner();
                }
            }
        }
    }

    @Override
    public void stateChanged(ChangeEvent ce) {
        if (ce.getSource() instanceof JTabbedPane) {
            JTabbedPane tabbedPane = (JTabbedPane) ce.getSource();
            
            if (tabbedPane == jtpDataViews) {
                stateChangedDataViews();
            }
        }
    }
}
