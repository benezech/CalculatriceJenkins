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
import Entity.DtSonde;
import Entity.DtTypesonde;
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
public class DtTypesondeJpaController implements Serializable {

    public DtTypesondeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DtTypesonde dtTypesonde) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (dtTypesonde.getDtSondeCollection() == null) {
            dtTypesonde.setDtSondeCollection(new ArrayList<DtSonde>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<DtSonde> attachedDtSondeCollection = new ArrayList<DtSonde>();
            for (DtSonde dtSondeCollectionDtSondeToAttach : dtTypesonde.getDtSondeCollection()) {
                dtSondeCollectionDtSondeToAttach = em.getReference(dtSondeCollectionDtSondeToAttach.getClass(), dtSondeCollectionDtSondeToAttach.getIdSonde());
                attachedDtSondeCollection.add(dtSondeCollectionDtSondeToAttach);
            }
            dtTypesonde.setDtSondeCollection(attachedDtSondeCollection);
            em.persist(dtTypesonde);
            for (DtSonde dtSondeCollectionDtSonde : dtTypesonde.getDtSondeCollection()) {
                DtTypesonde oldIdTypesondeOfDtSondeCollectionDtSonde = dtSondeCollectionDtSonde.getIdTypesonde();
                dtSondeCollectionDtSonde.setIdTypesonde(dtTypesonde);
                dtSondeCollectionDtSonde = em.merge(dtSondeCollectionDtSonde);
                if (oldIdTypesondeOfDtSondeCollectionDtSonde != null) {
                    oldIdTypesondeOfDtSondeCollectionDtSonde.getDtSondeCollection().remove(dtSondeCollectionDtSonde);
                    oldIdTypesondeOfDtSondeCollectionDtSonde = em.merge(oldIdTypesondeOfDtSondeCollectionDtSonde);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDtTypesonde(dtTypesonde.getIdTypesonde()) != null) {
                throw new PreexistingEntityException("DtTypesonde " + dtTypesonde + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DtTypesonde dtTypesonde) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtTypesonde persistentDtTypesonde = em.find(DtTypesonde.class, dtTypesonde.getIdTypesonde());
            Collection<DtSonde> dtSondeCollectionOld = persistentDtTypesonde.getDtSondeCollection();
            Collection<DtSonde> dtSondeCollectionNew = dtTypesonde.getDtSondeCollection();
            List<String> illegalOrphanMessages = null;
            for (DtSonde dtSondeCollectionOldDtSonde : dtSondeCollectionOld) {
                if (!dtSondeCollectionNew.contains(dtSondeCollectionOldDtSonde)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DtSonde " + dtSondeCollectionOldDtSonde + " since its idTypesonde field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DtSonde> attachedDtSondeCollectionNew = new ArrayList<DtSonde>();
            for (DtSonde dtSondeCollectionNewDtSondeToAttach : dtSondeCollectionNew) {
                dtSondeCollectionNewDtSondeToAttach = em.getReference(dtSondeCollectionNewDtSondeToAttach.getClass(), dtSondeCollectionNewDtSondeToAttach.getIdSonde());
                attachedDtSondeCollectionNew.add(dtSondeCollectionNewDtSondeToAttach);
            }
            dtSondeCollectionNew = attachedDtSondeCollectionNew;
            dtTypesonde.setDtSondeCollection(dtSondeCollectionNew);
            dtTypesonde = em.merge(dtTypesonde);
            for (DtSonde dtSondeCollectionNewDtSonde : dtSondeCollectionNew) {
                if (!dtSondeCollectionOld.contains(dtSondeCollectionNewDtSonde)) {
                    DtTypesonde oldIdTypesondeOfDtSondeCollectionNewDtSonde = dtSondeCollectionNewDtSonde.getIdTypesonde();
                    dtSondeCollectionNewDtSonde.setIdTypesonde(dtTypesonde);
                    dtSondeCollectionNewDtSonde = em.merge(dtSondeCollectionNewDtSonde);
                    if (oldIdTypesondeOfDtSondeCollectionNewDtSonde != null && !oldIdTypesondeOfDtSondeCollectionNewDtSonde.equals(dtTypesonde)) {
                        oldIdTypesondeOfDtSondeCollectionNewDtSonde.getDtSondeCollection().remove(dtSondeCollectionNewDtSonde);
                        oldIdTypesondeOfDtSondeCollectionNewDtSonde = em.merge(oldIdTypesondeOfDtSondeCollectionNewDtSonde);
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
                Integer id = dtTypesonde.getIdTypesonde();
                if (findDtTypesonde(id) == null) {
                    throw new NonexistentEntityException("The dtTypesonde with id " + id + " no longer exists.");
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
            DtTypesonde dtTypesonde;
            try {
                dtTypesonde = em.getReference(DtTypesonde.class, id);
                dtTypesonde.getIdTypesonde();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dtTypesonde with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DtSonde> dtSondeCollectionOrphanCheck = dtTypesonde.getDtSondeCollection();
            for (DtSonde dtSondeCollectionOrphanCheckDtSonde : dtSondeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtTypesonde (" + dtTypesonde + ") cannot be destroyed since the DtSonde " + dtSondeCollectionOrphanCheckDtSonde + " in its dtSondeCollection field has a non-nullable idTypesonde field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dtTypesonde);
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

    public List<DtTypesonde> findDtTypesondeEntities() {
        return findDtTypesondeEntities(true, -1, -1);
    }

    public List<DtTypesonde> findDtTypesondeEntities(int maxResults, int firstResult) {
        return findDtTypesondeEntities(false, maxResults, firstResult);
    }

    private List<DtTypesonde> findDtTypesondeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DtTypesonde.class));
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

    public DtTypesonde findDtTypesonde(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DtTypesonde.class, id);
        } finally {
            em.close();
        }
    }

    public int getDtTypesondeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DtTypesonde> rt = cq.from(DtTypesonde.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
