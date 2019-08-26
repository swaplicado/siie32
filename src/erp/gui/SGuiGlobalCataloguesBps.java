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
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerAddressee;
import erp.mbps.data.SDataBizPartnerBizAreaCatalogue;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mbps.data.SDataBizPartnerBranchContact;
import erp.mbps.data.SDataBizPartnerType;
import erp.mbps.form.SFormBizPartner;
import erp.mbps.form.SFormBizPartnerAddressee;
import erp.mbps.form.SFormBizPartnerAttribute;
import erp.mbps.form.SFormBizPartnerBizArea;
import erp.mbps.form.SFormBizPartnerBranchBankAccount;
import erp.mbps.form.SFormBizPartnerBranchContact;
import erp.mbps.form.SFormBizPartnerEmployee;
import erp.mbps.form.SFormBizPartnerSimple;
import erp.mbps.form.SFormBizPartnerType;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author Sergio Flores, Alfonso Flores, Sergio Flores
 */
public class SGuiGlobalCataloguesBps extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmMenuBizPartner;
    private javax.swing.JMenu jmMenuBranch;
    private javax.swing.JMenu jmMenuBranchAddress;
    private javax.swing.JMenu jmMenuBranchContact;
    private javax.swing.JMenu jmMenuBranchBankAccount;
    private javax.swing.JMenu jmMenuAddressee;
    private javax.swing.JMenuItem jmiBizPartner;
    private javax.swing.JMenuItem jmiBizPartnerCustomer;
    private javax.swing.JMenuItem jmiBizPartnerSupplier;
    private javax.swing.JMenuItem jmiBizPartnerDebtor;
    private javax.swing.JMenuItem jmiBizPartnerCreditor;
    private javax.swing.JMenuItem jmiBizPartnerAttBank;
    private javax.swing.JMenuItem jmiBizPartnerAttCarrier;
    private javax.swing.JMenuItem jmiBizPartnerAttEmployee;
    private javax.swing.JMenuItem jmiBizPartnerAttSalesAgent;
    private javax.swing.JMenuItem jmiBizPartnerCreditCustomer;
    private javax.swing.JMenuItem jmiBizPartnerCreditSupplier;
    private javax.swing.JMenuItem jmiBizPartnerBranch;
    private javax.swing.JMenuItem jmiBizPartnerBranchCustomer;
    private javax.swing.JMenuItem jmiBizPartnerBranchSupplier;
    private javax.swing.JMenuItem jmiBizPartnerBranchDebtor;
    private javax.swing.JMenuItem jmiBizPartnerBranchCreditor;
    private javax.swing.JMenuItem jmiBizPartnerBranchAddress;
    private javax.swing.JMenuItem jmiBizPartnerBranchAddressCustomer;
    private javax.swing.JMenuItem jmiBizPartnerBranchAddressSupplier;
    private javax.swing.JMenuItem jmiBizPartnerBranchAddressDebtor;
    private javax.swing.JMenuItem jmiBizPartnerBranchAddressCreditor;
    private javax.swing.JMenuItem jmiBizPartnerBranchAddressEmployee;
    private javax.swing.JMenuItem jmiBizPartnerBranchContact;
    private javax.swing.JMenuItem jmiBizPartnerBranchContactCustomer;
    private javax.swing.JMenuItem jmiBizPartnerBranchContactSupplier;
    private javax.swing.JMenuItem jmiBizPartnerBranchContactDebtor;
    private javax.swing.JMenuItem jmiBizPartnerBranchContactCreditor;
    private javax.swing.JMenuItem jmiBizPartnerBranchBankAccount;
    private javax.swing.JMenuItem jmiBizPartnerBranchBankAccountCustomer;
    private javax.swing.JMenuItem jmiBizPartnerBranchBankAccountSupplier;
    private javax.swing.JMenuItem jmiBizPartnerBranchBankAccountDebtor;
    private javax.swing.JMenuItem jmiBizPartnerBranchBankAccountCreditor;
    private javax.swing.JMenuItem jmiBizPartnerBranchBankAccountEmployee;
    private javax.swing.JMenuItem jmiBizPartnerAddresseeCustomer;
    private javax.swing.JMenuItem jmiBizPartnerType;
    private javax.swing.JMenuItem jmiBizPartnerBizArea;

    private erp.mbps.form.SFormBizPartner moFormBizPartner;
    private erp.mbps.form.SFormBizPartnerSimple moFormBizPartnerSimple;
    private erp.mbps.form.SFormBizPartnerAttribute moFormBizPartnerAttribute;
    private erp.mbps.form.SFormBizPartnerEmployee moFormBizPartnerEmployee;
    private erp.mbps.form.SFormBizPartnerBranchContact moFormBizPartnerBranchContact;
    private erp.mbps.form.SFormBizPartnerBranchBankAccount moFormBizPartnerBranchBankAccount;
    private erp.mbps.form.SFormBizPartnerAddressee moFormBizPartnerAddresseeCustomer;
    private erp.mbps.form.SFormBizPartnerType moFormBizPartnerType;
    private erp.mbps.form.SFormBizPartnerBizArea moFormBizPartnerBizArea;

    private erp.form.SFormOptionPicker moPickerBizPartnerIdentity;
    private erp.form.SFormOptionPicker moPickerBizPartnerType;
    private erp.form.SFormOptionPicker moPickerContactType;
    private erp.form.SFormOptionPicker moPickerBizPartnerCategory;
    private erp.form.SFormOptionPicker moPickerBizPartner;
    private erp.form.SFormOptionPicker moPickerBizPartnerSupplier;
    private erp.form.SFormOptionPicker moPickerBizPartnerCustomer;
    private erp.form.SFormOptionPicker moPickerBizPartnerCreditor;
    private erp.form.SFormOptionPicker moPickerBizPartnerDebtor;
    private erp.form.SFormOptionPicker moPickerBizPartnerEmployee;
    private erp.form.SFormOptionPicker moPickerBizPartnerExtraSupplierCustomer;
    private erp.form.SFormOptionPicker moPickerBizPartnerExtraCreditorDebtor;
    private erp.form.SFormOptionPicker moPickerBizPartnerAttBank;
    private erp.form.SFormOptionPicker moPickerBizPartnerAttCarrier;
    private erp.form.SFormOptionPicker moPickerBizPartnerSalesAgent;
    private erp.form.SFormOptionPicker moPickerBizPartnerEmployeeMfg;
    private erp.form.SFormOptionPicker moPickerBizPartnerBranch;
    private erp.form.SFormOptionPicker moPickerBizPartnerBranchAddress;
    private erp.form.SFormOptionPicker moPickerBankAccount;

    public SGuiGlobalCataloguesBps(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_CFG);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightBizPartner;
        boolean hasRightBizPartnerSupplier;
        boolean hasRightBizPartnerCustomer;
        boolean hasRightBizPartnerCreditor;
        boolean hasRightBizPartnerDebtor;
        boolean hasRightBizPartnerEmployee;
        boolean hasRightCreditPurchases;
        boolean hasRightCreditSales;
        boolean hasRightBizPartnerBranch;
        boolean hasRightBizPartnerBranchSupplier;
        boolean hasRightBizPartnerBranchCustomer;
        boolean hasRightBizPartnerBranchAddress;
        boolean hasRightBizPartnerBranchAddressSupplier;
        boolean hasRightBizPartnerBranchAddressCustomer;
        boolean hasRightBizPartnerBranchContact;
        boolean hasRightBizPartnerBranchContactSupplier;
        boolean hasRightBizPartnerBranchContactCustomer;
        boolean hasRightBizPartnerBranchBankAccount;
        boolean hasRightBizPartnerBranchBankAccountSupplier;
        boolean hasRightBizPartnerBranchBankAccountCustomer;
        boolean hasRightBizPartnerBranchBankAccountCreditor;
        boolean hasRightBizPartnerBranchBankAccountDebtor;
        boolean hasRightBizPartnerBranchBankAccountEmployee;
        boolean hasRightBizPartnerType;
        boolean hasRightBizPartnerBizArea;

        jmMenuBizPartner = new JMenu("Asociados de negocios");
        jmiBizPartner = new JMenuItem("Todos los asociados de negocios (Q)");
        jmiBizPartnerCustomer = new JMenuItem("Clientes");
        jmiBizPartnerSupplier = new JMenuItem("Proveedores");
        jmiBizPartnerDebtor = new JMenuItem("Deudores diversos");
        jmiBizPartnerCreditor = new JMenuItem("Acreedores diversos");
        jmiBizPartnerAttBank = new JMenuItem("Bancos");
        jmiBizPartnerAttCarrier = new JMenuItem("Transportistas");
        jmiBizPartnerAttEmployee = new JMenuItem("Empleados");
        jmiBizPartnerAttSalesAgent = new JMenuItem("Agentes de ventas");
        jmiBizPartnerCreditCustomer = new JMenuItem("Información crédito de clientes");
        jmiBizPartnerCreditSupplier = new JMenuItem("Información crédito de proveedores");
        jmMenuBranch = new JMenu("Sucursales");
        jmiBizPartnerBranch = new JMenuItem("Sucursales de todos los asociados de negocios (Q)");
        jmiBizPartnerBranchCustomer = new JMenuItem("Sucursales de clientes (Q)");
        jmiBizPartnerBranchSupplier = new JMenuItem("Sucursales de proveedores (Q)");
        jmiBizPartnerBranchDebtor = new JMenuItem("Sucursales de deudores diversos (Q)");
        jmiBizPartnerBranchCreditor = new JMenuItem("Sucursales de acreedores diversos (Q)");
        jmMenuBranchAddress = new JMenu("Domicilios");
        jmiBizPartnerBranchAddress = new JMenuItem("Domicilios de todos los asociados de negocios (Q)");
        jmiBizPartnerBranchAddressCustomer = new JMenuItem("Domicilios de clientes (Q)");
        jmiBizPartnerBranchAddressSupplier = new JMenuItem("Domicilios de proveedores (Q)");
        jmiBizPartnerBranchAddressDebtor = new JMenuItem("Domicilios de deudores diversos (Q)");
        jmiBizPartnerBranchAddressCreditor = new JMenuItem("Domicilios de acreedores diversos (Q)");
        jmiBizPartnerBranchAddressEmployee = new JMenuItem("Domicilios de empleados (Q)");
        jmMenuBranchContact = new JMenu("Contactos");
        jmiBizPartnerBranchContact = new JMenuItem("Contactos de todos los asociados de negocios (Q)");
        jmiBizPartnerBranchContactCustomer = new JMenuItem("Contactos de clientes");
        jmiBizPartnerBranchContactSupplier = new JMenuItem("Contactos de proveedores");
        jmiBizPartnerBranchContactDebtor = new JMenuItem("Contactos de deudores diversos");
        jmiBizPartnerBranchContactCreditor = new JMenuItem("Contactos de acreedores diversos");
        jmMenuBranchBankAccount = new JMenu("Cuentas bancarias");
        jmiBizPartnerBranchBankAccount = new JMenuItem("Cuentas bancarias de todos los asociados de negocios (Q)");
        jmiBizPartnerBranchBankAccountCustomer = new JMenuItem("Cuentas bancarias de clientes");
        jmiBizPartnerBranchBankAccountSupplier = new JMenuItem("Cuentas bancarias de proveedores");
        jmiBizPartnerBranchBankAccountDebtor = new JMenuItem("Cuentas bancarias de deudores diversos");
        jmiBizPartnerBranchBankAccountCreditor = new JMenuItem("Cuentas bancarias de acreedores diversos");
        jmiBizPartnerBranchBankAccountEmployee = new JMenuItem("Cuentas bancarias de empleados");
        jmMenuAddressee = new JMenu("Destinatarios");
        jmiBizPartnerAddresseeCustomer = new JMenuItem("Destinatarios de clientes");
        jmiBizPartnerType = new JMenuItem("Tipos de asociados de negocios");
        jmiBizPartnerBizArea = new JMenuItem("Áreas de negocios");

        jmMenuBizPartner.add(jmiBizPartner);
        jmMenuBizPartner.addSeparator();
        jmMenuBizPartner.add(jmiBizPartnerCustomer);
        jmMenuBizPartner.add(jmiBizPartnerSupplier);
        jmMenuBizPartner.add(jmiBizPartnerDebtor);
        jmMenuBizPartner.add(jmiBizPartnerCreditor);
        jmMenuBizPartner.addSeparator();
        jmMenuBizPartner.add(jmiBizPartnerAttBank);
        jmMenuBizPartner.add(jmiBizPartnerAttCarrier);
        jmMenuBizPartner.add(jmiBizPartnerAttEmployee);
        jmMenuBizPartner.add(jmiBizPartnerAttSalesAgent);

        jmMenuBizPartner.addSeparator();
        jmMenuBizPartner.add(jmiBizPartnerCreditCustomer);
        jmMenuBizPartner.add(jmiBizPartnerCreditSupplier);

        jmMenuBizPartner.addSeparator();
        jmMenuBranch.add(jmiBizPartnerBranch);
        jmMenuBranch.add(jmiBizPartnerBranchCustomer);
        jmMenuBranch.add(jmiBizPartnerBranchSupplier);
        jmMenuBranch.add(jmiBizPartnerBranchDebtor);
        jmMenuBranch.add(jmiBizPartnerBranchCreditor);
        jmMenuBizPartner.add(jmMenuBranch);
        jmMenuBranchAddress.add(jmiBizPartnerBranchAddress);
        jmMenuBranchAddress.add(jmiBizPartnerBranchAddressCustomer);
        jmMenuBranchAddress.add(jmiBizPartnerBranchAddressSupplier);
        jmMenuBranchAddress.add(jmiBizPartnerBranchAddressDebtor);
        jmMenuBranchAddress.add(jmiBizPartnerBranchAddressCreditor);
        jmMenuBranchAddress.addSeparator();
        jmMenuBranchAddress.add(jmiBizPartnerBranchAddressEmployee);
        jmMenuBizPartner.add(jmMenuBranchAddress);
        jmMenuBranchContact.add(jmiBizPartnerBranchContact);
        jmMenuBranchContact.add(jmiBizPartnerBranchContactCustomer);
        jmMenuBranchContact.add(jmiBizPartnerBranchContactSupplier);
        jmMenuBranchContact.add(jmiBizPartnerBranchContactDebtor);
        jmMenuBranchContact.add(jmiBizPartnerBranchContactCreditor);
        jmMenuBizPartner.add(jmMenuBranchContact);
        jmMenuBranchBankAccount.add(jmiBizPartnerBranchBankAccount);
        jmMenuBranchBankAccount.add(jmiBizPartnerBranchBankAccountCustomer);
        jmMenuBranchBankAccount.add(jmiBizPartnerBranchBankAccountSupplier);
        jmMenuBranchBankAccount.add(jmiBizPartnerBranchBankAccountDebtor);
        jmMenuBranchBankAccount.add(jmiBizPartnerBranchBankAccountCreditor);
        jmMenuBranchBankAccount.addSeparator();
        jmMenuBranchBankAccount.add(jmiBizPartnerBranchBankAccountEmployee);
        jmMenuBizPartner.add(jmMenuBranchBankAccount);
        jmMenuAddressee.add(jmiBizPartnerAddresseeCustomer);
        jmMenuBizPartner.add(jmMenuAddressee);

        jmMenuBizPartner.addSeparator();
        jmMenuBizPartner.add(jmiBizPartnerType);
        jmMenuBizPartner.add(jmiBizPartnerBizArea);

        jmiBizPartner.addActionListener(this);
        jmiBizPartnerCustomer.addActionListener(this);
        jmiBizPartnerSupplier.addActionListener(this);
        jmiBizPartnerDebtor.addActionListener(this);
        jmiBizPartnerCreditor.addActionListener(this);
        jmiBizPartnerAttBank.addActionListener(this);
        jmiBizPartnerAttCarrier.addActionListener(this);
        jmiBizPartnerAttEmployee.addActionListener(this);
        jmiBizPartnerAttSalesAgent.addActionListener(this);
        jmiBizPartnerCreditCustomer.addActionListener(this);
        jmiBizPartnerCreditSupplier.addActionListener(this);
        jmiBizPartnerBranch.addActionListener(this);
        jmiBizPartnerBranchSupplier.addActionListener(this);
        jmiBizPartnerBranchCustomer.addActionListener(this);
        jmiBizPartnerBranchCreditor.addActionListener(this);
        jmiBizPartnerBranchDebtor.addActionListener(this);
        jmiBizPartnerBranchAddress.addActionListener(this);
        jmiBizPartnerBranchAddressSupplier.addActionListener(this);
        jmiBizPartnerBranchAddressCustomer.addActionListener(this);
        jmiBizPartnerBranchAddressCreditor.addActionListener(this);
        jmiBizPartnerBranchAddressDebtor.addActionListener(this);
        jmiBizPartnerBranchAddressEmployee.addActionListener(this);
        jmiBizPartnerBranchContact.addActionListener(this);
        jmiBizPartnerBranchContactSupplier.addActionListener(this);
        jmiBizPartnerBranchContactCustomer.addActionListener(this);
        jmiBizPartnerBranchContactCreditor.addActionListener(this);
        jmiBizPartnerBranchContactDebtor.addActionListener(this);
        jmiBizPartnerBranchBankAccount.addActionListener(this);
        jmiBizPartnerBranchBankAccountSupplier.addActionListener(this);
        jmiBizPartnerBranchBankAccountCustomer.addActionListener(this);
        jmiBizPartnerBranchBankAccountCreditor.addActionListener(this);
        jmiBizPartnerBranchBankAccountDebtor.addActionListener(this);
        jmiBizPartnerBranchBankAccountEmployee.addActionListener(this);
        jmiBizPartnerAddresseeCustomer.addActionListener(this);
        jmiBizPartnerType.addActionListener(this);
        jmiBizPartnerBizArea.addActionListener(this);

        moFormBizPartner = null;
        moFormBizPartnerSimple = null;
        moFormBizPartnerBranchContact = null;
        moFormBizPartnerBranchBankAccount = null;
        moFormBizPartnerAddresseeCustomer = null;
        moFormBizPartnerType = null;
        moFormBizPartnerBizArea = null;

        moPickerBizPartnerCategory = null;
        moPickerBizPartner = null;
        moPickerBizPartnerAttBank = null;
        moPickerBizPartnerAttCarrier = null;
        moPickerBizPartnerSupplier = null;
        moPickerBizPartnerCustomer = null;
        moPickerBizPartnerCreditor = null;
        moPickerBizPartnerDebtor = null;
        moPickerBizPartnerEmployee = null;
        moPickerBizPartnerBranch = null;
        moPickerBizPartnerBranchAddress = null;
        moPickerBizPartnerIdentity = null;
        moPickerBizPartnerExtraSupplierCustomer = null;
        moPickerBizPartnerExtraCreditorDebtor = null;
        moPickerBizPartnerSalesAgent = null;
        moPickerBizPartnerEmployeeMfg = null;
        moPickerContactType = null;
        moPickerBizPartnerType = null;

        hasRightBizPartner = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP).HasRight;
        hasRightBizPartnerSupplier = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_SUP).HasRight;
        hasRightBizPartnerCustomer = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_CUS).HasRight;
        hasRightBizPartnerCreditor = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_CDR).HasRight;
        hasRightBizPartnerDebtor = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_DBR).HasRight;
        hasRightBizPartnerEmployee = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BP_EMP).HasRight;
        hasRightCreditPurchases = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_PUR_CRED).HasRight;
        hasRightCreditSales = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_SAL_CRED).HasRight;
        hasRightBizPartnerBranch = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB).HasRight;
        hasRightBizPartnerBranchSupplier = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_SUP).HasRight;
        hasRightBizPartnerBranchCustomer = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_CUS).HasRight;
        hasRightBizPartnerBranchAddress = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_ADD).HasRight;
        hasRightBizPartnerBranchAddressSupplier = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_ADD_SUP).HasRight;
        hasRightBizPartnerBranchAddressCustomer = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_ADD_CUS).HasRight;
        hasRightBizPartnerBranchContact = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_CON).HasRight;
        hasRightBizPartnerBranchContactSupplier = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_CON_SUP).HasRight;
        hasRightBizPartnerBranchContactCustomer = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BPB_CON_CUS).HasRight;
        hasRightBizPartnerBranchBankAccount = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC).HasRight;
        hasRightBizPartnerBranchBankAccountSupplier = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC_SUP).HasRight;
        hasRightBizPartnerBranchBankAccountCustomer = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC_CUS).HasRight;
        hasRightBizPartnerBranchBankAccountCreditor = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC_CDR).HasRight;
        hasRightBizPartnerBranchBankAccountDebtor = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC_DBR).HasRight;
        hasRightBizPartnerBranchBankAccountEmployee = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BK_ACC_EMP).HasRight;
        hasRightBizPartnerType = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_TP_BP).HasRight;
        hasRightBizPartnerBizArea = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_BPS_BA).HasRight;

        jmMenuBizPartner.setEnabled(hasRightBizPartner || hasRightBizPartnerSupplier || hasRightBizPartnerCustomer || hasRightBizPartnerCreditor || hasRightBizPartnerDebtor ||
                                    hasRightBizPartnerEmployee || hasRightCreditPurchases || hasRightCreditSales || hasRightBizPartnerType || hasRightBizPartnerBizArea ||
                                    hasRightBizPartnerBranch || hasRightBizPartnerBranchSupplier || hasRightBizPartnerBranchCustomer ||
                                    hasRightBizPartnerBranchAddress || hasRightBizPartnerBranchAddressSupplier || hasRightBizPartnerBranchAddressCustomer ||
                                    hasRightBizPartnerBranchContact || hasRightBizPartnerBranchContactSupplier || hasRightBizPartnerBranchContactCustomer ||
                                    hasRightBizPartnerBranchBankAccount || hasRightBizPartnerBranchBankAccountSupplier || hasRightBizPartnerBranchBankAccountCustomer ||
                                    hasRightBizPartnerBranchBankAccountCreditor || hasRightBizPartnerBranchBankAccountDebtor || hasRightBizPartnerBranchBankAccountEmployee);
        jmiBizPartner.setEnabled(hasRightBizPartner);
        jmiBizPartnerCustomer.setEnabled(hasRightBizPartnerCustomer);
        jmiBizPartnerSupplier.setEnabled(hasRightBizPartnerSupplier);
        jmiBizPartnerDebtor.setEnabled(hasRightBizPartnerDebtor);
        jmiBizPartnerCreditor.setEnabled(hasRightBizPartnerCreditor);
        jmiBizPartnerAttBank.setEnabled(hasRightBizPartner);
        jmiBizPartnerAttCarrier.setEnabled(hasRightBizPartner);
        jmiBizPartnerAttEmployee.setEnabled(hasRightBizPartnerEmployee);
        jmiBizPartnerAttSalesAgent.setEnabled(hasRightBizPartner);
        jmiBizPartnerCreditCustomer.setEnabled(hasRightCreditSales);
        jmiBizPartnerCreditSupplier.setEnabled(hasRightCreditPurchases);
        jmMenuBranch.setEnabled(hasRightBizPartnerBranch || hasRightBizPartnerBranchSupplier || hasRightBizPartnerBranchCustomer || hasRightBizPartnerCreditor || hasRightBizPartnerDebtor);
        jmiBizPartnerBranch.setEnabled(hasRightBizPartnerBranch);
        jmiBizPartnerBranchSupplier.setEnabled(hasRightBizPartnerBranchSupplier);
        jmiBizPartnerBranchCustomer.setEnabled(hasRightBizPartnerBranchCustomer);
        jmiBizPartnerBranchCreditor.setEnabled(hasRightBizPartnerCreditor);
        jmiBizPartnerBranchDebtor.setEnabled(hasRightBizPartnerDebtor);
        jmMenuBranchAddress.setEnabled(hasRightBizPartnerBranchAddress || hasRightBizPartnerBranchAddressSupplier || hasRightBizPartnerBranchAddressCustomer || hasRightBizPartnerCreditor || hasRightBizPartnerDebtor || hasRightBizPartnerEmployee);
        jmiBizPartnerBranchAddress.setEnabled(hasRightBizPartnerBranchAddress);
        jmiBizPartnerBranchAddressSupplier.setEnabled(hasRightBizPartnerBranchAddressSupplier);
        jmiBizPartnerBranchAddressCustomer.setEnabled(hasRightBizPartnerBranchAddressCustomer);
        jmiBizPartnerBranchAddressCreditor.setEnabled(hasRightBizPartnerCreditor);
        jmiBizPartnerBranchAddressDebtor.setEnabled(hasRightBizPartnerDebtor);
        jmiBizPartnerBranchAddressEmployee.setEnabled(hasRightBizPartnerEmployee);
        jmMenuBranchContact.setEnabled(hasRightBizPartnerBranchContact || hasRightBizPartnerBranchContactSupplier || hasRightBizPartnerBranchContactCustomer || hasRightBizPartnerCreditor || hasRightBizPartnerDebtor || hasRightBizPartnerEmployee);
        jmiBizPartnerBranchContact.setEnabled(hasRightBizPartnerBranchContact);
        jmiBizPartnerBranchContactSupplier.setEnabled(hasRightBizPartnerBranchContactSupplier);
        jmiBizPartnerBranchContactCustomer.setEnabled(hasRightBizPartnerBranchContactCustomer);
        jmiBizPartnerBranchContactCreditor.setEnabled(hasRightBizPartnerCreditor);
        jmiBizPartnerBranchContactDebtor.setEnabled(hasRightBizPartnerDebtor);
        jmMenuBranchBankAccount.setEnabled(hasRightBizPartnerBranchBankAccount || hasRightBizPartnerBranchBankAccountSupplier || hasRightBizPartnerBranchBankAccountCustomer || hasRightBizPartnerBranchBankAccountCreditor || hasRightBizPartnerBranchBankAccountDebtor || hasRightBizPartnerBranchBankAccountEmployee);
        jmiBizPartnerBranchBankAccount.setEnabled(hasRightBizPartnerBranchBankAccount);
        jmiBizPartnerBranchBankAccountSupplier.setEnabled(hasRightBizPartnerBranchBankAccountSupplier);
        jmiBizPartnerBranchBankAccountCustomer.setEnabled(hasRightBizPartnerBranchBankAccountCustomer);
        jmiBizPartnerBranchBankAccountCreditor.setEnabled(hasRightBizPartnerBranchBankAccountCreditor);
        jmiBizPartnerBranchBankAccountDebtor.setEnabled(hasRightBizPartnerBranchBankAccountDebtor);
        jmiBizPartnerBranchBankAccountEmployee.setEnabled(hasRightBizPartnerBranchBankAccountEmployee);
        jmMenuAddressee.setEnabled(hasRightBizPartner || hasRightBizPartnerCustomer || hasRightBizPartnerBranchAddressCustomer);
        jmiBizPartnerAddresseeCustomer.setEnabled(hasRightBizPartnerCustomer || hasRightBizPartnerBranchAddressCustomer);
        jmiBizPartnerType.setEnabled(hasRightBizPartnerType);
        jmiBizPartnerBizArea.setEnabled(hasRightBizPartnerBizArea);
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.BPSU_BP:
                case SDataConstants.BPSX_BP_CO:
                case SDataConstants.BPSX_BP_SUP:
                case SDataConstants.BPSX_BP_CUS:
                    if (moFormBizPartner == null) {
                        moFormBizPartner = new SFormBizPartner(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBizPartner();
                    }
                    miForm = moFormBizPartner;
                    miForm.setValue(SDataConstantsSys.VALUE_BIZ_PARTNER_TYPE, new int[] { formType });
                    break;
                case SDataConstants.BPSX_BP_CDR:
                case SDataConstants.BPSX_BP_DBR:
                    if (moFormBizPartnerSimple == null) {
                        moFormBizPartnerSimple = new SFormBizPartnerSimple(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBizPartner();
                    }
                    miForm = moFormBizPartnerSimple;
                    miForm.setValue(SDataConstantsSys.VALUE_BIZ_PARTNER_TYPE, new int[] { formType });
                    break;
                case SDataConstants.BPSX_BP_ATT_BANK:
                case SDataConstants.BPSX_BP_ATT_CARR:
                case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                    if (moFormBizPartnerAttribute == null) {
                        moFormBizPartnerAttribute = new SFormBizPartnerAttribute(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBizPartner();
                    }
                    miForm = moFormBizPartnerAttribute;
                    miForm.setValue(SDataConstantsSys.VALUE_BIZ_PARTNER_TYPE, new int[] { formType });
                    break;
                case SDataConstants.BPSX_BP_EMP:
                    if (moFormBizPartnerEmployee == null) {
                        moFormBizPartnerEmployee = new SFormBizPartnerEmployee(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBizPartner();
                    }
                    miForm = moFormBizPartnerEmployee;
                    miForm.setValue(SDataConstantsSys.VALUE_BIZ_PARTNER_TYPE, new int[] { formType });
                    break;
                case SDataConstants.BPSU_BPB_CON:
                case SDataConstants.BPSX_BPB_CON_SUP:
                case SDataConstants.BPSX_BPB_CON_CUS:
                case SDataConstants.BPSX_BPB_CON_CDR:
                case SDataConstants.BPSX_BPB_CON_DBR:
                case SDataConstants.BPSX_BPB_CON_EMP:
                    if (moFormBizPartnerBranchContact == null) {
                        moFormBizPartnerBranchContact = new SFormBizPartnerBranchContact(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBizPartnerBranchContact();
                    }
                    miForm = moFormBizPartnerBranchContact;
                    miForm.setValue(1, new int[] { formType });
                    break;
                case SDataConstants.BPSU_BANK_ACC:
                case SDataConstants.BPSX_BANK_ACC_SUP:
                case SDataConstants.BPSX_BANK_ACC_CUS:
                case SDataConstants.BPSX_BANK_ACC_CDR:
                case SDataConstants.BPSX_BANK_ACC_DBR:
                case SDataConstants.BPSX_BANK_ACC_EMP:
                    if (moFormBizPartnerBranchBankAccount == null) {
                        moFormBizPartnerBranchBankAccount = new SFormBizPartnerBranchBankAccount(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBizPartnerBranchBankAccount();
                    }
                    miForm = moFormBizPartnerBranchBankAccount;
                    miForm.setValue(1, new int[] { formType });
                    break;
                case SDataConstants.BPSU_BP_ADDEE:
                    switch (auxType) {
                        case SDataConstants.BPSX_BP_CUS:
                            if (moFormBizPartnerAddresseeCustomer == null) {
                                moFormBizPartnerAddresseeCustomer = new SFormBizPartnerAddressee(miClient, auxType);
                            }
                            if (pk != null) {
                                moRegistry = new SDataBizPartnerAddressee();
                            }
                            miForm = moFormBizPartnerAddresseeCustomer;
                            break;
                        default:
                            throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM);
                    }
                    break;
                case SDataConstants.BPSU_TP_BP:
                    if (moFormBizPartnerType == null) {
                        moFormBizPartnerType = new SFormBizPartnerType(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBizPartnerType();
                    }
                    miForm = moFormBizPartnerType;
                    break;
                case SDataConstants.BPSU_BA:
                    if (moFormBizPartnerBizArea == null) {
                        moFormBizPartnerBizArea = new SFormBizPartnerBizArea(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataBizPartnerBizAreaCatalogue();
                    }
                    miForm = moFormBizPartnerBizArea;
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_FORM);
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
        java.lang.String sViewTitle = "";
        java.lang.Class oViewClass = null;

        try {
            setFrameWaitCursor();

            switch (viewType) {
                case SDataConstants.BPSU_BP:
                case SDataConstants.BPSX_BP_CUS:
                case SDataConstants.BPSX_BP_SUP:
                case SDataConstants.BPSX_BP_DBR:
                case SDataConstants.BPSX_BP_CDR:
                case SDataConstants.BPSX_BP_ATT_BANK:
                case SDataConstants.BPSX_BP_ATT_CARR:
                case SDataConstants.BPSX_BP_EMP:
                case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                    oViewClass = erp.mbps.view.SViewBizPartner.class;
                    switch(viewType) {
                        case SDataConstants.BPSU_BP:
                            sViewTitle = "Todos asociados negocios Q";
                            break;
                        case SDataConstants.BPSX_BP_CUS:
                            sViewTitle = "Clientes";
                            break;
                        case SDataConstants.BPSX_BP_SUP:
                            sViewTitle = "Proveedores";
                            break;
                        case SDataConstants.BPSX_BP_DBR:
                            sViewTitle = "Deudores diversos";
                            break;
                        case SDataConstants.BPSX_BP_CDR:
                            sViewTitle = "Acreedores diversos";
                            break;
                        case SDataConstants.BPSX_BP_ATT_BANK:
                            sViewTitle = "Bancos";
                            break;
                        case SDataConstants.BPSX_BP_ATT_CARR:
                            sViewTitle = "Transportistas";
                            break;
                        case SDataConstants.BPSX_BP_EMP:
                            sViewTitle = "Empleados";
                            break;
                        case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                            sViewTitle = "Agentes ventas";
                            break;
                        default:
                    }
                    auxType01 = viewType;
                    break;
                case SDataConstants.BPSX_BP_EMP_REL:
                    oViewClass = erp.mbps.view.SViewBizPartnerEmployeeRelatives.class;
                    sViewTitle = "Datos personales empleados";
                    break;
                case SDataConstants.BPSU_BP_CT:
                    oViewClass = erp.mbps.view.SViewBizPartnerCategory.class;
                    switch(auxType01) {
                        case SDataConstantsSys.BPSS_CT_BP_SUP:
                            sViewTitle = "Info. créd. proveedores";
                            break;
                        case SDataConstantsSys.BPSS_CT_BP_CUS:
                            sViewTitle = "Info. créd. clientes";
                            break;
                    }
                    break;
                case SDataConstants.BPSU_BPB:
                case SDataConstants.BPSX_BPB_SUP:
                case SDataConstants.BPSX_BPB_CUS:
                case SDataConstants.BPSX_BPB_CDR:
                case SDataConstants.BPSX_BPB_DBR:
                    oViewClass = erp.mbps.view.SViewBizPartnerBranch.class;
                    switch(viewType) {
                        case SDataConstants.BPSU_BPB:
                            sViewTitle = "Sucursales todos asoc. negocios (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_CUS:
                            sViewTitle = "Sucursales clientes (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_SUP:
                            sViewTitle = "Sucursales proveedores (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_DBR:
                            sViewTitle = "Sucursales deudores diversos (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_CDR:
                            sViewTitle = "Sucursales acreedores diversos (Q)";
                            break;
                        default:
                    }
                    auxType01 = viewType;
                    break;
                case SDataConstants.BPSU_BPB_ADD:
                case SDataConstants.BPSX_BPB_ADD_SUP:
                case SDataConstants.BPSX_BPB_ADD_CUS:
                case SDataConstants.BPSX_BPB_ADD_CDR:
                case SDataConstants.BPSX_BPB_ADD_DBR:
                case SDataConstants.BPSX_BPB_ADD_EMP:
                    oViewClass = erp.mbps.view.SViewBizPartnerBranchAddress.class;
                    switch(viewType) {
                        case SDataConstants.BPSU_BPB_ADD:
                            sViewTitle = "Domicilios todos asoc. negocios (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_ADD_CUS:
                            sViewTitle = "Domicilios clientes (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_ADD_SUP:
                            sViewTitle = "Domicilios proveedores (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_ADD_DBR:
                            sViewTitle = "Domicilios deudores diversos (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_ADD_CDR:
                            sViewTitle = "Domicilios acreedores diversos (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_ADD_EMP:
                            sViewTitle = "Domicilios empleados (Q)";
                            break;
                        default:
                    }
                    auxType01 = viewType;
                    break;
                case SDataConstants.BPSU_BPB_CON:
                case SDataConstants.BPSX_BPB_CON_SUP:
                case SDataConstants.BPSX_BPB_CON_CUS:
                case SDataConstants.BPSX_BPB_CON_CDR:
                case SDataConstants.BPSX_BPB_CON_DBR:
                case SDataConstants.BPSX_BPB_CON_EMP:
                    oViewClass = erp.mbps.view.SViewBizPartnerBranchContact.class;
                    switch (viewType) {
                        case SDataConstants.BPSU_BPB_CON:
                            sViewTitle = "Contactos todos asoc. negocios (Q)";
                            break;
                        case SDataConstants.BPSX_BPB_CON_CUS:
                            sViewTitle = "Contactos clientes";
                            break;
                        case SDataConstants.BPSX_BPB_CON_SUP:
                            sViewTitle = "Contactos proveedores";
                            break;
                        case SDataConstants.BPSX_BPB_CON_DBR:
                            sViewTitle = "Contactos deudores diversos";
                            break;
                        case SDataConstants.BPSX_BPB_CON_CDR:
                            sViewTitle = "Contactos acreedores diversos";
                            break;
                        case SDataConstants.BPSX_BPB_CON_EMP:
                            sViewTitle = "Contactos empleados";
                            break;
                        default:
                    }
                    auxType01 = viewType;
                    break;
                case SDataConstants.BPSU_BANK_ACC:
                case SDataConstants.BPSX_BANK_ACC_SUP:
                case SDataConstants.BPSX_BANK_ACC_CUS:
                case SDataConstants.BPSX_BANK_ACC_CDR:
                case SDataConstants.BPSX_BANK_ACC_DBR:
                case SDataConstants.BPSX_BANK_ACC_EMP:
                    oViewClass = erp.mbps.view.SViewBizPartnerBranchBankAccount.class;
                    switch (viewType) {
                        case SDataConstants.BPSU_BANK_ACC:
                            sViewTitle = "Ctas. bancarias todos asoc. negocios (Q)";
                            break;
                        case SDataConstants.BPSX_BANK_ACC_CUS:
                            sViewTitle = "Ctas. bancarias clientes";
                            break;
                        case SDataConstants.BPSX_BANK_ACC_SUP:
                            sViewTitle = "Ctas. bancarias proveedores";
                            break;
                        case SDataConstants.BPSX_BANK_ACC_DBR:
                            sViewTitle = "Ctas. bancarias deudores diversos";
                            break;
                        case SDataConstants.BPSX_BANK_ACC_CDR:
                            sViewTitle = "Ctas. bancarias acreedores diversos";
                            break;
                        case SDataConstants.BPSX_BANK_ACC_EMP:
                            sViewTitle = "Ctas. bancarias empleados";
                            break;
                        default:
                    }
                    auxType01 = viewType;
                    break;
                case SDataConstants.BPSU_BP_ADDEE:
                    oViewClass = erp.mbps.view.SViewBizPartnerAddressee.class;
                    switch (auxType01) {
                        case SDataConstants.BPSX_BP_CUS:
                            sViewTitle = "Destinatarios clientes";
                            break;
                        default:
                    }
                    break;
                case SDataConstants.BPSU_TP_BP:
                    oViewClass = erp.mbps.view.SViewBizPartnerType.class;
                    sViewTitle = "Tipos asoc. negocios";
                    break;
                case SDataConstants.BPSU_BA:
                    oViewClass = erp.mbps.view.SViewBizPartnerBizArea.class;
                    sViewTitle = "Áreas negocios";
                    break;
                default:
                    throw new Exception(SLibConstants.MSG_ERR_UTIL_UNKNOWN_VIEW);
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
    public int showForm(int formType, int auxType, Object pk) {
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
                case SDataConstants.BPSS_TP_BP_IDY:
                    picker = moPickerBizPartnerIdentity = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerIdentity);
                    break;
                case SDataConstants.BPSU_TP_BP:
                    picker = moPickerBizPartnerType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerType);
                    break;
                case SDataConstants.BPSS_TP_CON:
                    picker = moPickerContactType = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerContactType);
                    break;
                case SDataConstants.BPSS_CT_BP:
                    picker = moPickerBizPartnerCategory = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerCategory);
                    break;
                case SDataConstants.BPSU_BP:
                    picker = moPickerBizPartner = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartner);
                    break;
                case SDataConstants.BPSX_BP_SUP:
                    picker = moPickerBizPartnerSupplier = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerSupplier);
                    break;
                case SDataConstants.BPSX_BP_CUS:
                    picker = moPickerBizPartnerCustomer = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerCustomer);
                    break;
                case SDataConstants.BPSX_BP_CDR:
                    picker = moPickerBizPartnerCreditor = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerCreditor);
                    break;
                case SDataConstants.BPSX_BP_DBR:
                    picker = moPickerBizPartnerDebtor = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerDebtor);
                    break;
                case SDataConstants.BPSX_BP_EMP:
                    picker = moPickerBizPartnerEmployee = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerEmployee);
                    break;
                case SDataConstants.BPSX_BP_X_SUP_CUS:
                    picker = moPickerBizPartnerExtraSupplierCustomer = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerExtraSupplierCustomer);
                    break;
                case SDataConstants.BPSX_BP_X_CDR_DBR:
                    picker = moPickerBizPartnerExtraCreditorDebtor = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerExtraCreditorDebtor);
                    break;
                case SDataConstants.BPSX_BP_ATT_BANK:
                    picker = moPickerBizPartnerAttBank = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerAttBank);
                    break;
                case SDataConstants.BPSX_BP_ATT_CARR:
                    picker = moPickerBizPartnerAttCarrier = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerAttCarrier);
                    break;
                case SDataConstants.BPSX_BP_ATT_EMP_MFG:
                    picker = moPickerBizPartnerEmployeeMfg = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerEmployeeMfg);
                    break;
                case SDataConstants.BPSX_BP_ATT_SAL_AGT:
                    picker = moPickerBizPartnerSalesAgent = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerSalesAgent);
                    break;
                case SDataConstants.BPSU_BPB:
                    picker = moPickerBizPartnerBranch = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerBranch);
                    break;
                case SDataConstants.BPSU_BPB_ADD:
                    picker = moPickerBizPartnerBranchAddress = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBizPartnerBranchAddress);
                    break;
                case SDataConstants.BPSU_BANK_ACC:
                    picker = moPickerBankAccount = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerBankAccount);
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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        javax.swing.JMenu[] menues = null;

        switch (moduleType) {
            case SDataConstants.MOD_CFG:
                menues = new JMenu[] { jmMenuBizPartner };
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

            if (item == jmiBizPartner) {
                showView(SDataConstants.BPSU_BP);
            }
            else if (item == jmiBizPartnerCustomer) {
                showView(SDataConstants.BPSX_BP_CUS);
            }
            else if (item == jmiBizPartnerSupplier) {
                showView(SDataConstants.BPSX_BP_SUP);
            }
            else if (item == jmiBizPartnerDebtor) {
                showView(SDataConstants.BPSX_BP_DBR);
            }
            else if (item == jmiBizPartnerCreditor) {
                showView(SDataConstants.BPSX_BP_CDR);
            }
            else if (item == jmiBizPartnerAttBank) {
                showView(SDataConstants.BPSX_BP_ATT_BANK);
            }
            else if (item == jmiBizPartnerAttCarrier) {
                showView(SDataConstants.BPSX_BP_ATT_CARR);
            }
            else if (item == jmiBizPartnerAttEmployee) {
                showView(SDataConstants.BPSX_BP_EMP);
            }
            else if (item == jmiBizPartnerAttSalesAgent) {
                showView(SDataConstants.BPSX_BP_ATT_SAL_AGT);
            }
            else if (item == jmiBizPartnerCreditCustomer) {
                showView(SDataConstants.BPSU_BP_CT, SDataConstantsSys.BPSS_CT_BP_CUS);
            }
            else if (item == jmiBizPartnerCreditSupplier) {
                showView(SDataConstants.BPSU_BP_CT, SDataConstantsSys.BPSS_CT_BP_SUP);
            }
            else if (item == jmiBizPartnerBranch) {
                showView(SDataConstants.BPSU_BPB);
            }
            else if (item == jmiBizPartnerBranchSupplier) {
                showView(SDataConstants.BPSX_BPB_SUP);
            }
            else if (item == jmiBizPartnerBranchCustomer) {
                showView(SDataConstants.BPSX_BPB_CUS);
            }
            else if (item == jmiBizPartnerBranchCreditor) {
                showView(SDataConstants.BPSX_BPB_CDR);
            }
            else if (item == jmiBizPartnerBranchDebtor) {
                showView(SDataConstants.BPSX_BPB_DBR);
            }
            else if (item == jmiBizPartnerBranchAddress) {
                showView(SDataConstants.BPSU_BPB_ADD);
            }
            else if (item == jmiBizPartnerBranchAddressSupplier) {
                showView(SDataConstants.BPSX_BPB_ADD_SUP);
            }
            else if (item == jmiBizPartnerBranchAddressCustomer) {
                showView(SDataConstants.BPSX_BPB_ADD_CUS);
            }
            else if (item == jmiBizPartnerBranchAddressCreditor) {
                showView(SDataConstants.BPSX_BPB_ADD_CDR);
            }
            else if (item == jmiBizPartnerBranchAddressDebtor) {
                showView(SDataConstants.BPSX_BPB_ADD_DBR);
            }
            else if (item == jmiBizPartnerBranchAddressEmployee) {
                showView(SDataConstants.BPSX_BPB_ADD_EMP);
            }
            else if (item == jmiBizPartnerBranchContact) {
                showView(SDataConstants.BPSU_BPB_CON);
            }
            else if (item == jmiBizPartnerBranchContactSupplier) {
                showView(SDataConstants.BPSX_BPB_CON_SUP);
            }
            else if (item == jmiBizPartnerBranchContactCustomer) {
                showView(SDataConstants.BPSX_BPB_CON_CUS);
            }
            else if (item == jmiBizPartnerBranchContactCreditor) {
                showView(SDataConstants.BPSX_BPB_CON_CDR);
            }
            else if (item == jmiBizPartnerBranchContactDebtor) {
                showView(SDataConstants.BPSX_BPB_CON_DBR);
            }
            else if (item == jmiBizPartnerBranchBankAccount) {
                showView(SDataConstants.BPSU_BANK_ACC);
            }
            else if (item == jmiBizPartnerBranchBankAccountSupplier) {
                showView(SDataConstants.BPSX_BANK_ACC_SUP);
            }
            else if (item == jmiBizPartnerBranchBankAccountCustomer) {
                showView(SDataConstants.BPSX_BANK_ACC_CUS);
            }
            else if (item == jmiBizPartnerBranchBankAccountCreditor) {
                showView(SDataConstants.BPSX_BANK_ACC_CDR);
            }
            else if (item == jmiBizPartnerBranchBankAccountDebtor) {
                showView(SDataConstants.BPSX_BANK_ACC_DBR);
            }
            else if (item == jmiBizPartnerBranchBankAccountEmployee) {
                showView(SDataConstants.BPSX_BANK_ACC_EMP);
            }
            else if (item == jmiBizPartnerAddresseeCustomer) {
                showView(SDataConstants.BPSU_BP_ADDEE, SDataConstants.BPSX_BP_CUS);
            }
            else if (item == jmiBizPartnerType) {
                showView(SDataConstants.BPSU_TP_BP);
            }
            else if (item == jmiBizPartnerBizArea) {
                showView(SDataConstants.BPSU_BA);
            }
        }
    }
}
