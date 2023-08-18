#!/bin/bash
for (( i = 1; i < 1000000; i++ )); do
      curl 47.98.144.206/hellorest?name=lisi
      echo
      curl 47.98.144.206/hellorest?name=jinfeng
      echo
      b=`expr $i % 30`
      if [ $b -eq 0 ]; then
          echo $b
          sleep 3
      fi
done