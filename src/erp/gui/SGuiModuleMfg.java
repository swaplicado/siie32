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
import erp.mmfg.data.SDataBom;
import erp.mmfg.data.SDataCost;
import erp.mmfg.data.SDataCostClosePeriod;
import erp.mmfg.data.SDataExplotionMaterials;
import erp.mmfg.data.SDataGang;
import erp.mmfg.data.SDataLeadtimeCo;
import erp.mmfg.data.SDataLeadtimeCob;
import erp.mmfg.data.SDataManufacturingLine;
import erp.mmfg.data.SDataManufacturingLineConfigItem;
import erp.mmfg.data.SDataProductionOrder;
import erp.mmfg.data.SDataProductionOrderCharge;
import erp.mmfg.data.SDataRequisition;
import erp.mmfg.form.SDialogExplotionMaterials;
import erp.mmfg.form.SDialogMfgClosePeriod;
import erp.mmfg.form.SDialogProductionOrderSaved;
import erp.mmfg.form.SDialogRepProductionOrder;
import erp.mmfg.form.SFormBom;
import erp.mmfg.form.SFormCost;
import erp.mmfg.form.SFormExplotionMaterials;
import erp.mmfg.form.SFormGang;
import erp.mmfg.form.SFormLeadtime;
import erp.mmfg.form.SFormManufacturingLine;
import erp.mmfg.form.SFormManufacturingLineConfigItem;
import erp.mmfg.form.SFormProductionOrder;
import erp.mmfg.form.SFormProductionOrderCharge;
import erp.mmfg.form.SFormRequisition;
import erp.mod.SModConsts;
import erp.mod.fin.view.SViewCustomReportsExpenses;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;

/**
 *
 * @author Sergio Flores, César Orozco, Sergio Flores
 */
