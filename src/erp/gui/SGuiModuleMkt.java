/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.form.SFormOptionPicker;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import erp.mmkt.data.SDataCommissionsSalesAgent;
import erp.mmkt.data.SDataCommissionsSalesAgentType;
import erp.mmkt.data.SDataCommissionsSalesAgentTypes;
import erp.mmkt.data.SDataCommissionsSalesAgents;
import erp.mmkt.data.SDataConfigurationSalesAgent;
import erp.mmkt.data.SDataCustomerConfig;
import erp.mmkt.data.SDataCustomerType;
import erp.mmkt.data.SDataDistributionChannel;
import erp.mmkt.data.SDataMarketSegment;
import erp.mmkt.data.SDataMarketSubSegment;
import erp.mmkt.data.SDataPriceList;
import erp.mmkt.data.SDataPriceListBizPartnerLink;
import erp.mmkt.data.SDataSalesRoute;
import erp.mmkt.form.SDialogRepCommissions;
import erp.mmkt.form.SFormCommissionsSalesAgent;
import erp.mmkt.form.SFormCommissionsSalesAgents;
import erp.mmkt.form.SFormConfigurationSalesAgent;
import erp.mmkt.form.SFormCustomerConfiguration;
import erp.mmkt.form.SFormCustomerType;
import erp.mmkt.form.SFormDistributionChannel;
import erp.mmkt.form.SFormMarketSegment;
import erp.mmkt.form.SFormMarketSubsegment;
import erp.mmkt.form.SFormPriceList;
import erp.mmkt.form.SFormPriceListBizPartnerLink;
import erp.mmkt.form.SFormSalesRoute;
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author Sergio Flores, Uriel Castañeda, Edwin Carmona
 */
