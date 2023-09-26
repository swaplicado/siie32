
package erp.mtrn.form;

import erp.lib.SLibUtilities;
import erp.mod.trn.db.SDbMaterialRequestEntry;
import erp.mod.trn.db.SMaterialRequestUtils;
import javax.swing.border.TitledBorder;

/**
 *
 * @author Edwin Carmona
 */
public class SPanelMaterialRequestEntry extends javax.swing.JPanel {

    private erp.client.SClientInterface miClient;
    private java.lang.String msTitledBorderComplement;
    private SDbMaterialRequestEntry moMaterialRequestEty;
    private int[] maDpsPk;
    private int[] maDpsType;
    private double mdQuantity;

    /** Creates new form SPanelDps */
    public SPanelMaterialRequestEntry(erp.client.SClientInterface client) {
        this(client, "");
    }

    /** Creates new form SPanelDps */
    public SPanelMaterialRequestEntry(erp.client.SClientInterface client, java.lang.String titledBorderComplement) {
        miClient = client;
        msTitledBorderComplement = titledBorderComplement;

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

        jpDps = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jlConcept = new javax.swing.JLabel();
        jtfConcept = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        jlUnit = new javax.swing.JLabel();
        jtfUnit = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jlConsumeEntity = new javax.swing.JLabel();
        jtfConsumeEntity = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        jlConsumeEntity1 = new javax.swing.JLabel();
        jtfConsumeEntity1 = new javax.swing.JTextField();
        jPanel17 = new javax.swing.JPanel();
        jlConsumeEntity2 = new javax.swing.JLabel();
        jtfConsumeEntity2 = new javax.swing.JTextField();
        jpDpsValue = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        jlSubtotalCy_r = new javax.swing.JLabel();
        jtfQuantityReqEty = new javax.swing.JTextField();
        jtfUnitSymbol = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        jlQuantitySupplied = new javax.swing.JLabel();
        jtfQuantitySupplied = new javax.swing.JTextField();
        jtfUnitSymbol4 = new javax.swing.JTextField();
        jPanel16 = new javax.swing.JPanel();
        jlTaxRetainedCy_r1 = new javax.swing.JLabel();
        jtfQuantityLinkedReqEty = new javax.swing.JTextField();
        jtfUnitSymbol1 = new javax.swing.JTextField();
        jPanel14 = new javax.swing.JPanel();
        jlTaxRetainedCy_r = new javax.swing.JLabel();
        jtfQuantityCurrentReqEty = new javax.swing.JTextField();
        jtfUnitSymbol2 = new javax.swing.JTextField();
        jPanel9 = new javax.swing.JPanel();
        jlTotalCy_r = new javax.swing.JLabel();
        jtfQuantityPendingReqEty = new javax.swing.JTextField();
        jtfUnitSymbol3 = new javax.swing.JTextField();

        setLayout(new java.awt.BorderLayout());

        jpDps.setBorder(javax.swing.BorderFactory.createTitledBorder("Datos de la partida:"));
        jpDps.setLayout(new java.awt.GridLayout(7, 1, 0, 1));

        jPanel6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlConcept.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlConcept.setText("Concepto:");
        jlConcept.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel6.add(jlConcept);

        jtfConcept.setEditable(false);
        jtfConcept.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfConcept.setText("DOCUMENT TYPE");
        jtfConcept.setFocusable(false);
        jtfConcept.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel6.add(jtfConcept);

        jpDps.add(jPanel6);

        jPanel18.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlUnit.setText("Unidad:");
        jlUnit.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel18.add(jlUnit);

        jtfUnit.setEditable(false);
        jtfUnit.setText("UNIT - SYMBOL");
        jtfUnit.setFocusable(false);
        jtfUnit.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel18.add(jtfUnit);

        jpDps.add(jPanel18);

        jPanel12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlConsumeEntity.setText("Centro consumo:");
        jlConsumeEntity.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel12.add(jlConsumeEntity);

        jtfConsumeEntity.setEditable(false);
        jtfConsumeEntity.setText("CONSUME ENTITY");
        jtfConsumeEntity.setFocusable(false);
        jtfConsumeEntity.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel12.add(jtfConsumeEntity);

        jpDps.add(jPanel12);

        jPanel15.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlConsumeEntity1.setText("Subcentro consumo:");
        jlConsumeEntity1.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel15.add(jlConsumeEntity1);

        jtfConsumeEntity1.setEditable(false);
        jtfConsumeEntity1.setText("CONSUME ENTITY");
        jtfConsumeEntity1.setFocusable(false);
        jtfConsumeEntity1.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel15.add(jtfConsumeEntity1);

        jpDps.add(jPanel15);

        jPanel17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlConsumeEntity2.setText("Centro de costo:");
        jlConsumeEntity2.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel17.add(jlConsumeEntity2);

        jtfConsumeEntity2.setEditable(false);
        jtfConsumeEntity2.setText("CONSUME ENTITY");
        jtfConsumeEntity2.setFocusable(false);
        jtfConsumeEntity2.setPreferredSize(new java.awt.Dimension(250, 23));
        jPanel17.add(jtfConsumeEntity2);

        jpDps.add(jPanel17);

        add(jpDps, java.awt.BorderLayout.WEST);

        jpDpsValue.setBorder(javax.swing.BorderFactory.createTitledBorder("Vínculo:"));
        jpDpsValue.setPreferredSize(new java.awt.Dimension(300, 190));
        jpDpsValue.setLayout(new java.awt.GridLayout(7, 1, 0, 1));

        jPanel19.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlSubtotalCy_r.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlSubtotalCy_r.setText("Cantidad requisición:");
        jlSubtotalCy_r.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel19.add(jlSubtotalCy_r);

        jtfQuantityReqEty.setEditable(false);
        jtfQuantityReqEty.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfQuantityReqEty.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantityReqEty.setText("000.000");
        jtfQuantityReqEty.setFocusable(false);
        jtfQuantityReqEty.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel19.add(jtfQuantityReqEty);

        jtfUnitSymbol.setEditable(false);
        jtfUnitSymbol.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfUnitSymbol.setText("UN");
        jtfUnitSymbol.setFocusable(false);
        jtfUnitSymbol.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel19.add(jtfUnitSymbol);

        jpDpsValue.add(jPanel19);

        jPanel20.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlQuantitySupplied.setText("Cantidad surtida:");
        jlQuantitySupplied.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel20.add(jlQuantitySupplied);

        jtfQuantitySupplied.setEditable(false);
        jtfQuantitySupplied.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantitySupplied.setText("000.000");
        jtfQuantitySupplied.setFocusable(false);
        jtfQuantitySupplied.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel20.add(jtfQuantitySupplied);

        jtfUnitSymbol4.setEditable(false);
        jtfUnitSymbol4.setText("UN");
        jtfUnitSymbol4.setFocusable(false);
        jtfUnitSymbol4.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel20.add(jtfUnitSymbol4);

        jpDpsValue.add(jPanel20);

        jPanel16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlTaxRetainedCy_r1.setText("Cantidad vinculada:");
        jlTaxRetainedCy_r1.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel16.add(jlTaxRetainedCy_r1);

        jtfQuantityLinkedReqEty.setEditable(false);
        jtfQuantityLinkedReqEty.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantityLinkedReqEty.setText("000.000");
        jtfQuantityLinkedReqEty.setFocusable(false);
        jtfQuantityLinkedReqEty.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel16.add(jtfQuantityLinkedReqEty);

        jtfUnitSymbol1.setEditable(false);
        jtfUnitSymbol1.setText("UN");
        jtfUnitSymbol1.setFocusable(false);
        jtfUnitSymbol1.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel16.add(jtfUnitSymbol1);

        jpDpsValue.add(jPanel16);

        jPanel14.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlTaxRetainedCy_r.setText("Cantidad partida:");
        jlTaxRetainedCy_r.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel14.add(jlTaxRetainedCy_r);

        jtfQuantityCurrentReqEty.setEditable(false);
        jtfQuantityCurrentReqEty.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantityCurrentReqEty.setText("000.000");
        jtfQuantityCurrentReqEty.setFocusable(false);
        jtfQuantityCurrentReqEty.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel14.add(jtfQuantityCurrentReqEty);

        jtfUnitSymbol2.setEditable(false);
        jtfUnitSymbol2.setText("UN");
        jtfUnitSymbol2.setFocusable(false);
        jtfUnitSymbol2.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel14.add(jtfUnitSymbol2);

        jpDpsValue.add(jPanel14);

        jPanel9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 0));

        jlTotalCy_r.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jlTotalCy_r.setText("Por vincular:");
        jlTotalCy_r.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jlTotalCy_r);

        jtfQuantityPendingReqEty.setEditable(false);
        jtfQuantityPendingReqEty.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfQuantityPendingReqEty.setHorizontalAlignment(javax.swing.JTextField.TRAILING);
        jtfQuantityPendingReqEty.setText("000.000");
        jtfQuantityPendingReqEty.setFocusable(false);
        jtfQuantityPendingReqEty.setPreferredSize(new java.awt.Dimension(125, 23));
        jPanel9.add(jtfQuantityPendingReqEty);

        jtfUnitSymbol3.setEditable(false);
        jtfUnitSymbol3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jtfUnitSymbol3.setText("UN");
        jtfUnitSymbol3.setFocusable(false);
        jtfUnitSymbol3.setPreferredSize(new java.awt.Dimension(50, 23));
        jPanel9.add(jtfUnitSymbol3);

        jpDpsValue.add(jPanel9);

        add(jpDpsValue, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void initComponentsExtra() {
        setDialogData(null, null, null, 0d);

        if (msTitledBorderComplement.length() > 0) {
            ((TitledBorder) jpDps.getBorder()).setTitle(SLibUtilities.textTrim("Datos del documento " + msTitledBorderComplement) + ":");
            ((TitledBorder) jpDpsValue.getBorder()).setTitle(SLibUtilities.textTrim("Valor del documento " + msTitledBorderComplement) + ":");
        }
    }

    private void renderMaterialRequestEntry() {
        if (moMaterialRequestEty != null) {
            jtfConcept.setText(moMaterialRequestEty.getDataItem().getNameShort().isEmpty() ? moMaterialRequestEty.getDataItem().getName() : moMaterialRequestEty.getDataItem().getNameShort());
            jtfConcept.setToolTipText(jtfConcept.getText());
            jtfConcept.setCaretPosition(0);
            
            jtfUnit.setText(moMaterialRequestEty.getDataItem().getDbmsDataUnit().getUnit());
            
            if (moMaterialRequestEty.getFkSubentMatConsumptionSubentityId_n() > 0) {
                jtfConsumeEntity.setText(moMaterialRequestEty.getConsumptionEntityInfo());
                jtfConsumeEntity1.setText(moMaterialRequestEty.getConsumptionSubentityInfo());
                jtfConsumeEntity2.setText(moMaterialRequestEty.getConsumptionSubentityInfo());
            }
            else {
                jtfConsumeEntity.setText("");
                jtfConsumeEntity1.setText("");
                jtfConsumeEntity2.setText("");
            }
            
            jtfQuantityReqEty.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(moMaterialRequestEty.getQuantity()));
            double dSupplied = SMaterialRequestUtils.getQuantitySupplied(miClient.getSession(), moMaterialRequestEty.getPrimaryKey());
            jtfQuantitySupplied.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(dSupplied));
            double dLinked = SMaterialRequestUtils.getQuantityLinkedOfReqEty(miClient.getSession(), 
                                                                            moMaterialRequestEty.getPrimaryKey(), 
                                                                            maDpsType, 
                                                                            this.maDpsPk == null ? null : maDpsPk);
            jtfQuantityLinkedReqEty.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(dLinked));
            jtfQuantityCurrentReqEty.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(mdQuantity));
            double dPending = moMaterialRequestEty.getQuantity() - dSupplied - dLinked - mdQuantity;
            jtfQuantityPendingReqEty.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(dPending < 0d ? 0d : dPending));
            
            jtfUnitSymbol.setText(moMaterialRequestEty.getDataItem().getDbmsDataUnit().getSymbol());
            jtfUnitSymbol1.setText(jtfUnitSymbol.getText());
            jtfUnitSymbol2.setText(jtfUnitSymbol.getText());
            jtfUnitSymbol3.setText(jtfUnitSymbol.getText());
        }
        else {
            jtfConcept.setText("");
            jtfConcept.setToolTipText(jtfConcept.getText());
            jtfConcept.setCaretPosition(0);
            
            jtfUnit.setText("");

            jtfQuantityReqEty.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(0d));
            jtfQuantitySupplied.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(0d));
            jtfQuantityLinkedReqEty.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(0d));
            jtfQuantityCurrentReqEty.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(0d));
            jtfQuantityPendingReqEty.setText(miClient.getSessionXXX().getFormatters().getDecimalsQuantityFormat().format(0d));

            jtfUnitSymbol.setText("");
            jtfUnitSymbol1.setText(jtfUnitSymbol.getText());
            jtfUnitSymbol2.setText(jtfUnitSymbol.getText());
            jtfUnitSymbol3.setText(jtfUnitSymbol.getText());
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JLabel jlConcept;
    private javax.swing.JLabel jlConsumeEntity;
    private javax.swing.JLabel jlConsumeEntity1;
    private javax.swing.JLabel jlConsumeEntity2;
    private javax.swing.JLabel jlQuantitySupplied;
    private javax.swing.JLabel jlSubtotalCy_r;
    private javax.swing.JLabel jlTaxRetainedCy_r;
    private javax.swing.JLabel jlTaxRetainedCy_r1;
    private javax.swing.JLabel jlTotalCy_r;
    private javax.swing.JLabel jlUnit;
    private javax.swing.JPanel jpDps;
    private javax.swing.JPanel jpDpsValue;
    private javax.swing.JTextField jtfConcept;
    private javax.swing.JTextField jtfConsumeEntity;
    private javax.swing.JTextField jtfConsumeEntity1;
    private javax.swing.JTextField jtfConsumeEntity2;
    private javax.swing.JTextField jtfQuantityCurrentReqEty;
    private javax.swing.JTextField jtfQuantityLinkedReqEty;
    private javax.swing.JTextField jtfQuantityPendingReqEty;
    private javax.swing.JTextField jtfQuantityReqEty;
    private javax.swing.JTextField jtfQuantitySupplied;
    private javax.swing.JTextField jtfUnit;
    private javax.swing.JTextField jtfUnitSymbol;
    private javax.swing.JTextField jtfUnitSymbol1;
    private javax.swing.JTextField jtfUnitSymbol2;
    private javax.swing.JTextField jtfUnitSymbol3;
    private javax.swing.JTextField jtfUnitSymbol4;
    // End of variables declaration//GEN-END:variables

    public void setDialogData(SDbMaterialRequestEntry matRequestEty, int[] dpsPk, int[] dpsType, double quantity) {
        moMaterialRequestEty = matRequestEty;
        maDpsPk = dpsPk;
        maDpsType = dpsType;
        renderMaterialRequestEntry();
    }
}
