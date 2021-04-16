package com.bandtec.lutador.controle;

import com.bandtec.lutador.IdLutador;
import com.bandtec.lutador.dominio.Lutador;
import com.bandtec.lutador.repositorio.LutadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/lutadores")
public class LutadorController {
    @Autowired
    LutadorRepository repository;

    @PostMapping
    public ResponseEntity postLutador(@RequestBody @Valid Lutador lutador){
        repository.save(lutador);
        return ResponseEntity.status(201).build();
    }

    @GetMapping
    public ResponseEntity getLutadores(){
        List<Lutador> lutadores = repository.findByOrderByForcaGolpeDesc();

        if(lutadores.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        else{
            return ResponseEntity.status(200).body(lutadores);
        }
    }

    @GetMapping("/contagem-vivos")
    public  ResponseEntity getLutadoresVivos(){
        Long contador = repository.countByVivoTrue();

        return ResponseEntity.status(200).body(contador);
    }

    @PostMapping("{id}/concentrar")
    public ResponseEntity postConcentrar(@PathVariable Integer id){
        if(repository.existsById(id)){
            Lutador lutador = repository.findById(id).get();
            if(lutador.getConcentracaoRealizada() > 3 || lutador.getVida() * 1.15 > 100) {
                return ResponseEntity.status(400).build();
            }
            lutador.setVida(lutador.getVida() * 1.15);
            lutador.setConcentracaoRealizada(lutador.getConcentracaoRealizada()+1);
            repository.save(lutador);
            return ResponseEntity.status(200).build();
        }
        else{
            return ResponseEntity.status(404).build();
        }
    }

    @PostMapping("/golpe")
    public ResponseEntity postGolpear(@RequestBody IdLutador idLutador){
        if(repository.existsById(idLutador.getIdLutadorApanha()) && repository.existsById(idLutador.getIdLutadorBate())){
            Lutador lutador1 = repository.findById(idLutador.getIdLutadorBate()).get();
            Lutador lutador2 = repository.findById(idLutador.getIdLutadorApanha()).get();
            if(lutador1.getVida() <= 0 || lutador2.getVida() <= 0){
                return ResponseEntity.status(400).build();
            }
            lutador2.setVida(lutador2.getVida() - lutador2.getForcaGolpe());

            if(lutador2.getVida() < 0){
                lutador2.setVida(0.0);
            }
            List<Lutador> lutadores = new ArrayList<>();
            lutadores.add(lutador1);
            lutadores.add(lutador2);
            if (lutador2.getVida() == 0){
                lutador2.setVivo(false);
            }
            repository.save(lutador2);

            return ResponseEntity.status(201).body(lutadores);
        }
        else{
            return ResponseEntity.status(404).build();
        }
    }


    @GetMapping("/contagem-mortos")
    public  ResponseEntity getLutadoresMortos(){
        Long contador = repository.countByVivoFalse();

        return ResponseEntity.status(200).body(contador);
    }
}
