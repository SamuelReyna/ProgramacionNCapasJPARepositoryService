package com.programacionNCapas.SReynaProgramacionNCapas.JPA;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "municipio")
@Table(name = "municipio")
public class MunicipioJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idmunicipio")
    private int idMunicipio;
    @Column(name = "nombre")
    private String Nombre;
    @ManyToOne
    @JoinColumn(name = "idestado")
    public EstadoJPA estado;

    public int getIdMunicipio() {
        return idMunicipio;
    }

    public void setIdMunicipio(int IdMunicipio) {
        this.idMunicipio = IdMunicipio;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }


    public MunicipioJPA(int IdMunicipio, String Nombre, EstadoJPA Estado) {
        this.idMunicipio = IdMunicipio;
        this.Nombre = Nombre;
        this.estado = Estado;
    }

    public MunicipioJPA() {
    }

}
