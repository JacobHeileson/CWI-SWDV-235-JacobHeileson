
package edu.cwi;

import io.javalin.http.staticfiles.Location;
import io.javalin.Javalin;
import static io.javalin.apibuilder.ApiBuilder.*;

public class Main {
    public static void main(String[] args) {

        TransactionsController transactionsController = new TransactionsController();

        var app = Javalin.create(config -> {
            config.staticFiles.add("/public", Location.CLASSPATH);

            config.routes.post("/account", ctx -> {
                ctx.redirect("/account.html");
            });

            config.routes.get("/logout", ctx -> {
                ctx.redirect("/");
            });

            config.routes.apiBuilder(() -> {
                path("/transactions", () -> {
                    crud("{id}", transactionsController);

                    post("/update", ctx ->
                        transactionsController.update(ctx, ctx.formParam("id"))
                    );

                    post("/delete", ctx ->
                        transactionsController.delete(ctx, ctx.formParam("id"))
                    );
                });
            });
        });

        app.start(7000);
    }
}

