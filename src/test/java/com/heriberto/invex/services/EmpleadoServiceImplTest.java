package com.heriberto.invex.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import com.heriberto.invex.entities.Empleado;
import com.heriberto.invex.entities.EmpleadoDTO;
import com.heriberto.invex.exceptions.DatabaseException;
import com.heriberto.invex.exceptions.IdNotFound;
import com.heriberto.invex.exceptions.InvalidDateFormatException;
import com.heriberto.invex.repositories.EmpleadoRepository;

@SpringBootTest
public class EmpleadoServiceImplTest {

    private Empleado Empleado1;
    private Empleado Empleado2;
    private EmpleadoDTO empleadoDTO;
    private EmpleadoDTO empleadoDTO2;
    private static final Logger logger = LoggerFactory.getLogger(EmpleadoServiceImplTest.class);


    @Mock
    private EmpleadoRepository empleadoRepository;


    @InjectMocks
    private EmpleadoServiceImpl empleadoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Empleado1 = new Empleado(1L, "Heriberto","" ,"Reyna", "Castro", 26, "Masculino", LocalDate.of(1998,07,8) , "Desarrollador");
        Empleado2 = new Empleado(2L, "Ana","Christina" ,"Reyna", "Castro", 26, "Femenino", LocalDate.of(1993,04,3) , "Desarrollador");

        empleadoDTO = new EmpleadoDTO();
        empleadoDTO.setPrimer_nombre("Juan");
        empleadoDTO.setSegundo_nombre("Antonio");
        empleadoDTO.setApellido_paterno("Perez");
        empleadoDTO.setApellido_materno("Lopez");
        empleadoDTO.setEdad(25);
        empleadoDTO.setSexo("Masculino");
        empleadoDTO.setFecha_nacimiento("03-04-1990");
        empleadoDTO.setPuesto("CEO");

