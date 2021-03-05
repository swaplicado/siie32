/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.table;

import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import javax.swing.JLabel;

/**
 *
 * @author Sergio Flores, Isabel Servín
 */
public class STableCellRendererIcon extends javax.swing.table.DefaultTableCellRenderer {

    private javax.swing.JLabel moLabel;
    public static final javax.swing.ImageIcon moIconNull = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_null.png"));
    public static final javax.swing.ImageIcon moIconStatusAnnul = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_st_annul.png"));
    public static final javax.swing.ImageIcon moIconStatusThumbsDown = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_st_thumbs_down.png"));
    public static final javax.swing.ImageIcon moIconStatusThumbsUp = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_st_thumbs_up.png"));
    public static final javax.swing.ImageIcon moIconStatusWait = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_st_wait.png"));
    public static final javax.swing.ImageIcon moIconWarn = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_warn.png"));
    public static final javax.swing.ImageIcon moIconXml = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_xml.png"));
    public static final javax.swing.ImageIcon moIconXmlPend = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_xml_pend.png"));
    public static final javax.swing.ImageIcon moIconXmlSign = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_xml_sign.png"));
    public static final javax.swing.ImageIcon moIconPdf = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_xml_pdf.png"));
    public static final javax.swing.ImageIcon moIconXmlPdf = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_xml_n_pdf.png"));
    public static final javax.swing.ImageIcon moIconXmlCancel = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_xml_canc.png"));
    public static final javax.swing.ImageIcon moIconXmlCancelPdf = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_xml_canc_pdf.png"));
    public static final javax.swing.ImageIcon moIconXmlCancelXml = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_xml_canc_xml.png"));
    public static final javax.swing.ImageIcon moIconMfgStatus01 = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_mfg_st_01.png"));
    public static final javax.swing.ImageIcon moIconMfgStatus02 = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_mfg_st_02.png"));
    public static final javax.swing.ImageIcon moIconMfgStatus03 = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_mfg_st_03.png"));
    public static final javax.swing.ImageIcon moIconMfgStatus04 = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_mfg_st_04.png"));
    public static final javax.swing.ImageIcon moIconMfgStatus05 = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_mfg_st_05.png"));
    public static final javax.swing.ImageIcon moIconMfgStatus06 = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_mfg_st_06.png"));
    public static final javax.swing.ImageIcon moIconViewLigGre = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_lig_gre.png"));
    public static final javax.swing.ImageIcon moIconViewLigRed = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_lig_red.png"));
    public static final javax.swing.ImageIcon moIconViewLigWhi = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_lig_whi.png"));
    public static final javax.swing.ImageIcon moIconViewLigYel = new javax.swing.ImageIcon(new Object().getClass().getResource("/erp/img/icon_view_lig_yel.png"));

    public STableCellRendererIcon() {
        moLabel = new JLabel();
        moLabel.setOpaque(true);
        moLabel.setHorizontalAlignment(JLabel.CENTER);
    }

    public void setLabel(javax.swing.JLabel o) { moLabel = o; }

    public javax.swing.JLabel getLabel() { return moLabel; }

    @Override
    public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int col) {
        int icon = SLibConstants.UNDEFINED;

        try {
            icon = value == null ? STableConstants.ICON_NULL : ((Number) value).intValue();
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }

        switch (icon) {
            case STableConstants.ICON_ST_ANNUL:
                moLabel.setIcon(moIconStatusAnnul);
                break;
            case STableConstants.ICON_ST_THUMBS_DOWN:
                moLabel.setIcon(moIconStatusThumbsDown);
                break;
            case STableConstants.ICON_ST_THUMBS_UP:
                moLabel.setIcon(moIconStatusThumbsUp);
                break;
            case STableConstants.ICON_ST_WAIT:
                moLabel.setIcon(moIconStatusWait);
                break;
            case STableConstants.ICON_WARN:
                moLabel.setIcon(moIconWarn);
                break;
            case STableConstants.ICON_XML:
                moLabel.setIcon(moIconXml);
                break;
            case STableConstants.ICON_XML_PEND:
                moLabel.setIcon(moIconXmlPend);
                break;
            case STableConstants.ICON_XML_SIGN:
                moLabel.setIcon(moIconXmlSign);
                break;
            case STableConstants.ICON_PDF:
                moLabel.setIcon(moIconPdf);
                break;
            case STableConstants.ICON_XML_PDF:
                moLabel.setIcon(moIconXmlPdf);
                break;
            case STableConstants.ICON_XML_CANC:
                moLabel.setIcon(moIconXmlCancel);
                break;
            case STableConstants.ICON_XML_CANC_PDF:
                moLabel.setIcon(moIconXmlCancelPdf);
                break;
            case STableConstants.ICON_XML_CANC_XML:
                moLabel.setIcon(moIconXmlCancelXml);
                break;
            case STableConstants.ICON_MFG_ST_01:
                moLabel.setIcon(moIconMfgStatus01);
                break;
            case STableConstants.ICON_MFG_ST_02:
                moLabel.setIcon(moIconMfgStatus02);
                break;
            case STableConstants.ICON_MFG_ST_03:
                moLabel.setIcon(moIconMfgStatus03);
                break;
            case STableConstants.ICON_MFG_ST_04:
                moLabel.setIcon(moIconMfgStatus04);
                break;
            case STableConstants.ICON_MFG_ST_05:
                moLabel.setIcon(moIconMfgStatus05);
                break;
            case STableConstants.ICON_MFG_ST_06:
                moLabel.setIcon(moIconMfgStatus06);
                break;
            case STableConstants.ICON_VIEW_LIG_GRE:
                moLabel.setIcon(moIconViewLigGre);
                break;
            case STableConstants.ICON_VIEW_LIG_RED:
                moLabel.setIcon(moIconViewLigRed);
                break;
            case STableConstants.ICON_VIEW_LIG_WHI:
                moLabel.setIcon(moIconViewLigWhi);
                break;
            case STableConstants.ICON_VIEW_LIG_YEL:
                moLabel.setIcon(moIconViewLigYel);
                break;
            default:
                moLabel.setIcon(moIconNull);
        }

        if (isSelected) {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_EDIT_FOCUS : STableConstants.BACKGROUND_SELECT_EDIT);
            }
            else {
                moLabel.setForeground(STableConstants.FOREGROUND_READ);
                moLabel.setBackground(hasFocus ? STableConstants.BACKGROUND_SELECT_READ_FOCUS : STableConstants.BACKGROUND_SELECT_READ);
            }
        }
        else {
            if (table.isCellEditable(row, col)) {
                moLabel.setForeground(STableConstants.FOREGROUND_EDIT);
                moLabel.setBackground(STableConstants.BACKGROUND_PLAIN_EDIT);
            }
            else {
                moLabel.setForeground(STableConstants.FOREGROUND_READ);
                moLabel.setBackground(STableConstants.BACKGROUND_PLAIN_READ);
            }
        }

        return moLabel;
    }
}
