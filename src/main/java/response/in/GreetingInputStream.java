package response.in;

import java.io.IOException;
import java.io.InputStream;

import io.vertx.core.http.HttpServerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GreetingInputStream extends InputStream {

  private static final Logger log = LoggerFactory.getLogger(GreetingInputStream.class);

  private final String test;

  private final int n;
  private final HttpServerRequest request;
  private int counter = 0;
  private byte[] buffer = new byte[0];
  private int pos;
  private boolean closed;

  public GreetingInputStream(HttpServerRequest request, int n, String test) {
    this.request = request;
    this.n = n;
    this.test = test;
  }

  private void fillBufferWithNextLine() throws IOException {
    if (counter < n) {
      buffer = (counter++ + "\n").getBytes();
      pos = 0;
    } else {
      buffer = null;
    }
  }

  @Override
  public int read() throws IOException {

    if (closed) {
      log.error("Read called after close!");
      closed = false;
    }

    // If the index is at the end of the buffer, refill the buffer
    if (buffer != null && pos >= buffer.length) {
      fillBufferWithNextLine();
    }

    if (counter > n/4) {
      if ("IOException".equalsIgnoreCase(test)) {
        log.info("Test IO Exception has been thrown!");
        throw new IOException("Simulated Error");
      }
      else if ("RuntimeException".equalsIgnoreCase(test)) {
        log.info("Test Runtime Exception has been thrown!");
        throw new RuntimeException("Simulated Error");
      }
      else if ("CloseConnection".equalsIgnoreCase(test)) {
        request.connection().close();
      }
    }

    if (buffer == null) {
      return -1;
    }

    return buffer[pos++] & 0xFF;
  }

  @Override
  public void close() {
    if (counter == n) {
      log.info("Stream is closed after EOF!");
    }
    else {
      log.info("Stream is closed before EOF at " + counter);
    }

    closed = true;
  }
}

