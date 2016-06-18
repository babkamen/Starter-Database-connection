
import org.sql2o.Sql2o;

import java.net.MalformedURLException;

import static spark.Spark.*;
import static spark.Spark.get;


public class Main {


    public static void main(String[] args) throws MalformedURLException {
        String portNumber = System.getenv("PORT");
        port(portNumber != null ? Integer.valueOf(portNumber) : 8080);
        String connURL = System.getenv("POSTGRES_URL") != null ?
                System.getenv("POSTGRES_URL") :
                "postgres://lunyerbx:gmy54jURFJW8m9AQKSUgA8khciCQhJjf@tantor.db.elephantsql.com:5432/lunyerbx";

        ConnectionDetails connection = new ConnectionDetails(connURL);
        System.out.println("Connection details="+connection.toString());

        Sql2o sql2o = new Sql2o(connection.getUrl(), connection.getUsername(), connection.getPass());

        Sql2oModel model = new Sql2oModel(sql2o);

        // get all post (using HTTP get method)
        get("/products", (request, response) -> {
            response.status(200);
            response.type("application/json");
            return model.getAllProducts();
        }, new JsonTransformer());
        System.out.println("READY");
        awaitInitialization();
    }

}
