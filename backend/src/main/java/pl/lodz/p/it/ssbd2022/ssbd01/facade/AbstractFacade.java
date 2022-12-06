package pl.lodz.p.it.ssbd2022.ssbd01.facade;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Główna fasada do wykonywania operacji CRUD na bazie danych
 *
 * @param <T> Fasada do rozszerzenie
 */

public abstract class AbstractFacade<T> {

    private final Class<T> entityClass;

    protected AbstractFacade(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    protected abstract EntityManager getEntityManager();

    /**
     * Metoda odpowiedzialna za operacji CREATE na bazie danych
     *
     * @param entity - encja do stworzenia
     */

    protected void create(T entity) {
        getEntityManager().persist(entity);
        getEntityManager().flush();
    }

    /**
     * Metoda odpowiedzialna za operacji UPDATE na bazie danych
     *
     * @param entity - encja do zmiany
     */

    protected void edit(T entity) {
        getEntityManager().merge(entity);
        getEntityManager().flush();
    }

    /**
     * Metoda odpowiedzialna za operacji DELETE na bazie danych
     *
     * @param entity - encja do usunięcia
     */

    protected void remove(T entity) {
        getEntityManager().remove(getEntityManager().merge(entity));
        getEntityManager().flush();
    }

    /**
     * Metoda odpowiedzialna za operacji READ na bazie danych
     *
     * @param id - id encji
     * @return znaleziona encja
     */

    protected T find(Object id) {
        return getEntityManager().find(entityClass, id);
    }

    /**
     * Metoda odpowiedzialna za znajodwanie wszyskich encji w bazie danych
     *
     * @return List of found entities
     */

    protected List<T> findAll() {
        javax.persistence.criteria.CriteriaQuery cq = getEntityManager().getCriteriaBuilder().createQuery();
        cq.select(cq.from(entityClass));
        return getEntityManager().createQuery(cq).getResultList();
    }
}
