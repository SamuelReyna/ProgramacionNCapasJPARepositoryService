package com.programacionNCapas.SReynaProgramacionNCapas.JPA;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity(name="pais")
@Table(name = "pais")
public class PaisJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idpais")
    private int IdPais;
    @Column(name = "nombre")
    private String Nombre;

    public PaisJPA() {
    }

    public PaisJPA(int IdPais, String Nombre) {
        this.IdPais = IdPais;
        this.Nombre = Nombre;
    }

    public int getIdPais() {
        return IdPais;
    }

    public void setIdPais(int IdPais) {
        this.IdPais = IdPais;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

}
