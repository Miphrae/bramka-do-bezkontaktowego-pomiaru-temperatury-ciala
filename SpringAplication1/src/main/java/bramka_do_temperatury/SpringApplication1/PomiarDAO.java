package bramka_do_temperatury.SpringApplication1;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PomiarDAO {

    private JdbcTemplate jdbcTemplate;
    public PomiarDAO(JdbcTemplate jdbcTemplate) {
        super();
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Pomiar> list(){
        String sql = "SELECT * FROM POMIARY";
        List<Pomiar> listPomiar = jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(Pomiar.class));

        return listPomiar;
    }
    /* Insert – wstawianie nowego wiersza do bazy */
    public void save(Pomiar pomiar) {
        SimpleJdbcInsert insertActor = new SimpleJdbcInsert(jdbcTemplate);
        insertActor.withTableName("POMIARY").usingColumns("NR_POMIARU", "TEMPERATURA_CIALA", "TEMPERATURA_OTOCZENIA", "IMIE", "NAZWISKO", "DATA_POMIARU", "BLAD_POMIARU", "CZY_DODANE_RECZNIE");
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(pomiar);
        insertActor.execute(param);
    }
    /* Read – odczytywanie danych z bazy */
    public Pomiar get(int nr_pomiaru) {
        String sql = "SELECT * FROM POMIARY WHERE NR_POMIARU = ?";
        Object[] args = {nr_pomiaru};
        Pomiar pomiar = jdbcTemplate.queryForObject(sql, args, BeanPropertyRowMapper.newInstance(Pomiar.class));
        return pomiar;
    }
    /* Update – aktualizacja danych */
    public void update(Pomiar pomiar) {
        String sql = "UPDATE POMIARY SET temperatura_ciala=:temperatura_ciala, temperatura_otoczenia=:temperatura_otoczenia, imie=:imie, nazwisko=:nazwisko, data_pomiaru=:data_pomiaru, blad_pomiaru=:blad_pomiaru, czy_dodane_recznie=:czy_dodane_recznie WHERE nr_pomiaru=:nr_pomiaru";
        BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(pomiar);
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        template.update(sql, param);
    }
    /* Delete – wybrany rekord z danym id */
    public void delete(int nr_pomiaru) {
        String sql = "DELETE FROM POMIARY WHERE NR_POMIARU = ?";
        jdbcTemplate.update(sql, nr_pomiaru);
    }
}
