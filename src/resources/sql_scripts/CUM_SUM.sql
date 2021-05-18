--SELECT *,
--SUM(Total) OVER (
--        ORDER BY InvoiceDate
--    ) RunningTotal
--FROM invoices i

--TODO is to get running total by months

--SELECT SUM(transaction) as Price,
--       strftime("%m-%Y", transDate) as 'month-year'
--       from transaction group by strftime("%m-%Y", transDate);

SELECT SUM(Total) as MonthlySales,
COUNT(InvoiceId) as SaleCount,
STRFTIME("%Y-%m", InvoiceDate) as 'Year-Month',
MIN(InvoiceDate) as FirstInvoice
FROM invoices i
GROUP BY STRFTIME("%Y-%m", InvoiceDate)
ORDER BY 'Year-Month';

--TODO create a running total month by month