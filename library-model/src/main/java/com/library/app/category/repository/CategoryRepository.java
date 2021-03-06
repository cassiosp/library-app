package com.library.app.category.repository;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.library.app.category.model.Category;

@Stateless
public class CategoryRepository {

    @PersistenceContext
    EntityManager em;

    public Category add(Category category) {
        em.persist(category);
        return category;
    }

    public Category findById(Long id) {
        if (id == null) {
            return null;
        }
        return em.find(Category.class, id);
    }

    public void update(Category category) {
        em.merge(category);
    }

    public List<Category> findAll(String orderField) {
        return em.createQuery("Select e From Category e Order by e." + orderField, Category.class).getResultList();
    }

    public Boolean alreadyExists(Category category) {
        final StringBuffer jpql = new StringBuffer();
        jpql.append("Select 1 From Category e where e.name = :name");
        if (category.getId() != null) {
            jpql.append(" And e.id != :id");
        }

        final Query query = em.createQuery(jpql.toString()).setParameter("name", category.getName());
        if (category.getId() != null) {
            query.setParameter("id", category.getId());
        }

        return query.setMaxResults(1).getResultList().size() > 0;
    }

    public Boolean existsById(Long id) {
        return em.createQuery("Select 1 From Category e where e.id = :id")
                .setParameter("id", id)
                .setMaxResults(1)
                .getResultList().size() > 0;
    }
}
