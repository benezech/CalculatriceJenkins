/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Control.exceptions.IllegalOrphanException;
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
import Entity.DtTypereponse;
import Entity.TlQuestionReponseSonde;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author gaele
 */
public class DtReponseJpaController implements Serializable {

    public DtReponseJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DtReponse dtReponse) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (dtReponse.getTlQuestionReponseSondeCollection() == null) {
            dtReponse.setTlQuestionReponseSondeCollection(new ArrayList<TlQuestionReponseSonde>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtQuestion idQuestion = dtReponse.getIdQuestion();
            if (idQuestion != null) {
                idQuestion = em.getReference(idQuestion.getClass(), idQuestion.getIdQuestion());
                dtReponse.setIdQuestion(idQuestion);
            }
            DtTypereponse idTypereponse = dtReponse.getIdTypereponse();
            if (idTypereponse != null) {
                idTypereponse = em.getReference(idTypereponse.getClass(), idTypereponse.getIdTypereponse());
                dtReponse.setIdTypereponse(idTypereponse);
            }
            Collection<TlQuestionReponseSonde> attachedTlQuestionReponseSondeCollection = new ArrayList<TlQuestionReponseSonde>();
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach : dtReponse.getTlQuestionReponseSondeCollection()) {
                tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach = em.getReference(tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach.getClass(), tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach.getIdTlQuestionReponseSonde());
                attachedTlQuestionReponseSondeCollection.add(tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach);
            }
            dtReponse.setTlQuestionReponseSondeCollection(attachedTlQuestionReponseSondeCollection);
            em.persist(dtReponse);
            if (idQuestion != null) {
                idQuestion.getDtReponseCollection().add(dtReponse);
                idQuestion = em.merge(idQuestion);
            }
            if (idTypereponse != null) {
                idTypereponse.getDtReponseCollection().add(dtReponse);
                idTypereponse = em.merge(idTypereponse);
            }
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionTlQuestionReponseSonde : dtReponse.getTlQuestionReponseSondeCollection()) {
                DtReponse oldIdReponseOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde = tlQuestionReponseSondeCollectionTlQuestionReponseSonde.getIdReponse();
                tlQuestionReponseSondeCollectionTlQuestionReponseSonde.setIdReponse(dtReponse);
                tlQuestionReponseSondeCollectionTlQuestionReponseSonde = em.merge(tlQuestionReponseSondeCollectionTlQuestionReponseSonde);
                if (oldIdReponseOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde != null) {
                    oldIdReponseOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSondeCollectionTlQuestionReponseSonde);
                    oldIdReponseOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde = em.merge(oldIdReponseOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDtReponse(dtReponse.getIdReponse()) != null) {
                throw new PreexistingEntityException("DtReponse " + dtReponse + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DtReponse dtReponse) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtReponse persistentDtReponse = em.find(DtReponse.class, dtReponse.getIdReponse());
            DtQuestion idQuestionOld = persistentDtReponse.getIdQuestion();
            DtQuestion idQuestionNew = dtReponse.getIdQuestion();
            DtTypereponse idTypereponseOld = persistentDtReponse.getIdTypereponse();
            DtTypereponse idTypereponseNew = dtReponse.getIdTypereponse();
            Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollectionOld = persistentDtReponse.getTlQuestionReponseSondeCollection();
            Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollectionNew = dtReponse.getTlQuestionReponseSondeCollection();
            List<String> illegalOrphanMessages = null;
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionOldTlQuestionReponseSonde : tlQuestionReponseSondeCollectionOld) {
                if (!tlQuestionReponseSondeCollectionNew.contains(tlQuestionReponseSondeCollectionOldTlQuestionReponseSonde)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TlQuestionReponseSonde " + tlQuestionReponseSondeCollectionOldTlQuestionReponseSonde + " since its idReponse field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idQuestionNew != null) {
                idQuestionNew = em.getReference(idQuestionNew.getClass(), idQuestionNew.getIdQuestion());
                dtReponse.setIdQuestion(idQuestionNew);
            }
            if (idTypereponseNew != null) {
                idTypereponseNew = em.getReference(idTypereponseNew.getClass(), idTypereponseNew.getIdTypereponse());
                dtReponse.setIdTypereponse(idTypereponseNew);
            }
            Collection<TlQuestionReponseSonde> attachedTlQuestionReponseSondeCollectionNew = new ArrayList<TlQuestionReponseSonde>();
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach : tlQuestionReponseSondeCollectionNew) {
                tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach = em.getReference(tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach.getClass(), tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach.getIdTlQuestionReponseSonde());
                attachedTlQuestionReponseSondeCollectionNew.add(tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach);
            }
            tlQuestionReponseSondeCollectionNew = attachedTlQuestionReponseSondeCollectionNew;
            dtReponse.setTlQuestionReponseSondeCollection(tlQuestionReponseSondeCollectionNew);
            dtReponse = em.merge(dtReponse);
            if (idQuestionOld != null && !idQuestionOld.equals(idQuestionNew)) {
                idQuestionOld.getDtReponseCollection().remove(dtReponse);
                idQuestionOld = em.merge(idQuestionOld);
            }
            if (idQuestionNew != null && !idQuestionNew.equals(idQuestionOld)) {
                idQuestionNew.getDtReponseCollection().add(dtReponse);
                idQuestionNew = em.merge(idQuestionNew);
            }
            if (idTypereponseOld != null && !idTypereponseOld.equals(idTypereponseNew)) {
                idTypereponseOld.getDtReponseCollection().remove(dtReponse);
                idTypereponseOld = em.merge(idTypereponseOld);
            }
            if (idTypereponseNew != null && !idTypereponseNew.equals(idTypereponseOld)) {
                idTypereponseNew.getDtReponseCollection().add(dtReponse);
                idTypereponseNew = em.merge(idTypereponseNew);
            }
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde : tlQuestionReponseSondeCollectionNew) {
                if (!tlQuestionReponseSondeCollectionOld.contains(tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde)) {
                    DtReponse oldIdReponseOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde = tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.getIdReponse();
                    tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.setIdReponse(dtReponse);
                    tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde = em.merge(tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde);
                    if (oldIdReponseOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde != null && !oldIdReponseOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.equals(dtReponse)) {
                        oldIdReponseOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde);
                        oldIdReponseOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde = em.merge(oldIdReponseOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde);
                    }
                }
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
                Integer id = dtReponse.getIdReponse();
                if (findDtReponse(id) == null) {
                    throw new NonexistentEntityException("The dtReponse with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtReponse dtReponse;
            try {
                dtReponse = em.getReference(DtReponse.class, id);
                dtReponse.getIdReponse();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dtReponse with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollectionOrphanCheck = dtReponse.getTlQuestionReponseSondeCollection();
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionOrphanCheckTlQuestionReponseSonde : tlQuestionReponseSondeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtReponse (" + dtReponse + ") cannot be destroyed since the TlQuestionReponseSonde " + tlQuestionReponseSondeCollectionOrphanCheckTlQuestionReponseSonde + " in its tlQuestionReponseSondeCollection field has a non-nullable idReponse field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            DtQuestion idQuestion = dtReponse.getIdQuestion();
            if (idQuestion != null) {
                idQuestion.getDtReponseCollection().remove(dtReponse);
                idQuestion = em.merge(idQuestion);
            }
            DtTypereponse idTypereponse = dtReponse.getIdTypereponse();
            if (idTypereponse != null) {
                idTypereponse.getDtReponseCollection().remove(dtReponse);
                idTypereponse = em.merge(idTypereponse);
            }
            em.remove(dtReponse);
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

    public List<DtReponse> findDtReponseEntities() {
        return findDtReponseEntities(true, -1, -1);
    }

    public List<DtReponse> findDtReponseEntities(int maxResults, int firstResult) {
        return findDtReponseEntities(false, maxResults, firstResult);
    }

    private List<DtReponse> findDtReponseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DtReponse.class));
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

    public DtReponse findDtReponse(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DtReponse.class, id);
        } finally {
            em.close();
        }
    }

    public int getDtReponseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DtReponse> rt = cq.from(DtReponse.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
