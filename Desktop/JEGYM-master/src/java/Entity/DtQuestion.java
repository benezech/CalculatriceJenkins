/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gaele
 */
@Entity
@Table(name = "DT_QUESTION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DtQuestion.findAll", query = "SELECT d FROM DtQuestion d")
    , @NamedQuery(name = "DtQuestion.findByIdQuestion", query = "SELECT d FROM DtQuestion d WHERE d.idQuestion = :idQuestion")
    , @NamedQuery(name = "DtQuestion.findByTitreQuestion", query = "SELECT d FROM DtQuestion d WHERE d.titreQuestion = :titreQuestion")
    , @NamedQuery(name = "DtQuestion.findByDescriptionQuestion", query = "SELECT d FROM DtQuestion d WHERE d.descriptionQuestion = :descriptionQuestion")
    , @NamedQuery(name = "DtQuestion.findByIdTyperep", query = "SELECT d FROM DtQuestion d WHERE d.idTyperep = :idTyperep")})
public class DtQuestion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_QUESTION")
    private Integer idQuestion;
    @Size(max = 254)
    @Column(name = "TITRE_QUESTION")
    private String titreQuestion;
    @Size(max = 254)
    @Column(name = "DESCRIPTION_QUESTION")
    private String descriptionQuestion;
    @Column(name = "ID_TYPEREP")
    private Integer idTyperep;
    @JoinColumn(name = "ID_FORMULAIRE", referencedColumnName = "ID_FORMULAIRE")
    @ManyToOne(optional = false)
    private DtFormulaire idFormulaire;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idQuestion")
    private Collection<DtReponse> dtReponseCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idQuestion")
    private Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollection;

    public DtQuestion() {
    }

    public DtQuestion(Integer idQuestion) {
        this.idQuestion = idQuestion;
    }

    public Integer getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(Integer idQuestion) {
        this.idQuestion = idQuestion;
    }

    public String getTitreQuestion() {
        return titreQuestion;
    }

    public void setTitreQuestion(String titreQuestion) {
        this.titreQuestion = titreQuestion;
    }

    public String getDescriptionQuestion() {
        return descriptionQuestion;
    }

    public void setDescriptionQuestion(String descriptionQuestion) {
        this.descriptionQuestion = descriptionQuestion;
    }

    public Integer getIdTyperep() {
        return idTyperep;
    }

    public void setIdTyperep(Integer idTyperep) {
        this.idTyperep = idTyperep;
    }

    public DtFormulaire getIdFormulaire() {
        return idFormulaire;
    }

    public void setIdFormulaire(DtFormulaire idFormulaire) {
        this.idFormulaire = idFormulaire;
    }

    @XmlTransient
    public Collection<DtReponse> getDtReponseCollection() {
        return dtReponseCollection;
    }

    public void setDtReponseCollection(Collection<DtReponse> dtReponseCollection) {
        this.dtReponseCollection = dtReponseCollection;
    }

    @XmlTransient
    public Collection<TlQuestionReponseSonde> getTlQuestionReponseSondeCollection() {
        return tlQuestionReponseSondeCollection;
    }

    public void setTlQuestionReponseSondeCollection(Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollection) {
        this.tlQuestionReponseSondeCollection = tlQuestionReponseSondeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idQuestion != null ? idQuestion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DtQuestion)) {
            return false;
        }
        DtQuestion other = (DtQuestion) object;
        if ((this.idQuestion == null && other.idQuestion != null) || (this.idQuestion != null && !this.idQuestion.equals(other.idQuestion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DtQuestion[ idQuestion=" + idQuestion + " ]";
    }
    
}
