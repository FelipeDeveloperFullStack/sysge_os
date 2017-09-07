package br.com.sysge.controller.financ;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;



import br.com.sysge.model.financ.AuditoriaFinanceiro;
import br.com.sysge.service.financ.AuditoriaFinanceiroService;
import br.com.sysge.util.RequestContextUtil;

@ViewScoped
@ManagedBean
public class AuditoriaFinanceiroController implements Serializable{

	private static final long serialVersionUID = 4911110666455939838L;
	
	private AuditoriaFinanceiroService auditoriaFinanceiroService = new AuditoriaFinanceiroService();
	
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
