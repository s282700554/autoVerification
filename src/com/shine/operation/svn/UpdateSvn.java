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

    // ��־.
    private static Logger logger = LoggerFactory.getLogger(InvokeBat.class);

    /**
     * ������߸���svn�ļ�.
     * 
     * @param svnPath
     * @param updatePath
     * @throws Exception
     * 
     *             <pre>
     * �޸�����		�޸���	�޸�ԭ��
     * 2014-11-6	SGJ	�½�
     * </pre>
     */
    public static void updateOrCheckoutSvn(String operation, Map<String, String> config) throws Exception {
        // ����
        String endCode = operation.substring(3);
        // svn�ļ�·��
        String svnFilePath = StringUtils.isNotBlank(endCode)?config.get("SVN_PATH" + endCode):config.get("SVN_PATH");
        // ����·��
        String localPath = StringUtils.isNotBlank(endCode)?config.get("LOCAL_PATH" + endCode):config.get("LOCAL_PATH");
        // ��½svn
        SVNClientManager clientManager = null;
        // svn��ַ
        String svnAddress = null;
        // ������������е�svn������Ϣ��Ϊ�գ���ȡ�����е�����
        if (StringUtils.isNotBlank(config.get("SVN_ADDRESS"))) {
            svnAddress = config.get("SVN_ADDRESS");
            clientManager = SVNUtil.authSvn(svnAddress, config.get("SVN_USERNAME"), config.get("SVN_PASSWORD"));
        }
        // ���Ϊ����ȡ��������
        else {
            // ȡ��svn������Ϣ
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
