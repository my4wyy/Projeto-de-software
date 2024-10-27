package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.dao.EmpresaParceiraDAO;
import br.com.demo.regescweb.models.EmpresaParceira;
import br.com.demo.regescweb.models.Vantagem;
import br.com.demo.regescweb.security.JwtUtil; 
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/empresas-parceiras")
public class EmpresaParceiraController {

    @Autowired
    private EmpresaParceiraDAO empresaParceiraDAO;

    @Autowired
    private JwtUtil jwtUtil; 

    @PostMapping
    public ResponseEntity<EmpresaParceira> criarEmpresaParceira(@RequestBody EmpresaParceira empresaParceira) {
     EmpresaParceira empresaExistenteEmail = empresaParceiraDAO.buscarPorEmail(empresaParceira.getEmail());
        
        if (empresaExistenteEmail != null) {
            return ResponseEntity.status(HttpServletResponse.SC_CONFLICT)
                                 .body(null); 
        }

        empresaParceiraDAO.salvar(empresaParceira);
        return ResponseEntity.ok(empresaParceira);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmpresaParceira> atualizarEmpresaParceira(@PathVariable Long id, @RequestBody EmpresaParceira empresaAtualizada) {
        EmpresaParceira empresaParceira = empresaParceiraDAO.buscarPorId(id);
        if (empresaParceira == null) {
            return ResponseEntity.notFound().build();
        }
        empresaAtualizada.setId(id);
        empresaParceiraDAO.atualizar(empresaAtualizada);
        return ResponseEntity.ok(empresaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresaParceira(@PathVariable Long id) {
        EmpresaParceira empresaParceira = empresaParceiraDAO.buscarPorId(id);
        if (empresaParceira == null) {
            return ResponseEntity.notFound().build();
        }
        empresaParceiraDAO.deletar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmpresaParceira> buscarEmpresaParceiraPorId(@PathVariable Long id) {
        EmpresaParceira empresaParceira = empresaParceiraDAO.buscarPorId(id);
        return empresaParceira != null ? ResponseEntity.ok(empresaParceira) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public List<EmpresaParceira> listarTodasEmpresasParceiras() {
        return empresaParceiraDAO.buscarTodas();
    }

    @GetMapping("/me")
    public ResponseEntity<EmpresaParceira> buscarEmpresaAtual(HttpServletRequest request) {

        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7); 
            
            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(jwtUtil.getSecretKey()) 
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();
                String emailEmpresa = claims.getSubject(); 

                EmpresaParceira empresaParceira = empresaParceiraDAO.buscarPorEmail(emailEmpresa);
                return empresaParceira != null ? ResponseEntity.ok(empresaParceira) : ResponseEntity.notFound().build();
            } catch (JwtException e) {
                return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build(); // Token inválido
            }
        }
        return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build(); 
    }

    @GetMapping("/me/vantagens")
public ResponseEntity<List<Vantagem>> listarVantagensDaEmpresa(HttpServletRequest request) {
    String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
        String jwt = authorizationHeader.substring(7);
        
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(jwtUtil.getSecretKey())
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            String emailEmpresa = claims.getSubject();
            
            EmpresaParceira empresaParceira = empresaParceiraDAO.buscarPorEmail(emailEmpresa);
            if (empresaParceira != null) {
                List<Vantagem> vantagens = empresaParceiraDAO.buscarVantagensPorEmpresa(empresaParceira.getId());
                return ResponseEntity.ok(vantagens);
            }
            return ResponseEntity.notFound().build();
        } catch (JwtException e) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
        }
    }
    return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).build();
}

}
