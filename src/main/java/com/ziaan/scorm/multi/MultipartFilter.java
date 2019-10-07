// Decompiled by Jad v1.5.7d. Copyright 2000 Pavel Kouznetsov.
// Jad home page: http:// www.geocities.com/SiliconValley/Bridge/8617/jad.html
// Decompiler options: packimports(3) 
// Source File Name:   MultipartFilter.java

package com.ziaan.scorm.multi;

import java.io.File;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

// Referenced classes of package com.ziaan.scorm.multi:
//            MultipartWrapper

public class MultipartFilter
    implements Filter
{ 
	public FilterConfig getFilterConfig() { 
		return config;
	}
	
	public void setFilterConfig(FilterConfig arg0 ) { 
	}	
	
    public MultipartFilter()
    { 
        config = null;
        dir = null;
    }

    public void init(FilterConfig filterconfig)
        throws ServletException
    { 
        config = filterconfig;
        dir = filterconfig.getInitParameter("uploadDir");
        if ( dir == null )
        { 
            File file = (File)filterconfig.getServletContext().getAttribute("javax.servlet.context.tempdir");
            if ( file != null )
                dir = file.toString();
            else
                throw new ServletException("MultipartFilter: No upload directory found: set an uploadDir init parameter or ensure the javax.servlet.context.tempdir directory is valid");
        }
    }

    public void destroy()
    { 
        config = null;
    }

    public void doFilter(ServletRequest servletrequest, ServletResponse servletresponse, FilterChain filterchain)
        throws IOException, ServletException
    { 
        HttpServletRequest httpservletrequest = (HttpServletRequest)servletrequest;
        String s = httpservletrequest.getHeader("Content-Type");
        if ( s == null || !s.startsWith("multipart/form-data"))
        { 
            filterchain.doFilter(servletrequest, servletresponse);
        } else
        { 
            MultipartWrapper multipartwrapper = new MultipartWrapper(httpservletrequest, dir);
            filterchain.doFilter(multipartwrapper, servletresponse);
        }
    }

    private FilterConfig config;
    private String dir;
}
