package org.casadeguara.views;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import org.casadeguara.componentes.AutoCompleteTextField;
import org.casadeguara.componentes.LimitedTextField;
import org.casadeguara.componentes.MaskedTextField;
import org.casadeguara.entidades.Leitor;
import org.casadeguara.models.GenericModel;

/**
 * Controi a tela de cadastro de leitor
 * 
 * @author Gustavo
 */
public class CadastroLeitorView implements GenericView {

	private AnchorPane painelLeitor;
	private Button btnCadastrar;
	private Button btnAlterar;
	private Button btnLimpar;
	private Button btnFicha;
	private CheckBox chbTrabalhador;
	private CheckBox chbInativo;
	private CheckBox chbIncompleto;
	private ComboBox<String> cbbSexo;
	private AutoCompleteTextField<Leitor> pesquisarLeitores;
	private MaskedTextField txtRg;
	private MaskedTextField txtCep;
	private MaskedTextField txtCpf;
	private MaskedTextField txtTelefone1;
	private MaskedTextField txtTelefone2;
	private TextField txtNome;
	private TextField txtEmail;
	private TextField txtLogradouro;
	private TextField txtBairro;
	private TextField txtComplemento;

	public CadastroLeitorView() {
		painelLeitor = new AnchorPane();

		Label titulo = new Label("Cadastro de Leitores");
		aplicarEstilo(titulo, "titulo");

		Label pesquise = new Label("Pesquise um leitor:");
		Label dadosPessoais = new Label("Dados do leitor:");
		Label info = new Label("Informações Adicionais:");
		Label opcoes = new Label("Opções:");

		Label lblNome = new Label("Nome:*");
		Label lblEmail = new Label("Email: ");
		Label lblLogradouro = new Label("Endereço: ");
		Label lblBairro = new Label("Bairro: ");
		Label lblComp = new Label("Comp.: ");
		Label lblCep = new Label("Cep: ");
		Label lblTelefone1 = new Label("Celular: ");
		Label lblTelefone2 = new Label("Whatsapp: ");
		Label lblRg = new Label("RG: ");
		Label lblCpf = new Label("CPF: ");
		Label lblSexo = new Label("Sexo: ");

		btnCadastrar = new Button("Cadastrar");
		btnAlterar = new Button("Alterar");
		btnLimpar = new Button("Limpar");
		btnFicha = new Button("Ficha de Cadastro");

		btnAlterar.setDisable(true);
		btnFicha.setDisable(true);

		configurarTamanhoBotao(btnAlterar);
		configurarTamanhoBotao(btnCadastrar);
		configurarTamanhoBotao(btnFicha);
		configurarTamanhoBotao(btnLimpar);

		cbbSexo = new ComboBox<>();
		cbbSexo.getItems().addAll("F", "M");
		cbbSexo.getSelectionModel().selectFirst();

		chbTrabalhador = new CheckBox("Trabalhador");
		chbInativo = new CheckBox("Marcar como inativo");
		chbIncompleto = new CheckBox("Cadastro incompleto");

		txtNome = new TextField();
		txtEmail = new TextField();
		txtLogradouro = new TextField();
		txtBairro = new LimitedTextField(30);
		txtComplemento = new LimitedTextField(15);
		txtCep = new MaskedTextField("#####-###");
		txtTelefone1 = new MaskedTextField("(##)#####-####");
		txtTelefone2 = new MaskedTextField("(##)#####-####");
		txtRg = new MaskedTextField("########-##");
		txtCpf = new MaskedTextField("###.###.###-##");

		txtNome.setPrefWidth(535);
		txtEmail.setPrefWidth(535);
		txtLogradouro.setPrefWidth(535);
		txtTelefone1.setPrefWidth(135);
		txtTelefone2.setPrefWidth(135);

		pesquisarLeitores = new AutoCompleteTextField<>();
		pesquisarLeitores.setPrefWidth(535);
		pesquisarLeitores.setPromptText("Selecione um leitor para alteração");

		AnchorPane.setTopAnchor(titulo, 0.0);
		AnchorPane.setTopAnchor(pesquise, 40.0);
		AnchorPane.setTopAnchor(pesquisarLeitores, 70.0);
		AnchorPane.setTopAnchor(dadosPessoais, 130.0);
		AnchorPane.setTopAnchor(lblNome, 164.0);
		AnchorPane.setTopAnchor(txtNome, 160.0);
		AnchorPane.setTopAnchor(lblEmail, 199.0);
		AnchorPane.setTopAnchor(txtEmail, 195.0);
		AnchorPane.setTopAnchor(lblTelefone1, 234.0);
		AnchorPane.setTopAnchor(txtTelefone1, 230.0);
		AnchorPane.setTopAnchor(lblTelefone2, 234.0);
		AnchorPane.setTopAnchor(txtTelefone2, 230.0);
		AnchorPane.setTopAnchor(lblSexo, 234.0);
		AnchorPane.setTopAnchor(cbbSexo, 230.0);
		AnchorPane.setTopAnchor(lblRg, 269.0);
		AnchorPane.setTopAnchor(txtRg, 265.0);
		AnchorPane.setTopAnchor(lblCpf, 304.0);
		AnchorPane.setTopAnchor(txtCpf, 300.0);
		AnchorPane.setTopAnchor(lblLogradouro, 339.0);
		AnchorPane.setTopAnchor(txtLogradouro, 335.0);
		AnchorPane.setTopAnchor(lblBairro, 374.0);
		AnchorPane.setTopAnchor(txtBairro, 370.0);
		AnchorPane.setTopAnchor(lblCep, 374.0);
		AnchorPane.setTopAnchor(txtCep, 370.0);
		AnchorPane.setTopAnchor(lblComp, 409.0);
		AnchorPane.setTopAnchor(txtComplemento, 405.0);
		AnchorPane.setTopAnchor(info, 460.0);
		AnchorPane.setTopAnchor(chbTrabalhador, 490.0);
		AnchorPane.setTopAnchor(chbInativo, 490.0);
		AnchorPane.setTopAnchor(chbIncompleto, 490.0);
		AnchorPane.setTopAnchor(opcoes, 550.0);
		AnchorPane.setTopAnchor(btnCadastrar, 580.0);
		AnchorPane.setTopAnchor(btnLimpar, 580.0);
		AnchorPane.setTopAnchor(btnAlterar, 580.0);
		AnchorPane.setTopAnchor(btnFicha, 580.0);

		AnchorPane.setLeftAnchor(titulo, 0.0);
		AnchorPane.setLeftAnchor(pesquise, 0.0);
		AnchorPane.setLeftAnchor(pesquisarLeitores, 0.0);
		AnchorPane.setLeftAnchor(dadosPessoais, 0.0);
		AnchorPane.setLeftAnchor(lblNome, .0);
		AnchorPane.setLeftAnchor(txtNome, 82.0);
		AnchorPane.setLeftAnchor(lblEmail, 0.0);
		AnchorPane.setLeftAnchor(txtEmail, 82.0);
		AnchorPane.setLeftAnchor(lblTelefone1, 0.0);
		AnchorPane.setLeftAnchor(txtTelefone1, 82.0);
		AnchorPane.setLeftAnchor(lblTelefone2, 235.0);
		AnchorPane.setLeftAnchor(txtTelefone2, 315.0);
		AnchorPane.setLeftAnchor(lblRg, 0.0);
		AnchorPane.setLeftAnchor(txtRg, 82.0);
		AnchorPane.setLeftAnchor(lblCpf, 0.0);
		AnchorPane.setLeftAnchor(txtCpf, 82.0);
		AnchorPane.setLeftAnchor(lblSexo, 523.0);
		AnchorPane.setLeftAnchor(cbbSexo, 563.0);
		AnchorPane.setLeftAnchor(lblLogradouro, 0.0);
		AnchorPane.setLeftAnchor(txtLogradouro, 82.0);
		AnchorPane.setLeftAnchor(lblBairro, 0.0);
		AnchorPane.setLeftAnchor(txtBairro, 82.0);
		AnchorPane.setLeftAnchor(lblComp, 0.0);
		AnchorPane.setLeftAnchor(txtComplemento, 82.0);
		AnchorPane.setLeftAnchor(lblCep, 405.0);
		AnchorPane.setLeftAnchor(txtCep, 439.0);
		AnchorPane.setLeftAnchor(info, 0.0);
		AnchorPane.setLeftAnchor(chbTrabalhador, 0.0);
		AnchorPane.setLeftAnchor(chbInativo, 150.0);
		AnchorPane.setLeftAnchor(chbIncompleto, 300.0);
		AnchorPane.setLeftAnchor(opcoes, 0.0);
		AnchorPane.setLeftAnchor(btnCadastrar, 0.0);
		AnchorPane.setLeftAnchor(btnLimpar, 135.0);
		AnchorPane.setLeftAnchor(btnAlterar, 270.0);
		AnchorPane.setLeftAnchor(btnFicha, 405.0);

		painelLeitor.getChildren().addAll(titulo, pesquise, dadosPessoais, lblNome, lblCpf, lblRg, lblLogradouro,
				lblBairro, lblComp, lblCep, lblEmail, lblTelefone1, lblTelefone2, lblSexo, opcoes, info,
				pesquisarLeitores, txtNome, txtEmail, txtTelefone1, txtTelefone2, txtRg, txtCpf, txtLogradouro,
				txtBairro, txtComplemento, txtCep, btnCadastrar, btnLimpar, btnAlterar, btnFicha, cbbSexo,
				chbTrabalhador, chbInativo, chbIncompleto);
	}

