CREATE TABLE Bidder (BidderID varchar(40) NOT NULL, Rating int,  Location varchar(256), Country varchar(256), 
			PRIMARY KEY(BidderID));

CREATE TABLE Seller (SellerID varchar(40) NOT NULL, Rating int, PRIMARY KEY(SellerID));

CREATE TABLE Item (ItemID varchar(40) NOT NULL, Name varchar(256), Description varchar(4000),
			SellerID varchar(40), Started timestamp, Ends timestamp, Currently decimal(8,2),
			BuyPrice decimal(8,2), FirstBid decimal(8,2), NumBids int,
			Location varchar(256), Country varchar(256), Latitude varchar(256), Longitude varchar(256),
			FOREIGN KEY (SellerID) REFERENCES Seller(SellerID), PRIMARY KEY(ItemID));

CREATE TABLE Bid (ItemID varchar(40) NOT NULL, BidderID varchar(40) NOT NULL, Time timestamp NOT NULL,
			Amount decimal(8,2), FOREIGN KEY (ItemID) REFERENCES Item(ItemID),
			PRIMARY KEY(ItemID, BidderID, Time));

CREATE TABLE ItemCategory (ItemID varchar(40) NOT NULL, Category varchar(256), 
			FOREIGN KEY (ItemID) REFERENCES Item(ItemID));
