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
@Table(name = "DT_TYPEREPONSE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DtTypereponse.findAll", query = "SELECT d FROM DtTypereponse d")
    , @NamedQuery(name = "DtTypereponse.findByIdTypereponse", query = "SELECT d FROM DtTypereponse d WHERE d.idTypereponse = :idTypereponse")
    , @NamedQuery(name = "DtTypereponse.findByLibelleTypereponse", query = "SELECT d FROM DtTypereponse d WHERE d.libelleTypereponse = :libelleTypereponse")})
public class DtTypereponse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TYPEREPONSE")
    private Integer idTypereponse;
    @Size(max = 254)
    @Column(name = "LIBELLE_TYPEREPONSE")
    private String libelleTypereponse;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idTypereponse")
    private Collection<DtReponse> dtReponseCollection;

    public DtTypereponse() {
    }

    public DtTypereponse(Integer idTypereponse) {
        this.idTypereponse = idTypereponse;
    }

    public Integer getIdTypereponse() {
        return idTypereponse;
    }

    public void setIdTypereponse(Integer idTypereponse) {
        this.idTypereponse = idTypereponse;
    }

    public String getLibelleTypereponse() {
        return libelleTypereponse;
    }

    public void setLibelleTypereponse(String libelleTypereponse) {
        this.libelleTypereponse = libelleTypereponse;
    }

    @XmlTransient
    public Collection<DtReponse> getDtReponseCollection() {
        return dtReponseCollection;
    }

    public void setDtReponseCollection(Collection<DtReponse> dtReponseCollection) {
        this.dtReponseCollection = dtReponseCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTypereponse != null ? idTypereponse.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DtTypereponse)) {
            return false;
        }
        DtTypereponse other = (DtTypereponse) object;
        if ((this.idTypereponse == null && other.idTypereponse != null) || (this.idTypereponse != null && !this.idTypereponse.equals(other.idTypereponse))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DtTypereponse[ idTypereponse=" + idTypereponse + " ]";
    }
    
}
