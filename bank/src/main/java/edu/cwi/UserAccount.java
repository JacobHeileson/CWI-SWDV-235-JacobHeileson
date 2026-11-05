package edu.cwi;

import java.util.Map;
import java.text.DecimalFormat;

public record UserAccount (String name, Double balance, Map<String, String> transactions) {

    public String formatBalance() {
        DecimalFormat df = new DecimalFormat("$#,###.00");
        return df.format(this.balance);
    }
}
