
import org.sql2o.Sql2o;

import java.net.MalformedURLException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static spark.Spark.*;
import static spark.Spark.get;


public class Main {

    private static ObjectMapper mapper=new ObjectMapper();
    public static void main(String[] args) throws MalformedURLException {
        String portNumber = System.getenv("PORT");
        port(portNumber != null ? Integer.valueOf(portNumber) : 8080);
        String connURL = System.getenv("POSTGRES_URL") ;

        ConnectionDetails connection = new ConnectionDetails(connURL);
        System.out.println("Connection details="+connection.toString());

        Sql2o sql2o = new Sql2o(connection.getUrl());

        Sql2oModel model = new Sql2oModel(sql2o);

        // get all post (using HTTP get method)
        get("/products", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return mapper.writeValueAsString(model.getAllProducts());
        });
        System.out.println("READY");
        awaitInitialization();
    }

}
