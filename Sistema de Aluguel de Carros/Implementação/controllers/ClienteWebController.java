package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.models.Cliente;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Controller
@RequestMapping("/clientes-web")
public class ClienteWebController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping
    public String listarClientes(Model model) {
        model.addAttribute("clientes", entityManager.createQuery("SELECT c FROM Cliente c", Cliente.class).getResultList());
        return "clientes/list";
    }

    @GetMapping("/novo")
    public String novoClienteForm(Model model) {
        model.addAttribute("cliente", new Cliente());
        return "clientes/form";
    }

    @PostMapping("/salvar")
    @Transactional
    public String salvarCliente(@ModelAttribute Cliente cliente) {
        if (cliente.getId() == null) {
            // Novo cliente
            entityManager.persist(cliente);
        } else {
            // Atualizar cliente existente
            entityManager.merge(cliente);
        }
        return "redirect:/clientes-web";
    }

    @GetMapping("/{id}")
    public String verCliente(@PathVariable Long id, Model model) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            return "clientes/ver";
        }
        return "redirect:/clientes-web";
    }

    @GetMapping("/editar/{id}")
    public String editarClienteForm(@PathVariable Long id, Model model) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            return "clientes/form";
        }
        return "redirect:/clientes-web";
    }

    @GetMapping("/deletar/{id}")
    @Transactional
    public String deletarCliente(@PathVariable Long id) {
        Cliente cliente = entityManager.find(Cliente.class, id);
        if (cliente != null) {
            entityManager.remove(cliente);
        }
        return "redirect:/clientes-web";
    }
}
