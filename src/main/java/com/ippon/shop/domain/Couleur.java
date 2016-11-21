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
 * A Couleur.
 */
@Entity
@Table(name = "couleur")
@Document(indexName = "couleur")
public class Couleur implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "code_hexadecimal")
    private String codeHexadecimal;

    @ManyToMany(mappedBy = "couleurs")
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

    public Couleur name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCodeHexadecimal() {
        return codeHexadecimal;
    }

    public Couleur codeHexadecimal(String codeHexadecimal) {
        this.codeHexadecimal = codeHexadecimal;
        return this;
    }

    public void setCodeHexadecimal(String codeHexadecimal) {
        this.codeHexadecimal = codeHexadecimal;
    }

    public Set<Produit> getProduits() {
        return produits;
    }

    public Couleur produits(Set<Produit> produits) {
        this.produits = produits;
        return this;
    }

    public Couleur addProduit(Produit produit) {
        produits.add(produit);
        produit.getCouleurs().add(this);
        return this;
    }

    public Couleur removeProduit(Produit produit) {
        produits.remove(produit);
        produit.getCouleurs().remove(this);
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
        Couleur couleur = (Couleur) o;
        if(couleur.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, couleur.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Couleur{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", codeHexadecimal='" + codeHexadecimal + "'" +
            '}';
    }
}
