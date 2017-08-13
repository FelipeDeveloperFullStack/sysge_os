package br.com.sysge.service.sys;


import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.sys.UserTheme;

public class UserThemeService extends GenericDaoImpl<UserTheme, Long>{

	private static final long serialVersionUID = 7055998580113643610L;
	
	public String getTheme(String theme){
		try {
			for(UserTheme userTheme : super.findAll()){
				if(userTheme.equals(theme)){
					return userTheme.getTheme();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return theme;
	}
	
	public boolean verificarSeExisteTemaBaseDados(String theme){
		try {
			for(UserTheme userTheme : super.findAll()){
				if(userTheme.equals(theme)){
					return true;
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
		return false;
	}

}
