package br.com.sysge.model.global;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import br.com.sysge.infraestrutura.dao.GenericDomain;
import br.com.sysge.model.type.Atividade;
import br.com.sysge.model.type.Categoria;
import br.com.sysge.model.type.Sexo;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.model.type.TipoContribuinte;
import br.com.sysge.model.type.TipoPessoa;
import br.com.sysge.model.type.UnidadeFederativa;

@Entity
@Table(name = "tbl_cliente")
public class Cliente extends GenericDomain{

	private static final long serialVersionUID = -4365055249547093538L;

	@Enumerated(EnumType.STRING)
	private TipoPessoa tipoPessoa;
	
	@Transient
	private String nomeTemporario;
	
	private String nomeFantasia;
	
	private String tipoEmpresa;
	
	private String telefone;
	
	private String celular;
	
	private String email;
	
	private String site;
	
	@Enumerated(EnumType.STRING)
	private Atividade atividade;
	
	@Enumerated(EnumType.STRING)
	private Categoria categoria;
	
	private String cnpj;
	
	private String razaoSocial;
	
	@Enumerated(EnumType.STRING)
	private TipoContribuinte tipoContribuinte;
	
	private String inscEstadual;
	
	private String inscMunicipal;
	
	private String telefoneAlternativo;
	
	private String observacao;
	
	private String horarioFuncionamento;
	
	private String cep;
	
	private String logradouro;
	
	private String numero = "S/N";
	
	private String complemento;
	
	private String bairro;
	
	private String cidade;
	
	private String codigoIBGE;
	
	@Enumerated(EnumType.STRING)
	private UnidadeFederativa unidadeFederativa;
	
	private String facebook;
	
	private String twitter;
	
	private String linkdin;
	
	private String youtube;
	
	private String nomeDaPessoaFisica;
	
	private String cpf;
	
	private String rg;
	
	@Temporal(TemporalType.DATE)
	private Date dataNascimento;
	
	private String localDeTrabalho;
	
	@Enumerated(EnumType.STRING)
	private Sexo sexo;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;
	
	public TipoPessoa getTipoPessoa() {
		return tipoPessoa;
	}

