package test.backend.gerenciamento;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import backend.gerenciamento.Notificacao;

public class NotificacaoTeste {

    @BeforeEach
    void resetOption() {
        // Reseta o valor de `option` antes de cada teste.
        Notificacao.setOption(-1);
    }

    @Test
    void testNotificarUsuarioAceita() {
        // Simula o clique do usuário em "SIM"
        Notificacao.setOption(0);
        assertTrue(Notificacao.notificar("Hora de tomar o remédio"));
    }

    @Test
    void testNotificarUsuarioRecusa() {
        // Simula o clique do usuário em "NÃO"
        Notificacao.setOption(1);
        assertFalse(Notificacao.notificar("Hora de tomar o remédio"));
    }

    @Test
    void testNotificarUsuarioNaoInterage() {
        // Não altera `option`, simulando o usuário não interagindo com a notificação
        assertFalse(Notificacao.notificar("Hora de tomar o remédio"));
    }

    @Test
    void testNotificarCompra() {
        // Verifica se `notificarCompra` executa sem erros
        assertDoesNotThrow(() -> Notificacao.notificarCompra("Comprar remédio"));
    }
    @Test
    void testNotificarResetaOption() {
        // Simula o usuário interagindo com "SIM" e verifica que `option` é resetado após a notificação
        Notificacao.setOption(0);
        Notificacao.notificar("Hora de tomar o remédio");
        assertEquals(-1, Notificacao.getOption(), "Option não foi resetado após notificação.");
    }

    @Test
    void testNotificarCompraSemInteracao() {
        // Garante que `notificarCompra` não depende de interação e não lança exceção
        assertDoesNotThrow(() -> Notificacao.notificarCompra("Comprar remédio sem interação do usuário"));
    }

    @Test
    void testInterrupcaoNaEspera() {
        // Testa o comportamento do código caso ocorra uma interrupção durante a espera no método `notificar`
        Thread.currentThread().interrupt();  // Interrompe a thread atual para simular um erro
        Notificacao.setOption(-1);
        assertDoesNotThrow(() -> Notificacao.notificar("Testando interrupção durante a espera"));
        assertFalse(Thread.currentThread().isInterrupted(), "A interrupção não foi limpa após o teste.");
    }
}
