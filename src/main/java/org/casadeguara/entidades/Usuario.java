package org.casadeguara.entidades;

import java.util.List;

/**
 * Esta classe representa um usuário do sistema.
 * 
 * @author Gustavo
 * @since 1.0
 */
public class Usuario {

    private int idusuario;
    private String nome;
    private String login;
    private String senha;
    private String tipo;
    
    private List<Integer> listaAcessos;

    private boolean status;
    
    public Usuario() {
        this(0, "", "", "", "", null, true);
    }
    
    public Usuario(int idusuario, String nome) {
        this(idusuario, nome, "", "", "", null, true);
    }

    public Usuario(int idusuario, String nome, String login, String senha,
            String tipo, List<Integer> listaAcessos, boolean status) {
        this.idusuario = idusuario;
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.tipo = tipo;
        this.listaAcessos = listaAcessos;
        this.status = status;
    }

    public int getId() {
        return idusuario;
    }

    public List<Integer> getListaAcessos() {
        return listaAcessos;
    }
    
    public String getLogin() {
        return login;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public boolean getStatus() {
        return status;
    }

    public String getTipo() {
        return tipo;
    }

    public void setId(int id) {
        this.idusuario = id;
    }

    public void setListaAcessos(List<Integer> listaAcessos) {
        this.listaAcessos = listaAcessos;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
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
        Usuario other = (Usuario) obj;
        if (nome == null) {
            if (other.nome != null) {
                return false;
            }
        } else if (!nome.equals(other.nome)) {
            return false;
        }
        return true;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((nome == null) ? 0 : nome.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return nome;
    }
}