public class SGuiModuleMkt extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmCatalogs;
    private javax.swing.JMenuItem jmiCustomerType;
    private javax.swing.JMenuItem jmiMarketSegment;
    private javax.swing.JMenuItem jmiMarketSubsegment;
    private javax.swing.JMenuItem jmiDistributionChannel;
    private javax.swing.JMenuItem jmiSalesRoute;
    private javax.swing.JMenuItem jmiSalesAgent;
    private javax.swing.JMenuItem jmiCustomerConfiguration;

    private javax.swing.JMenu jmConfiguration;
    private javax.swing.JMenuItem jmiCommisionsAgent;
    private javax.swing.JMenuItem jmiCommisionsAgents;
    private javax.swing.JMenuItem jmiCommisionsQueryAgent;
    private javax.swing.JMenuItem jmiCommisionsAgentType;
    private javax.swing.JMenuItem jmiCommisionsAgentTypes;
    private javax.swing.JMenuItem jmiCommisionsQueryAgentType;
    private javax.swing.JMenuItem jmiCommisionsConfigAgent;

    private javax.swing.JMenu jmPurchasesPriceList;
    private javax.swing.JMenuItem jmiPurchasesPriceList;
    private javax.swing.JMenuItem jmiPurchasesPriceListBizPartnerType;
    private javax.swing.JMenuItem jmiPurchasesPriceListSupplier;
    private javax.swing.JMenuItem jmiPurchasesPriceListBizPartnerBranch;
    private javax.swing.JMenuItem jmiPurchasesPriceListItemPrice;
    
    private javax.swing.JMenu jmSalesPriceList;
    private javax.swing.JMenuItem jmiSalesPriceList;
    private javax.swing.JMenuItem jmiSalesPriceListCustomerType;
    private javax.swing.JMenuItem jmiSalesPriceListCustomer;
    private javax.swing.JMenuItem jmiSalesPriceListCustomerBranch;
    private javax.swing.JMenuItem jmiSalesPriceListItemPrice;

    private javax.swing.JMenu jmCommissions;
    private javax.swing.JMenuItem jmiCommissions;
    private javax.swing.JMenuItem jmiCommissionsDetail;
    private javax.swing.JMenuItem jmiCommissionsPayment;
    private javax.swing.JMenuItem jmiCommissionsPaymentDetail;
    private javax.swing.JMenuItem jmiCommissionsDpsSalesAgent;
    private javax.swing.JMenuItem jmiCommissionsMoneyFlow;

    private javax.swing.JMenu jmReports;
    private javax.swing.JMenuItem jmiReportsCommisionsDps;

    private erp.mmkt.form.SFormCustomerType moFormCustomerType;
    private erp.mmkt.form.SFormMarketSegment moFormMarketSegment;
    private erp.mmkt.form.SFormMarketSubsegment moFormMarketSubsegment;
    private erp.mmkt.form.SFormCustomerConfiguration moFormCustomerConfiguration;
    private erp.mmkt.form.SFormDistributionChannel moFormDistributionChannel;
    private erp.mmkt.form.SFormSalesRoute moFormSalesRoute;

    private erp.mmkt.form.SFormPriceList moFormPriceList;
    private erp.mmkt.form.SFormPriceListBizPartnerLink moFormPriceListCustomer;
    private erp.mmkt.form.SFormConfigurationSalesAgent moFormConfigurationSalesAgent;
    private erp.mmkt.form.SFormCommissionsSalesAgents moFormCommisionsSalesAgents;
    private erp.mmkt.form.SFormCommissionsSalesAgents moFormCommisionsSalesAgentTypes;
    private erp.mmkt.form.SFormCommissionsSalesAgent moFormCommisionsSalesAgent;
    private erp.mmkt.form.SFormCommissionsSalesAgent moFormCommisionsSalesAgentType;

    private erp.mmkt.form.SDialogRepCommissions moDialogRepCommisions;

    private erp.form.SFormOptionPicker moPickerCustomerType;
    private erp.form.SFormOptionPicker moPickerCustomerConfiguration;
    private erp.form.SFormOptionPicker moPickerPriceList;
    private erp.form.SFormOptionPicker moPickerPriceListCustomer;
    private erp.form.SFormOptionPicker moPickerSegment;
    private erp.form.SFormOptionPicker moPickerSubsegment;
    private erp.form.SFormOptionPicker moPickerDistributionChannel;
    private erp.form.SFormOptionPicker moPickerSalesRoute;
    private erp.form.SFormOptionPicker moPickerSalesAgentType;

    public SGuiModuleMkt(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_MKT);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightPurchasesPriceList = false;
        boolean hasRightSalesPriceList = false;
        boolean hasRightConfigComms = false;
        boolean hasRightReports = false;

        jmCatalogs = new JMenu("Catálogos");
        jmiCustomerType = new JMenuItem("Tipos de cliente");
        jmiMarketSegment = new JMenuItem("Segmentos de mercado");
        jmiMarketSubsegment = new JMenuItem("Subsegmentos de mercado");
        jmiDistributionChannel = new JMenuItem("Canales de distribución");
        jmiSalesRoute = new JMenuItem("Rutas de ventas");
        jmiSalesAgent = new JMenuItem("Agentes de ventas");
        jmiCustomerConfiguration = new JMenuItem("Configuraciones de clientes");

        jmConfiguration = new JMenu("Configuración");
        jmiCommisionsAgents = new JMenuItem("Comisiones de agente de ventas");
        jmiCommisionsAgent = new JMenuItem("Comisiones de agente de ventas a detalle");
        jmiCommisionsQueryAgent = new JMenuItem("Consulta de comisiones de agente de ventas");
        jmiCommisionsAgentTypes = new JMenuItem("Comisiones de tipo de agente de ventas");
        jmiCommisionsAgentType = new JMenuItem("Comisiones de tipo de agente de ventas a detalle");
        jmiCommisionsQueryAgentType = new JMenuItem("Consulta de comisiones de tipo de agente de ventas");
        jmiCommisionsConfigAgent = new JMenuItem("Configuración de agentes de ventas");

        jmPurchasesPriceList = new JMenu("Listas de precios compras");
        jmiPurchasesPriceList = new JMenuItem("Documentos de listas de precios");
        jmiPurchasesPriceListBizPartnerType = new JMenuItem("Listas de precios por tipo de proveedor");
        jmiPurchasesPriceListSupplier = new JMenuItem("Listas de precios por proveedor");
        jmiPurchasesPriceListBizPartnerBranch = new JMenuItem("Listas de precios por sucursal de proveedor");
        jmiPurchasesPriceListItemPrice = new JMenuItem("Precios de ítems");

        jmSalesPriceList = new JMenu("Listas de precios ventas");
        jmiSalesPriceList = new JMenuItem("Documentos de listas de precios");
        jmiSalesPriceListCustomerType = new JMenuItem("Listas de precios por tipo de cliente");
        jmiSalesPriceListCustomer = new JMenuItem("Listas de precios por cliente");
        jmiSalesPriceListCustomerBranch = new JMenuItem("Listas de precios por sucursal de cliente");
        jmiSalesPriceListItemPrice = new JMenuItem("Precios de ítems");

        jmCommissions = new JMenu("Comisiones ventas");
        jmiCommissions = new JMenuItem("Comisiones");
        jmiCommissionsDetail = new JMenuItem("Comisiones a detalle");
        jmiCommissionsPayment = new JMenuItem("Pagos comisiones");
        jmiCommissionsPaymentDetail = new JMenuItem("Pagos comisiones a detalle");
        jmiCommissionsDpsSalesAgent = new JMenuItem("Facturas por agente ventas");
        jmiCommissionsMoneyFlow = new JMenuItem("Comisiones por flujo de dinero");

        jmReports = new JMenu("Reportes");
        jmiReportsCommisionsDps = new JMenuItem("Reporte de comisiones y pagos");

        jmCatalogs.add(jmiCustomerType);
        jmCatalogs.add(jmiMarketSegment);
        jmCatalogs.add(jmiMarketSubsegment);
        jmCatalogs.add(jmiDistributionChannel);
        jmCatalogs.add(jmiSalesRoute);
        jmCatalogs.add(jmiSalesAgent);
        jmCatalogs.addSeparator();
        jmCatalogs.add(jmiCustomerConfiguration);

        jmConfiguration.add(jmiCommisionsAgents);
        jmConfiguration.add(jmiCommisionsAgent);
        // jmConfiguration.add(jmiCommisionsQueryAgent);
        jmConfiguration.addSeparator();
        jmConfiguration.add(jmiCommisionsAgentTypes);
        jmConfiguration.add(jmiCommisionsAgentType);
        // jmConfiguration.add(jmiCommisionsQueryAgentType);
        jmConfiguration.addSeparator();
        jmConfiguration.add(jmiCommisionsConfigAgent);

        jmPurchasesPriceList.add(jmiPurchasesPriceList);
        jmPurchasesPriceList.addSeparator();
        jmPurchasesPriceList.add(jmiPurchasesPriceListBizPartnerType);
        jmPurchasesPriceList.add(jmiPurchasesPriceListSupplier);
        jmPurchasesPriceList.add(jmiPurchasesPriceListBizPartnerBranch);
        jmPurchasesPriceList.addSeparator();
        jmPurchasesPriceList.add(jmiPurchasesPriceListItemPrice);

        jmSalesPriceList.add(jmiSalesPriceList);
        jmSalesPriceList.addSeparator();
        jmSalesPriceList.add(jmiSalesPriceListCustomerType);
        jmSalesPriceList.add(jmiSalesPriceListCustomer);
        jmSalesPriceList.add(jmiSalesPriceListCustomerBranch);
        jmSalesPriceList.addSeparator();
        jmSalesPriceList.add(jmiSalesPriceListItemPrice);

        jmCommissions.add(jmiCommissions);
        jmCommissions.add(jmiCommissionsDetail);
        jmCommissions.addSeparator();
        jmCommissions.add(jmiCommissionsPayment);
        jmCommissions.add(jmiCommissionsPaymentDetail);
        jmCommissions.addSeparator();
        jmCommissions.add(jmiCommissionsDpsSalesAgent);
        jmCommissions.addSeparator();
        jmCommissions.add(jmiCommissionsMoneyFlow);

        // jmReports.add(jmiReportsCommisionsItems); XXX pending with new format and version
        jmReports.add(jmiReportsCommisionsDps);

        jmiCustomerType.addActionListener(this);
        jmiCustomerConfiguration.addActionListener(this);
        jmiMarketSegment.addActionListener(this);
        jmiMarketSubsegment.addActionListener(this);
        jmiDistributionChannel.addActionListener(this);
        jmiSalesRoute.addActionListener(this);
        jmiSalesAgent.addActionListener(this);
        jmiSalesPriceList.addActionListener(this);
        jmiSalesPriceListCustomerType.addActionListener(this);
        jmiSalesPriceListCustomer.addActionListener(this);
        jmiSalesPriceListCustomerBranch.addActionListener(this);
        jmiSalesPriceListItemPrice.addActionListener(this);
        jmiPurchasesPriceList.addActionListener(this);
        jmiPurchasesPriceListBizPartnerType.addActionListener(this);
        jmiPurchasesPriceListSupplier.addActionListener(this);
        jmiPurchasesPriceListBizPartnerBranch.addActionListener(this);
        jmiPurchasesPriceListItemPrice.addActionListener(this);
        jmiCommissions.addActionListener(this);
        jmiCommissionsDetail.addActionListener(this);
        jmiCommissionsPayment.addActionListener(this);
        jmiCommissionsPaymentDetail.addActionListener(this);
        jmiCommissionsDpsSalesAgent.addActionListener(this);
        jmiCommissionsMoneyFlow.addActionListener(this);
        jmiCommisionsAgents.addActionListener(this);
        jmiCommisionsAgentTypes.addActionListener(this);
        jmiCommisionsAgent.addActionListener(this);
        jmiCommisionsAgentType.addActionListener(this);
        jmiCommisionsConfigAgent.addActionListener(this);
        jmiCommisionsQueryAgent.addActionListener(this);
        jmiCommisionsQueryAgentType.addActionListener(this);
        jmiReportsCommisionsDps.addActionListener(this);

        moFormCustomerType = null;
        moFormCustomerConfiguration = null;
        moFormDistributionChannel = null;
        moFormMarketSegment = null;
        moFormSalesRoute = null;
        moFormPriceList = null;
        moFormPriceListCustomer = null;
        moFormCommisionsSalesAgent = null;
        moFormCommisionsSalesAgentType = null;
        moFormConfigurationSalesAgent = null;

        moPickerCustomerType = null;
        moPickerCustomerConfiguration = null;
        moPickerPriceList = null;
        moPickerPriceListCustomer = null;
        moPickerSegment = null;
        moPickerSubsegment = null;
        moPickerDistributionChannel = null;
        moPickerSalesRoute = null;

        moDialogRepCommisions = new SDialogRepCommissions(miClient);

        hasRightPurchasesPriceList = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MKT_PLIST_PUR).HasRight;
        hasRightSalesPriceList = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MKT_PLIST_SAL).HasRight;
        hasRightConfigComms = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MKT_PLIST_SAL).HasRight;
        hasRightReports = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MKT_REP).HasRight;

        jmPurchasesPriceList.setEnabled(hasRightPurchasesPriceList);
        jmPurchasesPriceList.setEnabled(hasRightSalesPriceList);

        jmConfiguration.setEnabled(hasRightConfigComms);
        jmCommissions.setEnabled(hasRightConfigComms);
        jmiCommisionsAgents.setEnabled(hasRightConfigComms);
        jmiCommisionsAgentTypes.setEnabled(hasRightConfigComms);
        jmiCommisionsAgent.setEnabled(hasRightConfigComms);
        jmiCommisionsAgentType.setEnabled(hasRightConfigComms);
        jmiCommisionsConfigAgent.setEnabled(hasRightConfigComms);

        jmReports.setEnabled(hasRightReports);
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.MKTU_TP_CUS:
                    if (moFormCustomerType == null) {
                        moFormCustomerType = new SFormCustomerType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCustomerType();
                    }
                    miForm = moFormCustomerType;
                    break;
                case SDataConstants.MKT_CFG_CUS:
                    if (moFormCustomerConfiguration == null) {
                        moFormCustomerConfiguration = new SFormCustomerConfiguration(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataCustomerConfig();
                    }
                    miForm = moFormCustomerConfiguration;
                    break;
                case SDataConstants.MKTU_MKT_SEGM:
                    if (moFormMarketSegment == null) {
                        moFormMarketSegment = new SFormMarketSegment(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataMarketSegment();
                    }
                    miForm = moFormMarketSegment;
                    break;
                case SDataConstants.MKTU_MKT_SEGM_SUB:
                    if (moFormMarketSubsegment == null) {
                        moFormMarketSubsegment = new SFormMarketSubsegment(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataMarketSubSegment();
                    }
                    miForm = moFormMarketSubsegment;
                    break;
                case SDataConstants.MKTU_DIST_CHAN:
                    if (moFormDistributionChannel == null) {
                        moFormDistributionChannel = new SFormDistributionChannel(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataDistributionChannel();
                    }
                    miForm = moFormDistributionChannel;
                    break;
                case SDataConstants.MKTU_SAL_ROUTE:
                    if (moFormSalesRoute == null) {
                        moFormSalesRoute = new SFormSalesRoute(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataSalesRoute();
                    }
                    miForm = moFormSalesRoute;
                    break;
                case SDataConstants.MKT_PLIST:
                    if (moFormPriceList == null) {
                        moFormPriceList = new SFormPriceList(miClient);
                    }
                    moFormPriceList.setValue(SDataConstantsSys.TRNS_CT_DPS_SAL, auxType);

                    if (pk != null) {
                        moRegistry = new SDataPriceList();
                        ((SDataPriceList) moRegistry).setAuxSortingItem(miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());
                    }
                    miForm = moFormPriceList;
                    break;
                case SDataConstants.MKT_PLIST_CUS:
                    if (moFormPriceListCustomer == null) {
                        moFormPriceListCustomer = new SFormPriceListBizPartnerLink(miClient);
                    }
                    moFormPriceListCustomer.setValue(SDataConstants.BPSS_LINK, auxType);
                    if (pk != null) {
                        moRegistry = new SDataPriceListBizPartnerLink();
                    }
                    miForm = moFormPriceListCustomer;
                    break;
                case SDataConstants.MKT_CFG_SAL_AGT:
                    if (moFormConfigurationSalesAgent == null) {
                        moFormConfigurationSalesAgent = new SFormConfigurationSalesAgent(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataConfigurationSalesAgent();
                    }
                    miForm = moFormConfigurationSalesAgent;
                    break;

                case SDataConstants.MKTX_COMMS_SAL_AGTS:
                    if (moFormCommisionsSalesAgents == null) {
                        moFormCommisionsSalesAgents = new SFormCommissionsSalesAgents(miClient, SDataConstants.MKT_COMMS_SAL_AGT, "Configuración agentes de ventas por referencia");
                    }
                    if (pk != null) {
                        moRegistry = new SDataCommissionsSalesAgents();
                    }
                    miForm = moFormCommisionsSalesAgents;
                    break;

                case SDataConstants.MKTX_COMMS_SAL_AGT_TPS:
                    if (moFormCommisionsSalesAgentTypes == null) {
                        moFormCommisionsSalesAgentTypes = new SFormCommissionsSalesAgents(miClient, SDataConstants.MKT_COMMS_SAL_AGT_TP, "Configuración tipos de agente de ventas por referencia");
                    }
                    if (pk != null) {
                        moRegistry = new SDataCommissionsSalesAgentTypes();
                    }
                    miForm = moFormCommisionsSalesAgentTypes;
                    break;

                case SDataConstants.MKT_COMMS_SAL_AGT:
                    if (moFormCommisionsSalesAgent == null) {
                        moFormCommisionsSalesAgent = new SFormCommissionsSalesAgent(miClient, SDataConstants.MKT_COMMS_SAL_AGT, "Comisión por agente de ventas");
                    }
                    if (pk != null) {
                        moRegistry = new SDataCommissionsSalesAgent();
                    }
                    miForm = moFormCommisionsSalesAgent;
                    break;

                case SDataConstants.MKT_COMMS_SAL_AGT_TP:
                    if (moFormCommisionsSalesAgentType == null) {
                        moFormCommisionsSalesAgentType = new SFormCommissionsSalesAgent(miClient, SDataConstants.MKT_COMMS_SAL_AGT_TP, "Comisión por tipo agente de ventas");
                    }
                    if (pk != null) {
                        moRegistry = new SDataCommissionsSalesAgentType();
                    }
                    miForm = moFormCommisionsSalesAgentType;
                    break;

                default:
                    throw new Exception("¡La forma no existe!");
            }

            // Additional configuration, if applies:

            switch (formType) {
                case SDataConstants.MKT_PLIST_CUS:
                    if (moFormComplement != null) {
                        miForm.setValue(SModConsts.BPSS_CT_BP, moFormComplement);
                    }
                    break;

                default:
            }

            result = processForm(pk, isCopy);
            clearFormComplement();
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }

        return result;
    }

    @Override
    public void showView(int viewType) {
        showView(viewType, 0, 0);
    }

    @Override
    public void showView(int viewType, int auxType) {
        showView(viewType, auxType, 0);
    }

    @Override
    public void showView(int viewType, int auxType01, int auxType02) {
        int nAuxType = SDataConstants.UNDEFINED;
        java.lang.String sViewTitle = "";
        java.lang.Class oViewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {
                case SDataConstants.MKTU_TP_CUS:
                    oViewClass = erp.mmkt.view.SViewCustomerType.class;
                    sViewTitle = "Tipos cliente";
                    break;
                case SDataConstants.MKT_CFG_CUS:
                    oViewClass = erp.mmkt.view.SViewCustomerConfiguration.class;
                    sViewTitle = "Configuraciones clientes";
                    break;
                case SDataConstants.MKTU_MKT_SEGM:
                    oViewClass = erp.mmkt.view.SViewMarketSegment.class;
                    sViewTitle = "Segmentos mercado";
                    break;
                case SDataConstants.MKTU_MKT_SEGM_SUB:
                    oViewClass = erp.mmkt.view.SViewMarketSubsegment.class;
                    sViewTitle = "Subsegmentos mercado";
                    break;
                case SDataConstants.MKTU_DIST_CHAN:
                    oViewClass = erp.mmkt.view.SViewDistributionChannel.class;
                    sViewTitle = "Canales distribución";
                    break;
                case SDataConstants.MKTU_SAL_ROUTE:
                    oViewClass = erp.mmkt.view.SViewSalesRoute.class;
                    sViewTitle = "Rutas ventas";
                    break;
                case SDataConstants.MKTX_SAL_AGT:
                    oViewClass = erp.mbps.view.SViewBizPartner.class;
                    sViewTitle = "Agentes ventas";
                    auxType01 = SDataConstants.BPSX_BP_ATT_SAL_AGT;
                    viewType = auxType01;
                    break;
                case SDataConstants.MKT_PLIST:
                    oViewClass = erp.mmkt.view.SViewPriceList.class;
                    sViewTitle = (auxType01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? "VTA" : "CPA") + " - Docs. listas precios";
                    break;
                case SDataConstants.MKT_PLIST_CUS:
                    oViewClass = erp.mmkt.view.SViewPriceListBizPartnerLink.class;
                    sViewTitle = (auxType02 == SDataConstantsSys.BPSS_CT_BP_CUS ? "VTA" : "CPA") +  " - Listas precios x " +
                            (auxType01 == SModSysConsts.BPSS_LINK_BP ? (auxType02 == SDataConstantsSys.BPSS_CT_BP_CUS ? "cliente" : "proveedor") :
                            auxType01 == SModSysConsts.BPSS_LINK_CUS_MKT_TP ? "tipo " + (auxType02 == SDataConstantsSys.BPSS_CT_BP_CUS ? "cliente" : "proveedor") :
                            auxType01 == SModSysConsts.BPSS_LINK_BP_TP ? "tipo " + (auxType02 == SDataConstantsSys.BPSS_CT_BP_CUS ? "cliente" : "proveedor") :
                            auxType01 == SModSysConsts.BPSS_LINK_BPB ? "sucursal " + (auxType02 == SDataConstantsSys.BPSS_CT_BP_CUS ? "cliente" : "proveedor") : "todos");
                    break;

                case SDataConstants.MKT_PLIST_ITEM:
                    oViewClass = erp.mmkt.view.SViewPriceListItem.class;
                    sViewTitle = (auxType01 == SDataConstantsSys.TRNS_CT_DPS_SAL ? "VTA" : "CPA") + " - Precios ítems";
                    break;
                case SDataConstants.MKTX_COMMS_DPS_SAL_AGT:
                    oViewClass = erp.mmkt.view.SViewDpsSalesAgent.class;
                    sViewTitle = "Facturas x agente ventas";
                    break;
                case SDataConstants.MKT_COMMS:
                    oViewClass = erp.mmkt.view.SViewCommissions.class;
                    sViewTitle = "Comisiones " + (auxType01 != SDataConstants.MKTX_COMMS_RES ? " (detalle)" : "");
                    break;
                case SDataConstants.MKT_CFG_SAL_AGT:
                    oViewClass = erp.mmkt.view.SViewConfigurationSalesAgent.class;
                    sViewTitle = "Config. agentes ventas";
                    break;
                case SDataConstants.MKTX_COMMS_SAL_AGTS:
                    oViewClass = erp.mmkt.view.SViewCommissionsSalesAgents.class;
                    sViewTitle = "Coms. " + (auxType01 == SDataConstants.MKTX_COMMS_SAL_AGT_TPS ? "tipo " : "") + "agente ventas";
                    break;
                case SDataConstants.MKT_COMMS_SAL_AGT:
                    oViewClass = erp.mmkt.view.SViewCommissionsSalesAgent.class;
                    sViewTitle = "Coms. " + (auxType01 == SDataConstants.MKT_COMMS_SAL_AGT_TP ? "tipo " : "") + "agente ventas (detalle)";
                    break;
                case SDataConstants.MKTX_COMMS_SAL_AGT_CONS:
                    oViewClass = erp.mmkt.view.SViewCommissionsConsultSalesAgent.class;
                    sViewTitle = "Consulta coms. " + (auxType01 == SDataConstants.MKT_COMMS_SAL_AGT_TP ? "tipo " : "") + "agente ventas";
                    break;
                default:
                    throw new Exception("¡La vista no existe!");
            }

            processView(oViewClass, sViewTitle, viewType, auxType01, auxType02);
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            restoreFrameCursor();
        }
    }

    @Override
    public int showForm(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, false);
    }

    @Override
    public int showForm(int formType, int auxType, java.lang.Object pk) {
        return showForm(formType, auxType, pk, false);
    }

    @Override
    public int showFormForCopy(int formType, java.lang.Object pk) {
        return showForm(formType, SDataConstants.UNDEFINED, pk, true);
    }

    @Override
    public erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType) {
         SFormOptionPickerInterface picker = null;

        try {
            switch (optionType) {
                case SDataConstants.MKTU_TP_CUS:
                    picker = moPickerCustomerType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCustomerType);
                    break;
                case SDataConstants.MKT_CFG_CUS:
                    picker = moPickerCustomerConfiguration = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCustomerConfiguration);
                    break;
                case SDataConstants.MKT_PLIST:
                    picker = moPickerPriceList = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerPriceList);
                    break;
                case SDataConstants.MKT_PLIST_CUS:
                    picker = moPickerPriceListCustomer = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerPriceListCustomer);
                    break;
                case SDataConstants.MKTU_MKT_SEGM:
                    picker = moPickerSegment = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerSegment);
                    break;
                case SDataConstants.MKTU_MKT_SEGM_SUB:
                    picker = moPickerSubsegment = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerSubsegment);
                    break;
                case SDataConstants.MKTU_DIST_CHAN:
                    picker = moPickerDistributionChannel = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerDistributionChannel);
                    break;
                case SDataConstants.MKTU_SAL_ROUTE:
                    picker = moPickerSalesRoute = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerSalesRoute);
                    break;
                case SDataConstants.MKTU_TP_SAL_AGT:
                    picker = moPickerSalesAgentType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerSalesAgentType);
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM_PICK);
            }
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }

        return picker;
    }

    @Override
    public int annulRegistry(int registryType, java.lang.Object pk, sa.lib.gui.SGuiParams params) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int deleteRegistry(int registryType, java.lang.Object pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JMenu[] getMenues() {
        return new JMenu[] { jmCatalogs, jmConfiguration, jmPurchasesPriceList, jmSalesPriceList, jmCommissions, jmReports };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        javax.swing.JMenu[] menues = null;

        switch (moduleType) {
            case SDataConstants.MOD_MKT:
                menues = new JMenu[] { jmCatalogs, jmPurchasesPriceList, jmSalesPriceList };
                break;
            default:
                break;
        }

        return menues;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiCustomerType) {
                showView(SDataConstants.MKTU_TP_CUS);
            }
            else if (item == jmiCustomerConfiguration) {
                showView(SDataConstants.MKT_CFG_CUS);
            }
            else if (item == jmiMarketSegment) {
                showView(SDataConstants.MKTU_MKT_SEGM);
            }
            else if (item == jmiMarketSubsegment) {
                showView(SDataConstants.MKTU_MKT_SEGM_SUB);
            }
            else if (item == jmiDistributionChannel) {
                showView(SDataConstants.MKTU_DIST_CHAN);
            }
            else if (item == jmiSalesRoute) {
                showView(SDataConstants.MKTU_SAL_ROUTE);
            }
            else if (item == jmiSalesAgent) {
                showView(SDataConstants.MKTX_SAL_AGT);
            }
            else if (item == jmiSalesPriceList) {
                showView(SDataConstants.MKT_PLIST, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiSalesPriceListCustomerType) {
                showView(SDataConstants.MKT_PLIST_CUS, SModSysConsts.BPSS_LINK_CUS_MKT_TP, SDataConstantsSys.BPSS_CT_BP_CUS);
            }
            else if (item == jmiSalesPriceListCustomer) {
                showView(SDataConstants.MKT_PLIST_CUS, SModSysConsts.BPSS_LINK_BP, SDataConstantsSys.BPSS_CT_BP_CUS);
            }
            else if (item == jmiSalesPriceListCustomerBranch) {
                showView(SDataConstants.MKT_PLIST_CUS, SModSysConsts.BPSS_LINK_BPB, SDataConstantsSys.BPSS_CT_BP_CUS);
            }
            else if (item == jmiSalesPriceListItemPrice) {
                showView(SDataConstants.MKT_PLIST_ITEM, SDataConstantsSys.TRNS_CT_DPS_SAL);
            }
            else if (item == jmiPurchasesPriceList) {
                showView(SDataConstants.MKT_PLIST, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiPurchasesPriceListBizPartnerType) {
                showView(SDataConstants.MKT_PLIST_CUS, SModSysConsts.BPSS_LINK_BP_TP, SDataConstantsSys.BPSS_CT_BP_SUP);
            }
            else if (item == jmiPurchasesPriceListSupplier) {
                showView(SDataConstants.MKT_PLIST_CUS, SModSysConsts.BPSS_LINK_BP, SDataConstantsSys.BPSS_CT_BP_SUP);
            }
            else if (item == jmiPurchasesPriceListBizPartnerBranch) {
                showView(SDataConstants.MKT_PLIST_CUS, SModSysConsts.BPSS_LINK_BPB, SDataConstantsSys.BPSS_CT_BP_SUP);
            }
            else if (item == jmiPurchasesPriceListItemPrice) {
                showView(SDataConstants.MKT_PLIST_ITEM, SDataConstantsSys.TRNS_CT_DPS_PUR);
            }
            else if (item == jmiCommissions) {
                showView(SDataConstants.MKT_COMMS, SDataConstants.MKTX_COMMS_RES);
            }
            else if (item == jmiCommissionsDetail) {
                showView(SDataConstants.MKT_COMMS, SDataConstants.MKTX_COMMS_DET);
            }
            else if (item == jmiCommissionsPayment) {
                miClient.getSession().showView(SModConsts.MKT_COMMS_PAY, SModConsts.VIEW_SC_SUM, null);
            }
            else if (item == jmiCommissionsPaymentDetail) {
                miClient.getSession().showView(SModConsts.MKT_COMMS_PAY, SModConsts.VIEW_SC_DET, null);
            }
            else if (item == jmiCommissionsDpsSalesAgent) {
                showView(SDataConstants.MKTX_COMMS_DPS_SAL_AGT, SDataConstantsSys.TRNX_TP_DPS_DOC);
            }
            else if (item == jmiCommissionsMoneyFlow) {
                miClient.getSession().showView(SModConsts.MKTX_COMMS_PAY_REC, SDataConstants.UNDEFINED, null);
            }
            else if (item == jmiCommisionsAgentTypes) {
                showView(SDataConstants.MKTX_COMMS_SAL_AGTS, SDataConstants.MKTX_COMMS_SAL_AGT_TPS);
            }
            else if (item == jmiCommisionsAgents) {
                showView(SDataConstants.MKTX_COMMS_SAL_AGTS, SDataConstants.MKTX_COMMS_SAL_AGTS);
            }
            else if (item == jmiCommisionsAgentType) {
                showView(SDataConstants.MKT_COMMS_SAL_AGT, SDataConstants.MKT_COMMS_SAL_AGT_TP);
            }
            else if (item == jmiCommisionsAgent) {
                showView(SDataConstants.MKT_COMMS_SAL_AGT, SDataConstants.MKT_COMMS_SAL_AGT);
            }
            else if (item == jmiCommisionsQueryAgent) {
                showView(SDataConstants.MKTX_COMMS_SAL_AGT_CONS, SDataConstants.MKT_COMMS_SAL_AGT);
            }
            else if (item == jmiCommisionsQueryAgentType) {
                showView(SDataConstants.MKTX_COMMS_SAL_AGT_CONS, SDataConstants.MKT_COMMS_SAL_AGT_TP);
            }
            else if (item == jmiCommisionsConfigAgent) {
                showView(SDataConstants.MKT_CFG_SAL_AGT);
            }
            else if (item == jmiReportsCommisionsDps) {
                moDialogRepCommisions.formReset();
                moDialogRepCommisions.setValue(1, SDataConstantsSys.REP_TRN_COMMS_DPS);
                moDialogRepCommisions.formRefreshCatalogues();
                moDialogRepCommisions.setVisible(true);
            }
        }
    }
}
