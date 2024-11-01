package test.backend;

import backend.Agenda;
import backend.Endereco;
import backend.Pessoa;
import backend.usuario.PessoaFisica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

class AgendaTest {
    private Agenda agenda;
    private Endereco endereco;
    private Pessoa contato;

    @BeforeEach
    public void setUp() {
        agenda = new Agenda();
        endereco = new Endereco("João Paulo Trindade", "22", "", "Apollo", 
        "São Gonçalo", "Rio de Janeiro", "Brail", "000000000");
        contato = new PessoaFisica("Teste nome", "123456789", "teste@gmail.com", "123.456.789-00", "12345", endereco);
    }

    @Test
    public void adicionarContatoTest() {
        agenda.adicionarContato(contato);
        assertAll(() -> {
            assertEquals(contato, agenda.getContatos().get(0));
            assertTrue(agenda.getContatos().size() > 0);
        });
    }

    @Test
    public void adicionarContatoNullTest() {
        assertThrows(IllegalArgumentException.class, () -> agenda.adicionarContato(null));
    }

    @Test
    public void alterarTelefoneContatoExistenteTest() {
        agenda.adicionarContato(contato);

        boolean resultado = agenda.alterarTelContato("Teste nome", "1635521244");
        assertTrue(resultado);
        assertEquals("1635521244", agenda.getContatos().get(0).getTelefone());
    }

    @Test
    public void alterarTelefoneContatoInexistenteTest() {
        boolean resultado = agenda.alterarTelContato("Teste nome", "123456789");
        assertFalse(resultado);
    }

    @Test
    public void alterarEmailContatoExistenteTest() {
        agenda.adicionarContato(contato);

        boolean resultado = agenda.alterarEmailContato("Teste nome", "testenovo@example.com");
        assertTrue(resultado);
        assertEquals("testenovo@example.com", agenda.getContatos().get(0).getEmail());
    }

    @Test
    public void alterarEmailContatoInexistenteTest() {
        boolean resultado = agenda.alterarEmailContato("Teste nome", "testenovo@example.com");
        assertFalse(resultado);
    }

    @Test
    public void alterarNomeContatoTest() {
        agenda.adicionarContato(contato);

        boolean resultado = agenda.alterarNomeContato("Teste nome", "Novo nome");
        assertNotEquals(contato.getNome(), "Teste Nome");
        assertEquals(contato.getNome(), "Novo nome");;
        assertTrue(resultado);
    }

    @Test
    public void removerContatoExistenteTest() {
        agenda.adicionarContato(contato);

        boolean resultado = agenda.removerContato("Teste nome");
        assertTrue(resultado);
        assertTrue(agenda.getContatos().isEmpty());
    }

    @Test
    public void removerContatoInexistenteTest() {
        agenda.adicionarContato(contato);

        boolean resultado = agenda.removerContato("teste");
        assertFalse(resultado);
    }

    @Test
    public void toStringWithContatosTest() {
        PessoaFisica contato1 = new PessoaFisica("Teste 1", "123456789", "test1@gmail.com", "123.456.789-00", "12345", endereco);
        PessoaFisica contato2 = new PessoaFisica("Teste 2", "987654321", "test2@gmail.com", "987.654.321-00", "54321", endereco);
        agenda.adicionarContato(contato1);
        agenda.adicionarContato(contato2);

        String emailsConcatenados = "test1@gmail.com/test2@gmail.com";
        assertEquals(emailsConcatenados, agenda.toString());
    }

    @Test
    public void testToStringWithoutContatos() {
        assertEquals("null", agenda.toString());
    }
}
