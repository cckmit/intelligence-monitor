import com.zhikuntech.intellimonitor.core.stream.DataConsumerUtils;
import com.zhikuntech.intellimonitor.core.stream.DataProduceUtils;
import com.zhikuntech.intellimonitor.core.stream.dto.MonitorStructDTO;

import java.util.List;
import java.util.UUID;

/**
 * @author 滕楠
 * @className test1
 * @create 2021/7/8 16:40
 **/
public class test2 {
    public static void main(String[] args) {
        while (true){
            List<MonitorStructDTO> monitorStructDTOS = DataConsumerUtils.fetchData();
            System.out.println("######################3"+monitorStructDTOS.toString());
        }
    }
}