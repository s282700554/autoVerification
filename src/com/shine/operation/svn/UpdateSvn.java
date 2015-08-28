package com.shine.operation.svn;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;

import com.shine.operation.bat.InvokeBat;
import com.shine.operation.xml.BasicsConfigXmlOper;
import com.shine.utils.SVNUtil;
import com.shine.utils.StringUtils;

public class UpdateSvn {

    // 日志.
    private static Logger logger = LoggerFactory.getLogger(InvokeBat.class);

    /**
     * 检出或者更新svn文件.
     * 
     * @param svnPath
     * @param updatePath
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-11-6	SGJ	新建
     * </pre>
     */
    public static void updateOrCheckoutSvn(String operation, Map<String, String> config) throws Exception {
        // 操作
        String endCode = operation.substring(3);
        // svn文件路径
        String svnFilePath = StringUtils.isNotBlank(endCode)?config.get("SVN_PATH" + endCode):config.get("SVN_PATH");
        // 本地路径
        String localPath = StringUtils.isNotBlank(endCode)?config.get("LOCAL_PATH" + endCode):config.get("LOCAL_PATH");
        // 登陆svn
        SVNClientManager clientManager = null;
        // svn地址
        String svnAddress = null;
        // 如果任务配置中的svn配置信息不为空，则取任务中的配置
        if (StringUtils.isNotBlank(config.get("SVN_ADDRESS"))) {
            svnAddress = config.get("SVN_ADDRESS");
            clientManager = SVNUtil.authSvn(svnAddress, config.get("SVN_USERNAME"), config.get("SVN_PASSWORD"));
        }
        // 如果为空则取基础配置
        else {
            // 取得svn配置信息
            Map<String, String> svnConfig = BasicsConfigXmlOper.getBasicConfigInfo("svn");
            svnAddress = svnConfig.get("SVN_ADDRESS");
            clientManager = SVNUtil.authSvn(svnAddress, svnConfig.get("SVN_USERNAME"), StringUtils.isNotBlank(svnConfig.get("SVN_PASSWORD"))?svnConfig.get("SVN_PASSWORD"):"0457037612s");
        }
        String svnPath = svnFilePath.replace("\\", "/");
        SVNURL projectURL = null;
        try {
            projectURL = SVNURL.parseURIEncoded(svnAddress).appendPath(svnPath, false);
        } catch (SVNException e) {
            logger.error(e.getMessage());
        }
        File wc = new File(localPath);
        if (!SVNUtil.isWorkingCopy(wc)) {
            SVNUtil.checkout(clientManager, projectURL, SVNRevision.HEAD, wc, SVNDepth.INFINITY);
        } else {
            SVNUtil.update(clientManager, wc, SVNRevision.HEAD, SVNDepth.INFINITY);
        }
        logger.warn("update end!");
    }
}
