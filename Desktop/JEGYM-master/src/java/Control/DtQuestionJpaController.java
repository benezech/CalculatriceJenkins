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
import Entity.DtFormulaire;
import Entity.DtQuestion;
import Entity.DtReponse;
import java.util.ArrayList;
import java.util.Collection;
import Entity.TlQuestionReponseSonde;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author gaele
 */
public class DtQuestionJpaController implements Serializable {

    public DtQuestionJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DtQuestion dtQuestion) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (dtQuestion.getDtReponseCollection() == null) {
            dtQuestion.setDtReponseCollection(new ArrayList<DtReponse>());
        }
        if (dtQuestion.getTlQuestionReponseSondeCollection() == null) {
            dtQuestion.setTlQuestionReponseSondeCollection(new ArrayList<TlQuestionReponseSonde>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtFormulaire idFormulaire = dtQuestion.getIdFormulaire();
            if (idFormulaire != null) {
                idFormulaire = em.getReference(idFormulaire.getClass(), idFormulaire.getIdFormulaire());
                dtQuestion.setIdFormulaire(idFormulaire);
            }
            Collection<DtReponse> attachedDtReponseCollection = new ArrayList<DtReponse>();
            for (DtReponse dtReponseCollectionDtReponseToAttach : dtQuestion.getDtReponseCollection()) {
                dtReponseCollectionDtReponseToAttach = em.getReference(dtReponseCollectionDtReponseToAttach.getClass(), dtReponseCollectionDtReponseToAttach.getIdReponse());
                attachedDtReponseCollection.add(dtReponseCollectionDtReponseToAttach);
            }
            dtQuestion.setDtReponseCollection(attachedDtReponseCollection);
            Collection<TlQuestionReponseSonde> attachedTlQuestionReponseSondeCollection = new ArrayList<TlQuestionReponseSonde>();
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach : dtQuestion.getTlQuestionReponseSondeCollection()) {
                tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach = em.getReference(tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach.getClass(), tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach.getIdTlQuestionReponseSonde());
                attachedTlQuestionReponseSondeCollection.add(tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach);
            }
            dtQuestion.setTlQuestionReponseSondeCollection(attachedTlQuestionReponseSondeCollection);
            em.persist(dtQuestion);
            if (idFormulaire != null) {
                idFormulaire.getDtQuestionCollection().add(dtQuestion);
                idFormulaire = em.merge(idFormulaire);
            }
            for (DtReponse dtReponseCollectionDtReponse : dtQuestion.getDtReponseCollection()) {
                DtQuestion oldIdQuestionOfDtReponseCollectionDtReponse = dtReponseCollectionDtReponse.getIdQuestion();
                dtReponseCollectionDtReponse.setIdQuestion(dtQuestion);
                dtReponseCollectionDtReponse = em.merge(dtReponseCollectionDtReponse);
                if (oldIdQuestionOfDtReponseCollectionDtReponse != null) {
                    oldIdQuestionOfDtReponseCollectionDtReponse.getDtReponseCollection().remove(dtReponseCollectionDtReponse);
                    oldIdQuestionOfDtReponseCollectionDtReponse = em.merge(oldIdQuestionOfDtReponseCollectionDtReponse);
                }
            }
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionTlQuestionReponseSonde : dtQuestion.getTlQuestionReponseSondeCollection()) {
                DtQuestion oldIdQuestionOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde = tlQuestionReponseSondeCollectionTlQuestionReponseSonde.getIdQuestion();
                tlQuestionReponseSondeCollectionTlQuestionReponseSonde.setIdQuestion(dtQuestion);
                tlQuestionReponseSondeCollectionTlQuestionReponseSonde = em.merge(tlQuestionReponseSondeCollectionTlQuestionReponseSonde);
                if (oldIdQuestionOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde != null) {
                    oldIdQuestionOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSondeCollectionTlQuestionReponseSonde);
                    oldIdQuestionOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde = em.merge(oldIdQuestionOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDtQuestion(dtQuestion.getIdQuestion()) != null) {
                throw new PreexistingEntityException("DtQuestion " + dtQuestion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DtQuestion dtQuestion) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtQuestion persistentDtQuestion = em.find(DtQuestion.class, dtQuestion.getIdQuestion());
            DtFormulaire idFormulaireOld = persistentDtQuestion.getIdFormulaire();
            DtFormulaire idFormulaireNew = dtQuestion.getIdFormulaire();
            Collection<DtReponse> dtReponseCollectionOld = persistentDtQuestion.getDtReponseCollection();
            Collection<DtReponse> dtReponseCollectionNew = dtQuestion.getDtReponseCollection();
            Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollectionOld = persistentDtQuestion.getTlQuestionReponseSondeCollection();
            Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollectionNew = dtQuestion.getTlQuestionReponseSondeCollection();
            List<String> illegalOrphanMessages = null;
            for (DtReponse dtReponseCollectionOldDtReponse : dtReponseCollectionOld) {
                if (!dtReponseCollectionNew.contains(dtReponseCollectionOldDtReponse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DtReponse " + dtReponseCollectionOldDtReponse + " since its idQuestion field is not nullable.");
                }
            }
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionOldTlQuestionReponseSonde : tlQuestionReponseSondeCollectionOld) {
                if (!tlQuestionReponseSondeCollectionNew.contains(tlQuestionReponseSondeCollectionOldTlQuestionReponseSonde)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TlQuestionReponseSonde " + tlQuestionReponseSondeCollectionOldTlQuestionReponseSonde + " since its idQuestion field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idFormulaireNew != null) {
                idFormulaireNew = em.getReference(idFormulaireNew.getClass(), idFormulaireNew.getIdFormulaire());
                dtQuestion.setIdFormulaire(idFormulaireNew);
            }
            Collection<DtReponse> attachedDtReponseCollectionNew = new ArrayList<DtReponse>();
            for (DtReponse dtReponseCollectionNewDtReponseToAttach : dtReponseCollectionNew) {
                dtReponseCollectionNewDtReponseToAttach = em.getReference(dtReponseCollectionNewDtReponseToAttach.getClass(), dtReponseCollectionNewDtReponseToAttach.getIdReponse());
                attachedDtReponseCollectionNew.add(dtReponseCollectionNewDtReponseToAttach);
            }
            dtReponseCollectionNew = attachedDtReponseCollectionNew;
            dtQuestion.setDtReponseCollection(dtReponseCollectionNew);
            Collection<TlQuestionReponseSonde> attachedTlQuestionReponseSondeCollectionNew = new ArrayList<TlQuestionReponseSonde>();
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach : tlQuestionReponseSondeCollectionNew) {
                tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach = em.getReference(tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach.getClass(), tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach.getIdTlQuestionReponseSonde());
                attachedTlQuestionReponseSondeCollectionNew.add(tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach);
            }
            tlQuestionReponseSondeCollectionNew = attachedTlQuestionReponseSondeCollectionNew;
            dtQuestion.setTlQuestionReponseSondeCollection(tlQuestionReponseSondeCollectionNew);
            dtQuestion = em.merge(dtQuestion);
            if (idFormulaireOld != null && !idFormulaireOld.equals(idFormulaireNew)) {
                idFormulaireOld.getDtQuestionCollection().remove(dtQuestion);
                idFormulaireOld = em.merge(idFormulaireOld);
            }
            if (idFormulaireNew != null && !idFormulaireNew.equals(idFormulaireOld)) {
                idFormulaireNew.getDtQuestionCollection().add(dtQuestion);
                idFormulaireNew = em.merge(idFormulaireNew);
            }
            for (DtReponse dtReponseCollectionNewDtReponse : dtReponseCollectionNew) {
                if (!dtReponseCollectionOld.contains(dtReponseCollectionNewDtReponse)) {
                    DtQuestion oldIdQuestionOfDtReponseCollectionNewDtReponse = dtReponseCollectionNewDtReponse.getIdQuestion();
                    dtReponseCollectionNewDtReponse.setIdQuestion(dtQuestion);
                    dtReponseCollectionNewDtReponse = em.merge(dtReponseCollectionNewDtReponse);
                    if (oldIdQuestionOfDtReponseCollectionNewDtReponse != null && !oldIdQuestionOfDtReponseCollectionNewDtReponse.equals(dtQuestion)) {
                        oldIdQuestionOfDtReponseCollectionNewDtReponse.getDtReponseCollection().remove(dtReponseCollectionNewDtReponse);
                        oldIdQuestionOfDtReponseCollectionNewDtReponse = em.merge(oldIdQuestionOfDtReponseCollectionNewDtReponse);
                    }
                }
            }
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde : tlQuestionReponseSondeCollectionNew) {
                if (!tlQuestionReponseSondeCollectionOld.contains(tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde)) {
                    DtQuestion oldIdQuestionOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde = tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.getIdQuestion();
                    tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.setIdQuestion(dtQuestion);
                    tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde = em.merge(tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde);
                    if (oldIdQuestionOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde != null && !oldIdQuestionOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.equals(dtQuestion)) {
                        oldIdQuestionOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde);
                        oldIdQuestionOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde = em.merge(oldIdQuestionOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde);
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
                Integer id = dtQuestion.getIdQuestion();
                if (findDtQuestion(id) == null) {
                    throw new NonexistentEntityException("The dtQuestion with id " + id + " no longer exists.");
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
            DtQuestion dtQuestion;
            try {
                dtQuestion = em.getReference(DtQuestion.class, id);
                dtQuestion.getIdQuestion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dtQuestion with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DtReponse> dtReponseCollectionOrphanCheck = dtQuestion.getDtReponseCollection();
            for (DtReponse dtReponseCollectionOrphanCheckDtReponse : dtReponseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtQuestion (" + dtQuestion + ") cannot be destroyed since the DtReponse " + dtReponseCollectionOrphanCheckDtReponse + " in its dtReponseCollection field has a non-nullable idQuestion field.");
            }
            Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollectionOrphanCheck = dtQuestion.getTlQuestionReponseSondeCollection();
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionOrphanCheckTlQuestionReponseSonde : tlQuestionReponseSondeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtQuestion (" + dtQuestion + ") cannot be destroyed since the TlQuestionReponseSonde " + tlQuestionReponseSondeCollectionOrphanCheckTlQuestionReponseSonde + " in its tlQuestionReponseSondeCollection field has a non-nullable idQuestion field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            DtFormulaire idFormulaire = dtQuestion.getIdFormulaire();
            if (idFormulaire != null) {
                idFormulaire.getDtQuestionCollection().remove(dtQuestion);
                idFormulaire = em.merge(idFormulaire);
            }
            em.remove(dtQuestion);
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

    public List<DtQuestion> findDtQuestionEntities() {
        return findDtQuestionEntities(true, -1, -1);
    }

    public List<DtQuestion> findDtQuestionEntities(int maxResults, int firstResult) {
        return findDtQuestionEntities(false, maxResults, firstResult);
    }

    private List<DtQuestion> findDtQuestionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DtQuestion.class));
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

    public DtQuestion findDtQuestion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DtQuestion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDtQuestionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DtQuestion> rt = cq.from(DtQuestion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
