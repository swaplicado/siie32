/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.print;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.awt.print.Printable;
import java.util.Vector;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.OrientationRequested;

import erp.data.SDataConstants;
import erp.data.SDataReadRegistries;
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.print.SPrintConstants;
import erp.lib.print.SPrintUtilities;
import erp.lib.print.SPrintableItem;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchBankAccount;
import erp.mcfg.data.SDataCurrency;
import erp.mfin.data.SDataCheck;
import erp.mfin.data.SDataCheckPrintingFormat;
import erp.mfin.data.SDataCheckWallet;
import erp.mfin.form.SDialogPrintPreviewCheck;

/**
 *
 * @author Alfonso Flores
 */
public class SPrintCheckBack extends erp.print.SAbstractPrintableDoc implements java.awt.print.Printable {
    
    private boolean mbIsPrintPreview;    
    private int mnPage;
    
    private erp.mfin.data.SDataCheck moCheck;    
    private erp.lib.print.SPrintableItem[] maiData;
    private erp.mfin.data.SDataCheckPrintingFormat moCheckFormat;
    
    public SPrintCheckBack(erp.client.SClientInterface client, int[] key, int marginLeft) {
        super(client);

        manKey = (int[]) key.clone();

        mnMarginTop = SPrintConstants.TAB_QUARTER;
        mnMarginBottom = SPrintConstants.TAB;
        mnMarginLeft = SPrintConstants.TAB_HALF + marginLeft;
        mnMarginRight = SPrintConstants.TAB_EIGHTH;

        mdViewWidth = mdPageWidth - mnMarginLeft - mnMarginRight;
        mdViewHeight = mdPageHeight - mnMarginTop - mnMarginBottom;
        
        mbIsPrintPreview = false;
        mnPages = 2;
        mnPage = 0;
    }

    @Override
    public boolean preparePrinting() {
        boolean isError = false;

        moCheck = (SDataCheck) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CHECK,
                manKey, SLibConstants.EXEC_MODE_SILENT);

        if (moCheck != null) {
            isError = true;
        }

