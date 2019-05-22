package fr.loria.orpailleur.revisor.webplatform.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrePersist;
import javax.persistence.Table;

/**
 * @author William Philbert
 */
@Entity
@Table(name = "USERS")
@NamedQueries({@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")})
public class User implements Serializable {
	
	// Constants :
	
	private static final long serialVersionUID = 1L;
	
	// Fields :
	
	@Id
	@Column(name = "EMAIL", nullable = false)
	private String email;
	
	@Column(name = "PASSWORD", nullable = false)
	private String password;
	
	@Column(name = "FIRSTNAME", nullable = false)
	private String firstname;
	
	@Column(name = "LASTNAME", nullable = false)
	private String lastname;
	
	@ManyToOne
	@JoinTable(name = "USER_GROUPS", joinColumns = {@JoinColumn(name = "USER_EMAIL", referencedColumnName = "EMAIL")}, inverseJoinColumns = {@JoinColumn(name = "GROUP_NAME", referencedColumnName = "NAME")})
	private Group group;
	
	// Constructors :
	
	public User() {
		this("", "", "", "", Group.REGISTERED); // TODO - WEB - remettre null par defaut. REGISTERED doit etre mis apres la confirmation de l'email.
	}
	
	public User(String email, String password, String firstname, String lastname, Group group) {
		this.email = email;
		this.password = password;
		this.firstname = firstname;
		this.lastname = lastname;
		this.group = group;
	}
	
	// Getters :
	
	public String getEmail() {
		return this.email;
	}
	
	public String getPassword() {
		return this.password;
	}
	
	public String getFirstname() {
		return this.firstname;
	}
	
	public String getLastname() {
		return this.lastname;
	}
	
	public Group getGroup() {
		return this.group;
	}
	
	// Setters :
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	public void setGroup(Group group) {
		this.group = group;
	}
	
	// Methods :
	
	@PrePersist
	private void lowerCaseMail() {
		this.email = this.email.toLowerCase();
	}
	
	public String getUserName() {
		return this.firstname + " " + this.lastname;
	}
	
	public String getGroupName() {
		return this.group.getName();
	}
	
	@Override
	public String toString() {
		return this.getUserName() + " (" + this.email + ")";
	}
	
}
