# Shoe-Store

Concurrency and Synchronization practice as part of SPL course.

Implemented using Micro-Services Framework Architecture with Message-Bus.

The Shoe Store project is demonstrating a shoe store that handles different costumers who want to buy shoes by types (spontaneously or by specific scheduled times) by their private wishlists - the store is selling if the shoe is in stock or ordering from the factory if it is not, and then notifying the costumers and selling them the requested shoe. 
Specific 'sale' times are appointed by the store manager for discounts of different shoes in the store for limited times.
Factory managment is done behind the scenes when there is an options for multiple factories working together for utilization.

All the system is working with Micro-Services that are running threads that communicate with each other using messages and callbacks.
For example there is a Micro-Service for a request of a customer for buying a specific shoe form the store that triggers a Micro-Service for a request of manufacturing a several amount of that shoe in the factory (because there are non in stock) and that request will enter the queue in the factory and only when it will be manufactured the request will be completed then the buying request of the costumer could also be completed.

Input parameters are pulled from a JSON file - such as customers and thier wishlists, store stock, amount of factories, amount of sellers, sale times etc.
