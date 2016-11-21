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
 * A Caracteristique.
 */
@Entity
@Table(name = "caracteristique")
@Document(indexName = "caracteristique")
public class Caracteristique implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "description", nullable = false)
    private String description;

    @ManyToMany(mappedBy = "caracteristiques")
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

    public Caracteristique name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Caracteristique description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public Caracteristique produits(Set<Produit> produits) {
        this.produits = produits;
        return this;
    }

    public Caracteristique addProduit(Produit produit) {
        produits.add(produit);
        produit.getCaracteristiques().add(this);
        return this;
    }

    public Caracteristique removeProduit(Produit produit) {
        produits.remove(produit);
        produit.getCaracteristiques().remove(this);
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
        Caracteristique caracteristique = (Caracteristique) o;
        if(caracteristique.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, caracteristique.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Caracteristique{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            '}';
    }
}
