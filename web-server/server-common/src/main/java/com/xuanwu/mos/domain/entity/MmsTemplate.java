package com.xuanwu.mos.domain.entity;

        import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

/**
 * @Description
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Data 2012-11-8
 * @Version 1.0.0
 */
@JacksonXmlRootElement(localName="MMSTemplate")
public class MmsTemplate{

    @JacksonXmlProperty(localName="SMIL")
    public String smil;

    @JacksonXmlProperty(localName="Subject")
    public String subject;

    @JacksonXmlProperty(localName="Title")
    public String title;

    @JacksonXmlElementWrapper(localName="Files")
    @JacksonXmlProperty(localName = "MMSAttachFile")
    public List<MmsAttachFile> mmsAttachFile;

}
