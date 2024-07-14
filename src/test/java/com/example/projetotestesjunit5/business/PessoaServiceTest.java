package com.example.projetotestesjunit5.business;

import com.example.projetotestesjunit5.infrastructure.PessoaRepository;
import com.example.projetotestesjunit5.infrastructure.entity.Pessoa;
import com.example.projetotestesjunit5.infrastructure.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) //para rodar os testes com o mockito
public class PessoaServiceTest {

    @InjectMocks
    PessoaService service;

    @Mock //cria um mock do repository, mock serve para simular o comportamento de um objeto
    PessoaRepository repository;

    Pessoa pessoa;

    //mockando pessoa para ser usada antes de cada teste
    @BeforeEach
    public void setUp() {
        pessoa = new Pessoa("Angelica", "12358569852", "Desenvolvedora", 30, "Sao Paulo", "Rua das Cruzes", 54);
    }

    //Annotation @Test da JUNIT.JUPITER e não apenas JUNIT
    @Test
    void deveBuscarPessoasPorCPFComSucesso() {
        //cenário
        when(repository.findPessoa(pessoa.getCpf())).thenReturn(Collections.singletonList(pessoa));

        //execução
        List<Pessoa> pessoas = service.buscaPessoasPorCpf(pessoa.getCpf());

        //verificação
        assertEquals(Collections.singletonList(pessoa), pessoas);
        verify(repository).findPessoa(pessoa.getCpf());
        verifyNoMoreInteractions(repository);

    }

    @Test
    void naoDeveChamaroRepositoryCasoParametroCPFNulo(){
        final BusinessException e = assertThrows(BusinessException.class, () -> {
            service.buscaPessoasPorCpf(null);
        });

        //verificando se a exceção foi lançada
        assertThat(e, notNullValue());
        //verificando a mensagem da exceção
        assertThat(e.getMessage(), is("Erro ao buscar pessoas por cpf = null"));
        //verificando se a causa da exceção é uma IllegalArgumentException
        assertThat(e.getCause(), notNullValue());
        //verificando a mensagem da causa da exceção
        assertThat(e.getCause().getMessage(), is("Cpf é obrigatório!"));
        //verifica se não houve interação com o repository
        verifyNoInteractions(repository);

    }

    @Test
    void deveAcionarExceptionQuandoRepositoryFalhar() {
        when(repository.findPessoa(pessoa.getCpf())).thenThrow(new RuntimeException("Falha ao buscar pessoas por cpf!"));

        final BusinessException e = assertThrows(BusinessException.class, () -> {
            service.buscaPessoasPorCpf(pessoa.getCpf());
        });

        //verificando se a exceção foi lançada
        assertThat(e.getMessage(), is(format("Erro ao buscar pessoas por cpf = %s", pessoa.getCpf())));
        //verificando se a causa da exceção é uma RuntimeException
        assertThat(e.getCause().getClass(), is(RuntimeException.class));
        //verificando a mensagem da causa da exceção
        assertThat(e.getCause().getMessage(), is("Falha ao buscar pessoas por cpf!" ));
        //verifica se houve interação com o repository
        verify(repository).findPessoa(pessoa.getCpf());
        //verifica se não houve mais interações com o repository
        verifyNoMoreInteractions(repository);

    }



}
