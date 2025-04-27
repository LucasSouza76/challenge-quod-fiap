#!/bin/bash

echo "====================================================="
echo "   Biometric Verification System - Setup Helper      "
echo "====================================================="

# Check if MongoDB is running
if command -v mongod &>/dev/null; then
    echo "✓ MongoDB is installed"
    
    # Check if MongoDB is running
    if pgrep -x mongod >/dev/null; then
        echo "✓ MongoDB is running"
    else
        echo "✗ MongoDB is not running."
        echo "  Starting MongoDB..."
        brew services start mongodb/brew/mongodb-community
        
        # Wait for MongoDB to start
        sleep 5
        if pgrep -x mongod >/dev/null; then
            echo "✓ MongoDB started successfully"
        else
            echo "✗ Failed to start MongoDB. Please start it manually with:"
            echo "  brew services start mongodb/brew/mongodb-community"
        fi
    fi
else
    echo "✗ MongoDB is not installed."
    echo "  Please install MongoDB with:"
    echo "  brew tap mongodb/brew && brew install mongodb-community"
    echo "  Then run: brew services start mongodb/brew/mongodb-community"
fi

# Build the project
echo -e "\nBuilding the project..."
./mvnw clean install -DskipTests

# Check if build was successful
if [ $? -eq 0 ]; then
    echo -e "\n✓ Build successful!"
    
    # Run the application
    echo -e "\nRunning the application..."
    echo "Press Ctrl+C to stop the application when done"
    echo ""
    ./mvnw spring-boot:run
else
    echo -e "\n✗ Build failed. Please check the error messages above."
fi 