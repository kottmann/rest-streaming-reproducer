package response.in;

import io.quarkus.runtime.LaunchMode;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class DevModeUsage {

  private static Logger log = LoggerFactory.getLogger(DevModeUsage.class);

  void onStart(@Observes StartupEvent ev) {
    if (LaunchMode.DEVELOPMENT.equals(LaunchMode.current())) {
      var message = """
          
          Quarkus Application in Development Mode
          --------------------------------------------------------------------------------------------------------------------------------

          Run the following command to reproduce the issue:
         
          Download the file without a simulated error, should return all 50m lines, in case the client
          closes the connection the InputStream is still read till the end (why?)
          curl "http://localhost:8080/hello" | wc -l
         
          The InputStream just closes the connection to show that this can be detected by the client
          curl "http://localhost:8080/hello?test=CloseConnection" | wc -l
         
          The InputStream will throw a RuntimeException from read() (exception is logged and stream closes)
          curl "http://localhost:8080/hello?test=RuntimeException" | wc -l
         
          The InputStream throws an IOException from read, in this case it ends up in an endless loop:
          curl "http://localhost:8080/hello?test=IOException" | wc -l
          """;

      log.info(message);
    }
  }
}
