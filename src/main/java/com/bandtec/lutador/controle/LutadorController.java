package com.bandtec.lutador.controle;

import com.bandtec.lutador.dominio.Lutador;
import com.bandtec.lutador.repositorio.LutadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
        List<Lutador> lutadore = repository.findByOrderByForcaGolpeDesc();

        if(lutadore.isEmpty()){
            return ResponseEntity.status(204).build();
        }
        else{
            return ResponseEntity.status(200).body(lutadore);
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
    public ResponseEntity postGolpear(@RequestBody Integer idLutadorBate, Integer idLutadorApanha){
        if(repository.existsById(idLutadorApanha) && repository.existsById(idLutadorBate)){
            Lutador lutador1 = repository.findById(idLutadorBate).get();
            Lutador lutador2 = repository.findById(idLutadorApanha).get();
            lutador.setVida(lutador.getVida() * 1.15);
            lutador.setConcentracaoRealizada(lutador.getConcentracaoRealizada()+1);
            repository.save(lutador);
            return ResponseEntity.status(201).build(lutador1,lutador2);
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
