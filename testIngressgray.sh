#!/bin/bash
for (( i = 1; i < 1000000; i++ )); do
      curl -H "name:lisi" xl.mse.test/hellorest
      echo ""
      curl -H "name:jinfeng" xl.mse.test/hellorest
      echo ""
      b=`expr $i % 100`
      if [ $b -eq 0 ]; then
          echo $b
      fi
done