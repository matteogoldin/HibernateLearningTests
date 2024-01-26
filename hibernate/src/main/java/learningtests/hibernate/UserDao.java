package learningtests.hibernate;

import java.util.List;

import jakarta.persistence.Persistence;

public class UserDao extends BaseDao<User> {
	protected UserDao(String persistenceUnit) {
		super(User.class);
		emf = Persistence.createEntityManagerFactory(persistenceUnit);
		em = emf.createEntityManager();
	}

	@Override
	public List<User> getAll() {
		return em.createQuery("SELECT us FROM User us", User.class).getResultList();
	}
}