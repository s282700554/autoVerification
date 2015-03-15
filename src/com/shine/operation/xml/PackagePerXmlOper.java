package com.shine.operation.xml;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;

import com.shine.utils.XmlUtils;

public class PackagePerXmlOper {

    private static String filePath = "config/xml/PackagePer.xml";

    /**
     * 
     * 更新节点.
     * 
     * @param elements
     * @param flag
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-4	SGJ	新建
     * </pre>
     */
    public static void updateXMLNode(List<Element> elements, String flag) throws Exception {
        for (Element element : elements) {
            element.attribute("flag").setValue(flag);
        }
    }

    /**
     * 
     * 更新到下一个验包人员.
     * 
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-4	SGJ	新建
     * </pre>
     */
    public static void updateNext() throws Exception {
        // 读取XML文件
        Document document = XmlUtils.getDocument(filePath);
        // 查询一共有几个验包人员
        int number = document.getRootElement().elements().size();
        // 查询今天验包人员的排序号
        String order = PackagePerXmlOper.selectVerifyOrder(document);
        // 推算下一个验包人员
        Integer orderInteger = Integer.valueOf(order) + 1;
        if (orderInteger > number) {
            orderInteger = 1;
        }
        // 查询当前验包人员节点,将标识更新为1(非验包)
        List<Element> elements = PackagePerXmlOper.selectElementsByFlag(document, "0");
        updateXMLNode(elements, "1");
        // 根据排序号查询下一个验包人员,将标识更新为0(验包)
        List<Element> nextElements = PackagePerXmlOper.selectElementsByOrder(document, String.valueOf(orderInteger));
        updateXMLNode(nextElements, "0");
        // 保存新文件
        XmlUtils.saveXMLFile(document, filePath);
        // 查询更新后今天的验包人员,并写入命令文件供查询.
        String per = PackagePerXmlOper.selectVerifyPersonnel();
        TalkConfigXmlOper.updateXMLNode("@今天谁验包?", per + "出来,今天该你验包了!", "74");
    }

    /**
     * 
     * 根据排序号搜索节点.
     * 
     * @param document
     * @param order
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-4	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static List<Element> selectElementsByOrder(Document document, String order) throws Exception {
        return document.selectNodes("Verify/personnel[col = '" + order + "']");
    }

    /**
     * 
     * 根据标示查找验包节点.
     * 
     * @param document
     * @param flag
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-4	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static List<Element> selectElementsByFlag(Document document, String flag) throws Exception {
        return document.selectNodes("Verify/personnel[@flag = '" + flag + "']");
    }

    /**
     * 
     * 查验包人员.
     * 
     * @param document
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-4	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static String selectVerifyPersonnel() throws Exception {
        Document document = XmlUtils.getDocument(filePath);
        List<Element> elements = document.selectNodes("Verify/personnel[@flag = '0']/col[@name = 'PER_NAME']");
        return elements.get(0).getText();
    }

    /**
     * 
     * 查询验包人员排序.
     * 
     * @param document
     * @return
     * @throws Exception
     * 
     *             <pre>
     * 修改日期		修改人	修改原因
     * 2014-6-4	SGJ	新建
     * </pre>
     */
    @SuppressWarnings("unchecked")
    public static String selectVerifyOrder(Document document) throws Exception {
        List<Element> elements = document
                .selectNodes("Verify/personnel[@flag = '0']/col[@name = 'PER_ORDER']");
        return elements.get(0).getText();
    }
}
