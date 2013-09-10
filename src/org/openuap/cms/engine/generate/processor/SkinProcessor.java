/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openuap.cms.engine.generate.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FilenameUtils;
import org.openuap.base.util.FileUtil;
import org.openuap.base.util.StringUtil;
import org.openuap.cms.config.CMSConfig;
import org.openuap.cms.engine.profile.PublishProfileInfoHolder;
import org.openuap.cms.engine.profile.impl.PublishOperationProfileImpl;
import org.openuap.cms.node.model.Node;
import org.openuap.cms.psn.manager.PsnManager;
import org.openuap.cms.psn.model.Psn;
import org.openuap.tpl.engine.TemplateContext;
import org.openuap.tpl.engine.TemplateProcessor;
import org.openuap.tpl.engine.TemplateProcessorChain;

/**
 * <p> 模板中的资源处理 </p>
 *
 * <p> $Id: SkinProcessor.java 4080 2012-05-04 16:52:23Z orangeforjava $ </p>
 *
 * @author Joseph
 * @version 1.0
 */
public class SkinProcessor implements TemplateProcessor {

    private int priority = 10;
    private PsnManager psnManager;
    public final static String EXT_POINT_NAME = "tpl-processor-skin";

    public String getName() {
        return EXT_POINT_NAME;
    }

    public void processTemplate(TemplateProcessorChain chain,
            TemplateContext context, List errors) {
        String tplContent = context.getTplContent();
        if (tplContent != null) {
            // 性能诊断-start
            PublishOperationProfileImpl op = null;
            Exception exception = null;
            if (PublishProfileInfoHolder.isEnableProfile()) {
                op = new PublishOperationProfileImpl();
                op.setOperation("SkinProcessor");
                op.setStartTime(System.currentTimeMillis());
            }
            //
            Object preview = context.getModel().get("__preview__");

            if (preview == null) {
                //
                String tplContent2 = processSkinFile(tplContent, context,
                        errors);
                //
                if (PublishProfileInfoHolder.isEnableProfile()) {
                    op.setEndTime(System.currentTimeMillis());
                    op.setException(exception);
                    PublishProfileInfoHolder.getProfile().addPublishOperation(
                            op);
                }
                context.setTplContent(tplContent2);
                //
            }
            chain.doProcess(context, errors);
        }
    }

