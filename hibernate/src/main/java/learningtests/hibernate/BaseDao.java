package learningtests.hibernate;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

public abstract class BaseDao<T> {
	private final Class<T> tClass;
	protected EntityManager em;
	protected EntityManagerFactory emf;
	
	protected BaseDao(Class<T> tClass) {
		this.tClass = tClass;
	}
	
	public T findById(String id) {
		return em.find(tClass, id);
	}
	
	public void add(T t) {
		em.getTransaction().begin();
		em.persist(t);
		em.getTransaction().commit();
	}
	
	public void remove(T t) {
		em.getTransaction().begin();
		em.remove(em.merge(t));
		em.getTransaction().commit();
	}
	
	public void update(T t) {
		em.getTransaction().begin();
		em.merge(t);
		em.getTransaction().commit();
	}
	
	public abstract List<T> getAll();
}