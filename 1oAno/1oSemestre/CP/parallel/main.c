#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <time.h>

#include "papi.h"

#include "parallelBucketSort.h"

// PAPI events to monitor
#define NUM_EVENTS 4
int Events[NUM_EVENTS] = { PAPI_TOT_CYC, PAPI_TOT_INS, PAPI_L1_DCM, PAPI_L2_DCM };

// PAPI counters' values
long long values[NUM_EVENTS], min_values[NUM_EVENTS];
int retval, EventSet=PAPI_NULL;

// number of times the function is executed and measured
#define NUM_RUNS 5


int main (int argc, char *argv[]) {
 
   int run,num_hwcntrs = 0;
   long long start_usec, end_usec, elapsed_usec, min_usec=0L;
   int * initial_array;
   int dim,n_buckets;
   int limit, i, num_threads;


   // Initialize PAPI
   retval = PAPI_library_init(PAPI_VER_CURRENT);
   if (retval != PAPI_VER_CURRENT) {
      fprintf(stderr,"PAPI library init error!\n");
      return 0;
   }


   // Create an Event set
   if (PAPI_create_eventset(&EventSet) != PAPI_OK) {
      fprintf(stderr,"PAPI create event set error\n");
      return 0;
   }

   /* Get the number of hardware counters available */
   if ((num_hwcntrs = PAPI_num_hwctrs()) <= PAPI_OK)  {
      fprintf (stderr, "PAPI error getting number of available hardware counters!\n");
      return 0;
   }
      
   fprintf(stdout, "done!\nThis system has %d available counters.\n\n", num_hwcntrs);


   // We will be using at most NUM_EVENTS counters
   if (num_hwcntrs >= NUM_EVENTS) {
      num_hwcntrs = NUM_EVENTS;
   } 
   else {
      fprintf (stderr, "Error: there aren't enough counters to monitor %d events!\n", NUM_EVENTS);
      return 0;
   }

   if (PAPI_add_events(EventSet,Events,NUM_EVENTS) != PAPI_OK)  {
      fprintf(stderr,"PAPI library add events error!\n");
      return 0;
   } 


   dim = atoi(argv[1]);
   n_buckets = atoi(argv[2]);
   num_threads = atoi(argv[3]);

   //highest possible number in array
   limit = 100;
   
   initial_array = (int *) malloc(sizeof(int)*dim);
   


   // Generating random numbers within a range to put in initial array
   srand ( time(NULL) );
   for(i=0;i<dim;i++) {
      initial_array[i] = rand() % limit;
   }

   // If dimension is small, then array created is displayed

   if (dim <= 100) {
      printf("Unsorted data \n");
      for(i=0;i<dim;i++) {
         printf("%d ",initial_array[i]);
      }
      printf("\n");
   }


   // warmup caches
   fprintf (stdout, "Warming up caches...");
   bucket_sort (dim,n_buckets,initial_array, num_threads);
   fprintf (stdout, "done!\n");
   

   for (run=0 ; run < NUM_RUNS ; run++) {
      //fprintf (stdout, "run=%d - Computation", run);
      start_usec = PAPI_get_real_usec();

      /* Start counting events */
      if (PAPI_start(EventSet) != PAPI_OK) {
         fprintf (stderr, "PAPI error starting counters!\n");
         return 0;
      }

      bucket_sort (dim,n_buckets,initial_array, num_threads);


      /* Stop counting events */
      if (PAPI_stop(EventSet,values) != PAPI_OK) {
         fprintf (stderr, "PAPI error stoping counters!\n");
         return 0;
      }

      end_usec = PAPI_get_real_usec();
      fprintf (stdout, "done!\n");

      elapsed_usec = end_usec - start_usec;


      if ((run==0) || (elapsed_usec < min_usec)) {
         min_usec = elapsed_usec;
         for (i=0 ; i< NUM_EVENTS ; i++) min_values[i] = values [i];
      }

      if (dim <= 100) {
         printf("%d: Sorted data \n", run);

         for(i=0;i<dim;i++) {
               printf("%d ",initial_array[i]);
         }
         printf("\n");
      }

   } // end runs

   free(initial_array);

   fprintf (stdout,"\nWall clock time: %lld usecs\n", min_usec);   
   
   // output PAPI counters' values
   for (i=0 ; i< NUM_EVENTS ; i++) {
      char EventCodeStr[PAPI_MAX_STR_LEN];
   
      if (PAPI_event_code_to_name(Events[i], EventCodeStr) == PAPI_OK) {
         fprintf (stdout, "%s = %lld\n", EventCodeStr, min_values[i]);
      } else {
         fprintf (stdout, "PAPI UNKNOWN EVENT = %lld\n", min_values[i]);
      }
   }
   #if NUM_EVENTS >1
   //evaluate CPI and Texec here
   if ((Events[0] == PAPI_TOT_CYC) && (Events[1] == PAPI_TOT_INS)) {
   	float CPI = ((float) min_values[0]) / ((float) min_values[1]);
        fprintf (stdout, "CPI = %.2f\n", CPI);
   }
   #endif

   fprintf (stdout,"\nThat's all, folks\n");
   return 0;
}
