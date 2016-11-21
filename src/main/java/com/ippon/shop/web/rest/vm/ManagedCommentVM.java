package com.ippon.shop.web.rest.vm;

import java.time.ZonedDateTime;

/**
 * Created by Zakaria on 20/11/2016.
 */
public class ManagedCommentVM {

    private Long id;

    private Integer note;

    private String avis;

    private Long produitId;

    private ZonedDateTime dateCreation;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNote() {
        return note;
    }

    public void setNote(Integer note) {
        this.note = note;
    }

    public String getAvis() {
        return avis;
    }

    public void setAvis(String avis) {
        this.avis = avis;
    }

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public ZonedDateTime getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(ZonedDateTime dateCreation) {
        this.dateCreation = dateCreation;
    }

    public ManagedCommentVM() {
    }

    public ManagedCommentVM(Long id, Integer note, String avis, Long produitId, ZonedDateTime dateCreation) {
        this.id = id;
        this.note = note;
        this.avis = avis;
        this.produitId = produitId;
        this.dateCreation = dateCreation;
    }

    @Override
    public String toString() {
        return "ManagedCommentVM{" +
            "id=" + id +
            ", note='" + note + '\'' +
            ", avis='" + avis + '\'' +
            ", produitId=" + produitId +
            ", dateCreation=" + dateCreation +
            '}';
    }
}
