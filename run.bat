
mvn gatling:test -Dgatling.simulationClass=load.tests.ReqResAPITest -Durl=https://reqres.in -DkgName=bush_25x_470_beta_2 -DisThinkEnabled=false -DrunType=BaseLineTest -Duser1Volume=30 -Duser2Volume=30 -Duser3Volume=30 -Duser4Volume=1 -Duser5Volume=6 -Dtotal1Duration=150 -Dtotal2Duration=180 -Dtotal3Duration=90 -Dtotal4Duration=60 -Dtotal5Duration=10 -DdropDown1Volume=100 -DdropDown2Volume=100
