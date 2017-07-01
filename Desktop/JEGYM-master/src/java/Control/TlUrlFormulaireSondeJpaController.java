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
import Entity.DtFormulaire;
import Entity.DtSonde;
import Entity.TlUrlFormulaireSonde;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author gaele
 */
public class TlUrlFormulaireSondeJpaController implements Serializable {

    public TlUrlFormulaireSondeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TlUrlFormulaireSonde tlUrlFormulaireSonde) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            DtFormulaire idFormulaire = tlUrlFormulaireSonde.getIdFormulaire();
            if (idFormulaire != null) {
                idFormulaire = em.getReference(idFormulaire.getClass(), idFormulaire.getIdFormulaire());
                tlUrlFormulaireSonde.setIdFormulaire(idFormulaire);
            }
            DtSonde idSonde = tlUrlFormulaireSonde.getIdSonde();
            if (idSonde != null) {
                idSonde = em.getReference(idSonde.getClass(), idSonde.getIdSonde());
                tlUrlFormulaireSonde.setIdSonde(idSonde);
            }
            em.persist(tlUrlFormulaireSonde);
            if (idFormulaire != null) {
                idFormulaire.getTlUrlFormulaireSondeCollection().add(tlUrlFormulaireSonde);
                idFormulaire = em.merge(idFormulaire);
            }
            if (idSonde != null) {
                idSonde.getTlUrlFormulaireSondeCollection().add(tlUrlFormulaireSonde);
                idSonde = em.merge(idSonde);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTlUrlFormulaireSonde(tlUrlFormulaireSonde.getIdTlUrlFormulaireSonde()) != null) {
                throw new PreexistingEntityException("TlUrlFormulaireSonde " + tlUrlFormulaireSonde + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TlUrlFormulaireSonde tlUrlFormulaireSonde) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TlUrlFormulaireSonde persistentTlUrlFormulaireSonde = em.find(TlUrlFormulaireSonde.class, tlUrlFormulaireSonde.getIdTlUrlFormulaireSonde());
            DtFormulaire idFormulaireOld = persistentTlUrlFormulaireSonde.getIdFormulaire();
            DtFormulaire idFormulaireNew = tlUrlFormulaireSonde.getIdFormulaire();
            DtSonde idSondeOld = persistentTlUrlFormulaireSonde.getIdSonde();
            DtSonde idSondeNew = tlUrlFormulaireSonde.getIdSonde();
            if (idFormulaireNew != null) {
                idFormulaireNew = em.getReference(idFormulaireNew.getClass(), idFormulaireNew.getIdFormulaire());
                tlUrlFormulaireSonde.setIdFormulaire(idFormulaireNew);
            }
            if (idSondeNew != null) {
                idSondeNew = em.getReference(idSondeNew.getClass(), idSondeNew.getIdSonde());
                tlUrlFormulaireSonde.setIdSonde(idSondeNew);
            }
            tlUrlFormulaireSonde = em.merge(tlUrlFormulaireSonde);
            if (idFormulaireOld != null && !idFormulaireOld.equals(idFormulaireNew)) {
                idFormulaireOld.getTlUrlFormulaireSondeCollection().remove(tlUrlFormulaireSonde);
                idFormulaireOld = em.merge(idFormulaireOld);
            }
            if (idFormulaireNew != null && !idFormulaireNew.equals(idFormulaireOld)) {
                idFormulaireNew.getTlUrlFormulaireSondeCollection().add(tlUrlFormulaireSonde);
                idFormulaireNew = em.merge(idFormulaireNew);
            }
            if (idSondeOld != null && !idSondeOld.equals(idSondeNew)) {
                idSondeOld.getTlUrlFormulaireSondeCollection().remove(tlUrlFormulaireSonde);
                idSondeOld = em.merge(idSondeOld);
            }
            if (idSondeNew != null && !idSondeNew.equals(idSondeOld)) {
                idSondeNew.getTlUrlFormulaireSondeCollection().add(tlUrlFormulaireSonde);
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
                Integer id = tlUrlFormulaireSonde.getIdTlUrlFormulaireSonde();
                if (findTlUrlFormulaireSonde(id) == null) {
                    throw new NonexistentEntityException("The tlUrlFormulaireSonde with id " + id + " no longer exists.");
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
            TlUrlFormulaireSonde tlUrlFormulaireSonde;
            try {
                tlUrlFormulaireSonde = em.getReference(TlUrlFormulaireSonde.class, id);
                tlUrlFormulaireSonde.getIdTlUrlFormulaireSonde();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tlUrlFormulaireSonde with id " + id + " no longer exists.", enfe);
            }
            DtFormulaire idFormulaire = tlUrlFormulaireSonde.getIdFormulaire();
            if (idFormulaire != null) {
                idFormulaire.getTlUrlFormulaireSondeCollection().remove(tlUrlFormulaireSonde);
                idFormulaire = em.merge(idFormulaire);
            }
            DtSonde idSonde = tlUrlFormulaireSonde.getIdSonde();
            if (idSonde != null) {
                idSonde.getTlUrlFormulaireSondeCollection().remove(tlUrlFormulaireSonde);
                idSonde = em.merge(idSonde);
            }
            em.remove(tlUrlFormulaireSonde);
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

    public List<TlUrlFormulaireSonde> findTlUrlFormulaireSondeEntities() {
        return findTlUrlFormulaireSondeEntities(true, -1, -1);
    }

    public List<TlUrlFormulaireSonde> findTlUrlFormulaireSondeEntities(int maxResults, int firstResult) {
        return findTlUrlFormulaireSondeEntities(false, maxResults, firstResult);
    }

    private List<TlUrlFormulaireSonde> findTlUrlFormulaireSondeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TlUrlFormulaireSonde.class));
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

    public TlUrlFormulaireSonde findTlUrlFormulaireSonde(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TlUrlFormulaireSonde.class, id);
        } finally {
            em.close();
        }
    }

    public int getTlUrlFormulaireSondeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TlUrlFormulaireSonde> rt = cq.from(TlUrlFormulaireSonde.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
