package com.heriberto.invex.services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.heriberto.invex.entities.Empleado;
import com.heriberto.invex.entities.EmpleadoDTO;
import com.heriberto.invex.exceptions.IdNotFound;
import com.heriberto.invex.exceptions.DatabaseException;
import com.heriberto.invex.exceptions.InvalidDateFormatException;
import com.heriberto.invex.repositories.EmpleadoRepository;

@Service
public class EmpleadoServiceImpl implements EmpleadoService {
@Autowired
    private EmpleadoRepository empleadoRepository;

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImpl.class);


    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    // Obtener todos los empleados
    @Override
    @Transactional(readOnly = true)
    public List<Empleado> findAll() {
        try {
            List<Empleado> empleados = empleadoRepository.findAll();
            logger.info("Se han obtenido {} empleados.", empleados.size());
            return empleados;

        } catch (Exception e) {
            logger.error("Error al obtener empleados", e);
            throw new DatabaseException("Error al obtener la lista de empleados", e);
        }
    }

    // Obtener empleado por ID
    @Override
    @Transactional(readOnly = true)
    public Empleado findById(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Empleado no encontrado con el id: {}", id);
                    return new IdNotFound("Empleado no encontrado con el id: " + id);
                });
    }

    // Eliminar empleados por ID
    @Override
    @Transactional
    public void delete(Long id) {
        if (empleadoRepository.existsById(id)) {
            empleadoRepository.deleteById(id);
        } else {
            throw new IdNotFound("Empleado no encontrado con el id: " + id);
        }
    }

    // Actualizar datos del empleado por ID
    @Override
    @Transactional
    public Empleado actualizarEmpleado(Long id, EmpleadoDTO empleadoDTO) {
        // Buscar si el empleado existe
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new IdNotFound("Empleado no encontrado con el id: " + id));

        // Actualizar los campos con los datos recibidos del DTO
        if (empleadoDTO.getPrimer_nombre() != null)
            empleado.setPrimer_nombre(empleadoDTO.getPrimer_nombre());
        if (empleadoDTO.getSegundo_nombre() != null)
            empleado.setSegundo_nombre(empleadoDTO.getSegundo_nombre());
        if (empleadoDTO.getApellido_paterno() != null)
            empleado.setApellido_paterno(empleadoDTO.getApellido_paterno());
        if (empleadoDTO.getApellido_materno() != null)
            empleado.setApellido_materno(empleadoDTO.getApellido_materno());
        if (empleadoDTO.getEdad() != null)
            empleado.setEdad(empleadoDTO.getEdad());
        if (empleadoDTO.getSexo() != null)
            empleado.setSexo(empleadoDTO.getSexo());
        if (empleadoDTO.getFecha_nacimiento() != null) {
            try {
                LocalDate fechaNacimiento = LocalDate.parse(empleadoDTO.getFecha_nacimiento(), formatter);
                empleado.setFecha_nacimiento(fechaNacimiento); // Ahora pasamos el LocalDate
            } catch (DateTimeParseException e) {
                // Manejo del error en caso de que el String no siga el formato esperado
                throw new InvalidDateFormatException("Formato de fecha inválido: " + empleadoDTO.getFecha_nacimiento()
                        + ". El formato esperado es dd-MM-yyyy.", e);
            }

        }
        if (empleadoDTO.getPuesto() != null)
            empleado.setPuesto(empleadoDTO.getPuesto());

        // Guardar los cambios en la base de datos
        return empleadoRepository.save(empleado);
    }

    // Insertar nuevos empleados
    @Override
    @Transactional
    public List<Empleado> insertarEmpleados(List<EmpleadoDTO> empleadosDTO) {
        if (empleadosDTO == null || empleadosDTO.isEmpty()) {
            logger.warn("Intento de insertar una lista vacía de empleados");
            throw new IllegalArgumentException("La lista de empleados no puede estar vacía");
        }

        // Convertir los DTO a entidades Empleado
        List<Empleado> empleados = empleadosDTO.stream().map(dto -> {
            Empleado empleado = new Empleado();
            empleado.setPrimer_nombre(dto.getPrimer_nombre());
            empleado.setSegundo_nombre(dto.getSegundo_nombre());
            empleado.setApellido_paterno(dto.getApellido_paterno());
            empleado.setApellido_materno(dto.getApellido_materno());
            empleado.setEdad(dto.getEdad());
            empleado.setSexo(dto.getSexo());
            try {
                // Parsear la fecha de String a LocalDate
                LocalDate fechaNacimiento = LocalDate.parse(dto.getFecha_nacimiento(), formatter);
                empleado.setFecha_nacimiento(fechaNacimiento);
            } catch (DateTimeParseException e) {
                // Manejo del error en caso de que el String no siga el formato esperado
                throw new InvalidDateFormatException("El formato esperado es dd-MM-yyyy.", e);
            }
            empleado.setPuesto(dto.getPuesto());
            return empleado;
        }).collect(Collectors.toList());

        // Guardar los empleados en la base de datos
        List<Empleado> empleadosGuardados = empleadoRepository.saveAll(empleados);

        // Retornar los empleados guardados
        return empleadosGuardados;
}

    

}