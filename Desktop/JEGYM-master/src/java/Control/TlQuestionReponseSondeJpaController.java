/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Control.exceptions.NonexistentEntityException;
import Control.exceptions.PreexistingEntityException;
import Control.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.DtQuestion;
import Entity.DtReponse;
import Entity.DtSonde;
import Entity.TlQuestionReponseSonde;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author gaele
 */
public class TlQuestionReponseSondeJpaController implements Serializable {

    public TlQuestionReponseSondeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TlQuestionReponseSonde tlQuestionReponseSonde) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtQuestion idQuestion = tlQuestionReponseSonde.getIdQuestion();
            if (idQuestion != null) {
                idQuestion = em.getReference(idQuestion.getClass(), idQuestion.getIdQuestion());
                tlQuestionReponseSonde.setIdQuestion(idQuestion);
            }
            DtReponse idReponse = tlQuestionReponseSonde.getIdReponse();
            if (idReponse != null) {
                idReponse = em.getReference(idReponse.getClass(), idReponse.getIdReponse());
                tlQuestionReponseSonde.setIdReponse(idReponse);
            }
            DtSonde idSonde = tlQuestionReponseSonde.getIdSonde();
            if (idSonde != null) {
                idSonde = em.getReference(idSonde.getClass(), idSonde.getIdSonde());
                tlQuestionReponseSonde.setIdSonde(idSonde);
            }
            em.persist(tlQuestionReponseSonde);
            if (idQuestion != null) {
                idQuestion.getTlQuestionReponseSondeCollection().add(tlQuestionReponseSonde);
                idQuestion = em.merge(idQuestion);
            }
            if (idReponse != null) {
                idReponse.getTlQuestionReponseSondeCollection().add(tlQuestionReponseSonde);
                idReponse = em.merge(idReponse);
            }
            if (idSonde != null) {
                idSonde.getTlQuestionReponseSondeCollection().add(tlQuestionReponseSonde);
                idSonde = em.merge(idSonde);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTlQuestionReponseSonde(tlQuestionReponseSonde.getIdTlQuestionReponseSonde()) != null) {
                throw new PreexistingEntityException("TlQuestionReponseSonde " + tlQuestionReponseSonde + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TlQuestionReponseSonde tlQuestionReponseSonde) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TlQuestionReponseSonde persistentTlQuestionReponseSonde = em.find(TlQuestionReponseSonde.class, tlQuestionReponseSonde.getIdTlQuestionReponseSonde());
            DtQuestion idQuestionOld = persistentTlQuestionReponseSonde.getIdQuestion();
            DtQuestion idQuestionNew = tlQuestionReponseSonde.getIdQuestion();
            DtReponse idReponseOld = persistentTlQuestionReponseSonde.getIdReponse();
            DtReponse idReponseNew = tlQuestionReponseSonde.getIdReponse();
            DtSonde idSondeOld = persistentTlQuestionReponseSonde.getIdSonde();
            DtSonde idSondeNew = tlQuestionReponseSonde.getIdSonde();
            if (idQuestionNew != null) {
                idQuestionNew = em.getReference(idQuestionNew.getClass(), idQuestionNew.getIdQuestion());
                tlQuestionReponseSonde.setIdQuestion(idQuestionNew);
            }
            if (idReponseNew != null) {
                idReponseNew = em.getReference(idReponseNew.getClass(), idReponseNew.getIdReponse());
                tlQuestionReponseSonde.setIdReponse(idReponseNew);
            }
            if (idSondeNew != null) {
                idSondeNew = em.getReference(idSondeNew.getClass(), idSondeNew.getIdSonde());
                tlQuestionReponseSonde.setIdSonde(idSondeNew);
            }
            tlQuestionReponseSonde = em.merge(tlQuestionReponseSonde);
            if (idQuestionOld != null && !idQuestionOld.equals(idQuestionNew)) {
                idQuestionOld.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSonde);
                idQuestionOld = em.merge(idQuestionOld);
            }
            if (idQuestionNew != null && !idQuestionNew.equals(idQuestionOld)) {
                idQuestionNew.getTlQuestionReponseSondeCollection().add(tlQuestionReponseSonde);
                idQuestionNew = em.merge(idQuestionNew);
            }
            if (idReponseOld != null && !idReponseOld.equals(idReponseNew)) {
                idReponseOld.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSonde);
                idReponseOld = em.merge(idReponseOld);
            }
            if (idReponseNew != null && !idReponseNew.equals(idReponseOld)) {
                idReponseNew.getTlQuestionReponseSondeCollection().add(tlQuestionReponseSonde);
                idReponseNew = em.merge(idReponseNew);
            }
            if (idSondeOld != null && !idSondeOld.equals(idSondeNew)) {
                idSondeOld.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSonde);
                idSondeOld = em.merge(idSondeOld);
            }
            if (idSondeNew != null && !idSondeNew.equals(idSondeOld)) {
                idSondeNew.getTlQuestionReponseSondeCollection().add(tlQuestionReponseSonde);
                idSondeNew = em.merge(idSondeNew);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = tlQuestionReponseSonde.getIdTlQuestionReponseSonde();
                if (findTlQuestionReponseSonde(id) == null) {
                    throw new NonexistentEntityException("The tlQuestionReponseSonde with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TlQuestionReponseSonde tlQuestionReponseSonde;
            try {
                tlQuestionReponseSonde = em.getReference(TlQuestionReponseSonde.class, id);
                tlQuestionReponseSonde.getIdTlQuestionReponseSonde();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tlQuestionReponseSonde with id " + id + " no longer exists.", enfe);
            }
            DtQuestion idQuestion = tlQuestionReponseSonde.getIdQuestion();
            if (idQuestion != null) {
                idQuestion.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSonde);
                idQuestion = em.merge(idQuestion);
            }
            DtReponse idReponse = tlQuestionReponseSonde.getIdReponse();
            if (idReponse != null) {
                idReponse.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSonde);
                idReponse = em.merge(idReponse);
            }
            DtSonde idSonde = tlQuestionReponseSonde.getIdSonde();
            if (idSonde != null) {
                idSonde.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSonde);
                idSonde = em.merge(idSonde);
            }
            em.remove(tlQuestionReponseSonde);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TlQuestionReponseSonde> findTlQuestionReponseSondeEntities() {
        return findTlQuestionReponseSondeEntities(true, -1, -1);
    }

    public List<TlQuestionReponseSonde> findTlQuestionReponseSondeEntities(int maxResults, int firstResult) {
        return findTlQuestionReponseSondeEntities(false, maxResults, firstResult);
    }

    private List<TlQuestionReponseSonde> findTlQuestionReponseSondeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TlQuestionReponseSonde.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public TlQuestionReponseSonde findTlQuestionReponseSonde(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TlQuestionReponseSonde.class, id);
        } finally {
            em.close();
        }
    }

    public int getTlQuestionReponseSondeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TlQuestionReponseSonde> rt = cq.from(TlQuestionReponseSonde.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
