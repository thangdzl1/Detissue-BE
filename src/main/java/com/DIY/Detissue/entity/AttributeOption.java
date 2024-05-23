package com.DIY.Detissue.entity;

import jakarta.persistence.*;

import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "attribute_options", schema = "detissue")
public class AttributeOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "attribute_id")
    private com.DIY.Detissue.entity.Attribute attribute;

    @Column(name = "value", length = 100)
    private String value;

    @ManyToMany
    @JoinTable(name = "attribute_option_skus",
            joinColumns = @JoinColumn(name = "attribute_option_id"),
            inverseJoinColumns = @JoinColumn(name = "skus_id"))
    private Set<com.DIY.Detissue.entity.ProductSkus> productSkuses = new LinkedHashSet<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public com.DIY.Detissue.entity.Attribute getAttribute() {
        return attribute;
    }

    public void setAttribute(com.DIY.Detissue.entity.Attribute attribute) {
        this.attribute = attribute;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Set<com.DIY.Detissue.entity.ProductSkus> getProductSkuses() {
        return productSkuses;
    }

    public void setProductSkuses(Set<com.DIY.Detissue.entity.ProductSkus> productSkuses) {
        this.productSkuses = productSkuses;
    }

}