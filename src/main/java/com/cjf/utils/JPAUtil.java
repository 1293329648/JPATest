package com.cjf.utils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public  final  class JPAUtil {
   private static EntityManagerFactory factory;
    static {

         factory = Persistence.createEntityManagerFactory("myJpa");
    }

    public static  EntityManager getEntityManager(){
        return  factory.createEntityManager();
    }


}
