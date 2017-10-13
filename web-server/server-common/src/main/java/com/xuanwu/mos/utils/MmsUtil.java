package com.xuanwu.mos.utils;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.xuanwu.mos.config.Config;
import com.xuanwu.mos.domain.entity.MmsAttachFile;
import com.xuanwu.mos.domain.entity.MmsContent;
import com.xuanwu.mos.domain.entity.MmsTemplate;
import com.xuanwu.mos.domain.enums.BizDataType;
import com.xuanwu.mos.file.FileUtil;
import com.xuanwu.msggate.common.sbi.entity.MediaItem;
import com.xuanwu.msggate.common.sbi.entity.MsgContent;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author <a href="mailto:liushuaiying@139130.net">Shuaiying.Liu</a>
 * @Description 彩信工具类
 * @Data 2012-11-8
 * @Version 1.0.0
 */
public class MmsUtil {

    private static final XmlMapper xmlMapper = new XmlMapper();
    private static final String smilName = ".smil";

    // 将数据封装到smil的标签中
    public static String toSmil(MmsContent mc) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sbR = new StringBuilder();
        StringBuilder sbP = new StringBuilder();
        int i = 0;
        for (MmsContent.Frame mf : mc.getFrames()) {
            if (mf.getSize() <= 0)
                continue;
            boolean hasImg = false;
            sbP.append("<par dur=\"").append(mf.getDuration() * 1000)
                    .append("ms\">");
            if (mf.getImg().getSize() > 0) {
                hasImg = true;
                sbR.append("<region id=\"img")
                        .append(i)
                        .append("\" width=\"160\" height=\"280\" left=\"0\" top=\"0\" fit=\"hidden\" />");
                sbP.append("<img src=\"").append(mf.getImg().getName())
                        .append("\" region=\"img").append(i).append("\" />");
            }
            if (mf.getText().getSize() > 0) {
                mf.getText().setName(mf.getId() + ".txt");
                if (hasImg) {
                    sbR.append("<region id=\"text")
                            .append(i)
                            .append("\" width=\"160\" height=\"140\" left=\"0\" top=\"140\" fit=\"hidden\" />");
                } else {
                    sbR.append("<region id=\"text")
                            .append(i)
                            .append("\" width=\"160\" height=\"140\" left=\"0\" top=\"0\" fit=\"hidden\" />");
                }
                sbP.append("<text src=\"").append(mf.getText().getName())
                        .append("\" region=\"text").append(i).append("\" />");
            }
            if (mf.getAudio().getSize() > 0) {
                sbP.append("<audio src=\"").append(mf.getAudio().getName())
                        .append("\" />");
            }
            if (mf.getVideo().getSize() > 0) {
                sbP.append("<video src=\"").append(mf.getVideo().getName())
                        .append("\" />");
            }
            sbP.append("</par>");
            i++;
        }
        sb.append("<smil>");
        sb.append("<head>");
        sb.append("<meta name=\"title\" content=\"mms\" />");
        sb.append("<meta name=\"author\" content=\"xuanwu\" />");
        sb.append("<layout>");
        sb.append("<root-layout width=\"160\" height=\"280\" />");
        sb.append(sbR);
        sb.append("</layout>");
        sb.append("</head>");
        sb.append("<body>");
        sb.append(sbP);
        sb.append("</body>");
        sb.append("</smil>");
        return sb.toString();
    }

    /**
     * 将数据库中的blob数据重新渲染为xml文件
     *
     *
     * @param mc
     * @return
     * @throws Exception
     */
    public static String toTemplate(MmsContent mc) throws Exception {
        MmsTemplate mt = new MmsTemplate();
        mt.subject = mc.getSubject();
        mt.smil = mc.getSmil();
        mt.title = mc.getSubject();
        ArrayList<MmsAttachFile> mfiles = new ArrayList<MmsAttachFile>();
        mt.mmsAttachFile = mfiles;
        MmsAttachFile mfile = null;
        for (MmsContent.Frame mf : mc.getFrames()) {
            if (mf.getSize() <= 0)
                continue;
            if (mf.getText().getSize() > 0) {
                mfile = new MmsAttachFile();
                mfile.fileName = mf.getId() + ".txt";
                mfile.content = mf.getText().getContent().getBytes(WebConstants.DEFAULT_CHARSET);
                mfiles.add(mfile);
            }
            if (mf.getImg().getSize() > 0) {
                mfile = new MmsAttachFile();
                mfile.fileName = mf.getImg().getName();
                mfile.content = FileUtils.readFileToByteArray(FileUtil.getImportedFile(BizDataType.Tmp, mfile.fileName,
                                Config.getContextPath()));
                mfiles.add(mfile);
            }
            if (mf.getAudio().getSize() > 0) {
                mfile = new MmsAttachFile();
                mfile.fileName = mf.getAudio().getName();
                mfile.content = FileUtils.readFileToByteArray(FileUtil
                        .getImportedFile(BizDataType.Tmp, mfile.fileName, Config.getContextPath()));
                mfiles.add(mfile);
            }
            if (mf.getVideo().getSize() > 0) {
                mfile = new MmsAttachFile();
                mfile.fileName = mf.getVideo().getName();
                mfile.content = FileUtils.readFileToByteArray(FileUtil
                        .getImportedFile(BizDataType.Tmp, mfile.fileName, Config.getContextPath()));
                mfiles.add(mfile);
            }
        }
        mt.smil = toSmil(mc);
        return xmlMapper.writeValueAsString(mt);
    }

    @SuppressWarnings("unchecked")
    public static MmsContent fromTemplate(String src) throws Exception {
        MmsTemplate mt = xmlMapper.readValue(src, MmsTemplate.class);
        MmsContent mc = new MmsContent();
        mc.setSubject(mt.subject);
        // 兼容以前的协议
        if (StringUtils.isBlank(mt.subject)) {
            mc.setSubject(mt.title);
        }
        ArrayList<MmsContent.Frame> mfs = new ArrayList<MmsContent.Frame>();
        mc.setFrames(mfs);
        MmsContent.Frame frame = null;
        MmsContent.Text text = null;
        MmsContent.Audio audio = null;
        MmsContent.Video video = null;
        MmsContent.Image img = null;
        File file = null;
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (MmsAttachFile mfile : mt.mmsAttachFile) {
            if (mfile.content == null)
                continue;
            int type = getFileType(mfile.fileName);
            if (type == 1) {
                text = new MmsContent.Text();
                text.setName(mfile.fileName);
                text.setSize(mfile.content.length);
                text.setContent(new String(mfile.content, WebConstants.DEFAULT_CHARSET));
                map.put(mfile.fileName, text);
            } else if (type == 2) {
                img = new MmsContent.Image();
                file = FileUtil.createTmpFile(Config.getContextPath(), mfile.fileName.substring(mfile.fileName.lastIndexOf('.')));
                FileUtils.writeByteArrayToFile(file, mfile.content);
                img.setName(file.getName());
                img.setSize(mfile.content.length);
                map.put(mfile.fileName, img);
            } else if (type == 3) {
                audio = new MmsContent.Audio();
                file = FileUtil.createTmpFile(Config.getContextPath(), mfile.fileName.substring(mfile.fileName.lastIndexOf('.')));
                FileUtils.writeByteArrayToFile(file, mfile.content);
                audio.setName(file.getName());
                audio.setSize(mfile.content.length);
                map.put(mfile.fileName, audio);
            } else if(type ==4){
                video = new MmsContent.Video();
                file = FileUtil.createTmpFile(Config.getContextPath(),mfile.fileName.substring(mfile.fileName.lastIndexOf(".")));
                FileUtils.writeByteArrayToFile(file,mfile.content);
                video.setName(file.getName());
                video.setSize(mfile.content.length);
                map.put(mfile.fileName,video);
            }
        }
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read(new StringReader(mt.smil));
        Element body = doc.getRootElement().element("body");
        List<Element> pars = body.elements();
        String dur = null;
        for (Element par : pars) {
            frame = new MmsContent.Frame();
            dur = par.attributeValue("dur");
            dur = dur.substring(0, dur.length() - 2);
            frame.setDuration(Integer.parseInt(dur) / 1000);
            List<Element> subs = par.elements();
            for (Element sub : subs) {
                Object obj = map.get(sub.attributeValue("src"));
                if (obj instanceof MmsContent.Text) {
                    frame.setText((MmsContent.Text) obj);
                } else if (obj instanceof MmsContent.Image) {
                    frame.setImg((MmsContent.Image) obj);
                } else if (obj instanceof MmsContent.Video) {
                    frame.setVideo((MmsContent.Video) obj);
                } else {
                    frame.setAudio((MmsContent.Audio) obj);
                }
            }
            mfs.add(frame);
        }
        return mc;
    }

    @SuppressWarnings("unchecked")
    public static MmsContent fromFrameContent(MsgContent content)
            throws Exception {
        MmsContent mc = new MmsContent();
        ArrayList<MmsContent.Frame> mfs = new ArrayList<MmsContent.Frame>();
        mc.setFrames(mfs);
        HashMap<String, Object> map = getMediaMap(mc, content);

        SAXReader saxReader = new SAXReader();
        String smil = (String) map.get(smilName);
        Document doc = saxReader.read(new StringReader(smil));
        Element body = doc.getRootElement().element("body");
        List<Element> pars = body.elements();
        String dur = null;
        for (Element par : pars) {
            MmsContent.Frame frame = new MmsContent.Frame();
            dur = par.attributeValue("dur");
            dur = dur.substring(0, dur.length() - 2);
            frame.setDuration(Integer.parseInt(dur) / 1000);
            List<Element> subs = par.elements();
            for (Element sub : subs) {
                Object obj = map.get(sub.attributeValue("src"));
                if (obj instanceof MmsContent.Text) {
                    frame.setText((MmsContent.Text) obj);
                } else if (obj instanceof MmsContent.Image) {
                    frame.setImg((MmsContent.Image) obj);
                } else  if (obj instanceof MmsContent.Video) {
                    frame.setVideo((MmsContent.Video)obj);
                }else {
                    frame.setAudio((MmsContent.Audio) obj);
                }
            }
            mfs.add(frame);
        }
        return mc;
    }

    @SuppressWarnings("unchecked")
    public static MmsContent fromTicketContent(MsgContent msgContent,
                                               String ticketSmil) throws Exception {
        MmsContent mc = new MmsContent();
        ArrayList<MmsContent.Frame> mfs = new ArrayList<MmsContent.Frame>();
        mc.setFrames(mfs);
        HashMap<String, Object> map = getMediaMap(mc, msgContent);

        String charSet = WebConstants.DEFAULT_CHARSET;
        SAXReader saxReader = new SAXReader();
        Document doc = saxReader.read(new StringReader(ticketSmil));
        Element body = doc.getRootElement().element("body");
        List<Element> pars = body.elements();
        String dur = null;
        for (Element par : pars) {
            MmsContent.Frame frame = new MmsContent.Frame();
            dur = par.attributeValue("dur");
            dur = dur.substring(0, dur.length() - 2);
            frame.setDuration(Integer.parseInt(dur) / 1000);
            List<Element> subs = par.elements();
            for (Element sub : subs) {
                String src = sub.attributeValue("src");
                Object obj = map.get(src);
                int type = getFileType(src);
                if (type == 0 || obj == null) {
                    MmsContent.Text text = new MmsContent.Text();
                    text.setName(src);
                    text.setSize(src.getBytes(charSet).length);
                    text.setContent(src);
                    frame.setText(text);
                    continue;
                }

                if (obj instanceof MmsContent.Text) {
                    frame.setText((MmsContent.Text) obj);
                } else if (obj instanceof MmsContent.Image) {
                    frame.setImg((MmsContent.Image) obj);
                } else {
                    frame.setAudio((MmsContent.Audio) obj);
                }
            }
            mfs.add(frame);
        }
        return mc;
    }

    private static HashMap<String, Object> getMediaMap(MmsContent mc,
                                                       MsgContent msgContent) throws Exception {
        MmsContent.Text text = null;
        MmsContent.Audio audio = null;
        MmsContent.Image img = null;
        MmsContent.Video video = null;
        File file = null;

        String charSet = WebConstants.DEFAULT_CHARSET;
        HashMap<String, Object> map = new HashMap<String, Object>();
        for (MediaItem item : msgContent.getMediaItems()) {
            if (item.getMeta().endsWith(".smil")) {
                String smil = new String(item.getData(), charSet);
                map.put(smilName, smil);
                continue;
            }
            if ("subject.txt".equalsIgnoreCase(item.getMeta())) {
                mc.setSubject(new String(item.getData(), charSet));
                continue;
            }
            int type = getFileType(item.getMeta());
            if (type == 1) {
                text = new MmsContent.Text();
                text.setName(item.getMeta());
                text.setSize(item.getData().length);
                text.setContent(new String(item.getData(), charSet));
                map.put(item.getMeta(), text);
            } else if (type == 2) {
                img = new MmsContent.Image();
                file = FileUtil.createTmpFile(Config.getContextPath(), item
                        .getMeta().substring(item.getMeta().lastIndexOf('.')));
                FileUtils.writeByteArrayToFile(file, item.getData());
                img.setName(file.getName());
                img.setSize(item.getData().length);
                map.put(item.getMeta(), img);
            } else if (type == 3) {
                audio = new MmsContent.Audio();
                file = FileUtil.createTmpFile(Config.getContextPath(), item
                        .getMeta().substring(item.getMeta().lastIndexOf('.')));
                FileUtils.writeByteArrayToFile(file, item.getData());
                audio.setName(file.getName());
                audio.setSize(item.getData().length);
                map.put(item.getMeta(), audio);
            } else if (type == 4) {
                video = new MmsContent.Video();
                file = FileUtil.createTmpFile(Config.getContextPath(), item
                        .getMeta().substring(item.getMeta().lastIndexOf('.')));
                FileUtils.writeByteArrayToFile(file, item.getData());
                video.setName(file.getName());
                video.setSize(item.getData().length);
                map.put(item.getMeta(), video);
            }
        }
        return map;
    }

    /**
     * 1:text, 2:image, 3:audio , 4:video
     */
    public static int getFileType(String fn) {
        if (fn.endsWith(".txt")) {
            return 1;
        }
        if (fn.endsWith(".jpg") || fn.endsWith(".jpeg") || fn.endsWith(".gif") || fn.endsWith(".bmp")) {
            return 2;
        }
        if (fn.endsWith(".mid") || fn.endsWith(".amr") || fn.endsWith(".wma") || fn.endsWith(".wav") || fn.endsWith(".mp3")) {
            return 3;
        }
        if (fn.endsWith(".mp4") || fn.endsWith(".avi") || fn.endsWith(".rmvb") || fn.endsWith(".mkv") || fn.endsWith(".mpeg") ||fn.endsWith(".3gp")) {
            return 4;
        }
        return 0;
    }
}
