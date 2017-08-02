package br.com.sysge.controller.gestserv;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Named;

import br.com.sysge.infraestrutura.email.Email;

@Named
@ViewScoped
public class EmailOSController implements Serializable{

	private static final long serialVersionUID = 7411624959358133535L;
	
	private Email email;

}
