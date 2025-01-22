package test.backend;

import backend.Agenda;
import backend.Endereco;
import backend.Pessoa;
import backend.farmacia.PessoaJuridica;
import backend.usuario.Medico;
import backend.usuario.PessoaFisica;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AgendaTest {
    private Agenda agenda;
    private Endereco endereco;
    private PessoaFisica contato;

    @BeforeEach
    public void setUp() {
        agenda = new Agenda();
        endereco = new Endereco("João Paulo Trindade", "22", "", "Apollo", 
                "São Gonçalo", "Rio de Janeiro", "Brasil", "000000000");
        contato = Mockito.spy(new PessoaFisica("Teste nome", "123456789", "teste@gmail.com", "123.456.789-00", "12345", endereco));
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
        verify(contato).setTelefone("1635521244");
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
        verify(contato).setEmail("testenovo@example.com");
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
        assertTrue(resultado);
        verify(contato).setNome("Novo nome");
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
        boolean resultado = agenda.removerContato("teste");
        assertFalse(resultado);
    }

    @Test
    public void toStringWithContatosTest() {
        PessoaFisica contato1 = Mockito.spy(new PessoaFisica("Teste 1", "123456789", "test1@gmail.com", "123.456.789-00", "12345", endereco));
        PessoaFisica contato2 = Mockito.spy(new PessoaFisica("Teste 2", "987654321", "test2@gmail.com", "987.654.321-00", "54321", endereco));
        agenda.adicionarContato(contato1);
        agenda.adicionarContato(contato2);

        String emailsConcatenados = "test1@gmail.com/test2@gmail.com";
        assertEquals(emailsConcatenados, agenda.toString());
    }

    @Test
    public void testToStringWithoutContatos() {
        assertEquals("null", agenda.toString());
    }

    @Test
    public void alterarParticularidadeContatoExistenteTest() {
        PessoaFisica contatoMock = mock(PessoaFisica.class);
        when(contatoMock.getNome()).thenReturn("Teste nome");

        agenda.adicionarContato(contatoMock);
        boolean resultado = agenda.alterarParticularidadeContato("Teste nome", "Nova particularidade");

        assertTrue(resultado, "O método deve retornar true para contatos existentes.");
        verify(contatoMock).setParticularidade("Nova particularidade");
    }


    @Test
    public void stringToAgendaDeveConverterStringParaAgendaComUsuarios() {
        try (MockedStatic<PessoaFisica> pessoaFisicaMock = mockStatic(PessoaFisica.class)) {
            PessoaFisica contatoMock = mock(PessoaFisica.class);
            pessoaFisicaMock.when(() -> PessoaFisica.resgatarUsuarioArquivo("usuario1", "senha123", true, false))
                    .thenReturn(contatoMock);
            pessoaFisicaMock.when(() -> PessoaFisica.resgatarUsuarioArquivo("usuario2", "senha123", true, false))
                    .thenReturn(contatoMock);

            String agendaString = "usuario1/usuario2";
            Agenda resultado = Agenda.stringToAgenda(agendaString, "senha123", "usuario", true, false);

            assertAll(
                () -> assertNotNull(resultado, "A agenda resultante não deve ser nula."),
                () -> assertEquals(2, resultado.getContatos().size(), "A agenda deve conter dois contatos."),
                () -> verify(contatoMock, times(0)).getNome() // Para garantir que não haja chamadas extras inesperadas.
            );
        }
    }

    @Test
    public void stringToAgendaDeveConverterStringParaAgendaComFarmacias() {
        try (MockedStatic<PessoaJuridica> pessoaJuridicaMock = mockStatic(PessoaJuridica.class)) {
            PessoaJuridica farmaciaMock = mock(PessoaJuridica.class);
            pessoaJuridicaMock.when(() -> PessoaJuridica.resgatarFarmaciaArquivo("farmacia1", "senha123", true, false))
                    .thenReturn(farmaciaMock);
            pessoaJuridicaMock.when(() -> PessoaJuridica.resgatarFarmaciaArquivo("farmacia2", "senha123", true, false))
                    .thenReturn(farmaciaMock);

            String agendaString = "farmacia1/farmacia2";
            Agenda resultado = Agenda.stringToAgenda(agendaString, "senha123", "farmacia", true, false);

            assertAll(
                () -> assertNotNull(resultado, "A agenda resultante não deve ser nula."),
                () -> assertEquals(2, resultado.getContatos().size(), "A agenda deve conter duas farmácias."),
                () -> verify(farmaciaMock, times(0)).getNome() // Para garantir que não haja chamadas extras inesperadas.
            );
        }
    }

    @Test
    public void stringToAgendaDeveConverterStringParaAgendaComMedicos() {
        try (MockedStatic<Medico> medicoMock = mockStatic(Medico.class)) {
            Medico medicoMockObj = mock(Medico.class);
            medicoMock.when(() -> Medico.resgatarMedicoArquivo("medico1", "senha123", true))
                    .thenReturn(medicoMockObj);
            medicoMock.when(() -> Medico.resgatarMedicoArquivo("medico2", "senha123", true))
                    .thenReturn(medicoMockObj);

            String agendaString = "medico1/medico2";
            Agenda resultado = Agenda.stringToAgenda(agendaString, "senha123", "medico", true, false);

            assertAll(
                () -> assertNotNull(resultado, "A agenda resultante não deve ser nula."),
                () -> assertEquals(2, resultado.getContatos().size(), "A agenda deve conter dois médicos."),
                () -> verify(medicoMockObj, times(0)).getNome() // Para garantir que não haja chamadas extras inesperadas.
            );
        }
    }
    
    @Test
    public void alterarParticularidadeContatoInexistenteTest() {
        boolean resultado = agenda.alterarParticularidadeContato("Teste nome", "Nova particularidade");
        assertFalse(resultado);
    }

    @Test
    public void verificarOrdemDeContatosTest() {
        PessoaFisica contato1 = new PessoaFisica("B", "123456789", "b@gmail.com", "123.456.789-00", "12345", endereco);
        PessoaFisica contato2 = new PessoaFisica("A", "987654321", "a@gmail.com", "987.654.321-00", "54321", endereco);
        agenda.adicionarContato(contato1);
        agenda.adicionarContato(contato2);

        assertEquals("A", agenda.getContatos().get(0).getNome());
        assertEquals("B", agenda.getContatos().get(1).getNome());
    }
}