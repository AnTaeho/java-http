package cache.com.example;

import cache.com.example.version.ResourceVersion;
import java.time.Duration;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class GreetingController {

    private final ResourceVersion resourceVersion;

    public GreetingController(ResourceVersion resourceVersion) {
        this.resourceVersion = resourceVersion;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 인터셉터를 쓰지 않고 response에 직접 헤더값을 지정할 수도 있다.
     */
    @GetMapping("/cache-control")
    public String cacheControl(final HttpServletResponse response) {
        final String cacheControl = CacheControl
                .noCache()
                .cachePrivate()
                .getHeaderValue();
        response.addHeader(HttpHeaders.CACHE_CONTROL, cacheControl);
        return "index";
    }

    @GetMapping("/etag")
    public String etag(final HttpServletResponse response) {
        response.addHeader(HttpHeaders.ETAG, "etag");
        return "index";
    }

    @GetMapping("/resource-versioning")
    public String resourceVersioning(final HttpServletResponse response) {
        return "resource-versioning";
    }
}
