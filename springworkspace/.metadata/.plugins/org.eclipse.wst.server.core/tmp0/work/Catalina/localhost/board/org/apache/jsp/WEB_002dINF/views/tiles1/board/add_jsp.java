/*
 * Generated by the Jasper component of Apache Tomcat
 * Version: Apache Tomcat/8.5.55
 * Generated at: 2020-07-15 02:23:27 UTC
 * Note: The last modified time of this file was set to
 *       the last modified time of the source file after
 *       generation to assist with modification tracking.
 */
package org.apache.jsp.WEB_002dINF.views.tiles1.board;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class add_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent,
                 org.apache.jasper.runtime.JspSourceImports {

  private static final javax.servlet.jsp.JspFactory _jspxFactory =
          javax.servlet.jsp.JspFactory.getDefaultFactory();

  private static java.util.Map<java.lang.String,java.lang.Long> _jspx_dependants;

  private static final java.util.Set<java.lang.String> _jspx_imports_packages;

  private static final java.util.Set<java.lang.String> _jspx_imports_classes;

  static {
    _jspx_imports_packages = new java.util.HashSet<>();
    _jspx_imports_packages.add("javax.servlet");
    _jspx_imports_packages.add("javax.servlet.http");
    _jspx_imports_packages.add("javax.servlet.jsp");
    _jspx_imports_classes = null;
  }

  private volatile javax.el.ExpressionFactory _el_expressionfactory;
  private volatile org.apache.tomcat.InstanceManager _jsp_instancemanager;

  public java.util.Map<java.lang.String,java.lang.Long> getDependants() {
    return _jspx_dependants;
  }

  public java.util.Set<java.lang.String> getPackageImports() {
    return _jspx_imports_packages;
  }

  public java.util.Set<java.lang.String> getClassImports() {
    return _jspx_imports_classes;
  }

  public javax.el.ExpressionFactory _jsp_getExpressionFactory() {
    if (_el_expressionfactory == null) {
      synchronized (this) {
        if (_el_expressionfactory == null) {
          _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
        }
      }
    }
    return _el_expressionfactory;
  }

  public org.apache.tomcat.InstanceManager _jsp_getInstanceManager() {
    if (_jsp_instancemanager == null) {
      synchronized (this) {
        if (_jsp_instancemanager == null) {
          _jsp_instancemanager = org.apache.jasper.runtime.InstanceManagerFactory.getInstanceManager(getServletConfig());
        }
      }
    }
    return _jsp_instancemanager;
  }

  public void _jspInit() {
  }

  public void _jspDestroy() {
  }

  public void _jspService(final javax.servlet.http.HttpServletRequest request, final javax.servlet.http.HttpServletResponse response)
      throws java.io.IOException, javax.servlet.ServletException {

    final java.lang.String _jspx_method = request.getMethod();
    if (!"GET".equals(_jspx_method) && !"POST".equals(_jspx_method) && !"HEAD".equals(_jspx_method) && !javax.servlet.DispatcherType.ERROR.equals(request.getDispatcherType())) {
      response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "JSP들은 오직 GET, POST 또는 HEAD 메소드만을 허용합니다. Jasper는 OPTIONS 메소드 또한 허용합니다.");
      return;
    }

    final javax.servlet.jsp.PageContext pageContext;
    javax.servlet.http.HttpSession session = null;
    final javax.servlet.ServletContext application;
    final javax.servlet.ServletConfig config;
    javax.servlet.jsp.JspWriter out = null;
    final java.lang.Object page = this;
    javax.servlet.jsp.JspWriter _jspx_out = null;
    javax.servlet.jsp.PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html; charset=UTF-8");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("\r\n");
      out.write("    \r\n");
 String ctxPath = request.getContextPath(); 
      out.write("\r\n");
      out.write("    \r\n");
      out.write("    \r\n");
      out.write("<style type=\"text/css\">\r\n");
      out.write("\r\n");
      out.write("table, th, td, input, textarea {border: solid gray 1px;}\r\n");
      out.write("\t\r\n");
      out.write("\t#table {border-collapse: collapse;\r\n");
      out.write("\t \t\twidth: 900px;\r\n");
      out.write("\t \t\t}\r\n");
      out.write("\t#table th, #table td{padding: 5px;}\r\n");
      out.write("\t#table th{width: 120px; background-color: #DDDDDD;}\r\n");
      out.write("\t#table td{width: 860px;}\r\n");
      out.write("\t.long {width: 470px;}\r\n");
      out.write("\t.short {width: 120px;}\r\n");
      out.write("\r\n");
      out.write("</style>\r\n");
      out.write("\r\n");
      out.write("<script type=\"text/javascript\">\r\n");
      out.write("\r\n");
      out.write("\t$(document).ready(function(){\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t// 쓰기 버튼 클릭시\r\n");
      out.write("\t\t$(\"#btnWrite\").click(function(){\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t// 글 제목 유효성 검사\r\n");
      out.write("\t\t\tvar subjectVal = $(\"#subject\").val().trim();\r\n");
      out.write("\t\t\tif(subjectVal == \"\") {\r\n");
      out.write("\t\t\t\talert(\"글 제목을 입력하세요\");\r\n");
      out.write("\t\t\t\treturn;\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\t// 글내용 유효성 검사\r\n");
      out.write("\t\t\tvar contentVal = $(\"#content\").val().trim();\r\n");
      out.write("\t\t\tif(contentVal == \"\") {\r\n");
      out.write("\t\t\t\talert(\"글 내용을 입력하세요\");\r\n");
      out.write("\t\t\t\treturn;\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t\t// 글 암호 유효성 검사\r\n");
      out.write("\t\t\tvar pwVal = $(\"#pw\").val().trim();\r\n");
      out.write("\t\t\tif(pwVal == \"\") {\r\n");
      out.write("\t\t\t\talert(\"글 암호를 입력하세요\");\r\n");
      out.write("\t\t\t\treturn;\r\n");
      out.write("\t\t\t}\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t\t// 폼(form) 전송(submit)하기\r\n");
      out.write("\t\t\tvar frm = document.addFrm;\r\n");
      out.write("\t\t\tfrm.method = \"POST\";\r\n");
      out.write("\t\t\tfrm.action = \"");
      out.print( ctxPath);
      out.write("/addEnd.action\";\r\n");
      out.write("\t\t\tfrm.submit();\r\n");
      out.write("\t\t\t\r\n");
      out.write("\t\t});\r\n");
      out.write("\t\t\r\n");
      out.write("\t}); // end of $(document).ready(function()) ---------------------\r\n");
      out.write("\t\r\n");
      out.write("\t\r\n");
      out.write("\tfunction goPrint(title) {\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar sw = screen.width; // 화면의 가로길이\r\n");
      out.write("\t\tvar sh = screen.height; // 화면의 세로길이\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar popw = 800; // 팝업창 가로길이\r\n");
      out.write("\t\tvar poph = 600; // 팝업창 세로길이\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar xpos = (sw-popw)/2; // 화면 중앙에 팝업창 띄우기\r\n");
      out.write("\t\tvar ypos = (sh-popw)/2; // 화면 중앙에 팝업창 띄우기\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tvar popWin = window.open(\"\",\"print\", \"width=\"+popw+\", height=\"+poph+\", top=\"+ypos+\", left=\"+xpos+\", status=yes, scrollbars=yes\"); \r\n");
      out.write("\t\t// 윈도우 팝업창 띄우기\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tpopWin.document.open(); // 윈도우 팝업창에 내용물을 넣을 수 있도록 열어주기\r\n");
      out.write("\t//\tpopWin.document.write(popContent); // 윈도우 팝업창에 내용물 넣기\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t// 윈도우 팝업창에 내용 입력하기\r\n");
      out.write("\t\tpopWin.document.write(\"<html><head><style type='text/css'>*{color:purple;}</style><body onload='window.print()'>\");\r\n");
      out.write("\t\tpopWin.document.write(document.getElementById(\"subject\").value);\r\n");
      out.write("\t\tpopWin.document.write(\"<br/><pre>굿모닝\")\r\n");
      out.write("\t\tpopWin.document.write(\"</pre></body></html>\")\r\n");
      out.write("\t\r\n");
      out.write("\t\tpopWin.document.close(); // 윈도우 팝업창 문서 닫기\r\n");
      out.write("\t\t\r\n");
      out.write("\t\tpopWin.print(); // 윈도우 팝업창에 대한 인쇄창을 띄우고 인쇄를 하던가 \r\n");
      out.write("\t\tpopWin.close(); // 취소를 누르면 윈도우 팝업창을 닫음\r\n");
      out.write("\t\t\r\n");
      out.write("\t} // end of function goPrint(title) ------------------\r\n");
      out.write("\r\n");
      out.write("</script>\r\n");
      out.write("\r\n");
      out.write("<div style=\"padding-left: 10px;\">\r\n");
      out.write("\t<h1>글쓰기</h1>\r\n");
      out.write("\t\r\n");
      out.write("\t<!-- form 태그에 있는 name 값 + DB 테이블에 있는 컬럼값 + VO에 있는 값이 똑같아야함 !! 3박자가 맞아야 문제없이 잘 돌아간다 ~ -->\r\n");
      out.write("\t<form name=\"addFrm\">\r\n");
      out.write("\t\t<table id=\"table\">\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<th>성명</th>\r\n");
      out.write("\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t<input type=\"hidden\" name=\"fk_userid\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sessionScope.loginuser.userid}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("\" />\r\n");
      out.write("\t\t\t\t\t<input type=\"text\" name=\"name\" value=\"");
      out.write((java.lang.String) org.apache.jasper.runtime.PageContextImpl.proprietaryEvaluate("${sessionScope.loginuser.name}", java.lang.String.class, (javax.servlet.jsp.PageContext)_jspx_page_context, null));
      out.write("\" class=\"short\" readonly/>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<th>제목</th>\r\n");
      out.write("\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t<input type=\"text\" name=\"subject\" id=\"subject\" class=\"long\" />\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<th>내용</th>\r\n");
      out.write("\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t<textarea rows=\"10\" cols=\"100\" style=\"width: 95%; height: 412px;\" name=\"content\" id=\"content\" ></textarea>\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t\t<tr>\r\n");
      out.write("\t\t\t\t<th>글암호</th>\r\n");
      out.write("\t\t\t\t<td>\r\n");
      out.write("\t\t\t\t\t<input type=\"password\" name=\"pw\" id=\"pw\" class=\"short\" />\r\n");
      out.write("\t\t\t\t</td>\r\n");
      out.write("\t\t\t</tr>\r\n");
      out.write("\t\t</table>\r\n");
      out.write("\t\t\r\n");
      out.write("\t\t<div style=\"margin: 20px;\">\r\n");
      out.write("\t\t\t<button type=\"button\" id=\"btnWrite\">쓰기</button>\r\n");
      out.write("\t\t\t<button type=\"button\" onclick=\"javascript:history.back()\">취소</button>\r\n");
      out.write("\t\t\t<button type=\"button\" onclick=\"goPrint()\">인쇄</button>\r\n");
      out.write("\t\t</div>\r\n");
      out.write("\t</form>\r\n");
      out.write("</div>");
    } catch (java.lang.Throwable t) {
      if (!(t instanceof javax.servlet.jsp.SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try {
            if (response.isCommitted()) {
              out.flush();
            } else {
              out.clearBuffer();
            }
          } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else throw new ServletException(t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}