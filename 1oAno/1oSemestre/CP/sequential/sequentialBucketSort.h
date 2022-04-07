#ifndef __GEMM__
#define __GEMM__

struct bucket {
        int n_elem;
        int * array;
        int max_elem;
};


int cmpfunc (const void * a, const void * b);

void bucket_sort(int dim, int n_buckets,int * initial_array);


#endif
