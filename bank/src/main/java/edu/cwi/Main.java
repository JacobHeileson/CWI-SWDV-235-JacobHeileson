package edu.cwi;
import io.javalin.http.staticfiles.Location;
import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {

        var app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);

            config.routes.post("/account", ctx -> {
                ctx.redirect("/account.html");
        });

            config.routes.get("/logout", ctx -> {
                ctx.redirect("/");
        });
    });

        app.start(7000);
    }
}
