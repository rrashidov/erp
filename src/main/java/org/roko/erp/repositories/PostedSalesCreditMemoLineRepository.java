package org.roko.erp.repositories;

import java.util.List;

import org.roko.erp.model.PostedSalesCreditMemo;
import org.roko.erp.model.PostedSalesCreditMemoLine;
import org.roko.erp.model.jpa.PostedSalesCreditMemoLineId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedSalesCreditMemoLineRepository
                extends JpaRepository<PostedSalesCreditMemoLine, PostedSalesCreditMemoLineId> {

        @Query("SELECT postedSalesCreditMemoLine FROM PostedSalesCreditMemoLine postedSalesCreditMemoLine WHERE postedSalesCreditMemoLine.postedSalesCreditMemo = :postedSalesCreditMemo")
        public List<PostedSalesCreditMemoLine> findFor(
                        @Param("postedSalesCreditMemo") PostedSalesCreditMemo postedSalesCreditMemo);

        @Query("SELECT COUNT(postedSalesCreditMemoLine) FROM PostedSalesCreditMemoLine postedSalesCreditMemoLine WHERE postedSalesCreditMemoLine.postedSalesCreditMemo = :postedSalesCreditMemo")
        public long count(
                        @Param("postedSalesCreditMemo") PostedSalesCreditMemo postedSalesCreditMemo);

}
