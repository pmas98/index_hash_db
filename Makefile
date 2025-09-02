# Makefile for Hash Index Project

# Directories
SRC_DIR = src/main/java
BUILD_DIR = build
CLASSES_DIR = $(BUILD_DIR)/classes

# Java compiler
JAVAC = javac
JAVA = java

# Source files
CORE_SOURCES = $(wildcard $(SRC_DIR)/hashindex/core/*.java)
FUNCTION_SOURCES = $(wildcard $(SRC_DIR)/hashindex/functions/*.java)
UTIL_SOURCES = $(wildcard $(SRC_DIR)/hashindex/utils/*.java)
DEMO_SOURCES = $(wildcard $(SRC_DIR)/hashindex/demo/*.java)

# All source files
SOURCES = $(CORE_SOURCES) $(FUNCTION_SOURCES) $(UTIL_SOURCES) $(DEMO_SOURCES)

# Main classes
MAIN_CLASS = hashindex.demo.HashIndexDemo
FILE_DEMO_CLASS = hashindex.demo.HashIndexDemoWithFile

# Default target
all: compile

# Compile all Java files
compile: $(CLASSES_DIR)
	$(JAVAC) -d $(CLASSES_DIR) -cp $(SRC_DIR) $(SOURCES)

# Create build directory
$(CLASSES_DIR):
	mkdir -p $(CLASSES_DIR)

# Run basic demo
demo: compile
	$(JAVA) -cp $(CLASSES_DIR) $(MAIN_CLASS)

# Run file-based demo
demo-file: compile
	$(JAVA) -cp $(CLASSES_DIR) $(FILE_DEMO_CLASS)

# Clean build files
clean:
	rm -rf $(BUILD_DIR)

# Clean all generated files
distclean: clean
	rm -f *.class

# Show project structure
tree:
	@echo "Project Structure:"
	@find $(SRC_DIR) -name "*.java" | sort

# Help
help:
	@echo "Available targets:"
	@echo "  all        - Compile all Java files"
	@echo "  compile    - Compile all Java files"
	@echo "  demo       - Run basic demo"
	@echo "  demo-file  - Run file-based demo"
	@echo "  clean      - Remove build files"
	@echo "  distclean  - Remove all generated files"
	@echo "  tree       - Show project structure"
	@echo "  help       - Show this help"

.PHONY: all compile demo demo-file clean distclean tree help
