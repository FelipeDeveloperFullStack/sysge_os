package br.com.sysge.model.global;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

import br.com.sysge.infraestrutura.dao.GenericDomain;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.model.type.UnidadeFederativa;

@Entity
@Table(name = "tbl_unidade_empresarial")
public class UnidadeEmpresarial extends GenericDomain{

	private static final long serialVersionUID = -8300719240163781339L;
	
	private String razaoSocial;
	
	private String nomeFantasia;
	
	private String Cnpj;
	
	private String inscEstadual;
	
	private String inscMunicipal;
	
	private String telefone;
	
	private String celular;
	
	private String celular2;
	
	private String celular3;
	
	private String logradouro;
	
	private String CEP;
	
	private String complemento;
	
	private String cidade;
	
	private String bairro;
	
	private String numero;
	
	private String email;
	
	@Enumerated(EnumType.STRING)
	private UnidadeFederativa unidadeFederativa;
	
	private String site;
	
	@Enumerated(EnumType.STRING)
	private Situacao situacao;

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	public String getCnpj() {
		return Cnpj;
	}

	public void setCnpj(String cnpj) {
		Cnpj = cnpj;
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

	public String getCelular2() {
		return celular2;
	}

	public void setCelular2(String celular2) {
		this.celular2 = celular2;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getCEP() {
		return CEP;
	}

	public void setCEP(String cEP) {
		CEP = cEP;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public UnidadeFederativa getUnidadeFederativa() {
		return unidadeFederativa;
	}

	public void setUnidadeFederativa(UnidadeFederativa unidadeFederativa) {
		this.unidadeFederativa = unidadeFederativa;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public Situacao getSituacao() {
		return situacao;
	}

	public void setSituacao(Situacao situacao) {
		this.situacao = situacao;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCelular3() {
		return celular3;
	}

	public void setCelular3(String celular3) {
		this.celular3 = celular3;
	}
	
	
}
