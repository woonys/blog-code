package com.example.cleancode.cleancodestudy.Ch3;

public class HtmlUtil {
    /**
     * setUp 페이지와 tearDown 페이지를 테스트 페이지에 넣은 후 해당 페이지를 HTML로 렌더링하는 메소드
     * @param pageData
     * @param isSuite
     * @return
     * @throws Exception
     */
    public static String renderPageWithSetupsAndTeardowns(PageData pageData, boolean isSuite) throws Exception {
        boolean isTestPage = pageData.hasAttribute("Test");
        if (isTestPage) {
            WikiPage testPage = pageData.getWikiPage();
            StringBuffer newPageContent = new StringBuffer();
            includeSetupPages(testPage, newPaageContent, isSuite);
            newPageContent.append(pageData.getContent());
            includeTearDownPages(testPage, newPageContent, isSuite);
            pageData.setContent(newPageContent.toString());
        }
        return pageData.getHtml();
    }

    public static String renderPageWithSetupsAndTearDown_refactoring(PageData pageData, boolean isSuite) throws Exception {
        if (isTestPage(pageData)) {
            includeSetupAndTearDownPages(pageData, isSuite);
        }
        return pageData.getHtml();
    }

    private static boolean isTestPage(pageData pageData) {
        return pageData.hasAttribute("Test");
    }

    private static void includeSetupAndTearDownPages(pageData, isSuite) {
        WikiPage testPage = pageData.getWikiPage();
        StringBuffer newPageContent = new StringBuffer();
        includeSetupPages(testPage, newPaageContent, isSuite);
        newPageContent.append(pageData.getContent());
        includeTearDownPages(testPage, newPageContent, isSuite);
        pageData.setContent(newPageContent.toString());
    }
}
