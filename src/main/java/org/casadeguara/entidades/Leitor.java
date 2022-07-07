package org.casadeguara.entidades;

public class Leitor {

	private int id;
	private String nome;
	private String email;
	private String telefone1;
	private String telefone2;
	private String logradouro;
	private String bairro;
	private String complemento;
	private String cep;
	private String cidade;
	private String sexo;
	private String rg;
	private String cpf;
	private String aniversario;
	private boolean active;
	private boolean trab;
	private boolean paciente;
	private boolean incompleto;

	public Leitor(int id, String nome, String email, String telefone1, String telefone2, String logradouro,
			String bairro, String complemento, String cep, String cidade, String sexo, String rg, String cpf,
			String aniversario, boolean status, boolean trab, boolean paciente, boolean incompleto) {

		this.id = id;
		this.nome = nome;
		this.email = email;
		this.telefone1 = telefone1;
		this.telefone2 = telefone2;
		this.logradouro = logradouro;
		this.bairro = bairro;
		this.complemento = complemento;
		this.cep = cep;
		this.cidade = cidade;
		this.sexo = sexo;
		this.rg = rg;
		this.cpf = cpf;
		this.aniversario = aniversario;
		this.active = status;
		this.trab = trab;
		this.paciente = paciente;
		this.incompleto = incompleto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefone1() {
		return telefone1;
	}

	public void setTelefone1(String telefone1) {
		this.telefone1 = telefone1;
	}

	public String getTelefone2() {
		return telefone2;
	}

	public void setTelefone2(String telefone2) {
		this.telefone2 = telefone2;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public boolean isAtivo() {
		return active;
	}

	public void setStatus(boolean status) {
		this.active = status;
	}

	public boolean isTrab() {
		return trab;
	}

	public void setTrab(boolean trab) {
		this.trab = trab;
	}

	public boolean isPaciente() {
		return paciente;
	}

	public void setPaciente(boolean paciente) {
		this.paciente = paciente;
	}

	public boolean isIncompleto() {
		return incompleto;
	}

	public void setIncompleto(boolean incompleto) {
		this.incompleto = incompleto;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getAniversario() {
		return aniversario;
	}

	public void setAniversario(String aniversario) {
		this.aniversario = aniversario;
	}

	@Override
	public String toString() {
		return nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cpf == null) ? 0 : cpf.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Leitor other = (Leitor) obj;
		if (cpf == null) {
			if (other.cpf != null) {
				return false;
			}
		} else if (!cpf.equals(other.cpf)) {
			return false;
		}
		if (nome == null) {
			if (other.nome != null) {
				return false;
			}
		} else if (!nome.equals(other.nome)) {
			return false;
		}
		return true;
	}
}
