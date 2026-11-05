package edu.cwi;

import io.javalin.apibuilder.CrudHandler;
import io.javalin.http.Context;
import static j2html.TagCreator.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TransactionsController implements CrudHandler {
private final Map<String, String> transactions = new ConcurrentHashMap<>();
private int nextId = 1;

    @Override
    public void getAll(Context ctx) {
        UserAccount account = new UserAccount("Checking Account", 0.0, transactions);
        ctx.render("jte/accountPage.jte",
            Map.of("title", "Account Details", "account", account));
    }

    @Override
    public void getOne(Context ctx, String resourceId) {

        String transaction = transactions.get(resourceId);

        if (transaction != null) {
            ctx.html(
                html(
                    body(
                        h1("Transaction " + resourceId),
                        p("Amount: " + transaction )
                    )
                ).render()
            );
        } else {
            ctx.status(404).result("Transaction not found");
        }
    }

    @Override
    public void create(Context ctx) {

        String description = ctx.formParam("description");
        String amount = ctx.formParam("amount");
        String body = description + " - $" + amount;
        String id = String.valueOf(nextId++);

        transactions.put(id, body);
		ctx.redirect("/transactions");
    }

    @Override
    public void update(Context ctx, String resourceId) {

        if (transactions.containsKey(resourceId)) {
            String description = ctx.formParam("description");
            String amount = ctx.formParam("amount");
            String body = description + " - $" + amount;

            transactions.put(resourceId, body);
            ctx.redirect("/transactions");
        } else {
            ctx.status(404).result("Transaction not found");
        }
    }

    @Override
    public void delete(Context ctx, String resourceId) {

        if (transactions.remove(resourceId) != null) {
            ctx.redirect("/transactions");
        } else {
            ctx.status(404).result("Transaction not found");
        }
    }
}
