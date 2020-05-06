import com.hbnet.fastsh.utils.GDTApiUtils;
import org.junit.Test;

/**
 * @Author: kyle_chan
 * @CreateDate: 2020/3/26
 */
public class GDTApiTest extends ServiceTest {
    @Test
    public void apiTest(){
        GDTApiUtils.getAccessToken("d4e73b86d69c7921b456e4a131118506","https://smartad.akeyn.com/smartad-web/admin/advertisers/authcallback/wx8f7ed53cf2134f29");
    }
}
