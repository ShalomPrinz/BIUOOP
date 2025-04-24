#!/bin/bash

# Configuration
SRC_DIR="src"
TEST_DIR="test"
BUILD_DIR="test/build"
CLASSES_DIR="$BUILD_DIR/classes"
TEST_CLASSES_DIR="$BUILD_DIR/test-classes"
CLASSPATH="bin:biuoop-1.4.jar"
JUNIT_JAR="junit-jupiter-api-5.8.2.jar:junit-jupiter-engine-5.8.2.jar:junit-platform-console-standalone-1.8.2.jar"

# Create build directories if they don't exist
mkdir -p $CLASSES_DIR
mkdir -p $TEST_CLASSES_DIR

# Check if JUnit jars exist, download if they don't
if [ ! -f "junit-platform-console-standalone-1.8.2.jar" ]; then
    echo "Downloading JUnit..."
    wget -q https://repo1.maven.org/maven2/org/junit/platform/junit-platform-console-standalone/1.8.2/junit-platform-console-standalone-1.8.2.jar
    echo "JUnit downloaded."
fi

# Compile the source files
echo "Compiling source files..."
javac -d $CLASSES_DIR -cp $CLASSPATH $SRC_DIR/*.java
if [ $? -ne 0 ]; then
    echo "Error compiling source files!"
    exit 1
fi

# Compile the test files
echo "Compiling test files..."
javac -d $TEST_CLASSES_DIR -cp "$CLASSES_DIR:$CLASSPATH:$JUNIT_JAR" $TEST_DIR/*.java
if [ $? -ne 0 ]; then
    echo "Error compiling test files!"
    exit 1
fi

# Function to run a specific test
run_test() {
    local TEST_CLASS=$1
    echo "Running test: $TEST_CLASS"
    java -cp "$CLASSES_DIR:$TEST_CLASSES_DIR:$CLASSPATH:$JUNIT_JAR" org.junit.platform.console.ConsoleLauncher --select-class $TEST_CLASS
}

# Check if a specific test is specified
if [ -z "$1" ]; then
    # No test specified, run all tests
    echo "Running all tests..."
    for TEST_FILE in $TEST_DIR/*.java; do
        # Extract the class name without .java extension
        TEST_CLASS=$(basename $TEST_FILE .java)
        run_test $TEST_CLASS
    done
else
    # Run the specified test
    run_test $1
fi

echo "Testing completed."