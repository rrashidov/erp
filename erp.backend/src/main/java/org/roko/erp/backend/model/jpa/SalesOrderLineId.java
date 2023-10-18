package org.roko.erp.backend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import org.roko.erp.backend.model.SalesOrder;

@Embeddable
public class SalesOrderLineId implements Serializable {
    
    @ManyToOne
    private SalesOrder salesOrder;
    
    private int lineNo;

    public SalesOrderLineId() {
    }

    public SalesOrder getSalesOrder() {
        return salesOrder;
    }

    public void setSalesOrder(SalesOrder salesOrder) {
        this.salesOrder = salesOrder;
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
        result = prime * result + ((salesOrder == null) ? 0 : salesOrder.hashCode());
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
        SalesOrderLineId other = (SalesOrderLineId) obj;
        if (salesOrder == null) {
            if (other.salesOrder != null)
                return false;
        } else if (!salesOrder.equals(other.salesOrder))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }

}
