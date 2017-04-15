package br.com.sysge.service.gestserv;

import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.infraestrutura.decimal.ConverteNumeroExtensoReal;
import br.com.sysge.infraestrutura.relatorios.ReportFactory;
import br.com.sysge.infraestrutura.relatorios.TiposRelatorio;
import br.com.sysge.model.conf.Parametro;
import br.com.sysge.model.financ.ParcelasPagamentoOs;
import br.com.sysge.model.gestserv.OrdemServico;
import br.com.sysge.model.gestserv.ProdutoOrdemServico;
import br.com.sysge.model.gestserv.ServicoOrdemServico;
import br.com.sysge.model.global.UnidadeEmpresarial;
import br.com.sysge.relatorios.to.PagamentoTO;
import br.com.sysge.relatorios.to.ProdutoTO;
import br.com.sysge.relatorios.to.ServicoTO;
import br.com.sysge.service.conf.ParametroService;
import br.com.sysge.service.estoque.ProdutoService;
import br.com.sysge.service.financ.ParcelasPagamentoOsService;
import br.com.sysge.service.global.ClienteService;
import net.sf.jasperreports.engine.JRException;

public class OrdemServicoService extends GenericDaoImpl<OrdemServico, Long> {

	private static final long serialVersionUID = 6697038638256448464L;
	
	private ConverteNumeroExtensoReal converteNumeroExtensoReal;

	@Inject
	private ServicoOrdemServicoService servicoOrdemServicoService;
	
	@Inject
	private ProdutoOrdemServicoService produtoOrdemServicoService;
	
	@Inject
	private ServicoService servicoService;

	@Inject
	private ProdutoService produtoService;
	
	@Inject
	private ParcelasPagamentoOsService parcelasPagamentoService;
	
	@Inject
	private ClienteService clienteService;
	
	@Inject
	private ParametroService parametroService;
	
	private static String NUMERO_RECIDO = "numero_recibo";
	private static String VALOR_OS = "valor_os";
	private static String VALOR_PARCELA = "valor_parcela";
	private static String RAZAO_SOCIAL_UNIDADE_EMPRESARIAL = "razao_social_unidade_empresarial";
	private static String TELEFONE_UNIDADE_EMPRESARIAL = "telefone_unidade_empresarial";
	private static String NOME_CLIENTE = "nome_cliente";
	private static String DOCUMENTO = "documento";
	private static String NUMERO_PARCELA = "numero_parcela";
	private static String NUMERO_OS = "numero_os";
	private static String NUMERO_EXTENSO = "numero_extenso";
	private static String DATA_EMISSAO = "data_emissao";
	
	private static String CLIENTE = "cliente";
	private static String ENDERECO = "endereco";
	private static String CIDADE = "cidade";
	private static String CPF_CNPJ = "cpf_cnpj";
	private static String TELEFONE = "telefone";
	private static String BAIRRO = "bairro";
	private static String CEP = "cep";
	private static String UF = "uf";
	private static String MARCA = "marca";
	private static String MODELO = "modelo";
	private static String ACESSORIOS = "acessorios";
	private static String SINTOMAS = "sintomas";
	private static String NUMERO_SERIE = "numero_serie";
	private static String NUMERO_PATRIMONIO = "numero_patrimonio";
	private static String DATA_ENTRADA = "data_entrada";
	private static String OBSERVACOES = "observacoes";
	
	private SimpleDateFormat sdf;
	
