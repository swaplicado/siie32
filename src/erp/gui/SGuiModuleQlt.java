/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.gui;

import erp.data.SDataConstants;
import erp.data.SDataConstantsSys;
import erp.mod.SModConsts;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import sa.lib.SLibConsts;

/**
 *
 * @author Uriel Castañeda
 */
public class SGuiModuleQlt extends erp.lib.gui.SGuiModule implements java.awt.event.ActionListener {

    private javax.swing.JMenu jmQlt;
    private javax.swing.JMenuItem jmiQltLot;
    
    public SGuiModuleQlt(erp.client.SClientInterface client) {
        super(client, SDataConstants.MOD_QLT);
        initComponents();
    }

    private void initComponents() {
        jmQlt = new JMenu("Calidad");
        jmiQltLot = new JMenuItem("Lotes aprobados");
        
        jmQlt.add(jmiQltLot);

        jmiQltLot.setEnabled(true);

        jmiQltLot.addActionListener(this);
        
        jmQlt.setEnabled(miClient.getSessionXXX().getUser().hasRight(miClient, SDataConstantsSys.PRV_QLT_LOT_APR).HasRight);
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
        throw new UnsupportedOperationException("Not supported yet.");
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
        return new JMenu[] { jmQlt };
    }

    @Override
    public javax.swing.JMenu[] getMenuesForModule(int moduleType) {
        return null;
    }

    @Override
    public void actionPerformed(java.awt.event.ActionEvent e) {
        if (e.getSource() instanceof javax.swing.JMenuItem) {
            javax.swing.JMenuItem item = (javax.swing.JMenuItem) e.getSource();

            if (item == jmiQltLot) {
                miClient.getSession().showView(SModConsts.QLT_LOT_APR, SLibConsts.UNDEFINED, null);
            }       
        }
    }
}
