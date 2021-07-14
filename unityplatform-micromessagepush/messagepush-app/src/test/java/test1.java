import com.zhikuntech.intellimonitor.core.stream.DataProduceUtils;
import com.zhikuntech.intellimonitor.core.stream.dto.MonitorStructDTO;

import java.util.UUID;

/**
 * @author 滕楠
 * @className test1
 * @create 2021/7/8 16:40
 **/
public class test1 {
    public static void main(String[] args) {
        while (true) {
            MonitorStructDTO dto = new MonitorStructDTO();
            dto.setMonitorNo("测试类id");
            dto.setUuid(UUID.randomUUID().toString());
            DataProduceUtils.sendData(dto);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}