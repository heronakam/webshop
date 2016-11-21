package com.ippon.shop.domain;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Commentaire.
 */
@Entity
@Table(name = "commentaire")
@Document(indexName = "commentaire")
public class Commentaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Max(value = 5)
    @Column(name = "note", nullable = false)
    private Integer note;

    @Column(name = "avis")
    private String avis;

    @ManyToOne
    @NotNull
    private User user;

    @ManyToOne
    @NotNull
    private Produit produit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNote() {
        return note;
    }

    public Commentaire note(Integer note) {
        this.note = note;
        return this;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getAvis() {
        return avis;
    }

    public Commentaire avis(String avis) {
        this.avis = avis;
        return this;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public User getUser() {
        return user;
    }

    public Commentaire user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Produit getProduit() {
        return produit;
    }

    public Commentaire produit(Produit produit) {
        this.produit = produit;
        return this;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Commentaire commentaire = (Commentaire) o;
        if(commentaire.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, commentaire.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Commentaire{" +
            "id=" + id +
            ", note='" + note + "'" +
            ", avis='" + avis + "'" +
            '}';
    }
}
