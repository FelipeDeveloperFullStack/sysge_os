package br.com.sysge.service.sys;

import java.util.ArrayList;
import java.util.List;

public class LicenceManagerService {

	public static void main(String[] args) {
		String cnpj = "37.640.083/0001-93";
		String cnpjNumeros = cnpj.replace(".", "").replace("/", "").replace("-", "").trim();

		List<String> listNumber = new ArrayList<String>();
		List<String> listSerialNumber = new ArrayList<String>();
		for (int i = 0; i < cnpjNumeros.length(); i++) {
			listNumber.add(cnpjNumeros.substring(i, i + 1));
		}

		for (int i = 0; i < listNumber.size(); i++) {
			int n = Integer.valueOf(listNumber.get(i));
			listSerialNumber.add(String.valueOf(n * (i+1)));
		}

		System.out.println(listNumber);
		System.out.println(listSerialNumber);

	}

}
