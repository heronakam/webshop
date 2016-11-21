package com.ippon.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Marque.
 */
@Entity
@Table(name = "marque")
@Document(indexName = "marque")
public class Marque implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Size(min = 10)
    @Column(name = "presentation", nullable = false)
    private String presentation;

    @Column(name = "logo_url")
    private String logoUrl;

    @OneToMany(mappedBy = "marque")
    @JsonIgnore
    private Set<Produit> produits = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Marque name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPresentation() {
        return presentation;
    }

    public Marque presentation(String presentation) {
        this.presentation = presentation;
        return this;
    }

    public void setPresentation(String presentation) {
        this.presentation = presentation;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public Marque logoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
        return this;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public Marque produits(Set<Produit> produits) {
        this.produits = produits;
        return this;
    }

    public Marque addProduit(Produit produit) {
        produits.add(produit);
        produit.setMarque(this);
        return this;
    }

    public Marque removeProduit(Produit produit) {
        produits.remove(produit);
        produit.setMarque(null);
        return this;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Marque marque = (Marque) o;
        if(marque.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, marque.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Marque{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", presentation='" + presentation + "'" +
            ", logoUrl='" + logoUrl + "'" +
            '}';
    }
}
