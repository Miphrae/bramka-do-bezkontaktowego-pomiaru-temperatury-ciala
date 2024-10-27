package bramka_do_temperatury.SpringApplication1;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PomiarDAOTest {

    private PomiarDAO dao;
    @BeforeEach
    void setUp() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl("jdbc:oracle:thin:@localhost:1522:xe");
        dataSource.setUsername("AZATOR2");
        dataSource.setPassword("AZATOR2");
        dataSource.setDriverClassName("oracle.jdbc.OracleDriver");

        /*Import JdbcTemplate*/
        dao = new PomiarDAO(new JdbcTemplate(dataSource));

    }

    @Test
    void list() {
        List<Pomiar> listPomiar = dao.list();
        System.out.println(listPomiar);
        assertFalse(listPomiar.isEmpty());

    }

    @Test
    void save() {
        Pomiar pomiar = new Pomiar(1, 37, 24, "Michał", "Wojda", LocalDateTime.of(1927,7,16,12,45,5), 0.5, true);
        dao.save(pomiar);

    }

    @Test
    void get() {
        int nr_pomiaru = 0;
        Pomiar pomiar = dao.get(nr_pomiaru);
        System.out.println(pomiar);
        assertNotNull(pomiar);

    }

    @Test
    void update() {
        Pomiar pomiar = new Pomiar();
        pomiar.setNr_pomiaru(5);
        pomiar.setTemperatura_ciala(38);
        pomiar.setTemperatura_otoczenia(20);
        pomiar.setImie("Grzegorz");
        pomiar.setNazwisko("Stępniak");
        pomiar.setData_pomiaru(LocalDateTime.of(1820,5,3,3,0,1));
        pomiar.setBlad_pomiaru(0.7);

        dao.update(pomiar);

    }

    @Test
    void delete() {
        int nr_pomiaru = 3;
        dao.delete(nr_pomiaru);
    }
}