package com.library.app.commontests.utils;

import javax.persistence.EntityManager;

import org.junit.Ignore;

@Ignore
public class DBCommandTransactionalExecutor {

    private final EntityManager em;

    public DBCommandTransactionalExecutor(EntityManager em) {
        this.em = em;
    }

    public <T> T executeCommand(DBCommand<T> dbCommand) {

        try {
            em.getTransaction().begin();
            final T toReturn = dbCommand.execute();
            return toReturn;
        } catch (final Exception e) {
            e.printStackTrace();
            em.getTransaction().rollback();
            throw new IllegalStateException(e);
        } finally {
            em.getTransaction().commit();
            em.clear();
        }

    }
}
