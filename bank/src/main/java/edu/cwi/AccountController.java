package edu.cwi;

import io.javalin.http.Context;
import java.util.Optional;

public class AccountController {

    public static void renderAccount(Context ctx) {
        // 1. Grab the user ID out of the session cookie store
        String loggedInId = ctx.cookieStore().get("user-id");

        if (loggedInId == null) {
            ctx.redirect("/unauthorized.html");
            return;
        }

        // 2. Fetch the corresponding account record from our mocked memory database
        Optional<UserAccount> accountOpt = AuthController.getUser(loggedInId);

        if (accountOpt.isPresent()) {
            UserAccount account = accountOpt.get();
            
            // 3. Render your account template (or send a raw HTML snippet for easy validation)
            ctx.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Bank Account Dashboard</title>
                </head>
                <body>
                    <h1>Welcome back, %s!</h1>
                    <h2>Current Balance: %s</h2>
                    <h3>Recent Transactions:</h3>
                    <ul>
                        %s
                    </ul>
                    <hr>
                    <a href="/logout">Logout</a>
                </body>
                </html>
            """.formatted(
                account.name(), 
                account.formatBalance(), // Calls your custom DecimalFormat helper
                buildTransactionListHtml(account.transactions())
            ));
        } else {
            ctx.redirect("/unauthorized.html");
        }
    }

    private static String buildTransactionListHtml(java.util.Map<String, String> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return "<li>No recent ledger entries found.</li>";
        }
        StringBuilder sb = new StringBuilder();
        for (java.util.Map.Entry<String, String> entry : transactions.entrySet()) {
            sb.append("<li><strong>ID #")
              .append(entry.getKey())
              .append(":</strong> ")
              .append(entry.getValue())
              .append("</li>");
        }
        return sb.toString();
    }
}
