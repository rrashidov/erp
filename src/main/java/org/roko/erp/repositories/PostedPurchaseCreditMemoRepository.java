package org.roko.erp.repositories;

import org.roko.erp.model.PostedPurchaseCreditMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostedPurchaseCreditMemoRepository extends JpaRepository<PostedPurchaseCreditMemo, String> {

}
