# source files.
SRC = parallelBucketSort.c main.c

OBJ = $(SRC:.cpp=.o)

OUT = gemm

# include directories
INCLUDES = -I. 
 
# C compiler flags 
#CCFLAGS = -O0 -Wall
CCFLAGS = -funroll-loops -O2 -Wall -I/share/apps/papi/5.4.1/include 
CCFLAGS =  -ftree-vectorize -msse4 -O2 -Wall -I/share/apps/papi/5.4.1/include 
CCFLAGS =  -fopenmp -msse4 -O3 -Wall -I/share/apps/papi/5.4.1/include 
#CCFLAGS =  -O2 -Wall -I/share/apps/papi/5.4.1/include 
#CCFLAGS = -O3 -Wall -msse4.1 -march=i686

# compiler
CCC = gcc 
#CCC = /opt/intel/Compiler/11.1/073/bin/ia32/icpc 
#CCC = g++-4.5

# libraries
LIBS = -lm -lpapi -fopenmp -mavx -L/share/apps/papi/5.4.1/lib
LIBS = -lm -lpapi -L/share/apps/papi/5.4.1/lib

.SUFFIXES: .cpp .c 


default: $(OUT)

.cpp.o:
	$(CCC) $(CCFLAGS) $(INCLUDES)  -c $< -o $@

.c.o:
	$(CCC) $(CCFLAGS) $(INCLUDES) -c $< -o $@

$(OUT): $(OBJ)
	$(CCC) -o $(OUT) $(CCFLAGS) $(OBJ) $(LIBS) 

depend:  dep
#
#dep:
#	makedepend -- $(CFLAGS) -- $(INCLUDES) $(SRC)

clean:
	rm -f *.o .a *~ Makefile.bak $(OUT)
