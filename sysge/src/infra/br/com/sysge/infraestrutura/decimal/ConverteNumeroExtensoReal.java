package br.com.sysge.infraestrutura.decimal;


import java.math.BigDecimal;

import br.com.caelum.stella.inwords.FormatoDeReal;
import br.com.caelum.stella.inwords.NumericToWordsConverter;

/**
 * https://github.com/caelum/caelum-stella/wiki/Numeros-por-extenso-core
 * @author Felipe
 *
 */
public class ConverteNumeroExtensoReal {

	public String converterNumeroParaExtensoReal(BigDecimal numero){
		NumericToWordsConverter converter = new NumericToWordsConverter(new FormatoDeReal());  
		return converter.toWords(numero.doubleValue());  
	}
}
