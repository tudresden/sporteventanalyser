#!/bin/bash

mkdir -p ./js
coffee -j js/engine.js -c coffee/*.coffee || exit 1
