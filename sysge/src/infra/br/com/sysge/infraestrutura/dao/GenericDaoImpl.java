package br.com.sysge.infraestrutura.dao;

import java.lang.reflect.ParameterizedType;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import br.com.sysge.model.type.Categoria;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.model.type.StatusOS;
import br.com.sysge.model.type.TipoPessoa;

/**
 * @param <E> -> Entidade
 * @param <I> -> Identificador
 */

public class GenericDaoImpl<E, I> implements GenericDao<E, I> {

	private static final long serialVersionUID = 7317998972296404455L;

	private Class<E> entityClass;
	
	private EntityManager manager;
	
	private EntityTransaction tx;
	
	@SuppressWarnings({ "unchecked"})
	public GenericDaoImpl() {
		Class<?> clazz = getClass();
		if (!(clazz.getGenericSuperclass() instanceof ParameterizedType)) {
			clazz = clazz.getSuperclass();
		}

		ParameterizedType parameterizedType = (ParameterizedType) clazz.getGenericSuperclass();
		entityClass = (Class<E>) parameterizedType.getActualTypeArguments()[0];
		
	    this.manager = JPAUtil.getEntityManager();
		
	}
	
	private EntityTransaction initializationTransactional(){
		return this.manager.getTransaction();
	}
	
	private void consistEntityTransactional(EntityTransaction entityTransaction){
		if(entityTransaction.isActive()){
			entityTransaction.rollback();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public E save(E entity) {
		Object obj;
		try {
			tx = initializationTransactional();
			tx.begin();
			obj = manager.merge(entity);
			tx.commit();
			return (E) obj;
		} catch (RuntimeException e) {
			consistEntityTransactional(tx);
			throw e;
		}finally {
		}
	}

	@Override
	public void remove(I id) {
		try {
			tx = initializationTransactional();
			//E objeto = findById(id);
			tx.begin();
			manager.remove(manager.getReference(entityClass, id));
			tx.commit();
		} catch (RuntimeException e) {
			consistEntityTransactional(tx);
			throw e;
		}finally {
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAll() {
		try {
			Query query = manager.createQuery("SELECT a FROM "+entityClass.getSimpleName() + " a");
			return query.getResultList();
		} catch (RuntimeException e) {
			throw e;
		}finally {
		}
	}
	
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<E> findByDate(Date dataInicial, Date dataFinal, String atributoData){
		try {
			TypedQuery<E> query = (TypedQuery<E>) manager.createQuery(""
					+ "SELECT b FROM "+entityClass.getSimpleName()+" b "
					+ "WHERE b."+atributoData+" >= "+dataInicial+" OR b.dataBackup <= "+dataFinal+"");
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}finally {
		}
	}

	@Override
	public E findById(I id) {
		try {
			return manager.find(entityClass, id);
		} catch (RuntimeException e) {
			throw e;
		}finally {
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAllByIdWithNativeQuery(I id, String table, String column){
		try {
			TypedQuery<E> query = (TypedQuery<E>) manager.createNativeQuery(
					"SELECT * FROM "+table+" t "
				  + "WHERE t."+column+ " = "+id, entityClass);
			
			return query.getResultList();
		} catch (RuntimeException e) {
			throw e;
		}finally{
			manager.clear();
			
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findAllByIdWithNativeQuery(I id, boolean valueBoolean, String table, String columnID, String columnBoolean){
		try {
			TypedQuery<E> query = (TypedQuery<E>) manager.createNativeQuery(
					"SELECT * FROM "+table+" t "
							+ "WHERE t."+columnID+ " = "+id + " AND t."+columnBoolean+ " = "+valueBoolean, entityClass);
			
			return query.getResultList();
		} catch (RuntimeException e) {
			throw e;
		}finally{
			manager.clear();
			
		}
	}

	/***
	 * Esse método serve para procurar dados por parâmetros, definindo o tipo da condição.
	 * Por exemplo: O parâmetro 'value' serve para passar argumentos que irão ser procurados,
	 * o parâmetro 'situation' serve para definir se ira ser ATIVO ou INATIVO,
	 * o parâmetro 'attributeClassValue' é o atributo da classe,
	 * o parâmetro 'attributeClassSituacao' é o atributo da classe Enum 'Situacao',
	 * o parâmetro 'condition' é para definir qual o tipo de condição JPQL: Por exemplo 'LIKE', 
	 * '=', '!=' entre outros.
	 * O parâmtro 'paramLike' é para informar o símbolo '%', caso o parâmetro 'condition' esteja como 'LIKE'
	 * @param <value> Valor que irá ser pesquisado.
	 * @param <situation> Situação se é ATIVO ou INATIVO 
	 * @param <attributeClassValue> É o atributo da classe
	 * @param <attributeClassSituacao> É o atributo da classe Enum 'Situacao'.
	 * @param <condition> Definir qual o tipo de condição JPQL: Por exemplo 'LIKE', '=', '!=' entre outros.
	 * @param <paramLike> É para informar o símbolo '%', caso o parâmetro 'condition' esteja como 'LIKE'
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByParametersForSituation(Object value, Situacao situation, 
			String attributeClass, String condition, String paramLikeLeft, String paramLikeRight) {
		try {
			Query query = manager.createQuery(""
					+ "SELECT e FROM "+entityClass.getSimpleName()+" e "
					+ "WHERE e."+attributeClass+" "+condition+" '"+paramLikeLeft+""+value+""+paramLikeRight+"' "
					+ "AND e.situacao = '"+situation+"'");
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}finally{
			manager.clear();
			
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByParametersForSituation(Object value, Categoria categoria, Situacao situation, 
			String attributeClass, String condition, String paramLikeLeft, String paramLikeRight) {
		try {
			Query query = manager.createQuery(""
					+ "SELECT e FROM "+entityClass.getSimpleName()+" e "
					+ "WHERE e."+attributeClass+" "+condition+" '"+paramLikeLeft+""+value+""+paramLikeRight+"' "
					+ "AND e.situacao = '"+situation+"' "
					+ "AND e.categoria = '"+categoria+"'");
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}finally{
			manager.clear();
			
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<E> findByParametersForSituation(Object value, TipoPessoa tipoPessoa, Situacao situation, 
			String attributeClass, String condition, String paramLikeLeft, String paramLikeRight) {
		try {
			Query query = manager.createQuery(""
					+ "SELECT e FROM "+entityClass.getSimpleName()+" e "
					+ "WHERE e."+attributeClass+" "+condition+" '"+paramLikeLeft+""+value+""+paramLikeRight+"' "
					+ "AND e.situacao = '"+situation+"' "
					+ "AND e.tipoPessoa = '"+tipoPessoa+"'");
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}finally{
			manager.clear();
			
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findBySituation(Situacao situacao) {
		try {
			Query query = manager.createQuery("SELECT s FROM "+entityClass.getSimpleName() + " s WHERE s.situacao = :situacao"); 
			query.setParameter("situacao", situacao);
			return query.getResultList();
		} catch (Exception e) {
			throw e;			
		}finally{
			manager.clear();
			
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByStatusOs(StatusOS statusOs) {
		try {
			Query query = manager.createQuery("SELECT s FROM "+entityClass.getSimpleName() + " s WHERE s.statusOS = :statusOS");
			query.setParameter("statusOS", statusOs);
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByStatusOs(StatusOS statusOS1, StatusOS statusOS2){
		try {
			Query query = manager.createQuery
					("SELECT os FROM "+entityClass.getSimpleName()+ " os "
							+ "WHERE os.statusOS = :status1 OR os.statusOS = :status2");
			query.setParameter("status1", statusOS1);
			query.setParameter("status2", statusOS2);
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}finally{
			manager.clear();
			
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findBySituationAndTipoPessoa(Situacao situacao, TipoPessoa tipoPessoa) {
		try {
			Query query = manager.createQuery(""
					+ "SELECT sc FROM "+entityClass.getSimpleName()+ " sc "
					+ "WHERE sc.situacao = '"+situacao  +"' "
					+ "AND sc.tipoPessoa  = '"+tipoPessoa +"'");
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}finally{
			manager.clear();
			
		}
		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<E> findBySituationAndCategoriaAndTipoPessoa(Situacao situacao, Categoria categoria, TipoPessoa tipoPessoa){
		try {
			Query query = manager.createQuery(""
					+ "SELECT sc FROM "+entityClass.getSimpleName()+ " sc "
					+ "WHERE sc.situacao = '"+situacao  +"' "
					+ "AND sc.categoria  = '"+categoria +"' "
					+ "AND sc.tipoPessoa = '"+tipoPessoa+"'");
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}finally{
			manager.clear();
			
		}
		
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public E findUserAndPassword(String usuario, String senha, Situacao situacao){
		try {
			Query query = manager.createQuery(
					  "SELECT us FROM Usuario us "
					+ "WHERE us.nomeUsuario = :usuario "
					+ "AND us.senhaUsuario = :senha "
					+ "AND us.situacao = :situacao");
			query.setParameter("usuario", usuario);
			query.setParameter("senha", senha);
			query.setParameter("situacao", situacao);
			
			return (E) query.getSingleResult();
		} catch (NoResultException nre) {
			throw new NoResultException(nre.getMessage());
		}finally{
			manager.clear();
			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByNumeroStatusOS(long numero, StatusOS statusOS) {
		try {
			Query query = manager.createQuery
					("SELECT os FROM "+entityClass.getSimpleName() + " os "
					+ "WHERE os.id = '"+numero+"' AND os.statusOS = '"+statusOS+"'");
			
			return query.getResultList();
		} catch (NoResultException e) {
			throw new NoResultException(e.getMessage());
		}finally{
			manager.clear();
			
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByDataEntradaOs(Date dataInicial, Date dataFinal) {
		try {
			Query query = manager.createQuery
					("SELECT os FROM "+entityClass.getSimpleName() + " os "
					+ "WHERE os.dataEntrada between :dataInicial AND : dataFinal");
			query.setParameter("dataInicial", dataInicial);
			query.setParameter("dataFinal", dataFinal);
			
			return query.getResultList();
		} catch (NoResultException e) {
			throw new NoResultException(e.getMessage());
		}finally{
			manager.clear();
			
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public E findByData(Date data, String atributoClasse) {
		try {
			Query query = manager.createQuery
					("SELECT os FROM "+entityClass.getSimpleName() + " os "
							+ "WHERE os."+atributoClasse+" = :data");
			query.setParameter("data", data);
			
			return (E) query.getSingleResult();
		} catch (NoResultException e) {
			throw new NoResultException(e.getMessage());
		}finally{
			manager.clear();
			
		}
	}
	@SuppressWarnings({ "unchecked"})
	@Override
	public List<E> findByData(String atributoClasse, Date data) {
		try {
			Query query = manager.createQuery
					("SELECT b FROM "+entityClass.getSimpleName() + " b "
							+ "WHERE b."+atributoClasse+" = :data");
			query.setParameter("data", data);
			
			return query.getResultList();
		} catch (NoResultException e) {
			throw new NoResultException(e.getMessage());
		}finally{
			manager.clear();
		}
	}

	@Override
	public void removeByObject(E object) {
		try {
			tx = initializationTransactional();
			tx.begin();
			manager.remove(object);
			tx.commit();
		} catch (RuntimeException e) {
			consistEntityTransactional(tx);
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<E> findByListProperty(Object object, String attributeClass) {
		try {
			Query query = manager.createQuery
					("SELECT p FROM "+entityClass.getSimpleName() + " p "
							+ " WHERE p."+attributeClass+" = "+object+"");
			
			return query.getResultList();
		} catch (Exception e) {
			throw e;
		}finally{
			manager.clear();
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public E findByProperty(Object object, String attributeClass) {
		try {
			Query query = manager.createQuery
					("SELECT p FROM "+entityClass.getSimpleName() + " p "
							+ " WHERE p."+attributeClass+" = "+object+"");
			
			return (E) query.getSingleResult();
		} catch (Exception e) {
			throw e;
		}finally{
			manager.clear();
			
		}
	}

	public EntityManager getEntityManager() {
		return manager;
	}

	public void setManager(EntityManager entityManager) {
		this.manager = entityManager;
	}

	public Class<E> getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class<E> entityClass) {
		this.entityClass = entityClass;
	}
	
	
}