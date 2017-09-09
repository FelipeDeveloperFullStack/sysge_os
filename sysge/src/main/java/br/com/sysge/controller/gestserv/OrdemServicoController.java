package br.com.sysge.controller.gestserv;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;



import org.primefaces.event.SelectEvent;

import br.com.sysge.controller.sys.TemplateViewPage;
import br.com.sysge.model.estoque.Produto;
import br.com.sysge.model.financ.CondicaoPagamento;
import br.com.sysge.model.financ.ParcelasPagamentoOs;
import br.com.sysge.model.financ.type.StatusFinanceiro;
import br.com.sysge.model.gestserv.OrdemServico;
import br.com.sysge.model.gestserv.ProdutoOrdemServico;
import br.com.sysge.model.gestserv.Servico;
import br.com.sysge.model.gestserv.ServicoOrdemServico;
import br.com.sysge.model.global.Cliente;
import br.com.sysge.model.rh.Funcionario;
import br.com.sysge.model.type.FormaPagamento;
import br.com.sysge.model.type.Garantia;
import br.com.sysge.model.type.Situacao;
import br.com.sysge.model.type.StatusOS;
import br.com.sysge.model.type.TipoDesconto;
import br.com.sysge.service.conf.ParametroService;
import br.com.sysge.service.estoque.ProdutoService;
import br.com.sysge.service.financ.CondicaoPagamentoService;
import br.com.sysge.service.financ.MovimentoFinanceiroService;
import br.com.sysge.service.financ.ParcelasPagamentoOsService;
import br.com.sysge.service.gestserv.OrdemServicoService;
import br.com.sysge.service.gestserv.ProdutoOrdemServicoService;
import br.com.sysge.service.gestserv.ServicoOrdemServicoService;
import br.com.sysge.service.global.ClienteService;
import br.com.sysge.service.rh.FuncionarioService;
import br.com.sysge.util.FacesUtil;
import br.com.sysge.util.RequestContextUtil;


@ViewScoped
@ManagedBean
public class OrdemServicoController implements Serializable {

	private static final long serialVersionUID = 1267523434219231347L;
	
	private int currentTab = 0;
	
	private ProdutoOrdemServico produtoOrdemServico;

	private OrdemServico ordemServico;
	
	private BigDecimal quantidadeAdicionada = BigDecimal.ZERO;
	
	private ParcelasPagamentoOs parcelasPagamentoOs;
	
	private ParcelasPagamentoOs parcela;
	
	private Servico servico;
	
	private Produto produto;
	
	private String pesquisaCliente = "";
	
	private List<OrdemServico> ordensServicos;
	
	private List<ParcelasPagamentoOs> parcelas;
	
	@SuppressWarnings("unused")
	private List<CondicaoPagamento> condicoesPagamento;
	
	@SuppressWarnings("unused")
	private List<OrdemServico> ordensServicosAbertas;

	private List<Servico> servicos;

	private List<Produto> produtos;

	@SuppressWarnings("unused")
	private List<Funcionario> funcionarios;

	private List<Cliente> clientes;
	
	private List<ServicoOrdemServico> listaServicos;
	
	private List<ProdutoOrdemServico> listaProdutos;
	
	private List<ProdutoOrdemServico> listaProdutosTemporário;
	
	
	private FuncionarioService funcionarioService;
	
	
	private ClienteService clienteService;
	
	
	private TemplateViewPage templateViewPage;
	
	
	private OrdemServicoService ordemServicoService;
	
	
	private ServicoOrdemServicoService servicoOrdemServicoService;
	
	
	private ProdutoOrdemServicoService produtoOrdemServicoService;
	
	
	private ParcelasPagamentoOsService parcelasPagamentoOsService;
	
	
	private CondicaoPagamentoService condicaoPagamentoService;
	
	
	private ProdutoService produtoService;
	
	
	private ParametroService parametroService;
	
	
	private MovimentoFinanceiroService movimentoFinanceiroService;
	
	
	private EmailOSController emailOSController;
	
	private static final String PAGE_CLIENTE = "/pages_framework/p_cliente.xhtml";
	private static final String PAGE_SERVICO = "/pages_framework/p_servicos.xhtml";
	private static final String PAGE_PRODUTO = "/pages_framework/p_produto.xhtml";
	private static final String MSG_PAGAMENTO_REALIZADO = "Não é possível gerar o parcelamento, pois já existe parcela(s) recebida(s)!";
	private static final String PENDENTE_PAGAMENTO = "Pendente para pagamento!";
	
