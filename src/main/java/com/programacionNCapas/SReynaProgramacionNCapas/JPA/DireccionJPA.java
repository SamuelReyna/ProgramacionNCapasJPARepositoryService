package com.programacionNCapas.SReynaProgramacionNCapas.JPA;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity(name = "Direccion")
@Table(name = "direccion")
public class DireccionJPA {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iddireccion")
    private int IdDireccion;
    @Column(name = "calle")
    private String Calle;
    @Column(name = "numerointerior")
    private String NumeroInterior;
    @Column(name = "numeroexterior")
    private String NumeroExterior;
    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idusuario", referencedColumnName = "iduser", nullable = false)
    public UsuarioJPA Usuario;
    @ManyToOne
    @JoinColumn(name = "idcolonia")
    public ColoniaJPA Colonia;

    public DireccionJPA(int IdDireccion, String Calle, String NumeroInterior, String NumeroExterior, ColoniaJPA Colonia) {
        this.IdDireccion = IdDireccion;
        this.Calle = Calle;
        this.NumeroInterior = NumeroInterior;
        this.NumeroExterior = NumeroExterior;
        this.Colonia = Colonia;
    }

    public void setUsuario(UsuarioJPA Usuario) {
        this.Usuario = Usuario;
    }

    public void setColonia(ColoniaJPA Colonia) {
        this.Colonia = Colonia;
    }

    public DireccionJPA(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public DireccionJPA() {
    }

    public int getIdDireccion() {
        return IdDireccion;
    }

    public void setIdDireccion(int IdDireccion) {
        this.IdDireccion = IdDireccion;
    }

    public String getCalle() {
        return Calle;
    }

    public void setCalle(String Calle) {
        this.Calle = Calle;
    }

    public String getNumeroInterior() {
        return NumeroInterior;
    }

    public void setNumeroInterior(String NumeroInterior) {
        this.NumeroInterior = NumeroInterior;
    }

    public String getNumeroExterior() {
        return NumeroExterior;
    }

    public void setNumeroExterior(String NumeroExterior) {
        this.NumeroExterior = NumeroExterior;
    }

}
