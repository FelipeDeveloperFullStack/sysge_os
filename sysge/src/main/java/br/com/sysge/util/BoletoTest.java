package br.com.sysge.util;

import br.com.caelum.stella.boleto.Banco;
import br.com.caelum.stella.boleto.Beneficiario;
import br.com.caelum.stella.boleto.Boleto;
import br.com.caelum.stella.boleto.Datas;
import br.com.caelum.stella.boleto.Endereco;
import br.com.caelum.stella.boleto.Pagador;
import br.com.caelum.stella.boleto.bancos.Bradesco;
import br.com.caelum.stella.boleto.transformer.GeradorDeBoleto;

public class BoletoTest {

	  public static void main(String[] args) { 
		  
	        Datas datas = Datas.novasDatas()
	                .comDocumento(1, 5, 2008)
	                .comProcessamento(1, 5, 2008)
	                .comVencimento(2, 5, 2008);  

	        Endereco enderecoBeneficiario = Endereco.novoEndereco()
	        		.comLogradouro("Av das Empresas, 555")  
	        		.comBairro("Bairro Grande")  
	        		.comCep("01234-555")  
	        		.comCidade("São Paulo")  
	        		.comUf("SP");  

	        //Quem emite o boleto
	        Beneficiario beneficiario = Beneficiario.novoBeneficiario()  
	                .comNomeBeneficiario("Fulano de Tal")  
	                .comAgencia("1824").comDigitoAgencia("4")  
	                .comCodigoBeneficiario("76000")  
	                .comDigitoCodigoBeneficiario("5")  
	                .comNumeroConvenio("1207113")  
	                .comCarteira("18")  
	                .comEndereco(enderecoBeneficiario)
	                .comNossoNumero("9000206");  

	        Endereco enderecoPagador = Endereco.novoEndereco()
	        		.comLogradouro("Av dos testes, 111 apto 333")  
	        		.comBairro("Bairro Teste")  
	        		.comCep("01234-111")  
	        		.comCidade("São Paulo")  
	        		.comUf("SP");  
	        
	        //Quem paga o boleto
	        Pagador pagador = Pagador.novoPagador()  
	                .comNome("Fulano da Silva")  
	                .comDocumento("111.222.333-12")
	                .comEndereco(enderecoPagador);

	        Banco banco = new Bradesco();

	        Boleto boleto = Boleto.novoBoleto()  
	                .comBanco(banco)  
	                .comDatas(datas)  
	                .comBeneficiario(beneficiario)  
	                .comPagador(pagador)  
	                .comValorBoleto("200.00")  
	                .comNumeroDoDocumento("1234")  
	                .comInstrucoes("instrucao 1", "instrucao 2", "instrucao 3", "instrucao 4", "instrucao 5")  
	                .comLocaisDePagamento("local 1", "local 2");  

	        GeradorDeBoleto gerador = new GeradorDeBoleto(boleto);  

	        // Para gerar um boleto em PDF  
	        gerador.geraPDF("C:\\SYSGE_WEB\\Bradesco.pdf");  

	        // Para gerar um boleto em PNG  
	        //gerador.geraPNG("C:\\SYSGE_WEB\\Bradesco.png");  

	        // Para gerar um array de bytes a partir de um PDF  
	        //byte[] bPDF = gerador.geraPDF();  

	        // Para gerar um array de bytes a partir de um PNG  
	        //byte[] bPNG = gerador.geraPNG();
	    }  
}
