@param String title
@param edu.cwi.UserAccount account

@template.accountLayout(
	title = title,
	account = @template.userAccount(account = account),
	detail = @template.transactionsList(transactions = account.transactions())
)
