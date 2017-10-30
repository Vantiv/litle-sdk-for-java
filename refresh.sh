#!/bin/bash
# remove old generated classes and perform a Gradle build
rm build/generated/com/litle/sdk/generate/*.java && gradle cleanEclipse eclipse

