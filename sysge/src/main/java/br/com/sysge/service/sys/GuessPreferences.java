package br.com.sysge.service.sys;

import java.io.Serializable;
import java.util.Map;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

@Named
@SessionScoped
public class GuessPreferences implements Serializable{

	private static final long serialVersionUID = 2330138785773694068L;

	//private String theme = "hot-sneaks";
	private String theme = "redmond";
	
	public String getTheme(){
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if(params.containsKey("theme")){
			theme = params.get("theme");
		}
		return theme;
	}
	
	public void setTheme(String theme){
		this.theme = theme;
	}
	
}
