/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gaele
 */
@Entity
@Table(name = "TL_URL_FORMULAIRE_SONDE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TlUrlFormulaireSonde.findAll", query = "SELECT t FROM TlUrlFormulaireSonde t")
    , @NamedQuery(name = "TlUrlFormulaireSonde.findByIdTlUrlFormulaireSonde", query = "SELECT t FROM TlUrlFormulaireSonde t WHERE t.idTlUrlFormulaireSonde = :idTlUrlFormulaireSonde")
    , @NamedQuery(name = "TlUrlFormulaireSonde.findByUrlstring", query = "SELECT t FROM TlUrlFormulaireSonde t WHERE t.urlstring = :urlstring")})
public class TlUrlFormulaireSonde implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TL_URL_FORMULAIRE_SONDE")
    private Integer idTlUrlFormulaireSonde;
    @Size(max = 254)
    @Column(name = "URLSTRING")
    private String urlstring;
    @JoinColumn(name = "ID_FORMULAIRE", referencedColumnName = "ID_FORMULAIRE")
    @ManyToOne(optional = false)
    private DtFormulaire idFormulaire;
    @JoinColumn(name = "ID_SONDE", referencedColumnName = "ID_SONDE")
    @ManyToOne(optional = false)
    private DtSonde idSonde;

    public TlUrlFormulaireSonde() {
    }

    public TlUrlFormulaireSonde(Integer idTlUrlFormulaireSonde) {
        this.idTlUrlFormulaireSonde = idTlUrlFormulaireSonde;
    }

    public Integer getIdTlUrlFormulaireSonde() {
        return idTlUrlFormulaireSonde;
    }

    public void setIdTlUrlFormulaireSonde(Integer idTlUrlFormulaireSonde) {
        this.idTlUrlFormulaireSonde = idTlUrlFormulaireSonde;
    }

    public String getUrlstring() {
        return urlstring;
    }

    public void setUrlstring(String urlstring) {
        this.urlstring = urlstring;
    }

    public DtFormulaire getIdFormulaire() {
        return idFormulaire;
    }

    public void setIdFormulaire(DtFormulaire idFormulaire) {
        this.idFormulaire = idFormulaire;
    }

    public DtSonde getIdSonde() {
        return idSonde;
    }

    public void setIdSonde(DtSonde idSonde) {
        this.idSonde = idSonde;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTlUrlFormulaireSonde != null ? idTlUrlFormulaireSonde.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TlUrlFormulaireSonde)) {
            return false;
        }
        TlUrlFormulaireSonde other = (TlUrlFormulaireSonde) object;
        if ((this.idTlUrlFormulaireSonde == null && other.idTlUrlFormulaireSonde != null) || (this.idTlUrlFormulaireSonde != null && !this.idTlUrlFormulaireSonde.equals(other.idTlUrlFormulaireSonde))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.TlUrlFormulaireSonde[ idTlUrlFormulaireSonde=" + idTlUrlFormulaireSonde + " ]";
    }
    
}
