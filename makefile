all:
	javac -cp src -d . src/highwaysimulate/*.java src/cars/*.java
	jar cvfm hw5.jar manifest.mf highwaysimulate/*.class cars/*.class
	rm -rf highwaysimulate cars
clean:
	rm -rf hw5.jar
