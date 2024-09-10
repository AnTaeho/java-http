package org.apache.coyote.http11;

import com.techcourse.exception.UncheckedServletException;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.Objects;
import org.apache.coyote.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;

public class Http11Processor implements Runnable, Processor {

    private static final Logger log = LoggerFactory.getLogger(Http11Processor.class);

    private final Socket connection;

    public Http11Processor(final Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        log.info("connect host: {}, port: {}", connection.getInetAddress(), connection.getPort());
        process(connection);
    }

    @Override
    public void process(final Socket connection) {
        try (final var inputStream = new InputStreamReader(connection.getInputStream());
             final var outputStream = connection.getOutputStream();
             final var bufferedReader = new BufferedReader(inputStream)) {

            // GET /index.html HTTP1.1
            // 이렇게 들어오는 것을 분리
            String[] inputs = bufferedReader.readLine()
                    .split(" ");
            String method = inputs[0];
            String path = inputs[1];

            final var responseBody = generateResponseBody(method, path);

            final var response = String.join("\r\n",
                    "HTTP/1.1 200 OK ",
                    "Content-Type: text/html;charset=utf-8 ",
                    "Content-Length: " + responseBody.getBytes().length + " ",
                    "",
                    responseBody);

            outputStream.write(response.getBytes());
            outputStream.flush();
        } catch (IOException | UncheckedServletException e) {
            log.error(e.getMessage(), e);
        }
    }

    private String generateResponseBody(final String method, final String path) throws IOException {
        // base URL 이라면 문자열 반환
        if ("GET".equals(method) && "/".equals(path)) {
            return "Hello world!";
        }

        // index.html 반환
        if ("GET".equals(method) && "/index.html".equals(path)) {
            final File file = new File(Objects.requireNonNull(getClass()
                            .getClassLoader()
                            .getResource("static" + path))
                    .getFile());

            return new String(Files.readAllBytes(file.toPath()));
        }

        return "";
    }


}
