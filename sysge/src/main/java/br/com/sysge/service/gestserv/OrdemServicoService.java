package br.com.sysge.service.gestserv;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import br.com.sysge.infraestrutura.dao.GenericDaoImpl;
import br.com.sysge.model.gestserv.OrdemServico;
import br.com.sysge.model.gestserv.ProdutoOrdemServico;
import br.com.sysge.model.gestserv.ServicoOrdemServico;
import br.com.sysge.service.estoque.ProdutoService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.type.WhenNoDataTypeEnum;
import net.sf.jasperreports.engine.util.JRLoader;

public class OrdemServicoService extends GenericDaoImpl<OrdemServico, Long> {

	private static final long serialVersionUID = 6697038638256448464L;

	@Inject
	private ServicoOrdemServicoService servicoOrdemServicoService;
	
	@Inject
	private ProdutoOrdemServicoService produtoOrdemServicoService;
	
	@Inject
	private ServicoService servicoService;

	@Inject
	private ProdutoService produtoService;
	
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
	
	public static void main(String[] args) {
		new OrdemServicoService().gerarRelatorioOrdemServico();
	}
	
	public StreamedContent gerarRelatorioOrdemServico(){
		InputStream relatorio = null;
		try {
			ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
			JasperReport jasperReport = (JasperReport)JRLoader.loadObject(getClass().getClassLoader().getResourceAsStream("r_ordem_servico.jasper"));
			 jasperReport.setWhenNoDataType(WhenNoDataTypeEnum.ALL_SECTIONS_NO_DETAIL);

		     HashMap<String, Integer> params = new HashMap<String, Integer>();
		     JasperPrint print = JasperFillManager.fillReport(jasperReport, params);

		     JRExporter exporter = new net.sf.jasperreports.engine.export.JRPdfExporter();
		     //JRExporter exporter = new net.sf.jasperreports.engine.export.JRHtmlExporter();
		     //JRExporter exporter = new net.sf.jasperreports.engine.export.JRXlsExporter();
		     //JRExporter exporter = new net.sf.jasperreports.engine.export.JRXmlExporter();
		     //JRExporter exporter = new net.sf.jasperreports.engine.export.JRCsvExporter();

		     //exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, pdfFile);
		     exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, byteArray);
		     exporter.setParameter(JRExporterParameter.JASPER_PRINT, print);
		     exporter.exportReport();

		     relatorio = new ByteArrayInputStream(byteArray.toByteArray());
		} catch (JRException e) {
			e.printStackTrace();
		}
		return new DefaultStreamedContent(relatorio);
	}
	
}
