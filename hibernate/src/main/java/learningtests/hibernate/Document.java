package learningtests.hibernate;

import java.util.Date;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "documents")
public class Document {
	@Id
	private String number;
	private String name;
	private Date validity;
	
	@ManyToOne
	private User user;
	
	public Document() {	}

	public Document(String number, String name, Date validity, User user) {
		this.number = number;
		this.name = name;
		this.validity = validity;
		this.user = user;
	}
	
	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getValidity() {
		return validity;
	}

	public void setValidity(Date validity) {
		this.validity = validity;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, number, user, validity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Document other = (Document) obj;
		return Objects.equals(name, other.name) && Objects.equals(number, other.number)
				&& Objects.equals(user, other.user) && Objects.equals(validity.toInstant(), other.validity.toInstant());
	}
}
