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
@Table(name = "DT_REPONSE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DtReponse.findAll", query = "SELECT d FROM DtReponse d")
    , @NamedQuery(name = "DtReponse.findByIdReponse", query = "SELECT d FROM DtReponse d WHERE d.idReponse = :idReponse")
    , @NamedQuery(name = "DtReponse.findByLibelleReponse", query = "SELECT d FROM DtReponse d WHERE d.libelleReponse = :libelleReponse")})
public class DtReponse implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_REPONSE")
    private Integer idReponse;
    @Size(max = 254)
    @Column(name = "LIBELLE_REPONSE")
    private String libelleReponse;
    @JoinColumn(name = "ID_QUESTION", referencedColumnName = "ID_QUESTION")
    @ManyToOne(optional = false)
    private DtQuestion idQuestion;
    @JoinColumn(name = "ID_TYPEREPONSE", referencedColumnName = "ID_TYPEREPONSE")
    @ManyToOne(optional = false)
    private DtTypereponse idTypereponse;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idReponse")
    private Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollection;

    public DtReponse() {
    }

    public DtReponse(Integer idReponse) {
        this.idReponse = idReponse;
    }

    public Integer getIdReponse() {
        return idReponse;
    }

    public void setIdReponse(Integer idReponse) {
        this.idReponse = idReponse;
    }

    public String getLibelleReponse() {
        return libelleReponse;
    }

    public void setLibelleReponse(String libelleReponse) {
        this.libelleReponse = libelleReponse;
    }

    public DtQuestion getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(DtQuestion idQuestion) {
        this.idQuestion = idQuestion;
    }

    public DtTypereponse getIdTypereponse() {
        return idTypereponse;
    }

    public void setIdTypereponse(DtTypereponse idTypereponse) {
        this.idTypereponse = idTypereponse;
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
        hash += (idReponse != null ? idReponse.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DtReponse)) {
            return false;
        }
        DtReponse other = (DtReponse) object;
        if ((this.idReponse == null && other.idReponse != null) || (this.idReponse != null && !this.idReponse.equals(other.idReponse))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.DtReponse[ idReponse=" + idReponse + " ]";
    }
    
}
