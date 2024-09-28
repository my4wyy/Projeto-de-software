package br.com.demo.regescweb.controllers;

import br.com.demo.regescweb.models.Pedido;
import br.com.demo.regescweb.models.Automovel;
import br.com.demo.regescweb.models.StatusPedido;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.SecretKey;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private static final Logger logger = LoggerFactory.getLogger(PedidoController.class);

    @PersistenceContext
    private EntityManager entityManager;

    private SecretKey secretKey = Keys.hmacShaKeyFor("chaveSecretaSeguraDeveSerUsadaAquiParaAssinaturaDoToken".getBytes(StandardCharsets.UTF_8));

    // Método para validar o token JWT
    private boolean validarToken(String token) {
        logger.info("Validando token: {}", token);
        if (token == null || !token.startsWith("Bearer ")) {
            logger.warn("Token inválido ou não fornecido: {}", token);
            return false;
        }

        String jwt = token.substring(7);
        try {
            Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(jwt);
            logger.info("Token válido: {}", claims.getBody());
            return true; // Token é válido
        } catch (JwtException e) {
            logger.error("Erro na validação do token: {}", e.getMessage());
            return false; // Token inválido
        }
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> listarPedidos(@RequestHeader("Authorization") String token) {
        logger.info("Método listarPedidos chamado.");
        if (!validarToken(token)) {
            logger.warn("Token inválido ao listar pedidos.");
            return ResponseEntity.status(401).body(null);
        }

        List<Pedido> pedidos = entityManager.createQuery("SELECT p FROM Pedido p", Pedido.class).getResultList();
        logger.info("Pedidos listados com sucesso: {}", pedidos.size());
        return ResponseEntity.ok(pedidos);
    }

    @PostMapping
    @Transactional
    public ResponseEntity<Pedido> salvarPedido(@RequestHeader("Authorization") String token,
                                               @RequestParam("dataPedido") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataPedido,
                                               @RequestParam("automovel") Long automovelId) {
        logger.info("Método salvarPedido chamado.");

        if (!validarToken(token)) {
            logger.warn("Token inválido ao salvar pedido.");
            return ResponseEntity.status(401).build();
        }

        Automovel automovel = entityManager.find(Automovel.class, automovelId);
        if (automovel == null) {
            logger.warn("Automóvel não encontrado com ID: {}", automovelId);
            return ResponseEntity.notFound().build();
        }

        Pedido pedido = new Pedido(Date.from(dataPedido.atStartOfDay(ZoneId.systemDefault()).toInstant()), automovel);
        pedido.setStatus(StatusPedido.PENDENTE);
        entityManager.persist(pedido);
        logger.info("Pedido salvo com sucesso: {}", pedido);

        return ResponseEntity.ok(pedido);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Pedido> atualizarPedido(@RequestHeader("Authorization") String token,
                                                  @PathVariable Long id,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dataPedido,
                                                  @RequestParam(required = false) Long automovelId) {
        logger.info("Método atualizarPedido chamado.");

        if (!validarToken(token)) {
            logger.warn("Token inválido ao atualizar pedido.");
            return ResponseEntity.status(401).build();
        }

        Pedido pedido = entityManager.find(Pedido.class, id);
        if (pedido == null) {
            logger.warn("Pedido não encontrado com ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        if (dataPedido != null) {
            pedido.setDataPedido(Date.from(dataPedido.atStartOfDay(ZoneId.systemDefault()).toInstant()));
            logger.info("Data do pedido atualizada: {}", pedido);
        }

        if (automovelId != null) {
            Automovel automovel = entityManager.find(Automovel.class, automovelId);
            if (automovel != null) {
                pedido.setAutomovel(automovel);
                logger.info("Automóvel do pedido atualizado: {}", automovel);
            } else {
                logger.warn("Automóvel não encontrado com ID: {}", automovelId);
            }
        }

        return ResponseEntity.ok(pedido);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deletarPedido(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        logger.info("Método deletarPedido chamado.");

        if (!validarToken(token)) {
            logger.warn("Token inválido ao deletar pedido.");
            return ResponseEntity.status(401).build();
        }

        Pedido pedido = entityManager.find(Pedido.class, id);
        if (pedido == null) {
            logger.warn("Pedido não encontrado com ID: {}", id);
            return ResponseEntity.notFound().build();
        }

        entityManager.remove(pedido);
        logger.info("Pedido deletado com sucesso: {}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> verPedido(@RequestHeader("Authorization") String token, @PathVariable Long id) {
        logger.info("Método verPedido chamado.");

        if (!validarToken(token)) {
            logger.warn("Token inválido ao visualizar pedido.");
            return ResponseEntity.status(401).build();
        }

        Pedido pedido = entityManager.find(Pedido.class, id);
        return pedido != null ? ResponseEntity.ok(pedido) : ResponseEntity.notFound().build();
    }
}
