#include <stdio.h>
#include <time.h>
#include <stdlib.h>

int main()
{
        srand(time(NULL));
        int r = rand();
        printf("%d\n", r);

        return 0;
}