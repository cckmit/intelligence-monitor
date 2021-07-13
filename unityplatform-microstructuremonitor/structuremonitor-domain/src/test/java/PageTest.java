import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.ArrayList;

/**
 * @author 滕楠
 * @className PageTest
 * @create 2021/7/13 15:59
 **/
public class PageTest {
    public static void main(String[] args) {
        //PageHelper.startPage(1,5);
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            arrayList.add(i);
        }
        PageInfo<Integer> pageInfo = new PageInfo<>(arrayList);

        pageInfo.setTotal(arrayList.size());
        pageInfo.setPageSize(5);
        pageInfo.setPageNum(2);
        pageInfo.setSize(5);
        System.out.println(pageInfo);
    }
}