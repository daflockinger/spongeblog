#!/bin/bash
sleep 15
mongo test --eval 'db.addUser("root", "flo");'