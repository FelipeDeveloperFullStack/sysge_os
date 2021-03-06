package br.com.sysge.service.global;

import java.util.Date;
import java.util.List;

import br.com.sysge.infraestrutura.cnpj.CnpjResource;
import br.com.sysge.infraestrutura.cnpj.ConsultaCNPJ;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.global.Fornecedor;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.model.type.UnidadeFederativa;
import br.com.sysge.util.UsuarioSession;

public class FornecedorService extends GenericDaoImpl<Fornecedor, Long> {

	private static final long serialVersionUID = -3438513129762783683L;

	public Fornecedor salvar(Fornecedor fornecedor) {
		try {
			if (fornecedor.getNomeFantasia().trim().equals("")) {
				throw new RuntimeException("O nome fantasia é obrigatório!");
			}
			
			verificar_CNPJ_fornecedor(fornecedor);
			
			return super.save(consistirFornecedor(fornecedor));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}

	public List<Fornecedor> procurarFornecedor(Fornecedor fornecedor) {
		if (fornecedor.getNomeFantasia().trim().isEmpty() && fornecedor.getCnpj().trim().isEmpty()) {
			return super.findBySituation(fornecedor.getSituacao());
		} else if (!fornecedor.getNomeFantasia().trim().isEmpty()) {
			return super.findByParametersForSituation(fornecedor.getNomeFantasia(), fornecedor.getSituacao(),
					"nomeFantasia", "LIKE", "%", "%");
		} else {
			return super.findByParametersForSituation(fornecedor.getCnpj(), fornecedor.getSituacao(),
					"cnpj", "=", "", "");
		}
	}

	public Fornecedor consultarCnpj(Fornecedor fornecedor) {
		try {
			if (fornecedor.getCnpj().replaceAll("\\D*", "").trim().isEmpty()) {
				throw new RuntimeException("O Cnpj é obrigatório!");
			}
			CnpjResource cnpjResource = ConsultaCNPJ.consultarCnpj(fornecedor.getCnpj());
			fornecedor.setBairro(cnpjResource.getBairro());
			fornecedor.setCep(cnpjResource.getCep());
			fornecedor.setCnpj(cnpjResource.getCnpj());
			fornecedor.setComplemento(cnpjResource.getComplemento());
			fornecedor.setEmail(cnpjResource.getEmail());
			fornecedor.setNomeFantasia(cnpjResource.getFantasia());
			fornecedor.setLogradouro(cnpjResource.getLogradouro());
			fornecedor.setCidade(cnpjResource.getMunicipio());
			fornecedor.setRazaoSocial(cnpjResource.getNome());
			fornecedor.setNumero(cnpjResource.getNumero());
			fornecedor.setTelefone(cnpjResource.getTelefone());
			fornecedor.setUnidadeFederativa(UnidadeFederativa.valueOf(cnpjResource.getUf()));
			fornecedor.setTipoEmpresa(cnpjResource.getTipo());
			// cnpjResource.getNatureza_juridica();
			// cnpjResource.getAbertura();
			// cnpjResource.getData_situacao();
			return fornecedor;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private Fornecedor consistirFornecedor(Fornecedor fornecedor) {
		if (fornecedor.getId() == null) {
			fornecedor.setUsuarioQueCadastrou(UsuarioSession.getSessionUsuario());
			fornecedor.setDataUsuarioCadastro(new Date());
			fornecedor.setSituacao(Situacao.ATIVO);
		}else{
			fornecedor.setUsuarioQueAlterou(UsuarioSession.getSessionUsuario());
			fornecedor.setDataUsuarioAlteracao(new Date());
		}
		return fornecedor;
	}
	
	
	private void verificar_CNPJ_fornecedor(Fornecedor fornecedor){
		for(Fornecedor f : super.findAll()){
			if(f.getCnpj() == fornecedor.getCnpj()){
				throw new RuntimeException("Já existe um fornecedor com esse mesmo CNPJ, verifique e tente novamente");
			}
		}
	}

}
