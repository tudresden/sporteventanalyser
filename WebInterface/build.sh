#!/bin/bash

mkdir -p ./js
coffee -j js/engine.js -c coffee/*.coffee || exit 1
lessc --yuicompress less/style.less css/style.css
