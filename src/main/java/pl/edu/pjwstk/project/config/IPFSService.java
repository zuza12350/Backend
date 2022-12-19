package pl.edu.pjwstk.project.config;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import io.ipfs.multihash.Multihash;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.edu.pjwstk.project.filelogic.FileHolderRepository;


import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Service
@AllArgsConstructor
public class IPFSService implements FileServiceImpl {
    private final IPFSConfig ipfsConfig;
    private final FileHolderRepository fileHolderRepository;

    @Override
    public String saveFile(String filePath) {
        try {
            IPFS ipfs = ipfsConfig.ipfs;
            File _file = new File(filePath);
            NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(_file);
            MerkleNode response = ipfs.add(file).get(0);
            System.out.println("Hash (base 58): " + response.hash.toBase58());
            return response.hash.toBase58();
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }

    @Override
    public String saveFile(String fileName,MultipartFile file) {
        try {
            if(!fileHolderRepository.existsByName(fileName)){
                InputStream inputStream = new ByteArrayInputStream(file.getBytes());
                IPFS ipfs = ipfsConfig.ipfs;

                NamedStreamable.InputStreamWrapper is = new NamedStreamable.InputStreamWrapper(inputStream);
                MerkleNode response = ipfs.add(is).get(0);
                fileHolderRepository.saveFile(fileName,response.hash.toBase58());
                return response.hash.toBase58();
            }else{
                this.overrideFile(fileName, JsonParser
                        .parseString(new String(file.getBytes(), StandardCharsets.UTF_8))
                        .getAsJsonObject());
                return fileHolderRepository.getFileHash(fileName);
            }

        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }

    @Override
    public byte[] loadFile(String filename) {
        try {
            IPFS ipfs = ipfsConfig.ipfs;
            Multihash filePointer = Multihash.fromBase58(fileHolderRepository.getFileHash(filename));
            return ipfs.cat(filePointer);
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }
    @Override
    public void changePointerToNewFile(MultipartFile file, String fileHash) {
        try {
            InputStream inputStream = new ByteArrayInputStream(file.getBytes());
            IPFS ipfs = ipfsConfig.ipfs;

            NamedStreamable.InputStreamWrapper is = new NamedStreamable.InputStreamWrapper(inputStream);
            MerkleNode response = ipfs.add(is).get(0);
            fileHolderRepository.changeFilePointerToNewHash(file.getName(),response.hash.toBase58());
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }

    @Override
    @SneakyThrows
    public void overrideFile(String fileName, JsonObject data) {
        try {
            String workingDirectory = System.getProperty("user.dir");
            String absoluteFilePath = "";
            absoluteFilePath = workingDirectory + File.separator + fileName +".json";
//            System.out.println("Final filepath : " + absoluteFilePath);
            File file = new File(absoluteFilePath);

            if (file.createNewFile()) {
                FileWriter fileWriter = new FileWriter(file.getName(),true);
                BufferedWriter out = new BufferedWriter(fileWriter);
                out.write(data.toString());
                out.close();
            } else {
                FileChannel.open(Paths.get(absoluteFilePath), StandardOpenOption.WRITE).truncate(0).close();
                FileWriter fileWriter = new FileWriter(file.getName(),true);
                BufferedWriter out = new BufferedWriter(fileWriter);
                out.write(data.toString());
                out.close();
            }
            MultipartFile multipartFile = new MockMultipartFile(fileName, new FileInputStream(file));
            this.changePointerToNewFile(multipartFile,fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
