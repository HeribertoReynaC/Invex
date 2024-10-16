package com.heriberto.invex.controllers;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heriberto.invex.entities.Empleado;
import com.heriberto.invex.entities.EmpleadoDTO;
import com.heriberto.invex.exceptions.IdNotFound;
import com.heriberto.invex.exceptions.InvalidDateFormatException;
import com.heriberto.invex.exceptions.DatabaseException;
import javax.validation.ConstraintViolationException;
import com.heriberto.invex.services.EmpleadoService;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    @Autowired
    private EmpleadoService empleadoService;
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoController.class);

    /**
     * Obtener todos los empleados.
     * 
     * @return ResponseEntity con la lista de empleados y el estado HTTP OK (200),
     *         o un estado HTTP de error en caso de excepción.
     */
    @GetMapping
    public ResponseEntity<List<Empleado>> obtenerEmpleados() {
        try {
            List<Empleado> empleados = empleadoService.findAll();
            return new ResponseEntity<>(empleados, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtener un empleado por su ID.
     * 
     * @param id El ID del empleado a buscar.
     * @return ResponseEntity con el empleado encontrado y el estado HTTP OK (200),
     *         o un estado HTTP NOT_FOUND (404) si no se encuentra el empleado,
     *         o un estado HTTP de error en caso de excepción.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Empleado> obtenerEmpleadoPorId(@PathVariable("id") Long id) {
        try {
            Empleado empleado = empleadoService.findById(id);
            return new ResponseEntity<>(empleado, HttpStatus.OK);
        } catch (IdNotFound e) {
            logger.error("Empleado no encontrado con id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error interno al obtener el empleado con id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Eliminar un empleado por su ID.
     * 
     * @param id El ID del empleado a eliminar.
     * @return ResponseEntity con el estado HTTP NO_CONTENT (204) si la eliminación
     *         fue exitosa,
     *         o un estado HTTP NOT_FOUND (404) si no se encuentra el empleado,
     *         o un estado HTTP de error en caso de excepción.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEmpleado(@PathVariable("id") Long id) {
        try {
            empleadoService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content si la eliminación fue exitosa
        } catch (IdNotFound e) {
            logger.error("Empleado no encontrado con id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); // 404 Not Found si el empleado no fue encontrado
        } catch (Exception e) {
            logger.error("Error interno al eliminar el empleado con id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error si ocurre algún
                                                                           // problema inesperado
        }
    }

    /**
     * Actualizar un empleado existente.
     * 
     * @param id                El ID del empleado a actualizar.
     * @param empleadoUpdateDTO Los datos del empleado actualizados.
     * @return ResponseEntity con el empleado actualizado y el estado HTTP OK (200),
     *         o un estado HTTP NOT_FOUND (404) si no se encuentra el empleado,
     *         o un estado HTTP de error en caso de excepción.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Empleado> actualizarEmpleado(
            @PathVariable("id") Long id,
            @RequestBody EmpleadoDTO empleadoUpdateDTO) {
        try {
            Empleado empleadoActualizado = empleadoService.actualizarEmpleado(id, empleadoUpdateDTO);
            return new ResponseEntity<>(empleadoActualizado, HttpStatus.OK);
        } catch (InvalidDateFormatException e) {
            logger.error("Body error", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IdNotFound e) {
            logger.error("Empleado no encontrado con id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Error interno al actualizar el empleado con id: {}", id, e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Insertar uno o varios empleados.
     * 
     * @param empleados La lista de empleados a insertar.
     * @return ResponseEntity con la lista de empleados guardados y el estado HTTP
     *         CREATED (201) si la inserción fue exitosa,
     *         o un estado HTTP BAD_REQUEST (400) si los datos son inválidos,
     *         o un estado HTTP de error en caso de excepción.
     */
    @PostMapping
    public ResponseEntity<List<Empleado>> insertarEmpleados(@RequestBody @Valid List<EmpleadoDTO> empleadosDTO) {
        try {
            // Validar que la lista de empleados no esté vacía
            if (empleadosDTO == null || empleadosDTO.isEmpty()) {
                logger.warn("Intento de insertar una lista vacía de empleados");
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Respuesta 400 si la lista está vacía o nula
            }

            // Llamar al servicio para insertar empleados
            List<Empleado> empleadosGuardados = empleadoService.insertarEmpleados(empleadosDTO);

            return new ResponseEntity<>(empleadosGuardados, HttpStatus.CREATED); // 201 Created si la inserción fue
                                                                                 // exitosa
        } catch (IllegalArgumentException | DatabaseException | InvalidDateFormatException | ConstraintViolationException e) {
            // Si ocurre un error por datos no válidos
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // 400 Bad Request si hay algún argumento no válido
        } catch (Exception e) {
            // Cualquier otro error no esperado
            logger.error("error", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); // 500 Internal Server Error por defecto
        }
    }

}
