package com.ippon.shop.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Panier.
 */
@Entity
@Table(name = "panier")
@Document(indexName = "panier")
public class Panier implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToMany
    @NotNull
    @JoinTable(name = "panier_produit",
               joinColumns = @JoinColumn(name="paniers_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="produits_id", referencedColumnName="ID"))
    private Set<Produit> produits = new HashSet<>();

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public Panier produits(Set<Produit> produits) {
        this.produits = produits;
        return this;
    }

    public Panier addProduit(Produit produit) {
        produits.add(produit);
        produit.getPaniers().add(this);
        return this;
    }

    public Panier removeProduit(Produit produit) {
        produits.remove(produit);
        produit.getPaniers().remove(this);
        return this;
    }

    public void setProduits(Set<Produit> produits) {
        this.produits = produits;
    }

    public User getUser() {
        return user;
    }

    public Panier user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Panier panier = (Panier) o;
        if(panier.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, panier.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Panier{" +
            "id=" + id +
            '}';
    }
}
