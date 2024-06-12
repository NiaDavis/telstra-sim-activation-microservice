# Telstra SIM Activation Microservice

A microservice for SIM card activation using Spring Boot, with persistence using H2 database.

## Overview

This microservice is designed to handle activation requests for SIM cards. It exposes a REST controller to receive JSON requests from Telstra store locations across Australia. Each request contains information about the SIM card and its new owner. Upon receiving a request, the microservice attempts to activate the SIM card by reaching out to another microservice. It records the outcome of the activation attempt in an H2 database.

## Features

- Exposes a REST API endpoint to handle SIM card activation requests.
- Forwards activation requests to an external microservice.
- Records activation outcomes (success/failure) in an H2 database.
- Provides an endpoint to query activation records.

## Technologies Used

- Java 11
- Spring Boot
- Spring Data JPA
- H2 Database
- Maven

## Endpoints

### POST /activate

Accepts a JSON payload to activate a SIM card.

#### Request Body

```json
{
    "iccid": "string",
    "customerEmail": "string"
}
