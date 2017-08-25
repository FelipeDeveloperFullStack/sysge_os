package br.com.sysge.service.conf;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.conf.Usuario;
import br.com.sysge.model.rh.Funcionario;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.service.rh.FuncionarioService;
import br.com.sysge.util.UsuarioSession;

public class UsuarioService extends GenericDaoImpl<Usuario, Long>{

	private static final long serialVersionUID = -2651419634334617651L;
	
	@Inject
	private FuncionarioService funcionarioService;
	
	public List<Usuario> pesquisarUsuario(Usuario usuario){
		if(usuario.getPerfilAcesso() != null){
			if(usuario.getPerfilAcesso().equals("*")){
				return super.findBySituation(usuario.getSituacao());
			}else{
				return super.findByParametersForSituation(usuario.getPerfilAcesso(), 
						usuario.getSituacao(), "usuario", "LIKE", "%", "%");
			}
		}
		return new ArrayList<Usuario>();
	}
	
	public Usuario salvar(Usuario usuario){
		try {
			consistirPerfilAcesso(usuario);
			verificarUsuarioLogado(usuario);
			verificarFuncionario(usuario);
			
			return super.save(consistirUsuario(usuario));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private void verificarUsuarioLogado(Usuario usuario){
		if(usuario.getId() != null){
			if(usuario.equals(obterSessaoUsuario())){
				if(usuario.getSituacao() == Situacao.INATIVO){
					throw new RuntimeException("Não é possível inativar o mesmo usuário logado no sistema!");
				}
			}
		}
	}
	
	private void consistirPerfilAcesso(Usuario usuario){
		if(usuario.getPerfilAcesso() == null){
			throw new RuntimeException("O perfil é obrigatório para o usuário!");
		}
	}
	
	private void verificarFuncionario(Usuario usuario){
		if(usuario.getId() == null){
			List<Usuario> usuarios = super.findAll();
			for(Usuario u : usuarios){
				if(u.getFuncionario().getId() == usuario.getFuncionario().getId()){
					throw new RuntimeException("Já existe um usuário cadastrado para esse funcionário, escolha outro funcionário!");
				}
			}
		}
	}
	
	private Usuario consistirUsuario(Usuario usuario){
		if(usuario.getId() == null){
			usuario.setUsuarioQueCadastrou(UsuarioSession.getSessionUsuario());
			usuario.setDataUsuarioCadastro(new Date());
			usuario.setSituacao(Situacao.ATIVO);
		}else{
			usuario.setUsuarioQueAlterou(UsuarioSession.getSessionUsuario());
			usuario.setDataUsuarioAlteracao(new Date());
		}
		return usuario;
	}
	
	public Usuario obterSessaoUsuario(){
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		return (Usuario) session.getAttribute("usuario");
	}
	
	public boolean isExisteUsuario(Usuario usuario){
		
		if(funcionarioService.verificarIdNull(usuario.getFuncionario())){
			throw new RuntimeException("O none do funcionário é obrigatório!");
		}else{
			if(usuario.getId() == null){
				verificarUsuarioESenha(usuario);
				if(isExisteNovoUsuario(usuario)){
					return true;
				}
			}else{
				verificarUsuarioESenha(usuario);
				if(isExiste(usuario)){
					return true;
				}
			}
		}
		
		return false;
	}
	
	private void verificarUsuarioESenha(Usuario usuario){
		if(usuario.getNomeUsuario().trim().isEmpty()){
			throw new RuntimeException("O nome de usuário é obrigatório!");
		}
		if(usuario.getSenhaUsuario().trim().isEmpty()){
			throw new RuntimeException("A senha de usuário é obrigatório!");
		}
	}
	
	private boolean isExiste(Usuario usuario){
		for(Usuario u : super.findAll()){
			if(usuario.getId() != u.getId()){
				if(usuario.getNomeUsuario().equals(u.getNomeUsuario())){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isExisteNovoUsuario(Usuario usuario){
		for(Usuario u : super.findAll()){
			if(usuario.getNomeUsuario().equals(u.getNomeUsuario())){
				return true;
			}
		}
		return false;
	}
	
	public Usuario procurarUsuarioPorPerfil(long idPerfilAcesso){
		return super.findByProperty(idPerfilAcesso, "perfilAcesso.id");
	}
	
	public boolean verificarSeExistePerfilAcesso(long idPerfilAcesso){
		int cont = 0;
		for(Usuario u : super.findAll()){
			if(u.getPerfilAcesso().getId() == idPerfilAcesso){
				cont += 1;
			}
		}
		if(cont == 0){
			return false;
		}else{
			return true;
		}
	}
	
	public String gerarSenhaAleatoria(Funcionario funcionario){
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		
		char[] letras = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
		
		long novaSenha = random.nextInt(10000) + 1;
		
	    for (int i = 0; i < 3; i++) {
	        int ch = random.nextInt (letras.length);
	        sb.append (letras [ch]);
	    }
	    
	    String senhaAleatoria = sb.toString() + novaSenha;
	    verificarFuncionarioEUsuario(funcionario, senhaAleatoria);
	    return senhaAleatoria;
	}
	
	private void verificarFuncionarioEUsuario(Funcionario funcionario, String novaSenha){
		for(Usuario usuario : super.findBySituation(Situacao.ATIVO)){
			if(usuario.getFuncionario().getId() == funcionario.getId()){
				usuario.setSenhaUsuario(novaSenha);
				super.save(usuario);
			}
		}
	}
}
