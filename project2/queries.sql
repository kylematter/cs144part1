SELECT COUNT(*) FROM ((SELECT SellerID FROM Seller) UNION (SELECT BidderID FROM Bidder)) as Users;

SELECT COUNT(*) FROM Item WHERE BINARY Location = 'New York';

SELECT COUNT(*) FROM (SELECT ItemID FROM ItemCategory GROUP BY ItemID HAVING COUNT(*) = 4) as FourCount;

SELECT ItemID FROM Item WHERE NumBids > 0 AND Started < "2001-12-20 00:00:00" AND Ends > "2001-12-20 00:00:01" AND
Currently = (SELECT MAX(CURRENTLY) FROM Item WHERE NumBids > 0 AND Started < "2001-12-20 00:00:00" AND ends > "2001-12-20 00:00:01");

SELECT COUNT(*) FROM Seller WHERE Rating > 1000;

SELECT COUNT(*) FROM Seller S, Bidder B WHERE S.SellerID = B.BidderID;

SELECT COUNT(*) FROM (SELECT COUNT(*) FROM ItemCategory cat, Bid b WHERE cat.ItemID = b.ItemID AND b.Amount > 100 
GROUP BY cat.Category) as Cats;
