[![codecov](https://codecov.io/gh/gigena-git/meli-coupon-challenge/branch/main/graph/badge.svg)](https://codecov.io/gh/gigena-git/meli-coupon-challenge)# Mercado Libre Coupon Challenge
Solution for Mercado Libre's Java + AWS Challenge

## Problem and Solution
The purpose of the code in this repo, is to provide the optimal coupon given a list of products and a maximum amount. For example, if we have this list of items:

```json
{
"item_ids": ["MLA1120809452", "MLA816019440", "MLA3", "MLA4", "MLA5"],
"amount": 166352.03
}
```
we should respond with:
```json
{
"item_ids": ["MLA1120809452", "MLA816019440"],
"amount": 166352.03
}
```

So, our problem is analog to the [0-1 Knapsack Problem](https://www.javatpoint.com/0-1-knapsack-problem), with the peculiarity that weight and value are the same.
In this implementation, the API will:
* Ask the MELI items API whether the items exists, and its price.
* Create a set of unique items, that will be evaluated against the maximum amount.
* Cast the 2 digit floating point prices into long integers - so the price will be viewed in terms of cents.
* Run the Dynamic Programming algorithm that will determine the maximum amount to spend, and determine the items that compose the optimal solution.
* Recast the amount to floating point.
* Return the list of items.

## Requirements
This solution requires Java 17. If you want to run the solution by yourself, you have to download the jar file located on the [packages section](https://github.com/gigena-git/meli-coupon-challenge/packages/1614325) of the repo, and in your local instance, run:

```bash
java -jar meli-coupon-challenge.jar
```

## Usage
I have mounted the jar file in ElasticBeanstalk. To run the solution, you need to curl to the endpoint. For example:

```bash
curl -o result.txt \
    --header "Content-Type: application/json" \
    --request POST \
    --data '{"item_ids": ["MLA1120809452", "MLA816019440", "MLA3", "MLA4", "MLA5"], "amount": 166352.03}' \
    http://melichallenge-env.eba-wx55xpus.us-east-1.elasticbeanstalk.com:8080/coupon
```
