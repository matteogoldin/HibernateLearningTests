package learningtests.hibernate;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

class UserDaoTest {
	
	private static String persistenceUnit = "user-pu";
	private EntityManagerFactory emf;
	private EntityManager em;
	
	@BeforeEach
	public void setup() {
		//Establishes connection with the persistence unit and creates an entity manager instance
		emf = Persistence.createEntityManagerFactory(persistenceUnit);
		em = emf.createEntityManager();
	}
	
	@AfterEach
	public void teardown() {
		//Clean db
		em.createQuery("SELECT us FROM User us", User.class).getResultList().forEach(user -> em.remove(user));
		emf.close();
		em.close();
	}
	
	@Test
	void persistUserTest() {
		Address home = new Address("via Mucciano 42", "Borgo San Lorenzo", "Italia", "50032");
		User user = new User("1", "Matteo", "Goldin", home);
		Address work = new Address("via Roma 15", "Borgo San Lorenzo", "Italia", "50032");
		user.addSecondaryAddress(work);
		Document id_card = new Document("CA123GB", "id_card", new Date(), user);
		user.addDocument(id_card);
		
		UserDao userDao = new UserDao(persistenceUnit);
		userDao.add(user);
		
		User retrieved_user = em.createQuery("SELECT us FROM User us", User.class).getSingleResult();
		assertThat(retrieved_user).isEqualTo(user);
		assertThat(retrieved_user.getSecondaryAddresses()).contains(work);
		assertThat(retrieved_user.getDocuments()).contains(id_card);
	}
	
	@Test
	void findUser() {
		Address address = new Address("via Mucciano 42", "Borgo San Lorenzo", "Italia", "50032");
		User user1 = new User("1", "Matteo", "Goldin", address);
		User user2 = new User("2", "Marco", "Goldin", address);
		UserDao userDao = new UserDao(persistenceUnit);
		
		em.getTransaction().begin();
		em.persist(user1);
		em.persist(user2);
		em.getTransaction().commit();
		
		System.out.println(user2);
		System.out.println(userDao.findById(user2.getId()));
		assertThat(userDao.findById(user2.getId())).isEqualTo(user2);
	}
	
	@Test
	void removeUser() {
		Address address = new Address("via Mucciano 42", "Borgo San Lorenzo", "Italia", "50032");
		User user1 = new User("1", "Matteo", "Goldin", address);
		User user2 = new User("2", "Marco", "Goldin", address);
		Document id_card = new Document("CA123GB", "id_card", new Date(), user2);
		user2.addDocument(id_card);
		UserDao userDao = new UserDao(persistenceUnit);
		
		em.getTransaction().begin();
		em.persist(user1);
		em.persist(user2);
		em.getTransaction().commit();
		
		userDao.remove(user2);
		assertThat(em.createQuery("SELECT us FROM User us", User.class).getResultList()).containsOnly(user1);
		assertThat(em.createQuery("SELECT d FROM Document d", Document.class).getResultList()).doesNotContain(id_card);
	}
	
	@Test
	void mergeUser() {
		Address address = new Address("via Mucciano 42", "Borgo San Lorenzo", "Italia", "50032");
		User user = new User("1", "Matteo", "Goldin", address);
		Address work = new Address("via Roma 15", "Borgo San Lorenzo", "Italia", "50032");
		user.addSecondaryAddress(work);
		Document id_card = new Document("CA123GB", "id_card", new Date(), user);
		user.addDocument(id_card);
		UserDao userDao = new UserDao(persistenceUnit);
		
		em.getTransaction().begin();
		em.persist(user);
		em.getTransaction().commit();
		
		Address newAddress = new Address("via Don Luigi Sturzo 15", "Borgo San Lorenzo", "Italia", "50032");
		user.setMainAddress(newAddress);
		String new_street = "via Firenze 38";
		user.getSecondaryAddresses().get(0).setStreet(new_street);
		Date new_date = new Date();
		user.getDocuments().get(0).setValidity(new_date);
		
		userDao.update(user);
		User retrieved_user = em.find(User.class, user.getId());
		assertThat(retrieved_user.getMainAddress()).isEqualTo(newAddress);
		assertThat(retrieved_user.getSecondaryAddresses().get(0).getStreet()).isEqualTo(new_street);
		assertThat(retrieved_user.getDocuments().get(0).getValidity()).isEqualTo(new_date);
	}
	
	@Test
	void getAllUsers() {
		Address address = new Address("via Mucciano 42", "Borgo San Lorenzo", "Italia", "50032");
		User user1 = new User("1", "Matteo", "Goldin", address);
		User user2 = new User("2", "Marco", "Goldin", address);
		UserDao userDao = new UserDao(persistenceUnit);
		
		em.getTransaction().begin();
		em.persist(user1);
		em.persist(user2);
		em.getTransaction().commit();
		
		assertThat(userDao.getAll()).containsExactlyInAnyOrder(user1, user2);
	}

}