        return isError;
    }

    @Override
    public void printDocument() {
        boolean bPrint = true;
        SDialogPrintPreviewCheck oPrintPreviewCheck = null;
        
        miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));

        try {
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(OrientationRequested.PORTRAIT);
            aset.add(new Copies(1));
            aset.add(new MediaPrintableArea((float) (mnMarginLeft / 72.0), (float) (mnMarginTop / 72.0), (float) (mdViewWidth / 72.0),
                    (float) (mdViewHeight / 72.0), MediaPrintableArea.INCH));
            aset.add(new JobName("Reverso cheque", null));

            // Create a print job:

            PrinterJob pj = PrinterJob.getPrinterJob();
            pj.setPrintable(this);

            // Obtain data to be printed:

            if (preparePrinting()) {
                obtainCheckFormatData();
                
                if (mbIsPrintPreview) {
                    oPrintPreviewCheck = new SDialogPrintPreviewCheck(miClient);
                    oPrintPreviewCheck.formReset();
                    oPrintPreviewCheck.formRefreshCatalogues();
                    oPrintPreviewCheck.setValue(1, true);
                    oPrintPreviewCheck.setValue(2, maiData);
                    oPrintPreviewCheck.setVisible(true);
                    
                    if (oPrintPreviewCheck.getFormResult() == erp.lib.SLibConstants.FORM_RESULT_CANCEL) {
                        bPrint = false;
                    }
                }
                
                if (bPrint) {
                    if (pj.printDialog(aset)) {
                        pj.print(aset);
                    }
                }
            }
        }
        catch (java.awt.print.PrinterException e) {
            SLibUtilities.renderException(this, e);
        }
        catch (java.lang.Exception e) {
            SLibUtilities.renderException(this, e);
        }
        finally {
            miClient.getFrame().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        int nResult = 0;
        int i = 0;
        int item = 0;
        SDataBizPartnerBranchBankAccount oBankAccount = null;        
        SDataCheckPrintingFormat oCheckFormat = null;
        Vector<Object> params = new Vector<Object>();

        oBankAccount = (SDataBizPartnerBranchBankAccount)((Vector) SDataReadRegistries.readRegistries(
                miClient, SDataConstants.BPSX_BANK_ACC_CHECK, moCheck.getPrimaryKey())).get(0);

        if (oBankAccount != null) {
            params.clear();
            params.add(moCheck.getPkCheckWalletId());
            params.add(moCheck.getPkCheckId());
            params = SDataUtilities.callProcedure(miClient, SProcConstants.FIN_GET_ID_CHECK_FMT, params, SLibConstants.EXEC_MODE_SILENT);

            oCheckFormat = (SDataCheckPrintingFormat) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_CHECK_FMT,
                new int[] { (Integer) params.get(0) }, SLibConstants.EXEC_MODE_SILENT);

            if (oCheckFormat != null) {
                Graphics2D g2D = (Graphics2D) graphics;
                g2D.translate(mnMarginLeft, mnMarginTop);
                
                if (maiData != null) {                    
                    if (mnPage < mnPages) {
                        for (i = 0; i < maiData.length; i++) {
                            if (maiData[i] != null) {
                                SPrintUtilities.printPrintableItem(maiData[i], g2D, false);
                            }
                        }

                        mnPage += 1;
                        nResult = Printable.PAGE_EXISTS;
                    }
                    else {
                        nResult = Printable.NO_SUCH_PAGE;
                    }
                }
            }
        }
        else {
            nResult = Printable.NO_SUCH_PAGE;
        }

        return nResult;
    }
    
    public SPrintableItem[] obtainCheckFormatData() {
        int item = 0;
        Vector<Object> params = new Vector<Object>();
        
        SDataBizPartnerBranchBankAccount oBankAccount = null;        
        SDataCheckPrintingFormat oCheckFormat = null;
        
        oBankAccount = (SDataBizPartnerBranchBankAccount)((Vector) SDataReadRegistries.readRegistries(
                miClient, SDataConstants.BPSX_BANK_ACC_CHECK, moCheck.getPrimaryKey())).get(0);

        if (oBankAccount != null) {
            params.clear();
            params.add(moCheck.getPkCheckWalletId());
            params.add(moCheck.getPkCheckId());
            params = SDataUtilities.callProcedure(miClient, SProcConstants.FIN_GET_ID_CHECK_FMT, params, SLibConstants.EXEC_MODE_SILENT);

            oCheckFormat = (SDataCheckPrintingFormat) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_CHECK_FMT,
                new int[] { (Integer) params.get(0) }, SLibConstants.EXEC_MODE_SILENT);

            if (oCheckFormat != null) {
                item = 0;
                maiData = new SPrintableItem[7];

                maiData[item] = new SPrintableItem(SLibUtilities.textToAscii(moCheck.getBeneficiary()), SPrintConstants.fontText);
                maiData[item].y = ((Double) (oCheckFormat.getBackY() * 72)).intValue();
                maiData[item].x = ((Double) (oCheckFormat.getBackX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB * 4 - SPrintConstants.TAB_QUARTER;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;

                if (moCheck.getIsForBeneficiaryAccount()) {
                    maiData[++item] = new SPrintableItem(SLibConstants.TXT_BENEF_ACC_DEP, SPrintConstants.fontText);
                    maiData[item].y = maiData[item -1].y + SPrintConstants.TAB_EIGHTH;
                    maiData[item].x = ((Double) (oCheckFormat.getBackX() * 72)).intValue();
                    maiData[item].width = SPrintConstants.TAB * 2 + SPrintConstants.TAB_HALF;
                    maiData[item].align = SPrintConstants.ALIGN_LEFT;
                }

                maiData[++item] = new SPrintableItem("BANCO: " + oBankAccount.getDbmsBank(), SPrintConstants.fontText);
                maiData[item].y = maiData[item -1].y + SPrintConstants.TAB_EIGHTH;
                maiData[item].x = ((Double) (oCheckFormat.getBackX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB * 2;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;

                maiData[++item] = new SPrintableItem("SUC.: " + oBankAccount.getBankAccountBranchNumber(), SPrintConstants.fontText);
                maiData[item].y = maiData[item -1].y + SPrintConstants.TAB_EIGHTH;
                maiData[item].x = ((Double) (oCheckFormat.getBackX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB * 2;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;

                maiData[++item] = new SPrintableItem("CTA.: " + oBankAccount.getBankAccountNumber(), SPrintConstants.fontText);
                maiData[item].y = maiData[item -1].y + SPrintConstants.TAB_EIGHTH;
                maiData[item].x = ((Double) (oCheckFormat.getBackX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB * 2;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;

                maiData[++item] = new SPrintableItem(oBankAccount.getAgree(), SPrintConstants.fontText);
                maiData[item].y = maiData[item -1].y + SPrintConstants.TAB_EIGHTH;
                maiData[item].x = ((Double) (oCheckFormat.getBackX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB * 2;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;

                maiData[++item] = new SPrintableItem((oBankAccount.getReference().length() == 0 ? "" : "REF.: " + oBankAccount.getReference()), SPrintConstants.fontText);
                maiData[item].y = maiData[item -1].y + SPrintConstants.TAB_EIGHTH;
                maiData[item].x = ((Double) (oCheckFormat.getBackX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB * 2;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;
            }
        }
        
        return maiData;
    }
    
    public void setPrintPreview(boolean b) { mbIsPrintPreview = b; }
}
