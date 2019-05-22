package fr.loria.orpailleur.revisor.webplatform.entity;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2014-05-05T15:57:22.919+0200")
@StaticMetamodel(User.class)
public class User_ {
	public static volatile SingularAttribute<User, String> email;
	public static volatile SingularAttribute<User, String> password;
	public static volatile SingularAttribute<User, String> firstname;
	public static volatile SingularAttribute<User, String> lastname;
	public static volatile SingularAttribute<User, Group> group;
}
