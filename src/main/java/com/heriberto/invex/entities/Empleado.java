package com.heriberto.invex.entities;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "empleado")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "El primer nombre es obligatorio")
    @Column(name = "primerNombre", length = 15)
    private String primer_nombre;

    @Column(name = "segundoNombre", length = 15)
    private String segundo_nombre;

    @NotNull(message = "El apellido paterno es obligatorio")
    @Column(name = "apellidoPaterno", length = 15)
    private String apellido_paterno;

    @NotNull(message = "El apellido materno es obligatorio")
    @Column(name = "apellidoMaterno", length = 15)
    private String apellido_materno;

    @NotNull(message = "La edad es obligatoria")
    @Column(name = "edad")
    private Integer edad;

    @NotNull(message = "El sexo es obligatorio")
    @Column(name = "sexo", length = 1)
    private String sexo;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Column(name = "fechaNacimiento")
    private LocalDate fecha_nacimiento;

    @NotNull(message = "El puesto es obligatorio")
    @Column(name = "puesto", length = 15)
    private String puesto;

    public Empleado() {

    }

    public Empleado(Long id, @NotNull(message = "El primer nombre es obligatorio") String primer_nombre,
            String segundo_nombre, @NotNull(message = "El apellido paterno es obligatorio") String apellido_paterno,
            @NotNull(message = "El apellido materno es obligatorio") String apellido_materno,
            @NotNull(message = "La edad es obligatoria") Integer edad,
            @NotNull(message = "El sexo es obligatorio") String sexo,
            @NotNull(message = "La fecha de nacimiento es obligatoria") LocalDate fecha_nacimiento,
            @NotNull(message = "El puesto es obligatorio") String puesto) {
        this.id = id;
        this.primer_nombre = primer_nombre;
        this.segundo_nombre = segundo_nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.edad = edad;
        this.sexo = sexo;
        this.fecha_nacimiento = fecha_nacimiento;
        this.puesto = puesto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrimer_nombre() {
        return primer_nombre;
    }

    public void setPrimer_nombre(String primer_nombre) {
        this.primer_nombre = primer_nombre;
    }

    public String getSegundo_nombre() {
        return segundo_nombre;
    }

    public void setSegundo_nombre(String segundo_nombre) {
        this.segundo_nombre = segundo_nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public LocalDate getFecha_nacimiento() {
        return fecha_nacimiento;
    }

    public void setFecha_nacimiento(LocalDate fecha_nacimiento) {
        this.fecha_nacimiento = fecha_nacimiento;
    }

    public String getPuesto() {
        return puesto;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

}
