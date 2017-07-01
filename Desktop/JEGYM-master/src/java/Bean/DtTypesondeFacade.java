/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Bean;

import Entity.DtTypesonde;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gaele
 */
@Stateless
public class DtTypesondeFacade extends AbstractFacade<DtTypesonde> {

    @PersistenceContext(unitName = "JEGYME5PU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public DtTypesondeFacade() {
        super(DtTypesonde.class);
    }
    
}
