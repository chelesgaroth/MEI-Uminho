#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "sequentialBucketSort.h"


int cmpfunc (const void * a, const void * b) {
  return ( *(int*)a - *(int*)b );
}


void buckets_to_arr(int size, struct bucket* arr[size], int* res) {
  int w = 0;
  int bucket, elem;
  for(bucket = 0; bucket < size; bucket++) {
    for(elem = 0; elem < arr[bucket]->n_elem; elem++) {
      res[w++] = arr[bucket]->array[elem];
    }
  }
}


void insert(struct bucket* arr, int elem) {
  if(arr->n_elem >= arr->max_elem){
    printf("aqui");
    arr->array = realloc(arr->array, sizeof(int) * arr->max_elem * 2);
    arr->max_elem *= 2;
  }
  arr->array[arr->n_elem] = elem;
  arr->n_elem++;
}

void maxMin(int* arr, int dim, int* max, int* min) {
  int i;
  for(i = 1; i < dim; i++) {
    if(arr[i] > *max) *max = arr[i];
    if(arr[i] < *min) *min = arr[i];
  }
}

struct bucket* aloca_bucket(int max) {
  struct bucket* block_arr = malloc(sizeof(struct bucket));

  block_arr->array = malloc(sizeof(int) * max);
  block_arr->n_elem = 0;
  block_arr->max_elem = max;

  return block_arr;
}

void free_bucket(struct bucket* b) {
  free(b->array);
  free(b);
}

void bucket_sort(int dim, int n_buckets,int * initial_array) {
  int i;
  int max = initial_array[0];
  int min = initial_array[0];
  
  maxMin(initial_array, dim, &max, &min);

  

  struct bucket* buckets[n_buckets];

  for (i = 0; i < n_buckets; i++) {
    int tam = (dim/n_buckets)*2;
    buckets[i] = aloca_bucket(tam);
  }

  for (i = 0; i < dim; i++) {
    int n_bucket = (initial_array[i] + abs(min)) * n_buckets / (abs(max + abs(min)));
    if(n_bucket > n_buckets - 1) n_bucket = n_buckets - 1;

    insert(buckets[n_bucket], initial_array[i]);
  }

  for (i = 0; i < n_buckets; i++)
    if (buckets[i]->n_elem > 1)
      qsort(buckets[i]->array, buckets[i]->n_elem, sizeof(int), cmpfunc);

  buckets_to_arr(n_buckets, buckets, initial_array);

  for (i = 0; i < n_buckets; i++)
    free_bucket(buckets[i]);
  
}

