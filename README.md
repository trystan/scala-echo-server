# scala-echo-server

A sample project to play around with some things I want to learn more about.

## Roadmap

[+] Initial scala echo server
  * `GET /api/echo` endpoint that returns info about the request
  * Support other methods (`POST`, etc)

[-] Add auth

[-] Add history for each user
  * Store requests in a `mongo` collection for 24 hours
  * `GET /api/history` to see your requests

[-] Add stats for each user
  * `GET /api/stats` to see your total stats

[-] Refactor to microservices or lambdas
  * `LocalStack`: `sns` and `sqs` services
  * Use `Terraform` to set up `LocalStack` stuff