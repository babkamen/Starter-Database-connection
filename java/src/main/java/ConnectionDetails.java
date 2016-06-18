import lombok.Data;


@Data
public class ConnectionDetails {
    String url, username, pass;
    ConnectionDetails(String connURL){
        String str = "postgres://";
        int beginIndex = connURL.indexOf(str) + str.length();
        int endIndex = connURL.indexOf(":", beginIndex);
        username = connURL.substring(beginIndex, endIndex);
        pass = connURL.substring(endIndex + 1, connURL.indexOf("@"));
        connURL = connURL.replace(username + ":" + pass + "@", "").replace("postgres","postgresql");
        this.url ="jdbc:" + connURL;
    }
}
