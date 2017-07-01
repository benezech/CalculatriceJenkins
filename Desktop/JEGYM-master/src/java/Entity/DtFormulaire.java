/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gaele
 */
@Entity
@Table(name = "DT_FORMULAIRE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DtFormulaire.findAll", query = "SELECT d FROM DtFormulaire d")
    , @NamedQuery(name = "DtFormulaire.findByIdFormulaire", query = "SELECT d FROM DtFormulaire d WHERE d.idFormulaire = :idFormulaire")
    , @NamedQuery(name = "DtFormulaire.findByNomFormulaire", query = "SELECT d FROM DtFormulaire d WHERE d.nomFormulaire = :nomFormulaire")
    , @NamedQuery(name = "DtFormulaire.findByExpirationFormulaire", query = "SELECT d FROM DtFormulaire d WHERE d.expirationFormulaire = :expirationFormulaire")})
public class DtFormulaire implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FORMULAIRE")
    private Integer idFormulaire;
    @Size(max = 254)
    @Column(name = "NOM_FORMULAIRE")
    private String nomFormulaire;
    @Column(name = "EXPIRATION_FORMULAIRE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date expirationFormulaire;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFormulaire")
    private Collection<DtQuestion> dtQuestionCollection;
    @JoinColumn(name = "SIRET_ENTREPRISE", referencedColumnName = "SIRET_ENTREPRISE")
    @ManyToOne(optional = false)
    private DtEntreprise siretEntreprise;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFormulaire")
    private Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollection;

    public DtFormulaire() {
    }

    public DtFormulaire(Integer idFormulaire) {
        this.idFormulaire = idFormulaire;
    }

    public Integer getIdFormulaire() {
        return idFormulaire;
    }

    public void setIdFormulaire(Integer idFormulaire) {
        this.idFormulaire = idFormulaire;
    }

    public String getNomFormulaire() {
        return nomFormulaire;
    }

    public void setNomFormulaire(String nomFormulaire) {
        this.nomFormulaire = nomFormulaire;
    }

    public Date getExpirationFormulaire() {
        return expirationFormulaire;
    }

    public void setExpirationFormulaire(Date expirationFormulaire) {
        this.expirationFormulaire = expirationFormulaire;
    }

    @XmlTransient
    public Collection<DtQuestion> getDtQuestionCollection() {
        return dtQuestionCollection;
    }

    public void setDtQuestionCollection(Collection<DtQuestion> dtQuestionCollection) {
        this.dtQuestionCollection = dtQuestionCollection;
    }

    public DtEntreprise getSiretEntreprise() {
        return siretEntreprise;
    }

    public void setSiretEntreprise(DtEntreprise siretEntreprise) {
        this.siretEntreprise = siretEntreprise;
    }

    @XmlTransient
    public Collection<TlUrlFormulaireSonde> getTlUrlFormulaireSondeCollection() {
        return tlUrlFormulaireSondeCollection;
    }

    public void setTlUrlFormulaireSondeCollection(Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollection) {
        this.tlUrlFormulaireSondeCollection = tlUrlFormulaireSondeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idFormulaire != null ? idFormulaire.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DtFormulaire)) {
            return false;
        }
        DtFormulaire other = (DtFormulaire) object;
        if ((this.idFormulaire == null && other.idFormulaire != null) || (this.idFormulaire != null && !this.idFormulaire.equals(other.idFormulaire))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DtFormulaire[ idFormulaire=" + idFormulaire + " ]";
    }
    
}
