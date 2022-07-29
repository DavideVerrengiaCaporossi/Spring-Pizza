package jana60.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jana60.model.Pizza;
import jana60.repository.IngredientiRepository;
import jana60.repository.PizzaRepository;


@Controller
@RequestMapping("/")
public class PizzaController {
	@Autowired 
	private PizzaRepository repo;
	
	@Autowired
	private IngredientiRepository repoIngredienti;
	
	@GetMapping
	public String pizzaList (Model model) {
	    model.addAttribute("pizzaList", repo.findAll());
	    return "/pizza";
	}
	
	@GetMapping("/add")
	public String pizzaForm (Model model) {
		model.addAttribute("pizzaForm", new Pizza());
		return "editPizza";
	}
	
//	@PostMapping("/add")
//	public String save (@Valid @ModelAttribute("pizzaForm") Pizza addPizza, BindingResult br) {
//		if (br.hasErrors()) {
//		      
//		      return "editPizza";
//		    } else {
//		      
//		      repo.save(addPizza);
//		      return "redirect:/"; 
//		    }
//	}
	
	@PostMapping("add/{id}")
	  public String aggiungi(@Valid @ModelAttribute("pizza") Pizza pizzaForm, BindingResult br, Model model) {
	    // testo se ci sono errori di validazione
	    boolean hasErrors = br.hasErrors();
	    boolean validateNome = true;
	    if (pizzaForm.getId() != null) { // sono in edit non in create
	      Pizza pizzaBeforeUpdate = repo.findById(pizzaForm.getId()).get();
	      if (pizzaBeforeUpdate.getNome().equals(pizzaForm.getNome())) {
	        validateNome = false;
	      }
	    }
	    // testo se nome è univoco
	    if (validateNome && repo.countByNome(pizzaForm.getNome()) > 0) {
	      br.addError(new FieldError("pizza", "nome", "il nome deve essere unico"));
	      hasErrors = true;
	    }

	    if (hasErrors) {
	      // se ci sono errori non salvo il book su database ma ritorno alla form precaricata
	      return "/add";
	    } else {
	      // se non ci sono errori salvo il book che arriva dalla form
	      try {
	        repo.save(pizzaForm);
	      } catch (Exception e) { // gestisco eventuali eccezioni sql
	        model.addAttribute("errorMessage", "Impossibile salvare la pizza");
	        return "/add";
	      }
	      return "redirect:/"; // non cercare un template, ma fai la HTTP redirect a quel path
	    }
	  }
	 @GetMapping("/delete/{id}")
	  public String delete(@PathVariable("id") Integer pizzaId, RedirectAttributes ra) {
	    Optional<Pizza> result = repo.findById(pizzaId);
	    if (result.isPresent()) {
	      // repo.deleteById(bookId);
	      repo.delete(result.get());
	      ra.addFlashAttribute("successMessage", "pizza" + result.get().getNome() + " deleted!");
	      return "redirect:/";
	    } else {
	      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	          "Pizza con id " + pizzaId + " not present");
	    }
}
	 //modifica come serve a te come ha fatto vedere costanza
	 
	 
	 @GetMapping("/edit/{id}")
	  public String modifica(@PathVariable("id") Integer pizzaId, Model model) {
	    Optional<Pizza> result = repo.findById(pizzaId);
	  
	    if (result.isPresent()) {
	     

	      model.addAttribute("pizzaForm", result.get());
	      model.addAttribute("pizzaList", repo.findAllByOrderByNome());
	      model.addAttribute("ingredienti", repoIngredienti.findAllByOrderByNome());
	      return "/editPizza";
	    } else {
	      throw new ResponseStatusException(HttpStatus.NOT_FOUND,
	          "Book con id " + pizzaId + " not present");
	    }

	  }
}

