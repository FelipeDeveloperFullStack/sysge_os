package br.com.sysge.controller.global;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sysge.model.global.UnidadeEmpresarial;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.model.type.UnidadeFederativa;
import br.com.sysge.service.global.UnidadeEmpresarialService;
import br.com.sysge.service.sys.WebServiceCEPService;
import br.com.sysge.util.FacesUtil;
import br.com.sysge.util.RequestContextUtil;

@Named
@ViewScoped
public class UnidadeEmpresarialController implements Serializable{

	private static final long serialVersionUID = 4377896072467651845L;

	private UnidadeEmpresarial unidadeEmpresarial;
	
	private List<UnidadeEmpresarial> unidadeEmpresariais;
	
	@Inject
	private UnidadeEmpresarialService unidadeEmpresarialService;
	
	@PostConstruct
	public void init(){
		novaListaUnidadeEmpresarial();
		novo();
	}
	
	public void novaListaUnidadeEmpresarial(){
		this.unidadeEmpresariais = new ArrayList<UnidadeEmpresarial>();
	}
	
	public void novo(){
		this.unidadeEmpresarial = new UnidadeEmpresarial();
	}
	
	public void pesquisar(){
		novaListaUnidadeEmpresarial();
		this.unidadeEmpresariais = unidadeEmpresarialService.pesquisarUnidadeEmpresarial(unidadeEmpresarial);
	}
	
	public void cancelar(){
		novaListaUnidadeEmpresarial();
	}
	
	public void salvar(){
		try {
			unidadeEmpresarialService.salvar(unidadeEmpresarial);
			fecharDialogs();
			FacesUtil.mensagemInfo("Unidade Empresarial salva com sucesso!");
			novaListaUnidadeEmpresarial();
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void procurarCep(){
		try {
			Map<Object, Object> mapCep = new HashMap<Object, Object>();
			mapCep.putAll(WebServiceCEPService.procurarCEP(unidadeEmpresarial.getCEP()));
			this.unidadeEmpresarial.setLogradouro(mapCep.get(5).toString() + " " + mapCep.get(1).toString());
			this.unidadeEmpresarial.setCidade(mapCep.get(2).toString());
			this.unidadeEmpresarial.setUnidadeFederativa(UnidadeFederativa.valueOf(mapCep.get(3).toString()));
			this.unidadeEmpresarial.setBairro(mapCep.get(4).toString());
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void consultarCnpj(){
		try {
			unidadeEmpresarial = unidadeEmpresarialService.consultarCnpj(unidadeEmpresarial);
			FacesUtil.mensagemInfo("Dados encontrados com sucesso!");
			fecharDialodDeProcurarCnpj();
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	private void fecharDialodDeProcurarCnpj() {
		RequestContextUtil.execute("PF('dialog_procurar_cnpj').hide();");
		
	}

	public void setarUnidadeEmpresarial(UnidadeEmpresarial unidadeEmpresarial){
		this.unidadeEmpresarial = unidadeEmpresarial;
	}
	
	private void fecharDialogs(){
		RequestContextUtil.execute("PF('dialogNovoUnidadeEmpresarial').hide();");
		RequestContextUtil.execute("PF('dialogEditarUnidadeEmpresarial').hide();");
	}
	
	public UnidadeFederativa[] getUnidadesFederativa(){
		return UnidadeFederativa.values();
	}
	
	public Situacao[] getSituacoes(){
		return Situacao.values();
	}

	public UnidadeEmpresarial getUnidadeEmpresarial() {
		return unidadeEmpresarial == null ? new UnidadeEmpresarial() : unidadeEmpresarial;
	}

	public void setUnidadeEmpresarial(UnidadeEmpresarial unidadeEmpresarial) {
		this.unidadeEmpresarial = unidadeEmpresarial;
	}

	public List<UnidadeEmpresarial> getUnidadeEmpresariais() {
		return unidadeEmpresariais;
	}

	public void setUnidadeEmpresariais(List<UnidadeEmpresarial> unidadeEmpresariais) {
		this.unidadeEmpresariais = unidadeEmpresariais;
	}
	
	
	
}
