package org.casadeguara.builder;

import org.casadeguara.entidades.Leitor;

/**
 * Builder para a classe Leitor.
 * @author Gustavo
 * @since 3.0
 */
public class LeitorBuilder {
	
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
    private boolean ativo; 
    private boolean trabalhador; 
    private boolean paciente; 
    private boolean incompleto;
    
	public LeitorBuilder(String nome) {
		
		this.id = 0;
		this.nome = nome;
		this.email = null;
		this.telefone1 = null;
		this.telefone2 = null;
		this.logradouro = null;
		this.bairro = null;
		this.complemento = null;
		this.cep = null;
		this.cidade = null;
		this.sexo = null;
		this.rg = null;
		this.cpf = null;
		this.aniversario = null;
		this.ativo = true;
		this.trabalhador = false;
		this.paciente = false;
		this.incompleto = true;
	}

	public LeitorBuilder id(int id) {
		if(id > 0) {
			this.id = id;
		}
		return this;
	}
	
    public LeitorBuilder email(String email) {
    	if(email != null && !email.isEmpty()) {
    		this.email = email;
    	}
		return this;
    }
    
    public LeitorBuilder telefone1(String telefone1) {
    	if(telefone1 != null && !telefone1.isEmpty()) {
    		this.telefone1 = telefone1;
    	}
		return this;
    }
    
    public LeitorBuilder telefone2(String telefone2) {
    	if(telefone2 != null && !telefone2.isEmpty()) {
    		this.telefone2 = telefone2;
    	}
		return this;
    }
    
    public LeitorBuilder logradouro(String logradouro) {
    	if(logradouro != null && !logradouro.isEmpty()) {
    		this.logradouro = logradouro;
    	}
		return this;
    }
    
    public LeitorBuilder bairro(String bairro) {
    	if(bairro != null && !bairro.isEmpty()) {
    		this.bairro = bairro;
    	}
		return this;
    }
    
    public LeitorBuilder complemento(String complemento) {
    	if(complemento != null && !complemento.isEmpty()) {
    		this.complemento = complemento;
    	}
		return this;
    }
    
    public LeitorBuilder cep(String cep) {
    	if(cep != null && !cep.isEmpty()) {
    		this.cep = cep;
    	}
		return this;
    }
    
    public LeitorBuilder cidade(String cidade) {
    	this.cidade = cidade;
		return this;
    }
    
    public LeitorBuilder sexo(String sexo) {
    	this.sexo = sexo;
		return this;
    }
    
    public LeitorBuilder rg(String rg) {
    	if(rg != null && !rg.isEmpty()) {
    		this.rg = rg;
    	}
		return this;
    }
    
    public LeitorBuilder cpf(String cpf) {
    	if(cpf != null && !cpf.isEmpty()) {
    		this.cpf = cpf;
    	}
		return this;
    }
    
    public LeitorBuilder aniversario(String aniversario) {
    	if(aniversario != null && !aniversario.isEmpty()) {
    		this.aniversario = aniversario;
    	}
		return this;
    }
    
    public LeitorBuilder isAtivo(boolean ativo) {
		this.ativo = ativo;
    	return this;
    }

    public LeitorBuilder isTrabalhador(boolean trabalhador) {
    	this.trabalhador = trabalhador;
		return this;
    }
    
    public LeitorBuilder isPaciente(boolean paciente) {
    	this.paciente = paciente;
    	return this;
    } 
    
    public LeitorBuilder isIncompleto(boolean incompleto) {
    	this.incompleto = incompleto;
		return this;
    }

	public Leitor build() {
		return new Leitor(id, 
					nome,
					email,
					telefone1,
					telefone2,
					logradouro,
					bairro,
					complemento,
					cep,
					cidade,
					sexo,
					rg,
					cpf,
					aniversario,
					ativo,
					trabalhador,
					paciente,
					incompleto);
	}

}
