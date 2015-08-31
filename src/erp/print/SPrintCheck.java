/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.print;

import java.awt.Cursor;
import java.awt.ComponentOrientation;
import java.awt.geom.AffineTransform;
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
import erp.data.SDataUtilities;
import erp.data.SProcConstants;
import erp.lib.SLibConstants;
import erp.lib.SLibUtilities;
import erp.lib.print.SPrintConstants;
import erp.lib.print.SPrintUtilities;
import erp.lib.print.SPrintableItem;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mcfg.data.SDataCurrency;
import erp.mfin.data.SDataCheck;
import erp.mfin.data.SDataCheckPrintingFormat;
import erp.mfin.data.SDataCheckWallet;
import erp.mfin.form.SDialogPrintPreviewCheck;

/**
 *
 * @author Alfonso Flores
 */
public class SPrintCheck extends erp.print.SAbstractPrintableDoc implements java.awt.print.Printable {

    private boolean mbIsPrintPreview;
    private int mnPage;

    private erp.mfin.data.SDataCheck moCheck;
    private erp.lib.print.SPrintableItem[] maiData;
    private erp.mfin.data.SDataCheckPrintingFormat moCheckFormat;

    public SPrintCheck(erp.client.SClientInterface client, int[] key) {
        super(client);

        manKey = (int[]) key.clone();

        mnMarginTop = SPrintConstants.TAB_QUARTER;
        mnMarginBottom = SPrintConstants.TAB;
        mnMarginLeft = SPrintConstants.TAB_EIGHTH;
        mnMarginRight = SPrintConstants.TAB_EIGHTH;

        mdViewWidth = mdPageWidth - mnMarginLeft - mnMarginRight;
        mdViewHeight = mdPageHeight - mnMarginTop - mnMarginBottom;

        mbIsPrintPreview = false;
        mnPages = 2;
        mnPage = 0;

        maiData = null;
        moCheckFormat = null;
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
        miClient.getFrame().setCursor(new Cursor(Cursor.WAIT_CURSOR));

        SDialogPrintPreviewCheck oPrintPreviewCheck = null;

        try {
            PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
            aset.add(OrientationRequested.PORTRAIT);
            aset.add(new Copies(1));
            aset.add(new MediaPrintableArea((float) (mnMarginLeft / 72.0), (float) (mnMarginTop / 72.0), (float) (mdViewWidth / 72.0),
                    (float) (mdViewHeight / 72.0), MediaPrintableArea.INCH));
            aset.add(new JobName("Cheque", null));

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
                    oPrintPreviewCheck.setValue(1, false);
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

    public SPrintableItem[] obtainCheckFormatData() throws Exception {
        int item = 0;
        Vector<Object> params = new Vector<Object>();

        SDataCheckWallet oWallet = null;
        SDataBizPartnerBranch oBranch = null;
        SDataCurrency oCurrency = null;

        params.clear();

        params.add(moCheck.getPkCheckWalletId());
        params.add(moCheck.getPkCheckId());
        params = SDataUtilities.callProcedure(miClient, SProcConstants.FIN_GET_ID_CHECK_FMT, params, SLibConstants.EXEC_MODE_SILENT);

        if (params.isEmpty() || (Integer) params.get(0) == 0) {
            maiData = null;
            throw new Exception("No se encontro el formato del cheque.");
        }
        else {
            moCheckFormat = (SDataCheckPrintingFormat) SDataUtilities.readRegistry(miClient, SDataConstants.FINU_CHECK_FMT,
                    new int[] { (Integer) params.get(0) }, SLibConstants.EXEC_MODE_SILENT);

            if (moCheckFormat != null) {
                oCurrency = (SDataCurrency) SDataUtilities.readRegistry(miClient, SDataConstants.CFGU_CUR,
                    new int[] { moCheck.getAuxCurrencyId() }, SLibConstants.EXEC_MODE_SILENT);

                item = 0;
                maiData = new SPrintableItem[8];

                maiData[item] = new SPrintableItem(miClient.getSessionXXX().getFormatters().getDateTextFormat().format(moCheck.getDate()).toUpperCase(),
                        SPrintConstants.fontText);
                maiData[item].y = ((Double) (moCheckFormat.getDateY() * 72)).intValue();
                maiData[item].x = ((Double) (moCheckFormat.getDateX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB * 2 - SPrintConstants.TAB_QUARTER;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;

                maiData[++item] = new SPrintableItem(SLibUtilities.textToAscii(moCheck.getBeneficiary()), SPrintConstants.fontText);
                maiData[item].y = ((Double) (moCheckFormat.getBeneficiaryY() * 72)).intValue();
                maiData[item].x = ((Double) (moCheckFormat.getBeneficiaryX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB * 4 - SPrintConstants.TAB_QUARTER;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;

                maiData[++item] = new SPrintableItem(miClient.getSessionXXX().getFormatters().getNumberDoubleFormat().format(moCheck.getValue()),
                        SPrintConstants.fontBodyNumberArial);
                maiData[item].y = ((Double) (moCheckFormat.getValueY() * 72)).intValue();
                maiData[item].x = ((Double) (moCheckFormat.getValueX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB;
                maiData[item].align = SPrintConstants.ALIGN_RIGHT;

                maiData[++item] = new SPrintableItem(SLibUtilities.textToAscii(SLibUtilities.translateValueToText(moCheck.getValue(), 2,
                    oCurrency.getPkCurrencyId() == miClient.getSessionXXX().getParamsErp().getFkCurrencyId()? SLibConstants.LAN_SPANISH : SLibConstants.LAN_ENGLISH,
                    oCurrency.getTextSingular(), oCurrency.getTextPlural(), oCurrency.getTextPrefix(), oCurrency.getTextSuffix())), SPrintConstants.fontText);
                maiData[item].y = ((Double) (moCheckFormat.getValueTextY() * 72)).intValue();
                maiData[item].x = ((Double) (moCheckFormat.getValueTextX() * 72)).intValue();
                maiData[item].width = SPrintConstants.TAB * 7 - SPrintConstants.TAB_QUARTER;
                maiData[item].align = SPrintConstants.ALIGN_LEFT;

                if (moCheck.getIsForBeneficiaryAccount()) {
                    maiData[++item] = new SPrintableItem(SLibConstants.TXT_BENEF_ACC_DEP, SPrintConstants.fontText);
                    maiData[item].y = ((Double) (moCheckFormat.getBeneficiaryAccountY() * 72)).intValue();
                    maiData[item].x = ((Double) (moCheckFormat.getBeneficiaryAccountX() * 72)).intValue();
                    maiData[item].width = SPrintConstants.TAB * 2 + SPrintConstants.TAB_HALF;
                    maiData[item].align = SPrintConstants.ALIGN_LEFT;
                }

                if (moCheckFormat.getIsIssueLocalityApplying()) {
                    oWallet = (SDataCheckWallet) SDataUtilities.readRegistry(miClient, SDataConstants.FIN_CHECK_WAL,
                        new int[] { moCheck.getPkCheckWalletId() }, SLibConstants.EXEC_MODE_SILENT);
                    oBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB,
                        new int[] { oWallet.getFkCompanyBranchId() }, SLibConstants.EXEC_MODE_SILENT);

                    maiData[++item] = new SPrintableItem(oBranch.getDbmsBizPartnerBranchAddressOfficial().getLocality() + ", " +
                        oBranch.getDbmsBizPartnerBranchAddressOfficial().getCounty(),
                        SPrintConstants.fontText);
                    maiData[item].y = ((Double) (moCheckFormat.getIssueLocalityY() * 72)).intValue();
                    maiData[item].x = ((Double) (moCheckFormat.getIssueLocalityX() * 72)).intValue();
                    maiData[item].width = SPrintConstants.TAB * 2 - SPrintConstants.TAB_QUARTER;
                    maiData[item].align = SPrintConstants.ALIGN_LEFT;
                }

                if (moCheckFormat.getIsRecordNumberApplying()) {
                    params.clear();
                    params.add(moCheck.getPkCheckWalletId());
                    params.add(moCheck.getPkCheckId());
                    params = SDataUtilities.callProcedure(miClient, SProcConstants.FIN_GET_CHECK_NUM_REC, params, SLibConstants.EXEC_MODE_SILENT);

                    if (params.get(0) == null) {
                        miClient.showMsgBoxWarning("No se encontro la póliza contable.");
                    }
                    else {
                        maiData[++item] = new SPrintableItem((String) params.get(0), SPrintConstants.fontText);
                        maiData[item].y = ((Double) (moCheckFormat.getRecordNumberY() * 72)).intValue();
                        maiData[item].x = ((Double) (moCheckFormat.getRecordNumberX() * 72)).intValue();
                        maiData[item].width = SPrintConstants.TAB_HALF;
                        maiData[item].align = SPrintConstants.ALIGN_LEFT;
                    }
                }

                if (moCheckFormat.getIsCheckNumberApplying()) {
                    maiData[++item] = new SPrintableItem("C" + moCheck.getNumber(), SPrintConstants.fontText);
                    maiData[item].y = ((Double) (moCheckFormat.getCheckNumberY() * 72)).intValue();
                    maiData[item].x = ((Double) (moCheckFormat.getCheckNumberX() * 72)).intValue();
                    maiData[item].width = SPrintConstants.TAB_HALF;
                    maiData[item].align = SPrintConstants.ALIGN_LEFT;
                }
            }
        }

        return maiData;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        int i = 0;
        int nResult = 0;
        AffineTransform at = null;

        if (maiData != null) {
            Graphics2D g2D = (Graphics2D) graphics;
            g2D.translate(mnMarginLeft, mnMarginTop);

            if (mnPage < mnPages) {
                for (i = 0; i < maiData.length; i++) {
                    if (maiData[i] != null) {
                        SPrintUtilities.printPrintableItem(maiData[i], g2D, moCheckFormat.getIsRotate());
                    }
                }

                mnPage += 1;

                nResult = Printable.PAGE_EXISTS;
            }
            else {
                nResult = Printable.NO_SUCH_PAGE;
            }
        }
        else {
            nResult = Printable.NO_SUCH_PAGE;
        }

        return nResult;
    }

    public void setPrintPreview(boolean b) { mbIsPrintPreview = b; }
}
