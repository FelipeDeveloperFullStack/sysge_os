package br.com.sysge.controller.global;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

import br.com.sysge.model.global.Agenda;
import br.com.sysge.service.global.AgendaService;
import br.com.sysge.util.FacesUtil;
import br.com.sysge.util.RequestContextUtil;


@ViewScoped
@ManagedBean
public class AgendaController implements Serializable{

	private static final long serialVersionUID = -6536627098015550705L;
	
	private ScheduleModel agendas;
	
	private ScheduleEvent event;
	
	private Agenda agenda;
	
	private AgendaService agendaService = new AgendaService();

	public ScheduleModel getAgendas() {
		return agendas;
	}

	public void setAgendas(ScheduleModel agendas) {
		this.agendas = agendas;
	}
	
	@PostConstruct
	public void listarAgenda(){
		this.agendas = new DefaultScheduleModel();
		this.agenda = new Agenda();
		for(Agenda agenda : agendaService.findAll()){
			this.agendas.addEvent(new DefaultScheduleEvent(agenda.getDescricao(), 
					subtrairUmDia(agenda.getDataInicial()), 
					subtrairUmDia(agenda.getDataFinal()), agenda.getId()));
		}
	}
	
	public void novo(SelectEvent event){
		this.agenda = new Agenda();
		this.agenda.setDataInicial(adicionarUmDia((Date) event.getObject()));
		this.agenda.setDataFinal(adicionarUmDia((Date) event.getObject()));
	}
	
	public void setarAgenda(SelectEvent event){
		this.event = (ScheduleEvent) event.getObject();
		this.agenda = agendaService.findById((Long) this.event.getData());
	}
	
	private Date adicionarUmDia(Date data){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		return calendar.getTime();
	}
	
	private Date subtrairUmDia(Date data){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(data);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		return calendar.getTime();
	}
	
	public void salvar(){
		try {
			agendaService.salvar(agenda);
			listarAgenda();
			FacesUtil.mensagemInfo("Agenda adicionada com sucesso!");
			RequestContextUtil.execute("PF('agenda').hide();");
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}

	public Agenda getAgenda() {
		return agenda;
	}

	public void setAgenda(Agenda agenda) {
		this.agenda = agenda;
	}
	

}
