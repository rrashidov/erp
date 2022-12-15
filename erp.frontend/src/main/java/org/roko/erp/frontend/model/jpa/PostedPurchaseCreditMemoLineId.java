package org.roko.erp.frontend.model.jpa;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;

import org.roko.erp.frontend.model.PostedPurchaseCreditMemo;

@Embeddable
public class PostedPurchaseCreditMemoLineId implements Serializable {

    @ManyToOne
    private PostedPurchaseCreditMemo postedPurchaseCreditMemo;

    private int lineNo;

    public PostedPurchaseCreditMemo getPostedPurchaseCreditMemo() {
        return postedPurchaseCreditMemo;
    }

    public void setPostedPurchaseCreditMemo(PostedPurchaseCreditMemo postedPurchaseCreditMemo) {
        this.postedPurchaseCreditMemo = postedPurchaseCreditMemo;
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
        result = prime * result + ((postedPurchaseCreditMemo == null) ? 0 : postedPurchaseCreditMemo.hashCode());
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
        PostedPurchaseCreditMemoLineId other = (PostedPurchaseCreditMemoLineId) obj;
        if (postedPurchaseCreditMemo == null) {
            if (other.postedPurchaseCreditMemo != null)
                return false;
        } else if (!postedPurchaseCreditMemo.equals(other.postedPurchaseCreditMemo))
            return false;
        if (lineNo != other.lineNo)
            return false;
        return true;
    }

    
}
