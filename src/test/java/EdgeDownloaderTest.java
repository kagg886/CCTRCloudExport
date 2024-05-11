import com.kagg886.utils.MsEdgeUtils;
import com.kagg886.utils.ZipFileUtil;
import org.junit.jupiter.api.Test;

public class EdgeDownloaderTest {
    @Test
    void testPlatform() {
        System.out.println(MsEdgeUtils.getPlatform());
    }

    @Test
    void testVersion() {
        System.out.println(MsEdgeUtils.getFullDownloadUrl());
    }

    @Test
    void testDownload() {
        MsEdgeUtils.download(MsEdgeUtils.getFullDownloadUrl(), System.out::println);
    }
    @Test
    void testUnzip() {
        ZipFileUtil.unzip("msedgedriver.zip");
    }
}