    /**
     *
     * 处理模板中的资源
     *
     * @param tplContent
     *
     * @param node
     *
     * @param errors
     *
     * @return String
     */
    public String processSkinFile(String tplContent, TemplateContext context,
            List errors) {
        CMSConfig config = CMSConfig.getInstance();
        String imgExtensions = config.getUploadFileImageType();
        String flashExtensions = config.getUploadFileFlashType();
        String attachExtensions = config.getUploadFileAttachType();
        String sAllowExt = imgExtensions + "|" + flashExtensions + "|"
                + attachExtensions + "|js|html|htm|shtml|css|icon";
        // 1)处理资源库的引用
        List tplRsList = new ArrayList();
        // 资源匹配模式
        String sp = "\\.\\.\\/resource\\/((img|flash|attach)(\\/\\w+)+\\.("
                + sAllowExt + "))";
        String rsRootDir = config.getResourceRootPath();
        Pattern p = Pattern.compile(sp);
        Matcher m = p.matcher(tplContent);
        boolean result = m.find();
        StringBuffer sb = new StringBuffer();
        // 模板皮肤路径
        String skinRootPath = config.getTemplateSkinPath();
        String sysRootPath = config.getSysRootPath();
        //
        String destResourceUrl = "";
        String destSkinUrl = "";
        //
        Object oNode = context.getModel().get("node");
        if (oNode != null && oNode instanceof Node) {
            Node node = (Node) oNode;
            // 获得本结点的资源发布Url信息
            String rsUrl = node.getResourceUrl();
            //
            destResourceUrl = psnManager.getPsnUrlInfo(rsUrl);
            while (result) {
                //
                String path = m.group(1);
                if (!tplRsList.contains(path)) {
                    tplRsList.add(path);
                    //
                    String fullPath = rsRootDir;
                    fullPath += File.separator + path;
                    fullPath = StringUtil.normalizePath(fullPath);
                    File srcFile = new File(fullPath);
                    // 获得资源Copy的目的目录
                    File destFile = getResourceDestFile(sysRootPath, path,
                            node, errors);
                    try {
                        // decide if should copy file.
                        if (shouldCopyFile(srcFile, destFile)) {
                            FileUtil.copy(srcFile, destFile);
                        }
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                //
                String destResourceUrl2 = destResourceUrl + "/" + path;
                m.appendReplacement(sb, destResourceUrl2);
                result = m.find();
            }
            m.appendTail(sb);
            // 2)process the template skin refrence
            String skinContent = sb.toString();
            tplRsList = null;
            //			
            destSkinUrl = getDestSkinUrl(node);
            return processTemplateSkins(node, errors, sAllowExt, skinRootPath,
                    sysRootPath, destSkinUrl, skinContent);
        }
        return tplContent;
    }

    protected String getDestSkinUrl(Node node) {
        String destResourceUrl = "";
        String rsUrl = node.getResourceUrl();
        String sp = "\\{PSN-URL:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
        Pattern p = Pattern.compile(sp);
        Matcher m = p.matcher(rsUrl);
        boolean result = m.find();
        StringBuffer sb = new StringBuffer();
        while (result) {
            String path = m.group(2);
            String psnId = m.group(1);
            Psn psn = psnManager.getPsnFromCache(new Long(psnId));
            String psnUrl = psn.getUrl();
            m.appendReplacement(sb, psnUrl + path);
            result = m.find();
        } // end while result
        m.appendTail(sb);
        destResourceUrl = sb.toString();
        return destResourceUrl;
    }

    private File getResourceDestFile(String sysRootDir, String destFileName,
            Node node, List errors) {
        String fullPath = sysRootDir;
        String rsPsn = node.getResourcePsn();
        String relativePath = getRelativePath(rsPsn);
        if (!relativePath.equals("")) {
            fullPath += "/" + relativePath;
        }
        int pos = destFileName.lastIndexOf("/");
        String destPath = "";
        String destFile = "";
        if (pos > -1) {
            destPath = destFileName.substring(0, pos);
            destFile = destFileName.substring(pos + 1);
        } else {
            destFile = destFileName;
        }
        if (!destPath.equals("")) {
            fullPath += "/" + destPath;
        }
        fullPath = StringUtil.normalizePath(fullPath);
        File destDir = new File(fullPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File dest = new File(destDir, destFile);
        return dest;
    }

    private String getRelativePath(String selfPsn) throws NumberFormatException {
        String relativePath = "";
        String sp = "\\{PSN:(\\d+)\\}((\\/\\p{Print}*\\s*)*)";
        Pattern p = Pattern.compile(sp);
        Matcher m = p.matcher(selfPsn);
        boolean result = m.find();
        while (result) {
            String path = m.group(2);
            String psnId = m.group(1);
            Psn psn = psnManager.getPsnFromCache(new Long(psnId));
            // String psnUrl = psn.getPsn();
            if (psn.getType() == Psn.LOCAL_PSN_TYPE) {
                // now,only process the local
                // remote will be do later.
                relativePath = psn.getLocalPath();
                relativePath += "/" + path;
            }
            result = m.find();
        } // end while result
        return relativePath;
    }

    private boolean shouldCopyFile(File srcFile, File destFile) {
        if (srcFile.exists()) {
            if (!destFile.exists()
                    || (destFile.exists() && (destFile.lastModified() < srcFile.lastModified() || destFile.length() != srcFile.length()))) {
                return true;
            }
        }
        return false;
    }

    private String testProcessTemplateSkins(String sAllowExt, String skinRootPath, String sysRootPath,
            String destSkinUrl, String skinContent) {
        List tplSkinList = new ArrayList();
        //这里解决了一个Bug，若资源里出现.符号，将出现死循环
        String sp_skin = "\\.\\.\\/templates\\/skins(([\\/\\w\\-\\~\\.])+?\\.("
                + sAllowExt + ")+?)";
//        String sp_skin = "\\.\\.\\/templates\\/skins(([\\/\\w\\-\\~\\.]+)+\\.("
//                + sAllowExt + "))";
        Pattern p_skin = Pattern.compile(sp_skin);
        Matcher m_skin = p_skin.matcher(skinContent);
        boolean result_skin = m_skin.find();
        StringBuffer sb_skin = new StringBuffer();
        String css_url_patter = "";
        long s = System.currentTimeMillis();
        while (result_skin) {
            String full = m_skin.group(0);
            System.out.println("full="+full);
            String path = m_skin.group(1);
            if (!tplSkinList.contains(path)) {
                tplSkinList.add(path);
                String fullPath = skinRootPath;
                fullPath += File.separator + path;
                fullPath = StringUtil.normalizePath(fullPath);
                File srcFile = new File(fullPath);
                //File destFile = getSkinDestFile(sysRootPath, path, node, errors);
                try {

                    //if (shouldCopyFile(srcFile, destFile)) {
                    //    FileUtil.copy(srcFile, destFile);

                    //}
                    if (srcFile.exists()) {
                        // 处理css文件,css文件里可能会引用图片资源
                        // 目前只处理相对路径的引用，不处理绝对路径
                        String extension = FilenameUtils.getExtension(srcFile.getAbsolutePath());
                        //if ("css".equalsIgnoreCase(extension)) {
                        //    processCssImages(srcFile, destFile);
                        //}
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
            //
            String mydestSkinUrl = destSkinUrl;
            mydestSkinUrl += "/skins" + path;
            m_skin.appendReplacement(sb_skin, mydestSkinUrl);
            result_skin = m_skin.find();
        }
        m_skin.appendTail(sb_skin);
        tplSkinList = null;
        // System.out.println(sb_skin.toString());
        return sb_skin.toString();
    }

    /**
     * 处理模板中使用的资源
     *
     * @param node
     * @param errors
     * @param sAllowExt
     * @param skinRootPath
     * @param sysRootPath
     * @param destSkinUrl
     * @param skinContent
     * @return
     */
    protected String processTemplateSkins(Node node, List errors,
            String sAllowExt, String skinRootPath, String sysRootPath,
            String destSkinUrl, String skinContent) {
        //
        List tplSkinList = new ArrayList();
        //这里解决了一个Bug，若资源里出现.符号，将出现死循环
//        String sp_skin = "\\.\\.\\/templates\\/skins(([\\/\\w\\-\\~\\.]+)+\\.("
//                + sAllowExt + "))";
        String sp_skin = "\\.\\.\\/templates\\/skins(([\\/\\w\\-\\~\\.])+?\\.("
                + sAllowExt + ")+?)";
        Pattern p_skin = Pattern.compile(sp_skin);
        Matcher m_skin = p_skin.matcher(skinContent);
        boolean result_skin = m_skin.find();
        StringBuffer sb_skin = new StringBuffer();
        String css_url_patter = "";
        long s = System.currentTimeMillis();
        while (result_skin) {
            // String full = m_skin.group(0);
            String path = m_skin.group(1);
            if (!tplSkinList.contains(path)) {
                tplSkinList.add(path);
                String fullPath = skinRootPath;
                fullPath += File.separator + path;
                fullPath = StringUtil.normalizePath(fullPath);
                File srcFile = new File(fullPath);
                File destFile = getSkinDestFile(sysRootPath, path, node, errors);
                try {

                    if (shouldCopyFile(srcFile, destFile)) {
                        FileUtil.copy(srcFile, destFile);

                    }
                    if (srcFile.exists()) {
                        // 处理css文件,css文件里可能会引用图片资源
                        // 目前只处理相对路径的引用，不处理绝对路径
                        String extension = FilenameUtils.getExtension(srcFile.getAbsolutePath());
                        if ("css".equalsIgnoreCase(extension)) {
                            processCssImages(srcFile, destFile);
                        }
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

            }
            //
            String mydestSkinUrl = destSkinUrl;
            mydestSkinUrl += "/skins" + path;
            m_skin.appendReplacement(sb_skin, mydestSkinUrl);
            result_skin = m_skin.find();
        }
        m_skin.appendTail(sb_skin);
        tplSkinList = null;
        // System.out.println(sb_skin.toString());
        return sb_skin.toString();
    }

    protected File getSkinDestFile(String rootDir, String destFileName,
            Node node, List errors) {
        String fullPath = rootDir;
        // Node node = nodeManager.getNodeById(nodeId);
        String rsPsn = node.getResourcePsn();
        String relativePath = getRelativePath(rsPsn);
        if (!relativePath.equals("")) {
            fullPath += "/" + relativePath;
        }
        fullPath += "/" + "skins";
        int pos = destFileName.lastIndexOf("/");
        String destPath = "";
        String destFile = "";
        if (pos > -1) {
            destPath = destFileName.substring(0, pos);
            destFile = destFileName.substring(pos + 1);
        } else {
            destFile = destFileName;
        }
        if (!destPath.equals("")) {
            fullPath += "/" + destPath;
        }
        fullPath = StringUtil.normalizePath(fullPath);
        File destDir = new File(fullPath);
        if (!destDir.exists()) {
            destDir.mkdirs();
        }
        File dest = new File(destDir, destFile);
        return dest;
    }

    public void setPsnManager(PsnManager psnManager) {
        this.psnManager = psnManager;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    protected void processCssImages(File srcFile, File destFile) {
        try {
            String cssContent = FileUtil.readTextFile(srcFile, "UTF-8");
            String css_url_patter = "url\\((['\"]*)?([^\\)]*?)(['\"]*)?\\)";
            Pattern p_css_url = Pattern.compile(css_url_patter);
            Matcher m_skin = p_css_url.matcher(cssContent);
            boolean result_skin = m_skin.find();
            String srcPath = srcFile.getAbsolutePath();
            String srcBaseDir = FilenameUtils.getFullPath(srcPath);
            String destPath = destFile.getAbsolutePath();
            String destBaseDir = FilenameUtils.getFullPath(destPath);
            while (result_skin) {
                //
                String path = m_skin.group(2);
                String srcFullPath = StringUtil.normalizePath(srcBaseDir + path);
                // System.out.println(srcFullPath);
                String destFullPath = StringUtil.normalizePath(destBaseDir
                        + path);
                File imgSrcFile = new File(srcFullPath);
                File imgDestFile = new File(destFullPath);
                try {
                    if (shouldCopyFile(imgSrcFile, imgDestFile)) {
                        FileUtil.copy(imgSrcFile, imgDestFile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //
                result_skin = m_skin.find();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String skinContent = ".cgky5 li {float:left; margin-left:5px; width:200px;	text-indent:10px; text-indent /**/:15px; line-height:23px; background-image: url(../images/ind_r19_c23.jpg); background-repeat: no-repeat; background-position: 0px center; background-position /**/: 5px center;}"
                + ".cgky6{float:left; height:120px; width:228px;}"
                + ".cgky6 ul {clear:both; list-style:none;}.cgky6 li {float:left; margin-left:5px; width:200px;	text-indent:10px; text-indent /**/:15px; line-height:24px; background-image: url(../images/ind_r19_c23.jpg); background-repeat: no-repeat; background-position: 0px center; background-position /**/: 5px center;}"
                + ".tonglan-right-box2{float:left; width:237px; height:183px; margin:4px auto auto 0px; margin-left /**/:4px;}"
                + ".tonglan-right-box2-centents{float:left; width:234px; height:153px;  border:#e4b951 solid 1px}.zhpxzx42{float:left; width:237px; height:182px; margin:0px auto auto 0px; margin-left /**/:4px; background-image:url(\"../images/school_r5_c2.jpg\"); background-repeat:no-repeat; background-position:left top; background-attachment:scroll;}.zhpxzx42 h1{float:left; width:237px; height:30px; line-height:30px; margin-top:10px; font-size:13px; text-align:center; color:#FF9501; font-weight:bold;}";
        String css_url_patter = "url\\((['\"]*)?([^\\)]*?)(['\"]*)?\\)";
        Pattern p_css_url = Pattern.compile(css_url_patter);
        Matcher m_skin = p_css_url.matcher(skinContent);
        boolean result_skin = m_skin.find();
        File srcFile = new File("d:/home/dir/css/skin.css");
        File parentFile = srcFile.getParentFile();
        System.out.println(parentFile.getAbsolutePath());
        String extension = FilenameUtils.getExtension(srcFile.getAbsolutePath());
        System.out.println(extension);
        String path1 = srcFile.getAbsolutePath();
        System.out.println(path1);
        String baseDir = org.apache.commons.io.FilenameUtils.getFullPath(path1);
        System.out.println("baseDir=" + baseDir);
        while (result_skin) {
            // String full = m_skin.group(0);
            String path = m_skin.group(2);
            String fullPath = StringUtil.normalizePath(baseDir + path);
            System.out.println(fullPath);
            result_skin = m_skin.find();
        }
        //
        String testStr = "<li><a target=\"_blank\" href=\"http://www.cihucn.com/page-muqinjie.html\"><img alt=\"\" width=\"744\" height=\"108\" src=\"../templates/skins/ccun-main/images/744c108.JPG\" /></a></li>";
        SkinProcessor p = new SkinProcessor();
        p.testProcessTemplateSkins("jpg|bmp|gif", "", "", "http://test.mycms.org/skins", testStr);
        System.out.println("OK.");
    }
}
