package com.bandtec.lutador.repositorio;

import com.bandtec.lutador.dominio.Lutador;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LutadorRepository extends JpaRepository<Lutador, Integer> {
    List<Lutador> findByOrderByForcaGolpeDesc();

    Long countByVivoTrue();

    Long countByVivoFalse();
}
