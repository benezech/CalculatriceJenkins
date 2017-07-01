/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.DtSonde;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gaele
 */
@Stateless
public class DtSondeFacade extends AbstractFacade<DtSonde> {

    @PersistenceContext(unitName = "JEGYME5PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DtSondeFacade() {
        super(DtSonde.class);
    }
    
}
