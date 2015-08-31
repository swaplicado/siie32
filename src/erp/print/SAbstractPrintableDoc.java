/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.print;

import java.util.Vector;

import erp.lib.print.SPrintConstants;

/**
 *
 * @author Alfonso Flores
 */
public abstract class SAbstractPrintableDoc {

    protected int mnMode;
    protected int mnLevel;
    protected int[] manKey;

    protected double mdPageWidth;
    protected double mdPageHeight;
    protected double mdViewWidth;
    protected double mdViewHeight;

    protected int mnMarginTop;
    protected int mnMarginBottom;
    protected int mnMarginLeft;
    protected int mnMarginRight;

    protected int mnViewYHeader;
    protected int mnViewYBody;
    protected int mnViewYFooter;
    protected int mnHeightHeader;
    protected int mnHeightBody;
    protected int mnHeightFooter;
    protected int mnSerialsViewMarginLeft;
    protected int mnSerialsViewWidth;

    protected int mnPages;
    protected int mnRowsInBody;

    protected boolean mbPrintingArranged;

    protected erp.client.SClientInterface miClient;

    protected Vector mvHeader;
    protected Vector mvFooter;
    protected Vector mvBody;
    protected Vector mvSerialsWidth;
    protected Vector mvPrintedPages;

    public SAbstractPrintableDoc(erp.client.SClientInterface client) {
        miClient = client;

        mnMode = -1;
        mnLevel = -1;
        manKey = new int[0];

        // Letter sheet:
        mdPageWidth = (8f + (1f/2f)) * 72f;     // 612
        mdPageHeight = 11f * 72f;               // 792

        mnMarginTop = SPrintConstants.TAB_QUARTER;
        mnMarginBottom = SPrintConstants.TAB_QUARTER;
        mnMarginLeft = SPrintConstants.TAB_HALF + SPrintConstants.TAB_QUARTER;
        mnMarginRight = SPrintConstants.TAB_HALF;

        mdViewWidth = mdPageWidth - mnMarginLeft - mnMarginRight;
        mdViewHeight = mdPageHeight - mnMarginTop - mnMarginBottom;

        mnViewYHeader = 0;
        mnViewYBody = mnViewYHeader + SPrintConstants.TAB;
        mnViewYFooter = mnViewYBody + (SPrintConstants.TAB * 9);

        mnHeightHeader = mnViewYBody - mnViewYHeader;
        mnHeightBody = mnViewYFooter - mnViewYBody;
        mnHeightFooter = (int) mdPageHeight - mnMarginTop - mnMarginBottom - mnViewYFooter;

        mnSerialsViewMarginLeft = SPrintConstants.TAB_EIGHTH;
        mnSerialsViewWidth = SPrintConstants.TAB * 6;

        mbPrintingArranged = false;

        mvHeader = new Vector();
        mvFooter = new Vector();
        mvBody = new Vector();
        mvSerialsWidth = new Vector();
        mvPrintedPages = new Vector();
    }

    public void setKey(int[] key) { manKey = (int[]) key.clone(); }
    public void setMode(int n) { mnMode = n; }
    public void setLevel(int n) { mnLevel = n; }

    public abstract boolean preparePrinting();
    public abstract void printDocument();
}
