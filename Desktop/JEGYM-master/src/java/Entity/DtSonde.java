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
@Table(name = "DT_SONDE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DtSonde.findAll", query = "SELECT d FROM DtSonde d")
    , @NamedQuery(name = "DtSonde.findByIdSonde", query = "SELECT d FROM DtSonde d WHERE d.idSonde = :idSonde")
    , @NamedQuery(name = "DtSonde.findByNomSonde", query = "SELECT d FROM DtSonde d WHERE d.nomSonde = :nomSonde")
    , @NamedQuery(name = "DtSonde.findByPrenomSonde", query = "SELECT d FROM DtSonde d WHERE d.prenomSonde = :prenomSonde")
    , @NamedQuery(name = "DtSonde.findByTypeSonde", query = "SELECT d FROM DtSonde d WHERE d.typeSonde = :typeSonde")
    , @NamedQuery(name = "DtSonde.findByMailSonde", query = "SELECT d FROM DtSonde d WHERE d.mailSonde = :mailSonde")
    , @NamedQuery(name = "DtSonde.findByTelSonde", query = "SELECT d FROM DtSonde d WHERE d.telSonde = :telSonde")})
public class DtSonde implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_SONDE")
    private Integer idSonde;
    @Size(max = 100)
    @Column(name = "NOM_SONDE")
    private String nomSonde;
    @Size(max = 100)
    @Column(name = "PRENOM_SONDE")
    private String prenomSonde;
    @Column(name = "TYPE_SONDE")
    private Integer typeSonde;
    @Size(max = 150)
    @Column(name = "MAIL_SONDE")
    private String mailSonde;
    @Size(max = 10)
    @Column(name = "TEL_SONDE")
    private String telSonde;
    @JoinColumn(name = "ID_TYPESONDE", referencedColumnName = "ID_TYPESONDE")
    @ManyToOne(optional = false)
    private DtTypesonde idTypesonde;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSonde")
    private Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idSonde")
    private Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollection;

    public DtSonde() {
    }

    public DtSonde(Integer idSonde) {
        this.idSonde = idSonde;
    }

    public Integer getIdSonde() {
        return idSonde;
    }

    public void setIdSonde(Integer idSonde) {
        this.idSonde = idSonde;
    }

    public String getNomSonde() {
        return nomSonde;
    }

    public void setNomSonde(String nomSonde) {
        this.nomSonde = nomSonde;
    }

    public String getPrenomSonde() {
        return prenomSonde;
    }

    public void setPrenomSonde(String prenomSonde) {
        this.prenomSonde = prenomSonde;
    }

    public Integer getTypeSonde() {
        return typeSonde;
    }

    public void setTypeSonde(Integer typeSonde) {
        this.typeSonde = typeSonde;
    }

    public String getMailSonde() {
        return mailSonde;
    }

    public void setMailSonde(String mailSonde) {
        this.mailSonde = mailSonde;
    }

    public String getTelSonde() {
        return telSonde;
    }

    public void setTelSonde(String telSonde) {
        this.telSonde = telSonde;
    }

    public DtTypesonde getIdTypesonde() {
        return idTypesonde;
    }

    public void setIdTypesonde(DtTypesonde idTypesonde) {
        this.idTypesonde = idTypesonde;
    }

    @XmlTransient
    public Collection<TlUrlFormulaireSonde> getTlUrlFormulaireSondeCollection() {
        return tlUrlFormulaireSondeCollection;
    }

    public void setTlUrlFormulaireSondeCollection(Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollection) {
        this.tlUrlFormulaireSondeCollection = tlUrlFormulaireSondeCollection;
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
        hash += (idSonde != null ? idSonde.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DtSonde)) {
            return false;
        }
        DtSonde other = (DtSonde) object;
        if ((this.idSonde == null && other.idSonde != null) || (this.idSonde != null && !this.idSonde.equals(other.idSonde))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DtSonde[ idSonde=" + idSonde + " ]";
    }
    
}
