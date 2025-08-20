#!/bin/bash
set -e

echo "🔨 Cleaning..."
./gradlew clean

echo "📦 Building & Publishing slang-core..."
./gradlew :slang-core:publishToMavenLocal

echo "📦 Building & Publishing slang-spring-boot-starter..."
./gradlew :slang-spring-boot-starter:publishToMavenLocal

echo "✅ Done! Artifacts published to ~/.m2/repository/org/dolgo/"