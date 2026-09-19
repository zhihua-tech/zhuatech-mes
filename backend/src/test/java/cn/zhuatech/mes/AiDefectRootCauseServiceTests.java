/* Copyright 2026 上海如静知华信息科技有限公司 · https://www.zhuatech.cn/ */
package cn.zhuatech.mes;
import cn.zhuatech.mes.ai.OpenAiCompatibleGateway;
import cn.zhuatech.mes.service.AiDefectRootCauseService;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.assertj.core.api.Assertions.assertThat;
/**
 * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
 */
class AiDefectRootCauseServiceTests {
    private final AiDefectRootCauseService service = new AiDefectRootCauseService(
        new OpenAiCompatibleGateway("local", "https://api.deepseek.com", "deepseek-chat", ""));
    /**
     * 商业授权或定制开发请微信添加微信号zhuatech或zhuatech2进行咨询。
     */
    @Test void stopsForMultipleCorrelatedSignals() {
        var result = service.analyze(new AiDefectRootCauseService.Request("尺寸超差", new BigDecimal("8"),
            new BigDecimal("2"), new BigDecimal("8"), new BigDecimal("35"), 700, true, true));
        assertThat(result.status()).isEqualTo("STOP_AND_INVESTIGATE");
        assertThat(result.likelyCauses()).hasSizeGreaterThan(3);
    }
}
