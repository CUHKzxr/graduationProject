package com.example.demo.report;

public class BFPRT implements FindMedian{


    @Override
    public int getMedian(int[] list, int leftBorder, int rightBorder) {
        int size=rightBorder-leftBorder+1;
        if (size % 2 != 0) {
            return  list[BFPRT.BFPRT(list, 0, size - 1, 1 + size / 2)];
        } else {
            int a=list[BFPRT.BFPRT(list, 0, size - 1, size / 2)];
            if(a==INT_MAX){
                return INT_MAX;
            }
            int b=list[BFPRT.BFPRT(list, 0, size - 1, 1 + size / 2)];
            if(b==INT_MAX){
                return INT_MAX;
            }
            return  (a+b) / 2;
        }
    }
    //返回偏移量
    public static int BFPRT(int[] list,int leftBorder,int rightBorder, int k){
        int p = FindMid(list, leftBorder, rightBorder);    //寻找中位数的中位数
        int i = Partion(list, leftBorder, rightBorder, p);

        int m = i - leftBorder + 1;
        //if(m == k) return list[i];
        if(m == k) return i;
        if(m > k)  return BFPRT(list, leftBorder, i - 1, k);
        return BFPRT(list, i + 1, rightBorder, k - m);
    }
    //返回偏移量
    private static int Partion(int list[], int leftBorder, int rightBorder, int pivotPosition)
    {
        swap(list,pivotPosition, leftBorder);
        int i = leftBorder;
        int j = rightBorder;
        int pivot = list[leftBorder];
        while(i < j)
        {
            while(list[j] >= pivot && i < j)
                j--;
            list[i] = list[j];
            while(list[i] <= pivot && i < j)
                i++;
            list[j] = list[i];
        }
        list[i] = pivot;
        return i;
    }
    //返回偏移量
    private static int FindMid(int list[], int leftBorder, int rightBorder)
    {
        if(leftBorder == rightBorder) return leftBorder;
        //--------------------
        if(rightBorder-leftBorder<=25){
            InsertSort(list,leftBorder,rightBorder);
            return (leftBorder+rightBorder)/2;
        }
        //-------------------------
        int i = 0;
        int n = 0;
        for(i = leftBorder; i < rightBorder - 5; i += 5)
        {
            InsertSort(list, i, i + 4);
            n = i - leftBorder;
            swap(list,leftBorder + n / 5, i + 2);
        }

        int num = rightBorder - i + 1;
        if(num > 0)
        {
            InsertSort(list, i, i + num - 1);
            n = i - leftBorder;
            swap(list,leftBorder + n / 5,i + num / 2);
        }
        n /= 5;
        if(n == leftBorder) return leftBorder;
        return BFPRT(list,leftBorder,leftBorder+n,n/2);
        //return FindMid(list, leftBorder, leftBorder + n);
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

    private static void swap(int[] list,int p1,int p2){
        int temp=list[p1];
        list[p1]=list[p2];
        list[p2]=temp;
    }


}
