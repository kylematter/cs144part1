1. These are the relations in my proposed schema:
Item(ItemID, Name, Description, SellerID foreign key Seller(SellerID), Started, Ends, Currently, BuyPrice, FirstBid, NumberOfBids, 
	Location, Country, Latitude, Longitude) Primary Key: ItemID
Bid(ItemID foreign key Item(ItemID), BidderID foreign key Bidder(BidderID), Time, Amount) Primary Keys: ItemID, BidderID, Time
ItemCategory(ItemID foreign key Item(ItemID), Category) Primary Keys: ItemID, Category
Bidder(BidderID, Location, Country, Rating) Primary Key: BidderID
Seller(SellerID, Rating) Primary Key: SellerID

2, While there are not any nontrivial functional dependencies, my keys have the following relations:
ItemID -> Name, Description, SellerID, Started, Ends, Currently, FirstBid, BuyPrice, NumberOfBids,
	  Location, Country, Latitude, Longitude
ItemID, Time, BidderID -> Amount
ItemID -> Category
SellerID -> Rating
BidderID -> Rating, Location, Country

3. Yes they are in BCNF.

4. Yes they are in 4NF.

