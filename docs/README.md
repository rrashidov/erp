# erp

## TOC

- [Intro](#Intro)
- [Features](#Features)
- [Domain Model](#DomainModel)
- [UI](#UI)
- [Processes](#Processes)
- [Architecture](#Architecture)
- [Caveats](#Caveats)
- [Roadmap](#Roadmap)

## Intro

*erp* is a project which has two main goals:

* put in practice some distributed computing princinples;
* put in practice some Spring projects (Spring Boot, Spring Web, Sprint Data JPA, etc.);

To achieve these goals, *erp* is implemented as Java web application. It covers a very limited part of an [ERP](https://en.wikipedia.org/wiki/Enterprise_resource_planning) domain. See [Features](#Features) sections for more details regading implemented functionality.

## Features

The application provides the following set of features:

* inventory management:
  * manage a catalog of Items;
  * manage the inventory of Items;
  * manage movement (sales and purchases) of Items;
* sales management:
  * manage a catalog of Customers;
  * manage customer payments and refunds;
  * manage Sales Orders;
  * manage Sales Credit Memos;
  * manage Sales Order and Credit Memo posting;
  * manage Posted Sales Orders;
  * manage Posted Sales Credit Memos;
* purchases management
  * manage a catalog of Vendors
  * manage vendor payments and refunds;
  * manage Purchase Orders;
  * manage Purchase Credit Memos;
  * manage Purchase Order and Credit Memo posting;
  * manage Posted Purchase Orders;
  * manage Posted Purchase Credit Memos;
* financeial assets management
  * manage a catalog of Bank Accounts;
  * manage a catalog of Payment Methods;
  * managet bank accounts operations - payments, refunds, inter balance transfers;

## Domain Model

In order to provide the features describe above, the application is centered around a domain model and the objects it is comprised of.

![DomainModel.png](./assets/DomainModel.png)

## UI

## Processes

## Architecture

## Caveats

## Roadmap