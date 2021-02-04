package lendingplace.library.model;

import java.sql.Timestamp;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "request")
public class Recommendation {

	@Id
	@Column(name = "request_id")
	private int id;
	
	@Column(name = "requester_name")
	private String name;
	
	@Column(name = "title")
	private String title;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "date_posted")
	private Timestamp postDate;
	
	public Recommendation() {}

	public Recommendation(int id, String name, String title, String content, Timestamp postDate) {
		this.id = id;
		this.name = name;
		this.title = title;
		this.content = content;
		this.postDate = postDate;
	}

	public Recommendation(String name, String title, String content, Timestamp postDate) {
		this.name = name;
		this.title = title;
		this.content = content;
		this.postDate = postDate;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Timestamp getPostDate() {
		return postDate;
	}

	public void setPostDate(Timestamp postDate) {
		this.postDate = postDate;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!(obj instanceof Recommendation))
			return false;
		Recommendation other = (Recommendation) obj;
		return id == other.id;
	}
}
