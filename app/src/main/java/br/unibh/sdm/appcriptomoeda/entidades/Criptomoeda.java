package br.unibh.sdm.appcriptomoeda.entidades;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class Criptomoeda implements Serializable {

    private String codigo;
    private String nome;
    private String descricao;
    private Date dataCriacao;

    public Criptomoeda() {
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "Criptomoeda{" +
                "codigo='" + codigo + '\'' +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", dataCriacao=" + dataCriacao +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Criptomoeda)) return false;
        Criptomoeda that = (Criptomoeda) o;
        return Objects.equals(getCodigo(), that.getCodigo()) &&
                Objects.equals(getNome(), that.getNome()) &&
                Objects.equals(getDescricao(), that.getDescricao()) &&
                Objects.equals(getDataCriacao(), that.getDataCriacao());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCodigo(), getNome(), getDescricao(), getDataCriacao());
    }
}