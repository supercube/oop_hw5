
all:
	javac -cp src -d . src/highwaysimulate/*.java
	jar cvfm hw5.jar manifest.mf highwaysimulate/*.class
