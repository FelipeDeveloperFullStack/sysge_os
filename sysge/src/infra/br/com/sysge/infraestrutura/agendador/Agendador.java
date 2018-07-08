package br.com.sysge.infraestrutura.agendador;

import org.quartz.Job;

/**
 * @author Felipe M. Santos
 */

import org.quartz.SchedulerException;

// If you need of the HOURLY, WEEKLY, MONTHLY and YEARLY see http://www.cronmaker.com/

public interface Agendador extends Job{

    String DAILY = "0 0/30 * 1/1 * ? *";

	void start() throws SchedulerException;
	
	
	
}
