package br.com.demo.regescweb.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private JwtFilter jwtFilter;

    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .cors().and()
            .authorizeRequests(authorize -> authorize
                .antMatchers(HttpMethod.POST, "/api/alunos/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/contas/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/professores/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/empresas-parceiras/**").permitAll()
                .antMatchers(HttpMethod.POST, "/api/transacoes/**").permitAll()

                .antMatchers(HttpMethod.GET, "/api/alunos/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/alunos/**").hasRole("ALUNO")
                .antMatchers(HttpMethod.DELETE, "/api/alunos/**").hasRole("ALUNO")
                
                .antMatchers(HttpMethod.GET, "/api/empresas-parceiras/**").hasRole("EMPRESA")
                .antMatchers(HttpMethod.PUT, "/api/empresas-parceiras/**").hasRole("EMPRESA")
                .antMatchers(HttpMethod.DELETE, "/api/empresas-parceiras/**").hasRole("EMPRESA")
    
                .antMatchers(HttpMethod.GET, "/api/professores/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/api/professores/**").hasRole("PROFESSOR")
                .antMatchers(HttpMethod.DELETE, "/api/professores/**").hasRole("PROFESSOR")
                
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/api/instituicoes/**", "/api/vantagens/**", "/api/contas/**").permitAll()
                .antMatchers("/api/test/**", "/auth/**", "/auth/login", 
                             "/v3/api-docs/**", "/swagger-ui/**", "/webjars/**", "/api/empresas/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );
    
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
    

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("alunoUser")
            .password(passwordEncoder().encode("passwordAluno"))
            .roles("ALUNO")
            .and()
            .withUser("empresaUser")
            .password(passwordEncoder().encode("passwordEmpresa"))
            .roles("EMPRESA")
            .and()
            .withUser("professorUser")
            .password(passwordEncoder().encode("passwordProfessor"))
            .roles("PROFESSOR");
    }
    


    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); 
    }
}
