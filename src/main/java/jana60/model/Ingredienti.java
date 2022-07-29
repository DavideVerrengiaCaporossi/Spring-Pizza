package jana60.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;

@Entity
@Table(name = "ingredienti")
public class Ingredienti {
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
	
private Integer id;
@NotEmpty
private String nome;

@ManyToMany(mappedBy = "ingredienti")
private List<Pizza> pizze;


public Integer getId() {
	return id;
}


public void setId(Integer id) {
	this.id = id;
}


public String getNome() {
	return nome;
}


public void setNome(String nome) {
	this.nome = nome;
}


public List<Pizza> getPizze() {
	return pizze;
}


public void setPizze(List<Pizza> pizze) {
	this.pizze = pizze;
}


//metodo che restituisce il numero delle pizze
public int getNumberOfBooks() {
    return pizze.size();
  }

}
