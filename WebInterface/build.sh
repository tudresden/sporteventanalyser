#!/bin/bash

function AssertSuccess {
  if [ $? -ne 0 ]; then
    echo "Died at line $1";
    exit 1;
  fi
}

if [[ "$1" == "help" ]]; then
  echo "USAGE:"
  echo
  echo "$0 all    -   build everything"
  echo "$0 img    -   convert images aswell"
  echo "$0 help   -   show this help"
  exit 0
fi

mkdir -p ./js
coffee -j js/engine.js -c coffee/*.coffee
AssertSuccess $LINENO
lessc --yuicompress less/style.less css/style.css
AssertSuccess $LINENO

if [[ "$1" == "all" ]] || [[ "$1" == "img" ]]; then
  cd img
  for f in $(ls *.svg | grep -o "^[^\.]\+"); do
    inkscape -D -e "$f.png" -f "$f.svg";
    AssertSuccess $LINENO
  done
  cd ..
fi
