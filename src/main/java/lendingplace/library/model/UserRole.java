package lendingplace.library.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "role")
public class UserRole {
	
	@Id
	@Column(name = "role_id")
	private int id;
	
	@Column(name = "name_en_us")
	private String english;

	@Column(name = "name_hi_in")
	private String hindi;

	@Column(name = "name_sw_tz")
	private String swahili;

	@Column(name = "name_ar_eg")
	private String arabic;

	@Column(name = "name_zh_cn")
	private String mandarin;

	@Column(name = "name_es_mx")
	private String spanish;

	@Column(name = "name_fr_fr")
	private String french;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "roles")
	private Set<User> users;
	
	public UserRole() {}

	public UserRole(int id, String english, String hindi, String swahili, String arabic, String mandarin,
			String spanish, String french) {
		super();
		this.id = id;
		this.english = english;
		this.hindi = hindi;
		this.swahili = swahili;
		this.arabic = arabic;
		this.mandarin = mandarin;
		this.spanish = spanish;
		this.french = french;
	}

	public UserRole(String english, String hindi, String swahili, String arabic, String mandarin, String spanish,
			String french) {
		super();
		this.english = english;
		this.hindi = hindi;
		this.swahili = swahili;
		this.arabic = arabic;
		this.mandarin = mandarin;
		this.spanish = spanish;
		this.french = french;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEnglish() {
		return english;
	}

	public void setEnglish(String english) {
		this.english = english;
	}

	public String getHindi() {
		return hindi;
	}

	public void setHindi(String hindi) {
		this.hindi = hindi;
	}

	public String getSwahili() {
		return swahili;
	}

	public void setSwahili(String swahili) {
		this.swahili = swahili;
	}

	public String getArabic() {
		return arabic;
	}

	public void setArabic(String arabic) {
		this.arabic = arabic;
	}

	public String getMandarin() {
		return mandarin;
	}

	public void setMandarin(String mandarin) {
		this.mandarin = mandarin;
	}

	public String getSpanish() {
		return spanish;
	}

	public void setSpanish(String spanish) {
		this.spanish = spanish;
	}

	public String getFrench() {
		return french;
	}

	public void setFrench(String french) {
		this.french = french;
	}
	
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> users) {
		this.users = users;
	}

	public String[] getNameArray() { 
		return new String[] {
			swahili, arabic, mandarin, french, hindi, spanish, english
		};
	}
	
	public Set<String> getNameSet() {
		Set<String> nameSet = new HashSet<>();
		for (String name: getNameArray()) {
			nameSet.add(name);
		}
		return nameSet;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof UserRole))
			return false;
		UserRole other = (UserRole) obj;
		return id == other.id;
	}

	@Override
	/** Returns the name of the role in several languages, 
	 * with a semicolon separating each translation. */
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < getNameArray().length; i++) {
			sb.append(getNameArray()[i]);
			if (i < getNameArray().length - 1) {
				sb.append(";");
			}
		}
		return sb.toString();
	}
	
	
}
