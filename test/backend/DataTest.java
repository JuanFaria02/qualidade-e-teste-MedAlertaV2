package test.backend;

import backend.Medicamento;
import backend.gerenciamento.Data;
import backend.usuario.Uso;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

class DataTest {

    Uso uso;
    ArrayList<String> dias;
    Calendar c;

    @BeforeEach
    void inicializar() {
        Medicamento remedio = new Medicamento("Dipirona");
        int dose = 1;
        int duracaoDoTratamento = 10;
        int qtdDisponivel = 30;

        dias = new ArrayList<>();
        uso = new Uso(remedio, dose, dias, duracaoDoTratamento, qtdDisponivel);
    }

    // Alterar o parâmetro do .add conforme o dia do teste
    // Alterar o parâmetro horario do horaDoRemedio conforme horário do teste
    @Test
    void testHoraDoRemedio_CorrespondeHoraEDia() {
        dias.add("sex");
        assertTrue(Data.horaDoRemedio(uso, 18));
    }

    // Alterar o parâmetro do .add para outro diferente do dia do teste    
    // Alterar o parâmetro horario do horaDoRemedio para outro diferente horário do teste
    @Test
    void testHoraDoRemedio_NaoCorrespondeHora() {
        dias.add("sex");
        assertFalse(Data.horaDoRemedio(uso, 15));
    }

    // Alterar o horário diretamente na classe para esse teste funcionar
    @Test
    void testEhMeiaNoite_True() {
        assertTrue(Data.ehMeiaNoite());
    }

    // Este teste passará caso o horário não seja 00h
    // Caso seja 00h, alterar o horário diretamente na classe para esse teste funcionar
    @Test
    void testEhMeiaNoite_False() {
        assertFalse(Data.ehMeiaNoite());
    }

    // Alterar o parâmetro "hora" do verificarHora conforme o horário do teste para funcionar
    @Test
    void testVerificarHora_CorrespondeHora() {
        assertTrue(Data.verificarHora(18));
    }

    // Alterar o parâmetro "hora" do verificarHora para outro horário diferente do teste para funcionar    
    @Test
    void testVerificarHora_NaoCorrespondeHora() {
        assertFalse(Data.verificarHora(14));
    }

    @Test
    void testVerificarUltimaVerificacao() throws NoSuchFieldException, IllegalAccessException {
        int hora = 8;
        assertTrue(Data.verificarUltimaVerificacao(hora));

        // Acessa o campo privado "ultimaVerficacaoHorario" da classe Data
        Field field = Data.class.getDeclaredField("ultimaVerficacaoHorario");

        // Permite acesso ao campo privado
        field.setAccessible(true);

        // Define o valor do campo "ultimaVerficacaoHorario" para "hora" (8)
        field.setInt(null, hora);
        assertFalse(Data.verificarUltimaVerificacao(hora));
    }

    @Test
    void testFormatarDia() {
        assertEquals(2, Data.formatarDia("seg"));
        assertEquals(3, Data.formatarDia("ter"));
        assertEquals(4, Data.formatarDia("qua"));
        assertEquals(5, Data.formatarDia("qui"));
        assertEquals(6, Data.formatarDia("sex"));
        assertEquals(7, Data.formatarDia("sab"));
        assertEquals(1, Data.formatarDia("dom"));
    }

    // Alterar para o dia correto para o teste funcionar
    @Test
    void testVerificarDia_DiaCorreto() {
        dias.add("sex"); // Define o dia para o teste
        assertTrue(Data.verificarDia(dias));
    }

    // Alterar para o dia incorreto para o teste funcionar
    @Test
    void testVerificarDia_DiaIncorreto() {
        dias.add("qui");
        assertFalse(Data.verificarDia(dias));
    }

    @Test
    void testHoraDoRemedio_alterandoCenario() {
    dias.add("seg");
    dias.add("ter");
    dias.add("qui");
    dias.add("sex");

    uso = new Uso(new Medicamento("Paracetamol"), 1, dias, 5, 10);

    // Neste cenário, dia e hora correspondentes
    assertTrue(Data.horaDoRemedio(uso, 18));

    // Alterar para o dia incorreto para o teste funcionar
    dias.remove("qui");
    dias.add("dom");

    // Neste cenário, dia não correspondente
    assertFalse(Data.horaDoRemedio(uso, 7));
    }


}
