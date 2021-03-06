# Hermeez

## Automatic cycle route builder

### Description
Hermeez generates routes for cyclists according to the distance the user chooses, the weather conditions, and the type of roads.

It is similar to Strava's Route Builder, Garmin's RouteCourse or Under Armour's MapMyRide.

### Tech Stack

| Role              | Component       | Implementation   |
|:-----------------:|:---------------:|:----------------:|
| Client interface  | Mobile App      | Android (Java)   |
| Web service       | REST API        | Spring (Java)    |
| Business logic    | Routing Engine  | OSRM (C++)       |
