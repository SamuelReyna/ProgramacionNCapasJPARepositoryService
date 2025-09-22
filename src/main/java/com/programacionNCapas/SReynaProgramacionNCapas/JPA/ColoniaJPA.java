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

@Entity(name = "colonia")
@Table(name = "colonia")
public class ColoniaJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idcolonia")
    private int IdColonia;
    @Column(name = "codigopostal")
    private String CodigoPostal;
    @Column(name = "nombre")
    private String Nombre;
    @ManyToOne
    @JoinColumn(name = "idmunicipio")
    public MunicipioJPA municipio;


    public ColoniaJPA() {
    }

    public int getIdColonia() {
        return IdColonia;
    }

    public void setIdColonia(int IdColonia) {
        this.IdColonia = IdColonia;
    }

    public String getCodigoPostal() {
        return CodigoPostal;
    }

    public void setCodigoPostal(String CodigoPostal) {
        this.CodigoPostal = CodigoPostal;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public ColoniaJPA(int IdColonia, String CodigoPostal, String Nombre, MunicipioJPA Municipio) {
        this.IdColonia = IdColonia;
        this.CodigoPostal = CodigoPostal;
        this.Nombre = Nombre;
        this.municipio = Municipio;
    }
}
