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
import Entity.DtEntreprise;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import Entity.DtFormulaire;
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
public class DtEntrepriseJpaController implements Serializable {

    public DtEntrepriseJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DtEntreprise dtEntreprise) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (dtEntreprise.getDtFormulaireCollection() == null) {
            dtEntreprise.setDtFormulaireCollection(new ArrayList<DtFormulaire>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<DtFormulaire> attachedDtFormulaireCollection = new ArrayList<DtFormulaire>();
            for (DtFormulaire dtFormulaireCollectionDtFormulaireToAttach : dtEntreprise.getDtFormulaireCollection()) {
                dtFormulaireCollectionDtFormulaireToAttach = em.getReference(dtFormulaireCollectionDtFormulaireToAttach.getClass(), dtFormulaireCollectionDtFormulaireToAttach.getIdFormulaire());
                attachedDtFormulaireCollection.add(dtFormulaireCollectionDtFormulaireToAttach);
            }
            dtEntreprise.setDtFormulaireCollection(attachedDtFormulaireCollection);
            em.persist(dtEntreprise);
            for (DtFormulaire dtFormulaireCollectionDtFormulaire : dtEntreprise.getDtFormulaireCollection()) {
                DtEntreprise oldSiretEntrepriseOfDtFormulaireCollectionDtFormulaire = dtFormulaireCollectionDtFormulaire.getSiretEntreprise();
                dtFormulaireCollectionDtFormulaire.setSiretEntreprise(dtEntreprise);
                dtFormulaireCollectionDtFormulaire = em.merge(dtFormulaireCollectionDtFormulaire);
                if (oldSiretEntrepriseOfDtFormulaireCollectionDtFormulaire != null) {
                    oldSiretEntrepriseOfDtFormulaireCollectionDtFormulaire.getDtFormulaireCollection().remove(dtFormulaireCollectionDtFormulaire);
                    oldSiretEntrepriseOfDtFormulaireCollectionDtFormulaire = em.merge(oldSiretEntrepriseOfDtFormulaireCollectionDtFormulaire);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDtEntreprise(dtEntreprise.getSiretEntreprise()) != null) {
                throw new PreexistingEntityException("DtEntreprise " + dtEntreprise + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DtEntreprise dtEntreprise) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtEntreprise persistentDtEntreprise = em.find(DtEntreprise.class, dtEntreprise.getSiretEntreprise());
            Collection<DtFormulaire> dtFormulaireCollectionOld = persistentDtEntreprise.getDtFormulaireCollection();
            Collection<DtFormulaire> dtFormulaireCollectionNew = dtEntreprise.getDtFormulaireCollection();
            List<String> illegalOrphanMessages = null;
            for (DtFormulaire dtFormulaireCollectionOldDtFormulaire : dtFormulaireCollectionOld) {
                if (!dtFormulaireCollectionNew.contains(dtFormulaireCollectionOldDtFormulaire)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DtFormulaire " + dtFormulaireCollectionOldDtFormulaire + " since its siretEntreprise field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<DtFormulaire> attachedDtFormulaireCollectionNew = new ArrayList<DtFormulaire>();
            for (DtFormulaire dtFormulaireCollectionNewDtFormulaireToAttach : dtFormulaireCollectionNew) {
                dtFormulaireCollectionNewDtFormulaireToAttach = em.getReference(dtFormulaireCollectionNewDtFormulaireToAttach.getClass(), dtFormulaireCollectionNewDtFormulaireToAttach.getIdFormulaire());
                attachedDtFormulaireCollectionNew.add(dtFormulaireCollectionNewDtFormulaireToAttach);
            }
            dtFormulaireCollectionNew = attachedDtFormulaireCollectionNew;
            dtEntreprise.setDtFormulaireCollection(dtFormulaireCollectionNew);
            dtEntreprise = em.merge(dtEntreprise);
            for (DtFormulaire dtFormulaireCollectionNewDtFormulaire : dtFormulaireCollectionNew) {
                if (!dtFormulaireCollectionOld.contains(dtFormulaireCollectionNewDtFormulaire)) {
                    DtEntreprise oldSiretEntrepriseOfDtFormulaireCollectionNewDtFormulaire = dtFormulaireCollectionNewDtFormulaire.getSiretEntreprise();
                    dtFormulaireCollectionNewDtFormulaire.setSiretEntreprise(dtEntreprise);
                    dtFormulaireCollectionNewDtFormulaire = em.merge(dtFormulaireCollectionNewDtFormulaire);
                    if (oldSiretEntrepriseOfDtFormulaireCollectionNewDtFormulaire != null && !oldSiretEntrepriseOfDtFormulaireCollectionNewDtFormulaire.equals(dtEntreprise)) {
                        oldSiretEntrepriseOfDtFormulaireCollectionNewDtFormulaire.getDtFormulaireCollection().remove(dtFormulaireCollectionNewDtFormulaire);
                        oldSiretEntrepriseOfDtFormulaireCollectionNewDtFormulaire = em.merge(oldSiretEntrepriseOfDtFormulaireCollectionNewDtFormulaire);
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
                String id = dtEntreprise.getSiretEntreprise();
                if (findDtEntreprise(id) == null) {
                    throw new NonexistentEntityException("The dtEntreprise with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtEntreprise dtEntreprise;
            try {
                dtEntreprise = em.getReference(DtEntreprise.class, id);
                dtEntreprise.getSiretEntreprise();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dtEntreprise with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DtFormulaire> dtFormulaireCollectionOrphanCheck = dtEntreprise.getDtFormulaireCollection();
            for (DtFormulaire dtFormulaireCollectionOrphanCheckDtFormulaire : dtFormulaireCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtEntreprise (" + dtEntreprise + ") cannot be destroyed since the DtFormulaire " + dtFormulaireCollectionOrphanCheckDtFormulaire + " in its dtFormulaireCollection field has a non-nullable siretEntreprise field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(dtEntreprise);
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

    public List<DtEntreprise> findDtEntrepriseEntities() {
        return findDtEntrepriseEntities(true, -1, -1);
    }

    public List<DtEntreprise> findDtEntrepriseEntities(int maxResults, int firstResult) {
        return findDtEntrepriseEntities(false, maxResults, firstResult);
    }

    private List<DtEntreprise> findDtEntrepriseEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DtEntreprise.class));
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

    public DtEntreprise findDtEntreprise(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DtEntreprise.class, id);
        } finally {
            em.close();
        }
    }

    public int getDtEntrepriseCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DtEntreprise> rt = cq.from(DtEntreprise.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
