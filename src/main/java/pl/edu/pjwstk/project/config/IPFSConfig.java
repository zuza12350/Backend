package pl.edu.pjwstk.project.config;

import io.ipfs.api.IPFS;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class IPFSConfig {
    IPFS ipfs;
    public IPFSConfig() {
        ipfs = new IPFS("localhost", 5001);
    }

    public IPFS getIpfs() {
        return ipfs;
    }
    public void setIpfs(IPFS ipfs) {
        this.ipfs = ipfs;
    }
}
