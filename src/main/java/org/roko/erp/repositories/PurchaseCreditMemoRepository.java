package org.roko.erp.repositories;

import org.roko.erp.model.PurchaseCreditMemo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseCreditMemoRepository extends JpaRepository<PurchaseCreditMemo, String> {

}
