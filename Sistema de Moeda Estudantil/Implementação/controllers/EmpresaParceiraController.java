package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.dao.EmpresaParceiraDAO;
import br.com.demo.regescweb.models.EmpresaParceira;
import br.com.demo.regescweb.models.Vantagem;
import br.com.demo.regescweb.security.JwtUtil;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/empresas-parceiras")
public class EmpresaParceiraController {

    @Autowired
    private EmpresaParceiraDAO empresaParceiraDAO;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<?> criarEmpresaParceira(@RequestBody EmpresaParceira empresaParceira) {
        if (empresaParceiraDAO.buscarPorEmail(empresaParceira.getEmail()) != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Empresa já cadastrada com este e-mail.");
        }

        empresaParceiraDAO.salvar(empresaParceira);
        return ResponseEntity.status(HttpStatus.CREATED).body(empresaParceira);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarEmpresaParceira(@PathVariable Long id, @RequestBody EmpresaParceira empresaAtualizada) {
        return empresaParceiraDAO.buscarPorId(id)
                .map(existingEmpresa -> {
                    empresaAtualizada.setId(id);
                    empresaParceiraDAO.atualizar(empresaAtualizada);
                    return ResponseEntity.ok(empresaAtualizada);
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada."));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletarEmpresaParceira(@PathVariable Long id) {
        return empresaParceiraDAO.buscarPorId(id)
                .map(existingEmpresa -> {
                    empresaParceiraDAO.deletar(id);
                    return ResponseEntity.noContent().build();
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada."));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarEmpresaParceiraPorId(@PathVariable Long id) {
        return empresaParceiraDAO.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada."));
    }

    @GetMapping
    public ResponseEntity<List<EmpresaParceira>> listarTodasEmpresasParceiras() {
        return ResponseEntity.ok(empresaParceiraDAO.buscarTodas());
    }

    @GetMapping("/me")
    public ResponseEntity<?> buscarEmpresaAtual(HttpServletRequest request) {
        return processarJwt(request)
                .map(email -> empresaParceiraDAO.buscarPorEmail(email))
                .map(empresa -> empresa != null ? ResponseEntity.ok(empresa) : ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada."))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou não fornecido."));
    }

    @GetMapping("/me/vantagens")
    public ResponseEntity<?> listarVantagensDaEmpresa(HttpServletRequest request) {
        return processarJwt(request)
                .map(email -> empresaParceiraDAO.buscarPorEmail(email))
                .map(empresa -> {
                    if (empresa != null) {
                        List<Vantagem> vantagens = empresaParceiraDAO.buscarVantagensPorEmpresa(empresa.getId());
                        return ResponseEntity.ok(vantagens);
                    }
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada.");
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token inválido ou não fornecido."));
    }

    // Método para processar o JWT e extrair o e-mail da empresa
    private Optional<String> processarJwt(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            try {
                Claims claims = jwtUtil.parseToken(jwt);
                return Optional.ofNullable(claims.getSubject());
            } catch (JwtException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}
