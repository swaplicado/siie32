/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.data.SDataReadDescriptions;
import erp.data.SDataUtilities;
import erp.form.SFormOptionPicker;
import erp.gui.mod.cfg.SCfgMenu;
import erp.gui.mod.cfg.SCfgModule;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.form.SFormOptionPickerInterface;
import erp.mmfg.data.*;
import erp.mmfg.form.*;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 *
 * @author Sergio Flores
 */
public class SGuiModuleMfg extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmCatalogs;
    private javax.swing.JMenuItem jmiBom;
    private javax.swing.JMenuItem jmiManufacturingLine;
    private javax.swing.JMenuItem jmiManufacturingLineConfigItem;
    private javax.swing.JMenuItem jmiLeadtimeCo;
    private javax.swing.JMenuItem jmiLeadtimeCob;
    private javax.swing.JMenuItem jmiGang;
    private javax.swing.JMenuItem jmiGangEmployee;
    private javax.swing.JMenu jmProductionOrders;
    private javax.swing.JMenuItem jmiProductionOrderAll;
    private javax.swing.JMenuItem jmiProductionOrderNew;
    private javax.swing.JMenuItem jmiProductionOrderLot;
    private javax.swing.JMenuItem jmiProductionOrderLotAssigned;
    private javax.swing.JMenuItem jmiProductionOrderProcess;
    private javax.swing.JMenuItem jmiProductionOrderEnd;
    private javax.swing.JMenuItem jmiProductionOrderClose;
    private javax.swing.JMenuItem jmiProductionOrderDelay;
    private javax.swing.JMenuItem jmiProductionOrderForecast;
    private javax.swing.JMenuItem jmiProductionOrderPerformance;
    private javax.swing.JMenuItem jmiProductionOrderFatherSon;
    private javax.swing.JMenu jmExplotionMaterials;
    private javax.swing.JMenuItem jmiExplotionMaterialsDialog;
    private javax.swing.JMenuItem jmiExplotionMaterials;
    private javax.swing.JMenuItem jmiExplotionMaterialsForecast;
    private javax.swing.JMenuItem jmiExplotionMaterialsProductionOrder;
    private javax.swing.JMenuItem jmiExplotionMaterialsForecastProductionOrder;
    private javax.swing.JMenuItem jmiExplotionMaterialsForecastDialog;
    private javax.swing.JMenu jmAssignLots;
    private javax.swing.JMenuItem jmiAssingLotsFinishedGood;
    private javax.swing.JMenuItem jmiDocumentsRequeriments;
    private javax.swing.JMenuItem jmiDocumentsPurchasesOrder;
    private javax.swing.JMenu jmManufacturingCost;
    private javax.swing.JMenuItem jmiManufacturingCost;
    private javax.swing.JMenuItem jmiManufacturingCostEmployees;
    private javax.swing.JMenuItem jmiManufacturingClosePeriod;
    private javax.swing.JMenu jmMaintenance;
    private javax.swing.JMenuItem jmiMaintenanceWorkOrder;
    private javax.swing.JMenuItem jmiMaintenanceEquipment;
    private javax.swing.JMenuItem jmiStatiticts;
    private javax.swing.JMenu jmMaintenanceReport;
    private javax.swing.JMenuItem jmiMaintenanceReportGlobal;
    private javax.swing.JMenuItem jmiMaintenanceReportByEquipment;
    private javax.swing.JMenuItem jmiMaintenanceReportByArea;
    private javax.swing.JMenu jmReportsStatiticsManufacturing;
    private javax.swing.JMenuItem jmiReportProductionByItem;
    private javax.swing.JMenuItem jmiReportProductionByItemGeneric;
    private javax.swing.JMenuItem jmiReportProductionByBizPartnetItem;
    private javax.swing.JMenuItem jmiReportProductionByItemBizPartner;
    private javax.swing.JMenu jmReports;
    private javax.swing.JMenuItem jmiReportProductionOrderProgramMonitoring;
    private javax.swing.JMenuItem jmiReportBomItems;
    private javax.swing.JMenuItem jmiReportProductionOrderPerformance;

    private erp.mmfg.form.SFormBom moFormBom;
    private erp.mmfg.form.SFormManufacturingLine moFormManufacturingLine;
    private erp.mmfg.form.SFormManufacturingLineConfigItem moFormManufacturingLineConfigItem;
    private erp.mmfg.form.SDialogExplotionMaterials moFormBomExp;
    private erp.mmfg.form.SFormCost moFormCost;
    private erp.mmfg.form.SFormExplotionMaterials moFormExplotionMaterials;
    private erp.mmfg.form.SFormGang moFormGang;
    private erp.mmfg.form.SFormLeadtime moFormLeadtime;
    private erp.mmfg.form.SFormProductionOrder moFormProductionOrder;
    private erp.mmfg.form.SFormProductionOrderCharge moFormProductionOrderCharges;
    private erp.mmfg.form.SFormRequisition moFormRequisition;
    private erp.mmfg.form.SDialogMfgClosePeriod moDialogMfgClosePeriod;
    private erp.mmfg.form.SDialogRepProductionOrderPerformance moDialogRepProductionOrderPerformance;
    private erp.mmfg.form.SDialogProductionOrderSaved moDialogProductionOrderSaved;
    private erp.mmfg.form.SDialogExplotionMaterials moDialogExplotionMaterials;

    private erp.form.SFormOptionPicker moPickerBom;
    private erp.form.SFormOptionPicker moPickerBomSubstitute;
    private erp.form.SFormOptionPicker moPickerOrd;
    private erp.form.SFormOptionPicker moPickerOrdPP;
    private erp.form.SFormOptionPicker moPickerLine;
    private erp.form.SFormOptionPicker moPickerManufacturingLine;

    public SGuiModuleMfg(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_MFG);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightBom = false;
        boolean hasRightExplotionMaterials = false;
        boolean hasRightAssignLot = false;
        boolean hasRightReports = false;

        jmCatalogs = new JMenu("Catálogos");
        jmiBom = new JMenuItem("Fórmulas");
        jmiManufacturingLine = new JMenuItem("Líneas de producción");
        jmiManufacturingLineConfigItem = new JMenuItem("Configuración de líneas de producción");
        jmiLeadtimeCo = new JMenuItem("Leadtime empresa");
        jmiLeadtimeCob = new JMenuItem("Leadtime sucursal");
        jmiGang = new JMenuItem("Cuadrillas");
        jmiGangEmployee = new JMenuItem("Cuadrillas vs. empleados (resumen)");

        jmCatalogs.add(jmiBom);
        jmCatalogs.add(new JSeparator());
        jmCatalogs.add(jmiManufacturingLine);
        jmCatalogs.add(jmiManufacturingLineConfigItem);
        jmCatalogs.add(new JSeparator());
        jmCatalogs.add(jmiLeadtimeCo);
        jmCatalogs.add(jmiLeadtimeCob);
        jmCatalogs.add(new JSeparator());
        jmCatalogs.add(jmiGang);
        jmCatalogs.add(jmiGangEmployee);

        jmProductionOrders = new JMenu("Órdenes prod.");
        jmiProductionOrderAll = new JMenuItem("Órdenes prod. todas");
        jmiProductionOrderNew = new JMenuItem("Órdenes prod. nuevas");
        jmiProductionOrderLot = new JMenuItem("Órdenes prod. en pesado");
        jmiProductionOrderLotAssigned = new JMenuItem("Órdenes prod. en piso");
        jmiProductionOrderProcess = new JMenuItem("Órdenes prod. en proceso");
        jmiProductionOrderEnd = new JMenuItem("Órdenes prod. terminadas");
        jmiProductionOrderClose = new JMenuItem("Órdenes prod. cerradas");
        jmiProductionOrderDelay = new JMenuItem("Órdenes prod. retrasadas");
        jmiProductionOrderForecast = new JMenuItem("Órdenes prod. pronósticos");
        jmiProductionOrderPerformance = new JMenuItem("Órdenes prod. rendimiento");
        jmiProductionOrderFatherSon = new JMenuItem("Órdenes prod. padre vs. hijo(s)");

        jmProductionOrders.add(jmiProductionOrderAll);
        jmProductionOrders.add(jmiProductionOrderNew);
        jmProductionOrders.add(jmiProductionOrderLot);
        jmProductionOrders.add(jmiProductionOrderLotAssigned);
        jmProductionOrders.add(jmiProductionOrderProcess);
        jmProductionOrders.add(jmiProductionOrderEnd);
        jmProductionOrders.add(jmiProductionOrderClose);
        jmProductionOrders.add(new JSeparator());
        jmProductionOrders.add(jmiProductionOrderDelay);
        jmProductionOrders.add(new JSeparator());
        jmProductionOrders.add(jmiProductionOrderForecast);
        jmProductionOrders.add(new JSeparator());
        jmProductionOrders.add(jmiProductionOrderPerformance);
        jmProductionOrders.add(new JSeparator());
        jmProductionOrders.add(jmiProductionOrderFatherSon);

        jmExplotionMaterials = new JMenu("Explosiones materiales");
        jmiExplotionMaterials = new JMenuItem("Docs. explosiones de materiales");
        jmiExplotionMaterialsProductionOrder = new JMenuItem("Docs. explosiones de materiales vs. órdenes prod.");
        jmiExplotionMaterialsDialog = new JMenuItem("Explosión de materiales...");
        jmiExplotionMaterialsForecast = new JMenuItem("Docs. explosiones de materiales de pronósticos");
        jmiExplotionMaterialsForecastProductionOrder = new JMenuItem("Docs. explosiones de materiales vs. órdenes prod. de pronósticos");
        jmiExplotionMaterialsForecastDialog = new JMenuItem("Explosión de materiales de pronósticos...");

        jmExplotionMaterials.add(jmiExplotionMaterials);
        jmExplotionMaterials.add(jmiExplotionMaterialsProductionOrder);
        jmExplotionMaterials.add(jmiExplotionMaterialsDialog);
        jmExplotionMaterials.add(new JSeparator());
        jmExplotionMaterials.add(jmiExplotionMaterialsForecast);
        jmExplotionMaterials.add(jmiExplotionMaterialsForecastProductionOrder);
        jmExplotionMaterials.add(jmiExplotionMaterialsForecastDialog);

        jmAssignLots = new JMenu("Producto/materia prima");
        jmiAssingLotsFinishedGood = new JMenuItem("Asignación de lotes producto");
        jmiDocumentsRequeriments = new JMenuItem("Docs. requisiciones de materiales");
        jmiDocumentsPurchasesOrder = new JMenuItem("Docs. pedidos de compras");

        jmAssignLots.add(jmiAssingLotsFinishedGood);
        jmAssignLots.add(new JSeparator());
        jmAssignLots.add(jmiDocumentsRequeriments);
        jmAssignLots.add(jmiDocumentsPurchasesOrder);

        jmManufacturingCost = new JMenu("Control horas trabajadas");
        jmiManufacturingCost = new JMenuItem("Docs. control horas trabajadas");
        jmiManufacturingClosePeriod = new JMenuItem("Docs. control horas trabajadas cerrados");
        jmiManufacturingCostEmployees =  new JMenuItem("Resumen docs. control horas trabajadas por empleado");

        jmManufacturingCost.add(jmiManufacturingCost);
        jmManufacturingCost.add(jmiManufacturingClosePeriod);
        jmManufacturingCost.add(new JSeparator());
        jmManufacturingCost.add(jmiManufacturingCostEmployees);

        jmMaintenance = new JMenu("Mantenimiento");
        jmiMaintenanceWorkOrder = new JMenuItem("Órdenes de trabajo");
        jmiMaintenanceEquipment = new JMenuItem("Lista de equipos");
        jmiStatiticts = new JMenuItem("Estadísticas");
        jmMaintenanceReport = new JMenu("Reportes");
        jmiMaintenanceReportGlobal = new JMenuItem("Reporte de paro global");
        jmiMaintenanceReportByEquipment = new JMenuItem("Reporte por equipo");
        jmiMaintenanceReportByArea = new JMenuItem("Reporte por área");

        jmMaintenanceReport.add(jmiMaintenanceReportGlobal);
        jmMaintenanceReport.add(jmiMaintenanceReportByEquipment);
        jmMaintenanceReport.add(jmiMaintenanceReportByArea);
        jmMaintenance.add(jmiMaintenanceWorkOrder);
        jmMaintenance.add(jmiMaintenanceEquipment);
        jmMaintenance.add(jmiStatiticts);
        jmMaintenance.add(jmMaintenanceReport);

        // Disabled maintenance options, module for next version:

        jmMaintenance.setEnabled(false);

        jmReportsStatiticsManufacturing = new JMenu("Estadísticas prod.");
        jmiReportProductionByItem = new JMenuItem("Producción por ítem");
        jmiReportProductionByItemGeneric = new JMenuItem("Producción por ítem genérico");
        jmiReportProductionByBizPartnetItem = new JMenuItem("Producción por operador-ítem");
        jmiReportProductionByItemBizPartner = new JMenuItem("Producción por ítem-operador");

        jmReports = new JMenu("Reportes");
        jmiReportProductionOrderProgramMonitoring = new JMenuItem("Órdenes prod. seguimiento programa");
        jmiReportBomItems = new JMenuItem("Productos por insumo");
        jmiReportProductionOrderPerformance = new JMenuItem("Rendimiento de órdenes prod.");

        jmReportsStatiticsManufacturing.add(jmiReportProductionByItem);
        jmReportsStatiticsManufacturing.add(jmiReportProductionByItemGeneric);
        jmReportsStatiticsManufacturing.add(jmiReportProductionByBizPartnetItem);
        jmReportsStatiticsManufacturing.add(jmiReportProductionByItemBizPartner);

        jmReports.add(jmReportsStatiticsManufacturing);
        jmReports.add(new JSeparator());
        jmReports.add(jmiReportBomItems);
        jmReports.add(new JSeparator());
        jmReports.add(jmiReportProductionOrderPerformance);

        jmiBom.addActionListener(this);
        jmiManufacturingLine.addActionListener(this);
        jmiManufacturingLineConfigItem.addActionListener(this);
        jmiExplotionMaterials.addActionListener(this);
        jmiExplotionMaterialsDialog.addActionListener(this);
        jmiExplotionMaterialsProductionOrder.addActionListener(this);
        jmiExplotionMaterialsForecast.addActionListener(this);
        jmiExplotionMaterialsForecastDialog.addActionListener(this);
        jmiExplotionMaterialsForecastProductionOrder.addActionListener(this);
        jmiAssingLotsFinishedGood.addActionListener(this);
        jmiDocumentsRequeriments.addActionListener(this);
        jmiDocumentsPurchasesOrder.addActionListener(this);
        jmiLeadtimeCo.addActionListener(this);
        jmiLeadtimeCob.addActionListener(this);
        jmiGang.addActionListener(this);
        jmiGangEmployee.addActionListener(this);
        jmiProductionOrderAll.addActionListener(this);
        jmiProductionOrderNew.addActionListener(this);
        jmiProductionOrderLot.addActionListener(this);
        jmiProductionOrderLotAssigned.addActionListener(this);
        jmiProductionOrderProcess.addActionListener(this);
        jmiProductionOrderEnd.addActionListener(this);
        jmiProductionOrderClose.addActionListener(this);
        jmiProductionOrderDelay.addActionListener(this);
        jmiProductionOrderForecast.addActionListener(this);
        jmiProductionOrderPerformance.addActionListener(this);
        jmiProductionOrderFatherSon.addActionListener(this);
        jmiManufacturingCost.addActionListener(this);
        jmiManufacturingCostEmployees.addActionListener(this);
        jmiManufacturingClosePeriod.addActionListener(this);
        jmiReportProductionOrderProgramMonitoring.addActionListener(this);
        jmiReportBomItems.addActionListener(this);
        jmiReportProductionOrderPerformance.addActionListener(this);
        jmiReportProductionByItem.addActionListener(this);
        jmiReportProductionByItemGeneric.addActionListener(this);
        jmiReportProductionByBizPartnetItem.addActionListener(this);
        jmiReportProductionByItemBizPartner.addActionListener(this);

        moDialogExplotionMaterials = new SDialogExplotionMaterials(miClient);
        moDialogRepProductionOrderPerformance = new SDialogRepProductionOrderPerformance(miClient);
        moDialogProductionOrderSaved = new SDialogProductionOrderSaved(miClient);

        hasRightBom = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_BOM).HasRight;
        hasRightExplotionMaterials = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_EXP).HasRight;
        hasRightAssignLot = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ASG_LOT).HasRight;
        hasRightReports = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_REP).HasRight;

        jmiBom.setVisible(hasRightBom);
        jmiManufacturingLine.setVisible(hasRightBom);
        jmiManufacturingLineConfigItem.setVisible(hasRightBom);
        jmiExplotionMaterialsDialog.setEnabled(hasRightExplotionMaterials);
        jmiAssingLotsFinishedGood.setEnabled(hasRightAssignLot);
        jmReports.setEnabled(hasRightReports);

        jmiProductionOrderNew.setVisible(!(Boolean)SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_NEW }, SLibConstants.FIELD_DEL));
        jmiProductionOrderLot.setVisible(!(Boolean)SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_LOT }, SLibConstants.FIELD_DEL));
        jmiProductionOrderLotAssigned.setVisible(!(Boolean)SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG }, SLibConstants.FIELD_DEL));
        jmiProductionOrderProcess.setVisible(!(Boolean)SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_PROC }, SLibConstants.FIELD_DEL));
        jmiProductionOrderEnd.setVisible(!(Boolean)SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_END }, SLibConstants.FIELD_DEL));
        jmiProductionOrderClose.setVisible(!(Boolean)SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_CLS }, SLibConstants.FIELD_DEL));
        
        // GUI configuration:
        
        if (((erp.SClient) miClient).getCfgProcesor() != null) {
            SCfgModule module = new SCfgModule("" + mnModuleType);
            SCfgMenu menu = null;
            
            menu = new SCfgMenu(jmManufacturingCost, "" + SDataConstants.MOD_MFG_HRS);
            module.getChildMenus().add(menu);
            
            ((erp.SClient) miClient).getCfgProcesor().processModule(module);
        }
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;
        SDataProductionOrder oProductionOrder = null;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.MFGU_LINE:
                    if (moFormManufacturingLine == null) {
                        moFormManufacturingLine = new SFormManufacturingLine(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataManufacturingLine();
                    }
                    miForm = moFormManufacturingLine;
                    break;
                case SDataConstants.MFGU_LINE_CFG_ITEM:
                    if (moFormManufacturingLineConfigItem == null) {
                        moFormManufacturingLineConfigItem = new SFormManufacturingLineConfigItem(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataManufacturingLineConfigItem();
                    }
                    miForm = moFormManufacturingLineConfigItem;
                    break;
                case SDataConstants.MFG_BOM:
                    if (moFormBom == null) {
                        moFormBom = new SFormBom(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBom();
                        ((SDataBom) moRegistry).setDbmsAuxSortingItem(miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());
                    }
                    miForm = moFormBom;
                    break;
                case SDataConstants.MFG_ORD:
                    if (moFormProductionOrder == null) {
                        moFormProductionOrder = new SFormProductionOrder(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataProductionOrder();
                    }
                    miForm = moFormProductionOrder;
                    break;

                case SDataConstants.MFGX_LT:
                    if (moFormLeadtime == null) {
                        moFormLeadtime = new SFormLeadtime(miClient);
                        moFormLeadtime.setValue(1,  auxType);
                    }
                    if (pk != null) {
                        if (auxType == SDataConstants.MFG_LT_CO) {
                            moRegistry = new SDataLeadtimeCo();
                            ((SDataLeadtimeCo) moRegistry).setDbmsAuxSortingItem(miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());
                        }
                        else {
                            moRegistry = new SDataLeadtimeCob();
                            ((SDataLeadtimeCob) moRegistry).setDbmsAuxSortingItem(miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());
                        }
                    }
                    moFormLeadtime.setValue(1, auxType);
                    miForm = moFormLeadtime;
                    break;

                case SDataConstants.MFGU_GANG:
                    if (moFormGang == null) {
                        moFormGang = new SFormGang(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataGang();
                    }
                    miForm = moFormGang;
                    break;

                case SDataConstants.MFG_EXP:
                    if (moFormExplotionMaterials == null) {
                        moFormExplotionMaterials = new SFormExplotionMaterials(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataExplotionMaterials();
                        ((SDataExplotionMaterials) moRegistry).setDbmsAuxSortingItem(miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());
                    }
                    miForm = moFormExplotionMaterials;
                    break;
                /*case SDataConstants.MFG_EXP_ETY:
                    if (moFormBomExp == null) {
                        moFormBomExp = new SDialogExplotionMaterials(miClient);
                    }
                    miForm = moFormBomExp;
                    break;*/
                case SDataConstants.MFG_REQ:
                    if (moFormRequisition == null) {
                        moFormRequisition = new SFormRequisition(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataRequisition();
                        ((SDataRequisition) moRegistry).setDbmsAuxSortingItem(miClient.getSessionXXX().getParamsErp().getFkSortingItemTypeId());
                    }
                    miForm = moFormRequisition;
                    break;
                case SDataConstants.MFG_ORD_CHG:
                    if (moFormProductionOrderCharges == null) {
                        moFormProductionOrderCharges = new SFormProductionOrderCharge(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataProductionOrderCharge();
                    }
                    miForm = moFormProductionOrderCharges;
                    break;
                case SDataConstants.MFG_COST:
                    if (moFormCost == null) {
                        moFormCost = new SFormCost(miClient);
                    }

                    if (pk != null) {
                        moRegistry = new SDataCost();
                    }
                    miForm = moFormCost;
                    break;

                case SDataConstants.MFGX_COST_CLS_PER:
                    if (moDialogMfgClosePeriod == null) {
                        moDialogMfgClosePeriod = new SDialogMfgClosePeriod(miClient);
                    }

                    if (pk != null) {
                        moRegistry = new SDataCostClosePeriod();
                    }

                    miForm = moDialogMfgClosePeriod;
                    break;

                default:
                    throw new Exception("¡La forma no existe!");
            }

            result = processForm(pk, isCopy);

            if (result == SLibConstants.DB_ACTION_SAVE_OK) {
                switch (formType) {
                    case SDataConstants.MFG_ORD:
                        oProductionOrder = (SDataProductionOrder) SDataUtilities.readRegistry(miClient, SDataConstants.MFG_ORD, getLastSavedPrimaryKey(), SLibConstants.EXEC_MODE_VERBOSE);
                        if (oProductionOrder.getUserNewTs().getTime() == oProductionOrder.getUserEditTs().getTime()) {
                            moDialogProductionOrderSaved.formReset();
                            moDialogProductionOrderSaved.setFormParams(oProductionOrder);
                            moDialogProductionOrderSaved.setVisible(true);
                        }
                        break;
                    default:
                }
            }

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
        java.lang.String sViewTitle = "";
        java.lang.Class oViewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {
                case SDataConstants.MFGU_LINE:
                    oViewClass = erp.mmfg.view.SViewManufacturingLine.class;
                    sViewTitle = "Líneas producción";
                    break;
                case SDataConstants.MFGU_LINE_CFG_ITEM:
                    oViewClass = erp.mmfg.view.SViewManufacturingLineConfigItem.class;
                    sViewTitle = "Config líneas producción";
                    break;
                case SDataConstants.MFG_BOM:
                    oViewClass = erp.mmfg.view.SViewBom.class;
                    sViewTitle = "Prod. fórmulas";
                    break;
                case SDataConstants.MFG_ORD:
                    switch (auxType01) {
                        case SDataConstantsSys.MFGS_ST_ORD_NEW:
                            sViewTitle = "Órdenes prod. nuevas";
                            break;
                        case SDataConstantsSys.MFGS_ST_ORD_LOT:

                            if (auxType02 == SDataConstants.MFGX_ORD_LOT_RM) {
                                sViewTitle = "Asignación lotes insumos";
                            }
                            else {
                                sViewTitle = "Órdenes prod. en pesado";
                            }

                            break;
                        case SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG:
                            sViewTitle = "Órdenes prod. en piso";
                            break;
                        case SDataConstantsSys.MFGS_ST_ORD_PROC:
                            if (auxType02 == SDataConstants.MFGX_ORD_LOT_FG) {
                                sViewTitle = "Asignación lotes producto";
                            }
                            else {
                                sViewTitle = "Órdenes prod. en proceso";
                            }
                            break;
                        case SDataConstantsSys.MFGS_ST_ORD_END:
                            sViewTitle = "Órdenes prod. terminadas";
                            break;
                        case SDataConstantsSys.MFGS_ST_ORD_CLS:
                            sViewTitle = "Órdenes prod. cerradas";
                            break;
                        case SDataConstantsSys.MFGS_ST_ORD_DLY:
                            sViewTitle = "Órdenes prod. retrasadas";
                            break;
                        case SDataConstants.MFGX_ORD_FOR:
                            sViewTitle = "Órdenes prod. pronósticos";
                            break;
                        default:
                            sViewTitle = "Órdenes prod. todas";
                    }
                    oViewClass = erp.mmfg.view.SViewProductionOrder.class;
                    break;
                case SDataConstants.MFGX_ORD_PERF:
                    sViewTitle = "Órdenes prod. índice rendimiento";
                    oViewClass = erp.mmfg.view.SViewProductionOrderPerformance.class;
                    break;
                case SDataConstants.MFGX_ORD_FAT_SON:
                    sViewTitle = "Prod. ord. padre vs. hijo(s)";
                    oViewClass = erp.mmfg.view.SViewProductionOrderFatherSon.class;
                    break;
                case SDataConstants.MFGX_LT:
                    switch (auxType01) {
                        case SDataConstants.MFG_LT_CO:
                            sViewTitle = "Prod. leadtime empresa";
                            break;
                        case SDataConstants.MFG_LT_COB:
                            sViewTitle = "Prod. leadtime sucursal";
                            break;
                    }
                    oViewClass = erp.mmfg.view.SViewLeadtime.class;
                    break;

                case SDataConstants.MFGU_GANG:
                    sViewTitle = "Prod. cuadrillas";
                    oViewClass = erp.mmfg.view.SViewGang.class;
                    break;

                case SDataConstants.MFGX_GANG_EMP:
                    sViewTitle = "Prod. cuadrillas vs. emp. (resumen)";
                    oViewClass = erp.mmfg.view.SViewGangEmployees.class;
                    break;

                case SDataConstants.MFGX_BOM_ITEMS:
                    sViewTitle = "Productos por insumo";
                    oViewClass = erp.mmfg.view.SViewBomItems.class;
                    break;

                case SDataConstants.MFG_EXP:
                    if (auxType01 == SDataConstants.MFGX_EXP_FOR) {
                        sViewTitle = "Prod. docs. explosión materiales pronósticos";
                    }
                    else {
                        sViewTitle = "Prod. docs. explosión materiales";
                    }
                    oViewClass = erp.mmfg.view.SViewExplotionMaterials.class;
                    break;

                case SDataConstants.MFG_EXP_ORD:
                    if (auxType01 == SDataConstants.MFGX_EXP_FOR) {
                        sViewTitle = "Prod. docs. exp. mats. vs. órds. prod. pronósticos";
                    }
                    else {
                        sViewTitle = "Prod. docs. exp. mats. vs. órds. prod.";
                    }
                    oViewClass = erp.mmfg.view.SViewExplotionMaterialsProductionOrder.class;
                    break;

                case SDataConstants.MFG_REQ:
                    sViewTitle = "Prod. docs. requisiciones mats.";
                    oViewClass = erp.mmfg.view.SViewRequisition.class;
                    break;

                case SDataConstants.TRN_DPS:
                    oViewClass = erp.mtrn.view.SViewDps.class;
                    switch (auxType02) {
                        case SDataConstantsSys.TRNX_TP_DPS_ORD:
                            sViewTitle = "Compras pedidos";
                            break;
                        default:
                    }
                    break;

                case SDataConstants.MFG_COST:
                    oViewClass = erp.mmfg.view.SViewCost.class;

                    switch (auxType01) {
                        case SDataConstants.MFGX_COST_CLS_PER:
                            sViewTitle = "Ctrl. hrs. trabajadas docs. cerrados";
                            break;
                        case SDataConstants.MFGX_COST_EMP:
                            sViewTitle = "Ctrl. hrs. trabajadas resumen por empleado";
                            break;
                        default:
                            sViewTitle = "Ctrl. hrs. trabajadas docs.";
                            break;
                    }

                    break;

                case SDataConstantsSys.REP_MFG_PROG_MON:
                    oViewClass = erp.mmfg.view.SViewProductionOrderProgramMonitoring.class;
                    sViewTitle = "Órdenes prod. seguimiento prog.";

                    break;

                case SDataConstants.MFGX_PROD:
                    oViewClass = erp.mmfg.view.SViewProductionByPeriod.class;
                    switch (auxType01) {
                        case SDataConstants.MFGX_PROD_BY_ITM:
                            sViewTitle = "Producción x ítem";
                            break;
                        case SDataConstants.MFGX_PROD_BY_IGEN:
                            sViewTitle = "Productión x ítem genérico";
                            break;
                        case SDataConstants.MFGX_PROD_BY_BIZ_ITM:
                            sViewTitle = "Producción x operador-ítem";
                            break;
                        case SDataConstants.MFGX_PROD_BY_ITM_BIZ:
                            sViewTitle = "Producción x ítem-operador";
                            break;
                    }
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
                case SDataConstants.MFG_BOM:
                    picker = moPickerBom = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBom);
                    break;
                case SDataConstants.MFG_BOM_SUB:
                    picker = moPickerBomSubstitute = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBom);
                    break;
                case SDataConstants.MFG_ORD:
                case SDataConstants.MFGX_ORD_ALL:
                    picker = moPickerOrd = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerOrd);
                    break;
                case SDataConstants.MFG_LINE:
                    picker = moPickerLine = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerLine);
                    break;
                case SDataConstants.MFGU_LINE:
                    picker = moPickerManufacturingLine = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerManufacturingLine);
                    break;
                case SDataConstants.MFGX_ORD:
                    picker = moPickerOrdPP = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerOrdPP);
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
        return new JMenu[] { jmCatalogs, jmProductionOrders, jmExplotionMaterials, jmAssignLots, jmManufacturingCost, jmReports };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
       javax.swing.JMenu[] menues = null;

        switch (moduleType) {
            case SDataConstants.MOD_MFG:
                menues = new JMenu[] { jmCatalogs, jmProductionOrders, jmExplotionMaterials, jmAssignLots, jmManufacturingCost, jmReports };
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

            if (item == jmiBom) {
                showView(SDataConstants.MFG_BOM);
            }
            else if (item == jmiManufacturingLine) {
                showView(SDataConstants.MFGU_LINE);
            }
            else if (item == jmiManufacturingLineConfigItem) {
                showView(SDataConstants.MFGU_LINE_CFG_ITEM);
            }
            else if (item == jmiExplotionMaterialsDialog) {
                moDialogExplotionMaterials.setIsForecast(false);
                moDialogExplotionMaterials.formRefreshCatalogues();
                moDialogExplotionMaterials.formReset();
                moDialogExplotionMaterials.setFormVisible(true);
            }
            else if (item == jmiExplotionMaterialsForecastDialog) {
                moDialogExplotionMaterials.setIsForecast(true);
                moDialogExplotionMaterials.formRefreshCatalogues();
                moDialogExplotionMaterials.formReset();
                moDialogExplotionMaterials.setFormVisible(true);
            }
            else if (item == jmiLeadtimeCo) {
                showView(SDataConstants.MFGX_LT, SDataConstants.MFG_LT_CO);
            }
            else if (item == jmiLeadtimeCob) {
                showView(SDataConstants.MFGX_LT, SDataConstants.MFG_LT_COB);
            }
            else if (item == jmiGang) {
                showView(SDataConstants.MFGU_GANG);
            }
            else if (item == jmiGangEmployee) {
                showView(SDataConstants.MFGX_GANG_EMP);
            }
            else if (item == jmiProductionOrderAll) {
                showView(SDataConstants.MFG_ORD, SDataConstants.MFGX_ORD_ALL);
            }
            else if (item == jmiProductionOrderNew) {
                showView(SDataConstants.MFG_ORD, SDataConstantsSys.MFGS_ST_ORD_NEW);
            }
            else if (item == jmiProductionOrderLot) {
                showView(SDataConstants.MFG_ORD, SDataConstantsSys.MFGS_ST_ORD_LOT);
            }
            else if (item == jmiProductionOrderLotAssigned) {
                showView(SDataConstants.MFG_ORD, SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG);
            }
            else if (item == jmiProductionOrderProcess) {
                showView(SDataConstants.MFG_ORD, SDataConstantsSys.MFGS_ST_ORD_PROC);
            }
            else if (item == jmiProductionOrderEnd) {
                showView(SDataConstants.MFG_ORD, SDataConstantsSys.MFGS_ST_ORD_END);
            }
            else if (item == jmiProductionOrderClose) {
                showView(SDataConstants.MFG_ORD, SDataConstantsSys.MFGS_ST_ORD_CLS);
            }
            else if (item == jmiProductionOrderDelay) {
                showView(SDataConstants.MFG_ORD, SDataConstantsSys.MFGS_ST_ORD_DLY);
            }
            else if (item == jmiProductionOrderForecast) {
                showView(SDataConstants.MFG_ORD, SDataConstants.MFGX_ORD_FOR);
            }
            else if (item == jmiProductionOrderPerformance) {
                showView(SDataConstants.MFGX_ORD_PERF);
            }
            else if (item == jmiProductionOrderFatherSon) {
                showView(SDataConstants.MFGX_ORD_FAT_SON);
            }
            else if (item == jmiAssingLotsFinishedGood) {
                showView(SDataConstants.MFG_ORD, SDataConstantsSys.MFGS_ST_ORD_PROC, SDataConstants.MFGX_ORD_LOT_FG);
            }
            else if (item == jmiExplotionMaterials) {
                showView(SDataConstants.MFG_EXP, SDataConstantsSys.MFGS_ST_ORD_LOT);
            }
            else if (item == jmiExplotionMaterialsForecast) {
                showView(SDataConstants.MFG_EXP, SDataConstants.MFGX_EXP_FOR);
            }
            else if (item == jmiExplotionMaterialsProductionOrder) {
                showView(SDataConstants.MFG_EXP_ORD, SDataConstants.MFGX_ORD);
            }
            else if (item == jmiExplotionMaterialsForecastProductionOrder) {
                showView(SDataConstants.MFG_EXP_ORD, SDataConstants.MFGX_ORD_FOR);
            }
            else if (item == jmiDocumentsRequeriments) {
                showView(SDataConstants.MFG_REQ);
            }
            else if (item == jmiDocumentsPurchasesOrder) {
                showView(SDataConstants.TRN_DPS, SDataConstantsSys.TRNS_CT_DPS_PUR, SDataConstantsSys.TRNX_TP_DPS_ORD);
            }
            else if (item == jmiManufacturingCost) {
                showView(SDataConstants.MFG_COST, SDataConstants.MFG_COST);
            }
            else if (item == jmiManufacturingClosePeriod) {
                showView(SDataConstants.MFG_COST, SDataConstants.MFGX_COST_CLS_PER);
            }
            else if (item == jmiManufacturingCostEmployees) {
                //showView(SDataConstants.MFGX_COST_EMP);
                showView(SDataConstants.MFG_COST, SDataConstants.MFGX_COST_EMP);
            }
            else if (item == jmiReportProductionOrderProgramMonitoring) {
                showView(SDataConstantsSys.REP_MFG_PROG_MON);
            }
            else if (item == jmiReportBomItems) {
                showView(SDataConstants.MFGX_BOM_ITEMS);
            }
            else if (item == jmiReportProductionOrderPerformance) {
                moDialogRepProductionOrderPerformance.formReset();
                moDialogRepProductionOrderPerformance.setFormVisible(true);
            }
            else if (item == jmiReportProductionByItem) {
                showView(SDataConstants.MFGX_PROD, SDataConstants.MFGX_PROD_BY_ITM);
            }
            else if (item == jmiReportProductionByItemGeneric) {
                showView(SDataConstants.MFGX_PROD, SDataConstants.MFGX_PROD_BY_IGEN);
            }
            else if (item == jmiReportProductionByBizPartnetItem) {
                showView(SDataConstants.MFGX_PROD, SDataConstants.MFGX_PROD_BY_BIZ_ITM);
            }
            else if (item == jmiReportProductionByItemBizPartner) {
                showView(SDataConstants.MFGX_PROD, SDataConstants.MFGX_PROD_BY_ITM_BIZ);
            }
        }
    }
}
