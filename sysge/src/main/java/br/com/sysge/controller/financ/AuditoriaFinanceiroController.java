package br.com.sysge.controller.financ;

import java.io.Serializable;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import br.com.sysge.model.financ.AuditoriaFinanceiro;
import br.com.sysge.service.financ.AuditoriaFinanceiroService;
import br.com.sysge.util.RequestContextUtil;

@Named
@ViewScoped
public class AuditoriaFinanceiroController implements Serializable{

	private static final long serialVersionUID = 4911110666455939838L;
	
	@Inject
	private AuditoriaFinanceiroService auditoriaFinanceiroService;
	
	private List<AuditoriaFinanceiro> auditorias;
	
	private AuditoriaFinanceiro auditoriaFinanceiro;
	
	public void setarAuditoria(AuditoriaFinanceiro auditoriaFinanceiro){
		this.auditoriaFinanceiro = auditoriaFinanceiro;
		RequestContextUtil.execute("PF('detalhe_auditoria').show();");
	}
	
	public List<AuditoriaFinanceiro> getAuditorias(){
		return this.auditorias;
	}
	
	public void listar(){
		this.auditorias = auditoriaFinanceiroService.findAll();
	}

	public void setAuditorias(List<AuditoriaFinanceiro> auditorias) {
		this.auditorias = auditorias;
	}

	public AuditoriaFinanceiro getAuditoriaFinanceiro() {
		return auditoriaFinanceiro;
	}

	public void setAuditoriaFinanceiro(AuditoriaFinanceiro auditoriaFinanceiro) {
		this.auditoriaFinanceiro = auditoriaFinanceiro;
	}
	
	

}
