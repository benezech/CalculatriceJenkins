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
@Table(name = "DT_ENTREPRISE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DtEntreprise.findAll", query = "SELECT d FROM DtEntreprise d")
    , @NamedQuery(name = "DtEntreprise.findBySiretEntreprise", query = "SELECT d FROM DtEntreprise d WHERE d.siretEntreprise = :siretEntreprise")
    , @NamedQuery(name = "DtEntreprise.findByRaisonSocialEntreprise", query = "SELECT d FROM DtEntreprise d WHERE d.raisonSocialEntreprise = :raisonSocialEntreprise")
    , @NamedQuery(name = "DtEntreprise.findByAdresseEntreprise", query = "SELECT d FROM DtEntreprise d WHERE d.adresseEntreprise = :adresseEntreprise")
    , @NamedQuery(name = "DtEntreprise.findByCpEntreprise", query = "SELECT d FROM DtEntreprise d WHERE d.cpEntreprise = :cpEntreprise")
    , @NamedQuery(name = "DtEntreprise.findByVilleEntreprise", query = "SELECT d FROM DtEntreprise d WHERE d.villeEntreprise = :villeEntreprise")
    , @NamedQuery(name = "DtEntreprise.findByTelEntreprise", query = "SELECT d FROM DtEntreprise d WHERE d.telEntreprise = :telEntreprise")
    , @NamedQuery(name = "DtEntreprise.findByEmailEntreprise", query = "SELECT d FROM DtEntreprise d WHERE d.emailEntreprise = :emailEntreprise")
    , @NamedQuery(name = "DtEntreprise.findByPwdEntreprise", query = "SELECT d FROM DtEntreprise d WHERE d.pwdEntreprise = :pwdEntreprise")})
public class DtEntreprise implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 14)
    @Column(name = "SIRET_ENTREPRISE")
    private String siretEntreprise;
    @Size(max = 254)
    @Column(name = "RAISON_SOCIAL_ENTREPRISE")
    private String raisonSocialEntreprise;
    @Size(max = 254)
    @Column(name = "ADRESSE_ENTREPRISE")
    private String adresseEntreprise;
    @Size(max = 5)
    @Column(name = "CP_ENTREPRISE")
    private String cpEntreprise;
    @Size(max = 88)
    @Column(name = "VILLE_ENTREPRISE")
    private String villeEntreprise;
    @Size(max = 10)
    @Column(name = "TEL_ENTREPRISE")
    private String telEntreprise;
    @Size(max = 80)
    @Column(name = "EMAIL_ENTREPRISE")
    private String emailEntreprise;
    @Size(max = 254)
    @Column(name = "PWD_ENTREPRISE")
    private String pwdEntreprise;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "siretEntreprise")
    private Collection<DtFormulaire> dtFormulaireCollection;

    public DtEntreprise() {
    }

    public DtEntreprise(String siretEntreprise) {
        this.siretEntreprise = siretEntreprise;
    }

    public String getSiretEntreprise() {
        return siretEntreprise;
    }

    public void setSiretEntreprise(String siretEntreprise) {
        this.siretEntreprise = siretEntreprise;
    }

    public String getRaisonSocialEntreprise() {
        return raisonSocialEntreprise;
    }

    public void setRaisonSocialEntreprise(String raisonSocialEntreprise) {
        this.raisonSocialEntreprise = raisonSocialEntreprise;
    }

    public String getAdresseEntreprise() {
        return adresseEntreprise;
    }

    public void setAdresseEntreprise(String adresseEntreprise) {
        this.adresseEntreprise = adresseEntreprise;
    }

    public String getCpEntreprise() {
        return cpEntreprise;
    }

    public void setCpEntreprise(String cpEntreprise) {
        this.cpEntreprise = cpEntreprise;
    }

    public String getVilleEntreprise() {
        return villeEntreprise;
    }

    public void setVilleEntreprise(String villeEntreprise) {
        this.villeEntreprise = villeEntreprise;
    }

    public String getTelEntreprise() {
        return telEntreprise;
    }

    public void setTelEntreprise(String telEntreprise) {
        this.telEntreprise = telEntreprise;
    }

    public String getEmailEntreprise() {
        return emailEntreprise;
    }

    public void setEmailEntreprise(String emailEntreprise) {
        this.emailEntreprise = emailEntreprise;
    }

    public String getPwdEntreprise() {
        return pwdEntreprise;
    }

    public void setPwdEntreprise(String pwdEntreprise) {
        this.pwdEntreprise = pwdEntreprise;
    }

    @XmlTransient
    public Collection<DtFormulaire> getDtFormulaireCollection() {
        return dtFormulaireCollection;
    }

    public void setDtFormulaireCollection(Collection<DtFormulaire> dtFormulaireCollection) {
        this.dtFormulaireCollection = dtFormulaireCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (siretEntreprise != null ? siretEntreprise.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DtEntreprise)) {
            return false;
        }
        DtEntreprise other = (DtEntreprise) object;
        if ((this.siretEntreprise == null && other.siretEntreprise != null) || (this.siretEntreprise != null && !this.siretEntreprise.equals(other.siretEntreprise))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DtEntreprise[ siretEntreprise=" + siretEntreprise + " ]";
    }
    
}
