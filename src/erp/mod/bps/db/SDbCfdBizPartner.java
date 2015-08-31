/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.bps.db;

import erp.cfd.SCfdDataAsociadoNegocios;
import erp.client.SClientInterface;
import erp.data.SDataConstants;
import erp.data.SDataUtilities;
import erp.lib.SLibConstants;
import erp.mbps.data.SDataBizPartner;
import erp.mbps.data.SDataBizPartnerBranch;
import erp.mbps.data.SDataBizPartnerBranchAddress;
import sa.lib.SLibConsts;
import sa.lib.SLibUtils;

/**
 *
 * @author Juan Barajas
 */
public class SDbCfdBizPartner {

    SClientInterface miClient;
    
    protected int mnBizPartnerId;
    protected int mnBizPartnerBranchId;
    protected int mnBizPartnerExpeditionId;
    protected int mnBizPartnerBranchExpeditionId;
    protected boolean mbIsEmisor;
    
    public SDbCfdBizPartner(SClientInterface client) {
        miClient = client;
        
        mnBizPartnerId = 0;
        mnBizPartnerBranchId = 0;
        mnBizPartnerExpeditionId = 0;
        mnBizPartnerBranchExpeditionId = 0;
        mbIsEmisor = false;
    }
    
    public void setBizPartnerId(int n) { mnBizPartnerId = n; }
    public void setBizPartnerBranchId(int n) { mnBizPartnerBranchId = n; }
    public void setBizPartnerExpeditionId(int n) { mnBizPartnerExpeditionId = n; }
    public void setBizPartnerBranchExpeditionId(int n) { mnBizPartnerBranchExpeditionId = n; }
    public void setIsEmisor(boolean b) { mbIsEmisor = b; }
    
    public int getBizPartnerId() { return mnBizPartnerId; }
    public int getBizPartnerBranchId() { return mnBizPartnerBranchId; }
    public int getBizPartnerExpeditionId() { return mnBizPartnerExpeditionId; }
    public int getBizPartnerBranchExpeditionId() { return mnBizPartnerBranchExpeditionId; }
    public boolean getIsEmisor() { return mbIsEmisor; }
    
    public SCfdDataAsociadoNegocios getBizPartner() {
        SDataBizPartner moBizPartner = null;
        SDataBizPartnerBranch moBizPartnerBranch = null;
        SDataBizPartnerBranchAddress moBizPartnerBranchAddress = null;
        SCfdDataAsociadoNegocios asociadoNegocios = null;
        
        try {
            moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnBizPartnerId }, SLibConstants.EXEC_MODE_SILENT);
            
            if (moBizPartner == null) {
                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
            }
            else {
                moBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { mnBizPartnerBranchId }, SLibConstants.EXEC_MODE_SILENT);
                if (moBizPartnerBranch == null) {
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                }
                else {
                    if (!mbIsEmisor && moBizPartnerBranch.getIsAddressPrintable()) {
                        moBizPartnerBranchAddress = moBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                    }
                    else {
                        moBizPartnerBranchAddress = moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial();
                    }
                    asociadoNegocios = new SCfdDataAsociadoNegocios();

                    asociadoNegocios.setBizPartnerId(mnBizPartnerId);
                    asociadoNegocios.setBizPartnerBranchId(mnBizPartnerBranchId);
                    asociadoNegocios.setBizPartnerBranchHqId(moBizPartner.getDbmsHqBranch().getPkBizPartnerBranchId());
                    asociadoNegocios.setBizPartnerRfc(moBizPartner.getFiscalId());
                    asociadoNegocios.setBizPartnerName(moBizPartner.getProperName());
                    asociadoNegocios.setBizPartnerStreet(moBizPartnerBranchAddress.getStreet());
                    asociadoNegocios.setBizPartnerStreetNumberExt(moBizPartnerBranchAddress.getStreetNumberExt());
                    asociadoNegocios.setBizPartnerStreetNumberInt(moBizPartnerBranchAddress.getStreetNumberInt());
                    asociadoNegocios.setBizPartnerNeighborhood(moBizPartnerBranchAddress.getNeighborhood());
                    asociadoNegocios.setBizPartnerReference(moBizPartnerBranchAddress.getReference());
                    asociadoNegocios.setBizPartnerLocality(moBizPartnerBranchAddress.getLocality());
                    asociadoNegocios.setBizPartnerCounty(moBizPartnerBranchAddress.getCounty());
                    asociadoNegocios.setBizPartnerState(moBizPartnerBranchAddress.getState());
                    asociadoNegocios.setBizPartnerZipCode(moBizPartnerBranchAddress.getZipCode());
                    asociadoNegocios.setBizPartnerPoBox(moBizPartnerBranchAddress.getPoBox());
                    asociadoNegocios.setBizPartnerCountry(moBizPartnerBranchAddress.getDbmsDataCountry().getCountry());
                    
                    if (mnBizPartnerExpeditionId != SLibConsts.UNDEFINED && mnBizPartnerBranchExpeditionId != SLibConsts.UNDEFINED) {
                        moBizPartner = (SDataBizPartner) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BP, new int[] { mnBizPartnerExpeditionId }, SLibConstants.EXEC_MODE_SILENT);

                        if (moBizPartner == null) {
                            throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                        }
                        else {
                            moBizPartnerBranch = (SDataBizPartnerBranch) SDataUtilities.readRegistry(miClient, SDataConstants.BPSU_BPB, new int[] { mnBizPartnerBranchExpeditionId }, SLibConstants.EXEC_MODE_SILENT);
                            if (moBizPartnerBranch == null) {
                                throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ_DEP);
                            }
                            else {
                                if (moBizPartnerBranch.getIsAddressPrintable()) {
                                    moBizPartnerBranchAddress = moBizPartnerBranch.getDbmsBizPartnerBranchAddressOfficial();
                                }
                                else {
                                    moBizPartnerBranchAddress = moBizPartner.getDbmsHqBranch().getDbmsBizPartnerBranchAddressOfficial();
                                }
                                
                                asociadoNegocios.setBizPartnerExpeditionStreet(moBizPartnerBranchAddress.getStreet());
                                asociadoNegocios.setBizPartnerExpeditionStreetNumberExt(moBizPartnerBranchAddress.getStreetNumberExt());
                                asociadoNegocios.setBizPartnerExpeditionStreetNumberInt(moBizPartnerBranchAddress.getStreetNumberInt());
                                asociadoNegocios.setBizPartnerExpeditionNeighborhood(moBizPartnerBranchAddress.getNeighborhood());
                                asociadoNegocios.setBizPartnerExpeditionReference(moBizPartnerBranchAddress.getReference());
                                asociadoNegocios.setBizPartnerExpeditionLocality(moBizPartnerBranchAddress.getLocality());
                                asociadoNegocios.setBizPartnerExpeditionCounty(moBizPartnerBranchAddress.getCounty());
                                asociadoNegocios.setBizPartnerExpeditionState(moBizPartnerBranchAddress.getState());
                                asociadoNegocios.setBizPartnerExpeditionZipCode(moBizPartnerBranchAddress.getZipCode());
                                asociadoNegocios.setBizPartnerExpeditionPoBox(moBizPartnerBranchAddress.getPoBox());
                                asociadoNegocios.setBizPartnerExpeditionCountry(moBizPartnerBranchAddress.getDbmsDataCountry().getCountry());
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            SLibUtils.showException(this, e);
        }
        return asociadoNegocios;
    }
}
