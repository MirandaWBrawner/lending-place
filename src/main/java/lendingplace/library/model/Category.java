package lendingplace.library.model;

import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "item_type")
public class Category {
	
	@Id
	@Column(name = "type_id")
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
	
	public Category() {}

	public Category(int id, String english, String hindi, String swahili, String arabic, String mandarin,
			String spanish, String french, String imagePath) {
		this.id = id;
		this.english = english;
		this.hindi = hindi;
		this.swahili = swahili;
		this.arabic = arabic;
		this.mandarin = mandarin;
		this.spanish = spanish;
		this.french = french;
		this.imagePath = imagePath;
	}

	public Category(String english, String hindi, String swahili, String arabic, String mandarin, String spanish,
			String french, String imagePath) {
		this.english = english;
		this.hindi = hindi;
		this.swahili = swahili;
		this.arabic = arabic;
		this.mandarin = mandarin;
		this.spanish = spanish;
		this.french = french;
		this.imagePath = imagePath;
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

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Category))
			return false;
		Category other = (Category) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "Category [id=" + id + ", english=" + english + ", hindi=" + hindi + ", swahili=" + swahili + ", arabic="
				+ arabic + ", mandarin=" + mandarin + ", spanish=" + spanish + ", french=" + french + ", imagePath="
				+ imagePath + "]";
	}
	
	
	
}
