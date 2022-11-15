package org.roko.erp.repositories;

import org.roko.erp.model.PostedSalesCreditMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedSalesCreditMemoRepository extends JpaRepository<PostedSalesCreditMemo, String> {

}
