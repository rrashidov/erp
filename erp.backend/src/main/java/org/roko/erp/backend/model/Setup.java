package org.roko.erp.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Setup {
    
    @Id
    private String code = "";

    @ManyToOne
    private CodeSerie salesOrderCodeSerie;

    @ManyToOne
    private CodeSerie purchaseOrderCodeSerie;

    @ManyToOne
    private CodeSerie salesCreditMemoCodeSerie;

    @ManyToOne
    private CodeSerie purchaseCreditMemoCodeSerie;

    @ManyToOne
    private CodeSerie postedSalesOrderCodeSerie;

    @ManyToOne
    private CodeSerie postedPurchaseOrderCodeSerie;

    @ManyToOne
    private CodeSerie postedSalesCreditMemoCodeSerie;

    @ManyToOne
    private CodeSerie postedPurchaseCreditMemoCodeSerie;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CodeSerie getSalesOrderCodeSerie() {
        return salesOrderCodeSerie;
    }

    public void setSalesOrderCodeSerie(CodeSerie salesOrderCodeSerie) {
        this.salesOrderCodeSerie = salesOrderCodeSerie;
    }

    public CodeSerie getPurchaseOrderCodeSerie() {
        return purchaseOrderCodeSerie;
    }

    public void setPurchaseOrderCodeSerie(CodeSerie purchaseOrderCodeSerie) {
        this.purchaseOrderCodeSerie = purchaseOrderCodeSerie;
    }

    public CodeSerie getSalesCreditMemoCodeSerie() {
        return salesCreditMemoCodeSerie;
    }

    public void setSalesCreditMemoCodeSerie(CodeSerie salesCreditMemoCodeSerie) {
        this.salesCreditMemoCodeSerie = salesCreditMemoCodeSerie;
    }

    public CodeSerie getPostedSalesOrderCodeSerie() {
        return postedSalesOrderCodeSerie;
    }

    public void setPostedSalesOrderCodeSerie(CodeSerie postedSalesOrderCodeSerie) {
        this.postedSalesOrderCodeSerie = postedSalesOrderCodeSerie;
    }

    public CodeSerie getPostedPurchaseOrderCodeSerie() {
        return postedPurchaseOrderCodeSerie;
    }

    public void setPostedPurchaseOrderCodeSerie(CodeSerie postedPurchaseOrderCodeSerie) {
        this.postedPurchaseOrderCodeSerie = postedPurchaseOrderCodeSerie;
    }

    public CodeSerie getPostedSalesCreditMemoCodeSerie() {
        return postedSalesCreditMemoCodeSerie;
    }

    public void setPostedSalesCreditMemoCodeSerie(CodeSerie postedSalesCreditMemoCodeSerie) {
        this.postedSalesCreditMemoCodeSerie = postedSalesCreditMemoCodeSerie;
    }

    public CodeSerie getPostedPurchaseCreditMemoCodeSerie() {
        return postedPurchaseCreditMemoCodeSerie;
    }

    public void setPostedPurchaseCreditMemoCodeSerie(CodeSerie postedPurchaseCreditMemoCodeSerie) {
        this.postedPurchaseCreditMemoCodeSerie = postedPurchaseCreditMemoCodeSerie;
    }

    public CodeSerie getPurchaseCreditMemoCodeSerie() {
        return purchaseCreditMemoCodeSerie;
    }

    public void setPurchaseCreditMemoCodeSerie(CodeSerie purchaseCreditMemoCodeSerie) {
        this.purchaseCreditMemoCodeSerie = purchaseCreditMemoCodeSerie;
    }
    
}