	public void acaoBotaoAlterar(EventHandler<ActionEvent> event) {
		btnAlterar.setOnAction(event);
	}

	public void acaoBotaoCadastrar(EventHandler<ActionEvent> event) {
		btnCadastrar.setOnAction(event);
	}

	public void acaoBotaoImprimirFicha(EventHandler<ActionEvent> event) {
		btnFicha.setOnAction(event);
	}

	public void acaoBotaoLimpar(EventHandler<ActionEvent> event) {
		btnLimpar.setOnAction(event);
	}

	public void acaoPesquisarLeitor(EventHandler<ActionEvent> event) {
		pesquisarLeitores.setOnAction(event);
	}

	public void estaCadastrando(boolean resposta) {
		btnAlterar.setDisable(resposta);
		btnCadastrar.setDisable(!resposta);
		btnFicha.setDisable(resposta);
		pesquisarLeitores.setDisable(!resposta);
	}

	public String getNomeLeitor() {
		return txtNome.getText().toUpperCase();
	}

	public String getEmailLeitor() {
		return txtEmail.getText();
	}

	public String getTelefone1Leitor() {
		return txtTelefone1.getPlainText();
	}

	public String getTelefone2Leitor() {
		return txtTelefone2.getPlainText();
	}

	public String getLogradouro() {
		return txtLogradouro.getText();
	}

