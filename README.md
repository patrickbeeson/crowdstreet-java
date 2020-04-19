# crowdstreet-java

This is a Java program that solves the Java portion of the Crowdstreet assignment.

It generates a randomized data set, containing the numbers 1-20, with the following distribution:
 - 1-12: 83000
 - 13: 1000
 - 14: 500
 - 15: 250
 - 16: 100
 - 17: 50
 - 18: 25
 - 19: 10
 - 20: 5

The only requirement is that no consecutive elements are equal.

## Approach

The approach I took was to construct an initial ordering of the data that meets the requirements (which is the tricky part)
and then randomize the list while keeping the constraints valid. The construction works as long as one value doesn't make
up more than 50% of all the values.

Randomization is done by calling swap on 2 randomly chosen elements, and only swapping if the constraints aren't violated.

I call swap about 10x the size of the list, which seems reasonable, but can also be increased if needed.

## Test

 - `./gradlew test`

## Run

 - `./gradlew jar`
 - `java -jar build/libs/crowdstreet-java-0.1.jar`
