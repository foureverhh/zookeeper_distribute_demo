import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
public class DistributeClient {

    private String ip = "192.168.10.120:2181,192.168.10.130:2181,192.168.10.140:2181";
    private int timeout = 2000;
    private ZooKeeper zkClient;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        DistributeClient client = new DistributeClient();
        //1.获取zookeeper连接
        client.getConnectionToZookeeper();
        //2.注册监听
        client.getChildren();
        //3.业务逻辑
        client.business();
    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void getChildren() throws InterruptedException, KeeperException {
        List<String> nodes = zkClient.getChildren("/servers",true);
        List<String> currentNodes = new ArrayList<>();
        for (String node: nodes) {
            byte[] data = zkClient.getData("/servers/"+ node,false,null);
            log.info("node is " + node + ", and hostname is "+ new String(data));
            currentNodes.add(new String(data));
        }
        log.info(currentNodes.toString());
    }

    private void getConnectionToZookeeper() throws IOException {
        zkClient = new ZooKeeper(ip,timeout,watchedEvent -> {
            log.info("Client side connection to zookeeper setup");
            try {
                //每一次改变都重新注册
                getChildren();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
