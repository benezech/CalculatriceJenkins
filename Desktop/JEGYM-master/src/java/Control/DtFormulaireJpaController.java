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
import Entity.DtEntreprise;
import Entity.DtFormulaire;
import Entity.DtQuestion;
import java.util.ArrayList;
import java.util.Collection;
import Entity.TlUrlFormulaireSonde;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author gaele
 */
public class DtFormulaireJpaController implements Serializable {

    public DtFormulaireJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DtFormulaire dtFormulaire) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (dtFormulaire.getDtQuestionCollection() == null) {
            dtFormulaire.setDtQuestionCollection(new ArrayList<DtQuestion>());
        }
        if (dtFormulaire.getTlUrlFormulaireSondeCollection() == null) {
            dtFormulaire.setTlUrlFormulaireSondeCollection(new ArrayList<TlUrlFormulaireSonde>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtEntreprise siretEntreprise = dtFormulaire.getSiretEntreprise();
            if (siretEntreprise != null) {
                siretEntreprise = em.getReference(siretEntreprise.getClass(), siretEntreprise.getSiretEntreprise());
                dtFormulaire.setSiretEntreprise(siretEntreprise);
            }
            Collection<DtQuestion> attachedDtQuestionCollection = new ArrayList<DtQuestion>();
            for (DtQuestion dtQuestionCollectionDtQuestionToAttach : dtFormulaire.getDtQuestionCollection()) {
                dtQuestionCollectionDtQuestionToAttach = em.getReference(dtQuestionCollectionDtQuestionToAttach.getClass(), dtQuestionCollectionDtQuestionToAttach.getIdQuestion());
                attachedDtQuestionCollection.add(dtQuestionCollectionDtQuestionToAttach);
            }
            dtFormulaire.setDtQuestionCollection(attachedDtQuestionCollection);
            Collection<TlUrlFormulaireSonde> attachedTlUrlFormulaireSondeCollection = new ArrayList<TlUrlFormulaireSonde>();
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach : dtFormulaire.getTlUrlFormulaireSondeCollection()) {
                tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach = em.getReference(tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach.getClass(), tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach.getIdTlUrlFormulaireSonde());
                attachedTlUrlFormulaireSondeCollection.add(tlUrlFormulaireSondeCollectionTlUrlFormulaireSondeToAttach);
            }
            dtFormulaire.setTlUrlFormulaireSondeCollection(attachedTlUrlFormulaireSondeCollection);
            em.persist(dtFormulaire);
            if (siretEntreprise != null) {
                siretEntreprise.getDtFormulaireCollection().add(dtFormulaire);
                siretEntreprise = em.merge(siretEntreprise);
            }
            for (DtQuestion dtQuestionCollectionDtQuestion : dtFormulaire.getDtQuestionCollection()) {
                DtFormulaire oldIdFormulaireOfDtQuestionCollectionDtQuestion = dtQuestionCollectionDtQuestion.getIdFormulaire();
                dtQuestionCollectionDtQuestion.setIdFormulaire(dtFormulaire);
                dtQuestionCollectionDtQuestion = em.merge(dtQuestionCollectionDtQuestion);
                if (oldIdFormulaireOfDtQuestionCollectionDtQuestion != null) {
                    oldIdFormulaireOfDtQuestionCollectionDtQuestion.getDtQuestionCollection().remove(dtQuestionCollectionDtQuestion);
                    oldIdFormulaireOfDtQuestionCollectionDtQuestion = em.merge(oldIdFormulaireOfDtQuestionCollectionDtQuestion);
                }
            }
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde : dtFormulaire.getTlUrlFormulaireSondeCollection()) {
                DtFormulaire oldIdFormulaireOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde = tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde.getIdFormulaire();
                tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde.setIdFormulaire(dtFormulaire);
                tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde = em.merge(tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde);
                if (oldIdFormulaireOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde != null) {
                    oldIdFormulaireOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde.getTlUrlFormulaireSondeCollection().remove(tlUrlFormulaireSondeCollectionTlUrlFormulaireSonde);
                    oldIdFormulaireOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde = em.merge(oldIdFormulaireOfTlUrlFormulaireSondeCollectionTlUrlFormulaireSonde);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findDtFormulaire(dtFormulaire.getIdFormulaire()) != null) {
                throw new PreexistingEntityException("DtFormulaire " + dtFormulaire + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DtFormulaire dtFormulaire) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtFormulaire persistentDtFormulaire = em.find(DtFormulaire.class, dtFormulaire.getIdFormulaire());
            DtEntreprise siretEntrepriseOld = persistentDtFormulaire.getSiretEntreprise();
            DtEntreprise siretEntrepriseNew = dtFormulaire.getSiretEntreprise();
            Collection<DtQuestion> dtQuestionCollectionOld = persistentDtFormulaire.getDtQuestionCollection();
            Collection<DtQuestion> dtQuestionCollectionNew = dtFormulaire.getDtQuestionCollection();
            Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollectionOld = persistentDtFormulaire.getTlUrlFormulaireSondeCollection();
            Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollectionNew = dtFormulaire.getTlUrlFormulaireSondeCollection();
            List<String> illegalOrphanMessages = null;
            for (DtQuestion dtQuestionCollectionOldDtQuestion : dtQuestionCollectionOld) {
                if (!dtQuestionCollectionNew.contains(dtQuestionCollectionOldDtQuestion)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain DtQuestion " + dtQuestionCollectionOldDtQuestion + " since its idFormulaire field is not nullable.");
                }
            }
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionOldTlUrlFormulaireSonde : tlUrlFormulaireSondeCollectionOld) {
                if (!tlUrlFormulaireSondeCollectionNew.contains(tlUrlFormulaireSondeCollectionOldTlUrlFormulaireSonde)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain TlUrlFormulaireSonde " + tlUrlFormulaireSondeCollectionOldTlUrlFormulaireSonde + " since its idFormulaire field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (siretEntrepriseNew != null) {
                siretEntrepriseNew = em.getReference(siretEntrepriseNew.getClass(), siretEntrepriseNew.getSiretEntreprise());
                dtFormulaire.setSiretEntreprise(siretEntrepriseNew);
            }
            Collection<DtQuestion> attachedDtQuestionCollectionNew = new ArrayList<DtQuestion>();
            for (DtQuestion dtQuestionCollectionNewDtQuestionToAttach : dtQuestionCollectionNew) {
                dtQuestionCollectionNewDtQuestionToAttach = em.getReference(dtQuestionCollectionNewDtQuestionToAttach.getClass(), dtQuestionCollectionNewDtQuestionToAttach.getIdQuestion());
                attachedDtQuestionCollectionNew.add(dtQuestionCollectionNewDtQuestionToAttach);
            }
            dtQuestionCollectionNew = attachedDtQuestionCollectionNew;
            dtFormulaire.setDtQuestionCollection(dtQuestionCollectionNew);
            Collection<TlUrlFormulaireSonde> attachedTlUrlFormulaireSondeCollectionNew = new ArrayList<TlUrlFormulaireSonde>();
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach : tlUrlFormulaireSondeCollectionNew) {
                tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach = em.getReference(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach.getClass(), tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach.getIdTlUrlFormulaireSonde());
                attachedTlUrlFormulaireSondeCollectionNew.add(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSondeToAttach);
            }
            tlUrlFormulaireSondeCollectionNew = attachedTlUrlFormulaireSondeCollectionNew;
            dtFormulaire.setTlUrlFormulaireSondeCollection(tlUrlFormulaireSondeCollectionNew);
            dtFormulaire = em.merge(dtFormulaire);
            if (siretEntrepriseOld != null && !siretEntrepriseOld.equals(siretEntrepriseNew)) {
                siretEntrepriseOld.getDtFormulaireCollection().remove(dtFormulaire);
                siretEntrepriseOld = em.merge(siretEntrepriseOld);
            }
            if (siretEntrepriseNew != null && !siretEntrepriseNew.equals(siretEntrepriseOld)) {
                siretEntrepriseNew.getDtFormulaireCollection().add(dtFormulaire);
                siretEntrepriseNew = em.merge(siretEntrepriseNew);
            }
            for (DtQuestion dtQuestionCollectionNewDtQuestion : dtQuestionCollectionNew) {
                if (!dtQuestionCollectionOld.contains(dtQuestionCollectionNewDtQuestion)) {
                    DtFormulaire oldIdFormulaireOfDtQuestionCollectionNewDtQuestion = dtQuestionCollectionNewDtQuestion.getIdFormulaire();
                    dtQuestionCollectionNewDtQuestion.setIdFormulaire(dtFormulaire);
                    dtQuestionCollectionNewDtQuestion = em.merge(dtQuestionCollectionNewDtQuestion);
                    if (oldIdFormulaireOfDtQuestionCollectionNewDtQuestion != null && !oldIdFormulaireOfDtQuestionCollectionNewDtQuestion.equals(dtFormulaire)) {
                        oldIdFormulaireOfDtQuestionCollectionNewDtQuestion.getDtQuestionCollection().remove(dtQuestionCollectionNewDtQuestion);
                        oldIdFormulaireOfDtQuestionCollectionNewDtQuestion = em.merge(oldIdFormulaireOfDtQuestionCollectionNewDtQuestion);
                    }
                }
            }
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde : tlUrlFormulaireSondeCollectionNew) {
                if (!tlUrlFormulaireSondeCollectionOld.contains(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde)) {
                    DtFormulaire oldIdFormulaireOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde = tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde.getIdFormulaire();
                    tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde.setIdFormulaire(dtFormulaire);
                    tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde = em.merge(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde);
                    if (oldIdFormulaireOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde != null && !oldIdFormulaireOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde.equals(dtFormulaire)) {
                        oldIdFormulaireOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde.getTlUrlFormulaireSondeCollection().remove(tlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde);
                        oldIdFormulaireOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde = em.merge(oldIdFormulaireOfTlUrlFormulaireSondeCollectionNewTlUrlFormulaireSonde);
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
                Integer id = dtFormulaire.getIdFormulaire();
                if (findDtFormulaire(id) == null) {
                    throw new NonexistentEntityException("The dtFormulaire with id " + id + " no longer exists.");
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
            DtFormulaire dtFormulaire;
            try {
                dtFormulaire = em.getReference(DtFormulaire.class, id);
                dtFormulaire.getIdFormulaire();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The dtFormulaire with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<DtQuestion> dtQuestionCollectionOrphanCheck = dtFormulaire.getDtQuestionCollection();
            for (DtQuestion dtQuestionCollectionOrphanCheckDtQuestion : dtQuestionCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtFormulaire (" + dtFormulaire + ") cannot be destroyed since the DtQuestion " + dtQuestionCollectionOrphanCheckDtQuestion + " in its dtQuestionCollection field has a non-nullable idFormulaire field.");
            }
            Collection<TlUrlFormulaireSonde> tlUrlFormulaireSondeCollectionOrphanCheck = dtFormulaire.getTlUrlFormulaireSondeCollection();
            for (TlUrlFormulaireSonde tlUrlFormulaireSondeCollectionOrphanCheckTlUrlFormulaireSonde : tlUrlFormulaireSondeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This DtFormulaire (" + dtFormulaire + ") cannot be destroyed since the TlUrlFormulaireSonde " + tlUrlFormulaireSondeCollectionOrphanCheckTlUrlFormulaireSonde + " in its tlUrlFormulaireSondeCollection field has a non-nullable idFormulaire field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            DtEntreprise siretEntreprise = dtFormulaire.getSiretEntreprise();
            if (siretEntreprise != null) {
                siretEntreprise.getDtFormulaireCollection().remove(dtFormulaire);
                siretEntreprise = em.merge(siretEntreprise);
            }
            em.remove(dtFormulaire);
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

    public List<DtFormulaire> findDtFormulaireEntities() {
        return findDtFormulaireEntities(true, -1, -1);
    }

    public List<DtFormulaire> findDtFormulaireEntities(int maxResults, int firstResult) {
        return findDtFormulaireEntities(false, maxResults, firstResult);
    }

    private List<DtFormulaire> findDtFormulaireEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DtFormulaire.class));
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

    public DtFormulaire findDtFormulaire(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DtFormulaire.class, id);
        } finally {
            em.close();
        }
    }

    public int getDtFormulaireCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DtFormulaire> rt = cq.from(DtFormulaire.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
