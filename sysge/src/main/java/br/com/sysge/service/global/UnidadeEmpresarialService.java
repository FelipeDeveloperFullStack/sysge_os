package br.com.sysge.service.global;

import java.util.Date;
import java.util.List;

import br.com.sysge.infraestrutura.cnpj.CnpjResource;
import br.com.sysge.infraestrutura.cnpj.ConsultaCNPJ;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.global.UnidadeEmpresarial;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.model.type.UnidadeFederativa;
import br.com.sysge.util.UsuarioSession;

public class UnidadeEmpresarialService extends GenericDaoImpl<UnidadeEmpresarial, Long>{

	private static final long serialVersionUID = 2733687487752814027L;
	
	public UnidadeEmpresarial salvar(UnidadeEmpresarial unidadeEmpresarial){
		try {
			if(unidadeEmpresarial.getNomeFantasia().trim().equals("")){
				throw new RuntimeException("O nome fantasia é obrigatório!");
			}
			if(unidadeEmpresarial.getRazaoSocial().trim().equals("")){
				throw new RuntimeException("A razão social é obrigatório!");
			}
			return super.save(consistirUnidadeEmpresarial(unidadeEmpresarial));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private UnidadeEmpresarial consistirUnidadeEmpresarial(UnidadeEmpresarial unidadeEmpresarial){
		if(unidadeEmpresarial.getId() == null){
			unidadeEmpresarial.setUsuarioQueCadastrou(UsuarioSession.getSessionUsuario());
			unidadeEmpresarial.setDataUsuarioCadastro(new Date());
			unidadeEmpresarial.setSituacao(Situacao.ATIVO);
		}else{
			unidadeEmpresarial.setUsuarioQueAlterou(UsuarioSession.getSessionUsuario());
			unidadeEmpresarial.setDataUsuarioAlteracao(new Date());
		}
		return unidadeEmpresarial;
	}
	
	public List<UnidadeEmpresarial> pesquisarUnidadeEmpresarial(UnidadeEmpresarial unidadeEmpresarial){
		if(unidadeEmpresarial.getRazaoSocial().trim().isEmpty()){
			return super.findBySituation(unidadeEmpresarial.getSituacao());
		}else{
			return super.findByParametersForSituation(unidadeEmpresarial.getRazaoSocial(), 
					unidadeEmpresarial.getSituacao(), "razaoSocial", "LIKE", "%", "%");
		}
	}
	
	public void verificarSeExisteUnidadeEmpresarialCadastradoComMesmaDescricao(UnidadeEmpresarial unidadeEmpresarial){
		List<UnidadeEmpresarial> unidadeEmpresariais = super.findAll();
		for(UnidadeEmpresarial s : unidadeEmpresariais){
			verificarDescricaoIgual(s, unidadeEmpresarial);
		}
	}
	
	private void verificarDescricaoIgual(UnidadeEmpresarial u, UnidadeEmpresarial unidadeEmpresarial){
		if(unidadeEmpresarial.getNomeFantasia().trim().equalsIgnoreCase(u.getNomeFantasia())){
			if(unidadeEmpresarial.getId() == null){
				mostrarMensagemParaUsuario(u);
			}else{
				if(u.getId() != unidadeEmpresarial.getId()){
					mostrarMensagemParaUsuario(u);
				}
			}
		}
	}
	
	private void mostrarMensagemParaUsuario(UnidadeEmpresarial u){
		throw new RuntimeException("Existe unidade empresarial "+u.getNomeFantasia()+" "
				+ "de código "+u.getId()+" já está cadastrado, "
				+ "por favor escolha outra unidade empresarial.");
	}
	
	public UnidadeEmpresarial consultarCnpj(UnidadeEmpresarial unidadeEmpresarial) {
		try {
			if (unidadeEmpresarial.getCnpj().replaceAll("\\D*", "").trim().isEmpty()) {
				throw new RuntimeException("O Cnpj é obrigatório!");
			}
			CnpjResource cnpjResource = ConsultaCNPJ.consultarCnpj(unidadeEmpresarial.getCnpj());
			unidadeEmpresarial.setBairro(cnpjResource.getBairro());
			unidadeEmpresarial.setCEP(cnpjResource.getCep());
			unidadeEmpresarial.setCnpj(cnpjResource.getCnpj());
			unidadeEmpresarial.setComplemento(cnpjResource.getComplemento());
			unidadeEmpresarial.setEmail(cnpjResource.getEmail());
			unidadeEmpresarial.setNomeFantasia(cnpjResource.getFantasia());
			unidadeEmpresarial.setLogradouro(cnpjResource.getLogradouro());
			unidadeEmpresarial.setCidade(cnpjResource.getMunicipio());
			unidadeEmpresarial.setRazaoSocial(cnpjResource.getNome());
			unidadeEmpresarial.setNumero(cnpjResource.getNumero());
			unidadeEmpresarial.setTelefone(cnpjResource.getTelefone());
			unidadeEmpresarial.setUnidadeFederativa(UnidadeFederativa.valueOf(verificarCnpjVazio(cnpjResource.getUf())));
			
			return unidadeEmpresarial;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private String verificarCnpjVazio(String cnpj){
		if(cnpj.isEmpty()){
			return UnidadeFederativa.GO.toString();
		}
		return cnpj;
	}

}
