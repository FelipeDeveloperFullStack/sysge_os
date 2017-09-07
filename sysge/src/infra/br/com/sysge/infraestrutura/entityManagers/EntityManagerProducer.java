package br.com.sysge.infraestrutura.entityManagers;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

//@ApplicationScoped
public class EntityManagerProducer {
	private EntityManagerFactory factory;
	
	public EntityManagerProducer() {
		this.factory = Persistence.createEntityManagerFactory("sysge-unit");
	}
	
	//@Produces
	//@RequestScoped
	public EntityManager createEntityManager() {
		return this.factory.createEntityManager();
	}
	
	/*public void closeEntityManager(@Disposes EntityManager manager) {
		manager.close();
	}*/
}