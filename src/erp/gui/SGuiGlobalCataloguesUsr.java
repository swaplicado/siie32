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
import erp.mtrn.data.SDataUserConfigurationTransaction;
import erp.mtrn.form.SFormUserConfigurationTransaction;
import erp.musr.data.*;
import erp.musr.form.*;
import erp.redis.form.SFormRedisSessions;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 *
 * @author Sergio Flores
 */
public class SGuiGlobalCataloguesUsr extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmMenuUser;
    private javax.swing.JMenuItem jmiUser;
    private javax.swing.JMenuItem jmiRedisLocks;
    private javax.swing.JMenuItem jmiRedisSessions;
    private javax.swing.JMenuItem jmiUpdatedUser;
    private javax.swing.JMenuItem jmiAccessCompany;
    private javax.swing.JMenuItem jmiAccessCompanyBranch;
    private javax.swing.JMenuItem jmiAccessCompanyBranchEntity;
    private javax.swing.JMenuItem jmiUserRight;
    private javax.swing.JMenuItem jmiUserRightRole;
    private javax.swing.JMenuItem jmiUserRightPrivilege;
    private javax.swing.JMenuItem jmiUserConfigurationTransaction;
    private javax.swing.JMenuItem jmiUserFunctionalArea;

    private erp.musr.form.SFormUser moFormUser;
    private erp.musr.form.SFormUpdatedUser moFormUpdatedUser;
    private erp.musr.form.SFormUserRight moFormUserRolePrivilege;
    private erp.mtrn.form.SFormUserConfigurationTransaction moFormUserConfigurationTransaction;
    
    private erp.musr.form.SFormLockUser moFormLock;
    private erp.redis.form.SFormRedisSessions moFormRedisSessions;

    private erp.form.SFormOptionPicker moPickerUsers;

    public SGuiGlobalCataloguesUsr(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_CFG);
        initComponents();
    }

    private void initComponents() {
        boolean hasRightUser;

        jmMenuUser = new JMenu("Usuarios");
        jmiUser = new JMenuItem("Usuarios");
        jmiRedisLocks = new JMenuItem("Locks en Redis");
        jmiRedisSessions = new JMenuItem("Sesiones en Redis");
        jmiUpdatedUser = new JMenuItem("Get Updated Users");
        jmiAccessCompany = new JMenuItem("Acceso a empresas");
        jmiAccessCompanyBranch = new JMenuItem("Acceso a sucursales de empresas");
        jmiAccessCompanyBranchEntity = new JMenuItem("Acceso a entidades de sucursales");
        jmiUserRight = new JMenuItem("Derechos de usuarios");
        jmiUserRightRole = new JMenuItem("Roles de usuarios");
        jmiUserRightPrivilege = new JMenuItem("Privilegios de usuarios");
        jmiUserConfigurationTransaction = new JMenuItem("Configuración de usuarios para transacciones");
        jmiUserFunctionalArea = new JMenuItem("Usuarios vs. áreas funcionales");

        jmMenuUser.add(jmiUser);
        jmMenuUser.add(jmiRedisLocks);
        jmMenuUser.add(jmiRedisSessions);
        jmMenuUser.add(jmiUpdatedUser);
        jmMenuUser.addSeparator();
        jmMenuUser.add(jmiAccessCompany);
        jmMenuUser.add(jmiAccessCompanyBranch);
        jmMenuUser.add(jmiAccessCompanyBranchEntity);
        jmMenuUser.addSeparator();
        jmMenuUser.add(jmiUserRight);
        jmMenuUser.add(jmiUserRightRole);
        jmMenuUser.add(jmiUserRightPrivilege);
        jmMenuUser.addSeparator();
        jmMenuUser.add(jmiUserConfigurationTransaction);
        jmMenuUser.add(jmiUserFunctionalArea);

        jmiUser.addActionListener(this);
        jmiRedisLocks.addActionListener(this);
        jmiRedisSessions.addActionListener(this);
        jmiUpdatedUser.addActionListener(this);
        jmiAccessCompany.addActionListener(this);
        jmiAccessCompanyBranch.addActionListener(this);
        jmiAccessCompanyBranchEntity.addActionListener(this);
        jmiUserRight.addActionListener(this);
        jmiUserRightRole.addActionListener(this);
        jmiUserRightPrivilege.addActionListener(this);
        jmiUserConfigurationTransaction.addActionListener(this);
        jmiUserFunctionalArea.addActionListener(this);

        moFormUser = null;
        moFormLock = null;
        moFormRedisSessions = null;
        moFormUpdatedUser = null;
        moFormUserRolePrivilege = null;
        moFormUserConfigurationTransaction = null;

        hasRightUser = miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_CAT_USR).HasRight;

        jmMenuUser.setEnabled(hasRightUser);
        jmiUser.setEnabled(hasRightUser);
        jmiRedisLocks.setEnabled(hasRightUser);
        jmiRedisSessions.setEnabled(hasRightUser);
        jmiUpdatedUser.setEnabled(hasRightUser);
        jmiAccessCompany.setEnabled(hasRightUser);
        jmiAccessCompanyBranch.setEnabled(hasRightUser);
        jmiAccessCompanyBranchEntity.setEnabled(hasRightUser);
        jmiUserRight.setEnabled(hasRightUser);
        jmiUserRightRole.setEnabled(hasRightUser);
        jmiUserRightPrivilege.setEnabled(hasRightUser);
        jmiUserConfigurationTransaction.setEnabled(hasRightUser);
        jmiUserFunctionalArea.setEnabled(hasRightUser && miClient.getSessionXXX().getParamsCompany().getIsFunctionalAreas());

        moPickerUsers = null;
    }

    private int showForm(int formType, int auxType, java.lang.Object pk, boolean isCopy) {
        int result = SLibConstants.UNDEFINED;

        try {
            clearFormMembers();
            setFrameWaitCursor();

            switch (formType) {
                case SDataConstants.USRU_USR:
                    if (moFormUser == null) {
                        moFormUser = new SFormUser(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataUser();
                    }
                    miForm = moFormUser;
                    break;
                case SDataConstants.USRU_USR_REDIS_LOCKS:
                    moFormLock = new SFormLockUser(miClient);
                    miForm = moFormLock;
                    break;
                case SDataConstants.USRU_USR_REDIS:
                    moFormRedisSessions = new SFormRedisSessions(miClient);
                    miForm = moFormRedisSessions;
                    break;
                case SDataConstants.USRU_USR_UPDATED:
                    moFormUpdatedUser = new SFormUpdatedUser(miClient);
                    miForm = moFormUpdatedUser;
                    break;
                case SDataConstants.USRX_RIGHT:
                    if (moFormUserRolePrivilege == null) {
                        moFormUserRolePrivilege = new SFormUserRight(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataUser();
                    }
                    miForm = moFormUserRolePrivilege;
                    break;
                case SDataConstants.TRN_USR_CFG:
                case SDataConstants.USRX_USR_FUNC:
                    if (moFormUserConfigurationTransaction == null) {
                        moFormUserConfigurationTransaction = new SFormUserConfigurationTransaction(miClient);
                    }
                    if (pk != null) {
                        moRegistry = new SDataUserConfigurationTransaction();
                    }
                    miForm = moFormUserConfigurationTransaction;
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
                case SDataConstants.USRU_USR:
                    oViewClass = erp.musr.view.SViewUser.class;
                    sViewTitle = "Usuarios";
                    break;
                case SDataConstants.USRU_ACCESS_CO:
                    oViewClass = erp.musr.view.SViewAccessCompany.class;
                    sViewTitle = "Acceso empresas";
                    break;
                case SDataConstants.USRU_ACCESS_COB:
                    oViewClass = erp.musr.view.SViewAccessCompanyBranch.class;
                    sViewTitle = "Acceso sucursales empresas";
                    break;
                case SDataConstants.USRU_ACCESS_COB_ENT:
                    oViewClass = erp.musr.view.SViewAccessCompanyBranchEntity.class;
                    sViewTitle = "Acceso entidades sucursales";
                    break;
                case SDataConstants.USRX_RIGHT:
                    oViewClass = erp.musr.view.SViewUserRight.class;
                    sViewTitle = "Derechos usuarios";
                    break;
                case SDataConstants.USRX_RIGHT_ROL:
                    oViewClass = erp.musr.view.SViewUserRightRole.class;
                    sViewTitle = "Roles usuarios";
                    break;
                case SDataConstants.USRX_RIGHT_PRV:
                    oViewClass = erp.musr.view.SViewUserRightPrivilege.class;
                    sViewTitle = "Privilegios usuarios";
                    break;
                case SDataConstants.USRX_USR_FUNC:
                    oViewClass = erp.musr.view.SViewUserFunctionalArea.class;
                    sViewTitle = "Usuarios vs. áreas funcionales";
                    break;
                case SDataConstants.TRN_USR_CFG:
                    oViewClass = erp.musr.view.SViewUserConfigurationTransaction.class;
                    sViewTitle = "Config. usuarios transac.";
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
                case SDataConstants.USRU_USR:
                    picker = moPickerUsers = SFormOptionPicker.createOptionPicker(miClient, optionType, moPickerUsers);
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
                menues = new JMenu[] { jmMenuUser };
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

            if (item == jmiUser) {
                showView(SDataConstants.USRU_USR);
            }
            else if (item == jmiRedisLocks) {
                showForm(SDataConstants.USRU_USR_REDIS_LOCKS, null);
            }
            else if (item == jmiRedisSessions) {
                showForm(SDataConstants.USRU_USR_REDIS, null);
            }
            else if (item == jmiUpdatedUser) {
                showForm(SDataConstants.USRU_USR_UPDATED, null);
            }
            else if (item == jmiAccessCompany) {
                showView(SDataConstants.USRU_ACCESS_CO);
            }
            else if (item == jmiAccessCompanyBranch) {
                showView(SDataConstants.USRU_ACCESS_COB);
            }
            else if (item == jmiAccessCompanyBranchEntity) {
                showView(SDataConstants.USRU_ACCESS_COB_ENT);
            }
            else if (item == jmiUserRight) {
                showView(SDataConstants.USRX_RIGHT);
            }
            else if (item == jmiUserRightRole) {
                showView(SDataConstants.USRX_RIGHT_ROL);
            }
            else if (item == jmiUserRightPrivilege) {
                showView(SDataConstants.USRX_RIGHT_PRV);
            }
            else if (item == jmiUserConfigurationTransaction) {
                showView(SDataConstants.TRN_USR_CFG);
            }
            else if (item == jmiUserFunctionalArea) {
                showView(SDataConstants.USRX_USR_FUNC);
            }
        }
    }
}
