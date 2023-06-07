package com.example.demo;

import org.springframework.validation.BindingResult;

public class PizzaCreateResponse {
	Pizza pizza;
	BindingResult bindingResult;

	public PizzaCreateResponse(Pizza pizza, BindingResult bindingResult) {
		setPizza(pizza);
		setBindingResult(bindingResult);
	}

	public PizzaCreateResponse(BindingResult bindingResult2) {
		setBindingResult(bindingResult);
	}

	public PizzaCreateResponse(Pizza pizza2) {
		setPizza(pizza);
	}

	public Pizza getPizza() {
		return pizza;
	}

	public void setPizza(Pizza pizza) {
		this.pizza = pizza;
	}

	public BindingResult getBindingResult() {
		return bindingResult;
	}

	public void setBindingResult(BindingResult bindingResult) {
		this.bindingResult = bindingResult;
	}
}