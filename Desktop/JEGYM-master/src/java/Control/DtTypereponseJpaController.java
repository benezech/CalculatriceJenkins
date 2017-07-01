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
import Entity.DtReponse;
import Entity.DtTypereponse;
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
public class DtTypereponseJpaController implements Serializable {

    public DtTypereponseJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DtTypereponse dtTypereponse) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (dtTypereponse.getDtReponseCollection() == null) {
            dtTypereponse.setDtReponseCollection(new ArrayList<DtReponse>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<DtReponse> attachedDtReponseCollection = new ArrayList<DtReponse>();
            for (DtReponse dtReponseCollectionDtReponseToAttach : dtTypereponse.getDtReponseCollection()) {
                dtReponseCollectionDtReponseToAttach = em.getReference(dtReponseCollectionDtReponseToAttach.getClass(), dtReponseCollectionDtReponseToAttach.getIdReponse());
                attachedDtReponseCollection.add(dtReponseCollectionDtReponseToAttach);
            }
            dtTypereponse.setDtReponseCollection(attachedDtReponseCollection);
            em.persist(dtTypereponse);
            for (DtReponse dtReponseCollectionDtReponse : dtTypereponse.getDtReponseCollection()) {
                DtTypereponse oldIdTypereponseOfDtReponseCollectionDtReponse = dtReponseCollectionDtReponse.getIdTypereponse();
                dtReponseCollectionDtReponse.setIdTypereponse(dtTypereponse);
                dtReponseCollectionDtReponse = em.merge(dtReponseCollectionDtReponse);
                if (oldIdTypereponseOfDtReponseCollectionDtReponse != null) {
                    oldIdTypereponseOfDtReponseCollectionDtReponse.getDtReponseCollection().remove(dtReponseCollectionDtReponse);
                    oldIdTypereponseOfDtReponseCollectionDtReponse = em.merge(oldIdTypereponseOfDtReponseCollectionDtReponse);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDtTypereponse(dtTypereponse.getIdTypereponse()) != null) {
                throw new PreexistingEntityException("DtTypereponse " + dtTypereponse + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DtTypereponse dtTypereponse) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtTypereponse persistentDtTypereponse = em.find(DtTypereponse.class, dtTypereponse.getIdTypereponse());
            Collection<DtReponse> dtReponseCollectionOld = persistentDtTypereponse.getDtReponseCollection();
            Collection<DtReponse> dtReponseCollectionNew = dtTypereponse.getDtReponseCollection();
            List<String> illegalOrphanMessages = null;
            for (DtReponse dtReponseCollectionOldDtReponse : dtReponseCollectionOld) {
                if (!dtReponseCollectionNew.contains(dtReponseCollectionOldDtReponse)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DtReponse " + dtReponseCollectionOldDtReponse + " since its idTypereponse field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DtReponse> attachedDtReponseCollectionNew = new ArrayList<DtReponse>();
            for (DtReponse dtReponseCollectionNewDtReponseToAttach : dtReponseCollectionNew) {
                dtReponseCollectionNewDtReponseToAttach = em.getReference(dtReponseCollectionNewDtReponseToAttach.getClass(), dtReponseCollectionNewDtReponseToAttach.getIdReponse());
                attachedDtReponseCollectionNew.add(dtReponseCollectionNewDtReponseToAttach);
            }
            dtReponseCollectionNew = attachedDtReponseCollectionNew;
            dtTypereponse.setDtReponseCollection(dtReponseCollectionNew);
            dtTypereponse = em.merge(dtTypereponse);
            for (DtReponse dtReponseCollectionNewDtReponse : dtReponseCollectionNew) {
                if (!dtReponseCollectionOld.contains(dtReponseCollectionNewDtReponse)) {
                    DtTypereponse oldIdTypereponseOfDtReponseCollectionNewDtReponse = dtReponseCollectionNewDtReponse.getIdTypereponse();
                    dtReponseCollectionNewDtReponse.setIdTypereponse(dtTypereponse);
                    dtReponseCollectionNewDtReponse = em.merge(dtReponseCollectionNewDtReponse);
                    if (oldIdTypereponseOfDtReponseCollectionNewDtReponse != null && !oldIdTypereponseOfDtReponseCollectionNewDtReponse.equals(dtTypereponse)) {
                        oldIdTypereponseOfDtReponseCollectionNewDtReponse.getDtReponseCollection().remove(dtReponseCollectionNewDtReponse);
                        oldIdTypereponseOfDtReponseCollectionNewDtReponse = em.merge(oldIdTypereponseOfDtReponseCollectionNewDtReponse);
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
                Integer id = dtTypereponse.getIdTypereponse();
                if (findDtTypereponse(id) == null) {
                    throw new NonexistentEntityException("The dtTypereponse with id " + id + " no longer exists.");
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
            DtTypereponse dtTypereponse;
            try {
                dtTypereponse = em.getReference(DtTypereponse.class, id);
                dtTypereponse.getIdTypereponse();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dtTypereponse with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DtReponse> dtReponseCollectionOrphanCheck = dtTypereponse.getDtReponseCollection();
            for (DtReponse dtReponseCollectionOrphanCheckDtReponse : dtReponseCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtTypereponse (" + dtTypereponse + ") cannot be destroyed since the DtReponse " + dtReponseCollectionOrphanCheckDtReponse + " in its dtReponseCollection field has a non-nullable idTypereponse field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dtTypereponse);
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

    public List<DtTypereponse> findDtTypereponseEntities() {
        return findDtTypereponseEntities(true, -1, -1);
    }

    public List<DtTypereponse> findDtTypereponseEntities(int maxResults, int firstResult) {
        return findDtTypereponseEntities(false, maxResults, firstResult);
    }

    private List<DtTypereponse> findDtTypereponseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DtTypereponse.class));
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

    public DtTypereponse findDtTypereponse(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DtTypereponse.class, id);
        } finally {
            em.close();
        }
    }

    public int getDtTypereponseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DtTypereponse> rt = cq.from(DtTypereponse.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
