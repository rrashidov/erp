# Changelog

## v0.0.5

- update RabbitMQ version to 3.12.6
- update MySQL version to 8.1.0
- run backend and frontend with SapMachine 17
- compile Java components with Java 17

## v0.0.4

- fix typos in README
- fix issue in GeneralJournalBatchLine REST API
- improve GeneralJournalBatch REST API - return 404 for non existing entities
- improve SalesOrder REST API - return 404 for non existing entities
- improve SalesCreditMemo REST API - return 404 for non existing entities
- improve PurchaseOrder REST API - return 404 for non existing entities
- improve PurchaseCreditMemo REST API - return 404 for non existing entities
- implement REST API integration tests
- enable frontend http access logging
- enable backend http access logging
- enable frontend trace logging
- enable backend trace logging
- remove calculated fields from db schema
- update RabbitMQ version to 3.12.2
- update MySQL version to 8.0.34
- preserve current page when creating document lines
