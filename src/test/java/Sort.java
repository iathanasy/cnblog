import java.util.Arrays;

/**
 * @description:
 * @author: Mr.Wang
 * @create: 2020-06-10 14:09
 **/
public class Sort {
    public static void main(String[] args) {
        int[] arr = { 6, 1, 8, 2, 9, 3 };
        int temp;
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) { // 如果前一个数比后一个小，则交换
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        System.out.println(Arrays.toString(arr));
    }
}
