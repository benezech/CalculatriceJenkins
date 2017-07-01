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
@Table(name = "DT_TYPESONDE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DtTypesonde.findAll", query = "SELECT d FROM DtTypesonde d")
    , @NamedQuery(name = "DtTypesonde.findByIdTypesonde", query = "SELECT d FROM DtTypesonde d WHERE d.idTypesonde = :idTypesonde")
    , @NamedQuery(name = "DtTypesonde.findByLibelleTypesonde", query = "SELECT d FROM DtTypesonde d WHERE d.libelleTypesonde = :libelleTypesonde")})
public class DtTypesonde implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TYPESONDE")
    private Integer idTypesonde;
    @Size(max = 254)
    @Column(name = "LIBELLE_TYPESONDE")
    private String libelleTypesonde;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTypesonde")
    private Collection<DtSonde> dtSondeCollection;

    public DtTypesonde() {
    }

    public DtTypesonde(Integer idTypesonde) {
        this.idTypesonde = idTypesonde;
    }

    public Integer getIdTypesonde() {
        return idTypesonde;
    }

    public void setIdTypesonde(Integer idTypesonde) {
        this.idTypesonde = idTypesonde;
    }

    public String getLibelleTypesonde() {
        return libelleTypesonde;
    }

    public void setLibelleTypesonde(String libelleTypesonde) {
        this.libelleTypesonde = libelleTypesonde;
    }

    @XmlTransient
    public Collection<DtSonde> getDtSondeCollection() {
        return dtSondeCollection;
    }

    public void setDtSondeCollection(Collection<DtSonde> dtSondeCollection) {
        this.dtSondeCollection = dtSondeCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTypesonde != null ? idTypesonde.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DtTypesonde)) {
            return false;
        }
        DtTypesonde other = (DtTypesonde) object;
        if ((this.idTypesonde == null && other.idTypesonde != null) || (this.idTypesonde != null && !this.idTypesonde.equals(other.idTypesonde))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DtTypesonde[ idTypesonde=" + idTypesonde + " ]";
    }
    
}
