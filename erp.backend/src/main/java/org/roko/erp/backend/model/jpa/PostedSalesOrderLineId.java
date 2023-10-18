package org.roko.erp.backend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import org.roko.erp.backend.model.PostedSalesOrder;

@Embeddable
public class PostedSalesOrderLineId implements Serializable {

    @ManyToOne
    private PostedSalesOrder postedSalesOrder;

    private int lineNo;

    public PostedSalesOrder getPostedSalesOrder() {
        return postedSalesOrder;
    }

    public void setPostedSalesOrder(PostedSalesOrder postedSalesOrder) {
        this.postedSalesOrder = postedSalesOrder;
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
        result = prime * result + ((postedSalesOrder == null) ? 0 : postedSalesOrder.hashCode());
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
        PostedSalesOrderLineId other = (PostedSalesOrderLineId) obj;
        if (postedSalesOrder == null) {
            if (other.postedSalesOrder != null)
                return false;
        } else if (!postedSalesOrder.equals(other.postedSalesOrder))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }

    
}
