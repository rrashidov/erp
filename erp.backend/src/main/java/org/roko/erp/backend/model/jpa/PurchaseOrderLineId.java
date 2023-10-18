package org.roko.erp.backend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import org.roko.erp.backend.model.PurchaseOrder;

@Embeddable
public class PurchaseOrderLineId implements Serializable {
    
    @ManyToOne
    private PurchaseOrder purchaseOrder;

    private int lineNo;

    public PurchaseOrderLineId() {
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public int getLineNo() {
        return lineNo;
    }

    public void setLineNo(int lineNo) {
        this.lineNo = lineNo;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((purchaseOrder == null) ? 0 : purchaseOrder.hashCode());
        result = prime * result + lineNo;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PurchaseOrderLineId other = (PurchaseOrderLineId) obj;
        if (purchaseOrder == null) {
            if (other.purchaseOrder != null)
                return false;
        } else if (!purchaseOrder.equals(other.purchaseOrder))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }
    
}
