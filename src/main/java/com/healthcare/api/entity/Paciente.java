package com.healthcare.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "pacientes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "usuario_id", unique = true)
    private Usuario usuario;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 14)
    private String cpf;

    @Column(nullable = false)
    private LocalDate nascimento;

    @Column(length = 20)
    private String telefone;

    @Column(length = 255)
    private String endereco;

    // Preenchido apenas quando o paciente é menor de idade
    @Column(name = "responsavel_nome", length = 150)
    private String responsavelNome;

    @Column(name = "responsavel_cpf", length = 14)
    private String responsavelCpf;

    @Column(name = "responsavel_telefone", length = 20)
    private String responsavelTelefone;

    @Column(nullable = false)
    @Builder.Default
    private Boolean ativo = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // Regra de negócio: paciente menor de idade precisa de responsável
    public boolean isMenorDeIdade() {
        return this.nascimento.plusYears(18).isAfter(LocalDate.now());
    }
}