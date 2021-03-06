package br.com.sysge.service.global;

import java.util.Date;
import java.util.List;

import br.com.sysge.infraestrutura.cnpj.CnpjResource;
import br.com.sysge.infraestrutura.cnpj.ConsultaCNPJ;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.global.Cliente;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.model.type.TipoPessoa;
import br.com.sysge.model.type.UnidadeFederativa;
import br.com.sysge.util.UsuarioSession;

public class ClienteService extends GenericDaoImpl<Cliente, Long> {

	private static final long serialVersionUID = -3438513129762783683L;

	public Cliente salvar(Cliente cliente) {
		try {
			if (cliente.getTipoPessoa() == TipoPessoa.PESSOA_FISICA) {
				if (cliente.getNomeDaPessoaFisica().trim().equals("")) {
					throw new RuntimeException("O nome da pessoa física é obrigatório!");
				}
			}
			if (cliente.getTipoPessoa() == TipoPessoa.PESSOA_JURIDICA) {
				if (cliente.getNomeFantasia().trim().equals("")) {
					throw new RuntimeException("O nome da pessoa jurídica é obrigatório!");
				}
			}
			
			
			return super.save(consistirCliente(setarNomes(cliente)));
		} catch (RuntimeException e) {
			e.getStackTrace();
			throw new RuntimeException(e.getMessage());
		}
	}
	
	private Cliente setarNomes(Cliente cliente){
		if(cliente.getTipoPessoa() == TipoPessoa.PESSOA_FISICA){
			cliente.setNomeFantasia("");
			cliente.setNomeTemporario("");
			return cliente;
		}else{
			cliente.setNomeDaPessoaFisica("");
			cliente.setNomeTemporario("");
			return cliente;
		}
	}
	
	public Cliente verificarTipoPessoa(Cliente cliente){
		if(cliente.getTipoPessoa() == TipoPessoa.PESSOA_FISICA){
			cliente.setNomeTemporario(cliente.getNomeDaPessoaFisica());
			return cliente;
		}else{
			cliente.setNomeTemporario(cliente.getNomeFantasia());
			return cliente;
		}
	}
	
	public String getTipoDocumentoPessoa(Cliente cliente){
		if(cliente.getTipoPessoa() == TipoPessoa.PESSOA_FISICA){
			return cliente.getCpf();
		}else{
			return cliente.getCnpj();
		}
	}

	public List<Cliente> procurarCliente(Cliente cliente) {
		if (cliente.getNomeTemporario().trim().isEmpty() && cliente.getCnpj().trim().isEmpty() && cliente.getCpf().trim().isEmpty()) {
			return super.findBySituationAndTipoPessoa(cliente.getSituacao(), cliente.getTipoPessoa());
		} else {
			if(cliente.getTipoPessoa() == TipoPessoa.PESSOA_FISICA){
				if(!cliente.getNomeDaPessoaFisica().trim().isEmpty()){
					return super.findByParametersForSituation(cliente.getNomeDaPessoaFisica(), cliente.getTipoPessoa(),
							cliente.getSituacao(), "nomeDaPessoaFisica", "LIKE", "%", "%");
				}else{
					return super.findByParametersForSituation(cliente.getCpf(), cliente.getSituacao(),
							"cpf", "=", "", "");
				}
			}else{
				if(!cliente.getNomeFantasia().trim().isEmpty()){
					return super.findByParametersForSituation(cliente.getNomeFantasia(), cliente.getTipoPessoa(),
							cliente.getSituacao(), "nomeFantasia", "LIKE", "%", "%");
				}else{
					return super.findByParametersForSituation(cliente.getCnpj(), cliente.getSituacao(),
							"cnpj", "=", "", "");
				}
			}
		}
	}

	public Cliente consultarCnpj(Cliente cliente) {
		try {
			if (cliente.getCnpj().replaceAll("\\D*", "").trim().isEmpty()) {
				throw new RuntimeException("O Cnpj é obrigatório!");
			}
			CnpjResource cnpjResource = ConsultaCNPJ.consultarCnpj(cliente.getCnpj());
			cliente.setBairro(cnpjResource.getBairro());
			cliente.setCep(cnpjResource.getCep());
			cliente.setCnpj(cnpjResource.getCnpj());
			cliente.setComplemento(cnpjResource.getComplemento());
			cliente.setEmail(cnpjResource.getEmail());
			cliente.setNomeFantasia(cnpjResource.getFantasia());
			cliente.setNomeTemporario(cnpjResource.getFantasia());
			cliente.setLogradouro(cnpjResource.getLogradouro());
			cliente.setCidade(cnpjResource.getMunicipio());
			cliente.setRazaoSocial(cnpjResource.getNome());
			cliente.setNumero(cnpjResource.getNumero());
			cliente.setTelefone(cnpjResource.getTelefone());
			cliente.setUnidadeFederativa(UnidadeFederativa.valueOf(cnpjResource.getUf()));
			cliente.setTipoEmpresa(cnpjResource.getTipo());
			// cnpjResource.getNatureza_juridica();
			// cnpjResource.getAbertura();
			// cnpjResource.getData_situacao();
			return cliente;
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	private Cliente consistirCliente(Cliente cliente) {

		if (cliente.getId() == null) {
			cliente.setUsuarioQueCadastrou(UsuarioSession.getSessionUsuario());
			cliente.setDataUsuarioCadastro(new Date());
			cliente.setSituacao(Situacao.ATIVO);
		}else{
			cliente.setUsuarioQueAlterou(UsuarioSession.getSessionUsuario());
			cliente.setDataUsuarioAlteracao(new Date());
		}
		return cliente;
	}
	
	private void verificar_CPF_CNPJ_Cliente(Cliente cliente){
		
		for(Cliente c : super.findAll()){
			if(cliente.getTipoPessoa() == TipoPessoa.PESSOA_FISICA){
				if(c.getCpf() == cliente.getCpf()){
					throw new RuntimeException("Já existe um cliente cadastrado com esse CPF, verifique e tente novamente!");
				}
			}else{
				if(c.getCnpj().equals(cliente.getCnpj())){
					throw new RuntimeException("Já existe um cliente cadastrado com esse CNPJ, verifique e tente novamente!");
				}
			}
		}
	}

	
	public boolean isExisteClienteInativo(){
		if(super.findAll().isEmpty()){
			return true;
		}
		if(super.findBySituation(Situacao.ATIVO).isEmpty()){
			return true;
		}
		return false;
	}
	
}