	public String getBairro() {
		return txtBairro.getText();
	}

	public String getComplemento() {
		return txtComplemento.getText();
	}

	public String getCep() {
		return txtCep.getPlainText();
	}

	public String getCpfLeitor() {
		return txtCpf.getPlainText();
	}

	public String getRgLeitor() {
		return txtRg.getPlainText();
	}

	public String getSexoLeitor() {
		return cbbSexo.getSelectionModel().getSelectedItem();
	}

	public boolean isInativo() {
		return chbInativo.isSelected();
	}

	public boolean isTrabalhador() {
		return chbTrabalhador.isSelected();
	}

	public boolean isCadastroIncompleto() {
		return chbIncompleto.isSelected();
	}

	public void setNomeLeitor(String nome) {
		txtNome.setText(nome);
	}

	public void setEmailLeitor(String email) {
		txtEmail.setText(email);
	}

	public void setTelefone1Leitor(String telefone) {
		txtTelefone1.setPlainText(telefone);
	}

	public void setTelefone2Leitor(String telefone) {
		txtTelefone2.setPlainText(telefone);
	}

	public void setLogradouro(String logradouro) {
		txtLogradouro.setText(logradouro);
	}

	public void setBairro(String bairro) {
		txtBairro.setText(bairro);
	}

	public void setComplemento(String complemento) {
		txtComplemento.setText(complemento);
	}

	public void setCep(String cep) {
		txtCep.setPlainText(cep);
	}

	public void setCpfLeitor(String cpf) {
		txtCpf.setPlainText(cpf);
	}

	public void setRgLeitor(String rg) {
		txtRg.setPlainText(rg);
	}

	public void setSexoLeitor(String sexo) {
		cbbSexo.getSelectionModel().select(sexo);
	}

	public void setInativo(boolean selecionado) {
		chbInativo.setSelected(selecionado);
	}

	public void setTrabalhador(boolean selecionado) {
		chbTrabalhador.setSelected(selecionado);
	}

	public void cadastroIncompleto(boolean selecionado) {
		chbIncompleto.setSelected(selecionado);
	}

	public void limparCampos() {
		estaCadastrando(true);

		pesquisarLeitores.clear();
		txtNome.clear();
		txtEmail.clear();
		txtTelefone1.clear();
		txtTelefone2.clear();
		txtLogradouro.clear();
		txtBairro.clear();
		txtComplemento.clear();
		txtCep.clear();
		txtRg.clear();
		txtCpf.clear();
		chbTrabalhador.setSelected(false);
		chbInativo.setSelected(false);
		chbIncompleto.setSelected(false);
	}

	public void setAutoComplete(GenericModel<Leitor> model) {
		pesquisarLeitores.setModel(model);
	}

	public Leitor getTermoPesquisado() {
		return pesquisarLeitores.getResult();
	}

	@Override
	public AnchorPane getRoot() {
		return painelLeitor;
	}
}
