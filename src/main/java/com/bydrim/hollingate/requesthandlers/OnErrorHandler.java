package com.bydrim.hollingate.requesthandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Component
public class OnErrorHandler {
    private static final Logger logger = LoggerFactory.getLogger(OnErrorHandler.class);

    private static String throwableToString(Throwable t) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        t.printStackTrace(new PrintStream(out));
        return out.toString(StandardCharsets.UTF_8);
    }

    public ServerResponse onError(Throwable throwable, ServerRequest req) {
        logger.error("Uncaught exception: ", throwable);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error!");
    }

    public ServerResponse onErrorDetailed(Throwable throwable, ServerRequest req) {
        logger.error("Uncaught exception: ", throwable);
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).body(throwableToString(throwable));
    }
}
