package pl.edu.pjwstk.project.config;

import io.ipfs.api.IPFS;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IPFSConfig {
    IPFS ipfs;
    @SneakyThrows
    public IPFSConfig() {
//        ipfs = new IPFS("/dnsaddr/ipfs.infura.io/tcp/5001/https");
//        System.out.println("connected");
//        //System.out.println("id: " + ipfs.id());
        ipfs = new IPFS("localhost", 5001);
    }


    public IPFS getIpfs() {
        return ipfs;
    }
    public void setIpfs(IPFS ipfs) {
        this.ipfs = ipfs;
    }
}
