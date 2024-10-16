package com.heriberto.invex.services;

import java.util.List;

import com.heriberto.invex.entities.Empleado;
import com.heriberto.invex.entities.EmpleadoDTO;

public interface EmpleadoService {

    List<Empleado> findAll();

    Empleado findById(Long id);

    void delete(Long id);

    Empleado actualizarEmpleado(Long id, EmpleadoDTO empleadoDTO);

    List<Empleado> insertarEmpleados (List<EmpleadoDTO> empleadosDTO);

}
