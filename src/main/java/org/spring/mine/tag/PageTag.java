package org.spring.mine.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

@SuppressWarnings("serial")
public class PageTag extends TagSupport {

	private Integer total;
	private Integer pageSize;
	private Integer curr;
	private String url;
	
 
    public int doStartTag() throws JspException {
        try {
            JspWriter out = this.pageContext.getOut();
            String active=" class='active' ";
    		String url = "";
    		Integer total=0;
    		Integer pageLen=0;
    		Integer pageSize=0;
    		Integer curr=1;
    		
    		try{
    			total=(Integer) this.total;
    			pageSize=Math.abs((Integer) this.pageSize);
    			pageLen=(int) (total%pageSize==0 ? total/pageSize : total/pageSize+1);
    			curr=Math.abs((Integer) this.curr);
    			url=this.url;
    			
    		}catch(Throwable e){}
    		
    		out.println("<div class='page_bar'>");
    		//out.println("<font>共<b>"+total+"</b>条数据，<b>"+curr+"</b>/"+pageLen+"</font>");
    		if(pageLen<=1){
    			out.println("</div>");
    			return SKIP_BODY;
    		}
    		
    		if(pageLen>10){
    			 out.println("<font>");
    			 out.println("到 <input type='text' style='text-align:center;width:28px;' size='2' value='"+curr+"' id='gotoIndxPage'> 页");
    			 out.println("<input type='button' class='btn' onclick='goPage(document.getElementById(\"gotoIndxPage\").value,"+pageSize+");' value='确定' style='width:43px;'>");
    			 out.println("</font>");
            }
    		
    		if(curr>1 && curr<=pageLen){
    			out.println("<a class='prev' href='javascript://' onclick='goPage("+(curr-1)+","+pageSize+");' url='"+url+"'><span> < 上一页</span></a>" );
    		}
    		
    		if(pageLen<=10){
    			for(int pageNum=1;pageNum<=pageLen;pageNum++){
    				out.println("<a "+(pageNum==curr ? active:"")+" href='javascript://'  onclick='goPage("+pageNum+","+pageSize+");'  url='"+url+"'><span>"+(pageNum)+"</span></a> ");
    			}
    		}else{
    			out.println("<a "+(1==curr ? active:"")+" href='javascript://' onclick='goPage(1,"+pageSize+");'  url='"+url+"' style='margin:0 8px;'><span>首页</span></a> ");
    			//向前超出4；打印前3
    			int pre_limit=4;
    			int pageNum=curr-1>0?curr-1:curr;
    			if(pageNum>pre_limit){
    				for(int i=1;i<pre_limit;i++){
    					out.println("<a "+(i==curr ? active:"")+" href='javascript://'  onclick='goPage("+i+","+pageSize+");'  url='"+url+"'><span>"+i+"</span></a> ");
    				}
    				out.println("<a "+(pre_limit ==curr ? active:"")+" href='javascript://'  onclick='goPage("+(pre_limit+pageNum)/2+","+pageSize+");'  url='"+url+"'><span>...</span></a> ");
    			}
    			
    			int i=0;
    			boolean isHide=false;
    			for(;pageNum<=pageLen;pageNum++){
    				if(i++<3 || pageNum>pageLen-3){
    					out.println("<a "+(pageNum ==curr ? active:"")+" href='javascript://' onclick='goPage("+pageNum+","+pageSize+");'  url='"+url+"'><span>"+(pageNum)+"</span></a> ");
    				}
    				else if(!isHide){
    					out.println("<a "+(pageNum ==curr ? active:"")+" href='javascript://'  onclick='goPage("+(pageNum+pageLen-3)/2+","+pageSize+");'  url='"+url+"'><span>...</span></a> ");
    					isHide=true;
    				}
    			}
    			i=0;
    			isHide=false;
    			out.println("<a "+(pageLen ==curr ? active:"")+" href='javascript://' onclick='goPage("+pageLen+","+pageSize+");'  url='"+url+"' style='margin:0 8px;'><span>尾页</span></a> ");
    		}
    		
            if(pageLen>=2 && curr<pageLen){
            	out.println("<a class='next' style='cursor:pointer;' onclick='goPage("+(curr+1)+","+pageSize+");' url='"+url+"'><span>下一页  > </span></a>");
            }
           
            out.println("</div>");
            
            
        } catch(Exception e) {
            throw new JspException(e.getMessage());
        }

        return SKIP_BODY;

    }

    public int doEndTag() throws JspException {

        return EVAL_PAGE;

    }

    public void release() {
        super.release();
    }

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getCurr() {
		return curr;
	}

	public void setCurr(Integer curr) {
		this.curr = curr;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}

 