package com.ippon.shop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Produit.
 */
@Entity
@Table(name = "produit")
@Document(indexName = "produit")
public class Produit implements Serializable {

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

    @NotNull
    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @NotNull
    @Column(name = "prix_unitaire", precision=10, scale=2, nullable = false)
    private BigDecimal prixUnitaire;

    @Column(name = "solde")
    private Float solde;

    @NotNull
    @Column(name = "qte_en_stock", nullable = false)
    private Integer qteEnStock;

    @ManyToOne
    @NotNull
    private Categorie categorie;

    @ManyToMany
    @NotNull
    @JoinTable(name = "produit_caracteristique",
               joinColumns = @JoinColumn(name="produits_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="caracteristiques_id", referencedColumnName="ID"))
    private Set<Caracteristique> caracteristiques = new HashSet<>();

    @ManyToOne
    @NotNull
    private Marque marque;

    @OneToMany(mappedBy = "produit")
    private Set<Commentaire> commentaires = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "produit_couleur",
               joinColumns = @JoinColumn(name="produits_id", referencedColumnName="ID"),
               inverseJoinColumns = @JoinColumn(name="couleurs_id", referencedColumnName="ID"))
    private Set<Couleur> couleurs = new HashSet<>();

    @ManyToMany(mappedBy = "produits")
    @JsonIgnore
    private Set<Panier> paniers = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Produit name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Produit description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Produit imageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public Produit prixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        return this;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Float getSolde() {
        return solde;
    }

    public Produit solde(Float solde) {
        this.solde = solde;
        return this;
    }

    public void setSolde(Float solde) {
        this.solde = solde;
    }

    public Integer getQteEnStock() {
        return qteEnStock;
    }

    public Produit qteEnStock(Integer qteEnStock) {
        this.qteEnStock = qteEnStock;
        return this;
    }

    public void setQteEnStock(Integer qteEnStock) {
        this.qteEnStock = qteEnStock;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public Produit categorie(Categorie categorie) {
        this.categorie = categorie;
        return this;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }

    public Set<Caracteristique> getCaracteristiques() {
        return caracteristiques;
    }

    public Produit caracteristiques(Set<Caracteristique> caracteristiques) {
        this.caracteristiques = caracteristiques;
        return this;
    }

    public Produit addCaracteristique(Caracteristique caracteristique) {
        caracteristiques.add(caracteristique);
        caracteristique.getProduits().add(this);
        return this;
    }

    public Produit removeCaracteristique(Caracteristique caracteristique) {
        caracteristiques.remove(caracteristique);
        caracteristique.getProduits().remove(this);
        return this;
    }

    public void setCaracteristiques(Set<Caracteristique> caracteristiques) {
        this.caracteristiques = caracteristiques;
    }

    public Marque getMarque() {
        return marque;
    }

    public Produit marque(Marque marque) {
        this.marque = marque;
        return this;
    }

    public void setMarque(Marque marque) {
        this.marque = marque;
    }

    public Set<Commentaire> getCommentaires() {
        return commentaires;
    }

    public Produit commentaires(Set<Commentaire> commentaires) {
        this.commentaires = commentaires;
        return this;
    }

    public Produit addCommentaire(Commentaire commentaire) {
        commentaires.add(commentaire);
        commentaire.setProduit(this);
        return this;
    }

    public Produit removeCommentaire(Commentaire commentaire) {
        commentaires.remove(commentaire);
        commentaire.setProduit(null);
        return this;
    }

    public void setCommentaires(Set<Commentaire> commentaires) {
        this.commentaires = commentaires;
    }

    public Set<Couleur> getCouleurs() {
        return couleurs;
    }

    public Produit couleurs(Set<Couleur> couleurs) {
        this.couleurs = couleurs;
        return this;
    }

    public Produit addCouleur(Couleur couleur) {
        couleurs.add(couleur);
        couleur.getProduits().add(this);
        return this;
    }

    public Produit removeCouleur(Couleur couleur) {
        couleurs.remove(couleur);
        couleur.getProduits().remove(this);
        return this;
    }

    public void setCouleurs(Set<Couleur> couleurs) {
        this.couleurs = couleurs;
    }

    public Set<Panier> getPaniers() {
        return paniers;
    }

    public Produit paniers(Set<Panier> paniers) {
        this.paniers = paniers;
        return this;
    }

    public Produit addPanier(Panier panier) {
        paniers.add(panier);
        panier.getProduits().add(this);
        return this;
    }

    public Produit removePanier(Panier panier) {
        paniers.remove(panier);
        panier.getProduits().remove(this);
        return this;
    }

    public void setPaniers(Set<Panier> paniers) {
        this.paniers = paniers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Produit produit = (Produit) o;
        if(produit.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, produit.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Produit{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", description='" + description + "'" +
            ", imageUrl='" + imageUrl + "'" +
            ", prixUnitaire='" + prixUnitaire + "'" +
            ", solde='" + solde + "'" +
            ", qteEnStock='" + qteEnStock + "'" +
            '}';
    }
}
