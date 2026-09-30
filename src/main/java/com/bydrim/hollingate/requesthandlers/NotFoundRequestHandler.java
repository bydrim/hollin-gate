package com.bydrim.hollingate.requesthandlers;

import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

public class NotFoundRequestHandler {
    public static ServerResponse viewNotFound(ServerRequest req) {
        return ServerResponse.status(HttpStatus.NOT_FOUND).body("Not Found!");
    }
}
