/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.banque.managers;

import java.util.HashMap;
import java.util.Map;
import javax.persistence.Persistence;

/**
 *
 * @author wasser
 */
public class EntityManager {

    private static javax.persistence.EntityManager em;
    private static javax.persistence.EntityManagerFactory emf;

    private EntityManager() {
    }

    public static javax.persistence.EntityManager getEntityManager() {
        if (emf == null) {
            Map<String, String> properties = new HashMap<String, String>();
            properties.put("javax.persistence.jdbc.user", "root");
            properties.put("javax.persistence.jdbc.password", "root");
            emf = Persistence.createEntityManagerFactory("jdbc:mysql://localhost:3306/banque_dev", properties);
            em = emf.createEntityManager();
        }
        return em;
    }
    
    @Override
    protected void finalize() throws Throwable{
        super.finalize();
        
        em.close();
        emf.close();       
    }
    
    
}