public class SGuiModuleMfg extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmCatalogs;
    private javax.swing.JMenuItem jmiCatBom;
    private javax.swing.JMenuItem jmiCatManufacturingLine;
    private javax.swing.JMenuItem jmiCatManufacturingLineConfig;
    private javax.swing.JMenuItem jmiCatLeadtimeCo;
    private javax.swing.JMenuItem jmiCatLeadtimeCob;
    private javax.swing.JMenuItem jmiCatGang;
    private javax.swing.JMenuItem jmiCatGangEmployee;
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
    private javax.swing.JMenuItem jmiProductionOrderProgramMonitoring;
    private javax.swing.JMenu jmExplotionMaterials;
    private javax.swing.JMenuItem jmiExplotionMaterials;
    private javax.swing.JMenuItem jmiExplotionMaterialsProductionOrder;
    private javax.swing.JMenuItem jmiExplotionMaterialsDialog;
    private javax.swing.JMenuItem jmiExplotionMaterialsForecast;
    private javax.swing.JMenuItem jmiExplotionMaterialsForecastProductionOrder;
    private javax.swing.JMenuItem jmiExplotionMaterialsForecastDialog;
    private javax.swing.JMenu jmAssignLotsAndRequirements;
    private javax.swing.JMenuItem jmiAssingLotsFinishedGood;
    private javax.swing.JMenuItem jmiRequeriments;
    private javax.swing.JMenuItem jmiRequerimentsPurchasesOrder;
    private javax.swing.JMenu jmManufacturingCost;
    private javax.swing.JMenuItem jmiManufacturingCost;
    private javax.swing.JMenuItem jmiManufacturingClosePeriod;
    private javax.swing.JMenuItem jmiManufacturingCostEmployees;
    /* 2025-03-05, Sergio Flores: Maintenance is not supported yet.
    private javax.swing.JMenu jmMaintenance;
    private javax.swing.JMenuItem jmiMaintenanceWorkOrder;
    private javax.swing.JMenuItem jmiMaintenanceEquipment;
    private javax.swing.JMenuItem jmiMaintenanceStatiticts;
    private javax.swing.JMenu jmMaintenanceReport;
    private javax.swing.JMenuItem jmiMaintenanceReportGlobal;
    private javax.swing.JMenuItem jmiMaintenanceReportByEquipment;
    private javax.swing.JMenuItem jmiMaintenanceReportByArea;
    */
    private javax.swing.JMenu jmQryCustReps;
    private javax.swing.JMenuItem jmiQryCustRepsExpenses;
    private javax.swing.JMenuItem jmiQryCustRepsExpensesMonths;
    private javax.swing.JMenu jmReports;
    private javax.swing.JMenu jmReportsStatiticsManufacturing;
    private javax.swing.JMenuItem jmiReportProductionByItem;
    private javax.swing.JMenuItem jmiReportProductionByItemGeneric;
    private javax.swing.JMenuItem jmiReportProductionByBizPartnetItem;
    private javax.swing.JMenuItem jmiReportProductionByItemBizPartner;
    private javax.swing.JMenuItem jmiReportBomItems;
    private javax.swing.JMenuItem jmiReportBomCost;
    private javax.swing.JMenuItem jmiReportProductionOrderPerformance;
    private javax.swing.JMenuItem jmiReportFinishedGoodsEfficiency;
    private javax.swing.JMenuItem jmiReportRawMaterialsEfficiency;

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
    private erp.mmfg.form.SDialogRepProductionOrder moDialogRepProductionOrderPerformance;
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
        jmCatalogs = new JMenu("Catálogos");
        jmiCatBom = new JMenuItem("Fórmulas");
        jmiCatManufacturingLine = new JMenuItem("Líneas de producción");
        jmiCatManufacturingLineConfig = new JMenuItem("Configuración de líneas de producción");
        jmiCatLeadtimeCo = new JMenuItem("Leadtime empresa");
        jmiCatLeadtimeCob = new JMenuItem("Leadtime sucursal");
        jmiCatGang = new JMenuItem("Cuadrillas");
        jmiCatGangEmployee = new JMenuItem("Cuadrillas vs. empleados (resumen)");

        jmCatalogs.add(jmiCatBom);
        jmCatalogs.add(new JSeparator());
        jmCatalogs.add(jmiCatManufacturingLine);
        jmCatalogs.add(jmiCatManufacturingLineConfig);
        jmCatalogs.add(new JSeparator());
        jmCatalogs.add(jmiCatLeadtimeCo);
        jmCatalogs.add(jmiCatLeadtimeCob);
        jmCatalogs.add(new JSeparator());
        jmCatalogs.add(jmiCatGang);
        jmCatalogs.add(jmiCatGangEmployee);

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
        jmiProductionOrderProgramMonitoring = new JMenuItem("Órdenes prod. seguimiento programa");

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
        JSeparator sepProductionOrderProgramMonitoring = new JSeparator();
        jmProductionOrders.add(sepProductionOrderProgramMonitoring);
        jmProductionOrders.add(jmiProductionOrderProgramMonitoring);

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

        jmAssignLotsAndRequirements = new JMenu("Producto/ materia prima");
        jmiAssingLotsFinishedGood = new JMenuItem("Asignación de lotes producto");
        jmiRequeriments = new JMenuItem("Docs. requisiciones de materiales");
        jmiRequerimentsPurchasesOrder = new JMenuItem("Docs. pedidos de compras");

        jmAssignLotsAndRequirements.add(jmiAssingLotsFinishedGood);
        jmAssignLotsAndRequirements.add(new JSeparator());
        jmAssignLotsAndRequirements.add(jmiRequeriments);
        jmAssignLotsAndRequirements.add(jmiRequerimentsPurchasesOrder);

        jmManufacturingCost = new JMenu("Control horas trabajadas");
        jmiManufacturingCost = new JMenuItem("Docs. control horas trabajadas");
        jmiManufacturingClosePeriod = new JMenuItem("Docs. control horas trabajadas cerrados");
        jmiManufacturingCostEmployees =  new JMenuItem("Resumen docs. control horas trabajadas por empleado");

        jmManufacturingCost.add(jmiManufacturingCost);
        jmManufacturingCost.add(jmiManufacturingClosePeriod);
        jmManufacturingCost.add(new JSeparator());
        jmManufacturingCost.add(jmiManufacturingCostEmployees);

        /* 2025-03-05, Sergio Flores: Maintenance not supported yet.
        jmMaintenance = new JMenu("Mantenimiento");
        jmiMaintenanceWorkOrder = new JMenuItem("Órdenes de trabajo");
        jmiMaintenanceEquipment = new JMenuItem("Lista de equipos");
        jmiMaintenanceStatiticts = new JMenuItem("Estadísticas");
        jmMaintenanceReport = new JMenu("Reportes");
        jmiMaintenanceReportGlobal = new JMenuItem("Reporte de paro global");
        jmiMaintenanceReportByEquipment = new JMenuItem("Reporte por equipo");
        jmiMaintenanceReportByArea = new JMenuItem("Reporte por área");

        jmMaintenance.add(jmiMaintenanceWorkOrder);
        jmMaintenance.add(jmiMaintenanceEquipment);
        jmMaintenance.add(jmiMaintenanceStatiticts);
        jmMaintenanceReport.add(jmiMaintenanceReportGlobal);
        jmMaintenanceReport.add(jmiMaintenanceReportByEquipment);
        jmMaintenanceReport.add(jmiMaintenanceReportByArea);
        jmMaintenance.add(jmMaintenanceReport);
        */
        
        jmQryCustReps = new JMenu("Consultas");
        jmiQryCustRepsExpenses = new JMenuItem("Gastos por período");
        jmiQryCustRepsExpensesMonths = new JMenuItem("Gastos mensuales por año");
        
        jmQryCustReps.add(jmiQryCustRepsExpenses);
        jmQryCustReps.add(jmiQryCustRepsExpensesMonths);

        jmReports = new JMenu("Reportes");
        jmReportsStatiticsManufacturing = new JMenu("Estadísticas prod.");
        jmiReportProductionByItem = new JMenuItem("Producción por ítem");
        jmiReportProductionByItemGeneric = new JMenuItem("Producción por ítem genérico");
        jmiReportProductionByBizPartnetItem = new JMenuItem("Producción por operador-ítem");
        jmiReportProductionByItemBizPartner = new JMenuItem("Producción por ítem-operador");
        jmiReportBomItems = new JMenuItem("Productos por insumo");
        jmiReportBomCost = new JMenuItem("Costo teórico de fórmulas");
        jmiReportProductionOrderPerformance = new JMenuItem("Rendimiento de órdenes prod.");
        jmiReportFinishedGoodsEfficiency = new JMenuItem("Eficiencia global de productos terminados");
        jmiReportRawMaterialsEfficiency = new JMenuItem("Eficiencia global de insumos");

        jmReportsStatiticsManufacturing.add(jmiReportProductionByItem);
        jmReportsStatiticsManufacturing.add(jmiReportProductionByItemGeneric);
        jmReportsStatiticsManufacturing.add(jmiReportProductionByBizPartnetItem);
        jmReportsStatiticsManufacturing.add(jmiReportProductionByItemBizPartner);

        jmReports.add(jmReportsStatiticsManufacturing);
        jmReports.add(new JSeparator());
        jmReports.add(jmiReportBomItems);
        jmReports.add(new JSeparator());
        jmReports.add(jmiReportBomCost);
        jmReports.add(new JSeparator());
        jmReports.add(jmiReportProductionOrderPerformance);
        jmReports.add(jmiReportFinishedGoodsEfficiency);
        jmReports.add(jmiReportRawMaterialsEfficiency);

        jmiCatBom.addActionListener(this);
        jmiCatManufacturingLine.addActionListener(this);
        jmiCatManufacturingLineConfig.addActionListener(this);
        jmiCatLeadtimeCo.addActionListener(this);
        jmiCatLeadtimeCob.addActionListener(this);
        jmiCatGang.addActionListener(this);
        jmiCatGangEmployee.addActionListener(this);
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
        jmiProductionOrderProgramMonitoring.addActionListener(this);
        jmiExplotionMaterials.addActionListener(this);
        jmiExplotionMaterialsProductionOrder.addActionListener(this);
        jmiExplotionMaterialsDialog.addActionListener(this);
        jmiExplotionMaterialsForecast.addActionListener(this);
        jmiExplotionMaterialsForecastProductionOrder.addActionListener(this);
        jmiExplotionMaterialsForecastDialog.addActionListener(this);
        jmiAssingLotsFinishedGood.addActionListener(this);
        jmiRequeriments.addActionListener(this);
        jmiRequerimentsPurchasesOrder.addActionListener(this);
        jmiManufacturingCost.addActionListener(this);
        jmiManufacturingClosePeriod.addActionListener(this);
        jmiManufacturingCostEmployees.addActionListener(this);
        jmiQryCustRepsExpenses.addActionListener(this);
        jmiQryCustRepsExpensesMonths.addActionListener(this);
        jmiReportProductionByItem.addActionListener(this);
        jmiReportProductionByItemGeneric.addActionListener(this);
        jmiReportProductionByBizPartnetItem.addActionListener(this);
        jmiReportProductionByItemBizPartner.addActionListener(this);
        jmiReportBomItems.addActionListener(this);
        jmiReportBomCost.addActionListener(this);
        jmiReportProductionOrderPerformance.addActionListener(this);
        jmiReportFinishedGoodsEfficiency.addActionListener(this);
        jmiReportRawMaterialsEfficiency.addActionListener(this);

        moDialogExplotionMaterials = new SDialogExplotionMaterials(miClient);        
        moDialogProductionOrderSaved = new SDialogProductionOrderSaved(miClient);

        // user rights for catalogs:

        boolean hasAccessMenuCatalogs = miClient.getSessionXXX().getUser().hasPrivilege(new int[] { SDataConstantsSys.PRV_MFG_MISC, SDataConstantsSys.PRV_MFG_BOM, SDataConstantsSys.PRV_MFG_LT });
        boolean hasRightBom = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_BOM).HasRight;
        boolean hasRightLeadtime = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_LT).HasRight;
        
        jmCatalogs.setEnabled(hasAccessMenuCatalogs);
        jmiCatBom.setEnabled(hasRightBom);
        jmiCatManufacturingLine.setEnabled(hasRightBom);
        jmiCatManufacturingLineConfig.setEnabled(hasRightBom);
        jmiCatLeadtimeCo.setEnabled(hasRightLeadtime);
        jmiCatLeadtimeCob.setEnabled(hasRightLeadtime);

        // user rights for MFG orders:

        int[] privilegesProductionOrders = {
            SDataConstantsSys.PRV_MFG_ORD_NEW, // used in view of MFG orders
            SDataConstantsSys.PRV_MFG_ASG_REW, // used in view of MFG orders
            SDataConstantsSys.PRV_MFG_ASG_DATE, // used in view of MFG orders
            SDataConstantsSys.PRV_MFG_ORD_REOPEN, // used in view of MFG orders
            SDataConstantsSys.PRV_MFG_ORD_ST_NEW,
            SDataConstantsSys.PRV_MFG_ORD_ST_LOT,
            SDataConstantsSys.PRV_MFG_ORD_ST_LOT_ASG,
            SDataConstantsSys.PRV_MFG_ORD_ST_PROC,
            SDataConstantsSys.PRV_MFG_ORD_ST_END,
            SDataConstantsSys.PRV_MFG_ORD_ST_CLS };
        
        boolean hasAccessMenuProductionOrders = miClient.getSessionXXX().getUser().hasPrivilege(privilegesProductionOrders);
        
        jmProductionOrders.setEnabled(hasAccessMenuProductionOrders);
        jmiProductionOrderNew.setEnabled(miClient.getSessionXXX().getUser().hasPrivilege(new int[] { SDataConstantsSys.PRV_MFG_ORD_ST_NEW, SDataConstantsSys.PRV_MFG_ORD_NEW }));
        jmiProductionOrderLot.setEnabled(miClient.getSessionXXX().getUser().hasPrivilege(new int[] { SDataConstantsSys.PRV_MFG_ORD_ST_LOT, SDataConstantsSys.PRV_MFG_ASG_LOT }));
        jmiProductionOrderLotAssigned.setEnabled(miClient.getSessionXXX().getUser().hasPrivilege(new int[] { SDataConstantsSys.PRV_MFG_ORD_ST_LOT_ASG, SDataConstantsSys.PRV_MFG_ASG_LOT }));
        jmiProductionOrderProcess.setEnabled(miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_MFG_ORD_ST_PROC));
        jmiProductionOrderEnd.setEnabled(miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_MFG_ORD_ST_END));
        jmiProductionOrderClose.setEnabled(miClient.getSessionXXX().getUser().hasPrivilege(new int[] { SDataConstantsSys.PRV_MFG_ORD_ST_CLS, SDataConstantsSys.PRV_MFG_ORD_REOPEN }));

        // hide not supported status of manufacturing orders:
        jmiProductionOrderNew.setVisible(!(Boolean) SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_NEW }, SLibConstants.FIELD_DELETED));
        jmiProductionOrderLot.setVisible(!(Boolean) SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_LOT }, SLibConstants.FIELD_DELETED));
        jmiProductionOrderLotAssigned.setVisible(!(Boolean) SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_LOT_ASIG }, SLibConstants.FIELD_DELETED));
        jmiProductionOrderProcess.setVisible(!(Boolean) SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_PROC }, SLibConstants.FIELD_DELETED));
        jmiProductionOrderEnd.setVisible(!(Boolean) SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_END }, SLibConstants.FIELD_DELETED));
        jmiProductionOrderClose.setVisible(!(Boolean) SDataReadDescriptions.getField(miClient, SDataConstants.MFGS_ST_ORD, new int[] { SDataConstantsSys.MFGS_ST_ORD_CLS }, SLibConstants.FIELD_DELETED));
        
        /* 2025-03-05, Sergio Flores: Program monitoring is not supported yet.
        sepProductionOrderProgramMonitoring.setVisible(false);
        jmiProductionOrderProgramMonitoring.setVisible(false);
        */
        
        // user rights for explotion of materials:

        boolean hasAccessMenuExplotionMaterials = miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_MFG_EXP);
        boolean hasRightExplotionMaterials = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_EXP).HasRight;
        
        jmExplotionMaterials.setEnabled(hasAccessMenuExplotionMaterials);
        jmiExplotionMaterialsDialog.setEnabled(hasRightExplotionMaterials);
        jmiExplotionMaterialsForecastDialog.setEnabled(hasRightExplotionMaterials);
        
        // user rights for assignment of lots and requirements:

        boolean hasAccessMenuAssignLots = miClient.getSessionXXX().getUser().hasPrivilege(new int[] { SDataConstantsSys.PRV_MFG_ASG_LOT, SDataConstantsSys.PRV_MFG_REQ });
        boolean hasRightAssignLots = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_ASG_LOT).HasRight;
        boolean hasRightRequirements = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_MFG_REQ).HasRight;
        
        jmAssignLotsAndRequirements.setEnabled(hasAccessMenuAssignLots);
        jmiAssingLotsFinishedGood.setEnabled(hasRightAssignLots);
        jmiRequeriments.setEnabled(hasRightRequirements);
        jmiRequerimentsPurchasesOrder.setEnabled(hasRightRequirements);
        
        // user rights for MFG costs:
        
        boolean hasAccessMenuManufacturingCost = miClient.getSessionXXX().getUser().hasPrivilege(new int[] { SDataConstantsSys.PRV_MFG_ORD_NEW });
        
        jmManufacturingCost.setEnabled(hasAccessMenuManufacturingCost);
        
        // user rights for queries:
        
        boolean hasAccessMenuQueries = miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_MFG_QRY);
        
        jmQryCustReps.setEnabled(hasAccessMenuQueries);
        
        // user rights for reports:
        
        boolean hasAccessMenuReports = miClient.getSessionXXX().getUser().hasPrivilege(SDataConstantsSys.PRV_MFG_REP);

        jmReports.setEnabled(hasAccessMenuReports);
        
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
                        if (auxType == SDataConstants.TRN_SUP_LT_CO) {
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
                        case SDataConstants.TRN_SUP_LT_CO:
                            sViewTitle = "Prod. leadtime empresa";
                            break;
                        case SDataConstants.TRN_SUP_LT_COB:
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
                case SDataConstants.MFGX_BOM_COST:
                    sViewTitle = "Costo teórico fórmulas";
                    oViewClass = erp.mmfg.view.SViewTheoricalCost.class;
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
        return new JMenu[] { jmCatalogs, jmProductionOrders, jmExplotionMaterials, jmAssignLotsAndRequirements, jmManufacturingCost, jmQryCustReps, jmReports };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
       javax.swing.JMenu[] menues = null;

        switch (moduleType) {
            case SDataConstants.MOD_MFG:
                menues = new JMenu[] { jmCatalogs, jmProductionOrders, jmExplotionMaterials, jmAssignLotsAndRequirements, jmManufacturingCost, jmQryCustReps, jmReports };
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

            if (item == jmiCatBom) {
                showView(SDataConstants.MFG_BOM);
            }
            else if (item == jmiCatManufacturingLine) {
                showView(SDataConstants.MFGU_LINE);
            }
            else if (item == jmiCatManufacturingLineConfig) {
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
            else if (item == jmiCatLeadtimeCo) {
                showView(SDataConstants.MFGX_LT, SDataConstants.TRN_SUP_LT_CO);
            }
            else if (item == jmiCatLeadtimeCob) {
                showView(SDataConstants.MFGX_LT, SDataConstants.TRN_SUP_LT_COB);
            }
            else if (item == jmiCatGang) {
                showView(SDataConstants.MFGU_GANG);
            }
            else if (item == jmiCatGangEmployee) {
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
            else if (item == jmiRequeriments) {
                showView(SDataConstants.MFG_REQ);
            }
            else if (item == jmiRequerimentsPurchasesOrder) {
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
            else if (item == jmiQryCustRepsExpenses) {
                miClient.getSession().showView(SModConsts.FINX_CUST_REPS_EXPS, SViewCustomReportsExpenses.SUBTYPE_PERIOD, null);
            }
            else if (item == jmiQryCustRepsExpensesMonths) {
                miClient.getSession().showView(SModConsts.FINX_CUST_REPS_EXPS, SViewCustomReportsExpenses.SUBTYPE_MONTHS, null);
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
            else if (item == jmiProductionOrderProgramMonitoring) {
                showView(SDataConstantsSys.REP_MFG_PROG_MON);
            }
            else if (item == jmiReportBomItems) {
                showView(SDataConstants.MFGX_BOM_ITEMS);
            }
            else if (item == jmiReportBomCost) {
                showView(SDataConstants.MFGX_BOM_COST);
            }
            else if (item == jmiReportProductionOrderPerformance) {
                moDialogRepProductionOrderPerformance = new SDialogRepProductionOrder(miClient, "Rendimiento de orden de producción", SDataConstantsSys.REP_MFG_ORD_PERFORMANCE);
                moDialogRepProductionOrderPerformance.formReset();
                moDialogRepProductionOrderPerformance.setFormVisible(true);
            }
            else if (item == jmiReportFinishedGoodsEfficiency) {
                moDialogRepProductionOrderPerformance = new SDialogRepProductionOrder(miClient, "Eficiencia global de productos terminados", SDataConstantsSys.REP_MFG_FINISHED_GOODS_EFFICIENCY);
                moDialogRepProductionOrderPerformance.formReset();
                moDialogRepProductionOrderPerformance.setFormVisible(true);
            }
            else if (item == jmiReportRawMaterialsEfficiency) {
                moDialogRepProductionOrderPerformance = new SDialogRepProductionOrder(miClient, "Eficiencia global de insumos", SDataConstantsSys.REP_MFG_RAW_MATERIALS_EFFICIENCY);
                moDialogRepProductionOrderPerformance.formReset();
                moDialogRepProductionOrderPerformance.setFormVisible(true);
            }
        }
    }
}
