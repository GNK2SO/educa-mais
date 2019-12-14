package br.com.projeto.educamais.domain;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class EntidadeAuditavel implements Serializable {

    private static final long serialVersionUID = -4707221236917915815L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Version
    private Long versao;

    @CreatedDate
    private LocalDate dataCriacao;

    @LastModifiedDate
    private LocalDate dataUltimaModificacao;

    @CreatedBy
    @ManyToOne
    @JoinColumn
    @JsonIgnore
    private Usuario criadoPor;

    @LastModifiedBy
    @ManyToOne
    @JoinColumn
    private Usuario ultimaModificacaoPor;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getVersao() {
		return versao;
	}

	public void setVersao(Long versao) {
		this.versao = versao;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDate getDataUltimaModificacao() {
		return dataUltimaModificacao;
	}

	public void setDataUltimaModificacao(LocalDate dataUltimaModificacao) {
		this.dataUltimaModificacao = dataUltimaModificacao;
	}

	public Usuario getCriadoPor() {
		return criadoPor;
	}

	public void setCriadoPor(Usuario criadoPor) {
		this.criadoPor = criadoPor;
	}

	public Usuario getUltimaModificacaoPor() {
		return ultimaModificacaoPor;
	}

	public void setUltimaModificacaoPor(Usuario ultimaModificacaoPor) {
		this.ultimaModificacaoPor = ultimaModificacaoPor;
	}
}