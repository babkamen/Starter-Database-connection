import lombok.extern.slf4j.Slf4j;
import org.sql2o.Connection;
import org.sql2o.Query;
import org.sql2o.Sql2o;
import uk.org.fyodor.generators.RDG;
import uk.org.fyodor.range.Range;

import java.util.List;

@Slf4j
public class Sql2oModel {

    private Sql2o sql2o;

    public Sql2oModel(Sql2o sql2o) {
        this.sql2o = sql2o;
        createTable();
        insertRandomData();
    }

    public List<Product> getAllProducts() {
        try (Connection conn = sql2o.open()) {
            return conn.createQuery("select * from product")
                    .executeAndFetch(Product.class);
        }
    }

    private void insertRandomData() {
        try (Connection conn = sql2o.open()) {
            Query query = conn.createQuery("INSERT INTO product(name,quantity) VALUES(:name,:quantity)");
            for (int i = 0; i < 20; i++) {
                query.addParameter("name", RDG.currency().next().toString())
                        .addParameter("quantity", RDG.integer(Range.closed(10, 30)).next())
                        .addToBatch();
            }
            query.executeBatch(); // executes entire batch
        } catch (Exception e) {
            log.error("Error during init={}", e);
        }
    }


    private void createTable() {
        try (Connection conn = sql2o.open()) {
            conn.createQuery("DROP TABLE IF EXISTS product").executeUpdate();
            conn.createQuery("CREATE TABLE product\n" +
                    "(\n" +
                    "    id SERIAL NOT NULL,\n" +
                    "    name CHARACTER VARYING(20) NOT NULL,\n" +
                    "    quantity INT NOT NULL,\n" +
                    "    CONSTRAINT product_pkey PRIMARY KEY (id)\n" +
                    ")").executeUpdate();
        } catch (Exception e) {
            log.error("Error during init={}", e);
        }
    }


}