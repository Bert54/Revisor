package fr.loria.orpailleur.revisor.webplatform.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author William Philbert
 */
@Entity
@Table(name = "GROUPS")
public class Group implements Serializable {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	public static final Group ADMIN = new Group("admin", "Can use revisor engines AND manage user accounts and groups.");
	public static final Group USER = new Group("user", "Can use revisor engines.");
	public static final Group REGISTERED = new Group("registered", "Can only see their profile. Need admin check");
	public static final Group DENIED = new Group("denied", "Can only see their profile. Access denied by admin");
	
	// Fields :
	
	@Id
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "DESCRIPTION")
	private String description;
	
	// Constructors :
	
	public Group() {
	}
	
	public Group(String name, String description) {
		this.name = name;
		this.description = description;
	}
	
	// Getters :
	
	public String getName() {
		return this.name;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	// Setters :
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	// Methods :
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
