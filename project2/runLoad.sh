#!/bin/bash

mysql CS144 < drop.sql

mysql CS144 < create.sql

ant
ant run-all

cat item.dat | uniq > tempItem.dat
cat tempItem.dat > item.dat

cat itemCategory.dat | uniq > tempCategory.dat
cat tempCategory.dat > itemCategory.dat

cat bidder.dat | uniq > tempBidder.dat
cat tempBidder.dat > bidder.dat

cat bid.dat | uniq > tempBid.dat
cat tempBid.dat > bid.dat

cat seller.dat | uniq > tempSeller.dat
cat tempSeller.dat > seller.dat

mysql CS144 < load.sql

rm *.dat
