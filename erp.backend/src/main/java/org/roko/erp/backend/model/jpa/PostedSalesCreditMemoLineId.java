package org.roko.erp.backend.model.jpa;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ManyToOne;

import org.roko.erp.backend.model.PostedSalesCreditMemo;

@Embeddable
public class PostedSalesCreditMemoLineId implements Serializable {

    @ManyToOne
    private PostedSalesCreditMemo postedSalesCreditMemo;

    private int lineNo;

    public PostedSalesCreditMemo getPostedSalesCreditMemo() {
        return postedSalesCreditMemo;
    }

    public void setPostedSalesCreditMemo(PostedSalesCreditMemo postedSalesCreditMemo) {
        this.postedSalesCreditMemo = postedSalesCreditMemo;
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
        result = prime * result + ((postedSalesCreditMemo == null) ? 0 : postedSalesCreditMemo.hashCode());
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
        PostedSalesCreditMemoLineId other = (PostedSalesCreditMemoLineId) obj;
        if (postedSalesCreditMemo == null) {
            if (other.postedSalesCreditMemo != null)
                return false;
        } else if (!postedSalesCreditMemo.equals(other.postedSalesCreditMemo))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }

}
