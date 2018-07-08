package br.com.sysge.infraestrutura.agendador;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.Calendar;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import br.com.sysge.infraestrutura.email.ConfigHotmail;
import br.com.sysge.infraestrutura.googledrive.GoogleDrive;
import br.com.sysge.model.conf.ConfiguracaoBackup;
import br.com.sysge.service.conf.BackupService;
import br.com.sysge.util.DateUtil;

/**
 * @author Felipe M. Santos
 */

public class AgendadorFactory implements Agendador {

	// http://www.quartz-scheduler.org/documentation/quartz-2.2.x/tutorials/

	private BackupService backupService;
	private ConfigHotmail configHotmail;
	
	private static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
	private static final String FILE_NAME = BackupService.getNomeArquivo(DateUtil.dateFormat());
	
	public AgendadorFactory(){
		
		this.backupService = new BackupService();
		this.configHotmail = new ConfigHotmail();
		
	}
	
	@Override
	public void start() {
		try {
			Scheduler scheduler = new StdSchedulerFactory().getScheduler();

			JobDetail job1 = JobBuilder.newJob(AgendadorFactory.class).withIdentity("job1", "group1").build();

			Trigger trigger1 = TriggerBuilder.newTrigger().withIdentity("trigger1", "group1")
					.withSchedule(CronScheduleBuilder.cronSchedule(Agendador.DAILY)).build();

			scheduler.scheduleJob(job1, trigger1);
			scheduler.start();

		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			for (ConfiguracaoBackup c : this.backupService.configuracaoBackupList()) {
				if(c.isAutomatico()){
					this.backupService.fazerBackup();
					this.configHotmail.enviarEmailSimples("sysgeweb@hotmail.com"
							, c.getEmail()
							, "Backup automático realizado pelo sistema Sysge OS"
							, "Ola, <br> O sistema Sysge OS realizou um backup automático.</br> "
								 + "<br> Data da realização: "+DateUtil.dateToString(LocalDate.now())+" </br>"
								 		+ "<br></br>"
								 		+ "<br> Atenciosamente </br>"
								 		+ "<br> Sysge WEB </br> ");
					
					GoogleDrive.addFileCloud(FILE_NAME 
							, "Backup Sysge "+YEAR
							, "Pasta de backup do sistema Sysge "+YEAR
							, c.getDiretorio() + FILE_NAME);
				}
			}
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}
		
	}

}
