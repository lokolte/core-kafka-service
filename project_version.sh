#!/usr/bin/env bash
# This script retrieves the project version from sbt

# Extract version from sbt 'inspect actual version'
PROJECT_VERSION=$(sbt -no-color 'inspect actual version' | grep "Setting: java.lang.String" | cut -d '=' -f2 | tr -d ' ');

# Strip color output
output=$(echo $PROJECT_VERSION | sed 's/\x1b\[[0-9;]*m//g');

# Print to console
echo $output;
