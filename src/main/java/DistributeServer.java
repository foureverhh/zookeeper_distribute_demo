import lombok.extern.slf4j.Slf4j;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.junit.experimental.theories.Theories;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;
@Slf4j
public class DistributeServer {

    private String ip = "192.168.10.120:2181,192.168.10.130:2181,192.168.10.140:2181";
    private int timeout = 2000;
    private ZooKeeper zkClient;

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        DistributeServer server = new DistributeServer();
        //1.set up connection between servers and zookeeper
        server.getConnectionToZookeeper();
        //2.将server信息写入zookeeper集群
        System.out.println("args[0] is " + Arrays.toString(args));
        server.register(args[0]);
        //3.模拟处理业务逻辑
        server.business();

    }

    private void business() throws InterruptedException {
        Thread.sleep(Long.MAX_VALUE);
    }

    private void register(String hostname) throws InterruptedException, KeeperException {
        zkClient.create("/servers/server",hostname.getBytes(), ZooDefs.Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL_SEQUENTIAL);
    }

    private void getConnectionToZookeeper() throws IOException {
        zkClient = new ZooKeeper(ip,timeout, watcher->{
            log.info("Server side connection to zookeeper set up");
        });
    }
}
