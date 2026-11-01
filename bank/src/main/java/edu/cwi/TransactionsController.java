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
    ctx.html(
        html(
            body(
                h1("Bank Transactions"),

                ul(
                    each(transactions.entrySet(), transaction ->
                        li("ID: " + transaction.getKey() +
                            " - " + transaction.getValue())
                    )
                ),

                h2("Create Transaction"),

                form()
                    .withAction("/transactions")
                    .withMethod("post")
                    .with(
                        label("Description: "),
                        input().withName("description"),

                        label("Amount: "),
                        input().withName("amount"),

                        button("Create Transaction").withType("submit")
                    ),

                h2("Update Transaction"),

                form()
                    .withAction("/transactions/update")
                    .withMethod("post")
                    .with(
                        label("Transaction ID: "),
                        input().withName("id"),

                        label("New Description: "),
                        input().withName("description"),

                        label("New Amount: "),
                        input().withName("amount"),

                        button("Update Transaction").withType("submit")
                    ),
                    h2("Delete Transaction"),

                    form()
                        .withAction("/transactions/delete")
                        .withMethod("post")
                        .with(
                            label("Transaction ID: "),
                            input().withName("id"),

                            button("Delete Transaction").withType("submit")
                        )
            )
        ).render()
    );
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
