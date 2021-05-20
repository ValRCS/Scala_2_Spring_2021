--SELECT *,
--SUM(Total) OVER (
--        ORDER BY InvoiceDate
--    ) RunningTotal
--FROM invoices i

--TODO is to get running total by months

--SELECT SUM(transaction) as Price,
--       strftime("%m-%Y", transDate) as 'month-year'
--       from transaction group by strftime("%m-%Y", transDate);

--SELECT SUM(Total) as MonthlySales,
--COUNT(InvoiceId) as SaleCount,
--STRFTIME("%Y-%m", InvoiceDate) as 'Year-Month',
--MIN(InvoiceDate) as FirstInvoice
--FROM invoices i
--GROUP BY STRFTIME("%Y-%m", InvoiceDate)
--ORDER BY 'Year-Month';

--TODO create a running total month by month

--CREATE VIEW v_Sales
--AS
--SELECT SUM(Total) AS MSales,
--COUNT (InvoiceId) AS SalesCount,
--STRFTIME ("%Y-%m", InvoiceDate) AS "Year-Month",
--MIN (InvoiceDate) AS FirstInvoice
--FROM invoices i2
--GROUP BY STRFTIME("%Y-%m", InvoiceDate)
--ORDER BY "Year-Month";

--SELECT * FROM v_Sales ;
--
--SELECT *, SUM(MSales) OVER (
----	ORDER BY "Year-Month"
--	ORDER BY FirstInvoice
--) RunningTotal,
--CUME_DIST() OVER (ORDER BY FirstInvoice)
--CumDistr
--FROM v_Sales;

--SELECT *,
--DENSE_RANK() OVER (
--ORDER BY Composer ) DenseRanking,
--RANK() OVER (
--ORDER BY Composer) Ranking
--FROM v_long_songs vls ;

--SELECT COUNT(*) FROM v_tracks;

--creating 350 buckets for our 3500+ tracks
--SELECT *, NTILE(350) OVER (
--ORDER BY vt.TrackId
--) MyBucket,
--ROW_NUMBER() OVER (
--ORDER BY vt.TrackId
--) RowNumber
--FROM v_tracks vt;

--so we create row numbers for each country ordered by city
--SELECT *, ROW_NUMBER() OVER (
--PARTITION BY i.BillingCountry
--ORDER BY i.BillingCity
--) RowNumberByCountry
--FROM invoices i ;

--https://www.sqlitetutorial.net/sqlite-window-functions/sqlite-row_number/

--pagination example on how to create a 3rd page of results
--SELECT * FROM (
--SELECT ROW_NUMBER() OVER (
--ORDER BY c.LastName) myRow,
--c.FirstName, c.LastName , c.Country
--FROM customers c )
--WHERE myRow >= 21 AND myRow <= 30;

--we could have used LIMIT and OFFSET but then we would not have row numbers

--SELECT Country, LastName, City,
--PERCENT_RANK() OVER (
--ORDER BY c.Country) percRank
--FROM customers c;

--SELECT
--    AlbumId,
--    Name,
--    Bytes,
--    printf('%.2f',PERCENT_RANK() OVER(
--        PARTITION BY AlbumId
--        ORDER BY Bytes
--    )) SizePercentRank
--FROM
--    tracks;

--SELECT
--    Name,
--    printf('%,d',Bytes) Size,
--    FIRST_VALUE(Name) OVER (
--        ORDER BY Bytes
--    ) AS SmallestTrack
--FROM
--    tracks
--WHERE
--    AlbumId = 1;

SELECT AlbumId, Name, printf('%,d',Bytes) Size,
FIRST_VALUE(Name) OVER (
	PARTITION BY AlbumId
	ORDER BY Bytes DESC) BiggieTrack,
LAST_VALUE(Name) OVER (
	PARTITION BY AlbumId
	ORDER BY Bytes DESC) MiniTrack
FROM tracks t
ORDER BY Name DESC;
