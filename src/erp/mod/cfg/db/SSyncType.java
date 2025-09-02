/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package erp.mod.cfg.db;

/**
 *
 * @author Sergio Flores
 */
public enum SSyncType {
    // For ERP database:
    USER,
    PARTNER_SUPPLIER,
    PARTNER_CUSTOMER,
    AUTH_ACTOR,
    AUTH_JOB_TITLE,
    AUTH_DEPARTMENT,
    AUTH_FUNCTIONAL_AREA,
    // For company database:
    FUNCTIONAL_AREA,
    REQUISITION,
    REQUISITION_AUTH,
    PUR_QUOTE,
    PUR_QUOTE_REQ,
    PUR_ORDER,
    PUR_ORDER_AUTH,
    PUR_REF_ORDER,
    PUR_REF_SCALE_TICKET,
    PUR_INVOICE,
    PUR_INVOICE_REQ,
    PUR_INVOICE_REISSUE_REQ,
    PUR_INVOICE_AUTH,
    PUR_CREDIT_NOTE,
    PUR_CREDIT_NOTE_REQ,
    PUR_CREDIT_NOTE_REISSUE_REQ,
    PUR_CREDIT_NOTE_AUTH,
    PUR_EVIDENCE,
    PUR_EVIDENCE_REQ,
    PUR_EVIDENCE_REISSUE_REQ,
    PUR_EVIDENCE_AUTH,
    PUR_PAYMENT,
    PUR_PAYMENT_AUTH,
    PUR_PAYMENT_RCPT,
    PUR_PAYMENT_RCPT_REQ,
    PUR_PAYMENT_RCPT_REISSUE_REQ,
    PUR_PAYMENT_RCPT_AUTH,
    PUR_REFUND,
    PUR_REFUND_REQ
}
