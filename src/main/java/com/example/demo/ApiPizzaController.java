package com.example.demo;

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
		Pizza savedPizza = pizzaService.save(pizza);
		return new ResponseEntity<>(savedPizza, HttpStatus.CREATED);
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
	@PostMapping("/pizze/update/{pizzaId}")
	public ResponseEntity<Pizza> updatePizza(@PathVariable int pizzaId,
			@RequestParam(value = "ingredientiSelezionati", required = false) List<Integer> ingredientiSelezionati,
			@RequestBody Pizza updatedPizza) {
		Optional<Pizza> pizzaOpt = pizzaService.findById(pizzaId);
		if (pizzaOpt.isPresent()) {
			Pizza existingPizza = pizzaOpt.get();
			existingPizza.setNome(updatedPizza.getNome());
			existingPizza.setDescrizione(updatedPizza.getDescrizione());
			existingPizza.setFoto(updatedPizza.getFoto());
			existingPizza.setPrezzo(updatedPizza.getPrezzo());

			if (ingredientiSelezionati != null) {
				List<Ingrediente> ingredienti = ingredienteService.findByIds(ingredientiSelezionati);
				existingPizza.setIngredienti(ingredienti);
			} else {
				// Se non sono stati selezionati ingredienti, rimuovi tutti gli ingredienti
				existingPizza.getIngredienti().clear();
			}

			Pizza savedPizza = pizzaService.save(existingPizza);
			return new ResponseEntity<>(savedPizza, HttpStatus.OK);
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
