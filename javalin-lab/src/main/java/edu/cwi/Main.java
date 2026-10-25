
package edu.cwi;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        var app = Javalin.create(config -> {
            config.routes.get("/hello/{name}", ctx -> {
                String name = ctx.pathParam("name");
                ctx.result("Hello, " + name);
            });

            config.routes.get("/hello/{name}/html", ctx -> {
                String name = ctx.pathParam("name");

                ctx.html("<html>\n" +
                    "  <head>\n" +
                    "    <title>Javalin Intro</title>\n" +
                    "  </head>\n" +
                    "  <body>\n" +
                    "    <h1>Hello, " + name + "</h1>\n" +
                    "  </body>\n" +
                    "</html>");
            });
        });

        app.start(7070);
    }
}
