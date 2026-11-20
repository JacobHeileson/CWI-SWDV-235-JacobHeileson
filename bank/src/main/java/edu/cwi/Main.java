package edu.cwi;

import io.javalin.http.staticfiles.Location;
import io.javalin.Javalin;
import io.javalin.rendering.template.JavalinJte;
import static io.javalin.apibuilder.ApiBuilder.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {

        TransactionsController transactionsController = new TransactionsController();
        UsersController usersController = new UsersController();

        var app = Javalin.create(config -> {

            config.fileRenderer(new JavalinJte());

            config.staticFiles.add(staticFiles -> {
                staticFiles.hostedPath = "/public";
                staticFiles.location = Location.CLASSPATH;
            });

            config.routes.apiBuilder(() -> {

                before(ctx -> {
                    var permittedRoles = ctx.routeRoles();
                
                    if (permittedRoles.isEmpty()) {
                        return;
                    }

                    AppRole userRole = ctx.cookieStore().get("user-role");

                    if (userRole == null || !permittedRoles.contains(userRole)) {
                        ctx.redirect("/public/unauthorized.html");
                    }
                });

                // Redirect root URL (http://localhost:7070/) to your starting page
                get("/", ctx -> ctx.redirect("/public/index.html"));

                post("/login", AuthController::authenticate);
                get("/logout", AuthController::logout);

                path("/account", List.of(AppRole.CUSTOMER, AppRole.ADMIN), () -> {
                    get(AccountController::renderAccount);
                });

                path("/transactions", () -> {
                    crud("{id}", transactionsController);

                    post("/update", ctx ->
                        transactionsController.update(ctx, ctx.formParam("id"))
                    );

                    post("/delete", ctx ->
                        transactionsController.delete(ctx, ctx.formParam("id"))
                    );
                });

                path("/users", () -> {
                    crud("{id}", usersController);
                });
            });
        });

        app.start(7070);
    }
}