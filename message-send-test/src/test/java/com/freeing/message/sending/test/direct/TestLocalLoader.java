package com.freeing.message.sending.test.direct;

import com.freeing.message.loader.local.LocalTemplateLoader;
import com.freeing.message.sending.core.BaseMessageTemplate;
import org.junit.Test;

public class TestLocalLoader {

    @Test
    public void test1() {
        LocalTemplateLoader localTemplateLoader =
                new LocalTemplateLoader("src/test/local-template");
        BaseMessageTemplate messageTemplate = localTemplateLoader.loadTemplate("trans_issue");
        System.out.println(messageTemplate);
    }
}
