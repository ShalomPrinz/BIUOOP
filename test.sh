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

# Compile the source files
echo "Compiling source files..."
javac -d $CLASSES_DIR -cp $CLASSPATH $(find $SRC_DIR -name "*.java")
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

# Initialize summary variables
SUCCESSFUL_TESTS=0
FAILED_TESTS=0

run_test() {
    local TEST_CLASS=$1
    echo "Running test: $TEST_CLASS"
    OUTPUT=$(java -cp "$CLASSES_DIR:$TEST_CLASSES_DIR:$CLASSPATH:$JUNIT_JAR" org.junit.platform.console.ConsoleLauncher --select-class "$TEST_CLASS" --disable-banner)

    # Print general summary (optional)
    echo "$OUTPUT" | grep -E '^\[\s+[0-9]+ tests (successful|failed)\s+\]'

    # Find failed tests (✘ mark)
    local FAILED_METHODS
    FAILED_METHODS=$(echo "$OUTPUT" | grep '✘' | sed -E 's/.*├─ (.*)\(\).*/\1/')

    # Find your code locations only — ignore framework lines
    # Look for lines like 'YourClassName.java:123'
    local FAILURE_LOCATIONS
    FAILURE_LOCATIONS=$(echo "$OUTPUT" | grep -oE '[A-Za-z0-9_]+\.java:[0-9]+' | grep -v 'AssertionUtils\|AssertEquals\|Assertions')

    local FAILED_COUNT
    FAILED_COUNT=$(echo "$FAILED_METHODS" | grep -c .)

    if [[ "$FAILED_COUNT" -gt 0 ]]; then
        echo "❌  Failed tests in $TEST_CLASS:"

        # Pair method names with failure locations
        local METHOD
        local LOCATION
        local INDEX=1

        while read -r METHOD; do
            LOCATION=$(echo "$FAILURE_LOCATIONS" | sed -n "${INDEX}p")
            echo " - $METHOD (at $LOCATION)"
            INDEX=$((INDEX + 1))
        done <<< "$FAILED_METHODS"

        FAILED_TESTS=$((FAILED_TESTS + FAILED_COUNT))
    fi

    # Find successful tests (marked with ✔)
    SUCCESS_COUNT=$(echo "$OUTPUT" | grep -c '✔')

    if [[ "$SUCCESS_COUNT" -gt 0 ]]; then
        SUCCESSFUL_TESTS=$((SUCCESSFUL_TESTS + SUCCESS_COUNT))
    fi
}

run_all_tests() {
    for TEST_FILE in "$TEST_DIR"/*.java; do
        TEST_CLASS=$(basename "$TEST_FILE" .java)
        run_test "$TEST_CLASS"
    done
}

# Check if a specific test is specified as a command-line argument
if [ -n "$1" ]; then
    # A test name is provided, construct the expected filename
    LOCAL_TEST_FILE="$TEST_DIR/$1Test.java"
    # Check if the test file exists
    if [ -f "$LOCAL_TEST_FILE" ]; then
        # Extract the class name (without .java)
        TEST_CLASS=$(basename "$LOCAL_TEST_FILE" .java)
        echo "Running single test: $TEST_CLASS"
        run_test "$TEST_CLASS"
    else
        echo "Test file '$LOCAL_TEST_FILE' not found."
        exit 1
    fi
else
    # No test specified, run all tests
    echo "Running all tests..."
    run_all_tests
fi

echo ""
echo "--- Test Summary ---"
echo "✅  Successful: $SUCCESSFUL_TESTS"
echo "❌  Failed: $FAILED_TESTS"
echo "----------------------"

echo "Testing completed."
