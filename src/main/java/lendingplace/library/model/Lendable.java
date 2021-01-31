package lendingplace.library.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "lendable")
public class Lendable {

	@Id
	@Column(name = "lendable_id")
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

	@Column(name = "image_path")
	private String imagePath;

	@Column(name = "creator")
	private String creator;

	@Column(name = "number_available")
	private int numberAvailable;

	@ManyToMany
	@JoinTable(name = "lendable_type_join_table", joinColumns = @JoinColumn(name = "lendable_id"), inverseJoinColumns = @JoinColumn(name = "type_id"))
	private Set<Category> categories;

	public Lendable() {
	}

	public Lendable(int id, String english, String hindi, String swahili, String arabic, String mandarin,
			String spanish, String french, String imagePath, String creator, int numberAvailable) {
		this.id = id;
		this.english = english;
		this.hindi = hindi;
		this.swahili = swahili;
		this.arabic = arabic;
		this.mandarin = mandarin;
		this.spanish = spanish;
		this.french = french;
		this.imagePath = imagePath;
		this.creator = creator;
		this.numberAvailable = numberAvailable;
	}

	public Lendable(String english, String hindi, String swahili, String arabic, String mandarin, String spanish,
			String french, String imagePath, String creator, int numberAvailable) {
		this.english = english;
		this.hindi = hindi;
		this.swahili = swahili;
		this.arabic = arabic;
		this.mandarin = mandarin;
		this.spanish = spanish;
		this.french = french;
		this.imagePath = imagePath;
		this.creator = creator;
		this.numberAvailable = numberAvailable;
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

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public int getNumberAvailable() {
		return numberAvailable;
	}

	public void setNumberAvailable(int numberAvailable) {
		this.numberAvailable = numberAvailable;
	}

	public Set<Category> getCategories() {
		return categories;
	}

	public void setCategories(Set<Category> categories) {
		this.categories = categories;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Lendable))
			return false;
		Lendable other = (Lendable) obj;
		return id == other.id;
	}

	/** Returns true if the set of categories has changed. Returns false if the item
	 * is already in the category, or if the category is null. */
	public boolean addToCategory(Category newCategory) {
		if (newCategory == null)
			return false;
		if (categories == null)
			categories = new HashSet<>();
		if (categories.contains(newCategory))
			return false;
		categories.add(newCategory);
		return true;
	}

	@Override
	public String toString() {
		return "Lendable [id=" + id + ", english=" + english + ", hindi=" + hindi + ", swahili=" + swahili + ", arabic="
				+ arabic + ", mandarin=" + mandarin + ", spanish=" + spanish + ", french=" + french + ", imagePath="
				+ imagePath + ", creator=" + creator + ", numberAvailable=" + numberAvailable + "]";
	}
	
	

}