	public OrdemServico salvar(OrdemServico ordemServico) {
		try {
			if (ordemServico.getFuncionario().getNome().isEmpty()) {
				throw new RuntimeException("O nome do funcionário é obrigatório!");
			}
			if (ordemServico.getCliente().getNomeTemporario().isEmpty()) {
				throw new RuntimeException("O nome do cliente é obrigatório!");
			}
			return super.save(ordemServico);
		} catch (RuntimeException e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	public void consistirServico(List<ServicoOrdemServico> listaServicos, OrdemServico ordemServico) {
		if (!listaServicos.isEmpty()) {
			for (ServicoOrdemServico sos : listaServicos) {
				sos.setOrdemServico(ordemServico);
				sos.setServico(servicoService.salvar(sos.getServico()));
				servicoOrdemServicoService.save(sos);
			}
		}
	
	}
	
	public void consistirProduto(List<ProdutoOrdemServico> listaProdutos, OrdemServico ordemServico){
		if (!listaProdutos.isEmpty()) {
			for (ProdutoOrdemServico pos : listaProdutos) {
				pos.setOrdemServico(ordemServico);
				pos.setProduto(produtoService.salvar(pos.getProduto()));
				produtoOrdemServicoService.save(pos);
			}
		}
	}
	
	public List<ServicoOrdemServico> procurarServicosOS(long idOS){
		return servicoOrdemServicoService.findByListProperty(idOS, "ordemServico.id");
	}
	
	public List<ProdutoOrdemServico> procurarProdutosOS(long idOS){
		return produtoOrdemServicoService.findByListProperty(idOS, "ordemServico.id");
	}
	
	public List<ParcelasPagamentoOs> procurarPagamentoOS(long idOS){
		return parcelasPagamentoService.findByListProperty(idOS, "ordemServico.id");
	}
	
	public List<Parametro> listarParametros(long id){
		return parametroService.findByListProperty(id, "unidadeEmpresarialPadrao.id");
	}
	
	public List<OrdemServico> pesquisarPorNumeroEStatusOS(OrdemServico ordemServico){
		List<OrdemServico> listaOS = new ArrayList<OrdemServico>();
		if(ordemServico.getId() == null){
			listaOS = super.findByStatusOs(ordemServico.getStatusOS());
		}else{
			listaOS = super.findByNumeroStatusOS(ordemServico.getId(), ordemServico.getStatusOS());
		}
		if(listaOS.isEmpty()){
			if(ordemServico.getId() == null){
			throw new RuntimeException("Nenhuma ordem de servico de status '"+ordemServico.getStatusOS().getStatusOS()+"' encontrada, "
						+ "verifique e tente novamente!");
			}else{
				throw new RuntimeException("Nenhuma ordem de servico de nº "
						+ ""+ordemServico.getId()+ " "
						+ "e status '"+ordemServico.getStatusOS().getStatusOS()+"' encontrada, "
						+ "verifique e tente novamente!");
			}
		}
		return listaOS;
	}
	
	public void gerarComprovantePagamento(ParcelasPagamentoOs parcela) throws FileNotFoundException{
		
		converteNumeroExtensoReal = new ConverteNumeroExtensoReal();
		
		 HashMap<String, Object> params = new HashMap<String, Object>();
		 Calendar c = Calendar.getInstance();
		 DecimalFormat df = new DecimalFormat("###,###0.00");
		 sdf = new SimpleDateFormat("dd/MM/yyyy");
		 
		 parcela.getOrdemServico().setCliente(clienteService.verificarTipoPessoa(parcela.getOrdemServico().getCliente()));
		 
		 params.put(DATA_EMISSAO, String.valueOf(sdf.format(new Date())));
	     params.put(NUMERO_RECIDO, String.valueOf(c.get(Calendar.YEAR)) + String.valueOf(parcela.getOrdemServico().getId()) + String.valueOf(parcela.getNumero()));
	     params.put(VALOR_OS, df.format(parcela.getOrdemServico().getTotal()));
	     params.put(VALOR_PARCELA, df.format(parcela.getValorCobrado()));
	     params.put(NOME_CLIENTE, parcela.getOrdemServico().getCliente().getNomeTemporario());
	     params.put(DOCUMENTO, String.valueOf((parcela.getOrdemServico().getCliente().getCpf() == null ? "" : parcela.getOrdemServico().getCliente().getCpf()) 
	    		 		       + "" + (parcela.getOrdemServico().getCliente().getCnpj() == null ? "" : parcela.getOrdemServico().getCliente().getCnpj())));
	     params.put(NUMERO_PARCELA, String.valueOf(parcela.getNumero()+ "º"));
	     params.put(NUMERO_OS, String.valueOf(parcela.getOrdemServico().getId()));
	     params.put(NUMERO_EXTENSO, converteNumeroExtensoReal.converterNumeroParaExtensoReal(parcela.getValorCobrado()));
	     
	     	//Unidade Empresarial
			for(Parametro p : parametroService.findAll()){
				UnidadeEmpresarial u = p.getUnidadeEmpresarialPadrao();
				 params.put(RAZAO_SOCIAL_UNIDADE_EMPRESARIAL, u.getNomeFantasia());
			     params.put(TELEFONE_UNIDADE_EMPRESARIAL, u.getTelefone());
			}
	     
	     
	     ReportFactory reportFactory = new ReportFactory("r_comprovante_pagamento.jasper", params, TiposRelatorio.PDF);
	     reportFactory.getReportStream();
	}
	
	public void gerarNotaRecebimento(OrdemServico ordemServico){
		Map<String, Object> params = new HashMap<String, Object>();
		
		ordemServico.setCliente(clienteService.verificarTipoPessoa(ordemServico.getCliente()));
		
		params.put(CLIENTE, ordemServico.getCliente().getNomeTemporario());
		params.put(ENDERECO, ordemServico.getCliente().getLogradouro());
		params.put(CIDADE, ordemServico.getCliente().getCidade());
		params.put(CPF_CNPJ, (ordemServico.getCliente().getCpf() == null ? "" : ordemServico.getCliente().getCpf())
				   +""+(ordemServico.getCliente().getCnpj() == null ? "" : ordemServico.getCliente().getCnpj()));
		params.put(TELEFONE, ordemServico.getCliente().getTelefone());
		params.put(BAIRRO, ordemServico.getCliente().getBairro());
		params.put(CEP, ordemServico.getCliente().getCep());
		params.put(UF, String.valueOf(ordemServico.getCliente().getUnidadeFederativa()));
		
		params.put(MARCA, ordemServico.getMarca());
		params.put(MODELO, ordemServico.getModelo());
		params.put(ACESSORIOS, ordemServico.getAcessorios());
		params.put(SINTOMAS, ordemServico.getDefeito());
		params.put(OBSERVACOES, ordemServico.getObservacoes());
		params.put(NUMERO_SERIE, String.valueOf(ordemServico.getNumeroSerie()));
		params.put(NUMERO_PATRIMONIO, String.valueOf(ordemServico.getNumeroPatrimonio()));
		
		params.put(NUMERO_OS, String.valueOf(ordemServico.getId()));
		params.put(DATA_ENTRADA, ordemServico.getDataEntrada());
		
				//Unidade Empresarial
				for(Parametro p : parametroService.findAll()){
					UnidadeEmpresarial u = p.getUnidadeEmpresarialPadrao();
					params.put("razao_social_u", u.getRazaoSocial());
					params.put("endereco_u", u.getLogradouro() + " " + u.getComplemento());
					params.put("cidade_u", u.getCidade());
					params.put("bairro_u", u.getBairro());
					params.put("cep_u", u.getCEP());
					params.put("telefones_u", u.getTelefone() + " | "+u.getCelular());
					params.put("nome_fantasia_u", u.getNomeFantasia());
				}
		
		ReportFactory reportFactory = new ReportFactory("r_nota_recebimento.jasper", params, TiposRelatorio.PDF);
		reportFactory.getReportStream();
	}
	
	public void gerarOrdemServico(OrdemServico ordemServico, 
								  List<ServicoOrdemServico> servicos, 
								  List<ProdutoOrdemServico> produtos,
								  List<ParcelasPagamentoOs> pagamentos) throws JRException{
		Map<String, Object> params = new HashMap<String, Object>();
		sdf = new SimpleDateFormat("dd/MM/yyyy - hh:mm");
		ordemServico.setCliente(clienteService.verificarTipoPessoa(ordemServico.getCliente()));
		
		params.put("list_servicos", setarServicoTo(servicos));
		params.put("list_produtos", setarProdutoTo(produtos));
		params.put("list_pagamentos", setarPagamentoTo(pagamentos));
		params.put("subTotalServico", ordemServico.getTotalServico());
		params.put("subTotalProduto", ordemServico.getTotalProduto());
		params.put("totalOS", ordemServico.getTotal());
		params.put(NUMERO_OS, String.valueOf(ordemServico.getId()));
		params.put("data_hora", sdf.format(ordemServico.getDataEntrada()));
		params.put("data_hora_saida", verificarDataSaida(ordemServico.getDataSaida(), sdf));
		params.put("statusOS", ordemServico.getStatusOS().getStatusOS());
		params.put("nomeCliente", ordemServico.getCliente().getNomeTemporario());
		params.put("CPF_CNPJ", clienteService.getTipoDocumentoPessoa(ordemServico.getCliente()));
		params.put("telefone", ordemServico.getCliente().getTelefone());
		params.put("celular", ordemServico.getCliente().getCelular());
		params.put("email", ordemServico.getCliente().getEmail());
		params.put(ENDERECO, ordemServico.getCliente().getLogradouro());
		params.put(BAIRRO, ordemServico.getCliente().getBairro());
		params.put("cidade", ordemServico.getCliente().getCidade());
		
		params.put(MARCA, ordemServico.getMarca());
		params.put(MODELO, ordemServico.getModelo());
		params.put(ACESSORIOS, ordemServico.getAcessorios());
		params.put(SINTOMAS, ordemServico.getDefeito());
		params.put(NUMERO_SERIE, String.valueOf(ordemServico.getNumeroSerie()));
		params.put(NUMERO_PATRIMONIO, String.valueOf(ordemServico.getNumeroPatrimonio()));
		
		//Unidade Empresarial
		for(Parametro p : parametroService.findAll()){
			UnidadeEmpresarial u = p.getUnidadeEmpresarialPadrao();
			params.put("razao_social_u", u.getRazaoSocial());
			params.put("endereco_u", u.getLogradouro() + " " + u.getComplemento());
			params.put("cidade_u", u.getCidade());
			params.put("bairro_u", u.getBairro());
			params.put("cep_u", u.getCEP());
			params.put("telefones_u", u.getTelefone() + " | "+u.getCelular());
		}
		
		ReportFactory reportFactory = new ReportFactory("r_ordem_servico.jasper", params, TiposRelatorio.PDF);
		reportFactory.getReportStream();
	}
	
	private String verificarDataSaida(Date dataSaida, SimpleDateFormat sdf){
		if(dataSaida == null){
			return "";
		}else{
			return sdf.format(dataSaida);
		}
	}
	
	private List<ServicoTO> setarServicoTo(List<ServicoOrdemServico> servicos){
		List<ServicoTO> tos = new ArrayList<ServicoTO>();
		if(servicos.isEmpty()){
			ServicoTO to = new ServicoTO();
			to.setNome("Nenhum servico informado!");
			to.setSubTotal(null);
			tos.add(to);
			return tos;
		}else{
			for(ServicoOrdemServico s : servicos){
				ServicoTO to = new ServicoTO();
				to.setNome(s.getServico().getNome());
				to.setSubTotal(s.getSubTotal());
				tos.add(to);
			}
			return tos;
		}
	}
	private List<ProdutoTO> setarProdutoTo(List<ProdutoOrdemServico> produtos){
		List<ProdutoTO> tos = new ArrayList<ProdutoTO>();
		if(produtos.isEmpty()){
			ProdutoTO to = new ProdutoTO();
			to.setDescricaoProduto("Nenhum produto informado!");
			to.setQuantidade(null);
			to.setValor(null);
			to.setSubTotal(null);
			tos.add(to);
			return tos;
		}else{
			for(ProdutoOrdemServico p : produtos){
				ProdutoTO to = new ProdutoTO();
				to.setDescricaoProduto(p.getProduto().getDescricaoProduto());
				to.setQuantidade(p.getQuantidade());
				to.setValor(p.getValor());
				to.setSubTotal(p.getSubTotal());
				tos.add(to);
			}
			return tos;
		}
		
	}
	private List<PagamentoTO> setarPagamentoTo(List<ParcelasPagamentoOs> parcelas){
		List<PagamentoTO> tos = new ArrayList<PagamentoTO>();
		for(ParcelasPagamentoOs p : parcelas){
			PagamentoTO to = new PagamentoTO();
			p.getOrdemServico().setCliente(clienteService.verificarTipoPessoa(p.getOrdemServico().getCliente()));
			
			to.setAtendente(p.getOrdemServico().getFuncionario().getNome());
			to.setDesconto(p.getValorDesconto());
			to.setFormaPagamento(p.getOrdemServico().getFormaPagamento().getFormaPagamento());
			to.setNumero(String.valueOf(p.getNumero()));
			to.setParcela(p.getValorParcela());
			to.setParcelamento(p.getQuantidadeParcelas());
			to.setValorCobrado(p.getValorCobrado());
			to.setVencimento(p.getDataVencimento());
			to.setCliente(p.getOrdemServico().getCliente().getNomeTemporario());
			
			//Unidade Empresarial
			for(Parametro pm : parametroService.findAll()){
				UnidadeEmpresarial u = pm.getUnidadeEmpresarialPadrao();
				to.setNomeFantasiaUnidadeEmpresarial(u.getNomeFantasia());
			}
			tos.add(to);
		}
		return tos;
	}
	
}
