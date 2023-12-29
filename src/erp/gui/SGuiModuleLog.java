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
import erp.mod.SModConsts;
import erp.mod.SModSysConsts;
import erp.mod.log.form.SDialogRepRate;
import erp.mod.trn.form.SDialogUuidSearch;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.lib.gui.SGuiClient;
import sa.lib.gui.SGuiParams;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class SGuiModuleLog extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmShipment;
    private javax.swing.JMenuItem jmiShipment;
    private javax.swing.JMenuItem jmiShipmentDetail;
    private javax.swing.JMenuItem jmiShipmentAuthornPending;
    private javax.swing.JMenuItem jmiShipmentAuthornPentingDetail;
    private javax.swing.JMenuItem jmiShipmentAuthorn;
    private javax.swing.JMenuItem jmiShipmentAuthornDetail;
    private javax.swing.JMenuItem jmiShipmentReject;
    private javax.swing.JMenuItem jmiShipmentRejectDetail;

    private javax.swing.JMenuItem jmiShipmentDelivery;
    private javax.swing.JMenuItem jmiShipmentDeliveryDetail;
    private javax.swing.JMenuItem jmiShipmentDeliveryPending;
    private javax.swing.JMenuItem jmiShipmentDeliveryPendingDetail;
    private javax.swing.JMenuItem jmiShipmentBill;
    private javax.swing.JMenuItem jmiShipmentBillPending;
    private javax.swing.JMenuItem jmiShipmentDpsSalesPending;
    private javax.swing.JMenuItem jmiShipmentDpsSales;
    private javax.swing.JMenuItem jmiShipmentBol;
    private javax.swing.JMenuItem jmiShipmentBolInvoice;
    private javax.swing.JMenuItem jmiUuidSearch;

    private javax.swing.JMenu jmShipmentSales;
    private javax.swing.JMenuItem jmiShipmentDps;
    private javax.swing.JMenuItem jmiShipmentDpsDetail;
    private javax.swing.JMenuItem jmiShipmentDpsPending;
    private javax.swing.JMenuItem jmiShipmentDpsPendingDetail;

    private javax.swing.JMenu jmShipmentAdjustment;
    private javax.swing.JMenuItem jmiShipmentDpsAdjustment;
    private javax.swing.JMenuItem jmiShipmentDpsAdjustmentDetail;
    private javax.swing.JMenuItem jmiShipmentDpsAdjustmentPending;
    private javax.swing.JMenuItem jmiShipmentDpsAdjustmentPendingDetail;

    private javax.swing.JMenu jmShipmentDiog;
    private javax.swing.JMenuItem jmiShipmentDiog;
    private javax.swing.JMenuItem jmiShipmentDiogDetail;
    private javax.swing.JMenuItem jmiShipmentDiogPending;
    private javax.swing.JMenuItem jmiShipmentDiogPendingDetail;

    private javax.swing.JMenu jmCatalogue;
    private javax.swing.JMenuItem jmiCatalogueSpot;
    private javax.swing.JMenuItem jmiCatalogueSpotCompanyBranch;
    private javax.swing.JMenuItem jmiCatalogueSpotCompanyBranchEntity;
    private javax.swing.JMenuItem jmiCatalogueVehicleType;
    private javax.swing.JMenuItem jmiCatalogueVehicle;
    private javax.swing.JMenuItem jmiCatalogueTrailer;
    private javax.swing.JMenuItem jmiCatalogueBolPerson;
    private javax.swing.JMenuItem jmiCatalogueInsurer;
    private javax.swing.JMenuItem jmiCatalogueRate;

    private javax.swing.JMenu jmReports;
    private javax.swing.JMenuItem jmiRepRate;

    private erp.form.SFormOptionPicker moPickerModeOfTransportationType;
    private erp.form.SFormOptionPicker moPickerCarrierType;
    private erp.form.SFormOptionPicker moPickerIncoterm;
    private erp.form.SFormOptionPicker moPickerVehicleType;
    private erp.form.SFormOptionPicker moPickerVehicle;

    public SGuiModuleLog(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_LOG);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightMisc;
        boolean hasRightReports;
        boolean hasRightRate;

        jmShipment = new JMenu("Embarques");
        jmiShipment = new JMenuItem("Embarques");
        jmiShipmentDetail = new JMenuItem("Embarques a detalle");
        jmiShipmentAuthornPending = new JMenuItem("Embarques por autorizar");
        jmiShipmentAuthornPentingDetail = new JMenuItem("Embarques por autorizar a detalle");
        jmiShipmentAuthorn = new JMenuItem("Embarques autorizados");
        jmiShipmentAuthornDetail = new JMenuItem("Embarques autorizados a detalle");
        jmiShipmentReject = new JMenuItem("Embarques rechazados");
        jmiShipmentRejectDetail = new JMenuItem("Embarques rechazados a detalle");

        jmShipmentSales = new JMenu("Embarques ventas");
        jmiShipmentDpsPending = new JMenuItem("Facturas de ventas por embarcar");
        jmiShipmentDpsPendingDetail = new JMenuItem("Facturas de ventas por embarcar a detalle");
        jmiShipmentDps = new JMenuItem("Facturas de ventas embarcadas");
        jmiShipmentDpsDetail = new JMenuItem("Facturas de ventas embarcadas a detalle");

        jmShipmentAdjustment = new JMenu("Embarques ajustes ventas");
        jmiShipmentDpsAdjustmentPending = new JMenuItem("Notas de crédito de ventas por embarcar");
        jmiShipmentDpsAdjustmentPendingDetail = new JMenuItem("Notas de crédito de ventas por embarcar a detalle");
        jmiShipmentDpsAdjustment = new JMenuItem("Notas de crédito de ventas embarcadas");
        jmiShipmentDpsAdjustmentDetail = new JMenuItem("Notas de crédito de ventas embarcadas a detalle");

        jmShipmentDiog = new JMenu("Embarques docs. inventarios");
        jmiShipmentDiogPending = new JMenuItem("Docs. inventarios por embarcar");
        jmiShipmentDiogPendingDetail = new JMenuItem("Docs. inventarios por embarcar a detalle");
        jmiShipmentDiog = new JMenuItem("Docs. inventarios embarcados");
        jmiShipmentDiogDetail = new JMenuItem("Docs. inventarios embarcados a detalle");

        jmiShipmentDeliveryPending = new JMenuItem("Embarques por entregar");
        jmiShipmentDeliveryPendingDetail = new JMenuItem("Embarques por entregar a detalle");
        jmiShipmentDelivery = new JMenuItem("Embarques entregados");
        jmiShipmentDeliveryDetail = new JMenuItem("Embarques entregados a detalle");
        jmiShipmentBillPending = new JMenuItem("Embarques por facturar");
        jmiShipmentBill = new JMenuItem("Embarques facturados");
        jmiShipmentDpsSalesPending = new JMenuItem("Embarques vs. facturas ventas por facturar");
        jmiShipmentDpsSales = new JMenuItem("Embarques vs. facturas ventas facturadas");
        jmiShipmentBol = new JMenuItem("Carta porte traslados");
        jmiShipmentBolInvoice = new JMenuItem("Carta porte facturas");
        jmiUuidSearch = new JMenuItem("Busqueda de CFDI por UUID...");

        jmCatalogue = new JMenu("Catálogos");
        jmiCatalogueSpot = new JMenuItem("Lugares");
        jmiCatalogueSpotCompanyBranch = new JMenuItem("Lugares de sucursales de la empresa");
        jmiCatalogueSpotCompanyBranchEntity = new JMenuItem("Lugares de entidades de sucursales");
        jmiCatalogueVehicleType = new JMenuItem("Tipos de vehículo");
        jmiCatalogueVehicle = new JMenuItem("Vehículos");
        jmiCatalogueTrailer = new JMenuItem("Remolques");
        jmiCatalogueBolPerson = new JMenuItem("Figuras de transporte");
        jmiCatalogueInsurer = new JMenuItem("Aseguradoras");
        jmiCatalogueRate = new JMenuItem("Tarifas");

        jmReports = new JMenu("Reportes");
        jmiRepRate = new JMenuItem("Listado de tarifas...");

        jmShipment.add(jmiShipment);
        jmShipment.add(jmiShipmentDetail);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentAuthornPending);
        jmShipment.add(jmiShipmentAuthornPentingDetail);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentAuthorn);
        jmShipment.add(jmiShipmentAuthornDetail);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentReject);
        jmShipment.add(jmiShipmentRejectDetail);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentDeliveryPending);
        jmShipment.add(jmiShipmentDeliveryPendingDetail);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentDelivery);
        jmShipment.add(jmiShipmentDeliveryDetail);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentBillPending);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentBill);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentDpsSalesPending);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentDpsSales);
        jmShipment.addSeparator();
        jmShipment.add(jmiShipmentBol);
        jmShipment.add(jmiShipmentBolInvoice);
        jmShipment.addSeparator();
        jmShipment.add(jmiUuidSearch);

        jmShipmentSales.add(jmiShipmentDpsPending);
        jmShipmentSales.add(jmiShipmentDpsPendingDetail);
        jmShipmentSales.addSeparator();
        jmShipmentSales.add(jmiShipmentDps);
        jmShipmentSales.add(jmiShipmentDpsDetail);

        jmShipmentAdjustment.add(jmiShipmentDpsAdjustmentPending);
        jmShipmentAdjustment.add(jmiShipmentDpsAdjustmentPendingDetail);
        jmShipmentAdjustment.addSeparator();
        jmShipmentAdjustment.add(jmiShipmentDpsAdjustment);
        jmShipmentAdjustment.add(jmiShipmentDpsAdjustmentDetail);

        jmShipmentDiog.add(jmiShipmentDiogPending);
        jmShipmentDiog.add(jmiShipmentDiogPendingDetail);
        jmShipmentDiog.addSeparator();
        jmShipmentDiog.add(jmiShipmentDiog);
        jmShipmentDiog.add(jmiShipmentDiogDetail);

        jmCatalogue.add(jmiCatalogueSpot);
        jmCatalogue.add(jmiCatalogueSpotCompanyBranch);
        jmCatalogue.add(jmiCatalogueSpotCompanyBranchEntity);
        jmCatalogue.add(jmiCatalogueVehicleType);
        jmCatalogue.add(jmiCatalogueVehicle);
        jmCatalogue.add(jmiCatalogueTrailer);
        jmCatalogue.add(jmiCatalogueBolPerson);
        jmCatalogue.add(jmiCatalogueInsurer);
        jmCatalogue.add(jmiCatalogueRate);
        
        jmReports.add(jmiRepRate);

        jmiShipment.addActionListener(this);
        jmiShipmentDetail.addActionListener(this);
        jmiShipmentAuthornPending.addActionListener(this);
        jmiShipmentAuthornPentingDetail.addActionListener(this);
        jmiShipmentAuthorn.addActionListener(this);
        jmiShipmentAuthornDetail.addActionListener(this);
        jmiShipmentReject.addActionListener(this);
        jmiShipmentRejectDetail.addActionListener(this);
        jmiShipmentDpsPending.addActionListener(this);
        jmiShipmentDpsPendingDetail.addActionListener(this);
        jmiShipmentDps.addActionListener(this);
        jmiShipmentDpsDetail.addActionListener(this);
        jmiShipmentDpsAdjustmentPending.addActionListener(this);
        jmiShipmentDpsAdjustmentPendingDetail.addActionListener(this);
        jmiShipmentDpsAdjustment.addActionListener(this);
        jmiShipmentDpsAdjustmentDetail.addActionListener(this);
        jmiShipmentDiogPending.addActionListener(this);
        jmiShipmentDiogPendingDetail.addActionListener(this);
        jmiShipmentDiog.addActionListener(this);
        jmiShipmentDiogDetail.addActionListener(this);
        jmiShipmentDeliveryPending.addActionListener(this);
        jmiShipmentDeliveryPendingDetail.addActionListener(this);
        jmiShipmentDelivery.addActionListener(this);
        jmiShipmentDeliveryDetail.addActionListener(this);
        jmiShipmentBillPending.addActionListener(this);
        jmiShipmentBill.addActionListener(this);
        jmiShipmentDpsSalesPending.addActionListener(this);
        jmiShipmentDpsSales.addActionListener(this);
        jmiShipmentBol.addActionListener(this);
        jmiShipmentBolInvoice.addActionListener(this);
        jmiUuidSearch.addActionListener(this);

        jmiCatalogueSpot.addActionListener(this);
        jmiCatalogueSpotCompanyBranch.addActionListener(this);
        jmiCatalogueSpotCompanyBranchEntity.addActionListener(this);
        jmiCatalogueVehicleType.addActionListener(this);
        jmiCatalogueVehicle.addActionListener(this);
        jmiCatalogueTrailer.addActionListener(this);
        jmiCatalogueBolPerson.addActionListener(this);
        jmiCatalogueInsurer.addActionListener(this);
        jmiCatalogueRate.addActionListener(this);

        jmiRepRate.addActionListener(this);
        
        moPickerModeOfTransportationType = null;
        moPickerCarrierType = null;
        moPickerIncoterm = null;
        moPickerVehicle = null;
        moPickerVehicleType = null;

        hasRightMisc = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_LOG_MISC).HasRight;
        hasRightRate = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_LOG_RATE).HasRight;
        hasRightReports = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_LOG_REP).HasRight;

        jmCatalogue.setEnabled(hasRightMisc || hasRightRate);
        jmShipment.setEnabled(hasRightMisc);
        jmShipmentSales.setEnabled(hasRightMisc);
        jmShipmentAdjustment.setEnabled(hasRightMisc);
        jmShipmentDiog.setEnabled(hasRightMisc);
        jmReports.setEnabled(hasRightReports);
        
        jmiCatalogueSpot.setEnabled(hasRightMisc || hasRightRate);
        jmiCatalogueSpotCompanyBranch.setEnabled(hasRightMisc || hasRightRate);
        jmiCatalogueSpotCompanyBranchEntity.setEnabled(hasRightMisc || hasRightRate);
        jmiCatalogueVehicleType.setEnabled(hasRightMisc || hasRightRate);
        jmiCatalogueVehicle.setEnabled(hasRightMisc || hasRightRate);
        jmiCatalogueTrailer.setEnabled(hasRightMisc || hasRightRate);
        jmiCatalogueBolPerson.setEnabled(hasRightMisc || hasRightRate);
        jmiCatalogueInsurer.setEnabled(hasRightMisc || hasRightRate);
        jmiCatalogueRate.setEnabled(hasRightRate);
        
        jmiRepRate.setEnabled(hasRightRate);
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        throw new UnsupportedOperationException("Not supported yet.");
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
        throw new UnsupportedOperationException("Not supported yet.");
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
                case SModConsts.LOGS_TP_MOT:
                    picker = moPickerModeOfTransportationType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerModeOfTransportationType);
                    break;
                case SModConsts.LOGS_TP_CAR:
                    picker = moPickerCarrierType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerCarrierType);
                    break;
                case SModConsts.LOGS_INC:
                    picker = moPickerIncoterm = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerIncoterm);
                    break;
                case SModConsts.LOGU_TP_VEH:
                    picker = moPickerVehicleType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerVehicleType);
                    break;
                case SModConsts.LOG_VEH:
                    picker = moPickerVehicle = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerVehicle);
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
        return new JMenu[] { jmCatalogue, jmShipment, jmShipmentSales, jmShipmentAdjustment, jmShipmentDiog, jmReports };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiCatalogueSpot) {
                miClient.getSession().showView(SModConsts.LOGU_SPOT, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCatalogueSpotCompanyBranch) {
                miClient.getSession().showView(SModConsts.LOGU_SPOT_COB, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCatalogueSpotCompanyBranchEntity) {
                miClient.getSession().showView(SModConsts.LOGU_SPOT_COB_ENT, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCatalogueVehicleType) {
                miClient.getSession().showView(SModConsts.LOGU_TP_VEH, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCatalogueVehicle) {
                miClient.getSession().showView(SModConsts.LOG_VEH, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCatalogueTrailer) {
                miClient.getSession().showView(SModConsts.LOG_TRAILER, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCatalogueBolPerson) {
                miClient.getSession().showView(SModConsts.LOG_BOL_PERSON, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCatalogueInsurer) {
                miClient.getSession().showView(SModConsts.LOG_INSURER, SLibConstants.UNDEFINED, null);
            }
            else if (item == jmiCatalogueRate) {
                miClient.getSession().showView(SModConsts.LOG_RATE, SLibConstants.UNDEFINED, null);
            }

            else if (item == jmiShipment) {
                miClient.getSession().showView(SModConsts.LOG_SHIP, SLibConstants.UNDEFINED, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDetail) {
                miClient.getSession().showView(SModConsts.LOG_SHIP, SLibConstants.UNDEFINED, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentAuthornPending) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_AUTH, SModSysConsts.TRNS_ST_DPS_AUTHORN_PENDING, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentAuthornPentingDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_AUTH, SModSysConsts.TRNS_ST_DPS_AUTHORN_PENDING, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentAuthorn) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_AUTH, SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentAuthornDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_AUTH, SModSysConsts.TRNS_ST_DPS_AUTHORN_AUTHORN, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentReject) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_AUTH, SModSysConsts.TRNS_ST_DPS_AUTHORN_REJECT, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentRejectDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_AUTH, SModSysConsts.TRNS_ST_DPS_AUTHORN_REJECT, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentDpsPending) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDpsPendingDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentDps) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDpsDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentDpsAdjustmentPending) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS_ADJ, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDpsAdjustmentPendingDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS_ADJ, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentDpsAdjustment) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS_ADJ, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDpsAdjustmentDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS_ADJ, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentDiogPending) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DIOG, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDiogPendingDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DIOG, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentDiog) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DIOG, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDiogDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DIOG, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentDeliveryPending) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DLY, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDeliveryPendingDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DLY, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentDelivery) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DLY, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDeliveryDetail) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DLY, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_DET));
            }
            else if (item == jmiShipmentBillPending) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_BOL, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentBill) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_BOL, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDpsSalesPending) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS_SAL, SModConsts.VIEW_ST_PEND, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentDpsSales) {
                miClient.getSession().showView(SModConsts.LOGX_SHIP_DPS_SAL, SModConsts.VIEW_ST_DONE, new SGuiParams(SModConsts.VIEW_SC_SUM));
            }
            else if (item == jmiShipmentBol) {
                miClient.getSession().showView(SModConsts.LOG_BOL, SDataConstantsSys.TRNS_TP_CFD_BOL, null);
            }
            else if (item == jmiShipmentBolInvoice) {
                miClient.getSession().showView(SModConsts.LOG_BOL, SDataConstantsSys.TRNS_TP_CFD_INV, null);
            }
            else if (item == jmiUuidSearch) {
                new SDialogUuidSearch((SGuiClient) miClient, SDataConstantsSys.TRNS_TP_CFD_BOL, SLibConstants.UNDEFINED).setVisible(true);
            }
            else if (item == jmiRepRate) {
                new SDialogRepRate(miClient.getSession().getClient(), "Listado de tarifas").setVisible(true);
            }
        }
    }
}
