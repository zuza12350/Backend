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

/**
 * The IPFSService class overrides the methods defined in FileIPFSImpl while adding all the logic that tells you how to manipulate the data.
 * It includes the @Service annotation, which assigns classes in the service layer in Spring.
 *
 * @author Zuzanna Borkowska
 */
@Service
@AllArgsConstructor
public class IPFSService implements FileIPFSImpl {
    private final IPFSConfig ipfsConfig;
    private final FileHolderRepository fileHolderRepository;

    /**
     * The method responsible for writing the file to both the database and IPFS.
     * In case there is an annotation to a file in the database, its hash is changed.
     * @param fileName which indicate a database row
     * @param file  uploaded file
     * @return hash of the newly added file
     */
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

    /**
     * Method responsible for reading data from a file in a byte array substrate.
     * @param filename which indicate a database row
     * @return file content in bytes
     */
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

    /**
     *The method handles the case when I annotate a file with a given name in the database.
     * In this case, it adds the changed file to IPFS and changes the hash assigned to the given file name in the database.
     * @param file uploaded file
     * @param fileName filename which indicate a database row
     */
    @Override
    public void changePointerToNewFile(MultipartFile file, String fileName) {
        try {
            InputStream inputStream = new ByteArrayInputStream(file.getBytes());
            IPFS ipfs = ipfsConfig.ipfs;

            NamedStreamable.InputStreamWrapper is = new NamedStreamable.InputStreamWrapper(inputStream);
            MerkleNode response = ipfs.add(is).get(0);
            fileHolderRepository.changeFilePointerToNewHash(fileName,response.hash.toBase58());
        } catch (IOException ex) {
            throw new RuntimeException("Error whilst communicating with the IPFS node", ex);
        }
    }

    /**
     *
     * The method overwrite the data from the file and then call the method to change the pointer to the new hash in the database.
     * @param filename filename which indicate a database row
     * @param data new data that must be saved to a file in the form of json
     */
    @Override
    @SneakyThrows
    public void overrideFile(String filename, JsonObject data) {
        String fileName = filename+".json";
        try {
            String workingDirectory = System.getProperty("user.dir");
            String absoluteFilePath = "";
            absoluteFilePath = workingDirectory + File.separator + fileName;
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
            this.changePointerToNewFile(multipartFile,filename);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
