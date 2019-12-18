package br.com.projeto.educamais;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TesteUsuarioController {

	@Autowired
	public WebApplicationContext context;
	
	public MockMvc mock;
	
	@Before
	public void setUp() {
		mock = MockMvcBuilders.webAppContextSetup(this.context).build();
	}
	
	@Test
	public void teste00RequisicaoPost() throws Exception {
		String url = "/educamais/usuario";
		this.mock
			.perform(
				post(url)
					.content("{"
							+ " \"nome\" : \"Gabriel Neves\", "
							+ " \"email\": \"gabriel@email.com\", "
							+ " \"senha\": \"12345678\" "
							+ "}")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isCreated());
		
	}
	
	@Test
	public void teste01RequisicaoPostConflito() throws Exception {
		String url = "/educamais/usuario";
		this.mock
			.perform(
				post(url)
					.content("{"
							+ " \"nome\" : \"Gabriel Neves\", "
							+ " \"email\": \"gabriel@email.com\", "
							+ " \"senha\": \"12345678\" "
							+ "}")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isConflict());
		
	}
	
	@Test
	public void teste02RequisicaoPostNomeVazio() throws Exception {
		String url = "/educamais/usuario";
		this.mock
			.perform(
				post(url)
					.content("{"
							+ " \"nome\" : \"\", "
							+ " \"email\": \"gabriel@email.com\", "
							+ " \"senha\": \"12345678\" "
							+ "}")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(
					content()
					.json("["
							+ "{"
						   	+    " \"campo\":\"nome\","
						    +    " \"erro\":\"must not be empty\""
						    + "}"
						    + "]"));
		
	}
	
	@Test
	public void teste03RequisicaoPostEmailVazio() throws Exception {
		String url = "/educamais/usuario";
		this.mock
			.perform(
				post(url)
					.content("{"
							+ " \"nome\" : \"Gabriel Neves\", "
							+ " \"email\": \"\", "
							+ " \"senha\": \"12345678\" "
							+ "}")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(
					content()
					.json("["
							+ "{"
						   	+    " \"campo\":\"email\","
						    +    " \"erro\":\"must not be empty\""
						    + "}"
						    + "]"));
		
	}
	
	@Test
	public void teste04RequisicaoPostSenhaVazia() throws Exception {
		String url = "/educamais/usuario";
		this.mock
			.perform(
				post(url)
					.content("{"
							+ " \"nome\" : \"Gabriel Neves\", "
							+ " \"email\": \"gabriel@email.com\", "
							+ " \"senha\": \"\" "
							+ "}")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(
					content()
					.json("["
							+ "{"
						   	+    " \"campo\":\"senha\","
						    +    " \"erro\":\"length must be between 8 and 16\""
						    + "},"
						    + "{"
						    +    " \"campo\":\"senha\" ,"
						    +    " \"erro\":\"must not be empty\""
						    + "}"
						    + "]"));
		
	}
	
	@Test
	public void teste05RequisicaoPostNomeNulo() throws Exception {
		String url = "/educamais/usuario";
		this.mock
			.perform(
				post(url)
					.content("{"
							+ " \"nome\" : null, "
							+ " \"email\": \"gabriel@email.com\", "
							+ " \"senha\": \"12345678\" "
							+ "}")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(
					content()
					.json("["
							+ "{"
						   	+    " \"campo\":\"nome\","
						    +    " \"erro\":\"must not be empty\""
						    + "},"
						    + "{"
						    +    " \"campo\":\"nome\" ,"
						    +    " \"erro\":\"must not be null\""
						    + "}"
						    + "]"));
		
	}
	
	@Test
	public void teste06RequisicaoPostEmailNulo() throws Exception {
		String url = "/educamais/usuario";
		this.mock
			.perform(
				post(url)
					.content("{"
							+ " \"nome\" : \"Gabriel Neves\", "
							+ " \"email\": null, "
							+ " \"senha\": \"12345678\" "
							+ "}")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(
					content()
					.json("["
							+ "{"
						   	+    " \"campo\":\"email\","
						    +    " \"erro\":\"must not be empty\""
						    + "},"
						    + "{"
						    +    " \"campo\":\"email\" ,"
						    +    " \"erro\":\"must not be null\""
						    + "}"
						    + "]"));
		
	}
	
	@Test
	public void teste07RequisicaoPostSenhaNula() throws Exception {
		String url = "/educamais/usuario";
		this.mock
			.perform(
				post(url)
					.content("{"
							+ " \"nome\" : \"Gabriel Neves\", "
							+ " \"email\": \"gabriel@email.com\", "
							+ " \"senha\": null"
							+ "}")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(
					content()
					.json("["
							+ "{"
						   	+    " \"campo\":\"senha\","
						    +    " \"erro\":\"must not be empty\""
						    + "},"
						    + "{"
						    +    " \"campo\":\"senha\" ,"
						    +    " \"erro\":\"must not be null\""
						    + "}"
						    + "]"));
		
	}
	
	@Test
	public void teste08RequisicaoPostSenhaPequena() throws Exception {
		String url = "/educamais/usuario";
		this.mock
			.perform(
				post(url)
					.content("{"
							+ " \"nome\" : \"Gabriel Neves\", "
							+ " \"email\": \"gabriel@email.com\", "
							+ " \"senha\": \n1234567\n"
							+ "}")
					.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(
					content()
					.json("["
							+ "{"
						   	+    " \"campo\":\"senha\","
						    +    " \"erro\":\"length must be between 8 and 16\""
						    + "}"
						    + "]"));
		
	}
}
