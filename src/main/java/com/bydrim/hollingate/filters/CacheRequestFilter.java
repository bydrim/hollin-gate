package com.bydrim.hollingate.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class CacheRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(
            HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // allow 10kb request body
        filterChain.doFilter(new CachedBodyHttpServletRequest(request, 10240), response);
    }

    private static class CachedBodyServletInputStream extends ServletInputStream {
        private static final Logger logger = LoggerFactory.getLogger(CachedBodyServletInputStream.class);
        private InputStream internalInputStream;

        public CachedBodyServletInputStream(byte[] cachedBody) {
            this.internalInputStream = new ByteArrayInputStream(cachedBody);
        }

        @Override
        public boolean isFinished() {
            try {
                return internalInputStream.available() == 0;
            } catch (IOException e) {
                logger.error("Error checking availability of internalInputStream: ", e);
                return true;
            }
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
        }

        @Override
        public int read() throws IOException {
            return internalInputStream.read();
        }
    }

    private static class CachedBodyHttpServletRequest extends HttpServletRequestWrapper {
        private byte[] cachedBody;

        /**
         * Constructs a request object wrapping the given request.
         *
         * @param request The request to wrap
         * @param size    The size of the byte array of the request body
         * @throws IllegalArgumentException if the request is null
         */
        public CachedBodyHttpServletRequest(HttpServletRequest request, int size) throws IOException {
            super(request);
            ByteArrayOutputStream output = new ByteArrayOutputStream(size);
            StreamUtils.copy(request.getInputStream(), output);
            cachedBody = output.toByteArray();
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new CachedBodyServletInputStream(cachedBody);
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream(), StandardCharsets.UTF_8));
        }
    }
}
