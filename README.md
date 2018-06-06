# Shoe-Store

Concurrency and Synchronization practice as part of SPL course.

Implemented using Micro-Services Framework Architecture with Message-Bus.

The Shoe Store project is demonstrating a shoe store that handles different costumers who want to buy shoes by types (spontaneously or by specific scheduled times) by their private wishlists which can even specify to buy a specific shoe only when it is under discount. The seller is selling a shoe if it is in stock or ordering from the factory if it is not, and when the factory is done the seller is notifying the costumer and selling them the requested shoe. 
Specific 'sale' times are appointed by the store manager's discount-schedule for discounts of different shoes in the store for limited times.
Factory managment is done behind the scenes when there is an options for multiple factories working together for utilization.

All the system is working with Micro-Services that are running threads that communicate with each other using messages and callbacks.
For example there is a Micro-Service for a request of a customer for buying a specific shoe form the store that triggers a Micro-Service of a seller that handles purchase requestes (by subscribing in the message bus to this type of messages) and then it creates another Micro-Service for a new request of manufacturing a several amount of that specific shoe in a factory (because there are non of it in stock) and that request will enter the queue in the factory and only when it will be manufactured the request will be completed then the buying request of the costumer could also be completed.

Input parameters are pulled from a JSON file in the 'app' folder - such as customers and thier wishlists, store initial stock, amount of factories, ampunt of sellers in the store, discount schedule etc.
