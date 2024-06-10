package com.DIY.Detissue.repository;

import com.DIY.Detissue.entity.AttributeOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AttributeOptionsRepository extends JpaRepository<AttributeOption,Integer> {
    @Query("SELECT uci.attributeOptions FROM UserCartItemAttributesOption uci WHERE uci.shoppingCartItem.id = ?1")
    List<AttributeOption> findByShoppingCartItemsId(int id);

    @Query("SELECT ao FROM AttributeOptionSkus aos " +
            "join aos.attributeOption ao " +
            "join ao.attribute WHERE aos.skus.id = ?1 and ao.attribute.id = ?2")
    List<AttributeOption> findByProductSkusesIdAndAttributeId(int productSkusId, int attributeId);
}
