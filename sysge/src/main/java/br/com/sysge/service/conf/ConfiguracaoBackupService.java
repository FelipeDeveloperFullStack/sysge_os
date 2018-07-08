package br.com.sysge.service.conf;

import java.util.List;

import org.quartz.SchedulerException;

import br.com.sysge.infraestrutura.agendador.Agendador;
import br.com.sysge.infraestrutura.agendador.AgendadorFactory;
import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.conf.ConfiguracaoBackup;

public class ConfiguracaoBackupService extends GenericDaoImpl<ConfiguracaoBackup, Long>{

	private static final long serialVersionUID = 4360526737828013305L;
	
	public void salvar(ConfiguracaoBackup configuracaoBackup) throws SchedulerException{
		List<ConfiguracaoBackup> configuracaoBackups = super.findAll();
		if(configuracaoBackups.isEmpty()){
			super.save(configuracaoBackup);
		}else{
			if(configuracaoBackups.size() == 1){
				for(ConfiguracaoBackup c : configuracaoBackups){
					c.setAutomatico(configuracaoBackup.isAutomatico());
					c.setCaminhoArquivoMysqlDump(configuracaoBackup.getCaminhoArquivoMysqlDump());
					c.setDiretorio(configuracaoBackup.getDiretorio());
					c.setEmail(configuracaoBackup.getEmail());
					c.setResponsavel(configuracaoBackup.getResponsavel());
					c.setTempoBackupAutomatico(configuracaoBackup.getTempoBackupAutomatico());
					super.save(c);
				}
				
				realizarBackupAutomatico();
				
			}
		}
	}
	
	private void realizarBackupAutomatico() throws SchedulerException{
		Agendador agendador = new AgendadorFactory();
		try {
			agendador.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw new SchedulerException(e);
		}
	}

}
