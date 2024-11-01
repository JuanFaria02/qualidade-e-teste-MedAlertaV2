package test.backend.gerenciamento;

import backend.gerenciamento.Gerenciador;
import backend.Endereco;
import backend.usuario.PessoaFisica;
import backend.usuario.Uso;
import backend.Medicamento;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class GerenciadorTest {
    
    private PessoaFisica pessoa;
    private Endereco endereco;
    private Uso uso1;
    private Uso uso2;

    @BeforeEach
    public void setUp() {
        endereco = new Endereco("Rua A", "123", "", "Bairro B", "Cidade C", "Estado D", "Brasil", "12345-678");
        pessoa = new PessoaFisica("Teste Nome", "123456789", "teste@example.com", "123", "12345", endereco);
        Gerenciador.setPessoa(pessoa);

        // Criar instâncias de Medicamento com dados detalhados
        Medicamento remedioA = new Medicamento("Remédio A", 25.5f, "Para dor", "Comprimido", "Tomar após refeições", true);
        Medicamento remedioB = new Medicamento("Remédio B", 15.0f, "Para febre", "Xarope", "Tomar em jejum", false);

        // Criar alguns objetos de uso de teste com os parâmetros corretos
        uso1 = new Uso(remedioA, 10, new ArrayList<>(), 7, 42, 8, 4); // duracaoDoTratamento, qtdDisponivel, horarioInicial, intervalo de 4 hora
        uso2 = new Uso(remedioB, 20, new ArrayList<>(), 5, 14, 8, 8); // intervalo de 8 horas

        Gerenciador.listaDeUsos.add(uso1);
        Gerenciador.listaDeUsos.add(uso2);
    }

    @Test
    public void verificarIntervaloDoGerenciadorTest() {
        int intervalo = Gerenciador.verificarIntervaloDoGerenciador();
        assertAll(
            () -> assertTrue(intervalo > 0, "O intervalo deve ser positivo"),
            () -> assertEquals(4, intervalo, "O intervalo mínimo deve ser 1")
        );
    }

    @Test
    public void verificarQtdRemedioSuficienteTest() {
        boolean resultado = Gerenciador.verificarQtdRemedio(uso1);
        assertFalse(resultado, "Deve retornar falso pois a quantidade de remédio disponível é suficiente");
    }

    @Test
    public void verificarQtdRemedioInsuficienteTest() {        
        boolean resultado = Gerenciador.verificarQtdRemedio(uso2);
        assertTrue(resultado, "Deve retornar verdadeiro pois a quantidade necessária é maior que disponível");
    }

    @Test
    public void atualizarDuracaoDeUsoTest() {
        int duracaoInicial = uso1.getDuracaoDoTratamento();
        Gerenciador.atualizarDuracaoDeUso(uso1);
        assertAll(
            () -> assertTrue(uso1.getDuracaoDoTratamento() >= 0, "A duração do tratamento não deve ser negativa"),
            () -> assertEquals(duracaoInicial - 1, uso1.getDuracaoDoTratamento(), "A duração do tratamento deve diminuir em 1")
        );
    }

    @Test
    public void enviarNotificacaoCompraQuandoInsuficienteTest() {
        uso1.setQtdDisponivel(1);  // Quantidade insuficiente para simular necessidade de compra
        boolean resultado = Gerenciador.enviarNotificacaoCompra(uso1);
        assertFalse(resultado, "Deve retornar falso para continuar execução após notificação de compra");
    }

   }
