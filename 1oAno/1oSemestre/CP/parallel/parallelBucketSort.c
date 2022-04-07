#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include "omp.h"

#include "parallelBucketSort.h"


int cmpfunc (const void * a, const void * b)
{
  return ( *(int*)a - *(int*)b );
}


void buckets_to_arr(int size, struct bucket* arr, int* res) {
    memcpy(res, arr->array, arr->n_elem * sizeof(int));
}


void insert(struct bucket* arr, int elem){
    if(arr->n_elem >= arr->max_elem) {
        arr->array = realloc(arr->array, sizeof(int) * arr->max_elem * 2);
        arr->max_elem *= 2;
    }
    arr->array[arr->n_elem] = elem;
    arr->n_elem++;
}


struct bucket* aloca_bucket(int max){
    struct bucket* block_arr = malloc(sizeof(struct bucket));
    block_arr->array = malloc(sizeof(int) * max);
    block_arr->n_elem = 0;
    block_arr->max_elem = max;

    return block_arr;
}

void free_bucket(struct bucket* b){
    free(b->array);
    free(b);
}

void bucket_sort(int dim,int n_buckets,int * initial_array, int num_threads){

    int max = initial_array[0];
    int min = initial_array[0];
    int i;
    struct bucket** buckets;
    buckets = malloc(sizeof(struct Bucket*) * n_buckets);

    for(i = 1; i < dim; i++) { 
        if(initial_array[i] > max) max = initial_array[i];
        if(initial_array[i] < min) min = initial_array[i];
    }

    #pragma omp parallel num_threads(num_threads)
    {
        int i,j;

        #pragma omp for schedule(dynamic)
        for (i = 0; i < n_buckets; i++) {
            int tam = (dim/2);
            buckets[i] = aloca_bucket(tam);
        }
            

        #pragma omp barrier

        #pragma omp for schedule(dynamic)
        for (i = 0; i < n_buckets; i++) {
            for (j = 0; j < dim; j++){
                int n_bucket = (initial_array[j] + abs(min)) * n_buckets / (abs(max + abs(min)));

                if(n_bucket > n_buckets - 1) n_bucket = n_buckets - 1;

                if(n_bucket == i) {
                    insert(buckets[i],initial_array[j]);
                    //buckets[i]->array[buckets[i]->n_elem++] = initial_array[j];
                }
            }
            qsort(buckets[i]->array, buckets[i]->n_elem, sizeof(int), cmpfunc);
        }
    }
    int j;
    i = 0;
    for(j = 0; j < n_buckets; j++) {
        memcpy(initial_array + i, buckets[j]->array, buckets[j]->n_elem * sizeof(int));
        i += buckets[j]->n_elem;
	}
    
    #pragma omp for
    for ( i = 0; i < n_buckets; i++) {
        free_bucket(buckets[i]);
    }
    free(buckets);
}
