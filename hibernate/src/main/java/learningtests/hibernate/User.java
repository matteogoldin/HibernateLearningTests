package learningtests.hibernate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User {
	@Id
	private String id;
	
	private String firstName;
	private String lastName;
	
	@Embedded
	private Address mainAddress;
	
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
	        name="addresses",
	        joinColumns= @JoinColumn(name="user_id"))
	private List<Address> secondaryAddresses;
	
	@OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	private List<Document> documents; 
	
	public User() {
		this.secondaryAddresses = new ArrayList<>();
		this.documents = new ArrayList<>();
	}
	
	public User(String id, String firstName, String lastName, Address address) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mainAddress = address;
		this.secondaryAddresses = new ArrayList<>();
		this.documents = new ArrayList<>();
	}
	
	public void addDocument(Document document) {
		documents.add(document);
	}
	
	public void removeDocument(Document document) {
		documents.remove(document);
	}
	
	public void addSecondaryAddress(Address address) {
		secondaryAddresses.add(address);
	}
	
	public void removeSecondaryAddress(Address address) {
		secondaryAddresses.remove(address);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Address getMainAddress() {
		return mainAddress;
	}

	public void setMainAddress(Address address) {
		this.mainAddress = address;
	}

	public List<Address> getSecondaryAddresses() {
		return secondaryAddresses;
	}

	public void setSecondaryAddresses(List<Address> secondaryAddresses) {
		this.secondaryAddresses = secondaryAddresses;
	}

	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
	}

	@Override
	public int hashCode() {
		return Objects.hash(documents, firstName, id, lastName, mainAddress, secondaryAddresses);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(firstName, other.firstName)
				&& Objects.equals(id, other.id) && Objects.equals(lastName, other.lastName)
				&& Objects.equals(mainAddress, other.mainAddress);
	}
}