	@PostConstruct
	public void init() {
		novaOrdemServico();
		
		this.templateViewPage = new TemplateViewPage();
		this.movimentoFinanceiroService = new MovimentoFinanceiroService();
		this.parametroService = new ParametroService();
		this.produtoService = new ProdutoService();
		this.ordemServicoService = new OrdemServicoService();
		this.servicoOrdemServicoService = new ServicoOrdemServicoService();
		this.produtoOrdemServicoService = new ProdutoOrdemServicoService();
		this.parcelasPagamentoOsService = new ParcelasPagamentoOsService();
		this.funcionarioService = new FuncionarioService();
		this.clienteService = new ClienteService();
    	this.condicaoPagamentoService = new CondicaoPagamentoService();

	}
	
	public void pesquisarCliente() {
		clientes = clienteService.findByParametersForSituation(pesquisaCliente, 
				Situacao.ATIVO, "nomeDaPessoaFisica", "LIKE", "", "%"); 
		
		if (clientes.isEmpty()) {
			clientes = clienteService.findByParametersForSituation(pesquisaCliente, 
					Situacao.ATIVO, "telefone", "LIKE", "", "%"); 
		}
	}
	
	public void selecionarCliente(Cliente cliente){
		ordemServico.setCliente(clienteService.verificarTipoPessoa(clienteService.findById(cliente.getId())));
	}
	
	public void setarConfirmacaoPagamento(ParcelasPagamentoOs parcela){
		this.parcela = parcela;
		RequestContextUtil.execute("PF('dialog_confirmar_pagamento').show();");
	}
	
