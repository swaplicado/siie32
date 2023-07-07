/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package erp.lib.gui;

import erp.client.SClientDaemonTimeout;
import erp.lib.SLibConstants;
import erp.lib.data.SDataRegistry;
import erp.lib.form.SFormExtendedInterface;
import erp.lib.table.STableConstants;
import erp.lib.table.STableTabComponent;
import erp.lib.table.STableTabInterface;
import erp.redis.SLockUtils;
import erp.server.SServerConstants;
import erp.server.SServerRequest;
import erp.server.SServerResponse;
import java.awt.Cursor;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Vector;
import sa.lib.SLibMethod;
import sa.lib.SLibUtils;
import sa.lib.srv.SLock;
import sa.lib.srv.SSrvConsts;

/**
 *
 * @author Sergio Flores, Adrián Avilés, Sergio Flores
 */
public abstract class SGuiModule {

    protected erp.client.SClientInterface miClient;
    protected int mnModuleType;

    // form members:
    protected java.lang.Object moLastSavedPrimaryKey;
    protected erp.lib.data.SDataRegistry moRegistry;
    protected erp.lib.form.SFormInterface miForm;
    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
    protected java.util.Vector<sa.lib.srv.SSrvLock> mvIndependentLocks;
    */
    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
    protected java.util.Vector<sa.lib.srv.redis.SRedisLock> mvIndependentRedisLocks;
    */  
    protected java.util.Vector<sa.lib.srv.SLock> mvIndependentSLocks;
    // form option pickers:
    protected java.util.Vector<erp.lib.form.SFormOptionPickerInterface> mvOptionPickers;
    
    // form complement:
    protected java.lang.Object moFormComplement;
    protected int mnCurrentUserPrivilegeLevel;
    protected boolean mbIsFormReadOnly;
    
    // auxiliar registry for creating registries on the fly and passing them to forms:
    protected erp.lib.data.SDataRegistry moAuxRegistry;

    public SGuiModule(erp.client.SClientInterface client, int type) {
        miClient = client;
        mnModuleType = type;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        mvIndependentLocks = new Vector<>();
        */        
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        mvIndependentRedisLocks = new Vector<>();
        */
        mvIndependentSLocks = new Vector<>();
        mvOptionPickers = new Vector<>();
        
        clearFormMembers();
        clearFormComplement();
        moAuxRegistry = null;
    }

    protected void clearFormMembers() {
        moLastSavedPrimaryKey = null;
        moRegistry = null;
        miForm = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro     
        mvIndependentLocks.clear();
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        mvIndependentRedisLocks.clear();
        */
        mvIndependentSLocks.clear();
    }

    protected void clearFormComplement() {
        moFormComplement = null;
        mnCurrentUserPrivilegeLevel = SLibConstants.UNDEFINED;
        mbIsFormReadOnly = false;
    }

    protected void setFrameWaitCursor() {
        miClient.getFrame().getRootPane().setCursor(new Cursor(Cursor.WAIT_CURSOR));
    }

