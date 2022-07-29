package jana60.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jana60.model.Ingredienti;
import jana60.repository.IngredientiRepository;


@Controller
@RequestMapping("/ingredienti")
public class IngredientiController {
	@Autowired 
	private IngredientiRepository repo;
	
	 @GetMapping
	  public String listaIngredienti(Model model) {
		 //ordina ingredienti per nome
	    model.addAttribute("ingredienti", repo.findAllByOrderByNome());
	    //creiamo un ogetto con contenta quel ingrediente
	    model.addAttribute("newIngrediente", new Ingredienti());
	    return "/ingredienti";
}
	 //postmapping per collegare e salvare l aggiunta di un ingrediente
	 @PostMapping("/save")
	  public String saveCategory(@Valid @ModelAttribute("newIngrediente") Ingredienti ingrediente,
	      BindingResult br, Model model) {
	    if (br.hasErrors()) {
	      // ricarico la pagina
	      model.addAttribute("ingredienti", repo.findAllByOrderByNome());
	      return "/ingredienti";

	    } else {
	      // salvo gli ingredienti
	      repo.save(ingrediente);
	      return "redirect:/ingredienti";
	    }
}
}
