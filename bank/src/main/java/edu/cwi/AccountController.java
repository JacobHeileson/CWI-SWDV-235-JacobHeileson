package edu.cwi;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import org.apache.ibatis.session.SqlSession;
import io.javalin.http.Context;

public class AccountController {

    public static void renderAccount(Context ctx) {
        Integer loggedInId = ctx.cookieStore().get("user-id");

        if (loggedInId == null) {
            ctx.redirect("/public/unauthorized.html");
            return;
        }

        try (SqlSession session = MyBatisConfig.getSqlSessionFactory().openSession()) {
            AccountMapper accountMapper = session.getMapper(AccountMapper.class);
            TransactionMapper transactionMapper = session.getMapper(TransactionMapper.class);

            Account account = accountMapper.findByUserId(loggedInId);

            if (account == null) {
                ctx.redirect("/public/unauthorized.html");
                return;
            }

            List<Transaction> transactions = transactionMapper.findByAccountId(account.getId());
            String formattedBalance = NumberFormat.getCurrencyInstance(Locale.US).format(account.getBalance());

            ctx.html("""
                <!DOCTYPE html>
                <html>
                <head>
                    <title>Bank Account Dashboard</title>
                </head>
                <body>
                    <h1>Welcome back!</h1>
                    <h2>%s Account Balance: %s</h2>
                    <h3>Recent Transactions:</h3>
                    <ul>
                        %s
                    </ul>
                    <hr>
                    <a href="/logout">Logout</a>
                </body>
                </html>
            """.formatted(
                account.getAccountType(),
                formattedBalance,
                buildTransactionListHtml(transactions)
            ));
        }
    }

    private static String buildTransactionListHtml(List<Transaction> transactions) {
        if (transactions == null || transactions.isEmpty()) {
            return "<li>No recent ledger entries found.</li>";
        }
        NumberFormat currency = NumberFormat.getCurrencyInstance(Locale.US);
        StringBuilder sb = new StringBuilder();
        for (Transaction tx : transactions) {
            sb.append("<li><strong>ID #")
              .append(tx.getId())
              .append(" (")
              .append(tx.getTransactionType())
              .append("):</strong> ")
              .append(tx.getDescription())
              .append(" — ")
              .append(currency.format(tx.getAmount()))
              .append("</li>");
        }
        return sb.toString();
    }
}
