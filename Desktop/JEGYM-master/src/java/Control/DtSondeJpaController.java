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
import Entity.DtSonde;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.DtTypesonde;
import Entity.TlUrlFormulaireSonde;
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
public class DtSondeJpaController implements Serializable {

    public DtSondeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DtSonde dtSonde) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (dtSonde.getTlUrlFormulaireSondeCollection() == null) {
            dtSonde.setTlUrlFormulaireSondeCollection(new ArrayList<TlUrlFormulaireSonde>());
        }
        if (dtSonde.getTlQuestionReponseSondeCollection() == null) {
            dtSonde.setTlQuestionReponseSondeCollection(new ArrayList<TlQuestionReponseSonde>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtTypesonde idTypesonde = dtSonde.getIdTypesonde();
            if (idTypesonde != null) {
                idTypesonde = em.getReference(idTypesonde.getClass(), idTypesonde.getIdTypesonde());
                dtSonde.setIdTypesonde(idTypesonde);
            }
            Collection<TlUrlFormulaireSonde> attachedTlUrlFormulaireSondeCollection = new ArrayList<TlUrlFormulaireSonde>();
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach : dtSonde.getTlUrlFormulaireSondeCollection()) {
                tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach = em.getReference(tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach.getClass(), tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach.getIdTlUrlFormulaireSonde());
                attachedTlUrlFormulaireSondeCollection.add(tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach);
            }
            dtSonde.setTlUrlFormulaireSondeCollection(attachedTlUrlFormulaireSondeCollection);
            Collection<TlQuestionReponseSonde> attachedTlQuestionReponseSondeCollection = new ArrayList<TlQuestionReponseSonde>();
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach : dtSonde.getTlQuestionReponseSondeCollection()) {
                tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach = em.getReference(tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach.getClass(), tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach.getIdTlQuestionReponseSonde());
                attachedTlQuestionReponseSondeCollection.add(tlQuestionReponseSondeCollectionTlQuestionReponseSondeToAttach);
            }
            dtSonde.setTlQuestionReponseSondeCollection(attachedTlQuestionReponseSondeCollection);
            em.persist(dtSonde);
            if (idTypesonde != null) {
                idTypesonde.getDtSondeCollection().add(dtSonde);
                idTypesonde = em.merge(idTypesonde);
            }
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde : dtSonde.getTlUrlFormulaireSondeCollection()) {
                DtSonde oldIdSondeOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde = tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde.getIdSonde();
                tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde.setIdSonde(dtSonde);
                tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde = em.merge(tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde);
                if (oldIdSondeOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde != null) {
                    oldIdSondeOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde.getTlUrlFormulaireSondeCollection().remove(tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde);
                    oldIdSondeOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde = em.merge(oldIdSondeOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde);
                }
            }
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionTlQuestionReponseSonde : dtSonde.getTlQuestionReponseSondeCollection()) {
                DtSonde oldIdSondeOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde = tlQuestionReponseSondeCollectionTlQuestionReponseSonde.getIdSonde();
                tlQuestionReponseSondeCollectionTlQuestionReponseSonde.setIdSonde(dtSonde);
                tlQuestionReponseSondeCollectionTlQuestionReponseSonde = em.merge(tlQuestionReponseSondeCollectionTlQuestionReponseSonde);
                if (oldIdSondeOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde != null) {
                    oldIdSondeOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSondeCollectionTlQuestionReponseSonde);
                    oldIdSondeOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde = em.merge(oldIdSondeOfTlQuestionReponseSondeCollectionTlQuestionReponseSonde);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDtSonde(dtSonde.getIdSonde()) != null) {
                throw new PreexistingEntityException("DtSonde " + dtSonde + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DtSonde dtSonde) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtSonde persistentDtSonde = em.find(DtSonde.class, dtSonde.getIdSonde());
            DtTypesonde idTypesondeOld = persistentDtSonde.getIdTypesonde();
            DtTypesonde idTypesondeNew = dtSonde.getIdTypesonde();
            Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollectionOld = persistentDtSonde.getTlUrlFormulaireSondeCollection();
            Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollectionNew = dtSonde.getTlUrlFormulaireSondeCollection();
            Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollectionOld = persistentDtSonde.getTlQuestionReponseSondeCollection();
            Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollectionNew = dtSonde.getTlQuestionReponseSondeCollection();
            List<String> illegalOrphanMessages = null;
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionOldTlUrlFormulaireSonde : tlUrlFormulaireSondeCollectionOld) {
                if (!tlUrlFormulaireSondeCollectionNew.contains(tlUrlFormulaireSondeCollectionOldTlUrlFormulaireSonde)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TlUrlFormulaireSonde " + tlUrlFormulaireSondeCollectionOldTlUrlFormulaireSonde + " since its idSonde field is not nullable.");
                }
            }
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionOldTlQuestionReponseSonde : tlQuestionReponseSondeCollectionOld) {
                if (!tlQuestionReponseSondeCollectionNew.contains(tlQuestionReponseSondeCollectionOldTlQuestionReponseSonde)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TlQuestionReponseSonde " + tlQuestionReponseSondeCollectionOldTlQuestionReponseSonde + " since its idSonde field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (idTypesondeNew != null) {
                idTypesondeNew = em.getReference(idTypesondeNew.getClass(), idTypesondeNew.getIdTypesonde());
                dtSonde.setIdTypesonde(idTypesondeNew);
            }
            Collection<TlUrlFormulaireSonde> attachedTlUrlFormulaireSondeCollectionNew = new ArrayList<TlUrlFormulaireSonde>();
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach : tlUrlFormulaireSondeCollectionNew) {
                tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach = em.getReference(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach.getClass(), tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach.getIdTlUrlFormulaireSonde());
                attachedTlUrlFormulaireSondeCollectionNew.add(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach);
            }
            tlUrlFormulaireSondeCollectionNew = attachedTlUrlFormulaireSondeCollectionNew;
            dtSonde.setTlUrlFormulaireSondeCollection(tlUrlFormulaireSondeCollectionNew);
            Collection<TlQuestionReponseSonde> attachedTlQuestionReponseSondeCollectionNew = new ArrayList<TlQuestionReponseSonde>();
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach : tlQuestionReponseSondeCollectionNew) {
                tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach = em.getReference(tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach.getClass(), tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach.getIdTlQuestionReponseSonde());
                attachedTlQuestionReponseSondeCollectionNew.add(tlQuestionReponseSondeCollectionNewTlQuestionReponseSondeToAttach);
            }
            tlQuestionReponseSondeCollectionNew = attachedTlQuestionReponseSondeCollectionNew;
            dtSonde.setTlQuestionReponseSondeCollection(tlQuestionReponseSondeCollectionNew);
            dtSonde = em.merge(dtSonde);
            if (idTypesondeOld != null && !idTypesondeOld.equals(idTypesondeNew)) {
                idTypesondeOld.getDtSondeCollection().remove(dtSonde);
                idTypesondeOld = em.merge(idTypesondeOld);
            }
            if (idTypesondeNew != null && !idTypesondeNew.equals(idTypesondeOld)) {
                idTypesondeNew.getDtSondeCollection().add(dtSonde);
                idTypesondeNew = em.merge(idTypesondeNew);
            }
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde : tlUrlFormulaireSondeCollectionNew) {
                if (!tlUrlFormulaireSondeCollectionOld.contains(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde)) {
                    DtSonde oldIdSondeOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde = tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde.getIdSonde();
                    tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde.setIdSonde(dtSonde);
                    tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde = em.merge(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde);
                    if (oldIdSondeOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde != null && !oldIdSondeOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde.equals(dtSonde)) {
                        oldIdSondeOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde.getTlUrlFormulaireSondeCollection().remove(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde);
                        oldIdSondeOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde = em.merge(oldIdSondeOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde);
                    }
                }
            }
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde : tlQuestionReponseSondeCollectionNew) {
                if (!tlQuestionReponseSondeCollectionOld.contains(tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde)) {
                    DtSonde oldIdSondeOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde = tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.getIdSonde();
                    tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.setIdSonde(dtSonde);
                    tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde = em.merge(tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde);
                    if (oldIdSondeOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde != null && !oldIdSondeOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.equals(dtSonde)) {
                        oldIdSondeOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde.getTlQuestionReponseSondeCollection().remove(tlQuestionReponseSondeCollectionNewTlQuestionReponseSonde);
                        oldIdSondeOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde = em.merge(oldIdSondeOfTlQuestionReponseSondeCollectionNewTlQuestionReponseSonde);
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
                Integer id = dtSonde.getIdSonde();
                if (findDtSonde(id) == null) {
                    throw new NonexistentEntityException("The dtSonde with id " + id + " no longer exists.");
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
            DtSonde dtSonde;
            try {
                dtSonde = em.getReference(DtSonde.class, id);
                dtSonde.getIdSonde();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dtSonde with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollectionOrphanCheck = dtSonde.getTlUrlFormulaireSondeCollection();
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionOrphanCheckTlUrlFormulaireSonde : tlUrlFormulaireSondeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtSonde (" + dtSonde + ") cannot be destroyed since the TlUrlFormulaireSonde " + tlUrlFormulaireSondeCollectionOrphanCheckTlUrlFormulaireSonde + " in its tlUrlFormulaireSondeCollection field has a non-nullable idSonde field.");
            }
            Collection<TlQuestionReponseSonde> tlQuestionReponseSondeCollectionOrphanCheck = dtSonde.getTlQuestionReponseSondeCollection();
            for (TlQuestionReponseSonde tlQuestionReponseSondeCollectionOrphanCheckTlQuestionReponseSonde : tlQuestionReponseSondeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtSonde (" + dtSonde + ") cannot be destroyed since the TlQuestionReponseSonde " + tlQuestionReponseSondeCollectionOrphanCheckTlQuestionReponseSonde + " in its tlQuestionReponseSondeCollection field has a non-nullable idSonde field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            DtTypesonde idTypesonde = dtSonde.getIdTypesonde();
            if (idTypesonde != null) {
                idTypesonde.getDtSondeCollection().remove(dtSonde);
                idTypesonde = em.merge(idTypesonde);
            }
            em.remove(dtSonde);
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

    public List<DtSonde> findDtSondeEntities() {
        return findDtSondeEntities(true, -1, -1);
    }

    public List<DtSonde> findDtSondeEntities(int maxResults, int firstResult) {
        return findDtSondeEntities(false, maxResults, firstResult);
    }

    private List<DtSonde> findDtSondeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DtSonde.class));
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

    public DtSonde findDtSonde(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DtSonde.class, id);
        } finally {
            em.close();
        }
    }

    public int getDtSondeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DtSonde> rt = cq.from(DtSonde.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
