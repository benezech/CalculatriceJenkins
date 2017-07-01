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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gaele
 */
@Entity
@Table(name = "TL_QUESTION_REPONSE_SONDE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TlQuestionReponseSonde.findAll", query = "SELECT t FROM TlQuestionReponseSonde t")
    , @NamedQuery(name = "TlQuestionReponseSonde.findByIdTlQuestionReponseSonde", query = "SELECT t FROM TlQuestionReponseSonde t WHERE t.idTlQuestionReponseSonde = :idTlQuestionReponseSonde")})
public class TlQuestionReponseSonde implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_TL_QUESTION_REPONSE_SONDE")
    private Integer idTlQuestionReponseSonde;
    @JoinColumn(name = "ID_QUESTION", referencedColumnName = "ID_QUESTION")
    @ManyToOne(optional = false)
    private DtQuestion idQuestion;
    @JoinColumn(name = "ID_REPONSE", referencedColumnName = "ID_REPONSE")
    @ManyToOne(optional = false)
    private DtReponse idReponse;
    @JoinColumn(name = "ID_SONDE", referencedColumnName = "ID_SONDE")
    @ManyToOne(optional = false)
    private DtSonde idSonde;

    public TlQuestionReponseSonde() {
    }

    public TlQuestionReponseSonde(Integer idTlQuestionReponseSonde) {
        this.idTlQuestionReponseSonde = idTlQuestionReponseSonde;
    }

    public Integer getIdTlQuestionReponseSonde() {
        return idTlQuestionReponseSonde;
    }

    public void setIdTlQuestionReponseSonde(Integer idTlQuestionReponseSonde) {
        this.idTlQuestionReponseSonde = idTlQuestionReponseSonde;
    }

    public DtQuestion getIdQuestion() {
        return idQuestion;
    }

    public void setIdQuestion(DtQuestion idQuestion) {
        this.idQuestion = idQuestion;
    }

    public DtReponse getIdReponse() {
        return idReponse;
    }

    public void setIdReponse(DtReponse idReponse) {
        this.idReponse = idReponse;
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
        hash += (idTlQuestionReponseSonde != null ? idTlQuestionReponseSonde.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TlQuestionReponseSonde)) {
            return false;
        }
        TlQuestionReponseSonde other = (TlQuestionReponseSonde) object;
        if ((this.idTlQuestionReponseSonde == null && other.idTlQuestionReponseSonde != null) || (this.idTlQuestionReponseSonde != null && !this.idTlQuestionReponseSonde.equals(other.idTlQuestionReponseSonde))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Entity.TlQuestionReponseSonde[ idTlQuestionReponseSonde=" + idTlQuestionReponseSonde + " ]";
    }
    
}
