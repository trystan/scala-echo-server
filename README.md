# scala-echo-server

A sample project to play around with some things I want to learn more about. Basically, hello world with extra steps.

## Roadmap

[✔] Initial scala echo server
  * `GET /api/:userId/echo` endpoint that returns info about the request
  * Support other methods (`POST`, etc)

[❌] Add auth
  > After several attempts, I decided to just accept a  `userId` in the route and not worry about it. Auth is not something I can do in one of the rare 15 minutes I have to work on this. Even an in-memory collection with login and logout would take longer than I'd like. Maybe I'll come back to this later, but since that's not the real purpose of this project, I'm moving on. 

[✔] Add history for each user
  * Store requests in a `mongo` collection for 24 hours
  * `GET /api/:userId/history` to see your requests
  > I wasn't able to get the application lifecycle hooks to work so it doesn't set the ttl indexes on start and close the MongoClient on app close like I would prefer. I gave up after trying several different ways to do it that didn't work. 🤷‍♂️

[✔] Add stats for each user
  * `GET /api/:userId/stats` to see your total stats

[-] Refactor to microservices or lambdas
  * `LocalStack`: `sns` and `sqs` services
  * Use `Terraform` to set up `LocalStack` stuff