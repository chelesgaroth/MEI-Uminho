#define MAXVERTICES 10
typedef int Graph[MAXVERTICES][MAXVERTICES];

/*@ requires 0 < n <= MAXVERTICES  &&
  @ \valid(A+(0..(n*n-1))) &&
  @ \valid(R+(0..(n*n-1))) &&
  @ \separated(A+(0..n*n-1), R+(0..n*n-1)) ;
  @ assigns R[0..n-1][0..n-1];
  @ ensures \forall integer i, j;
  @             0 <= i < n && 0 <= j < n ==> A[i][j] == \at(A[i][j],Old);
*/

void WarshallTC (Graph A, Graph R, int n) {
    int i, j, k;

    /*@ loop invariant 0 <= i < n;              
      @ loop assigns i,j,R[0..n-1][0..n-1];
      @ loop invariant \forall integer x, y; 0 <= x < i && 0 <= y < n ==> R[x][y] == A[x][y];
      @ loop variant n-i;
      @*/
    for (i=0 ; i<n ; i++)
        /*@ 
          @ loop invariant 0 <= j < n;
          @ loop assigns j,R[0..n-1][0..n-1];
          @ loop invariant \forall integer k; 0 <= k < j ==> (R[i][k]==A[i][k]);
          @ loop invariant n-j;
        */
        for (j=0 ; j<n ; j++)
            R[i][j] = A[i][j];

    /*@ assert \forall integer i, j; 0 <= i < n && 0 <= j < n ==> R[i][j] == A[i][j];
      @*/
    
    /*@ loop invariant 0 <= i < n;
      @ loop assigns k,i,j,R[0..n-1][0..n-1];
      @ loop invariant \forall integer x, y, t; 0 <= t < k && 0 <= x < n && 0 <= y < n ==> R[x][t] && R[t][y] ==> R[x][y] == 1;
      @ loop variant n-k;
      @*/
    for (k=0 ; k<n; k++)
        /*@ loop invariant 0 <= i < n && 
          @             \forall integer x, y, t; 0 <= t < k && 0 <= x < i && 0 <= y < n ==> R[x][t] && R[t][y] ==> R[x][y] == 1;
          @ loop assigns i,j,R[0..n-1][0..n-1];        
          @ loop variant n-i;
          @*/
        for (i=0 ; i<n ; i++)
            /*@ loop invariant 0 <= j < n &&
              @ \valid(R+(0..(n*n-1))) && 
              @           \forall integer x, y, t; 0 <= t < k && 0 <= x < i && 0 <= y < j ==> R[x][t] && R[t][y] ==> R[x][y] == 1;
              @ loop assigns j,R[0..n-1][0..n-1];
              @ loop variant n-j;
              @*/
            for (j=0 ; j<n ; j++)
                if (R[i][k] && R[k][j])
                    R[i][j] = 1;
}