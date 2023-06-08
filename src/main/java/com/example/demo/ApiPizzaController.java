package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/pizza/v1")
public class ApiPizzaController {
	@Autowired
	private PizzaService pizzaService;
	@Autowired
	private IngredienteService ingredienteService;
	@Autowired
	private OffertaService offertaService;

	// INDEX
	@GetMapping("/pizze")
	public ResponseEntity<List<Pizza>> getPizze() {
		List<Pizza> pizze = pizzaService.findAll();
		return new ResponseEntity<>(pizze, HttpStatus.OK);
	}

	// FILTRO PIZZE PER NOME http://localhost:8080/api/pizza/v1/pizze/filter?nome=
	@GetMapping("/pizze/filter")
	public ResponseEntity<List<Pizza>> getPizze(@RequestParam(required = false) String nome) {
		List<Pizza> pizze;
		if (nome != null && !nome.isEmpty()) {
			pizze = pizzaService.findByNome(nome);
		} else {
			pizze = pizzaService.findAll();
		}
		return new ResponseEntity<>(pizze, HttpStatus.OK);
	}

	// SHOW
	@GetMapping("/pizze/{pizzaId}")
	public ResponseEntity<Pizza> getPizzaById(@PathVariable int pizzaId) {
		Optional<Pizza> pizzaOpt = pizzaService.findById(pizzaId);
		if (pizzaOpt.isPresent()) {
			Pizza pizza = pizzaOpt.get();
			return new ResponseEntity<>(pizza, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	// CREATE
	@PostMapping("/pizze/create")
	public ResponseEntity<Pizza> createPizza(@RequestBody Pizza pizza) {
	    List<Ingrediente> ingredienti = pizza.getIngredienti();
	    if (ingredienti != null) {
	        for (Ingrediente ingrediente : ingredienti) {
	            ingredienteService.save(ingrediente); // Salva gli ingredienti, se necessario
	        }
	    }
	    
	    List<Offerta> offerte = pizza.getOfferte();
	    if (offerte != null) {
	        for (Offerta offerta : offerte) {
	            offertaService.save(offerta); // Salva le offerte, se necessario
	        }
	    }
	    
	    Pizza savedPizza = pizzaService.save(pizza);
	    System.out.println(pizza);
	    return new ResponseEntity<>(savedPizza, HttpStatus.CREATED);
	}
	
	@PostMapping("/pizze/{pizzaId}/offerte/new")
	public ResponseEntity<Offerta> createOfferta(@PathVariable int pizzaId, @RequestBody Offerta offerta) {
	    Optional<Pizza> pizzaOptional = pizzaService.findById(pizzaId);

	    if (pizzaOptional.isPresent()) {
	        Pizza pizza = pizzaOptional.get();

	        // Crea una nuova istanza di Offerta
	        Offerta nuovaOfferta = new Offerta();
	        nuovaOfferta.setDataInizio(offerta.getDataInizio());
	        nuovaOfferta.setDataFine(offerta.getDataFine());
	        nuovaOfferta.setTitolo(offerta.getTitolo());
	        nuovaOfferta.setPercentualeSconto(offerta.getPercentualeSconto());
	        nuovaOfferta.setPizza(pizza);

	        Offerta savedOfferta = offertaService.save(nuovaOfferta);
	        return new ResponseEntity<>(savedOfferta, HttpStatus.CREATED);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}




	/*
	 * @PostMapping("/pizze/create") public ResponseEntity<PizzaCreateResponse>
	 * createPizza(@RequestBody Pizza pizza, BindingResult bindingResult) { if
	 * (bindingResult.hasErrors()) { return new ResponseEntity<>(new
	 * PizzaCreateResponse(bindingResult), HttpStatus.BAD_REQUEST); } pizza =
	 * pizzaService.save(pizza); return new ResponseEntity<>(new
	 * PizzaCreateResponse(pizza), HttpStatus.OK); }
	 */

	// UPDATE http://localhost:8080/api/pizza/v1/pizze/update/199
	@PutMapping("/pizze/update/{pizzaId}")
	public ResponseEntity<Pizza> updatePizza(@PathVariable int pizzaId,
	        @RequestBody Pizza updatedPizza) {
	    Optional<Pizza> pizzaOpt = pizzaService.findById(pizzaId);
	    if (pizzaOpt.isPresent()) {
	        Pizza existingPizza = pizzaOpt.get();
	        existingPizza.setNome(updatedPizza.getNome());
	        existingPizza.setDescrizione(updatedPizza.getDescrizione());
	        existingPizza.setFoto(updatedPizza.getFoto());
	        existingPizza.setPrezzo(updatedPizza.getPrezzo());

	        List<Ingrediente> updatedIngredienti = updatedPizza.getIngredienti();
	        if (updatedIngredienti != null) {
	            existingPizza.getIngredienti().clear(); // Rimuovi gli ingredienti esistenti
	            existingPizza.getIngredienti().addAll(updatedIngredienti); // Aggiungi gli ingredienti nuovi
	        } else {
	            existingPizza.getIngredienti().clear(); // Se non sono stati forniti ingredienti, rimuovi tutti gli ingredienti
	        }

	        Pizza savedPizza = pizzaService.save(existingPizza);
	        return new ResponseEntity<>(savedPizza, HttpStatus.OK);
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}

	@PostMapping("/pizze/{pizzaId}/offerte/{offertaId}/edit")
	public ResponseEntity<Offerta> updateOfferta(@PathVariable int pizzaId, @PathVariable int offertaId, @RequestBody Offerta updatedOfferta) {
	    Optional<Pizza> pizzaOptional = pizzaService.findById(pizzaId);
	    if (pizzaOptional.isPresent()) {
	        Pizza pizza = pizzaOptional.get();

	        Optional<Offerta> offertaOptional = offertaService.findById(offertaId);
	        if (offertaOptional.isPresent()) {
	            Offerta offertaPersistente = offertaOptional.get();

	            offertaPersistente.setDataInizio(updatedOfferta.getDataInizio());
	            offertaPersistente.setDataFine(updatedOfferta.getDataFine());
	            offertaPersistente.setTitolo(updatedOfferta.getTitolo());
	            offertaPersistente.setPercentualeSconto(updatedOfferta.getPercentualeSconto());
	            offertaPersistente.setPizza(pizza);

	            Offerta savedOfferta = offertaService.save(offertaPersistente);
	            return new ResponseEntity<>(savedOfferta, HttpStatus.OK);
	        } else {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }
	    } else {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	}








	// DELETE
	@DeleteMapping("/pizze/{id}")
	public ResponseEntity<Void> deletePizza(@PathVariable int id) {
	    Optional<Pizza> optPizza = pizzaService.findById(id);
	    if (optPizza.isEmpty()) {
	        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	    Pizza pizza = optPizza.get();
	    pizzaService.delete(pizza);
	    return new ResponseEntity<>(HttpStatus.OK);
	}


}
