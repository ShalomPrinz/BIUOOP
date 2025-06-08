#!/bin/bash

# Configuration
SRC_DIR="src"
TEST_DIR="test"
BUILD_DIR="test/build"
CLASSES_DIR="$BUILD_DIR/classes"
TEST_CLASSES_DIR="$BUILD_DIR/test-classes"
COVERAGE_DIR="$BUILD_DIR/coverage"
CLASSPATH="bin:biuoop-1.4.jar"
JUNIT_JAR="junit-jupiter-api-5.8.2.jar:junit-jupiter-engine-5.8.2.jar:junit-platform-console-standalone-1.8.2.jar"
JACOCO_JAR="jacoco/lib/jacocoagent.jar:jacoco/lib/jacococli.jar"

# Create build directories if they don't exist
mkdir -p $CLASSES_DIR
mkdir -p $TEST_CLASSES_DIR
mkdir -p $COVERAGE_DIR

# Check if JaCoCo is available
if [ ! -f "jacoco/lib/jacocoagent.jar" ]; then
    echo "Error: JaCoCo not found! Please download JaCoCo and extract to 'jacoco' directory."
    echo "Download from: https://www.jacoco.org/jacoco/"
    exit 1
fi

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

run_test_with_coverage() {
    local TEST_CLASS=$1
    echo "Running test with coverage: $TEST_CLASS"

    # Run test with JaCoCo agent
    OUTPUT=$(java -javaagent:jacoco/lib/jacocoagent.jar=destfile=$COVERAGE_DIR/jacoco-${TEST_CLASS}.exec \
        -cp "$CLASSES_DIR:$TEST_CLASSES_DIR:$CLASSPATH:$JUNIT_JAR" \
        org.junit.platform.console.ConsoleLauncher \
        --select-class "$TEST_CLASS" --disable-banner)

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

run_all_tests_with_coverage() {
    # Clean previous coverage data
    rm -f $COVERAGE_DIR/*.exec

    for TEST_FILE in "$TEST_DIR"/*.java; do
        TEST_CLASS=$(basename "$TEST_FILE" .java)
        run_test_with_coverage "$TEST_CLASS"
    done
}

generate_coverage_report() {
    echo ""
    echo "Generating coverage report..."

    # Merge all execution data files
    EXEC_FILES=$(find $COVERAGE_DIR -name "jacoco-*.exec" | tr '\n' ':' | sed 's/:$//')

    if [ -z "$EXEC_FILES" ]; then
        echo "No coverage data found!"
        return
    fi

    # Generate HTML report
    java -jar jacoco/lib/jacococli.jar report $COVERAGE_DIR/*.exec \
        --classfiles $CLASSES_DIR \
        --sourcefiles $SRC_DIR \
        --html $COVERAGE_DIR/html \
        --xml $COVERAGE_DIR/jacoco.xml

    if [ $? -eq 0 ]; then
        echo "📊  Coverage report generated:"
        echo "   HTML: $COVERAGE_DIR/html/index.html"
        echo "   XML:  $COVERAGE_DIR/jacoco.xml"

        # Extract and display coverage summary
        if [ -f "$COVERAGE_DIR/jacoco.xml" ]; then
            echo ""
            echo "--- Coverage Summary ---"

            # Parse XML for overall coverage (report level, not individual class level)
            # Look for the report-level counter elements
            INSTRUCTION_LINE=$(grep '<counter type="INSTRUCTION"' "$COVERAGE_DIR/jacoco.xml" | head -1)
            BRANCH_LINE=$(grep '<counter type="BRANCH"' "$COVERAGE_DIR/jacoco.xml" | head -1)

            if [ -n "$INSTRUCTION_LINE" ]; then
                INSTRUCTION_MISSED=$(echo "$INSTRUCTION_LINE" | sed -n 's/.*missed="\([0-9]*\)".*/\1/p')
                INSTRUCTION_COVERED=$(echo "$INSTRUCTION_LINE" | sed -n 's/.*covered="\([0-9]*\)".*/\1/p')

                if [ -n "$INSTRUCTION_COVERED" ] && [ -n "$INSTRUCTION_MISSED" ]; then
                    TOTAL_INSTRUCTIONS=$((INSTRUCTION_COVERED + INSTRUCTION_MISSED))
                    if [ $TOTAL_INSTRUCTIONS -gt 0 ]; then
                        COVERAGE_PERCENT=$((INSTRUCTION_COVERED * 100 / TOTAL_INSTRUCTIONS))
                        echo "📈  Instruction Coverage: $COVERAGE_PERCENT% ($INSTRUCTION_COVERED/$TOTAL_INSTRUCTIONS)"
                    fi
                fi
            fi

            if [ -n "$BRANCH_LINE" ]; then
                BRANCH_MISSED=$(echo "$BRANCH_LINE" | sed -n 's/.*missed="\([0-9]*\)".*/\1/p')
                BRANCH_COVERED=$(echo "$BRANCH_LINE" | sed -n 's/.*covered="\([0-9]*\)".*/\1/p')

                if [ -n "$BRANCH_COVERED" ] && [ -n "$BRANCH_MISSED" ]; then
                    TOTAL_BRANCHES=$((BRANCH_COVERED + BRANCH_MISSED))
                    if [ $TOTAL_BRANCHES -gt 0 ]; then
                        BRANCH_PERCENT=$((BRANCH_COVERED * 100 / TOTAL_BRANCHES))
                        echo "🌿  Branch Coverage: $BRANCH_PERCENT% ($BRANCH_COVERED/$TOTAL_BRANCHES)"
                    fi
                fi
            fi

            echo "-----------------------"
        fi
    else
        echo "Error generating coverage report!"
    fi
}

# Check if a specific test is specified as a command-line argument
if [ -n "$1" ]; then
    # A test name is provided, construct the expected filename
    LOCAL_TEST_FILE="$TEST_DIR/$1Test.java"
    # Check if the test file exists
    if [ -f "$LOCAL_TEST_FILE" ]; then
        # Extract the class name (without .java)
        TEST_CLASS=$(basename "$LOCAL_TEST_FILE" .java)
        echo "Running single test with coverage: $TEST_CLASS"
        rm -f $COVERAGE_DIR/*.exec
        run_test_with_coverage "$TEST_CLASS"
    else
        echo "Test file '$LOCAL_TEST_FILE' not found."
        exit 1
    fi
else
    # No test specified, run all tests
    echo "Running all tests with coverage..."
    run_all_tests_with_coverage
fi

echo ""
echo "--- Test Summary ---"
echo "✅  Successful: $SUCCESSFUL_TESTS"
echo "❌  Failed: $FAILED_TESTS"
echo "----------------------"

# Generate coverage report
generate_coverage_report

echo "Coverage analysis completed."