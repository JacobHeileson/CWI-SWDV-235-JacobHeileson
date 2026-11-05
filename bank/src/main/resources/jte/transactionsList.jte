<%-- Java imports --%>
@import java.util.Map 

<%-- Parameters --%>
@param Map<String, String> transactions

<%-- This must start with @template. --%>
<h1>Transactions</h1>
<ul>
<%-- Loop through the transactions map --%>
@for(var entry : transactions.entrySet())
    <li>${entry.getKey()}: ${entry.getValue()}</li>
@else
    <li>You have no transactions to display.</li>
@endfor
</ul>