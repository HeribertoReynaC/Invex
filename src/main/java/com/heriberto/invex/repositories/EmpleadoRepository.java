package com.heriberto.invex.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.heriberto.invex.entities.Empleado;

@Repository
public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

}
