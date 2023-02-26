#!/usr/bin/env bash

# *** ** "container_name" psql -U "POSTGRES_USER" "db_name"
docker exec -it simple_akka_slick_c psql -U postgres -d simple_akka_slick_db
