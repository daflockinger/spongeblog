#!/bin/bash
sleep 15
mongo test --eval "db.createUser({user:'root', pwd:'flo',roles:[{ role: 'readWrite', db: 'test' }]});"