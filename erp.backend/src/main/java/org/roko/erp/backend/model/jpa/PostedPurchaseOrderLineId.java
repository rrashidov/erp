package org.roko.erp.backend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import org.roko.erp.backend.model.PostedPurchaseOrder;

@Embeddable
public class PostedPurchaseOrderLineId implements Serializable {

    @ManyToOne
    private PostedPurchaseOrder postedPurchaseOrder;

    private int lineNo;

    public PostedPurchaseOrder getPostedPurchaseOrder() {
        return postedPurchaseOrder;
    }

    public void setPostedPurchaseOrder(PostedPurchaseOrder postedPurchaseOrder) {
        this.postedPurchaseOrder = postedPurchaseOrder;
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
        result = prime * result + ((postedPurchaseOrder == null) ? 0 : postedPurchaseOrder.hashCode());
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
        PostedPurchaseOrderLineId other = (PostedPurchaseOrderLineId) obj;
        if (postedPurchaseOrder == null) {
            if (other.postedPurchaseOrder != null)
                return false;
        } else if (!postedPurchaseOrder.equals(other.postedPurchaseOrder))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }

    
}
