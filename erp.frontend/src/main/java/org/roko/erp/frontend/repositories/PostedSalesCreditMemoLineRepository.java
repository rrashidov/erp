package org.roko.erp.frontend.repositories;

import java.util.List;

import org.roko.erp.frontend.model.PostedSalesCreditMemo;
import org.roko.erp.frontend.model.PostedSalesCreditMemoLine;
import org.roko.erp.frontend.model.jpa.PostedSalesCreditMemoLineId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

        @Query("SELECT postedSalesCreditMemoLine FROM PostedSalesCreditMemoLine postedSalesCreditMemoLine WHERE postedSalesCreditMemoLine.postedSalesCreditMemo = :postedSalesCreditMemo")
        public Page<PostedSalesCreditMemoLine> findFor(
                        @Param("postedSalesCreditMemo") PostedSalesCreditMemo postedSalesCreditMemo, Pageable pageable);

        @Query("SELECT COUNT(postedSalesCreditMemoLine) FROM PostedSalesCreditMemoLine postedSalesCreditMemoLine WHERE postedSalesCreditMemoLine.postedSalesCreditMemo = :postedSalesCreditMemo")
        public long count(
                        @Param("postedSalesCreditMemo") PostedSalesCreditMemo postedSalesCreditMemo);

}
