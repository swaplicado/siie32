/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.hrs.db;

import erp.client.SClientInterface;
import erp.lib.SLibConstants;
import erp.mhrs.data.SDataPayrollReceiptIssue;
import erp.mtrn.data.SCfdUtils;
import erp.mtrn.data.SDataCfd;
import erp.redis.SLockUtils;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.util.ArrayList;
import java.util.Date;
import sa.lib.srv.SLock;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Juan Barajas, Adrián Avilés, Sergio Flores
 */
public class SHrsPayrollAnnul {

    protected SClientInterface miClient;
    protected ArrayList<SDataCfd> maCfds;
    protected ArrayList<SDataPayrollReceiptIssue> maReceiptIssues;
    protected int mnCfdiPayrollVersion;
    protected boolean mbIsPayrollAll;
    protected Date mtDateAnnul;
    protected boolean mbAnnulSat;
    protected int mnDpsAnnulType;
    protected String msAnnulReason;
    protected String msAnnulRelatedUuid;
    
    public SHrsPayrollAnnul(SClientInterface client, ArrayList<SDataCfd> cfds, ArrayList<SDataPayrollReceiptIssue> receiptIssues, int cfdiPayrollVersion, boolean isPayrollAll, Date dateAnnul, boolean annulSat, int dpsAnnulType, String annulReason, String annulRelatedUuid) {
        miClient = client;
        maCfds = cfds;
        maReceiptIssues = receiptIssues;
        mnCfdiPayrollVersion = cfdiPayrollVersion;
        mbIsPayrollAll = isPayrollAll;
        mtDateAnnul = dateAnnul;
        mbAnnulSat = annulSat;
        mnDpsAnnulType = dpsAnnulType;
        msAnnulReason = annulReason;
        msAnnulRelatedUuid = annulRelatedUuid;
    }
    
    public boolean annulPayroll() throws Exception {
        boolean cancel = false;
        
        if (mbAnnulSat) {
            if (mbIsPayrollAll) {
                /* XXX jbarajas 04/02/2016 sign and sending CFDI
                cancel = SCfdUtils.cancelCfdi(miClient, maCfds, mnCfdiPayrollVersion, mtDateAnnul, (mbAnnulSat && !maCfds.isEmpty()));
                */
                if (miClient.getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
                    cancel = SCfdUtils.cancelAndSendCfdi(miClient, maCfds, mnCfdiPayrollVersion, mtDateAnnul, (mbAnnulSat && !maCfds.isEmpty()), mnDpsAnnulType, msAnnulReason, msAnnulRelatedUuid);
                }
                else {
                    cancel = SCfdUtils.cancelCfdi(miClient, maCfds, mnCfdiPayrollVersion, mtDateAnnul, (mbAnnulSat && !maCfds.isEmpty()), mnDpsAnnulType, msAnnulReason, msAnnulRelatedUuid);
                }
            }
            else {
                /* XXX jbarajas 04/02/2016 sign and sending CFDI
                cancel = SCfdUtils.cancelCfdi(miClient, maCfds.get(0), mnCfdiPayrollVersion, mtDateAnnul, (mbAnnulSat && !maCfds.isEmpty()));
                */
                if (miClient.getSessionXXX().getParamsCompany().getIsCfdiSendingAutomaticHrs()) {
                    cancel = SCfdUtils.cancelAndSendCfdi(miClient, maCfds.get(0), mnCfdiPayrollVersion, mtDateAnnul, (mbAnnulSat && !maCfds.isEmpty()), true, mnDpsAnnulType, msAnnulReason, msAnnulRelatedUuid);
                }
                else {
                    cancel = SCfdUtils.cancelCfdi(miClient, maCfds.get(0), mnCfdiPayrollVersion, mtDateAnnul, (mbAnnulSat && !maCfds.isEmpty()), true, mnDpsAnnulType, msAnnulReason, msAnnulRelatedUuid);
                }
            }
        }
        else {
            for (SDataPayrollReceiptIssue receiptIssue : maReceiptIssues) {
                processAnnul(receiptIssue);
            }
        }
        
        return cancel;
    }

    protected int processAnnul(SDataPayrollReceiptIssue moRegistry) throws java.lang.Exception {
        int result = SLibConstants.UNDEFINED;
        String msgError = "";
        SServerRequest request = null;
        SServerResponse response = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro       
        SSrvLock lock = null;
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock rlock = null;
        */
        SLock slock = null;
        try {
            // Attempt to gain data lock:
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), moRegistry.getRegistryType(), moRegistry.getPrimaryKey(), 1000 * 60);     // 1 minute timeout
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            rlock = SRedisLockUtils.gainLock(miClient, moRegistry.getRegistryType(), moRegistry.getPrimaryKey(), 60);
            */
            slock = SLockUtils.gainLock(miClient, moRegistry.getRegistryType(), moRegistry.getPrimaryKey(), 1000 * 60);
            // Read data registry:

            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_READ);
            request.setPrimaryKey(moRegistry.getPrimaryKey());
            request.setPacket(moRegistry);
            response = miClient.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                msgError = response.getMessage();
            }
            else {
                result = response.getResultType();

                if (result != SLibConstants.DB_ACTION_READ_OK) {
                    msgError = SLibConstants.MSG_ERR_DB_REG_READ + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                }
                else {
                    // Check if registry can be annuled:

                    moRegistry = (SDataPayrollReceiptIssue) response.getPacket();

                    request = new SServerRequest(SServerConstants.REQ_DB_CAN_ANNUL);
                    request.setPacket(moRegistry);
                    response = miClient.getSessionXXX().request(request);

                    if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                        msgError = response.getMessage();
                    }
                    else {
                        result = response.getResultType();

                        if (result != SLibConstants.DB_CAN_ANNUL_YES) {
                            msgError = SLibConstants.MSG_ERR_DB_REG_ANNUL_CAN + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                        }
                        else {
                            // Annul registry:

                            moRegistry.setIsRegistryRequestAnnul(true);
                            moRegistry.setFkUserUpdateId(miClient.getSession().getUser().getPkUserId());

                            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_ANNUL);
                            request.setPacket(moRegistry);
                            response = miClient.getSessionXXX().request(request);

                            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                msgError = response.getMessage();
                            }
                            else {
                                result = response.getResultType();

                                if (result != SLibConstants.DB_ACTION_ANNUL_OK) {
                                    msgError = SLibConstants.MSG_ERR_DB_REG_ANNUL + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e) {
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            if (lock != null) {
                SSrvUtils.releaseLock(miClient.getSession(), lock);
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            if (rlock != null) {
                SRedisLockUtils.releaseLock(miClient, rlock);
            }
            */
            if (slock != null) {
                SLockUtils.releaseLock(miClient, slock);
            }
            throw e;
        }
        finally {
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            if (lock != null) {
                SSrvUtils.releaseLock(miClient.getSession(), lock);
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            if (rlock != null) {
                SRedisLockUtils.releaseLock(miClient, rlock);
            }
            */
            if (slock != null) {
                SLockUtils.releaseLock(miClient, slock);
            }
            
            if (msgError.length() > 0) {
                throw new Exception(msgError);
            }
        }

        return result;
    }
}
