package com.programacionNCapas.SReynaProgramacionNCapas.JPA;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "estado")
@Table(name = "estado")
public class EstadoJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idestado")
    private int idEstado;
    @Column(name = "nombre")
    private String Nombre;
    @ManyToOne
    @JoinColumn(name = "idpais")
    public PaisJPA pais;

    public EstadoJPA() {
    }

    public EstadoJPA(int IdEstado, String Nombre) {
        this.idEstado = IdEstado;
        this.Nombre = Nombre;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int IdEstado) {
        this.idEstado = IdEstado;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }
    

}
