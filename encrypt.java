import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.security.Security;
import java.util.Base64;
import java.util.Properties;

@WebServlet("/encryptCredentials")
public class EncryptServlet extends HttpServlet {
  
  private static final String ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
  private static final int ITERATION_COUNT = 1000;
  private static final byte[] SALT = {
    (byte) 0x8E, (byte) 0x12, (byte) 0x39, (byte) 0x9C,
    (byte) 0x07, (byte) 0x72, (byte) 0x6F, (byte) 0x5A
  };
  private static final String PROPERTIES_FILE = "config.properties";
  
  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    // Read the request data
    InputStream inputStream = request.getInputStream();
    byte[] requestData = inputStream.readAllBytes();
    String requestString = new String(requestData);
    
    // Parse the request JSON
    JsonObject jsonObject = JsonParser.parseString(requestString).getAsJsonObject();
    String webname = jsonObject.get("webname").getAsString();
    String password = jsonObject.get("password").getAsString();
    
    // Load the encryption key from the properties file
    Properties properties = new Properties();
    InputStream propertiesStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE);
    properties.load(propertiesStream);
    String encryptionKey = properties.getProperty("encryption.key");
    
    // Initialize the Bouncy Castle provider
    Security.addProvider(new BouncyCastleProvider());
    
    // Generate the secret key and initialization vector
    PBEKeySpec keySpec = new PBEKeySpec(encryptionKey.toCharArray(), SALT, ITERATION_COUNT);
    SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
    SecretKey secretKey = keyFactory.generateSecret(keySpec);
    PBEParameterSpec parameterSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);
    
    // Initialize the cipher for encryption
    Cipher cipher = Cipher.getInstance(ALGORITHM);
    cipher.init(Cipher.ENCRYPT_MODE, secretKey, parameterSpec);
    
    // Encrypt the credentials and encode them in Base64
    byte[] webnameBytes = webname.getBytes();
    byte[] passwordBytes = password.getBytes();
    byte[] encryptedWebnameBytes = cipher.doFinal(webnameBytes);
    byte[] encryptedPasswordBytes = cipher.doFinal(passwordBytes);
    String encryptedWebname = Base64.getEncoder().encodeToString(encryptedWebnameBytes);
    String encryptedPassword = Base64.getEncoder().encodeToString(encryptedPasswordBytes);
    
    // Create the response JSON
    JsonObject responseJson = new JsonObject();
    responseJson.addProperty("encryptedWebname", encryptedWebname);
    responseJson.addProperty("encryptedPassword", encryptedPassword);
    String responseString = responseJson.toString();
    
    // Send the response back to the client
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(responseString);
  }
}
