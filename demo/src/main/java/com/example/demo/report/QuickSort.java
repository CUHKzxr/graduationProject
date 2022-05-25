package com.example.demo.report;

public class QuickSort implements FindMedian{

    @Override
    public int getMedian(int[] list, int leftBorder, int rightBorder) {
        sort(list,leftBorder,rightBorder);
        int size=rightBorder-leftBorder+1;
        if(size%2==0){
            int r1=list[leftBorder+size/2];
            int r2=list[leftBorder-1+size/2];
            if(r1==FindMedian.INT_MAX||r2==FindMedian.INT_MAX){
                return FindMedian.INT_MAX;
            }else {
                return (r1+r2)/2;
            }
        }else {
            return list[(leftBorder+rightBorder)/2];
        }
    }

    private static int partition(int[] arr, int leftBorder, int rightBorder){

        // 随机在arr[leftBorder...rightBorder]的范围中, 选择一个数值作为标定点pivot
        swap( arr, leftBorder , (int)(Math.random()*(rightBorder-leftBorder+1))+leftBorder );

        int v = arr[leftBorder];

        int j = leftBorder; // arr[leftBorder+1...j] < v ; arr[j+1...i) > v
        for( int i = leftBorder + 1 ; i <= rightBorder ; i ++ ){
            if( arr[i] < v ){
                j ++;
                swap(arr, j, i);
            }
        }
        swap(arr, leftBorder, j);

        return j;
    }

    private static void sort(int[] arr, int leftBorder, int rightBorder){

        // 对于小规模数组, 使用插入排序
        if( rightBorder - leftBorder <= 15 ){
            InsertSort(arr, leftBorder, rightBorder);
            return;
        }
        int p = partition(arr, leftBorder, rightBorder);
        sort(arr, leftBorder, p-1 );
        sort(arr, p+1, rightBorder);
    }
    private static void InsertSort(int list[], int leftBorder, int rightBorder)
    {
        for(int i = leftBorder + 1; i <= rightBorder; i++)
        {
            if(list[i - 1] > list[i])
            {
                int t = list[i];
                int j = i;
                while(j > leftBorder && list[j - 1] > t)
                {
                    list[j] = list[j - 1];
                    j--;
                }
                list[j] = t;
            }
        }
    }


    private static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }


}