        empleadoDTO2 = new EmpleadoDTO();
        empleadoDTO2.setPrimer_nombre("Juan");
        empleadoDTO2.setApellido_paterno("Perez");
        empleadoDTO2.setApellido_materno("Lopez");
        empleadoDTO2.setEdad(25);
        empleadoDTO2.setSexo("Masculino");
        empleadoDTO2.setFecha_nacimiento("03-04-1990");
        empleadoDTO2.setPuesto("CEO");

    }


    //findAll()
    @Test
    void testFindAllSuccess() {
        // Datos de prueba
        List<Empleado> empleados = new ArrayList<>();
        empleados.add(Empleado1);
        empleados.add(Empleado2);

       // Simulación del comportamiento del repositorio
        when(empleadoRepository.findAll()).thenReturn(empleados);

        // Llamada al método del servicio
        List<Empleado> result = empleadoService.findAll();

        // Verificaciones
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(empleadoRepository, times(1)).findAll();
    }

    @Test
    void testFindAllThrowsDatabaseException() {
        // Simular una excepción al interactuar con el repositorio
        when(empleadoRepository.findAll()).thenThrow(new RuntimeException("Error en la base de datos"));

        // Verificar que se lanza la excepción personalizada
        DatabaseException exception = assertThrows(DatabaseException.class, () -> {
            empleadoService.findAll();
        });

        // Verificaciones
        assertEquals("Error al obtener la lista de empleados", exception.getMessage());
        verify(empleadoRepository, times(1)).findAll();
    }

    //findbyId()
    @Test
    void testFindById_Success() {
        // Arrange
        Long id = 1L;
        when(empleadoRepository.findById(id)).thenReturn(Optional.of(Empleado1));

        // Act
        Empleado empleado = empleadoService.findById(id);

        // Assert
        assertNotNull(empleado);
        assertEquals(Empleado1.getPrimer_nombre(), empleado.getPrimer_nombre());
        assertEquals(Empleado1.getEdad(), empleado.getEdad());
        verify(empleadoRepository, times(1)).findById(id);
    }

    @Test
    void testFindById_ThrowsIdNotFound() {
        // Arrange
        Long id = 1L;
        when(empleadoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        IdNotFound exception = assertThrows(IdNotFound.class, () -> empleadoService.findById(id));
        assertEquals("Empleado no encontrado con el id: " + id, exception.getMessage());
        verify(empleadoRepository, times(1)).findById(id);
        logger.error("Empleado no encontrado con el id: {}", id);
    }


    //DeleteById
    @Test
    void testDelete_Success() {
        // Arrange
        Long id = 1L;
        when(empleadoRepository.existsById(id)).thenReturn(true);

        // Act
        empleadoService.delete(id);

        // Assert
        verify(empleadoRepository, times(1)).existsById(id);
        verify(empleadoRepository, times(1)).deleteById(id);
    }

    @Test
    void testDelete_ThrowsIdNotFound() {
        // Arrange
        Long id = 1L;
        when(empleadoRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        IdNotFound exception = assertThrows(IdNotFound.class, () -> empleadoService.delete(id));
        assertEquals("Empleado no encontrado con el id: " + id, exception.getMessage());
        verify(empleadoRepository, times(1)).existsById(id);
        verify(empleadoRepository, never()).deleteById(id);
    }

    //Actualizar por ID
    @Test
    void testActualizarEmpleado_Success() {
        // Arrange
        Long id = 1L;
        when(empleadoRepository.findById(id)).thenReturn(Optional.of(Empleado1));
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(Empleado1);

        // Act
        Empleado updatedEmpleado = empleadoService.actualizarEmpleado(id, empleadoDTO);

        // Assert
        assertNotNull(updatedEmpleado);
        assertEquals("Juan", updatedEmpleado.getPrimer_nombre());
        assertEquals("Antonio", updatedEmpleado.getSegundo_nombre());
        assertEquals("Perez", updatedEmpleado.getApellido_paterno());
        assertEquals("Lopez", updatedEmpleado.getApellido_materno());
        assertEquals(25, updatedEmpleado.getEdad());
        assertEquals("Masculino", updatedEmpleado.getSexo());
        assertEquals(LocalDate.of(1990, 4, 3), updatedEmpleado.getFecha_nacimiento());
        assertEquals("CEO", updatedEmpleado.getPuesto());

        verify(empleadoRepository, times(1)).findById(id);
        verify(empleadoRepository, times(1)).save(updatedEmpleado);
    }

    @Test
    void testActualizarEmpleado_IdNotFound() {
        // Arrange
        Long id = 1L;
        when(empleadoRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        IdNotFound exception = assertThrows(IdNotFound.class, () -> empleadoService.actualizarEmpleado(id, empleadoDTO));
        assertEquals("Empleado no encontrado con el id: " + id, exception.getMessage());
        verify(empleadoRepository, times(1)).findById(id);
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    void testActualizarEmpleado_InvalidDateFormat() {
        // Arrange
        Long id = 1L;
        empleadoDTO.setFecha_nacimiento("invalid-date");
        when(empleadoRepository.findById(id)).thenReturn(Optional.of(Empleado1));

        // Act & Assert
        InvalidDateFormatException exception = assertThrows(InvalidDateFormatException.class, () -> empleadoService.actualizarEmpleado(id, empleadoDTO));
        assertTrue(exception.getMessage().contains("Formato de fecha inválido"));
        verify(empleadoRepository, times(1)).findById(id);
        verify(empleadoRepository, never()).save(any(Empleado.class));
    }

    @Test
    void testActualizarEmpleado_PartialUpdate() {
        // Arrange
        Long id = 1L;
        empleadoDTO.setPrimer_nombre("Actualizado");
        empleadoDTO.setSegundo_nombre(null); // No actualizar este campo
        when(empleadoRepository.findById(id)).thenReturn(Optional.of(Empleado1));
        when(empleadoRepository.save(any(Empleado.class))).thenReturn(Empleado1);

        // Act
        Empleado updatedEmpleado = empleadoService.actualizarEmpleado(id, empleadoDTO);

        // Assert
        assertNotNull(updatedEmpleado);
        assertEquals("Actualizado", updatedEmpleado.getPrimer_nombre());
        assertEquals("", updatedEmpleado.getSegundo_nombre()); // No debe cambiar
        verify(empleadoRepository, times(1)).findById(id);
        verify(empleadoRepository, times(1)).save(updatedEmpleado);
    }

    //Crear nuevos empleados
    @Test
    void testInsertarEmpleados_Success() {
        // Arrange
        List<EmpleadoDTO> empleadosDTOList = Arrays.asList(empleadoDTO, empleadoDTO2);
        

        when(empleadoRepository.saveAll(anyList())).thenReturn(Arrays.asList(Empleado1, Empleado2));

        // Act
        List<Empleado> empleadosGuardados = empleadoService.insertarEmpleados(empleadosDTOList);

        // Assert
        assertNotNull(empleadosGuardados);
        assertEquals(2, empleadosGuardados.size());
        assertEquals("Heriberto", empleadosGuardados.get(0).getPrimer_nombre());
        assertEquals("Ana", empleadosGuardados.get(1).getPrimer_nombre());
        verify(empleadoRepository, times(1)).saveAll(anyList());
    }

    @Test
    void testInsertarEmpleados_EmptyList() {
        // Arrange
        List<EmpleadoDTO> empleadosDTOList = Collections.emptyList();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> empleadoService.insertarEmpleados(empleadosDTOList));
        assertEquals("La lista de empleados no puede estar vacía", exception.getMessage());
        verify(empleadoRepository, never()).saveAll(anyList());
    }

    @Test
    void testInsertarEmpleados_NullList() {
        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> empleadoService.insertarEmpleados(null));
        assertEquals("La lista de empleados no puede estar vacía", exception.getMessage());
        verify(empleadoRepository, never()).saveAll(anyList());
    }

    @Test
    void testInsertarEmpleados_InvalidDateFormat() {
        // Arrange
        empleadoDTO.setFecha_nacimiento("invalid-date");
        List<EmpleadoDTO> empleadosDTOList = Arrays.asList(empleadoDTO);

        // Act & Assert
        InvalidDateFormatException exception = assertThrows(InvalidDateFormatException.class, () -> empleadoService.insertarEmpleados(empleadosDTOList));
        assertTrue(exception.getMessage().contains("El formato esperado es dd-MM-yyyy"));
        verify(empleadoRepository, never()).saveAll(anyList());
    }

}



