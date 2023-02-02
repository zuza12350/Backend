package pl.edu.pjwstk.project.config;

import io.ipfs.api.IPFS;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * The IPFSConfig class is responsible for connecting the API to IPFS.
 * It contains @Configuration annotations, which tells Spring container that there is one or more beans that needs to be dealt with on runtime
 * and @Scope singleton annotation, which creates a single instance of that bean. This means that all requests for that bean name will return the same object, which is cached.
 *
 * @author Zuzanna Borkowwka
 */
@Configuration
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
@Getter @Setter
public class IPFSConfig {
    IPFS ipfs;
    @SneakyThrows
    public IPFSConfig() {
        ipfs = new IPFS("127.0.0.1", 4001);
    }

}
