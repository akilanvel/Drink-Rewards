# Drink-Rewards

Drink Rewards is an application that computes the statistics of consumers at a drink store and stores them. A consumer can either be a regular customer, a gold customer, or a platinum customer. A consumer is a regular customer until he/she spends at least $50.00

Gold Customer:
Gold customers are split into three categories. A gold customer is a consumer who has spent between $50.00 and $200.00 at the store. If they have spent less than $100.00, they have a discount of 5% on all orders. If they have spent less than $150.00, they have a discount of 10% on all orders. If they have spent less than $200.00, they have a discount of 15% on all orders. 

Platinum Customer:
Platinum customers do not have discount percentages like gold customers do - they have bonus bucks. For every $5.00 spent after reaching a total of $200.00 spent, a platinum customer gains 1 bonus buck. For example, if a platinum customer has spent $205.23, has 1 bonus buck, and then places an order for $4.00, he/she gets one bonus buck on their next order and moves to $208.23 total spent.

Customers can order either tea, soda, or punch of sizes L, M, or S. Any quantity is allowed. Furthermore, the consumer can choose to create a design on the outside of the cup. When ordering, the price per square inch for the design is established and applied depending on the size of the drink. Each drink and size has a different cost, which is outlined in the program's source code. 

Input:
The program reads in all the regular customers from the customers.dat file and all the preferred customers from the preferred.dat file (which may not exist). If it doesn't exist and a customer gets promoted, it is created. A list of all orders are in the orders.dat file. An order is formatted as follows: "IDNumber typeOfDrink size pricePerSquareInchOfDesign quantity"

Output:
The program computes the orders of all the customers that have ordered something and updates the customer.dat file and preferred.dat file. The total amount spent is changed, the standing of a customer is changed (if they get promoted), and the number of bonus bucks or discount percentage may change. If a customer gets promoted, they get the increased discount of their new standing on their current order. Therefore, there can be a customer that is gold with an amountspent of $49.00. 