	public void setTipoPessoa(TipoPessoa tipoPessoa) {
		this.tipoPessoa = tipoPessoa;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Atividade getAtividade() {
		return atividade;
	}

	public void setAtividade(Atividade atividade) {
		this.atividade = atividade;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public TipoContribuinte getTipoContribuinte() {
		return tipoContribuinte;
	}

	public void setTipoContribuinte(TipoContribuinte tipoContribuinte) {
		this.tipoContribuinte = tipoContribuinte;
	}

	public String getInscEstadual() {
		return inscEstadual;
	}

	public void setInscEstadual(String inscEstadual) {
		this.inscEstadual = inscEstadual;
	}

	public String getInscMunicipal() {
		return inscMunicipal;
	}

	public void setInscMunicipal(String inscMunicipal) {
		this.inscMunicipal = inscMunicipal;
	}

	public String getTelefoneAlternativo() {
		return telefoneAlternativo;
	}

	public void setTelefoneAlternativo(String telefoneAlternativo) {
		this.telefoneAlternativo = telefoneAlternativo;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getHorarioFuncionamento() {
		return horarioFuncionamento;
	}

	public void setHorarioFuncionamento(String horarioFuncionamento) {
		this.horarioFuncionamento = horarioFuncionamento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public String getFacebook() {
		return facebook;
	}

	public void setFacebook(String facebook) {
		this.facebook = facebook;
	}

	public String getTwitter() {
		return twitter;
	}

	public void setTwitter(String twitter) {
		this.twitter = twitter;
	}

	public String getLinkdin() {
		return linkdin;
	}

	public void setLinkdin(String linkdin) {
		this.linkdin = linkdin;
	}

	public String getYoutube() {
		return youtube;
	}

	public void setYoutube(String youtube) {
		this.youtube = youtube;
	}

	public String getNomeDaPessoaFisica() {
		return nomeDaPessoaFisica;
	}

	public void setNomeDaPessoaFisica(String nomeDaPessoaFisica) {
		this.nomeDaPessoaFisica = nomeDaPessoaFisica;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getLocalDeTrabalho() {
		return localDeTrabalho;
	}

	public void setLocalDeTrabalho(String localDeTrabalho) {
		this.localDeTrabalho = localDeTrabalho;
	}

	public Sexo getSexo() {
		return sexo;
	}

	public void setSexo(Sexo sexo) {
		this.sexo = sexo;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public String getTipoEmpresa() {
		return tipoEmpresa;
	}

	public void setTipoEmpresa(String tipoEmpresa) {
		this.tipoEmpresa = tipoEmpresa;
	}

	public String getCodigoIBGE() {
		return codigoIBGE;
	}

	public void setCodigoIBGE(String codigoIBGE) {
		this.codigoIBGE = codigoIBGE;
	}

	public String getNomeTemporario() {
		return nomeTemporario;
	}

	public void setNomeTemporario(String nomeTemporario) {
		this.nomeTemporario = nomeTemporario;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((atividade == null) ? 0 : atividade.hashCode());
		result = prime * result + ((bairro == null) ? 0 : bairro.hashCode());
		result = prime * result + ((categoria == null) ? 0 : categoria.hashCode());
		result = prime * result + ((celular == null) ? 0 : celular.hashCode());
		result = prime * result + ((cep == null) ? 0 : cep.hashCode());
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result + ((cnpj == null) ? 0 : cnpj.hashCode());
		result = prime * result + ((codigoIBGE == null) ? 0 : codigoIBGE.hashCode());
		result = prime * result + ((complemento == null) ? 0 : complemento.hashCode());
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((facebook == null) ? 0 : facebook.hashCode());
		result = prime * result + ((horarioFuncionamento == null) ? 0 : horarioFuncionamento.hashCode());
		result = prime * result + ((inscEstadual == null) ? 0 : inscEstadual.hashCode());
		result = prime * result + ((inscMunicipal == null) ? 0 : inscMunicipal.hashCode());
		result = prime * result + ((linkdin == null) ? 0 : linkdin.hashCode());
		result = prime * result + ((localDeTrabalho == null) ? 0 : localDeTrabalho.hashCode());
		result = prime * result + ((logradouro == null) ? 0 : logradouro.hashCode());
		result = prime * result + ((nomeDaPessoaFisica == null) ? 0 : nomeDaPessoaFisica.hashCode());
		result = prime * result + ((nomeFantasia == null) ? 0 : nomeFantasia.hashCode());
		result = prime * result + ((nomeTemporario == null) ? 0 : nomeTemporario.hashCode());
		result = prime * result + ((numero == null) ? 0 : numero.hashCode());
		result = prime * result + ((observacao == null) ? 0 : observacao.hashCode());
		result = prime * result + ((razaoSocial == null) ? 0 : razaoSocial.hashCode());
		result = prime * result + ((rg == null) ? 0 : rg.hashCode());
		result = prime * result + ((sexo == null) ? 0 : sexo.hashCode());
		result = prime * result + ((site == null) ? 0 : site.hashCode());
		result = prime * result + ((situacao == null) ? 0 : situacao.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
		result = prime * result + ((telefoneAlternativo == null) ? 0 : telefoneAlternativo.hashCode());
		result = prime * result + ((tipoContribuinte == null) ? 0 : tipoContribuinte.hashCode());
		result = prime * result + ((tipoEmpresa == null) ? 0 : tipoEmpresa.hashCode());
		result = prime * result + ((tipoPessoa == null) ? 0 : tipoPessoa.hashCode());
		result = prime * result + ((twitter == null) ? 0 : twitter.hashCode());
		result = prime * result + ((unidadeFederativa == null) ? 0 : unidadeFederativa.hashCode());
		result = prime * result + ((youtube == null) ? 0 : youtube.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cliente other = (Cliente) obj;
		if (atividade != other.atividade)
			return false;
		if (bairro == null) {
			if (other.bairro != null)
				return false;
		} else if (!bairro.equals(other.bairro))
			return false;
		if (categoria != other.categoria)
			return false;
		if (celular == null) {
			if (other.celular != null)
				return false;
		} else if (!celular.equals(other.celular))
			return false;
		if (cep == null) {
			if (other.cep != null)
				return false;
		} else if (!cep.equals(other.cep))
			return false;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (cnpj == null) {
			if (other.cnpj != null)
				return false;
		} else if (!cnpj.equals(other.cnpj))
			return false;
		if (codigoIBGE == null) {
			if (other.codigoIBGE != null)
				return false;
		} else if (!codigoIBGE.equals(other.codigoIBGE))
			return false;
		if (complemento == null) {
			if (other.complemento != null)
				return false;
		} else if (!complemento.equals(other.complemento))
			return false;
		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (facebook == null) {
			if (other.facebook != null)
				return false;
		} else if (!facebook.equals(other.facebook))
			return false;
		if (horarioFuncionamento == null) {
			if (other.horarioFuncionamento != null)
				return false;
		} else if (!horarioFuncionamento.equals(other.horarioFuncionamento))
			return false;
		if (inscEstadual == null) {
			if (other.inscEstadual != null)
				return false;
		} else if (!inscEstadual.equals(other.inscEstadual))
			return false;
		if (inscMunicipal == null) {
			if (other.inscMunicipal != null)
				return false;
		} else if (!inscMunicipal.equals(other.inscMunicipal))
			return false;
		if (linkdin == null) {
			if (other.linkdin != null)
				return false;
		} else if (!linkdin.equals(other.linkdin))
			return false;
		if (localDeTrabalho == null) {
			if (other.localDeTrabalho != null)
				return false;
		} else if (!localDeTrabalho.equals(other.localDeTrabalho))
			return false;
		if (logradouro == null) {
			if (other.logradouro != null)
				return false;
		} else if (!logradouro.equals(other.logradouro))
			return false;
		if (nomeDaPessoaFisica == null) {
			if (other.nomeDaPessoaFisica != null)
				return false;
		} else if (!nomeDaPessoaFisica.equals(other.nomeDaPessoaFisica))
			return false;
		if (nomeFantasia == null) {
			if (other.nomeFantasia != null)
				return false;
		} else if (!nomeFantasia.equals(other.nomeFantasia))
			return false;
		if (nomeTemporario == null) {
			if (other.nomeTemporario != null)
				return false;
		} else if (!nomeTemporario.equals(other.nomeTemporario))
			return false;
		if (numero == null) {
			if (other.numero != null)
				return false;
		} else if (!numero.equals(other.numero))
			return false;
		if (observacao == null) {
			if (other.observacao != null)
				return false;
		} else if (!observacao.equals(other.observacao))
			return false;
		if (razaoSocial == null) {
			if (other.razaoSocial != null)
				return false;
		} else if (!razaoSocial.equals(other.razaoSocial))
			return false;
		if (rg == null) {
			if (other.rg != null)
				return false;
		} else if (!rg.equals(other.rg))
			return false;
		if (sexo != other.sexo)
			return false;
		if (site == null) {
			if (other.site != null)
				return false;
		} else if (!site.equals(other.site))
			return false;
		if (situacao != other.situacao)
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		if (telefoneAlternativo == null) {
			if (other.telefoneAlternativo != null)
				return false;
		} else if (!telefoneAlternativo.equals(other.telefoneAlternativo))
			return false;
		if (tipoContribuinte != other.tipoContribuinte)
			return false;
		if (tipoEmpresa == null) {
			if (other.tipoEmpresa != null)
				return false;
		} else if (!tipoEmpresa.equals(other.tipoEmpresa))
			return false;
		if (tipoPessoa != other.tipoPessoa)
			return false;
		if (twitter == null) {
			if (other.twitter != null)
				return false;
		} else if (!twitter.equals(other.twitter))
			return false;
		if (unidadeFederativa != other.unidadeFederativa)
			return false;
		if (youtube == null) {
			if (other.youtube != null)
				return false;
		} else if (!youtube.equals(other.youtube))
			return false;
		return true;
	}
	
	
}