	public void confirmarPagamentoParcela(){
		try {
			parcelasPagamentoOsService.salvarMovimentoReceitaParcela(parcela);
			RequestContextUtil.execute("PF('dialog_confirmar_pagamento').hide();");
			this.parcela = new ParcelasPagamentoOs();
		} catch (Exception e) {
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void obterPageCliente(){
		templateViewPage.openDialog(PAGE_CLIENTE, 
				"idTitleCliente", 900L, 495L, true);
	}
	
	public void fecharDialogCliente(Cliente cliente){
		templateViewPage.closeDialog(cliente);
	}
	
	public void clienteSelecionado(SelectEvent event){
		this.ordemServico.setCliente(clienteService.verificarTipoPessoa((Cliente) event.getObject()));
	}
	
	public void obterPageServico(){
		templateViewPage.openDialog(PAGE_SERVICO, 
				"idTitleServico", 800L, 450L, true);
	}
	
	public void fecharDialogServico(Servico servico){
		templateViewPage.closeDialog(servico);
	}
	
	public void servicoSelecionado(SelectEvent event){
		if(getVerificarSeExistePagamentoRealizado()){
			FacesUtil.mensagemWarn("Não é possível adicionar o serviço, "
					+ "pois já existe uma parcela recebida na ordem de serviço!");
		}else{
			this.servico = (Servico) event.getObject();
			if(servicoOrdemServicoService.verificarSeExisteServicoNaTabela(listaServicos, servico)){
				FacesUtil.mensagemWarn("Já existe um serviço '"+this.servico.getNome() +
						"' na lista, por favor escolha outro serviço!");
			}else{
				somarTotalServico(this.servico.getValor());
				adicionarServico();
			}
		}
	}
	public void obterPageProduto(){
		templateViewPage.openDialog(PAGE_PRODUTO, 
				"idTitleProduto", 800L, 450L, true);
	}
	
	public void fecharDialogProduto(Produto produto){
		templateViewPage.closeDialog(produto);
	}
	
	public void produtoSelecionado(SelectEvent event){
		if(getVerificarSeExistePagamentoRealizado()){
			FacesUtil.mensagemWarn("Não é possível adicionar o produto, "
					+ "pois já existe uma parcela recebida na ordem de serviço!!");
		}else{
			this.produto = (Produto) event.getObject();
			if(parametroService.verificarParametroEstoqueNegativo(this.produto)){
				FacesUtil.mensagemWarn("O produto '"+this.produto.getDescricaoProduto()+"' "
						+ "se encontra com estoque igual a "+this.produto.getQuantidadeEstoque()+", não é permitido adicionar o produto, "
						+ "desmarque o parâmetro no sistema.");
			}else{
				if(produtoOrdemServicoService.verificarSeExisteProdutoNaTabela(listaProdutos, produto)){
					FacesUtil.mensagemWarn("Já existe um produto '"+this.produto.getDescricaoProduto() +
							"' na lista, por favor escolha outro produto!");
				}else{
					somarTotalProduto(this.produto.getValorVenda());
					adicionarProduto();
				}
			}
		}
	}
	
	public void calcularDescontoParcela(ParcelasPagamentoOs parcelasPagamentoOs){
		for(ParcelasPagamentoOs p : parcelas){
			if(parcelasPagamentoOs.getId() == p.getId()){
				p.setValorCobrado(parcelasPagamentoOs.getValorParcela().subtract(parcelasPagamentoOs.getValorDesconto()));
			}
		}
	}
	
	public void gerarParcelas(){
		try {
			if(getVerificarSeExistePagamentoRealizado()){
				FacesUtil.mensagemWarn(MSG_PAGAMENTO_REALIZADO);
			}else{
				parcelas = parcelasPagamentoOsService.gerarParcelas(ordemServico, parcelas, parcelasPagamentoOs);
			}
		} catch (RuntimeException e) {
			FacesUtil.mensagemWarn(e.getMessage());
		}
	}
	
	public void calcularDescontoReais(){
		ordemServico.setDescontoPorcento(BigDecimal.ZERO);
		ordemServico.setTotal((ordemServico.getTotalServico().add(ordemServico.getTotalProduto())).subtract(ordemServico.getDescontoReais()));
	}
	
	public void calcularDescontoPorcentagem(){
		ordemServico.setDescontoReais(BigDecimal.ZERO);
		BigDecimal porcentagem = (ordemServico.getTotalServico().add(ordemServico.getTotalProduto()))
								 .divide(new BigDecimal("100"))
								 .multiply(ordemServico.getDescontoPorcento());
		ordemServico.setTotal((ordemServico.getTotalServico().add(ordemServico.getTotalProduto())).subtract(porcentagem));
		
	}
	
	private void somarTotalServico(BigDecimal valorServico){
		ordemServico.setTotalServico(ordemServico.getTotalServico().add(valorServico));
	}
	private void somarTotalProduto(BigDecimal valorProduto){
		ordemServico.setTotalProduto(ordemServico.getTotalProduto().add(valorProduto));
	}
	
	public void pesquisarOS(){
		try {
			ordensServicos = new ArrayList<OrdemServico>();
			ordensServicos = ordemServicoService.pesquisarPorNumeroEStatusOS(ordemServico);
			System.out.println(ordensServicos);
		} catch (RuntimeException e) {
			FacesUtil.mensagemWarn(e.getMessage());
		}
	}
	
	public boolean getVerificarSeExistePagamentoRealizado(){
		return parcelasPagamentoOsService.verificarSeExistePagamentoRealizado(ordemServico);
	}
	public boolean getVerificarSeExistePagamentoNAORealizado(){
		return parcelasPagamentoOsService.verificarSeExistePagamentoNAORealizado(ordemServico);
	}
	
	public void adicionarServico(){
		ServicoOrdemServico servicoOrdemServico = new ServicoOrdemServico();
		//servico.setId(null);
		servicoOrdemServico.setServico(servico);
		servicoOrdemServico.setSubTotal(servico.getValor());
		servicoOrdemServico.setValor(servico.getValor());
		servicoOrdemServico.setOrdemServico(ordemServico);
		
		//servicoOrdemServicoService.save(servicoOrdemServico);
		
		listaServicos.add(servicoOrdemServico);
		
		//this.listaServicos = ordemServicoService.procurarServicosOS(ordemServico.getId());
		ordemServico.setTotal(ordemServico.getTotalServico().add(ordemServico.getTotalProduto()));
		
		//ordemServicoService.salvar(ordemServico);
		servico = new Servico();
	}
	
	public void adicionarProduto(){
		ProdutoOrdemServico produtoOrdemServico = new ProdutoOrdemServico();
		produtoOrdemServico.setProduto(produto);
		produtoOrdemServico.setSubTotal(produto.getValorVenda());
		produtoOrdemServico.setValor(produto.getValorVenda());
		produtoOrdemServico.setOrdemServico(ordemServico);
		
		listaProdutos.add(produtoOrdemServico);
		
		ordemServico.setTotal(ordemServico.getTotalProduto().add(ordemServico.getTotalServico()));
		
		produto = new Produto();
	}
	
	public void calcularValorServico(ServicoOrdemServico servicoOrdemServico){
		ordemServico.setTotalServico(BigDecimal.ZERO);
		for(ServicoOrdemServico so : listaServicos){
			if(so.getServico().getId() == servicoOrdemServico.getServico().getId()){
				BigDecimal valorServico = servicoOrdemServico.getServico().getValor().
									      multiply(BigDecimal.valueOf(servicoOrdemServico.getQuantidade()));
				so.setValor(servicoOrdemServico.getValor());
				so.setSubTotal(valorServico);
			}
			ordemServico.setTotal((ordemServico.getTotalServico().add(ordemServico.getTotalProduto())).add(so.getSubTotal()));
			
			ordemServico.setTotalServico(ordemServico.getTotalServico().add(so.getSubTotal()));
		}
	}
	
	public void setarProduto(ProdutoOrdemServico produtoOrdemServico){
		this.produtoOrdemServico = produtoOrdemServico;
		this.quantidadeAdicionada = BigDecimal.ZERO;
	}
	
	public void calcularValorProduto(){
		
			if(this.quantidadeAdicionada.compareTo(produtoOrdemServico.getProduto().getQuantidadeEstoque()) >= 0){
				if(parametroService.verificarParametroEstoqueNegativo()){
					FacesUtil.mensagemWarn("Não é possível adicionar a quantidade '"+this.quantidadeAdicionada+"' "
							+ "para o produto '"+produtoOrdemServico.getProduto().getDescricaoProduto()+" "
							+ "pois, o mesmo ficara com estoque negativo"
							+ "verifique os parâmetros do sistema "
							+ "e tente novamente!");
					return;
				}
			}
		
			if(parametroService.verificarParametroEstoqueNegativo(produtoOrdemServico.getProduto())){
				FacesUtil.mensagemWarn("O produto '"+produtoOrdemServico.getProduto().getDescricaoProduto()+"' "
						+ "se encontra com estoque igual a "+produtoOrdemServico.getProduto().getQuantidadeEstoque()+", não é permitido adicionar o produto, "
						+ "para permitir essa ação desmarque o parâmetro do sistema.");
				return;
			}
			
			for(ProdutoOrdemServico po : listaProdutos){
				if(po.getQuantidade().add(quantidadeAdicionada).compareTo(produtoOrdemServico.getProduto().getQuantidadeEstoque()) > 0){
					if(parametroService.verificarParametroEstoqueNegativo()){
						FacesUtil.mensagemWarn("Não é possível adicionar a quantidade '"+this.quantidadeAdicionada+"' "
								+ "para o produto '"+produtoOrdemServico.getProduto().getDescricaoProduto()+" "
								+ "pois, o mesmo ficara com estoque negativo "
								+ "verifique os parâmetros do sistema "
								+ "e tente novamente!");
						return;
					}
				}
			}

		if(quantidadeAdicionada == BigDecimal.ZERO){
			FacesUtil.mensagemWarn("A quantidade é obrigatório!");
		}else{
			ordemServico.setTotalProduto(BigDecimal.ZERO);
			produtoOrdemServico.setQuantidade(produtoOrdemServico.getQuantidade().add(quantidadeAdicionada));
			
			for(ProdutoOrdemServico po : listaProdutos){
				if(po.getProduto().getId() == produtoOrdemServico.getProduto().getId()){
					BigDecimal valorProduto = produtoOrdemServico.getProduto().getValorVenda().
							multiply(produtoOrdemServico.getQuantidade());
					po.setValor(produtoOrdemServico.getValor());
					po.setSubTotal(valorProduto);
				}
				ordemServico.setTotal((ordemServico.getTotalServico().add(ordemServico.getTotalProduto())).add(po.getSubTotal()));
				ordemServico.setTotalProduto(ordemServico.getTotalProduto().add(po.getSubTotal()));
				
			}
			RequestContextUtil.execute("PF('dialog_quantidade_estoque_produto').hide();");
		}
		
	}
	
	public void setarOrdemServico(OrdemServico ordemServico){
		this.ordemServico.setTotalServico(BigDecimal.ZERO);
		this.ordemServico.setCliente(clienteService.verificarTipoPessoa(ordemServico.getCliente()));
		this.ordemServico = ordemServico;
		this.listaServicos = ordemServicoService.procurarServicosOS(ordemServico.getId());
		this.listaProdutos = ordemServicoService.procurarProdutosOS(ordemServico.getId());
		this.parcelas = parcelasPagamentoOsService.procurarParcelasPorOS(ordemServico.getId());
		
		verificarPagamentoPendenteOS(parcelas);
		
		RequestContextUtil.execute("PF('dialogEditarOrdemDeServico').show();");
		ordensServicos = new ArrayList<OrdemServico>();
		
		setarTabIndex(0);
	}
	
	private void verificarPagamentoPendenteOS(List<ParcelasPagamentoOs> parcelas){
		for(ParcelasPagamentoOs p : parcelas){
			if(p.getOrdemServico().getId() == ordemServico.getId()){
				if(p.getStatusFinanceiro() == StatusFinanceiro.PENDENTE){
					if(ordemServico.getStatusOS() == StatusOS.ABERTO || ordemServico.getStatusOS() == StatusOS.EM_ANDAMENTO){
						ordemServico.setPagamentoPendente(PENDENTE_PAGAMENTO);
					}
				}
			}
		}
	}
	
	public void novaOrdemServico(){
		this.ordemServico = new OrdemServico();
		this.servico = new Servico();
		this.produtoOrdemServico = new ProdutoOrdemServico();
		this.ordensServicos = new ArrayList<OrdemServico>();
		this.servicos = new ArrayList<Servico>();
		this.produtos = new ArrayList<Produto>();
		this.funcionarios = new ArrayList<Funcionario>();
		this.clientes = new ArrayList<Cliente>();
		this.listaServicos = new ArrayList<ServicoOrdemServico>();
		this.listaProdutos = new ArrayList<ProdutoOrdemServico>();
		this.ordensServicosAbertas = new ArrayList<OrdemServico>();
		this.parcelas = new ArrayList<ParcelasPagamentoOs>();
		this.listaProdutosTemporário = new ArrayList<ProdutoOrdemServico>();
		
		setarTabIndex(0);
	}
	
	public void salvar(){
		try {
			if(ordemServico.getStatusOS() == StatusOS.CANCELADO){
				/*if(getVerificarSeExistePagamentoNAORealizado()){
					FacesUtil.mensagemWarn("Não é possível cancelar a ordem de serviço, "
							+ "pois ainda existe parcelas para ser recebido!!");
				}else{
				}*/
				RequestContextUtil.execute("PF('dialog_motivo_cancelamento').show();");
			}else{
				BigDecimal somaValorParcela = BigDecimal.ZERO;
				for(ParcelasPagamentoOs p : parcelas){
					somaValorParcela = somaValorParcela.add(p.getValorParcela());
				}
				
				if(somaValorParcela != BigDecimal.ZERO){
					if(ordemServico.getTotal().doubleValue() != somaValorParcela.doubleValue()){
						RequestContextUtil.execute("PF('dialog_confirmacao_valor_parcelas').show();");
					}else{
						salvarOrdemServico();
					}
				}else{
					salvarOrdemServico();
				}
			}
			
		} catch (RuntimeException e) {
			e.printStackTrace();
			FacesUtil.mensagemErro(e.getMessage());
		}
	}
	
	public void salvarMovimentoFinanceiro(OrdemServico ordemServico, List<ParcelasPagamentoOs> parcelasPagamentoOs){
		for(ParcelasPagamentoOs p : parcelasPagamentoOs){
			if(p.getStatusFinanceiro() == null){
				p.setStatusFinanceiro(StatusFinanceiro.PENDENTE);
			}
			if(p.getStatusFinanceiro() == StatusFinanceiro.PENDENTE){
				movimentoFinanceiroService.salvarMovimentoFinanceiroOS(ordemServico, p);
			}
		}
	}
	
	private void salvarOrdemServico(){
		if(ordemServico.getStatusOS() == StatusOS.FINALIZADO){
			if(getVerificarSeExistePagamentoNAORealizado()){
				FacesUtil.mensagemWarn("Não é possível finalizar a ordem de serviço, "
						+ "pois ainda existe parcelas para ser recebido!");
			}else{
				ordemServico.setDataSaida(Calendar.getInstance().getTime());
				ordemServico.setHoraSaida(Calendar.getInstance().getTime());
				salvarOS();
				fecharDialogs();
			}
		}else{
			salvarOS();
			//RequestContextUtil.execute("PF('dialog_opcoes').show();");
			fecharDialogs();
		}
	}
	
	public void confirmarSalvamentoOS(){
		salvarOrdemServico();
		RequestContextUtil.execute("PF('dialog_confirmacao_valor_parcelas').hide()");
	}
	
	private void salvarOS(){
		if(ordemServico.getId() == null){
			ordemServico = ordemServicoService.salvar(ordemServico);
			//parcelasPagamentoOsService.salvar(ordemServico, parcelas);
			//ordemServicoService.consistirServico(listaServicos, ordemServico);
			//ordemServicoService.consistirProduto(listaProdutos, ordemServico);
		}else{
			ordemServico = ordemServicoService.salvar(ordemServico);
			salvarMovimentoFinanceiro(ordemServico, parcelas);
			parcelas = parcelasPagamentoOsService.salvar(ordemServico, parcelas);
			
			List<ProdutoOrdemServico> listaProdutoOS = produtoOrdemServicoService.findByListProperty(ordemServico.getId(), "ordemServico.id");
			
			for(ProdutoOrdemServico listProduto : listaProdutos){
					List<ProdutoOrdemServico> prodBd = produtoOrdemServicoService.findByListProperty(listProduto.getProduto().getId(), "produto.id");
					if(listaProdutoOS.isEmpty()){
						subtrairQuantidadeEstoqueProduto(listProduto);
					}else{
						if(prodBd.isEmpty()){
							subtrairQuantidadeEstoqueProduto(listProduto);
						}else{
							for(ProdutoOrdemServico ps : listaProdutoOS){
								if(ps.getProduto().getId() == listProduto.getProduto().getId()){
									if(ps.getQuantidade() != listProduto.getQuantidade()){
										subtrairQuantidadeEstoqueProduto(listProduto, ps);
									}
								}
							}
						}
					}
					produtoOrdemServicoService.save(listProduto);
			}
			for(ServicoOrdemServico sos : listaServicos){
				servicoOrdemServicoService.save(sos);
			}
		}
		
		FacesUtil.mensagemInfo("Ordem de servico de nº "+ordemServico.getId() + " salvo com sucesso!");
	}
	
	private void subtrairQuantidadeEstoqueProduto(ProdutoOrdemServico listProduto){
		if(listProduto.getId() == null){
			listProduto.getProduto().setQuantidadeEstoque
						(listProduto.getProduto().getQuantidadeEstoque().subtract(listProduto.getQuantidade()));
		}else{
			listProduto.getProduto().setQuantidadeEstoque
					    (listProduto.getProduto().getQuantidadeEstoque().subtract(this.quantidadeAdicionada));
		}
		produtoService.salvar(listProduto.getProduto());
	}
	
	private void subtrairQuantidadeEstoqueProduto(ProdutoOrdemServico listProduto, ProdutoOrdemServico ps){
		if(listProduto.getQuantidade().compareTo(ps.getQuantidade()) > 0){
			subtrairQuantidadeEstoqueProduto(listProduto);
		}
		this.quantidadeAdicionada = BigDecimal.ZERO;
	}
	
	public void salvarMotivoCancelamento(){
		if(ordemServico.getMotivoCancelamento().trim().isEmpty()){
			FacesUtil.mensagemWarn("O motivo de cancelamento é obrigatório!");
		}else{
			ordemServico.setDataSaida(Calendar.getInstance().getTime());
			ordemServico.setHoraSaida(Calendar.getInstance().getTime());
			salvarOS();
			adicionarQtdeEstoqueProduto();
			RequestContextUtil.execute("PF('dialog_motivo_cancelamento').hide();");
			fecharDialogs();
		}
	}
	
	private void adicionarQtdeEstoqueProduto(){
		for(ProdutoOrdemServico listProduto : listaProdutos){
			if(listProduto.getOrdemServico().getId() == ordemServico.getId()){
				listProduto.getProduto().setQuantidadeEstoque(listProduto.getProduto().getQuantidadeEstoque().add(listProduto.getQuantidade()));
				produtoService.save(listProduto.getProduto());
			}
		}
	}
	
	public void fecharDialogs(){
		RequestContextUtil.execute("PF('dialogNovaOrdemDeServico').hide();");
		RequestContextUtil.execute("PF('dialogEditarOrdemDeServico').hide();");
		ordensServicos = new ArrayList<OrdemServico>();
		
	}
	
	public void fecharDialogMotivoCancelamento(){
		fecharDialogs();
		this.ordemServico = ordemServicoService.findById(ordemServico.getId());
		pesquisarOS();
	}
	
	public void removerServico(ServicoOrdemServico servicoOrdemServico){
		
		if(getVerificarSeExistePagamentoRealizado()){
			FacesUtil.mensagemWarn("Não é possível excluir o serviço, "
					+ "pois já existe uma parcela recebida na ordem de serviço!!");
		}else{
			if(servicoOrdemServico.getId() == null){
				for(int i = 0; i < this.listaServicos.size(); i++){
					if(this.listaServicos.get(i).getServico().getNome().equals(servicoOrdemServico.getServico().getNome())){
						this.listaServicos.remove(i);
						ordemServico.setTotalServico(ordemServico.getTotalServico().subtract(servicoOrdemServico.getSubTotal()));
						ordemServico.setTotal(BigDecimal.ZERO);
						ordemServico.setTotal(ordemServico.getTotalServico().add(ordemServico.getTotalProduto()));
					}
				}
			}else{
				
				this.listaServicos = servicoOrdemServicoService.removerServicoOSPeloID(listaServicos, servicoOrdemServico);	
				
				if(!servicoOrdemServicoService.verificarSeExisteIdNull(listaServicos)){
					this.listaServicos = ordemServicoService.procurarServicosOS(ordemServico.getId());
				}
				
				ordemServico.setTotalServico(ordemServico.getTotalServico().subtract(servicoOrdemServico.getSubTotal()));
				ordemServico.setTotal(BigDecimal.ZERO);
				ordemServico.setTotal(ordemServico.getTotalServico().add(ordemServico.getTotalProduto()));
				ordemServicoService.salvar(ordemServico);
			}
		}
		
	}
	
	public void removerProduto(ProdutoOrdemServico produtoOrdemServico){
		
		if(getVerificarSeExistePagamentoRealizado()){
			FacesUtil.mensagemWarn("Não é possível excluir o produto, "
					+ "pois já existe uma parcela recebida na ordem de serviço!!");
		}else{
			if(produtoOrdemServico.getId() == null){
				for(int i = 0; i < listaProdutos.size(); i++){
					if(this.listaProdutos.get(i).getProduto().getDescricaoProduto().trim().equals(produtoOrdemServico.getProduto().getDescricaoProduto().trim())){
						this.listaProdutos.remove(i);
						ordemServico.setTotalProduto(ordemServico.getTotalProduto().subtract(produtoOrdemServico.getSubTotal()));
						ordemServico.setTotal(BigDecimal.ZERO);
						ordemServico.setTotal(ordemServico.getTotalProduto().add(ordemServico.getTotalServico()));
					}
				}
			}else{
				
				for(int i = 0; i < listaProdutos.size(); i++){
					if(this.listaProdutos.get(i).getProduto().getDescricaoProduto().trim().equals(produtoOrdemServico.getProduto().getDescricaoProduto().trim())){
						produtoOrdemServicoService.remove(produtoOrdemServico.getId());
						this.listaProdutos.remove(i);
					}
				}	
				
				produtoOrdemServico.getProduto().setQuantidadeEstoque(produtoOrdemServico.getProduto().getQuantidadeEstoque().add(produtoOrdemServico.getQuantidade()));
				produtoService.salvar(produtoOrdemServico.getProduto());
				
				ordemServico.setTotalProduto(ordemServico.getTotalProduto().subtract(produtoOrdemServico.getSubTotal()));
				ordemServico.setTotal(BigDecimal.ZERO);
				ordemServico.setTotal(ordemServico.getTotalProduto().add(ordemServico.getTotalServico()));
				ordemServicoService.salvar(ordemServico);
			}
		}
		
	}
	
	public void gerarComprovantePagamento(ParcelasPagamentoOs parcelasPagamentoOs){
		try {
			ordemServicoService.gerarComprovantePagamento(parcelasPagamentoOs);
			RequestContextUtil.execute("PF('pdfComprovantePagamento').show();");
		} catch (FileNotFoundException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}finally {
			parcelasPagamentoOs = new ParcelasPagamentoOs();
		}
	}
	
	public void gerarNotaRecebimento(OrdemServico ordemServico){
		try {
			ordemServicoService.gerarNotaRecebimento(ordemServico);
			RequestContextUtil.execute("PF('pdfViewNotaRecebimento').show();");
		} catch (RuntimeException e) {
			FacesUtil.mensagemErro(e.getMessage());
		}finally {
			ordemServico = new OrdemServico();
		}
	}
	
	public void gerarOrdemServico(OrdemServico ordemServico){
			try {
				gerarOS(ordemServico);
				RequestContextUtil.execute("PF('pdfViewOrdemServico').show();");
			}finally {
				ordemServico = new OrdemServico();
			}
	}
	
	public void enviarEmail(){
			ordemServicoService.isEnviarEmail(true);
			ordemServicoService.gerarOrdemServico(ordemServico, 
					ordemServicoService.procurarServicosOS(ordemServico.getId()), 
					ordemServicoService.procurarProdutosOS(ordemServico.getId()),
					ordemServicoService.procurarPagamentoOS(ordemServico.getId()));
			emailOSController.enviarEmail(ordemServico.getId());
		
	}
	
	private void gerarOS(OrdemServico ordemServico){
		ordemServicoService.gerarOrdemServico(ordemServico, 
				ordemServicoService.procurarServicosOS(ordemServico.getId()), 
				ordemServicoService.procurarProdutosOS(ordemServico.getId()),
				ordemServicoService.procurarPagamentoOS(ordemServico.getId()));
	}
	
	public void setarTabIndex(int tabIndex) {
	     this.setCurrentTab(tabIndex);
	}

	public StatusOS[] getStatusOs() {
		return StatusOS.values();
	}

	public StatusFinanceiro[] getStatusFinanceiros() {
		return StatusFinanceiro.values();
	}

	public Garantia[] getGarantia() {
		return Garantia.values();
	}
	
	public TipoDesconto[] getTiposDesconto() {
		return TipoDesconto.values();
	}

	public FormaPagamento[] getFormaPagamento() {
		return FormaPagamento.values();
	}

	public OrdemServico getOrdemServico() {
			if(ordemServico == null){
				ordemServico = new OrdemServico();
			}
		return ordemServico;
	}

	public void setOrdemServico(OrdemServico ordemServico) {
		this.ordemServico = ordemServico;
	}

	public List<OrdemServico> getOrdensServicos() {
		return ordensServicos;
	}

	public void setOrdensServicos(List<OrdemServico> ordensServicos) {
		this.ordensServicos = ordensServicos;
	}

	public List<Servico> getServicos() {
		return servicos;
	}

	public void setServicos(List<Servico> servicos) {
		this.servicos = servicos;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public List<Funcionario> getFuncionarios() {
		return funcionarioService.findBySituation(Situacao.ATIVO);
	}

	public void setFuncionarios(List<Funcionario> funcionarios) {
		this.funcionarios = funcionarios;
	}

	public List<Cliente> getClientes() {
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public Servico getServico() {
		if(servico == null){
			servico = new Servico();
		}
		return servico;
	}

	public void setServico(Servico servico) {
		this.servico = servico;
	}

	public List<ServicoOrdemServico> getListaServicos() {
		return listaServicos;
	}

	public void setListaServicos(List<ServicoOrdemServico> listaServicos) {
		this.listaServicos = listaServicos;
	}

	public List<OrdemServico> getOrdensServicosAbertas() {
		return ordemServicoService.findByStatusOs(StatusOS.ABERTO, StatusOS.EM_ANDAMENTO);
	}

	public void setOrdensServicosAbertas(List<OrdemServico> ordensServicosAbertas) {
		this.ordensServicosAbertas = ordensServicosAbertas;
	}

	public List<CondicaoPagamento> getCondicoesPagamento() {
		return condicaoPagamentoService.findBySituation(Situacao.ATIVO);
	}

	public void setCondicoesPagamento(List<CondicaoPagamento> condicoesPagamento) {
		this.condicoesPagamento = condicoesPagamento;
	}

	public List<ParcelasPagamentoOs> getParcelas() {
		return parcelas;
	}

	public void setParcelas(List<ParcelasPagamentoOs> parcelas) {
		this.parcelas = parcelas;
	}

	public Produto getProduto() {
		if(produto == null){
			produto = new Produto();
		}
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<ProdutoOrdemServico> getListaProdutos() {
		return listaProdutos;
	}

	public void setListaProdutos(List<ProdutoOrdemServico> listaProdutos) {
		this.listaProdutos = listaProdutos;
	}

	public int getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(int currentTab) {
		this.currentTab = currentTab;
	}

	public List<ProdutoOrdemServico> getListaProdutosTemporário() {
		return listaProdutosTemporário;
	}

	public void setListaProdutosTemporário(List<ProdutoOrdemServico> listaProdutosTemporário) {
		this.listaProdutosTemporário = listaProdutosTemporário;
	}

	public ProdutoOrdemServico getProdutoOrdemServico() {
		return produtoOrdemServico == null ? new ProdutoOrdemServico() : this.produtoOrdemServico;
	}

	public void setProdutoOrdemServico(ProdutoOrdemServico produtoOrdemServico) {
		this.produtoOrdemServico = produtoOrdemServico;
	}

	public BigDecimal getQuantidadeAdicionada() {
		return quantidadeAdicionada;
	}

	public void setQuantidadeAdicionada(BigDecimal quantidadeAdicionada) {
		this.quantidadeAdicionada = quantidadeAdicionada;
	}

	public ParcelasPagamentoOs getParcela() {
		return parcela == null ? new ParcelasPagamentoOs() : this.parcela;
	}

	public void setParcela(ParcelasPagamentoOs parcela) {
		this.parcela = parcela;
	}

	public String getPesquisaCliente() {
		return pesquisaCliente;
	}

	public void setPesquisaCliente(String pesquisaCliente) {
		this.pesquisaCliente = pesquisaCliente;
	}

}
