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

--SELECT a2.Title AlbumName, t.AlbumId, Name, printf('%,d',Bytes) Size,
--FIRST_VALUE(Name) OVER (
--	PARTITION BY t.AlbumId
--	ORDER BY Bytes DESC) BiggieTrack,
--LAST_VALUE(Name) OVER (
--	PARTITION BY t.AlbumId
--	ORDER BY Bytes DESC) MiniTrack
--FROM tracks t
--JOIN albums a2
--ON a2.AlbumId = t.AlbumId
--ORDER BY AlbumName DESC;

--CREATE VIEW v_CustomerInvoices
--AS
--SELECT
--	CustomerId,
--	strftime('%Y',InvoiceDate) Year,
--	SUM( total ) Total
--FROM
--	invoices
--	GROUP BY CustomerId, strftime('%Y',InvoiceDate);

--CREATE VIEW v_InvoicesLead
--AS
--SELECT CustomerId, Year, Total,
--	LEAD(Total,1,0 ) OVER (
--	PARTITION BY CustomerId
--	ORDER BY Year) NextYearTotal
--FROM v_CustomerInvoices;
--WHERE CustomerId = 1;
--ORDER BY
--	CustomerId,
--	Year,
--	Total;

--SELECT *, NextYearTotal - Total YearToYearDelta FROM v_InvoicesLead vil ;

--SELECT
--	CustomerId,
--	Year,
--	Total,
--	LAG ( Total, 1, 0 ) OVER (
--		ORDER BY Year
--	) PreviousYearTotal
--FROM
--	v_CustomerInvoices vci
--WHERE
--	CustomerId = 4;

--SELECT
--	CustomerId,
--	Year,
--	Total,
--	LAG ( Total,1,0) OVER (
--		PARTITION BY CustomerId
--		ORDER BY Year ) PreviousYearTotal,
--		Total - PreviousYearTotal Delta
--FROM
--	v_CustomerInvoices;

--SELECT
--    AlbumId, Name,
--    Milliseconds Length,
--    NTH_VALUE(name,2) OVER (
--        ORDER BY Milliseconds DESC
----        RANGE BETWEEN
----            UNBOUNDED PRECEDING AND
----            UNBOUNDED FOLLOWING
--    ) SecondLongestTrack
--FROM
--    tracks;

--SELECT
--    a2.Name Artist, a.Title AlbumName, t.AlbumId,
--    t.Name,
--    Milliseconds Length,
--    NTH_VALUE ( t.Name,2 ) OVER (
--        PARTITION BY t.AlbumId
--        ORDER BY Milliseconds DESC
--        RANGE BETWEEN
--            UNBOUNDED PRECEDING AND
--            UNBOUNDED FOLLOWING
--    ) AS SecondLongestTrack
--FROM
--    tracks t
--JOIN albums a
--ON t.AlbumId = a.AlbumId
--JOIN artists a2
--ON a.ArtistId = a2.ArtistId ;
--
--SELECT *,
--SUM(Total) OVER (
--	PARTITION BY CustomerId
--	ORDER BY Year
--) CumulativeTotal,
--COUNT(Total)  OVER (
--PARTITION BY CustomerId
--ORDER BY Year
--) YearsOfSales,
--AVG(Total)  OVER (
--PARTITION BY CustomerId
--ORDER BY Year
--) AverageSales
--FROM v_CustomerInvoices vci

--TODO Create View of Tracks lets call it v_RankedTracks
--WIth extra columns:
-- giving average length of song (among songs in the album)
-- song ranking number among the songs in the album from the shortest
-- first song of the album
-- number of songs in the album
-- and for extra kicks album real name and also artist