    protected void restoreFrameCursor() {
        miClient.getFrame().getRootPane().setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @SuppressWarnings("unchecked")
    protected void processView(java.lang.Class viewClass, java.lang.String viewTitle, int type, int auxType01, int auxType02) throws java.lang.NoSuchMethodException, java.lang.InstantiationException, java.lang.IllegalAccessException, java.lang.reflect.InvocationTargetException, java.lang.Exception {
        int index = 0;
        int count = 0;
        boolean exists = false;
        STableTabInterface tableTab = null;
        Constructor<?> constructor = null;

        count = miClient.getTabbedPane().getTabCount();
        for (index = 0; index < count; index++) {
            if (miClient.getTabbedPane().getComponentAt(index) instanceof STableTabInterface) {
                tableTab = (STableTabInterface) miClient.getTabbedPane().getComponentAt(index);
                if (tableTab.getTabType() == type && tableTab.getTabTypeAux01() == auxType01 && tableTab.getTabTypeAux02() == auxType02) {
                    exists = true;
                    break;
                }
            }
        }

        if (exists) {
            miClient.getTabbedPane().setSelectedIndex(index);
        }
        else {
            Class[] classParams = null;
            Object[] instanceParams = null;

            if (auxType01 == 0 && auxType02 == 0) {
                classParams = new Class[] { erp.client.SClientInterface.class, java.lang.String.class };
                instanceParams = new Object[] { miClient, viewTitle };
            }
            else if (auxType02 == 0) {
                classParams = new Class[] { erp.client.SClientInterface.class, java.lang.String.class, int.class };
                instanceParams = new Object[] { miClient, viewTitle, auxType01 };
            }
            else {
                classParams = new Class[] { erp.client.SClientInterface.class, java.lang.String.class, int.class, int.class };
                instanceParams = new Object[] { miClient, viewTitle, auxType01, auxType02 };
            }

            try {
                constructor = viewClass.getConstructor(classParams);
            }
            catch (java.lang.NoSuchMethodException e) {
                SLibUtils.printException(this, e);
                
                // Last chance to identify view class construtor:

                classParams = new Class[] { erp.client.SClientInterface.class, java.lang.String.class, int.class, int.class, int.class };
                instanceParams = new Object[] { miClient, viewTitle, type, auxType01, auxType02 };

                constructor = viewClass.getConstructor(classParams);
            }
            
            miClient.getTabbedPane().addTab(viewTitle, (java.awt.Component) constructor.newInstance(instanceParams));
            miClient.getTabbedPane().setTabComponentAt(count, new STableTabComponent(miClient.getTabbedPane(), miClient.getImageIcon(mnModuleType)));
            miClient.getTabbedPane().setSelectedIndex(count);
        }
    }

    protected int processForm(java.lang.Object pk, boolean isCopy) throws java.lang.Exception {
        int result = SLibConstants.UNDEFINED;
        SServerRequest request = null;
        SServerResponse response = null;
        
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro     
        SSrvLock lock = null;
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock redisLock = null;
        */
        SLock slock = null;
        SClientDaemonTimeout daemonTimeout = null;
        SLibMethod method = null;

        miForm.formRefreshCatalogues();
        miForm.formReset();

        if (pk != null) {
            if (!mbIsFormReadOnly && !isCopy) {
                // Attempt to gain data lock:
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro 
                lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), moRegistry.getRegistryType(), pk, moRegistry.getRegistryTimeout());             
                */
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                redisLock = SRedisLockUtils.gainLock(miClient, moRegistry.getRegistryType(), pk, moRegistry.getRegistryTimeout()/1000);
                */
                slock = SLockUtils.gainLock(miClient, moRegistry.getRegistryType(), pk, moRegistry.getRegistryTimeout());
            }

            // Read data registry:

            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_READ);
            request.setPrimaryKey(pk);
            request.setPacket(moRegistry);
            response = miClient.getSessionXXX().request(request);
            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro 
                if (lock != null) {
                    SSrvUtils.releaseLock(miClient.getSession(), lock);
                }
                */
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                if (redisLock != null) {
                    SRedisLockUtils.releaseLock(miClient, redisLock);
                }
                */
                if (slock != null) {
                    SLockUtils.releaseLock(miClient, slock);
                }
                throw new Exception(response.getMessage());
            }
            else {
                result = response.getResultType();
                if (result != SLibConstants.DB_ACTION_READ_OK) {
                    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro 
                    if (lock != null) {
                        SSrvUtils.releaseLock(miClient.getSession(), lock);
                    }
                    */
                    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                    if (redisLock != null) {
                        SRedisLockUtils.releaseLock(miClient, redisLock);
                    }
                    */
                    if (slock != null) {
                        SLockUtils.releaseLock(miClient, slock);
                    }
                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_READ + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage()));
                }
                else {
                    moRegistry = (SDataRegistry) response.getPacket();
                    miForm.setRegistry(moRegistry);
                    if (isCopy) {
                        miForm.formClearRegistry();
                    }

                    if (miForm.getTimeoutLabel() != null) {
                        daemonTimeout = new SClientDaemonTimeout(miClient, miForm.getTimeoutLabel(), moRegistry.getRegistryTimeout());
                        daemonTimeout.startDaemon();
                    }
                }
            }
        }
        else if (moAuxRegistry != null) {
            miForm.setRegistry(moAuxRegistry);
            miForm.formClearRegistry();
            moAuxRegistry = null;
        }

        // Show form to user:

        miForm.setFormVisible(true);
        result = miForm.getFormResult();

        if (daemonTimeout != null) {
            daemonTimeout.stopDaemon();
        }

        if (result != SLibConstants.FORM_RESULT_OK) {
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro 
            if (lock != null) {
                SSrvUtils.releaseLock(miClient.getSession(), lock);
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            if (redisLock != null) {
                SRedisLockUtils.releaseLock(miClient, redisLock);
            }
            */
            if (slock != null) {
                SLockUtils.releaseLock(miClient, slock);
            }
        }
        else {
            SDataRegistry registry = miForm.getRegistry();
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro 
            if (lock != null) {
                // Verify that user still has data lock:
                lock = SSrvUtils.verifyLockStatus(miClient.getSession(), lock);
            }
            */  
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            if (redisLock != null) {
                redisLock = SRedisLockUtils.verifyLockStatus(miClient, redisLock);
            }
            */
            if (slock != null) {
                slock = SLockUtils.verifyLockStatus(miClient, slock);
            }
            
            // Verify that independent locks are still valid:

            for (Object complement : registry.getRegistryComplements()) {
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                if (complement instanceof SSrvLock) {
                    mvIndependentLocks.add((SSrvLock) complement);
                }
                */  
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                if (complement instanceof SRedisLock) {
                    mvIndependentRedisLocks.add((SRedisLock) complement);
                }
                */  
                if (complement instanceof SLock) {
                    mvIndependentSLocks.add((SLock) complement);
                }
            }
            
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            for (int i = 0; i < mvIndependentLocks.size(); i++) {
                SSrvLock il = SSrvUtils.verifyLockStatus(miClient.getSession(), mvIndependentLocks.get(i));
                mvIndependentLocks.set(i, il);
            }
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            for (int i = 0; i < mvIndependentRedisLocks.size(); i++) {
                SRedisLock irl = SRedisLockUtils.verifyLockStatus(miClient, mvIndependentRedisLocks.get(i));
                mvIndependentRedisLocks.set(i, irl);
            }
            */
            for (int i = 0; i < mvIndependentSLocks.size(); i++) {
                SLock sl = SLockUtils.verifyLockStatus(miClient, mvIndependentSLocks.get(i));
                mvIndependentSLocks.set(i, sl);
            }
            
            // Save data registry:
            
            HashMap<String, Object> nonSerializableMembersMap = registry.backupNonSerializableMembers();

            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_SAVE);
            request.setPacket(registry);
            response = miClient.getSessionXXX().request(request);
            
            if (nonSerializableMembersMap != null) {
                registry.restoreNonSerializableMembers(nonSerializableMembersMap);
            }

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro             
                if (lock != null) {
                    SSrvUtils.releaseLock(miClient.getSession(), lock);
                }
                
                for (SSrvLock il : mvIndependentLocks) {
                   SSrvUtils.releaseLock(miClient.getSession(), il);
                }
                */  
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                if (redisLock != null) {
                    SRedisLockUtils.releaseLock(miClient, redisLock);
                }
                
                for (SRedisLock irl : mvIndependentRedisLocks) {
                    SRedisLockUtils.releaseLock(miClient, irl);
                }
                */
                if (slock != null) {
                    SLockUtils.releaseLock(miClient, slock);
                }
                
                for (SLock sl : mvIndependentSLocks) {
                   SLockUtils.releaseLock(miClient, sl);
                }

                throw new Exception(response.getMessage());
            }
            else {
                result = response.getResultType();

                if (result != SLibConstants.DB_ACTION_SAVE_OK) {
                    /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro                 
                    if (lock != null) {
                        SSrvUtils.releaseLock(miClient.getSession(), lock);
                    }
                    
                    for (SSrvLock il : mvIndependentLocks) {
                        SSrvUtils.releaseLock(miClient.getSession(), il);
                    }
                    */
                    /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                    if (redisLock != null) {
                        SRedisLockUtils.releaseLock(miClient, redisLock);
                    }
                    
                    for (SRedisLock irl : mvIndependentRedisLocks) {
                         SRedisLockUtils.releaseLock(miClient, irl);
                    }
                    */
                    if (slock != null) {
                        SLockUtils.releaseLock(miClient, slock);
                    }
                    
                    for (SLock sl : mvIndependentSLocks) {
                        SLockUtils.releaseLock(miClient, sl);
                    }

                    throw new Exception(SLibConstants.MSG_ERR_DB_REG_SAVE + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage()));
                }
                else {
                    moRegistry = (SDataRegistry) response.getPacket();
                    moLastSavedPrimaryKey = moRegistry.getPrimaryKey();
                    registry.setPrimaryKey(moRegistry.getPrimaryKey());
                }
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro 
                if (lock != null) {
                    SSrvUtils.releaseLock(miClient.getSession(), lock);
                }

                
                for (SSrvLock il : mvIndependentLocks) {
                    SSrvUtils.releaseLock(miClient.getSession(), il);
                }
                */
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                if (redisLock != null) {
                    SRedisLockUtils.releaseLock(miClient, redisLock);
                }
                
                for (SRedisLock irl : mvIndependentRedisLocks) {
                    SRedisLockUtils.releaseLock(miClient, irl);
                }
                */
                if (slock != null) {
                    SLockUtils.releaseLock(miClient, slock);
                }

                
                for (SLock sl : mvIndependentSLocks) {
                    SLockUtils.releaseLock(miClient, sl);
                }

                // Post-save processing:
                
                if (result == SLibConstants.DB_ACTION_SAVE_OK) {
                    if (registry.getPostSaveTarget() != null && registry.getPostSaveMethod() != null) {
                        SLibUtils.invoke(registry.getPostSaveTarget(), registry.getPostSaveMethod(), registry.getPostSaveMethodArgs());
                    }
                    
                    if (miForm instanceof SFormExtendedInterface) {
                        method = ((SFormExtendedInterface) miForm).getPostSaveMethod(moRegistry);
                        if (method != null && method.getTarget() != null && method.getMethod() != null) {
                            SLibUtils.invoke(method.getTarget(), method.getMethod(), method.getMethodArgs());
                        }
                    }
                }
            }
        }

        return result;
    }

    protected int processActionAnnul(java.lang.Object pk, boolean annul) throws java.lang.Exception {
        int result = SLibConstants.UNDEFINED;
        String msgError = "";
        SServerRequest request = null;
        SServerResponse response = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro       
        SSrvLock lock = null;
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock redisLock = null;
        */
        SLock slock = null;
        
        try {
            // Attempt to gain data lock:
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro            
            lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), moRegistry.getRegistryType(), pk, 1000 * 60); // 1 minute timeout
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            redisLock = SRedisLockUtils.gainLock(miClient, moRegistry.getRegistryType(), pk, 60); // 1 minute timeout
            */
            slock = SLockUtils.gainLock(miClient, moRegistry.getRegistryType(), pk, 1000 * 60);
            // Read data registry:

            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_READ);
            request.setPrimaryKey(pk);
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

                    moRegistry = (SDataRegistry) response.getPacket();

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

                            moRegistry.setIsRegistryRequestAnnul(annul);
                            moRegistry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());

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
            if (redisLock != null) {
                SRedisLockUtils.releaseLock(miClient, redisLock);
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
            if (redisLock != null) {
                SRedisLockUtils.releaseLock(miClient, redisLock);
            }
            */
            if (slock != null) {
                SLockUtils.releaseLock(miClient, slock);
            }

            if (!msgError.isEmpty()) {
                throw new Exception(msgError);
            }
        }

        return result;
    }

    protected int processActionDelete(java.lang.Object pk, boolean delete) throws java.lang.Exception {
        int result = SLibConstants.UNDEFINED;
        String msgError = "";
        SServerRequest request = null;
        SServerResponse response = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
        SSrvLock lock = null;
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock redisLock = null;
        */
        SLock slock = null;
        try {
            // Attempt to gain data lock:
            /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
            lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), moRegistry.getRegistryType(), pk, 1000 * 60); // 1 minute timeout
            */
            /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
            redisLock = SRedisLockUtils.gainLock(miClient, moRegistry.getRegistryType(), pk, 60); // 1 minute timeou
            */
            slock  = SLockUtils.gainLock(miClient, moRegistry.getRegistryType(), pk, 1000 * 60);
            // Read data registry:

            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_READ);
            request.setPrimaryKey(pk);
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
                    // Check if registry can be deleted:

                    moRegistry = (SDataRegistry) response.getPacket();

                    request = new SServerRequest(SServerConstants.REQ_DB_CAN_DELETE);
                    request.setPacket(moRegistry);
                    response = miClient.getSessionXXX().request(request);

                    if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                        msgError = response.getMessage();
                    }
                    else {
                        result = response.getResultType();

                        if (result != SLibConstants.DB_CAN_DELETE_YES) {
                            msgError = SLibConstants.MSG_ERR_DB_REG_DELETE_CAN + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                        }
                        else {
                            // Delete registry:

                            moRegistry.setIsRegistryRequestDelete(delete);
                            if (delete) {
                                moRegistry.setFkUserDeleteId(miClient.getSession().getUser().getPkUserId());
                            }
                            else {
                                moRegistry.setFkUserEditId(miClient.getSession().getUser().getPkUserId());
                            }

                            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_DELETE);
                            request.setPacket(moRegistry);
                            response = miClient.getSessionXXX().request(request);

                            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                                msgError = response.getMessage();
                            }
                            else {
                                result = response.getResultType();

                                if (result != SLibConstants.DB_ACTION_DELETE_OK) {
                                    msgError = SLibConstants.MSG_ERR_DB_REG_DELETE + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
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
            if (redisLock != null) {
                SRedisLockUtils.releaseLock(miClient, redisLock);
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
            if (redisLock != null) {
                SRedisLockUtils.releaseLock(miClient, redisLock);
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

    public erp.lib.data.SDataRegistry processRegistry(erp.lib.data.SDataRegistry poRegistry) throws java.lang.Exception {
        int result = SLibConstants.UNDEFINED;
        String msgError = "";
        SServerRequest request = null;
        SServerResponse response = null;
        /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro       
        SSrvLock lock = null;
        */
        /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
        SRedisLock redisLock = null;
        */
        SLock slock = null;
        SDataRegistry registry = null;

        try {
            // Attempt to gain data lock:

            if (!poRegistry.getIsRegistryNew()) {
                /* Bloque de codigo de respaldo correspondiente a la version antigua sin Redis de candado de acceso exclusivo a registro
                lock = SSrvUtils.gainLock(miClient.getSession(), miClient.getSessionXXX().getCompany().getPkCompanyId(), poRegistry.getRegistryType(), poRegistry.getPrimaryKey(), 1000 * 60); // 1 minute timeout
                */
                /* Bloque de codigo de respaldo correspondiente a la version con Redis de candado de acceso exclusivo a registro
                redisLock = SRedisLockUtils.gainLock(miClient, moRegistry.getRegistryType(), poRegistry.getPrimaryKey(), 60); // 1 minute timeout
                */
                slock = SLockUtils.gainLock(miClient, poRegistry.getRegistryType(), poRegistry.getPrimaryKey(), 1000 * 60);
            }

            // Read data registry:

            request = new SServerRequest(SServerConstants.REQ_DB_ACTION_PROCESS);
            request.setPacket(poRegistry);
            response = miClient.getSessionXXX().request(request);

            if (response.getResponseType() != SSrvConsts.RESP_TYPE_OK) {
                msgError = response.getMessage();
            }
            else {
                result = response.getResultType();

                if (result != SLibConstants.DB_ACTION_PROCESS_OK) {
                    msgError = SLibConstants.MSG_ERR_DB_REG_PROCESS + (response.getMessage().length() == 0 ? "" : "\n" + response.getMessage());
                }
                else {
                    registry = (SDataRegistry) response.getPacket();
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
            if (redisLock != null) {
                SRedisLockUtils.releaseLock(miClient, redisLock);
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
            if (redisLock != null) {
                SRedisLockUtils.releaseLock(miClient, redisLock);
            }
            */
            if (slock != null) {
                SLockUtils.releaseLock(miClient, slock);
            }
            
            if (msgError.length() > 0) {
                throw new Exception(msgError);
            }
        }

        return registry;
    }
    
    public int getModuleType() { return mnModuleType; }

    public void setLastSavedPrimaryKey(java.lang.Object o) { moLastSavedPrimaryKey = o; }
    public void setRegistry(erp.lib.data.SDataRegistry o) { moRegistry = o; }
    public void setFormComplement(java.lang.Object o) { moFormComplement = o; }
    public void setCurrentUserPrivilegeLevel(int n) { mnCurrentUserPrivilegeLevel = n; }
    public void setIsFormReadOnly(boolean b) { mbIsFormReadOnly = b; }
    public void setAuxRegistry(erp.lib.data.SDataRegistry o) { moAuxRegistry = o; }

    public java.lang.Object getLastSavedPrimaryKey() { return moLastSavedPrimaryKey; }
    public erp.lib.data.SDataRegistry getRegistry() { return moRegistry; }
    public java.lang.Object getFormComplement() { return moFormComplement; }
    public int getCurrentUserPrivilegeLevel() { return mnCurrentUserPrivilegeLevel; }
    public boolean getIsFormReadOnly() { return mbIsFormReadOnly; }
    public erp.lib.data.SDataRegistry getAuxRegistry() { return moAuxRegistry; }

    public void refreshCatalogues(int suscriptor) {
        int i = 0;
        int j = 0;
        java.util.Vector<Integer> vector = null;

        for (i = 0; i < miClient.getTabbedPane().getTabCount(); i++) {
            if (miClient.getTabbedPane().getComponentAt(i) instanceof STableTabInterface) {
                vector = ((STableTabInterface) miClient.getTabbedPane().getComponentAt(i)).getSuscriptors();
                for (j = 0; j < vector.size(); j++) {
                    if (suscriptor == vector.get(j)) {
                        ((STableTabInterface) miClient.getTabbedPane().getComponentAt(i)).actionRefresh(STableConstants.REFRESH_MODE_RELOAD);
                    }
                }
            }
        }

        for (i = 0; i < mvOptionPickers.size(); i++) {
            if (suscriptor == mvOptionPickers.get(i).getOptionType()) {
                mvOptionPickers.get(i).formRefreshOptionPane();
            }
        }
    }

    public int showFormForCopy(int formType, java.lang.Object pk) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abstract void showView(int viewType);
    public abstract void showView(int viewType, int auxType01);
    public abstract void showView(int viewType, int auxType01, int auxType02);
    public abstract int showForm(int formType, java.lang.Object pk);
    public abstract int showForm(int formType, int auxType, java.lang.Object pk);
    public abstract erp.lib.form.SFormOptionPickerInterface getOptionPicker(int optionType);
    public abstract int annulRegistry(int registryType, java.lang.Object pk, sa.lib.gui.SGuiParams params);
    public abstract int deleteRegistry(int registryType, java.lang.Object pk);
    public abstract javax.swing.JMenu[] getMenues();
    public abstract javax.swing.JMenu[] getMenuesForModule(int moduleType);
}
